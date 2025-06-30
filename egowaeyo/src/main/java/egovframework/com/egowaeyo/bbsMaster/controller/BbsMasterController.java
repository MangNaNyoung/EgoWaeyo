package egovframework.com.egowaeyo.bbsMaster.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.com.egowaeyo.bbsMaster.VO.BoardMasterVO;
import egovframework.com.egowaeyo.bbsMaster.service.BbsMasterService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/bbsMaster")
@Controller
public class BbsMasterController {
	@Autowired
	final BbsMasterService bbsMasterService;

	// 부서, 사원 목록 가져오기(모달)
	@GetMapping("/selectDeptEmp")
	@ResponseBody
	public List<BoardMasterVO> getDeptEmp() {
		List<BoardMasterVO> deptEmpList = bbsMasterService.getDeptEmp();
		deptEmpList.forEach(emp -> System.out.println(emp.toString())); // 로그 출력
		return deptEmpList;
	}

	// 상위게시판 가져오기(모달)
	@GetMapping("/combbs")
	@ResponseBody
	public List<Map<String, String>> getCombbs() {
		List<Map<String, String>> result = bbsMasterService.getCombbs();
		System.out.println("API Response: " + result); // 로그 추가
		return result;
	}
	// 화면 이동
	/*
	 * @GetMapping("/bbsMasterList") public String bbsMasterList() { return
	 * "article/BbsMaster.html"; // 게시판 목록 페이지로 이동 }
	 */

	@PostMapping("/register/json")
	@ResponseBody
	public Map<String, Object> registerBbsMaster(@RequestBody Map<String, Object> requestData) {
		Map<String, Object> response = new HashMap<>();
		try {
			String bbsId = (String) requestData.get("bbsId");
			String bbsName = (String) requestData.get("bbsName");
			String useAt = (String) requestData.get("useAt");
			String bbsType = (String) requestData.get("bbsType");
			String parentBoard = (String) requestData.get("parentBoard");
			List<String> selectedUsers = (List<String>) requestData.get("selectedUsers");

			String bbsTyCode = bbsType.equals("type1") ? generateBbsTyCode() : parentBoard;

			BoardMasterVO boardMaster = new BoardMasterVO();
			boardMaster.setBbsId(bbsId);
			boardMaster.setBbsNm(bbsName);
			boardMaster.setUseAt(useAt);
			boardMaster.setBbsTyCode(bbsTyCode);

			bbsMasterService.insertBBSMaster(boardMaster);
			insertCommonDetailCode(bbsTyCode, bbsName, useAt);

			response.put("success", true);
		} catch (Exception e) {
			response.put("success", false);
			response.put("error", e.getMessage());
		}
		return response;
	}

	// 게시판 추가
	@PostMapping("/register")
	public String registerBbsMaster(@RequestBody BoardMasterVO boardMaster) {
		try {
			bbsMasterService.insertBBSMaster(boardMaster);
			return "게시판이 성공적으로 추가되었습니다.";
		} catch (Exception e) {
			e.printStackTrace();
			return "게시판 추가에 실패했습니다.";
		}
	}

	private String generateBbsTyCode() {
		// 가장 큰 BBS_TY_CODE 조회 후 +1
		String maxCode = bbsMasterService.getMaxBbsTyCode();
		int nextCode = Integer.parseInt(maxCode.substring(4)) + 1;
		return String.format("BBST%02d", nextCode);
	}

	private void insertCommonDetailCode(String bbsTyCode, String bbsName, String useAt) {
		// COMTCCMMNDETAILCODE 테이블에 삽입
		bbsMasterService.insertCommonDetailCode(bbsTyCode, bbsName, useAt);
	}

	// 사이드바
	@GetMapping("/groupedBbsData")
	@ResponseBody
	public Map<String, List<String>> getGroupedBbsData() {
		return bbsMasterService.getGroupedBbsData();
	}
}