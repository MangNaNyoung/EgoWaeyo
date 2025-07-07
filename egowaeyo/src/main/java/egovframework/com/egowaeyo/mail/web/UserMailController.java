package egovframework.com.egowaeyo.mail.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserMailController {

	@RequestMapping("/mailingBasic.do")
	public String noneReadMail() {
       
        return "egoMail/mail-NoneRead.html";
	   
	}
	
	@RequestMapping("/writingMail.do")
	public String writingMail() {
       
        return "egoMail/mail-Writing.html";
	   
	}
	
}
