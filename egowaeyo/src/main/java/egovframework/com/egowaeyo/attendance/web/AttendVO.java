package egovframework.com.egowaeyo.attendance.web;

import lombok.Data;

@Data
public class AttendVO {
	
	private String checkdate;        // 등록 일자
	private String emplyrId;		 // 출근계정
	private String checkin;			 // 출근 시간
	private String checkout;		 // 퇴근 시간
	private String instate;			 // 출근 상태
	private String outstate;		 // 퇴근 상태
	private int overCheckTime;	 // 초과 근무시간
	private int checkTime;		 // 근무시간
	private String startDate;		 // 조회용 검색 시작일
	private String endDate;			 // 조회용 마지막 조회일
	private String date;				 // 요일 
	private String strIn;				// 출근 시간 상태
	private String strOut;				// 퇴근 시간 상태
	//private boolean pick;			// 정정 선택
}
