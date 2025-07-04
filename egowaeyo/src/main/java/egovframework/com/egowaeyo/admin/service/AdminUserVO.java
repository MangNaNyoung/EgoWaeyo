package egovframework.com.egowaeyo.admin.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserVO {
	
	private String emplyrId; // 업무사용자ID
	private String userNm; // 사용자명
	private String password; // 비밀번호
	private String emplNo; // 사원번호
	private String sexdstnCode; // 성별코드
	private String brthdy; // 생일
	private String houseAdres; // 주택주소
	private String passwordHint; // 비밀번호힌트
	private String passwordCnsr; // 비밀번호정답
	private String houseEndTelno; // 주택끝전화번호
	private String areaNo; // 지역번호
	private String zip; // 우편번호
	private String houseMiddleTelno; // 주택중간전화번호
	private String emplyrSttusCode; // 사용자상태코드
	private String esntlId; // 고유ID
	private String empPhoto; // 전사얼굴사진
	private String signPhoto; // 전사결재사진
	private String departmentsId; // 부서코드
	private String positionId; // 직급코드
	private String phoneNumber; // 연락처
	private String email; // 이메일

}
