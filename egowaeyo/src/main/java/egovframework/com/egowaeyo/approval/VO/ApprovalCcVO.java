package egovframework.com.egowaeyo.approval.VO;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalCcVO {
	private int ccId;
	private String emplyrId;
	private Date readDt;
	private String ccStatus;
	private String docId;
}
