package egovframework.com.egowaeyo.approval.service;

import java.util.List;

import egovframework.com.cop.smt.lsm.service.EmplyrVO;
import egovframework.com.egowaeyo.admin.service.EgovDeptVO;
import egovframework.com.egowaeyo.approval.VO.ApprovalCcVO;
import egovframework.com.egowaeyo.approval.VO.ApprovalDetailVO;
import egovframework.com.egowaeyo.approval.VO.ApprovalDocVO;
import egovframework.com.egowaeyo.approval.VO.ApprovalFormVO;
import egovframework.com.egowaeyo.approval.VO.ApprovalLineVO;
import egovframework.com.egowaeyo.approval.VO.ApprovalTempVO;

public interface ApprovalService {
	List<ApprovalFormVO> getFormList();

	void insertFullApproval(ApprovalDocVO docVO, List<ApprovalLineVO> lineList, List<ApprovalCcVO> ccList);

	List<ApprovalDocVO> selectReceiveList(String empId);

	List<ApprovalDocVO> getDeptReceiveList(String deptId);

	List<ApprovalTempVO> getTempList(String empId);
	
	List<ApprovalCcVO> getReferenceList(String empId);

	ApprovalDetailVO getApprovalDetail(String docId);
	
	public String getFormHtml(String formId);

	List<EgovDeptVO> getDeptList();
	
	List<EmplyrVO> getEmpListByDept(String deptId);
	
	List<EmplyrVO> getAllUsers();
	
	void insertTemp(ApprovalTempVO vo);
}
