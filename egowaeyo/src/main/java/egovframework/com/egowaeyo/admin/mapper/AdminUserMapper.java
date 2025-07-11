package egovframework.com.egowaeyo.admin.mapper;

import java.util.List;

import egovframework.com.egowaeyo.admin.service.AdminUserVO;
import egovframework.com.egowaeyo.admin.service.DeptVO;
import egovframework.com.egowaeyo.admin.service.EgovDeptVO;
import egovframework.com.egowaeyo.admin.service.PosVO;

public interface AdminUserMapper {
	
	public List<PosVO> getPos(PosVO pos); // 직급 목록 조회용
	public List<EgovDeptVO> getEgovDept(EgovDeptVO edpt); // 부서 목록 조회용
	public int AdminUserIns(AdminUserVO adu); // [관리자] 사용자 등록
}
