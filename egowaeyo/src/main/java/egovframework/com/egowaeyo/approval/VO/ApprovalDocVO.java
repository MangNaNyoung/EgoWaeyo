package egovframework.com.egowaeyo.approval.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalDocVO {
	private String docId;
    private String docTitle;
    private String emplId;
    private String empName;
    private String docStatus;
    private String createdDt;
    private String apprformId;
    private String apprformName;
    private String deptName;
    private String openYn;
    private String drafterName;
    private String approverNames; 
    private String docHtml;
}
