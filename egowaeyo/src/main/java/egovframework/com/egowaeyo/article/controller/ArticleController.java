package egovframework.com.egowaeyo.article.controller;

import java.util.List;
import java.util.Locale;
import java.util.Map;

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

	final ArticleService articleservice;

	// 게시글 목록화면으로 이동
	@GetMapping("/articleList.do")
	public String listPage(Model model, BoardVO vo) {
		// selectBbsAll 호출
		Map<String, Object> data = articleservice.selectBbsAll(vo);

		if (data == null || data.get("list") == null) {
			log.error("No data found for bbsId: {}", vo.getBbsId());
			model.addAttribute("list", List.of()); // 빈 리스트 전달
			return "article/ArticleList.html";
		}

		// 필요한 데이터 추출
		Object listObject = data.get("list");
		if (listObject instanceof List<?>) {
			List<?> rawList = (List<?>) listObject;
			if (!rawList.isEmpty() && rawList.get(0) instanceof BoardVO) {
				List<BoardVO> list = (List<BoardVO>) rawList;
				model.addAttribute("list", list);
			} else {
				log.error("Invalid data type in list for bbsId: {}", vo.getBbsId());
				model.addAttribute("list", List.of()); // 빈 리스트 전달
			}
		} else {
			log.error("Invalid list object for bbsId: {}", vo.getBbsId());
			model.addAttribute("list", List.of()); // 빈 리스트 전달
		}

		return "article/ArticleList.html";
	}

	@GetMapping("/selectBbsAll")
	@ResponseBody
	public List<BoardVO> selectBbsAll(@RequestParam(value = "bbsId", required = false) String bbsId) {
		if (bbsId == null || bbsId.isEmpty()) {
			throw new IllegalArgumentException("bbsId is required");
		}
		BoardVO vo = new BoardVO();
		vo.setBbsId(bbsId);
		Map<String, Object> data = articleservice.selectBbsAll(vo);
		return (List<BoardVO>) data.get("list");
	}

	// 게시글 작성화면으로 이동
	@GetMapping("/articleRegister")
	public String home(Locale locale, Model model) {
		return "/article/ArticleRegist.html";
	}

	// 게시글 등록
	@PostMapping("/articleRegister")
	public String writePage(BoardVO vo, @RequestParam("files") MultipartFile[] files, RedirectAttributes rttr) {
		for (MultipartFile file : files) {
			if (!file.isEmpty()) {
				// 파일 저장 로직 추가
				System.out.println("파일 이름: " + file.getOriginalFilename());
			}
		}
		articleservice.articleInsert(vo);
		rttr.addAttribute("result", vo.getRowNo());
		return "redirect:articleList";
	}

	@GetMapping("/articleDetail")
	public String articleDetail() {
		return "article/ArticleDetail.html"; // HTML 파일 반환
	}

}
