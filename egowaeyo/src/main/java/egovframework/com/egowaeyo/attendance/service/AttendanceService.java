package egovframework.com.egowaeyo.attendance.service;

import java.util.List;

import egovframework.com.egowaeyo.attendance.web.AttendVO;

public interface AttendanceService {
	public List<AttendVO> getAttend(String id);
}
