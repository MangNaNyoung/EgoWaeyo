package attendance;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import egovframework.com.egowaeyo.attendance.mapper.AttendanceMapper;
import egovframework.com.egowaeyo.attendance.web.AttendVO;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/spring/com/*-context.xml")
public class AttendanceMapperTest {
	@Autowired AttendanceMapper mapper;
	
	@Test
	public void 불러오기() {
		AttendVO vo = new AttendVO();
		vo.setEmplyrId("TEST1");
		log.info("log-----------------------------"+mapper.getAttend(vo));
	}
		
}
