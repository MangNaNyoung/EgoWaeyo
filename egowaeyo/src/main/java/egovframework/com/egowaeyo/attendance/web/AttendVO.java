package egovframework.com.egowaeyo.attendance.web;

import lombok.Data;

@Data
public class AttendVO {
	
	String checkdate;        // 등록 일자
	String emplyrId;		 // 출근계정
	String checkin;			 // 출근 시간
	String checkout;		 // 퇴근 시간
	String instate;			 // 출근 상태
	String outstate;		 // 퇴근 상태
	String overCheckTime;	 // 초과 근무시간
	String checkTime;		 // 근무시간
	String startDate;		 // 조회용 검색 시작일
	String endDate;			 // 조회용 마지막 조회일
}
