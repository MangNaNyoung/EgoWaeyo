package egovframework.com.egowaeyo.attendance.web;

import lombok.Data;

@Data
public class EditAttendVO {
	
	    private String editCd;       // 정정코드
	    private String editIn;       // 정정 출근시간
	    private String editOut;      // 정정 퇴근 시간
	    private String editRs;       // 수정사유
	    private String createDt;     // 생성날짜
	    private String editDt;       // 수정 날짜
	    private String editer;       // 수정여부
	    private String supervisor;   // 결재승인자
	    private String supDt;        // 승인 일자
	    private String checkdate;    // 근태일자
	    private String emplyrId;     // 정정신청자
	    private String checkin;      // 기존 출근 시간
	    private String checkout;     // 기존 퇴근 시간
	    private String result;		// 프로시저 결과
	    private String message;		// 프로시저 결과 메세지

}
