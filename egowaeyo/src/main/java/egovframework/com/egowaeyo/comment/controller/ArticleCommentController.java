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

import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.util.EgovUserDetailsHelper;
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

		if (commentVO.getSubRecordCountPerPage() == 0) {
			commentVO.setSubRecordCountPerPage(10); // 한 페이지에 보여줄 댓글 수 기본 10개
		}
		int pageIndex = commentVO.getSubPageIndex();
		if (pageIndex < 0) {
			pageIndex = 0;
			commentVO.setSubPageIndex(pageIndex);
		}
		commentVO.setSubFirstIndex(pageIndex * commentVO.getSubRecordCountPerPage());

		Map<String, Object> result = articleCommentService.selectArticleCommList(commentVO);
		return ResponseEntity.ok(result);
	}

	@PostMapping("/add")
	public ResponseEntity<String> addComment(@RequestBody Comment comment) {
		try {
			System.out.println("등록 요청 comment : " + comment);

			// 현재 로그인 사용자 정보 가져오기
			LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();

			if (loginVO == null || loginVO.getId() == null) {
				return ResponseEntity.status(401).body("로그인한 사용자만 댓글을 작성할 수 있습니다.");
			}

			// comment 객체에 사용자 정보 세팅
			comment.setFrstRegisterId(loginVO.getId()); // 등록자 ID
			comment.setWrterId(loginVO.getId()); // WRTER_ID
			comment.setWrterNm(loginVO.getName()); // WRTER_NM

			articleCommentService.insertArticleComm(comment);

			return ResponseEntity.ok("댓글이 등록되었습니다.");
		} catch (Exception e) {
			System.err.println("댓글 등록 중 예외 발생:");
			e.printStackTrace();
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
