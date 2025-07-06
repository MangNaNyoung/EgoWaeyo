
package egovframework.com.egowaeyo.article.controller;

import java.io.File;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	@PostMapping("/articleRegister")
	public String writePage(BoardVO vo, @RequestParam("files") MultipartFile[] files, RedirectAttributes rttr) {
		for (MultipartFile file : files) {
			if (!file.isEmpty()) {
				// 파일 저장 로직 추가
				log.info("파일 이름: {}", file.getOriginalFilename());
			}
		}
		articleservice.articleInsert(vo);
		rttr.addAttribute("result", vo.getRowNo());
		return "redirect:articleList.do";
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

	    // 게시글 수정 처리
	    @PostMapping("/articleUpdate")
	    public String articleUpdate(BoardVO vo,
	                                 @RequestParam(value = "files", required = false) MultipartFile[] files,
	                                 RedirectAttributes rttr) {
	        try {
	        	// 전달된 값 디버깅
	        	 log.debug("Received BoardVO: {}", vo);
	            
	        	 if (vo.getBbsId() == null || vo.getBbsId().isEmpty()) {
	                 rttr.addFlashAttribute("errorMessage", "게시판 ID가 누락되었습니다.");
	                 return "redirect:/article/articleUpdateForm";
	             }
	             if (vo.getNttId() == 0) {
	                 rttr.addFlashAttribute("errorMessage", "게시글 ID가 누락되었습니다.");
	                 return "redirect:/article/articleUpdateForm";
	             }
	            
	            // 파일 업로드 처리
	            if (files != null) {
	                String uploadDir = "/upload/";
	                for (MultipartFile file : files) {
	                    if (!file.isEmpty()) {
	                        String filePath = uploadDir + file.getOriginalFilename();
	                        file.transferTo(new File(filePath));
	                        log.info("파일 저장 완료: {}", filePath);

	                        // 파일 ID 생성 로직 추가 필요
	                        vo.setAtchFileId(filePath); // 예시로 파일 경로를 설정
	                    }
	                }
	            }

	            // 게시글 수정 처리
	            articleservice.updateArticle(vo);

	            // 리다이렉트 설정
	            rttr.addFlashAttribute("message", "게시글이 성공적으로 수정되었습니다.");
	            return "redirect:/article/articleDetail.do?bbsId=" + vo.getBbsId() + "&nttId=" + vo.getNttId();
	        } catch (IllegalArgumentException e) {
	            log.error("게시글 수정 중 오류 발생: {}", vo, e);
	            rttr.addFlashAttribute("error", e.getMessage());
	            return "redirect:/article/articleUpdateForm";
	        } catch (Exception e) {
	            log.error("예기치 못한 오류 발생: {}", vo, e);
	            rttr.addFlashAttribute("error", "게시글 수정 중 오류가 발생했습니다.");
	            return "redirect:/article/articleUpdateForm";
	        }
	    }
	 
}
