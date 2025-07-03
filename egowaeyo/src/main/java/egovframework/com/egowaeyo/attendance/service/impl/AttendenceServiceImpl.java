package egovframework.com.egowaeyo.attendance.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.com.egowaeyo.attendance.mapper.AttendanceMapper;
import egovframework.com.egowaeyo.attendance.service.AttendanceService;
import egovframework.com.egowaeyo.attendance.web.AttendVO;

@Service
public class AttendenceServiceImpl implements AttendanceService {
	
	@Autowired AttendanceMapper AttendMapper;
	
	@Override
	public List<AttendVO> getAttend(AttendVO vo) {
		return AttendMapper.getAttend(vo);
	}

}
