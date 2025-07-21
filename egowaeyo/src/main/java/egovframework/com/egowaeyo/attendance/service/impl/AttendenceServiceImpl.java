package egovframework.com.egowaeyo.attendance.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.com.egowaeyo.attendance.mapper.AttendanceMapper;
import egovframework.com.egowaeyo.attendance.service.AttendanceService;
import egovframework.com.egowaeyo.attendance.web.AttendVO;
import egovframework.com.egowaeyo.attendance.web.EditAttendVO;
import egovframework.com.egowaeyo.attendance.web.GetInfoVO;

@Service
public class AttendenceServiceImpl implements AttendanceService {
	
	private int count = 0;
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

	@Override
	public List<EditAttendVO> editAttendList(List<EditAttendVO> vo) {
		
		vo.forEach(item -> {AttendMapper.callEditProcedure(item);
							count+=item.getRowcount();});
		if(vo.size()==count) {
			System.out.println(vo.size());
			System.out.println(count);
			count = 0;
			return vo;
		}
		return null;
	}

	@Override
	public AttendVO checkAttend(GetInfoVO vo) {
		AttendVO res = new AttendVO();
		if(vo.getStatus()==null) {
			res = AttendMapper.getAttendance(vo);
			res.setResult("E00");
		}else {
			try {
				AttendMapper.callCheckAttendanceProcedure(vo);
			}catch(Exception e){
				System.out.println(e);
			}
			System.out.println(vo.getStatus());
			if(vo.getStatus().equals("checkin")) {
				System.out.println(vo.getStatus());
				res.setCheckin(vo.getResult());
			}else if(vo.getStatus().equals("checkout")){
				res.setCheckout(vo.getResult());
			}
			res.setResult(vo.getResult());
		}
		return res;
	}

	@Override
	public AttendVO getToday(GetInfoVO vo) {
		return AttendMapper.getAttendance( vo);
	}

}
