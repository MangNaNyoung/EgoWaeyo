package egovframework.com.egowaeyo.bbsMaster.service;

import java.util.List;

import egovframework.com.egowaeyo.article.VO.BoardMasterVO;

public interface BbsMasterService {
	List<BoardMasterVO> slectComtccmmn();
	List<BoardMasterVO> selectBbsMasterList();
	public int insertBBSMasterInf(BoardMasterVO boardMaster);
	public int insertBBSMasterAuth(BoardMasterVO auth);
}
