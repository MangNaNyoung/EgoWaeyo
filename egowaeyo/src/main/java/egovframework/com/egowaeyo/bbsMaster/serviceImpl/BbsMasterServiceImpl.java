package egovframework.com.egowaeyo.bbsMaster.serviceImpl;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import egovframework.com.egowaeyo.bbsMaster.VO.BoardMasterVO;
import egovframework.com.egowaeyo.bbsMaster.mapper.BbsMasterMapper;
import egovframework.com.egowaeyo.bbsMaster.service.BbsMasterService;

@Transactional
@Service
public class BbsMasterServiceImpl implements BbsMasterService {
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
	public void insertCommonDetailCode(String bbsTyCode, String bbsName, String useAt, String frstRegisterId) {
		bbsMasterMapper.insertCommonDetailCode(bbsTyCode, bbsName, useAt, frstRegisterId);
	}

	@Override
	@Transactional
	public void saveBoard(String boardName, String boardType, String parentBoard, String useAt,
			List<String> selectedRights, String currentUserId) {
		String bbsId = getNextStringId() + RandomStringUtils.randomAlphabetic(10);

		String bbsTyCode;
		if ("게시판 추가".equals(boardType)) {
			String maxCode = getMaxBbsTyCode();
			int nextCode = Integer.parseInt(maxCode.substring(4)) + 1;
			bbsTyCode = String.format("BBST%02d", nextCode);

			insertCommonDetailCode(bbsTyCode, boardName, useAt, currentUserId);
		} else {
			bbsTyCode = parentBoard;
		}

		BoardMasterVO boardMaster = new BoardMasterVO();
		boardMaster.setBbsId(bbsId);
		boardMaster.setBbsNm(boardName);
		boardMaster.setBbsTyCode(bbsTyCode);
		boardMaster.setUseAt(useAt);
		boardMaster.setFrstRegisterId(currentUserId);
		bbsMasterMapper.insertBBSMaster(boardMaster);

		String authorCode = determineAuthorCode(selectedRights);
		BoardMasterVO auth = new BoardMasterVO();
		auth.setEmplyrId(currentUserId);
		auth.setBbsId(bbsId);
		auth.setAuthorCode(authorCode);
		bbsMasterMapper.insertBBSMasterAuth(auth);
	}

	@Override
	public int insertBBSMaster(BoardMasterVO boardMaster) {
		return bbsMasterMapper.insertBBSMaster(boardMaster);
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
