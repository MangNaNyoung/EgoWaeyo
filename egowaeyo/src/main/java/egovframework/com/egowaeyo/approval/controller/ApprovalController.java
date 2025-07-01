package egovframework.com.egowaeyo.approval.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import egovframework.com.egowaeyo.approval.VO.ApprovalBoxVO;
import egovframework.com.egowaeyo.approval.VO.ApprovalDocVO;
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
    public String submitDoc(@RequestParam String docTitle,
                            @RequestParam String apprformId,
                            @RequestParam String docContent) {
        ApprovalDocVO vo = new ApprovalDocVO();
        vo.setDocTitle(docTitle);
        vo.setApprformId(apprformId);
        vo.setDocStatus("작성중");
        vo.setEmplId("EMPL001"); // 로그인 사용자 세션에서 추출 예정
        vo.setCreatedDt(new Date());
        vo.setDocHtml(docContent);
        approvalService.insertApprovalDoc(vo);
        return "redirect:/approval/list";
    }
    
 // 목록 조회
    @GetMapping("/box")
    public String boxList(Model model, @SessionAttribute("loginEmpId") String empId) {
        List<ApprovalBoxVO> list = approvalService.selectBoxListByUser(empId);
        model.addAttribute("boxList", list);
        return "approval/box.html";
    }

    // 문서함 클릭 시 읽음 처리 예시
    @PostMapping("/box/read")
    @ResponseBody
    public String setBoxRead(@RequestParam int boxId) {
        approvalService.updateBoxRead(boxId);
        return "ok";
    }
}