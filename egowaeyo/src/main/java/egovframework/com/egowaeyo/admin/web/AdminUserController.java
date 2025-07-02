package egovframework.com.egowaeyo.admin.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdminUserController {
	
	@RequestMapping("/tests.do")
	public String pagetest() {
		return "pinggu/adminDeptManagement.html";
	}

}
