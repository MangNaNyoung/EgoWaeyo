package egovframework.com.egowaeyo.attendance.web;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.util.EgovUserDetailsHelper;
import egovframework.com.egowaeyo.attendance.service.AttendanceService;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
public class AttendanceController {
	
	@Autowired AttendanceService AttendService;
	
	//내 출퇴근 내역 페이지 이동
	@GetMapping("/attendanceBasic.do")
	public String goToAttend() {		
		return "attendance/attendance.html";
	}
	// 내 춡퇴근 리스트 조회, 검색
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
	
	// 정정신청 등록
	@PostMapping("/rgstEdit.do")
	@ResponseBody
	public Map<String,String>rgstEdit(@RequestBody List<AttendVO> vo){
		Map<String,String>map = new HashMap<>();
		System.out.println(vo);
		if(vo.get(1).getCheckout() == null) {
			vo.get(1).setCheckout("0000");
		}
		map.put("result",AttendService.rgstEdit(vo).getMessage());
		System.out.println(map.get("result"));
		return  map;
	}
	
	//관리자 정정 리스트 페이지 이동
	@GetMapping("/editAttend.do")
	public String goToEdit(){
		return "attendance/approveEdit.html";
	}
	
	//관리자,사용자 정정 리스트 호출, 검색
	@GetMapping("/getEditListForAttend.do")
	@ResponseBody
	public List<EditAttendVO> getEditList(@RequestParam Map<String, String> params) {
		EditAttendVO vo = new EditAttendVO();
		if(params.get("userId")!=null) {
			vo.setEmplyrId(params.get("userId"));
		}
		vo.setEditer(params.get("modstate"));
		vo.setStartDate(params.get("startDate").replace("-", ""));
		vo.setEndDate(params.get("endDate").replace("-", ""));
		System.out.println(vo.getEditer());
		System.out.println(vo.getStartDate());
		System.out.println(vo.getEndDate());
		return AttendService.getEditList(vo);
	}
	
	//관리자 정정요청 수락
	@PostMapping("/editAttend.do")
	@ResponseBody
	public Map<String,String>editListSelected(@RequestBody List<EditAttendVO> vo){
		Map<String,String>map= new HashMap<>();
		LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();
		System.out.println(user.getId());
		vo.forEach(item -> item.setSupervisor(user.getId()));
		List<EditAttendVO> result = AttendService.editAttendList(vo);
		if(result==null) {
			map.put("result","fail" );
			return map ;
		}else {
			map.put("result", "success");
			return map; 
		}
	}
	
	// 나의 정정리스트 페이지 이동
	@GetMapping("/myAttendList.do")
	public String goToMyList() {
		return "attendance/myAttendList.html";
	}
}
