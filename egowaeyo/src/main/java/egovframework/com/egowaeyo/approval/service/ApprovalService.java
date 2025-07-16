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
	
	public int getDeleteTempList(List<String> ids);
	
	void getSaveTemp(ApprovalTempVO vo);

	List<ApprovalCcVO> getReferenceList(String empId);

	ApprovalDetailVO getApprovalDetailForPrint(String docId);

	public String getFormHtml(String formId);

	List<EgovDeptVO> getDeptList();

	List<EmplyrVO> getEmpListByDept(String deptId);

	List<EmplyrVO> getAllUsers();

	void insertTemp(ApprovalTempVO vo);

	ApprovalDocVO getApprovalDetailForView(String docId);

	void approve(String docId, String approverId, String opinion);

	void reject(String docId, String approverId, String opinion);

	List<ApprovalDocVO> selectProgressList(String loginId);

	List<ApprovalDocVO> selectRejectList(String loginId);
	
	ApprovalDocVO getProgressDetail(String docId);
		
	ApprovalDocVO getRejectDetail(String docId);
	
	void getDeleteApproval(String docId);	

}
