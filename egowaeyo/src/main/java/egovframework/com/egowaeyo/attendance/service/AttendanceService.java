package egovframework.com.egowaeyo.attendance.service;

import java.util.List;

import egovframework.com.egowaeyo.attendance.web.AttendVO;
import egovframework.com.egowaeyo.attendance.web.EditAttendVO;
import egovframework.com.egowaeyo.attendance.web.GetInfoVO;

public interface AttendanceService {
	public List<AttendVO> getAttend(AttendVO vo);
	
	public EditAttendVO rgstEdit(List<AttendVO> vo); 
	
	public List<EditAttendVO> getEditList(EditAttendVO vo);
	public List<EditAttendVO> editAttendList(List<EditAttendVO> vo);
	public AttendVO checkAttend(GetInfoVO vo);
	public AttendVO getToday(GetInfoVO vo);
	
}
