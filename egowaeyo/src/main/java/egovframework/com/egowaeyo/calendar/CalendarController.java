package egovframework.com.egowaeyo.calendar;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequestMapping("/calendar")
public class CalendarController {
	
	@GetMapping("/calendarList.do")
	public String goToCal() {
		return "calendar/myCalendar.html";
	}
}
