package egovframework.com.egowaeyo.article.serviceImpl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.com.egowaeyo.article.VO.BoardVO;
import egovframework.com.egowaeyo.article.mapper.ArticleMapper;
import egovframework.com.egowaeyo.article.service.ArticleService;

@Service
public class ArticleServiceImpl implements ArticleService {

	@Autowired
	ArticleMapper articleMapper;

	@Override
	public int articleInsert(BoardVO vo) {
		return articleMapper.articleInsert(vo);
	}

	@Override
	public List<BoardVO> selectBbsAll(BoardVO boardVO) {
	    return articleMapper.selectBbsAll(boardVO);
	}
	@Override
	public Map<String, Object> selectArticleDetail(BoardVO boardVO) {

		return articleMapper.selectArticleDetail(boardVO);
	}

}
