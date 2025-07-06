package egovframework.com.egowaeyo.article.serviceImpl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.com.cop.bbs.service.Board;
import egovframework.com.egowaeyo.article.VO.BoardVO;
import egovframework.com.egowaeyo.article.mapper.ArticleMapper;
import egovframework.com.egowaeyo.article.service.ArticleService;
import lombok.extern.log4j.Log4j2;

@Log4j2
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
	    public BoardVO selectArticleDetail(BoardVO boardVO) {
	        log.info("Fetching article detail for bbsId: {}, nttId: {}", boardVO.getBbsId(), boardVO.getNttId());
	        return articleMapper.selectArticleDetail(boardVO);
	    }

	@Override
	public void updateArticle(Board board) {
		articleMapper.updateArticle(board);		
	}

}
