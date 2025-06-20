package egovframework.com.egowaeyo.approval.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.com.egowaeyo.approval.VO.ApprovalDocVO;
import egovframework.com.egowaeyo.approval.VO.ApprovalFormVO;
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

}
