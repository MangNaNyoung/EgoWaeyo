package egovframework.com.egowaeyo.approval.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.com.cop.smt.lsm.service.EmplyrVO;
import egovframework.com.egowaeyo.admin.service.DeptVO;
import egovframework.com.egowaeyo.approval.VO.ApprovalCcVO;
import egovframework.com.egowaeyo.approval.VO.ApprovalDetailVO;
import egovframework.com.egowaeyo.approval.VO.ApprovalDocVO;
import egovframework.com.egowaeyo.approval.VO.ApprovalFormVO;
import egovframework.com.egowaeyo.approval.VO.ApprovalTempVO;
import egovframework.com.egowaeyo.approval.mapper.ApprovalMapper;

@Service
public class ApprovalServiceImpl implements ApprovalService {

	@Autowired
	private ApprovalMapper approvalMapper;

	@Override
	public List<ApprovalFormVO> getFormList() {
		return approvalMapper.selectFormList();
	}

	@Override
	public void insertApprovalDoc(ApprovalDocVO vo) {
		approvalMapper.insertApprovalDoc(vo);
	}

	@Override
	public List<ApprovalDocVO> getReceiveList(String empId) {
		return approvalMapper.selectReceiveList(empId);
	}

	@Override
	public List<ApprovalDocVO> getDeptReceiveList(String deptId) {
		return approvalMapper.selectDeptReceiveList(deptId);
	}

	@Override
	public List<ApprovalTempVO> getTempList(String empId) {
		return approvalMapper.selectTempList(empId);
	}
	
	@Override
	public List<ApprovalCcVO> getReferenceList(String empId) {
	    return approvalMapper.selectReferenceList(empId);
	}

	@Override
	public ApprovalDetailVO getApprovalDetail(String docId) {
		 return approvalMapper.selectApprovalDetail(docId);
	}

	@Override
	public String getFormHtml(String formId) {
		return approvalMapper.selectApprFormHtml(formId);
	}

	@Override
	public List<DeptVO> getDeptList() {
		return approvalMapper.selectDeptList();
	}

	@Override
	public List<EmplyrVO> getEmpListByDept(String deptId) {	
		return approvalMapper.selectEmpListByDept(deptId);
	}

	@Override
	public List<EmplyrVO> getAllUsers() {
		return approvalMapper.selectAllUsers();
	}
	

}
