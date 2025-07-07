package egovframework.com.egowaeyo.comment.service;

import java.util.Map;

import org.egovframe.rte.fdl.cmmn.exception.FdlException;

import egovframework.com.egowaeyo.comment.VO.Comment;
import egovframework.com.egowaeyo.comment.VO.CommentVO;

public interface ArticleCommentService {

    public boolean canUseComm(String bbsId) throws Exception;

    Map<String, Object> selectArticleCommList(CommentVO commentVO);

	void insertArticleComm(Comment comment) throws FdlException;

	void deleteArticleComm(CommentVO commentVO);

	CommentVO selectArticleCommDetail(CommentVO commentVO);

	void updateArticleComm(Comment comment);

}
