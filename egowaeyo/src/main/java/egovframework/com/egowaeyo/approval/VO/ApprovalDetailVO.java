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
    private String docTitle;
    private String approformName;
    private String empName;
    private String deptName;
    private String docStatus;
    private String openYn;
    private String secLevel;
    private String retention;
    private String approvalContent;
    private Date createdDt;
    private String docHtml;

 // 결재선, 첨부파일 등
 private List<ApprovalLineVO> approvalLines;
}
