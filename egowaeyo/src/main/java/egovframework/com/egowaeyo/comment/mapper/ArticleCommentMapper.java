package egovframework.com.egowaeyo.comment.mapper;

import java.util.List;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

import egovframework.com.egowaeyo.comment.VO.Comment;
import egovframework.com.egowaeyo.comment.VO.CommentVO;

@Configuration
@MapperScan(basePackages = "egovframework.com.egowaeyo.comment.mapper")
public interface ArticleCommentMapper {
	// 댓글 목록 조회
    List<CommentVO> selectArticleCommList(CommentVO commentVO);

    // 댓글 목록 개수 조회
    int selectArticleCommListCnt(CommentVO commentVO);

    // 댓글 상세 조회
    CommentVO selectArticleCommDetail(CommentVO commentVO);

    // 댓글 삽입
    void insertArticleComm(Comment comment);

    // 댓글 삭제 (사용 여부 변경)
    void deleteArticleComm(CommentVO commentVO);

    // 댓글 수정
    void updateArticleComm(Comment comment);
    
    // 댓글 사용 여부 확인
    String selectCommentUsage(String bbsId);
}
