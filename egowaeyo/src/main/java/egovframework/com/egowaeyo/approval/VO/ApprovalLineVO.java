package egovframework.com.egowaeyo.approval.VO;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import oracle.sql.NCLOB;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalLineVO {
	private long lineId;
	private String approverId;
	private Integer lineOrder;
	private String lineStatus;
	private NCLOB lineOpinion;
	private Date lineDt;
	private String docId;
}
