package egovframework.com.egowaeyo.bbsMaster.mapper;

import java.util.List;

import egovframework.com.egowaeyo.article.VO.BoardMasterVO;

public interface BbsMasterMapper {
	List<BoardMasterVO> slectComtccmmn();
	List<BoardMasterVO> selectBbsMasterList();
	public int insertBBSMasterInf(BoardMasterVO boardMaster);
	public int insertBBSMasterAuth(BoardMasterVO auth);
	
	}
