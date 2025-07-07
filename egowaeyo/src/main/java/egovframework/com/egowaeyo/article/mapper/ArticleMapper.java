package egovframework.com.egowaeyo.article.mapper;

import java.util.List;

import egovframework.com.cop.bbs.service.Board;
import egovframework.com.egowaeyo.article.VO.BoardVO;

public interface ArticleMapper {

	BoardVO selectArticleDetail(BoardVO boardVO);

	int updateArticle(Board board);

	List<BoardVO> selectBbsAll(BoardVO boardVO);

	public int articleInsert(BoardVO vo);

	Long selectMaxNttId(); // 게시글 최대 NTT_ID 조회
	
	int deleteArticle(BoardVO boardVO); // 게시글 삭제

}
