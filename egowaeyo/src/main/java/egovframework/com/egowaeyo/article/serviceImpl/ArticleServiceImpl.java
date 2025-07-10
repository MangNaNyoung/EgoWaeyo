package egovframework.com.egowaeyo.article.serviceImpl;

import java.sql.Clob;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.com.egowaeyo.article.VO.BoardVO;
import egovframework.com.egowaeyo.article.mapper.ArticleMapper;
import egovframework.com.egowaeyo.article.service.ArticleService;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class ArticleServiceImpl implements ArticleService {

	@Autowired
	ArticleMapper articleMapper;

	@Override
	public int articleInsert(BoardVO vo) {
		return articleMapper.articleInsert(vo);
	}

	@Override
	public List<BoardVO> selectBbsAll(BoardVO boardVO) {
		return articleMapper.selectBbsAll(boardVO);
	}

	@Override
	public BoardVO selectArticleDetail(BoardVO boardVO) {
		log.info("Fetching article detail for bbsId: {}, nttId: {}", boardVO.getBbsId(), boardVO.getNttId());
		return articleMapper.selectArticleDetail(boardVO);
	}

	@Override
	public int updateArticle(BoardVO vo) {
		try {
	        // 데이터베이스 업데이트 로직 추가
	        return articleMapper.updateArticle(vo);
	    } catch (Exception e) {
	        log.error("게시글 업데이트 중 오류 발생: {}", e.getMessage(), e);
	        throw new RuntimeException("게시글 업데이트에 실패했습니다.", e);
	    }
	}

	@Override
	public Long selectMaxNttId() {
		try {
			return articleMapper.selectMaxNttId();
		} catch (Exception e) {
			log.error("최대 NTT_ID 조회 중 오류 발생:", e);
			throw new RuntimeException("최대 NTT_ID 조회에 실패했습니다.", e);
		}
	}

	@Override
	public int deleteArticle(BoardVO boardVO) {
		log.info("Deleting article with BBS_ID: {}, NTT_ID: {}", boardVO.getBbsId(), boardVO.getNttId());
		return articleMapper.deleteArticle(boardVO);
	}

	@Override
	public List<Map<String, String>> getBbsFilter() {
		return articleMapper.selectBbsFilter();
	}
	
	@Override
	public List<Map<String, Object>> filterArticles(Map<String, Object> params) {
	    List<Map<String, Object>> articles = articleMapper.selectFilterList(params);

	    for (Map<String, Object> article : articles) {
	        Object nttCn = article.get("NTT_CN");
	        if (nttCn instanceof Clob) {
	            try {
	                Clob clob = (Clob) nttCn;
	                article.put("NTT_CN", clob.getSubString(1, (int) clob.length()));
	            } catch (SQLException e) {
	                log.error("Error converting CLOB:", e);
	                article.put("NTT_CN", null);
	            }
	        }
	    }
	    log.debug("Filter parameters: {}", params);
	    return articles;
	}
	
	// 게시글 조회수 증가
	@Override
	public int updateArticleRdcnt(BoardVO boardVO) {
	    try {
	        return articleMapper.updateArticleRdcnt(boardVO);
	    } catch (Exception e) {
	        log.error("조회수 증가 중 오류 발생: {}", e.getMessage());
	        throw new RuntimeException("조회수 증가에 실패했습니다.", e);
	    }
	}
}
