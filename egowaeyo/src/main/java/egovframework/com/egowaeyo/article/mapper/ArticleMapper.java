package egovframework.com.egowaeyo.article.mapper;

import java.util.List;

import egovframework.com.egowaeyo.article.VO.BoardVO;

public interface ArticleMapper {
	
	public List<BoardVO> findAll(BoardVO vo);
	public int articleInsert(BoardVO vo);

}
