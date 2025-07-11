
package egovframework.com.egowaeyo.article.controller;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.com.cmm.util.EgovUserDetailsHelper;
import egovframework.com.egowaeyo.article.VO.BoardVO;
import egovframework.com.egowaeyo.article.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@RequestMapping("/article")
@Controller
public class ArticleController {

	private final ArticleService articleservice;
	private static final String FILE_STORE_PATH = EgovProperties.getProperty("Globals.fileStorePath");

	// 게시글 목록화면으로 이동
	@GetMapping("/articleList.do")
	public String listPage(Model model, BoardVO vo) {
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

	// 게시글 조회 필터
	@GetMapping("/bbsFilter")
	@ResponseBody
	public List<Map<String, String>> getBbsFilter() {
		List<Map<String, String>> bbsFilterList = articleservice.getBbsFilter();
		log.debug("BBS Filter List: {}", bbsFilterList);
		return bbsFilterList;
	}

	// 필터를 통한 게시글 목록 조회
	@GetMapping("/filterArticles")
	@ResponseBody
	public List<Map<String, Object>> filterArticles(@RequestParam("bbsId") String bbsId,
			@RequestParam("searchType") String searchType, @RequestParam("searchKeyword") String searchKeyword) {
		try {
			searchKeyword = URLDecoder.decode(searchKeyword, StandardCharsets.UTF_8.name());
			log.debug("Decoded searchKeyword: {}", searchKeyword);

			Map<String, Object> params = new HashMap<>();
			params.put("bbsId", bbsId);
			params.put("searchType", searchType);
			params.put("searchKeyword", searchKeyword);
			System.out.println("params: " + params);

			return articleservice.filterArticles(params);
		} catch (Exception e) {
			log.error("Error filtering articles:", e);
			return Collections.emptyList();
		}
	}

	// 게시글 등록
	@PostMapping("/articleRegister.do") // .do->인코딩필터(한글)
	@ResponseBody
	public String insertArticle(HttpServletRequest request, @RequestParam("bbsId") String bbsId,
			@RequestParam(value = "bbsFileName", required = false) MultipartFile bbsFileName,
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

			// 첨부파일 처리 (필수가 아님)
			if (bbsFileName != null && !bbsFileName.isEmpty()) {
				String originalFileName = bbsFileName.getOriginalFilename();
				String storedFileName = UUID.randomUUID().toString() + "_" + originalFileName;

				File directory = new File(FILE_STORE_PATH + "/article/");
				if (!directory.exists()) {
					directory.mkdirs();
				}
				File file = new File(directory, storedFileName);
				bbsFileName.transferTo(file);

				boardVO.setBbsFileName(storedFileName); // 실제 저장된 파일명 (UUID 포함)
				boardVO.setBbsFileOriName(originalFileName); // 원본 파일명
			}

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

	@GetMapping("/downloadFile")
	public ResponseEntity<byte[]> downloadFile(@RequestParam("bbsFileName") String bbsFileName,
			@RequestParam("bbsFileOriName") String bbsFileOriName) {
		try {
			Path filePath = Paths.get(FILE_STORE_PATH + "/article/" + bbsFileName);
			byte[] fileBytes = Files.readAllBytes(filePath);

			String contentDisposition = "attachment; filename=\"" + URLEncoder.encode(bbsFileOriName, "UTF-8") + "\"";

			return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
					.contentType(MediaType.APPLICATION_OCTET_STREAM).body(fileBytes);
		} catch (IOException e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	// 게시글 상세조회 API
	@GetMapping("/selectArticleDetail")
	@ResponseBody
	public BoardVO selectArticleDetail(@RequestParam("bbsId") String bbsId, @RequestParam("nttId") String nttId) {
		BoardVO vo = new BoardVO();
		vo.setBbsId(bbsId);
		vo.setNttId(Long.parseLong(nttId));

		// 조회수 증가 처리
		articleservice.updateArticleRdcnt(vo);

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

		// 필수 값 검증
		if (vo.getBbsId() == null || vo.getBbsId().isEmpty()) {
			throw new IllegalArgumentException("게시판 ID가 누락되었습니다.");
		}
		if (vo.getNttId() == 0) {
			throw new IllegalArgumentException("게시글 ID가 누락되었습니다.");
		}

		// 수정 전 게시글 조회
		BoardVO original = articleservice.selectArticleDetail(vo);
		if (original == null) {
			throw new IllegalArgumentException("기존 게시글 정보를 찾을 수 없습니다.");
		}

		boolean fileUploaded = files != null && files.length > 0 && !files[0].isEmpty();
		boolean fileDeleted = vo.getBbsFileName() == null || vo.getBbsFileName().isEmpty();

		// 파일 삭제 처리
		if (fileDeleted && original.getBbsFileName() != null) {
			deleteExistingFile(original.getBbsFileName());
			vo.setBbsFileName(null);
			vo.setBbsFileOriName(null);
		}

		// 파일 업로드 처리
		if (fileUploaded) {
			MultipartFile file = files[0];
			uploadNewFile(file, vo, original);
		}

		// 파일이 없고 삭제 요청도 없으면 기존 정보 유지
		if (!fileUploaded && !fileDeleted) {
			vo.setBbsFileName(original.getBbsFileName());
			vo.setBbsFileOriName(original.getBbsFileOriName());
		}

		// 게시글 업데이트
		int result = articleservice.updateArticle(vo);
		if (result == 0) {
			throw new RuntimeException("게시글 업데이트 실패");
		}

		return vo;
	}

	private void deleteExistingFile(String fileName) {
		String path = FILE_STORE_PATH + "/article/" + fileName;
		File existingFile = new File(path);
		if (existingFile.exists()) {
			if (existingFile.delete()) {
				log.info("기존 파일 삭제 완료: {}", path);
			} else {
				log.warn("기존 파일 삭제 실패: {}", path);
			}
		}
	}

	private void uploadNewFile(MultipartFile file, BoardVO vo, BoardVO original) {
		String oriName = file.getOriginalFilename();
		String storedName = UUID.randomUUID().toString() + "_" + oriName;
		String filePath = FILE_STORE_PATH + File.separator + "article" + File.separator + storedName;

		try {
			// 디렉토리 생성
			File directory = new File(FILE_STORE_PATH + File.separator + "article");
			if (!directory.exists() && !directory.mkdirs()) {
				throw new IOException("디렉토리 생성 실패: " + directory.getPath());
			}

			// 파일 저장
			file.transferTo(new File(filePath));
			log.info("새 파일 저장 완료: {}", filePath);

			vo.setBbsFileName(storedName);
			vo.setBbsFileOriName(oriName);

			// 기존 파일 삭제
			if (original.getBbsFileName() != null) {
				deleteExistingFile(original.getBbsFileName());
			}
		} catch (IOException e) {
			log.error("파일 저장 중 오류: {}", filePath, e);
			throw new RuntimeException("파일 저장 실패", e);
		}
	}

	// 게시글 삭제 API
	@DeleteMapping("/deleteArticle.do")
	@ResponseBody
	public ResponseEntity<?> deleteArticle(@RequestParam String bbsId, @RequestParam long nttId) {
		BoardVO boardVO = new BoardVO();
		boardVO.setBbsId(bbsId);
		boardVO.setNttId(nttId);

		int result = articleservice.deleteArticle(boardVO);
		if (result > 0) {
			return ResponseEntity.ok(Map.of("success", true));
		} else {
			return ResponseEntity.status(500).body(Map.of("success", false, "message", "게시글 삭제에 실패했습니다."));
		}
	}

}
