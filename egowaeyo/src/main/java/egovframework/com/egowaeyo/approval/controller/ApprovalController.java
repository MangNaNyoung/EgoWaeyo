package egovframework.com.egowaeyo.approval.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.util.EgovUserDetailsHelper;
import egovframework.com.cop.smt.lsm.service.EmplyrVO;
import egovframework.com.egowaeyo.admin.service.AdminUserVO;
import egovframework.com.egowaeyo.admin.service.EgovDeptVO;
import egovframework.com.egowaeyo.approval.VO.ApprovalCcVO;
import egovframework.com.egowaeyo.approval.VO.ApprovalDetailVO;
import egovframework.com.egowaeyo.approval.VO.ApprovalDocVO;
import egovframework.com.egowaeyo.approval.VO.ApprovalLineVO;
import egovframework.com.egowaeyo.approval.VO.ApprovalTempVO;
import egovframework.com.egowaeyo.approval.service.ApprovalService;

@Controller
@RequestMapping("/approval")
public class ApprovalController {

	@Autowired
	private ApprovalService approvalService;

	@GetMapping("/write")
	public String writeForm(Model model, HttpServletRequest req) {
		AdminUserVO sessionUser = (AdminUserVO) req.getSession().getAttribute("loginUser");
		if (sessionUser == null) {
			sessionUser = new AdminUserVO();
			sessionUser.setUserNm("알수없음");
		}
		LoginVO loginUser = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		model.addAttribute("loginUser", loginUser);
		model.addAttribute("formList", approvalService.getFormList());

		return "approval/write.html";
	}

	@PostMapping("/write.do")
	@Transactional
	public String submitDoc(@RequestParam String docTitle, @RequestParam String apprformId,
			@RequestParam String docContent, @RequestParam List<String> approverIds,
			@RequestParam(required = false) List<String> ccIds, Principal principal) {
		LoginVO user = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		// 1. 결재문서 정보 생성
		ApprovalDocVO docVO = new ApprovalDocVO();
		docVO.setDocTitle(docTitle);
		docVO.setApprformId(apprformId);
		docVO.setDocStatus("대기");
		docVO.setEmplId(user.getId());
		docVO.setDocHtml(docContent);

		// 2. 결재선 정보 생성
		List<ApprovalLineVO> lineList = new ArrayList<>();
		int order = 1;
		for (String approverId : approverIds) {
			ApprovalLineVO lineVO = new ApprovalLineVO();
			lineVO.setApproverId(approverId);
			lineVO.setLineOrder(order++);
			lineVO.setLineStatus("대기");
			lineList.add(lineVO);
		}
		// 3. 참조(수신)자 정보 생성
		List<ApprovalCcVO> ccList = new ArrayList<>();
		if (ccIds != null) {
			for (String ccId : ccIds) {
				ApprovalCcVO ccVO = new ApprovalCcVO();
				ccVO.setEmpId(ccId);
				ccList.add(ccVO);
			}
		}
		// 4. 서비스 호출 (트랜잭션 처리)
		approvalService.insertFullApproval(docVO, lineList, ccList);

		return "redirect:/approval/receive";
	}

	// 개인 수신함 (receive.html)
	@GetMapping("/receive")
	public String receivePage() {
		return "approval/receive.html";
	}

	@GetMapping("/receive/list")
	@ResponseBody
	public List<ApprovalDocVO> selectReceiveList(HttpServletRequest req) {
		LoginVO user = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String empId = user.getId();
		return approvalService.selectReceiveList(empId);
	}

	// 부서 수신함 (deptReceive.html)
	@GetMapping("/deptReceive")
	public String deptReceivePage() {
		return "approval/deptReceive.html";
	}

	@GetMapping("/deptReceive/list")
	@ResponseBody
	public List<ApprovalDocVO> deptReceiveList(HttpSession session) {
		String deptId = (String) session.getAttribute("deptId");
		return approvalService.getDeptReceiveList(deptId);
	}

	// 임시함 (temp.html)
	@GetMapping("/temp")
	public String tempBox() {
		return "approval/temp.html";
	}

	@GetMapping("/temp/list")
	@ResponseBody
	public List<ApprovalTempVO> tempList(HttpSession session) {
		LoginVO user = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String empId = user.getId();
		return approvalService.getTempList(empId);
	}

	@PostMapping("/tempSave.do")
	@ResponseBody
	public String saveTemp(@RequestParam("tempTitle") String tempTitle,
			@RequestParam("tempContent") String tempContent) {
		LoginVO user = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
	    ApprovalTempVO vo = new ApprovalTempVO();
	    vo.setEmpId(user.getId());
	    vo.setTempTitle(tempTitle);
	    vo.setTempContent(tempContent);
	    vo.setTempStatus("임시");
	    approvalService.getSaveTemp(vo);
	    return "OK";
	}


