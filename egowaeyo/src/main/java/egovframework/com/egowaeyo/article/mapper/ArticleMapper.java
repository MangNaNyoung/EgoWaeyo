package egovframework.com.egowaeyo.article.mapper;

import java.util.Map;

import egovframework.com.egowaeyo.article.VO.BoardVO;

public interface ArticleMapper {
	
	Map<String, Object> selectBbsAll(BoardVO boardVO);
	public int articleInsert(BoardVO vo);

}
