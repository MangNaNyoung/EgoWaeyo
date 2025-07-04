package egovframework.com.egowaeyo.attendance.web;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.com.egowaeyo.attendance.service.AttendanceService;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequestMapping("/attendance")
public class AttendanceController {
	
	@Autowired AttendanceService AttendService;
	
	@GetMapping("/attendance.do")
	public String goToAttend() {		
		return "attendance/attendance.html";
	}
	
	@PostMapping("/attendList.do")
	@ResponseBody
	public List<AttendVO>getAttend(@RequestBody AttendVO vo, Principal principal){
		
		if(vo.getStartDate() == null) {
			vo.setStartDate(null);
		}
		
		vo.setEmplyrId(principal.getName());
		
		return AttendService.getAttend(vo);
	}
}
