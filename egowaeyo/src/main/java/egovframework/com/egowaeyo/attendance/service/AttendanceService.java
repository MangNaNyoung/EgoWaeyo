package egovframework.com.egowaeyo.attendance.service;

import java.util.List;

import egovframework.com.egowaeyo.attendance.web.AttendVO;
import egovframework.com.egowaeyo.attendance.web.EditAttendVO;

public interface AttendanceService {
	public List<AttendVO> getAttend(AttendVO vo);
	
	public EditAttendVO rgstEdit(EditAttendVO vo); 
}
