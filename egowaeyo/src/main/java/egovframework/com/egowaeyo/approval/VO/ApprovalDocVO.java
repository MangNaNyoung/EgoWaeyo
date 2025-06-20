package egovframework.com.egowaeyo.approval.VO;

import java.util.Date;

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
	private String docStatus;
	private Date createdDt;
	private String apprformId;
	private String docHtml;
}
