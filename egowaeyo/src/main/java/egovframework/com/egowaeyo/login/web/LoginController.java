package egovframework.com.egowaeyo.login.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {
	
	@RequestMapping("/goToLogin")
	public String goToLogin() {
		return "login/login.html";
	}
	
}
