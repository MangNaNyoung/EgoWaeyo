package egovframework.com.egowaeyo.article.service;

import java.util.List;
import java.util.Map;

import egovframework.com.cop.bbs.service.Board;
import egovframework.com.egowaeyo.article.VO.BoardVO;

public interface ArticleService {

	BoardVO selectArticleDetail(BoardVO boardVO);

	int updateArticle(BoardVO vo);

	List<BoardVO> selectBbsAll(BoardVO boardVO);

	public int articleInsert(BoardVO vo);

	Long selectMaxNttId(); // 게시글 최대 NTT_ID 조회

	int deleteArticle(BoardVO boardVO);

	List<Map<String, String>> getBbsFilter();
	
	List<Map<String, Object>> filterArticles(Map<String, Object> params);
	
	int updateArticleRdcnt(BoardVO boardVO); //조회수

}
