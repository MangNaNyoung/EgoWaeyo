package egovframework.com.egowaeyo.article.serviceImpl;

import java.util.List;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.com.egowaeyo.article.VO.BoardVO;
import egovframework.com.egowaeyo.article.mapper.ArticleMapper;
import egovframework.com.egowaeyo.article.service.ArticleService;

@Service
public class ArticleServiceImpl extends EgovAbstractServiceImpl implements ArticleService {

	@Autowired ArticleMapper articleMapper;
	
	@Override
	public List<BoardVO> findAll(BoardVO vo) {
		System.out.println("여기도 안오지?");
        return articleMapper.findAll(vo);
	}



}
