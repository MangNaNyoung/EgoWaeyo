
package egovframework.com.egowaeyo.article.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.util.EgovUserDetailsHelper;
import egovframework.com.egowaeyo.article.VO.BoardVO;
import egovframework.com.egowaeyo.article.service.ArticleService;
import egovframework.com.egowaeyo.bbsMaster.service.BbsMasterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@RequestMapping("/article")
@Controller
public class ArticleController {

	private final ArticleService articleservice;
	private final BbsMasterService bbsMasterService;

	// 게시글 목록화면으로 이동
	@GetMapping("/articleList.do")
	public String listPage(Model model, BoardVO vo) {
		// selectBbsAll 호출
		List<BoardVO> list = articleservice.selectBbsAll(vo);

		if (list == null || list.isEmpty()) {
			log.error("No data found for bbsId: {}", vo.getBbsId());
			model.addAttribute("list", List.of()); // 빈 리스트 전달
		} else {
			model.addAttribute("list", list); // 조회된 리스트 전달
		}

		return "article/ArticleList.html";
	}

	// 게시글 목록 조회 API
	@GetMapping("/selectBbsAll")
	@ResponseBody
	public List<BoardVO> selectBbsAll(@RequestParam(value = "bbsId", required = false) String bbsId) {
		if (bbsId == null || bbsId.isEmpty()) {
			throw new IllegalArgumentException("bbsId is required");
		}
		BoardVO vo = new BoardVO();
		vo.setBbsId(bbsId);
		return articleservice.selectBbsAll(vo);
	}

	// 게시글 작성화면으로 이동
	@GetMapping("/articleRegister.do")
	public String home(Locale locale, Model model) {
		return "/article/ArticleRegist.html";
	}

	// 게시글 등록
	@PostMapping("/articleRegister.do") // .do->인코딩필터(한글)
	@ResponseBody
	public String insertArticle(HttpServletRequest request, @RequestParam("bbsId") String bbsId,
			@RequestParam("nttSj") String nttSj, @RequestParam("nttCn") String nttCn) {
		try {
			// 새로운 nttId 생성
			Long nttId = articleservice.selectMaxNttId();

			// 현재 로그인된 사용자 정보 가져오기
			LoginVO user = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
			String ntcrId = user.getId();
			String ntcrNm = user.getName();

			if (ntcrId == null || ntcrNm == null) {
				throw new IllegalArgumentException("로그인 정보가 없습니다.");
			}

			// BoardVO 객체 생성 및 값 설정
			BoardVO boardVO = new BoardVO();
			boardVO.setNttId(nttId);
			boardVO.setBbsId(bbsId);
			boardVO.setNttSj(nttSj);
			boardVO.setNttCn(nttCn);
			boardVO.setFrstRegisterId(ntcrId);
			boardVO.setNtcrId(ntcrId);
			boardVO.setNtcrNm(ntcrNm);

			// 게시글 삽입
			int result = articleservice.articleInsert(boardVO);
			if (result > 0) {
				return "게시글이 성공적으로 등록되었습니다.";
			} else {
				throw new RuntimeException("게시글 등록에 실패했습니다.");
			}
		} catch (Exception e) {
			log.error("게시글 등록 중 오류 발생:", e);
			return "게시글 등록 중 오류가 발생했습니다.";
		}
	}

	// 게시글 상세조회 화면으로 이동
	@GetMapping("/articleDetail.do")
	public String articleDetail(@RequestParam("bbsId") String bbsId, @RequestParam("nttId") String nttId, Model model) {
		BoardVO vo = new BoardVO();
		vo.setBbsId(bbsId);
		vo.setNttId(Long.parseLong(nttId));

		BoardVO articleDetail = articleservice.selectArticleDetail(vo);
		model.addAttribute("articleDetail", articleDetail);

		return "article/ArticleDetail.html";
	}

	// 게시글 상세조회 API
	@GetMapping("/selectArticleDetail")
	@ResponseBody
	public BoardVO selectArticleDetail(@RequestParam("bbsId") String bbsId, @RequestParam("nttId") String nttId) {
		BoardVO vo = new BoardVO();
		vo.setBbsId(bbsId);
		vo.setNttId(Long.parseLong(nttId));
		return articleservice.selectArticleDetail(vo);
	}

	// 게시글 수정 화면으로 이동
	@GetMapping("/articleUpdate.do")
	public String articleUpdate(@RequestParam("bbsId") String bbsId, @RequestParam("nttId") String nttId, Model model) {
		try {
			BoardVO boardVO = new BoardVO();
			boardVO.setBbsId(bbsId);
			boardVO.setNttId(Integer.parseInt(nttId));

			// 게시글 상세 정보 조회
			BoardVO articleDetail = articleservice.selectArticleDetail(boardVO);
			model.addAttribute("article", articleDetail);

			return "article/ArticleEdit.html"; // Thymeleaf 뷰 이름
		} catch (Exception e) {
			log.error("게시글 수정 화면 이동 중 오류 발생: bbsId={}, nttId={}", bbsId, nttId, e);
			return "error";
		}
	}

	// 게시글 수정
	@PostMapping("/articleUpdate.do")
	@ResponseBody
	public BoardVO articleUpdate(BoardVO vo, HttpServletRequest request,
			@RequestParam(value = "files", required = false) MultipartFile[] files, RedirectAttributes rttr) {
		log.debug("Received request for article update: {}", vo);
		String cps = request.getContextPath();

		// 전달된 값 디버깅
		log.debug("Received BoardVO: {}", vo);

		if (vo.getBbsId() == null || vo.getBbsId().isEmpty()) {
			throw new IllegalArgumentException("게시판 ID가 누락되었습니다.");
			// rttr.addFlashAttribute("errorMessage", "게시판 ID가 누락되었습니다.");
		}
		if (vo.getNttId() == 0) {
			throw new IllegalArgumentException("게시글 ID가 누락되었습니다.");
		}

		// 수정 전 게시글 조회
		BoardVO originalArticle = articleservice.selectArticleDetail(vo);
		if (originalArticle == null) {
			log.error("게시글 정보를 찾을 수 없습니다. bbsId={}, nttId={}", vo.getBbsId(), vo.getNttId());
			throw new IllegalArgumentException("게시글 정보를 찾을 수 없습니다.");
		}

		// 파일 업로드 처리
		String uploadDir = System.getProperty("upload.dir", "/default/path/to/upload/");
		if (files != null) {
			for (MultipartFile file : files) {
				if (!file.isEmpty()) {
					String filePath = uploadDir + File.separator + file.getOriginalFilename();
					try {
						file.transferTo(new File(filePath));
						log.info("파일 저장 완료: {}", filePath);
						vo.setAtchFileId(UUID.randomUUID().toString()); // 파일 ID 생성
					} catch (IOException e) {
						log.error("파일 저장 중 오류 발생: {}", filePath, e);
						throw new RuntimeException("파일 저장 중 오류가 발생했습니다.");
					}
				}
			}
		}

		// 게시글 수정 처리
		log.debug("Before update: bbsId={}, nttId={}", vo.getBbsId(), vo.getNttId());
		int result = articleservice.updateArticle(vo);
		if (result == 0) {
			log.error("게시글 업데이트 실패: bbsId={}, nttId={}", vo.getBbsId(), vo.getNttId());
			throw new RuntimeException("게시글 업데이트에 실패했습니다.");
		}
		return vo;
	}

}
