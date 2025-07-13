package egovframework.com.egowaeyo.bbsMaster.serviceImpl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
		try {
			// 권한 정보 검증
			validateRights(selectedRights, boardType);

			// 게시판 ID 생성
			String bbsId = generateBbsId();

			// 게시판 유형 코드 결정
			String bbsTyCode = determineBbsTyCode(boardType, parentBoard, boardName, useAt, currentUserId);

			// 게시판 정보 저장
			BoardMasterVO boardMaster = new BoardMasterVO();
			boardMaster.setBbsId(bbsId);
			boardMaster.setBbsNm(boardName);
			boardMaster.setBbsTyCode(bbsTyCode);
			boardMaster.setUseAt(useAt);
			boardMaster.setFrstRegisterId(currentUserId);
			insertBBSMaster(boardMaster);

			// 공통코드 테이블에 추가
			if ("type1".equals(boardType)) {
				insertCommonDetailCode(bbsTyCode, boardName, useAt, currentUserId);
			}

			// 권한 정보 검증 및 저장
			if (!"type1".equals(boardType) && (selectedRights == null || selectedRights.isEmpty())) {
				throw new IllegalArgumentException("권한 정보가 누락되었습니다.");
			}

			if (selectedRights != null && !selectedRights.isEmpty()) {
				selectedRights.forEach(right -> right.put("bbsId", boardMaster.getBbsId()));
				saveBoardAuth(selectedRights, boardMaster.getBbsId());
			}
		} catch (Exception e) {
			throw new RuntimeException("saveBoard 처리 중 오류 발생: " + e.getMessage(), e);
		}
	}

	private void validateRights(List<Map<String, String>> selectedRights, String boardType) {
		if (!"type1".equals(boardType) && (selectedRights == null || selectedRights.isEmpty())) {
			throw new IllegalArgumentException("권한 정보가 누락되었습니다.");
		}
	}

	private String generateBbsId() {
		 return "BBSMSTR_" + String.format("%010d", (int) (Math.random() * 1000000000));
	}

	private String determineBbsTyCode(String boardType, String parentBoard, String boardName, String useAt,
			String currentUserId) {
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
		return bbsTyCode;
	}

	@Override
	public void saveBoardAuth(List<Map<String, String>> selectedRights, String bbsId) {
		if (selectedRights == null || selectedRights.isEmpty()) {
			logger.warn("권한 정보가 없습니다.");
			return;
		}

		if (bbsId == null || bbsId.isEmpty()) {
			throw new IllegalArgumentException("유효하지 않은 bbsId입니다.");
		}

		for (Map<String, String> right : selectedRights) {
			String emplyrId = right.get("emplyrId"); // 키 이름 수정
			String authorCode = right.get("authorCode");

			// 권한 정보 누락 체크
			if (emplyrId == null || emplyrId.isEmpty()) {
				logger.warn("권한 정보 누락 - emplyrId: null or empty, authorCode: {}", authorCode);
				continue; // 누락된 권한 정보는 건너뜀
			}

			// 권한 유효성 검증
			if (!isValidPermission(authorCode)) {
				logger.error("유효하지 않은 권한: emplyrId={}, authorCode={}", emplyrId, authorCode);
				throw new IllegalArgumentException("유효하지 않은 권한입니다: " + authorCode);
			}

			// bbsId를 추가하여 권한 정보 저장
			right.put("bbsId", bbsId);

			BoardMasterVO auth = new BoardMasterVO();
			auth.setEmplyrId(emplyrId);
			auth.setBbsId(bbsId);
			auth.setAuthorCode(authorCode);

			// DB 저장
			try {
				insertBBSMasterAuth(auth);
				logger.info("권한 저장 성공: emplyrId={}, bbsId={}, authorCode={}", emplyrId, bbsId, authorCode);
			} catch (Exception e) {
				logger.error("권한 저장 실패: emplyrId={}, bbsId={}, authorCode={}", emplyrId, bbsId, authorCode, e);
				// 예외를 던지지 않고 로그만 기록하여 다른 작업이 계속 진행되도록 설정
			}
		}
	}

//권한 코드 유효성 체크 예시
	private boolean isValidPermission(String authorCode) {
		// 실제 권한 코드 검증 로직 (예: DB에서 검증)
		return authorCode.equals("A-001") || authorCode.equals("A-002") || "A-003".equals(authorCode); // 예시
	}

