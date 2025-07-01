package egovframework.com.egowaeyo.bbsMaster.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
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
			logger.info("parentBoard 값: {}", parentBoard);
			List<String> selectedUsers = (List<String>) requestData.get("selectedUsers");

			String bbsTyCode = bbsType.equals("type1") ? generateBbsTyCode() : parentBoard;

			BoardMasterVO boardMaster = new BoardMasterVO();
			boardMaster.setBbsId(bbsId);
			boardMaster.setBbsNm(bbsName);
			boardMaster.setUseAt(useAt);
			boardMaster.setBbsTyCode(bbsTyCode);

			bbsMasterService.insertBBSMaster(boardMaster);
			insertCommonDetailCode(bbsTyCode, bbsName, useAt, getCurrentUserId());

			response.put("success", true);
		} catch (Exception e) {
			response.put("success", false);
			response.put("error", e.getMessage());
		}
		return response;
	}

	private String getCurrentUserId() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}
	
	private static final Logger logger = LoggerFactory.getLogger(BbsMasterController.class);
	// 게시판 추가
	@PostMapping("/saveBoard")
	@ResponseBody
	public Map<String, Object> saveBoard(@RequestBody Map<String, Object> requestData) {
	    Map<String, Object> response = new HashMap<>();

	    try {
	        logger.info("saveBoard 호출 - 요청 데이터: {}", requestData);

	        String boardName = (String) requestData.get("boardName");
	        String boardType = (String) requestData.get("boardType");
	        String parentBoard = (String) requestData.get("parentBoard");
	        String useAt = (String) requestData.get("useAt");
	        List<String> selectedRights = (List<String>) requestData.get("selectedRights");
	        String currentUserId = getCurrentUserId(); // 현재 로그인된 사용자 ID 가져오기

	        if (boardName == null || useAt == null || currentUserId == null) {
	            logger.error("필수 값 누락 - boardName: {}, useAt: {}, currentUserId: {}", boardName, useAt, currentUserId);
	            throw new IllegalArgumentException("필수 값이 누락되었습니다.");
	        }

	        // BBSTYCODE 결정
	        String bbsTyCode;
	        if ("게시판 추가".equals(boardType)) {
	            bbsTyCode = bbsMasterService.getMaxBbsTyCode();
	            logger.info("새로운 BBSTYCODE 생성: {}", bbsTyCode);
	            bbsMasterService.insertCommonDetailCode(bbsTyCode, boardName, useAt, currentUserId);
	        } else {
	        	logger.info("parentBoard 값: {}", parentBoard);
	        	bbsTyCode = bbsMasterService.selectCodeByCodeNm(parentBoard);
	        	if (bbsTyCode == null) {
	        	    logger.error("상위 게시판 이름에 해당하는 CODE를 찾을 수 없습니다. parentBoard: {}", parentBoard);
	        	    throw new IllegalArgumentException("상위 게시판 이름에 해당하는 CODE를 찾을 수 없습니다.");
	        	}
	            
	            logger.info("상위 게시판 BBSTYCODE 사용: {}", bbsTyCode);
	        }

	        // bbsId 생성
	        String bbsId = "BBSMSTR_" + String.format("%010d", (int) (Math.random() * 100)) + RandomStringUtils.randomAlphabetic(10);
	        logger.info("생성된 bbsId: {}", bbsId);

	        // insertBBSMaster 실행
	        BoardMasterVO boardMaster = new BoardMasterVO();
	        boardMaster.setBbsId(bbsId);
	        boardMaster.setBbsNm(boardName);
	        boardMaster.setBbsTyCode(bbsTyCode);
	        boardMaster.setUseAt(useAt);
	        boardMaster.setFrstRegisterId(currentUserId);
	        bbsMasterService.insertBBSMaster(boardMaster);
	        logger.info("게시판 정보 저장 완료: {}", boardMaster);

	        // 권한 결정 및 insertBBSMasterAuth 실행
	        String authorCode = determineAuthorCode(selectedRights);
	        BoardMasterVO auth = new BoardMasterVO();
	        auth.setEmplyrId(currentUserId);
	        auth.setBbsId(bbsId);
	        auth.setAuthorCode(authorCode);
	        bbsMasterService.insertBBSMasterAuth(auth);
	        logger.info("권한 정보 저장 완료: {}", auth);

	        response.put("success", true);
	    } catch (Exception e) {
	        logger.error("saveBoard 처리 중 오류 발생: {}", e.getMessage(), e);
	        response.put("success", false);
	        response.put("error", e.getMessage());
	    }

	    return response;
	}

	private String determineAuthorCode(List<String> selectedRights) {
	    if (selectedRights == null || selectedRights.isEmpty()) {
	        throw new IllegalArgumentException("권한이 선택되지 않았습니다.");
	    }
	    if (selectedRights.contains("관리권한")) {
	        return "A-003";
	    } else if (selectedRights.contains("쓰기권한")) {
	        return "A-002";
	    } else if (selectedRights.contains("읽기권한")) {
	        return "A-001";
	    } else {
	        throw new IllegalArgumentException("유효하지 않은 권한입니다.");
	    }
	}

	private String generateBbsTyCode() {
	    String maxCode = bbsMasterService.getMaxBbsTyCode();

	    if (maxCode == null || maxCode.length() < 6) {
	        throw new IllegalStateException("유효한 BBS_TY_CODE를 찾을 수 없습니다.");
	    }

	    try {
	        int nextCode = Integer.parseInt(maxCode.substring(4, 6)) + 1;
	        return String.format("BBST%02d", nextCode);
	    } catch (NumberFormatException e) {
	        throw new IllegalStateException("BBS_TY_CODE의 형식이 올바르지 않습니다: " + maxCode, e);
	    }
	}

	private void insertCommonDetailCode(String bbsTyCode, String bbsName, String useAt, String currentUserId) {
	    if (bbsTyCode == null || bbsName == null || useAt == null || currentUserId == null) {
	        throw new IllegalArgumentException("공통 코드 삽입에 필요한 값이 누락되었습니다.");
	    }
	    bbsMasterService.insertCommonDetailCode(bbsTyCode, bbsName, useAt, currentUserId);
	}
	
	// 사이드바
	@GetMapping("/groupedBbsData")
	@ResponseBody
	public Map<String, List<String>> getGroupedBbsData() {
		return bbsMasterService.getGroupedBbsData();
	}
}