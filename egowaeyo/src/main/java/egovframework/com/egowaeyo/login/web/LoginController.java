package egovframework.com.egowaeyo.login.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.com.cmm.LoginVO;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
public class LoginController {
	

	
	/*
	 * 로그인 페이지 이동
	 */
	@RequestMapping("/goToLogin")
	public String goToLogin(@ModelAttribute("loginVO")LoginVO loginVO) {
		return "login/login.html";
	}
	
	@RequestMapping("/tests")
	public String pagetest() {
		return "pinggu/adminUserInsert.html";
	}
	@RequestMapping("/calendar")
	public String goToCal() {
		return "calendar/myCalendar.html";
	}
	
}
