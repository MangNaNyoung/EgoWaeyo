package egovframework.com.egowaeyo.article.service;

import java.util.Map;

import egovframework.com.egowaeyo.article.VO.BoardVO;

public interface ArticleService {

	Map<String, Object> selectArticleDetail(BoardVO boardVO);

	Map<String, Object> selectBbsAll(BoardVO boardVO);

	public int articleInsert(BoardVO vo);

}
