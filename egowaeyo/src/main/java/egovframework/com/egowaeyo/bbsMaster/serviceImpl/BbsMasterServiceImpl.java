package egovframework.com.egowaeyo.bbsMaster.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import egovframework.com.egowaeyo.bbsMaster.VO.BoardMasterVO;
import egovframework.com.egowaeyo.bbsMaster.mapper.BbsMasterMapper;
import egovframework.com.egowaeyo.bbsMaster.service.BbsMasterService;

@Transactional
@Service
public class BbsMasterServiceImpl implements BbsMasterService {
	 private static final Logger logger = LoggerFactory.getLogger(BbsMasterServiceImpl.class);

	@Autowired
	private BbsMasterMapper bbsMasterMapper;

	@Override
	public String getNextStringId() {
		return UUID.randomUUID().toString();
	}

	@Override
	public List<Map<String, String>> getCombbs() {
		return bbsMasterMapper.selectCombbs();
	}

	@Override
	public List<BoardMasterVO> getDeptEmp() {
		return bbsMasterMapper.selectDeptEmp();
	}

	@Override
	public String getMaxBbsTyCode() {
		return bbsMasterMapper.getMaxBbsTyCode();
	}
	
	@Override
	public void insertCommonDetailCode(String bbsTyCode, String bbsName, String useAt, String currentUserId) {
        Map<String, Object> params = new HashMap<>();
        params.put("code", bbsTyCode);
        params.put("codeNm", bbsName);
        params.put("useAt", useAt);
        params.put("frstRegisterId", currentUserId);
        bbsMasterMapper.insertCommonDetailCode(params);
    }

	
	@Override
	@Transactional
	public void saveBoard(String boardName, String boardType, String parentBoard, String useAt,
	                      List<Map<String, String>> selectedRights, String currentUserId) {
	    // 게시판 ID 생성
	    String bbsId = getNextStringId().substring(0, 20) + RandomStringUtils.randomAlphabetic(10);

	    // 게시판 유형 코드 결정
	    String bbsTyCode;
	    if ("게시판 추가".equals(boardType)) {
	        String maxCode = getMaxBbsTyCode();
	        int nextCode = Integer.parseInt(maxCode.substring(4)) + 1;
	        bbsTyCode = String.format("BBST%02d", nextCode);

	        insertCommonDetailCode(bbsTyCode, boardName, useAt, currentUserId);
	    } else {
	        bbsTyCode = selectCodeByCodeNm(parentBoard);
	        if (bbsTyCode == null) {
	            throw new IllegalArgumentException("상위 게시판 이름 '" + parentBoard + "'에 해당하는 CODE를 찾을 수 없습니다.");
	        }
	    }

	    // 게시판 정보 저장
	    BoardMasterVO boardMaster = new BoardMasterVO();
	    boardMaster.setBbsId(bbsId);
	    boardMaster.setBbsNm(boardName);
	    boardMaster.setBbsTyCode(bbsTyCode);
	    boardMaster.setUseAt(useAt);
	    boardMaster.setFrstRegisterId(currentUserId);
	    insertBBSMaster(boardMaster);

	    // 권한 정보 저장
	    saveBoardAuth(selectedRights, bbsId);
	}
	public void saveBoardAuth(List<Map<String, String>> selectedRights, String bbsId) {
	    if (selectedRights == null || selectedRights.isEmpty()) {
	        throw new IllegalArgumentException("권한이 선택되지 않았습니다.");
	    }

	    for (Map<String, String> right : selectedRights) {
	        String emplyrId = right.get("emplyrId");
	        String authorCode = right.get("authorCode");

	        if (emplyrId == null || authorCode == null || authorCode.isBlank()) {
	            logger.warn("권한 정보 누락 - emplyrId: {}, authorCode: {}", emplyrId, authorCode);
	            continue;
	        }

	        BoardMasterVO auth = new BoardMasterVO();
	        auth.setEmplyrId(emplyrId);
	        auth.setBbsId(bbsId);
	        auth.setAuthorCode(authorCode);

	        insertBBSMasterAuth(auth); // db저장
	        logger.info("삽입 중: emplyrId={}, bbsId={}, authorCode={}", emplyrId, bbsId, authorCode);

	    }
	}

	@Override
	public int insertBBSMaster(BoardMasterVO boardMaster) {
	    try {
	        logger.info("삽입할 게시판 데이터: {}", boardMaster.toString());
	        return bbsMasterMapper.insertBBSMaster(boardMaster);
	    } catch (Exception e) {
	        logger.error("게시판 데이터 삽입 중 오류 발생: {}", e.getMessage(), e);
	        throw e; // 예외를 다시 던져 호출한 쪽에서 처리할 수 있도록 함
	    }
	}

	@Override
	public int insertBBSMasterAuth(BoardMasterVO auth) {
		return bbsMasterMapper.insertBBSMasterAuth(auth);
	}
	

	
	private String determineAuthorCode(List<String> selectedRights) {
	    if (selectedRights == null || selectedRights.isEmpty()) {
	        throw new IllegalArgumentException("권한이 선택되지 않았습니다.");
	    }

	    // 권한 이름만 추출
	    List<String> rights = selectedRights.stream()
	        .map(right -> right.replaceAll("\\(.*?\\)", "").trim()) // 괄호와 그 안의 내용을 제거
	        .collect(Collectors.toList());

	    if (rights.contains("관리권한")) {
	        return "A-003";
	    } else if (rights.contains("쓰기권한")) {
	        return "A-002";
	    } else if (rights.contains("읽기권한")) {
	        return "A-001";
	    } else {
	        throw new IllegalArgumentException("유효하지 않은 권한입니다.");
	    }
	}
	// 사이드바
	   @Override
	   public Map<String, List<String>> getGroupedBbsData() {
		   List<Map<String, String>> rawData = bbsMasterMapper.selectGroupedBbsData();
		   
		   // 키 이름을 소문자로 변환하여 접근
		   Map<String, List<String>> groupedData = rawData.stream().filter(item -> item.get("CODENM") != null) // null 키
				   // 필터링
				   .collect(Collectors.groupingBy(item -> item.get("CODENM"), // 대소문자 구분 문제 해결
						   Collectors.mapping(item -> item.getOrDefault("BBSNM", "UNKNOWN"), Collectors.toList())));
		   return groupedData;
	   }

	@Override
	public void insertCommonDetailCode(Map<String, Object> params) {
		logger.info("삽입할 게시판 데이터: {}", params.toString());
		bbsMasterMapper.insertCommonDetailCode(params);
		
	}

	@Override
	public String selectCodeByCodeNm(String codeNm) {
	    String code = bbsMasterMapper.selectCodeByCodeNm(codeNm);
	    if (code == null) {
	        throw new IllegalArgumentException("상위 게시판 이름 '" + codeNm + "'에 해당하는 CODE를 찾을 수 없습니다.");
	    }
	    return code;
	}



	

	/*
	 * @SuppressWarnings("rawtypes")
	 * 
	 * @Override public Map<String, List<String>> getGroupedBbsData() {
	 * <List>rawData = bbsMasterMapper.getGroupedBbsData(); <Map>groupedData = new
	 * HashMap<>(); for(Map entry : rawData) { String codeNm = entry.get("codeNm");
	 * String bbsNm = entry.get("bbsNm");
	 * 
	 * groupedData.computeIfAbsent(codeNm, k -> new ArrayList<>()).add(bbsNm); }
	 * return groupedData(); }
	 */
}
