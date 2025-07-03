package egovframework.com.egowaeyo.attendance.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.com.egowaeyo.attendance.service.AttendanceService;

@Controller
@RequestMapping("/attendance")
public class AttendanceController {
	
	@Autowired AttendanceService AttendService;
	
	@GetMapping("/attendance.do")
	public String goToAttend() {		
		return "attendance/attendance.html";
	}
	
	@GetMapping("/attendList.do")
	@ResponseBody
	public List<AttendVO>getAttend(@RequestBody AttendVO vo){
		
		return AttendService.getAttend(vo);
	}
}
