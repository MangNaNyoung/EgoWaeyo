package egovframework.com.egowaeyo.approval.mapper;

import java.util.List;

import egovframework.com.egowaeyo.approval.VO.ApprovalDocVO;
import egovframework.com.egowaeyo.approval.VO.ApprovalFormVO;

public interface ApprovalMapper {
	  List<ApprovalFormVO> selectFormList();
	    void insertApprovalDoc(ApprovalDocVO vo);
}
