package egovframework.com.egowaeyo.admin.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.com.egowaeyo.admin.service.AdminUserService;
import egovframework.com.egowaeyo.admin.service.AdminUserVO;
import egovframework.com.egowaeyo.admin.service.DeptVO;
import egovframework.com.egowaeyo.admin.service.PosVO;

@Controller
public class AdminUserController {
	
	@Autowired AdminUserService adminuserservice;
	
	@GetMapping("/adUserIns.do")
	public String adminUserIns(Model model) {
	   
        List<PosVO> posilist = adminuserservice.getPos(null);
        System.out.println("Position list size: " + (posilist != null ? posilist.size() : "null"));
        model.addAttribute("posi", posilist);
       
        return "pinggu/adminUserInsert.html";
	   
	}
	
	@PostMapping("/adUserIns.do")
	public String adminUserIns(AdminUserVO adu) {
		adminuserservice.AdminUserIns(adu);
		return "redirect:adUserIns.do";
	}
	
	@GetMapping("/deptlist")
	@ResponseBody
	public List<DeptVO> getDeptList() {
		return adminuserservice.getDept(null);
	}
	
	@GetMapping("/deptpos")
	@ResponseBody
	public List<PosVO> getPositionList() {
		return adminuserservice.getPos(null);
	}
	
	@RequestMapping("/adDeptMge.do")
	public String adminDeptMge() {
       
        return "pinggu/adminDeptManagement.html";
	   
	}
	
	@RequestMapping("/adUserMge.do")
	public String adminUserMge() {
       
        return "pinggu/adminUserManagement.html";
	   
	}
	
	@RequestMapping("/adMenuAuth.do")
	public String adminMenuAuthority() {
       
        return "pinggu/adminMenuAuthority.html";
	   
	}
	

}
