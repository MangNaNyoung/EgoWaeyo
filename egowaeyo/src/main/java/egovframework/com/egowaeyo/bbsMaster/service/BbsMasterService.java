package egovframework.com.egowaeyo.bbsMaster.service;

import java.util.List;
import java.util.Map;

import egovframework.com.egowaeyo.bbsMaster.VO.BoardMasterVO;

public interface BbsMasterService {
	
	void saveBoard(String boardName, String boardType, String parentBoard, String useAt, List<Map<String, String>> selectedRights, String currentUserId);
	
	String getMaxBbsTyCode();

	void insertCommonDetailCode(String bbsTyCode, String bbsName, String useAt, String currentUserId);
	String getNextStringId();

	public int insertBBSMaster(BoardMasterVO boardMaster);


	
	// 모달
	List<Map<String, String>> getCombbs();
	List<BoardMasterVO> getDeptEmp();
	void insertCommonDetailCode(Map<String, Object> params);
	String selectCodeByCodeNm(String codeNm);
	public int insertBBSMasterAuth(BoardMasterVO auth);	
	void saveBoardAuth(List<Map<String, String>> selectedRights, String bbsId);

	// 사이드바
	Map<String, List<String>> getGroupedBbsData();
}
