package egovframework.com.egowaeyo.attendance.mapper;

import java.util.List;

import egovframework.com.egowaeyo.attendance.web.AttendVO;

public interface AttendanceMapper {
	public List<AttendVO> getAttend(String id);
}