	@PostMapping("/temp/delete")
	@ResponseBody
	public String deleteTempDocs(@RequestBody List<String> tempIds) {
	    approvalService.deleteTempDocs(tempIds);
	    return "OK";
	}

	// 참조함 (reference.html)
	@GetMapping("/reference")
	public String referencePage() {
		return "approval/reference.html";
	}

	@GetMapping("/reference/list")
	@ResponseBody
	public List<ApprovalCcVO> referenceList(HttpSession session) {
		LoginVO user = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String empId = user.getId();
		return approvalService.getReferenceList(empId);
	}

	// 프린팅
	@GetMapping("/print/{docId}")
	public String printApproval(@PathVariable String docId, Model model) {
		ApprovalDetailVO detail = approvalService.getApprovalDetailForPrint(docId);
		if (detail.getDocHtml() != null) {
			detail.setDocHtml(StringEscapeUtils.unescapeHtml4(detail.getDocHtml()));
		}
		model.addAttribute("detail", detail);
		return "approval/print.html";
	}

	@GetMapping("/form/{formId}")
	@ResponseBody
	public String getFormHtml(@PathVariable String formId) {
		return approvalService.getFormHtml(formId);
	}

	// 부서 리스트 반환
	@GetMapping("/dept")
	@ResponseBody
	public List<EgovDeptVO> getDeptList() {
		return approvalService.getDeptList();
	}

	// 부서별 사용자 목록 조회 (JSON 반환)
	@GetMapping("/dept/{deptId}")
	@ResponseBody
	public List<EmplyrVO> getEmpListByDept(@PathVariable String deptId) {
		return approvalService.getEmpListByDept(deptId);
	}

	@GetMapping("/approval/users")
	@ResponseBody
	public List<EmplyrVO> getAllUsers() {
		return approvalService.getAllUsers();
	}

	@GetMapping("/detail")
	public String detail(@RequestParam("docId") String docId, Model model) {
		ApprovalDocVO doc = approvalService.getApprovalDetailForView(docId);
		if (doc.getDocHtml() != null) {
			doc.setDocHtml(StringEscapeUtils.unescapeHtml4(doc.getDocHtml()));
		}
		model.addAttribute("doc", doc);
		return "approval/detail.html";
	}

	@PostMapping("/approve.do")
	@ResponseBody
	public String approve(@RequestBody Map<String, Object> param, Principal principal) {
		String docId = (String) param.get("docId");
		String opinion = (String) param.get("opinion");
		String loginId = principal.getName(); // 현재 로그인 결재자 ID
		approvalService.approve(docId, loginId, opinion);
		return "OK";
	}

	@PostMapping("/reject.do")
	@ResponseBody
	public String reject(@RequestBody Map<String, Object> param, Principal principal) {
		String docId = (String) param.get("docId");
		String opinion = (String) param.get("opinion");
		String loginId = principal.getName();
		approvalService.reject(docId, loginId, opinion);
		return "OK";
	}

	// 진행함
	@GetMapping("/progress")
	public String progress() {
		return "approval/progress.html";
	}

	@GetMapping("/progress/list")
	@ResponseBody
	public List<ApprovalDocVO> progressList(Principal principal) {
		return approvalService.selectProgressList(principal.getName());
	}
	
	@GetMapping("/progressDetail")
	public String progressDetail(@RequestParam("docId") String docId, Model model) {
		ApprovalDocVO doc = approvalService.getProgressDetail(docId);
		if (doc.getDocHtml() != null) {
			doc.setDocHtml(StringEscapeUtils.unescapeHtml4(doc.getDocHtml()));
		}
		model.addAttribute("doc", doc);
	    return "approval/progressDetail.html";
	}

	// 반려함
	@GetMapping("/reject")
	public String reject() {
		return "approval/reject.html";
	}

	@GetMapping("/reject/list")
	@ResponseBody
	public List<ApprovalDocVO> rejectList(Principal principal) {
		return approvalService.selectRejectList(principal.getName());
	}
	
	@GetMapping("/rejectDetail")
	public String rejectDetail(@RequestParam("docId") String docId, Model model) {
		ApprovalDocVO doc = approvalService.getRejectDetail(docId);
		if (doc.getDocHtml() != null) {
			doc.setDocHtml(StringEscapeUtils.unescapeHtml4(doc.getDocHtml()));
		}
		model.addAttribute("doc", doc);
	    return "approval/rejectDetail.html";
	}
	
	@PostMapping("/rejectDelete")
	@ResponseBody
	public String rejectDelete(@RequestBody Map<String, String> map) {
	    String docId = map.get("docId");
	    approvalService.getDeleteApproval(docId);
	    return "success";
	}

}