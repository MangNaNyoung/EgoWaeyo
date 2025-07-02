package egovframework.com.egowaeyo.attendance.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AttendanceController {
	
	
	@RequestMapping("/attend")
	public String goToAttend() {		
		return "attendance/attendance.html";
	}
	
}
