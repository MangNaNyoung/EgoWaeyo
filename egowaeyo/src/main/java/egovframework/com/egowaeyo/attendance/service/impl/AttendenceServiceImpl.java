package egovframework.com.egowaeyo.attendance.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.com.egowaeyo.attendance.mapper.AttendanceMapper;
import egovframework.com.egowaeyo.attendance.service.AttendanceService;
import egovframework.com.egowaeyo.attendance.web.AttendVO;
import egovframework.com.egowaeyo.attendance.web.EditAttendVO;

@Service
public class AttendenceServiceImpl implements AttendanceService {
	
	@Autowired AttendanceMapper AttendMapper;
	
	@Override
	public List<AttendVO> getAttend(AttendVO vo) {
		return AttendMapper.getAttend(vo);
	}

	@Override
	public EditAttendVO rgstEdit(List<AttendVO> vo) {
		EditAttendVO voa = new EditAttendVO();
				voa.setCheckdate(vo.get(0).getCheckdate());
				voa.setCheckin(vo.get(1).getCheckin());
				voa.setCheckout(vo.get(1).getCheckout());
				voa.setEmplyrId(vo.get(0).getEmplyrId());
				voa.setEditIn(vo.get(0).getCheckin());
				voa.setEditOut(vo.get(0).getCheckout());		
		System.out.println(voa);
		AttendMapper.callAttendanceEditProcedure(voa);
		
		return voa;
	}

	@Override
	public List<EditAttendVO> getEditList(EditAttendVO vo) {
		return AttendMapper.getEditList(vo);
	}

}
