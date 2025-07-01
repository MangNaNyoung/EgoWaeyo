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
import org.springframework.web.bind.annotation.RequestBody;

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
            List<String> selectedRights, String currentUserId) {

// 게시판 ID 생성
String bbsId = getNextStringId().substring(0, 20) + RandomStringUtils.randomAlphabetic(10);

String bbsTyCode;

// 게시판 유형에 따라 처리
if ("게시판 추가".equals(boardType)) {
// 새로운 게시판 유형 코드 생성
String maxCode = getMaxBbsTyCode();
int nextCode = Integer.parseInt(maxCode.substring(4)) + 1;
bbsTyCode = String.format("BBST%02d", nextCode);

// 공통 상세 코드 삽입
insertCommonDetailCode(bbsTyCode, boardName, useAt, currentUserId);
} else {
// 상위 게시판 이름을 기반으로 코드 조회
bbsTyCode = bbsMasterMapper.selectCodeByCodeNm(parentBoard);
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
bbsMasterMapper.insertBBSMaster(boardMaster);

// 권한 설정
String authorCode = determineAuthorCode(selectedRights);
BoardMasterVO auth = new BoardMasterVO();
auth.setEmplyrId(currentUserId);
auth.setBbsId(bbsId);
auth.setAuthorCode(authorCode);
bbsMasterMapper.insertBBSMasterAuth(auth);
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
		if (selectedRights.contains("관리권한")) {
			return "A-003";
		} else if (selectedRights.contains("쓰기권한")) {
			return "A-002";
		} else {
			return "A-001";
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
	public String selectCodeByCodeNm(@RequestBody String codeNm) {
	    // 매퍼를 호출하여 codeNm에 해당하는 코드를 반환
	    return bbsMasterMapper.selectCodeByCodeNm(codeNm);
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
