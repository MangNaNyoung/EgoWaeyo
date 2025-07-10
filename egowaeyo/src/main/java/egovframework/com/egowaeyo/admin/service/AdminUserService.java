package egovframework.com.egowaeyo.admin.service;

import java.util.List;

public interface AdminUserService {
	
	public List<DeptVO> getDept(DeptVO dept);
	public List<PosVO> getPos(PosVO pos);
	public List<EgovDeptVO> getEgovDept(EgovDeptVO edpt);
	public int AdminUserIns(AdminUserVO adu);
}
