package egovframework.com.egowaeyo.approval.VO;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalDetailVO {
 private String docId;
 private String approformName;
 private String docTitle;
 private String userNm;
 private String deptName;
 private String retention;
 private String openYn;
 private String secLevel;
 private String approvalContent;
 private Date regDate;
 private String status;

 // 결재선, 첨부파일 등
 private List<ApprovalLineVO> approvalLines;
}
