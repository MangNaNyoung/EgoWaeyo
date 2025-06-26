package egovframework.com.egowaeyo.article.controller;

import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	@GetMapping("/articleList")
	public String listPage(Model model, BoardVO vo) {
			 List<BoardVO> data = articleservice.findAll(vo);
			model.addAttribute("list",data);
		return "article/ArticleList.html";
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

}
