package egovframework.com.egowaeyo.admin.service;

import java.util.List;

public interface AdminUserService {
	
	public List<PosVO> getPos(PosVO pos); // 직급 목록 조회용
	public List<EgovDeptVO> getEgovDept(EgovDeptVO edpt); // 부서 목록 조회용
	public int AdminUserIns(AdminUserVO adu); // [관리자] 사용자 등록
}
