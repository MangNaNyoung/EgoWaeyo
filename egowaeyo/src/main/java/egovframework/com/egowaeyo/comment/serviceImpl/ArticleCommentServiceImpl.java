
package egovframework.com.egowaeyo.comment.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.cmmn.exception.FdlException;
import org.egovframe.rte.fdl.idgnr.EgovIdGnrService;
import org.springframework.stereotype.Service;

import egovframework.com.egowaeyo.comment.VO.Comment;
import egovframework.com.egowaeyo.comment.VO.CommentVO;
import egovframework.com.egowaeyo.comment.mapper.ArticleCommentMapper;
import egovframework.com.egowaeyo.comment.service.ArticleCommentService;

@Service("ArticleCommentService")
public class ArticleCommentServiceImpl implements ArticleCommentService {

    @Resource
    private ArticleCommentMapper articleCommentMapper;

    @Resource
    private EgovIdGnrService egovAnswerNoGnrService;

    @Override
    public boolean canUseComm(String bbsId) throws Exception {
        // 댓글 사용 가능 여부 확인 로직
        String usage = articleCommentMapper.selectCommentUsage(bbsId);
        return "Y".equals(usage);
    }

    @Override
    public Map<String, Object> selectArticleCommList(CommentVO commentVO) {
        List<CommentVO> result = articleCommentMapper.selectArticleCommList(commentVO);
        int cnt = articleCommentMapper.selectArticleCommListCnt(commentVO);

        Map<String, Object> map = new HashMap<>();
        map.put("resultList", result);
        map.put("resultCnt", cnt);

        // 디버깅 로그 추가
        System.out.println("댓글 목록: " + result);
        System.out.println("댓글 개수: " + cnt);
        
        return map;
    }

    @Override
    public void insertArticleComm(Comment comment) throws FdlException {
        comment.setCommentNo(String.valueOf(egovAnswerNoGnrService.getNextLongId()));
        articleCommentMapper.insertArticleComm(comment);
    }

    @Override
    public void deleteArticleComm(CommentVO commentVO) {
        articleCommentMapper.deleteArticleComm(commentVO);
    }

    @Override
    public CommentVO selectArticleCommDetail(CommentVO commentVO) {
        return articleCommentMapper.selectArticleCommDetail(commentVO);
    }

    @Override
    public void updateArticleComm(Comment comment) {
        articleCommentMapper.updateArticleComm(comment);
    }
}
