package egovframework.com.egowaeyo.bbsMaster.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.com.egowaeyo.article.VO.BoardMasterVO;
import egovframework.com.egowaeyo.bbsMaster.service.BbsMasterService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/bbsMaster")
@Controller
public class BbsMasterController {
	
	final BbsMasterService bbsMasterService;
	// 화면 이동
	/*
	 * @GetMapping("/bbsMasterList") public String bbsMasterList() { return
	 * "article/BbsMaster.html"; // 게시판 목록 페이지로 이동 }
	 */
	
	// 데이터 가져오기
	@GetMapping("/bbsNames")
	@ResponseBody
	public List<String> getBbsNames() {
	    return bbsMasterService.selectBbsMasterList()
	                           .stream()
	                           .map(BoardMasterVO::getBbsNm)
	                           .toList();
	}
}
