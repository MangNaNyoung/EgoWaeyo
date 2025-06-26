package egovframework.com.egowaeyo.bbsMaster.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import egovframework.com.egowaeyo.article.VO.BoardMasterVO;
import egovframework.com.egowaeyo.bbsMaster.mapper.BbsMasterMapper;
import egovframework.com.egowaeyo.bbsMaster.service.BbsMasterService;
@Transactional
@Service
public class BbsMasterServiceImpl implements BbsMasterService{

	@Autowired BbsMasterMapper bbsMasterMapper;
	
	@Override
	public List<BoardMasterVO> slectComtccmmn() {

		return bbsMasterMapper.slectComtccmmn();
	}
	
	@Override
	public List<BoardMasterVO> selectBbsMasterList() {

		return bbsMasterMapper.selectBbsMasterList();
	}
	
	@Override
	public int insertBBSMasterInf(BoardMasterVO boardMaster) {
		BoardMasterVO auth = new BoardMasterVO();
		bbsMasterMapper.insertBBSMasterInf(boardMaster);
		bbsMasterMapper.insertBBSMasterAuth(auth);
		return 0;
	}

	@Override
	public int insertBBSMasterAuth(BoardMasterVO auth) {

		return 0;
	}

}
