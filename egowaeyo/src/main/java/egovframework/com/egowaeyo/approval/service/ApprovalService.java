package egovframework.com.egowaeyo.approval.service;

import java.util.List;

import egovframework.com.egowaeyo.approval.VO.ApprovalDocVO;
import egovframework.com.egowaeyo.approval.VO.ApprovalFormVO;

public interface ApprovalService {
	List<ApprovalFormVO> getFormList();

	void insertApprovalDoc(ApprovalDocVO vo);
}
