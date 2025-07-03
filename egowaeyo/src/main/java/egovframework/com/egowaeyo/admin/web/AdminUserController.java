package egovframework.com.egowaeyo.admin.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.com.egowaeyo.admin.service.AdminUserService;
import egovframework.com.egowaeyo.admin.service.DeptVO;
import egovframework.com.egowaeyo.admin.service.PosVO;


@Controller
public class AdminUserController {
	
	@Autowired AdminUserService adminuserservice;
	
	@RequestMapping("/adUserIns.do")
	public String adminUserIns(Model model) {
		model.addAttribute("posi", adminuserservice.getPos(null));
		return "pinggu/adminUserInsert.html";
	}
	
	@GetMapping("/deptlist")
	@ResponseBody
	public List<DeptVO> getMethodName() {
		return adminuserservice.getDept(null);
	}
	
	@GetMapping("/deptpos")
	@ResponseBody
	public List<PosVO> getPositionList() {
		return adminuserservice.getPos(null);
	}
	
	
	
	

}
