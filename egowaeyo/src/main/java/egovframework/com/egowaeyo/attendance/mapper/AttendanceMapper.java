package egovframework.com.egowaeyo.attendance.mapper;

import java.util.List;
import java.util.Map;

import egovframework.com.egowaeyo.attendance.web.AttendVO;
import egovframework.com.egowaeyo.attendance.web.EditAttendVO;

public interface AttendanceMapper {
	public List<AttendVO> getAttend(AttendVO vo);
	public EditAttendVO callAttendanceEditProcedure(EditAttendVO vo);
	public List<EditAttendVO> getEditList();
}
