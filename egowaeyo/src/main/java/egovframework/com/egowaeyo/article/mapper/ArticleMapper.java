package egovframework.com.egowaeyo.article.mapper;

import java.util.List;
import java.util.Map;

import egovframework.com.cop.bbs.service.Board;
import egovframework.com.egowaeyo.article.VO.BoardVO;

public interface ArticleMapper {

	BoardVO selectArticleDetail(BoardVO boardVO);
	
	void updateArticle(Board board);
	
	List<BoardVO> selectBbsAll(BoardVO boardVO);

	public int articleInsert(BoardVO vo);

}
