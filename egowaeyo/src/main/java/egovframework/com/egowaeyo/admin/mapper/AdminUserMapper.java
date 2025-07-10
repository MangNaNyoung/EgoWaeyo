package egovframework.com.egowaeyo.admin.mapper;

import java.util.List;

import egovframework.com.egowaeyo.admin.service.AdminUserVO;
import egovframework.com.egowaeyo.admin.service.DeptVO;
import egovframework.com.egowaeyo.admin.service.EgovDeptVO;
import egovframework.com.egowaeyo.admin.service.PosVO;

public interface AdminUserMapper {
	
	public List<DeptVO> getDept(DeptVO dept);
	public List<PosVO> getPos(PosVO pos);
	public List<EgovDeptVO> getEgovDept(EgovDeptVO edpt);
	public int AdminUserIns(AdminUserVO adu);
}
