package egovframework.com.egowaeyo.approval.service;

import java.util.List;

import egovframework.com.egowaeyo.approval.VO.ApprovalCcVO;
import egovframework.com.egowaeyo.approval.VO.ApprovalDocVO;
import egovframework.com.egowaeyo.approval.VO.ApprovalFormVO;
import egovframework.com.egowaeyo.approval.VO.ApprovalTempVO;

public interface ApprovalService {
	List<ApprovalFormVO> getFormList();

	void insertApprovalDoc(ApprovalDocVO vo);

	List<ApprovalDocVO> getReceiveList(String empId);

	List<ApprovalDocVO> getDeptReceiveList(String deptId);

	List<ApprovalTempVO> getTempList(String empId);
	
	List<ApprovalCcVO> getReferenceList(String empId);

}
