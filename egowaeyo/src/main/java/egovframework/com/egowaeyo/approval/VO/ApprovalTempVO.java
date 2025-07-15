package egovframework.com.egowaeyo.approval.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalTempVO {
	private String tempId;
	private String empId;
	private String tempTitle;
	private String tempContent;
	private String tempDt;
	private String tempStatus;
	
	public String getTempContent() { return tempContent; }
	public void setTempContent(String tempContent) { this.tempContent = tempContent; }
}
