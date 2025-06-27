package egovframework.com.egowaeyo.article.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.com.egowaeyo.article.VO.BoardVO;
import egovframework.com.egowaeyo.article.mapper.ArticleMapper;
import egovframework.com.egowaeyo.article.service.ArticleService;

@Service
public class ArticleServiceImpl implements ArticleService {

	@Autowired ArticleMapper articleMapper;
	
	@Override
	public List<BoardVO> findAll(BoardVO vo) {
        return articleMapper.findAll(vo);
	}

	@Override
	public int articleInsert(BoardVO vo) {
		return articleMapper.articleInsert(vo);
	}

	


}
