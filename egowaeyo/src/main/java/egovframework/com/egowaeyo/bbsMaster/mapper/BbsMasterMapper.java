package egovframework.com.egowaeyo.bbsMaster.mapper;

import java.util.List;
import java.util.Map;

import egovframework.com.egowaeyo.bbsMaster.VO.BoardMasterVO;

public interface BbsMasterMapper {

	List<Map<String, String>> selectCombbs();

	List<BoardMasterVO> selectDeptEmp();

	List<Map<String, String>> selectGroupedBbsData();

	// 게시판 마스터 정보 삽입
	int insertBBSMaster(BoardMasterVO boardMaster);

	// 게시판 권한 정보 삽입
	int insertBBSMasterAuth(BoardMasterVO auth);

	// 게시판 권한 일괄 삽입 (selectedRights를 받아서 처리)
	void saveBoardAuth(List<Map<String, String>> selectedRights, String bbsId);

	 // 공통 코드 삽입
	void insertCommonDetailCode(Map<String, Object> params);

	// 새로운 게시판 유형 코드 생성
	String getNextStringId();

	// 최대 게시판 유형 코드 가져오기
	String getMaxBbsTyCode();

	 // 상위 코드 이름에 해당하는 코드 가져오기
	String selectCodeByCodeNm(String codeNm);

}
