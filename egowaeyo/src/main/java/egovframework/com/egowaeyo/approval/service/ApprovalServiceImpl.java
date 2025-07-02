package egovframework.com.egowaeyo.approval.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.com.egowaeyo.approval.VO.ApprovalCcVO;
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
}
