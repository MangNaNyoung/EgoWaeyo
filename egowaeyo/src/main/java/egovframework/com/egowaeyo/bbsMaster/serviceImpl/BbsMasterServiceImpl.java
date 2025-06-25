package egovframework.com.egowaeyo.bbsMaster.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.com.egowaeyo.article.VO.BoardMasterVO;
import egovframework.com.egowaeyo.bbsMaster.mapper.BbsMasterMapper;
import egovframework.com.egowaeyo.bbsMaster.service.BbsMasterService;

@Service
public class BbsMasterServiceImpl implements BbsMasterService{

	@Autowired BbsMasterMapper bbsMasterMapper;
	
	@Override
	public List<BoardMasterVO> selectBbsMasterList() {

		return bbsMasterMapper.selectBbsMasterList();
	}

}
