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
	private NCLOB tempContent;
	private Date tempDt;
}
