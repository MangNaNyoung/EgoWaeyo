package egovframework.com.egowaeyo.bbsMaster.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
			 
			// 로그 추가: bbsType과 parentBoard 값 확인
	        logger.info("bbsType: {}, parentBoard: {}", bbsType, parentBoard);
	        
			List<String> selectedUsers = (List<String>) requestData.get("selectedUsers");

			String bbsTyCode = bbsType.equals("type1") ? generateBbsTyCode() : parentBoard;

			// bbsTyCode 생성 로그 출력
	        logger.info("Generated BBS_TY_CODE: {}", bbsTyCode);
	        
			BoardMasterVO boardMaster = new BoardMasterVO();
			boardMaster.setBbsId(bbsId);
			boardMaster.setBbsNm(bbsName);
			boardMaster.setUseAt(useAt);
			boardMaster.setBbsTyCode(bbsTyCode);

			bbsMasterService.insertBBSMaster(boardMaster);
			insertCommonDetailCode(bbsTyCode, bbsName, useAt, getCurrentUserId());

			response.put("success", true);
		} catch (Exception e) {
			 logger.error("registerBbsMaster 처리 중 오류 발생: {}", e.getMessage(), e);
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
	        List<Map<String, String>> selectedRights = (List<Map<String, String>>) requestData.get("selectedRights");
	        String currentUserId = getCurrentUserId(); // 현재 로그인된 사용자 ID 가져오기

	        if (boardName == null || useAt == null || currentUserId == null) {
	            logger.error("필수 값 누락 - boardName: {}, useAt: {}, currentUserId: {}", boardName, useAt, currentUserId);
	            throw new IllegalArgumentException("필수 값이 누락되었습니다.");
	        }

	        // BBSTYCODE 결정
	        String bbsTyCode = determineBbsTyCode(boardType, parentBoard, boardName, useAt, currentUserId);

	        // bbsTyCode 값 로그 출력
	        logger.info("Determined BBS_TY_CODE: {}", bbsTyCode);

	        // bbsId 생성
	        String bbsId = generateBbsId();
	        logger.info("생성된 bbsId: {}", bbsId);

	        // 게시판 정보 저장
	        BoardMasterVO boardMaster = new BoardMasterVO();
	        boardMaster.setBbsId(bbsId);
	        boardMaster.setBbsNm(boardName);
	        boardMaster.setBbsTyCode(bbsTyCode);
	        boardMaster.setUseAt(useAt);
	        boardMaster.setFrstRegisterId(currentUserId);
	        bbsMasterService.insertBBSMaster(boardMaster);
	        logger.info("게시판 정보 저장 완료: {}", boardMaster);
	        
	        // 권한 정보에 bbsId 추가
	        selectedRights.forEach(right -> right.put("bbsId", bbsId));


	        // 권한 설정
	        bbsMasterService.saveBoardAuth(selectedRights, bbsId);
	        logger.info("권한 정보 저장 완료.");

	        response.put("success", true);
	    } catch (Exception e) {
	        logger.error("saveBoard 처리 중 오류 발생: {}", e.getMessage(), e);
	        response.put("success", false);
	        response.put("error", e.getMessage());
	    }

	    return response;
	}
	
	private String determineBbsTyCode(String boardType, String parentBoard, String boardName, String useAt, String currentUserId) {
	    String bbsTyCode;
	    
	    if ("게시판 추가".equals(boardType)) {
	        // 새로운 게시판 유형 코드 생성 (6자리로 제한)
	        bbsTyCode = generateBbsTyCode();
	    } else {
	        // 상위 게시판의 타입 코드 사용 (6자리로 맞추기)
	        bbsTyCode = parentBoard;

	        if (bbsTyCode == null || bbsTyCode.isEmpty()) {
	            throw new IllegalArgumentException("상위 게시판 정보가 필요합니다.");
	        }

	        // 부모 게시판 이름을 6자리로 잘라서 BBS_TY_CODE로 설정
	        if (bbsTyCode.length() > 6) {
	            bbsTyCode = bbsTyCode.substring(0, 6); // 6자 이내로 잘라냄
	        }
	    }

	    // 로그로 출력 (변경된 BBS_TY_CODE)
	    logger.info("Calculated BBS_TY_CODE (6자 이하): {}", bbsTyCode);

	    return bbsTyCode;
	}
	
	private String generateBbsId() {
	    return "BBSMSTR_" + String.format("%010d", (int) (Math.random() * 100)) + RandomStringUtils.randomAlphabetic(10);
	}
	
	

	private String generateBbsTyCode() {
		String maxCode = bbsMasterService.getMaxBbsTyCode();

	    if (maxCode == null || maxCode.length() < 6) {
	        throw new IllegalStateException("유효한 BBS_TY_CODE를 찾을 수 없습니다.");
	    }

	    try {
	        int nextCode = Integer.parseInt(maxCode.substring(4, 6)) + 1;
	        String generatedCode = String.format("BBST%02d", nextCode);

	        // 생성된 코드가 6자리 이상이면 잘라서 반환
	        if (generatedCode.length() > 6) {
	            generatedCode = generatedCode.substring(0, 6); // 6자리로 잘라냄
	        }

	        // 로그로 출력 (생성된 BBS_TY_CODE)
	        logger.info("Generated BBS_TY_CODE (6자 이하): {}", generatedCode);

	        return generatedCode;
	    } catch (NumberFormatException e) {
	        throw new IllegalStateException("BBS_TY_CODE의 형식이 올바르지 않습니다: " + maxCode, e);
	    }
	}

	private void insertCommonDetailCode(String bbsTyCode, String bbsName, String useAt, String currentUserId) {
		 if (bbsTyCode == null || bbsName == null || useAt == null || currentUserId == null) {
		        throw new IllegalArgumentException("공통 코드 삽입에 필요한 값이 누락되었습니다.");
		    }

		    // 6자 이상인 경우 6자로 잘라서 삽입
		    if (bbsTyCode.length() > 6) {
		        bbsTyCode = bbsTyCode.substring(0, 6);
		    }

		    // 삽입할 값 로그 출력
		    logger.info("Inserting common detail code with BBS_TY_CODE: {}, BBS_NAME: {}, USE_AT: {}, CURRENT_USER: {}", 
		                bbsTyCode, bbsName, useAt, currentUserId);

		    bbsMasterService.insertCommonDetailCode(bbsTyCode, bbsName, useAt, currentUserId);
	}

	// 사이드바
	@GetMapping("/groupedBbsData")
	@ResponseBody
	public Map<String, List<Map<String, String>>> getGroupedBbsData() {
		return bbsMasterService.getGroupedBbsData();
	}
}