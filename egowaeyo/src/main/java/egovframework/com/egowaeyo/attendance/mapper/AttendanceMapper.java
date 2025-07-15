package egovframework.com.egowaeyo.attendance.mapper;

import java.util.List;

import egovframework.com.egowaeyo.attendance.web.AttendVO;
import egovframework.com.egowaeyo.attendance.web.EditAttendVO;
import egovframework.com.egowaeyo.attendance.web.GetInfoVO;

public interface AttendanceMapper {
	public List<AttendVO> getAttend(AttendVO vo);
	public EditAttendVO callAttendanceEditProcedure(EditAttendVO vo);
	public List<EditAttendVO> getEditList(EditAttendVO vo);
	public int callEditProcedure (EditAttendVO vo);
	public GetInfoVO callCheckAttendanceProcedure(GetInfoVO vo);
	public AttendVO getAttendance(GetInfoVO vo);
}
