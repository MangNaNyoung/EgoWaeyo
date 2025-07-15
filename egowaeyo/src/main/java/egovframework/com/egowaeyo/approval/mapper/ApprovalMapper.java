package egovframework.com.egowaeyo.approval.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import egovframework.com.cop.smt.lsm.service.EmplyrVO;
import egovframework.com.egowaeyo.admin.service.EgovDeptVO;
import egovframework.com.egowaeyo.approval.VO.ApprovalCcVO;
import egovframework.com.egowaeyo.approval.VO.ApprovalDetailVO;
import egovframework.com.egowaeyo.approval.VO.ApprovalDocVO;
import egovframework.com.egowaeyo.approval.VO.ApprovalFormVO;
import egovframework.com.egowaeyo.approval.VO.ApprovalLineVO;
import egovframework.com.egowaeyo.approval.VO.ApprovalTempVO;

public interface ApprovalMapper {
	List<ApprovalFormVO> selectFormList();

	void insertApprovalDoc(ApprovalDocVO vo);

	void insertApprovalLine(ApprovalLineVO vo);

	void insertApprovalCc(ApprovalCcVO vo);

	List<ApprovalDocVO> selectReceiveList(String empId);

	List<ApprovalDocVO> selectDeptReceiveList(String deptId);

	List<ApprovalTempVO> selectTempList(String empId);

	List<ApprovalCcVO> selectReferenceList(String empId);

	ApprovalDetailVO selectApprovalDetail(String docId);

	String selectApprFormHtml(String formId);

	List<EgovDeptVO> selectDeptList();

	List<EmplyrVO> selectEmpListByDept(String deptId);

	List<EmplyrVO> selectAllUsers();

	void insertTemp(ApprovalTempVO vo);

	ApprovalDocVO selectApprovalDocDetail(String docId);

	void updateApprove(@Param("docId") String docId, @Param("approverId") String approverId,
			@Param("opinion") String opinion);

	void updateReject(@Param("docId") String docId, @Param("approverId") String approverId,
			@Param("opinion") String opinion);

	void updateDocStatusIfAllApproved(@Param("docId") String docId);

	void updateDocStatusToRejected(@Param("docId") String docId);

	List<ApprovalDocVO> selectProgressList(String loginId);

	List<ApprovalDocVO> selectRejectList(String loginId);

}
