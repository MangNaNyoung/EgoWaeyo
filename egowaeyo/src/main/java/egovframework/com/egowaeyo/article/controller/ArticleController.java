package egovframework.com.egowaeyo.article.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
		System.out.println("도착합니까?");
			
			 List<BoardVO> data = articleservice.findAll(vo);
			 log.info("data======================= : "+data);
			model.addAttribute("list",data);
		return "article/ArticleList.html";
	}

    
}
