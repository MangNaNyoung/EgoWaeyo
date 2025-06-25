package egovframework.com.egowaeyo.approval.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import oracle.sql.NCLOB;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalConfigVO {
	private String confUser;
	private NCLOB confDefault;
	private String confNotify;
	private String confAuto;
	
}
