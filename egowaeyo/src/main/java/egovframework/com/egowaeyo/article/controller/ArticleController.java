
package egovframework.com.egowaeyo.article.controller;

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
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@RequestMapping("/article")
@Controller
public class ArticleController {

	private final ArticleService articleservice;

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

	// 게시글 상세화면으로 이동
	@GetMapping("/articleDetail.do")
	public String articleDetail() {
		return "article/ArticleDetail.html"; // HTML 파일 반환
	}
}
