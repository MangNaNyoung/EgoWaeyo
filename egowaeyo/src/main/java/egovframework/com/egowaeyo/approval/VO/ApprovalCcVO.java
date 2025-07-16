package egovframework.com.egowaeyo.approval.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalCcVO {
		private String ccId;
	    private String empId;
	    private String empName;
	    private String ccStatus;
	    private String readDt;     
	    private String docId;
	    private String docTitle;
	    private String drafterName;
	    private String docStatus;
	    private String createdDt;  
	    private String finalDt;
}