//권한 부여 처리 예시
	private void assignPermissions(String bbsId, String emplryId, String authorCode) {
		// 실제 권한 부여 로직 (DB에 권한 정보 저장)
		// 예: insert 권한 데이터
		// insertBoardAuth(bbsId, emplryId, authorCode);
	}

	private String determineAuthorCode(String authorName) {
		if (authorName == null) {
			throw new IllegalArgumentException("권한 이름이 null입니다.");
		}

		switch (authorName.trim()) {
		case "관리권한":
			return "A-003";
		case "쓰기권한":
			return "A-002";
		case "읽기권한":
			return "A-001";
		default:
			throw new IllegalArgumentException("유효하지 않은 권한입니다: " + authorName);
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
		List<String> rights = selectedRights.stream().map(right -> right.replaceAll("\\(.*?\\)", "").trim()) // 괄호와 그 안의
																												// 내용을
																												// 제거
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
	public Map<String, List<Map<String, String>>> getGroupedBbsData() {
		List<Map<String, String>> rawData = bbsMasterMapper.selectGroupedBbsData();

		// 키 이름을 소문자로 변환하여 접근
	    Map<String, List<Map<String, String>>> groupedData = rawData.stream()
	        .filter(item -> item.get("codeNm") != null) // codeNm이 null이 아닌 경우만 필터링
	        .collect(Collectors.groupingBy(
	            item -> item.get("codeNm").toLowerCase(), // codeNm을 소문자로 변환하여 그룹화
	            Collectors.mapping(item -> item, Collectors.toList())
	        ));
	    
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

	@Override
	public void insertBoardAuth(List<Map<String, String>> selectedRights, String bbsId) {
		if (selectedRights == null || selectedRights.isEmpty()) {
			throw new IllegalArgumentException("권한이 선택되지 않았습니다.");
		}

		for (Map<String, String> right : selectedRights) {
			String emplyrId = right.get("emplyrId");
			String authorCode = determineAuthorCode(right.get("authorCode"));

			if (emplyrId == null || authorCode == null || authorCode.isBlank()) {
				logger.warn("권한 정보 누락 - emplyrId: {}, authorCode: {}", emplyrId, authorCode);
				continue;
			}

			// 권한 정보를 저장할 객체 생성
			BoardMasterVO auth = new BoardMasterVO();
			auth.setEmplyrId(emplyrId);
			auth.setBbsId(bbsId);
			auth.setAuthorCode(authorCode);

			// DB에 권한 정보 삽입
			insertBBSMasterAuth(auth);
			logger.info("삽입 중: emplyrId={}, bbsId={}, authorCode={}", emplyrId, bbsId, authorCode);
		}
	}

	@Override
	public List<BoardMasterVO> getBbsMasterInfo(String bbsId) {
		return bbsMasterMapper.selectBBSMasterInfo(bbsId);
	}

	@Override
	public void updateBBSMaster(BoardMasterVO vo) throws Exception {
		bbsMasterMapper.updateBBSMaster(vo);
	}

	@Override
	public void updateBoardWithAuth(BoardMasterVO board, List<BoardMasterVO> authList) {

	}

	@Override
	public void updateBoardAuth(List<Map<String, String>> selectedRights, String bbsId) {
		if (bbsId == null || bbsId.isEmpty()) {
			throw new IllegalArgumentException("bbsId가 유효하지 않습니다.");
		}

		try {
			// 1. 기존 권한 조회
			List<BoardMasterVO> existingAuths = bbsMasterMapper.selectBBSMasterAuthList(bbsId);

			// 2. 새로운 권한 리스트 → Map으로 전환 (emplyrId → authorCode)
			Map<String, String> newAuthMap = new HashMap<>();
			for (Map<String, String> right : selectedRights) {
				String emplyrId = right.get("emplyrId");
				String authorCode = right.get("authorCode");
				if (emplyrId != null && authorCode != null) {
					newAuthMap.put(emplyrId, authorCode);
				}
			}

			// 3. 기존 권한과 비교
			Set<String> processed = new HashSet<>();

			for (BoardMasterVO existing : existingAuths) {
				String emplyrId = existing.getEmplyrId();
				String oldAuthorCode = existing.getAuthorCode();
				String newAuthorCode = newAuthMap.get(emplyrId);

				BoardMasterVO vo = new BoardMasterVO();
				vo.setEmplyrId(emplyrId);
				vo.setBbsId(bbsId);

				if (newAuthorCode == null) {
					// 삭제
					bbsMasterMapper.deleteBBSMasterAuth(vo);
				} else if (!oldAuthorCode.equals(newAuthorCode)) {
					// 수정
					vo.setAuthorCode(newAuthorCode);
					bbsMasterMapper.updateBBSMasterAuth(vo);
				}
				processed.add(emplyrId);
			}

			// 4. 새로 추가된 권한
			for (Map<String, String> right : selectedRights) {
				String emplyrId = right.get("emplyrId");
				if (!processed.contains(emplyrId)) {
					BoardMasterVO vo = new BoardMasterVO();
					vo.setEmplyrId(emplyrId);
					vo.setBbsId(bbsId);
					vo.setAuthorCode(right.get("authorCode"));
					bbsMasterMapper.insertBBSMasterAuth(vo);
				}
			}
			logger.info("게시판 권한이 성공적으로 수정되었습니다.");
		} catch (Exception e) {
			logger.error("게시판 권한 수정 중 오류 발생: {}", e.getMessage(), e);
			throw new RuntimeException("게시판 권한 수정에 실패했습니다.");
		}

	}

	@Override
	public Map<String, String> getCommonCodeInfo(String code) {
		 Map<String, String> result = bbsMasterMapper.selectCommonCodeInfo(code);
		    if (result == null) {
		        result = new HashMap<>();
		    }
		    return result;
	}

	@Override
	public int updateCommonCodeInfo(BoardMasterVO boardMasterVO) {
	    if (boardMasterVO.getCode() == null || boardMasterVO.getCode().isEmpty()) {
	        throw new IllegalArgumentException("Code 값이 비어있습니다.");
	    }
	    logger.debug("Updating common code info: {}", boardMasterVO);
	    int result = bbsMasterMapper.updateCommonCodeInfo(boardMasterVO);
	    if (result == 0) {
	        logger.warn("No rows were updated for code: {}", boardMasterVO.getCode());
	    }
	    return result;

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