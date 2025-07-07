package egovframework.com.egowaeyo.article.service;

import java.util.List;

import egovframework.com.cop.bbs.service.Board;
import egovframework.com.egowaeyo.article.VO.BoardVO;

public interface ArticleService {

	BoardVO selectArticleDetail(BoardVO boardVO);

	int updateArticle(Board board);

	List<BoardVO> selectBbsAll(BoardVO boardVO);

	public int articleInsert(BoardVO vo);

	Long selectMaxNttId(); // 게시글 최대 NTT_ID 조회

}
