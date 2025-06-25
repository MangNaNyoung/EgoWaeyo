package egovframework.com.egowaeyo.article.service;

import java.util.List;

import egovframework.com.egowaeyo.article.VO.BoardVO;

public interface ArticleService {
	
	public List<BoardVO> findAll(BoardVO vo);
	public int articleInsert(BoardVO vo);

	

}
