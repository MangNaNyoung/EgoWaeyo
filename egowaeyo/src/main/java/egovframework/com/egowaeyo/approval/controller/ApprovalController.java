package egovframework.com.egowaeyo.approval.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.com.egowaeyo.approval.VO.ApprovalCcVO;
import egovframework.com.egowaeyo.approval.VO.ApprovalDocVO;
import egovframework.com.egowaeyo.approval.VO.ApprovalTempVO;
import egovframework.com.egowaeyo.approval.service.ApprovalService;

@Controller
@RequestMapping("/approval")
public class ApprovalController {

	@Autowired
	private ApprovalService approvalService;

	@GetMapping("/write")
	public String writeForm(Model model) {
		model.addAttribute("formList", approvalService.getFormList());
		return "approval/write.html";
	}

	@PostMapping("/write")
	public String submitDoc(@RequestParam String docTitle, @RequestParam String apprformId,
			@RequestParam String docContent, HttpSession session) {
		ApprovalDocVO vo = new ApprovalDocVO();
		vo.setDocTitle(docTitle);
		vo.setApprformId(apprformId);
		vo.setDocStatus("작성중");
		vo.setEmplId((String) session.getAttribute("loginId"));
		vo.setCreatedDt(new Date());
		vo.setDocHtml(docContent);
		approvalService.insertApprovalDoc(vo);
		return "redirect:/approval/list";
	}

	// 개인 수신함 (receive.html)
	@GetMapping("/receive")
	public String receivePage() {
		return "approval/receive.html";
	}

	@GetMapping("/receive/list")
	@ResponseBody
	public List<ApprovalDocVO> receiveList(HttpSession session) {
		String empId = (String) session.getAttribute("loginId");
		return approvalService.getReceiveList(empId);
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
		String empId = (String) session.getAttribute("loginId");
		return approvalService.getTempList(empId);
	}
	
	// 참조함 (reference.html)
	@GetMapping("/reference")
	public String referencePage() {
	    return "approval/reference.html";
	}

	@GetMapping("/reference/list")
	@ResponseBody
	public List<ApprovalCcVO> referenceList(HttpSession session) {
	    String empId = (String) session.getAttribute("loginId");
	    return approvalService.getReferenceList(empId);
	}

}