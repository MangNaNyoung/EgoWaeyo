package egovframework.com.egowaeyo.approval.mapper;

import java.util.List;

import egovframework.com.egowaeyo.approval.VO.ApprovalBoxVO;
import egovframework.com.egowaeyo.approval.VO.ApprovalDocVO;
import egovframework.com.egowaeyo.approval.VO.ApprovalFormVO;

public interface ApprovalMapper {
	  List<ApprovalFormVO> selectFormList();
	    void insertApprovalDoc(ApprovalDocVO vo);
	    
	  List<ApprovalBoxVO> selectBoxListByUser(String empId);
	  void updateBoxRead(int boxId);  // 읽음 표시용 메서드
}
