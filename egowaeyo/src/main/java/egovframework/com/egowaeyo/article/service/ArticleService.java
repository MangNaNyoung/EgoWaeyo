package egovframework.com.egowaeyo.article.service;

import java.util.List;
import java.util.Map;

import egovframework.com.cop.bbs.service.Board;
import egovframework.com.egowaeyo.article.VO.BoardVO;

public interface ArticleService {

	BoardVO selectArticleDetail(BoardVO boardVO);
	
	void updateArticle(Board board);

	List<BoardVO> selectBbsAll(BoardVO boardVO);

	public int articleInsert(BoardVO vo);

}
