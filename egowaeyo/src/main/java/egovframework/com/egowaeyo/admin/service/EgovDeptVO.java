package egovframework.com.egowaeyo.admin.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EgovDeptVO {
	
	private String orgnztId;
	private String orgnztNm;
	private String orgnztDc;

}
