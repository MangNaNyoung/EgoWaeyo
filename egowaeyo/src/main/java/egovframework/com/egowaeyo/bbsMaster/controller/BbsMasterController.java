package egovframework.com.egowaeyo.bbsMaster.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
	
	// 상위 게시판의 하위 게시판 페이지로 이동
	
	
	// 상위게시판 등록
	@PostMapping("register")
	public String registerBbsMaster(BoardMasterVO boardMaster) {
		int result = bbsMasterService.insertBBSMasterInf(boardMaster);
		if (result > 0) {
			return "redirect:/bbsMaster/bbsList"; // 성공 시 게시판 목록으로 리다이렉트
		} else {
			return "error"; // 실패 시 에러 페이지로 이동
		}
	}
}
