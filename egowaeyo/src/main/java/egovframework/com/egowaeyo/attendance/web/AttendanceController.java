package egovframework.com.egowaeyo.attendance.web;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.util.EgovUserDetailsHelper;
import egovframework.com.egowaeyo.attendance.service.AttendanceService;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
public class AttendanceController {
	
	@Autowired AttendanceService AttendService;
	
	@GetMapping("/attendanceBasic.do")
	public String goToAttend() {		
		return "attendance/attendance.html";
	}
	
	@PostMapping("/attendList.do")
	@ResponseBody
	public List<AttendVO>getAttend(@RequestBody AttendVO vo, Principal principal){
//		LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();
//		System.out.println(user.getId()+user.getName());
		
		if(vo.getStartDate() == null) {
			LocalDate currentDate = LocalDate.now(); 
			String firstDate = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), 1).format(DateTimeFormatter.BASIC_ISO_DATE);
			String lastDate = LocalDate.of(currentDate.getYear(), currentDate.getMonth(),currentDate.lengthOfMonth()).format(DateTimeFormatter.BASIC_ISO_DATE);
			vo.setStartDate(firstDate);
			vo.setEndDate(lastDate);
		}
		vo.setEmplyrId(principal.getName());
		vo.setStartDate(vo.getStartDate().replace("-", ""));
		vo.setEndDate(vo.getEndDate().replace("-", ""));
		List<AttendVO> result = AttendService.getAttend(vo);
		result.forEach(item->{
			item.getCheckdate();
		    item.setDate(item.getCheckdate()+" ("+LocalDate.of(Integer.parseInt(item.getCheckdate().substring(0,4)) ,Integer.parseInt(item.getCheckdate().substring(4,6)),Integer.parseInt(item.getCheckdate().substring(6,8))).getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN)+")");
		});
		
		System.out.println(result);
		return result ;
	}
}
