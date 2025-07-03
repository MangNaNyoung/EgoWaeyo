package egovframework.com.egowaeyo.attendance.web;

import lombok.Data;

@Data
public class AttendVO {
	
	String checkdate; 
	String emplyr_id;
	String checkin;
	String checkout;
	String instate;
	String outstate;
	String over_check_time;
	String check_time;
	String startDate;
	String endDate;
}
