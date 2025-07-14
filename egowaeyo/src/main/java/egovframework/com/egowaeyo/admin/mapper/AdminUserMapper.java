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
	
	public String getNextEmplNo(); // 다음 사번 조회
	public String getNextOrgnztId(); // 다음 부서코드 조회
	
	public int DeptIns(EgovDeptVO edpt); // 부서관리 페이지 부서목록 부서추가
	
	public int DeptUdt(EgovDeptVO edpt); // 부서관리 페이지 부서목록 부서수정
	public int DeptDel(String orgnztId); // 부서관리 페이지 부서목록 부서삭제
	
	public List<AdminUserVO> selectemp(AdminUserVO au); // 부서관리 페이지 전체 사용자 조회
	
	public int EmpDel(String emplyrId); // 부서관리 페이지 사용자삭제
	public int EmpUdt(String emplyrId, String positionId); // 사용자 수정 (직급 변경)
}
