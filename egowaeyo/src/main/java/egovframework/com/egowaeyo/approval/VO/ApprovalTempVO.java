package egovframework.com.egowaeyo.approval.VO;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import oracle.sql.NCLOB;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalTempVO {
	private String temId;
	private String emplyrId;
	private String tempTitle;
	private String tempContent;
	private Date tempDt;
	
	public String getTempContent() { return tempContent; }
	public void setTempContent(String tempContent) { this.tempContent = tempContent; }
}
