package egovframework.com.egowaeyo.article.mapper;

import java.util.List;
import java.util.Map;

import egovframework.com.egowaeyo.article.VO.BoardVO;

public interface ArticleMapper {

	Map<String, Object> selectArticleDetail(BoardVO boardVO);

	List<BoardVO> selectBbsAll(BoardVO boardVO);

	public int articleInsert(BoardVO vo);

}
