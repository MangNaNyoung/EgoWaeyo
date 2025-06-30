package egovframework.com.egowaeyo.bbsMaster.service;

import java.util.List;
import java.util.Map;

import egovframework.com.egowaeyo.bbsMaster.VO.BoardMasterVO;

public interface BbsMasterService {
	
	String getMaxBbsTyCode();

	void insertCommonDetailCode(String bbsTyCode, String bbsName, String useAt);

	public int insertBBSMaster(BoardMasterVO boardMaster);

	public int insertBBSMasterAuth(BoardMasterVO auth);
	
	// 모달
	List<Map<String, String>> getCombbs();
	List<BoardMasterVO> getDeptEmp();

	// 사이드바
	Map<String, List<String>> getGroupedBbsData();
}
