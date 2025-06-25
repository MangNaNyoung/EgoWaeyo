package egovframework.com.egowaeyo.approval.VO;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalBoxVO {
	private int boxId;
	private String emplyrId;
	private String boxType;
	private String boxRead;
	private Date boxDt;
	private String docId;
}
