package egovframework.com.egowaeyo.approval.controller;

import java.security.Principal;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.com.cop.smt.lsm.service.EmplyrVO;
import egovframework.com.egowaeyo.admin.service.DeptVO;
import egovframework.com.egowaeyo.approval.VO.ApprovalCcVO;
import egovframework.com.egowaeyo.approval.VO.ApprovalDetailVO;
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
			@RequestParam String docContent, HttpSession session, Principal principal) {
		ApprovalDocVO vo = new ApprovalDocVO();
		vo.setDocTitle(docTitle);
		vo.setApprformId(apprformId);
		vo.setDocStatus("작성중");
		vo.setEmplId(principal.getName());
		vo.setCreatedDt(new Date());
		vo.setDocHtml(docContent);
		approvalService.insertApprovalDoc(vo);
		return "redirect:/approval/receive.html";
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
	
	//프린팅 
	@GetMapping("/approval/print/{docId}")
	public String printApproval(@PathVariable String docId, Model model) {
	    ApprovalDetailVO detail = approvalService.getApprovalDetail(docId);
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
    public List<DeptVO> getDeptList() {
        return approvalService.getDeptList();
    }
    
	// 부서별 사용자 목록 조회 (JSON 반환)
    @GetMapping("/dept/{deptId}")
    public List<EmplyrVO> getEmpListByDept(@PathVariable String deptId) {
        return approvalService.getEmpListByDept(deptId);
    }
    
    @GetMapping("/approval/users")
    @ResponseBody
    public List<EmplyrVO> getAllUsers() {
        return approvalService.getAllUsers();
    }

}