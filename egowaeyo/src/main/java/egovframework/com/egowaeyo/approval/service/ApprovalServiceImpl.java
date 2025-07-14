package egovframework.com.egowaeyo.approval.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.com.cop.smt.lsm.service.EmplyrVO;
import egovframework.com.egowaeyo.admin.service.EgovDeptVO;
import egovframework.com.egowaeyo.approval.VO.ApprovalCcVO;
import egovframework.com.egowaeyo.approval.VO.ApprovalDetailVO;
import egovframework.com.egowaeyo.approval.VO.ApprovalDocVO;
import egovframework.com.egowaeyo.approval.VO.ApprovalFormVO;
import egovframework.com.egowaeyo.approval.VO.ApprovalLineVO;
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
	    @Transactional
	    public void insertFullApproval(ApprovalDocVO docVO, List<ApprovalLineVO> lineList, List<ApprovalCcVO> ccList) {
	        // 1. 결재문서 등록
	        approvalMapper.insertApprovalDoc(docVO); // doc_id는 selectKey로 생성했다고 가정

	        // 2. 결재선 등록
	        for (ApprovalLineVO lineVO : lineList) {
	            lineVO.setDocId(docVO.getDocId()); // doc_id 세팅
	            approvalMapper.insertApprovalLine(lineVO);
	        }

	        // 3. 참조자 등록
	        for (ApprovalCcVO ccVO : ccList) {
	            ccVO.setDocId(docVO.getDocId());
	            approvalMapper.insertApprovalCc(ccVO);
	        }
	    }

	@Override
	public List<ApprovalDocVO> selectReceiveList(String empId) {
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
	public List<EgovDeptVO> getDeptList() {
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
	
	@Override
	public void insertTemp(ApprovalTempVO vo) {
	    approvalMapper.insertTemp(vo);
	}
	

}
