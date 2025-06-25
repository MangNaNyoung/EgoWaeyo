package egovframework.com.egowaeyo.approval.VO;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import oracle.sql.NCLOB;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalHistoryVO {
	private int hisId;
	private String approverId;
	private String hisAction;
	private NCLOB hisOpinion;
	private Date hisDt;
	private String docId;
}
