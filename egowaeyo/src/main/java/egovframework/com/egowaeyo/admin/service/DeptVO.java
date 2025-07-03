package egovframework.com.egowaeyo.admin.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeptVO {
	
	private String departmentsId;
	private String departmentsName;
	private String departmentsMaster;

}
