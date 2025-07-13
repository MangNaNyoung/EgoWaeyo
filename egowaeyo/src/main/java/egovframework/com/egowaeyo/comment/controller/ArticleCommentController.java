package egovframework.com.egowaeyo.comment.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import egovframework.com.egowaeyo.comment.VO.Comment;
import egovframework.com.egowaeyo.comment.VO.CommentVO;
import egovframework.com.egowaeyo.comment.service.ArticleCommentService;

@RestController
@RequestMapping("/comment")
public class ArticleCommentController {

	@Resource
	private ArticleCommentService articleCommentService;

	// 댓글 목록 조회
	@GetMapping("/list")
	public ResponseEntity<Map<String, Object>> getCommentList(CommentVO commentVO) {
		System.out.println("댓글 목록 조회 요청: " + commentVO);
		// commentVO.setSubRecordCountPerPage(10);
		// commentVO.setSubFirstIndex(commentVO.getSubPageIndex() *
		// commentVO.getSubRecordCountPerPage());
		Map<String, Object> result = articleCommentService.selectArticleCommList(commentVO);
		return ResponseEntity.ok(result);
	}

	// 댓글 등록
	@PostMapping("/add")
	public ResponseEntity<String> addComment(@RequestBody Comment comment) {
		try {
			articleCommentService.insertArticleComm(comment);
			return ResponseEntity.ok("댓글이 등록되었습니다.");
		} catch (Exception e) {
			return ResponseEntity.status(500).body("댓글 등록에 실패했습니다.");
		}
	}

	// 댓글 수정
	@PutMapping("/update")
	public ResponseEntity<String> updateComment(@RequestBody Comment comment) {
		try {
			articleCommentService.updateArticleComm(comment);
			return ResponseEntity.ok("댓글이 수정되었습니다.");
		} catch (Exception e) {
			return ResponseEntity.status(500).body("댓글 수정에 실패했습니다.");
		}
	}

	// 댓글 삭제
	@DeleteMapping("/delete")
	public ResponseEntity<String> deleteComment(@RequestBody CommentVO commentVO) {
		try {
			articleCommentService.deleteArticleComm(commentVO);
			return ResponseEntity.ok("댓글이 삭제되었습니다.");
		} catch (Exception e) {
			return ResponseEntity.status(500).body("댓글 삭제에 실패했습니다.");
		}
	}
}
