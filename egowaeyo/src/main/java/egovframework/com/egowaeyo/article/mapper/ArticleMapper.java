package egovframework.com.egowaeyo.article.mapper;

import java.util.List;
import java.util.Map;

import egovframework.com.cop.bbs.service.Board;
import egovframework.com.egowaeyo.article.VO.BoardVO;

public interface ArticleMapper {

	BoardVO selectArticleDetail(BoardVO boardVO);

	int updateArticle(BoardVO vo);

	List<BoardVO> selectBbsAll(BoardVO boardVO);

	List<Map<String, String>> selectBbsFilter();

	public int articleInsert(BoardVO vo);

	Long selectMaxNttId(); // 게시글 최대 NTT_ID 조회

	int deleteArticle(BoardVO boardVO); // 게시글 삭제
	
	List<Map<String, Object>> selectFilterList(Map<String, Object> params);
	
	int updateArticleRdcnt(BoardVO boardVO); // 조회수 업데이트

}
