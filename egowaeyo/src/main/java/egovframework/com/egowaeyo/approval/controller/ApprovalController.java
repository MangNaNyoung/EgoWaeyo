package egovframework.com.egowaeyo.approval.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
}