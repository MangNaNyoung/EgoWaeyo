package egovframework.com.egowaeyo.bbsMaster.service;

import java.util.List;
import java.util.Map;

import egovframework.com.egowaeyo.bbsMaster.VO.BoardMasterVO;

public interface BbsMasterService {

	// 게시판 저장
    void saveBoard(String boardName, String boardType, String parentBoard, String useAt,
                   List<Map<String, String>> selectedRights, String currentUserId);

    // 최대 게시판 유형 코드 가져오기
    String getMaxBbsTyCode();

    // 공통 코드 삽입
    void insertCommonDetailCode(String bbsTyCode, String bbsName, String useAt, String currentUserId);

    // 새로운 게시판 ID 생성
    String getNextStringId();

    // 게시판 마스터 정보 삽입
    int insertBBSMaster(BoardMasterVO boardMaster);

    // 게시판 권한 정보 삽입
    int insertBBSMasterAuth(BoardMasterVO auth);
    
    // 게시판 권한 일괄 삽입
    void saveBoardAuth(List<Map<String, String>> selectedRights, String bbsId);

    // 게시판 권한 일괄 삽입 (insertBoardAuth 사용)
    void insertBoardAuth(List<Map<String, String>> selectedRights, String bbsId);

    // 게시판 모달 데이터 가져오기
    List<Map<String, String>> getCombbs();

    // 부서별 직원 정보 가져오기
    List<BoardMasterVO> getDeptEmp();

    // 그룹별 게시판 데이터 가져오기
    Map<String, List<Map<String, String>>> getGroupedBbsData();

	void insertCommonDetailCode(Map<String, Object> params);

	String selectCodeByCodeNm(String codeNm);
	
	List<BoardMasterVO> getBbsMasterInfo(String bbsId);
	
	void updateBBSMaster(BoardMasterVO vo) throws Exception;
	
	void updateBoardWithAuth(BoardMasterVO board, List<BoardMasterVO> authList);
    
    void updateBoardAuth(List<Map<String, String>> selectedRights, String bbsId);

}
