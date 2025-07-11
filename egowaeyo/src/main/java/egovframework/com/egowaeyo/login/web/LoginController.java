package egovframework.com.egowaeyo.login.web;


import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.util.EgovUserDetailsHelper;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
public class LoginController {
	

	
	/*
	 * 로그인 페이지 이동
	 */
	@RequestMapping("/goToLogin")
	public String goToLogin(@ModelAttribute("loginVO")LoginVO loginVO,Model model) {
		return "login/login.html";
	}
	
	
	
	@RequestMapping("/getSessionTime")
	@ResponseBody
	public Map<String,Object> getSeessionData(HttpServletRequest request,LoginVO loginVO, Principal principal){
		Map<String,Object> map = new HashMap<String, Object>();
		//LoginVO vo = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String egovLatestServerTime = "";
		String egovExpireSessionTime = "";
		// 쿠키값 가져오기
		Cookie[] cookies = request.getCookies() ;
		if(cookies != null){
			for(int i=0; i < cookies.length; i++){
				Cookie c = cookies[i] ;
				// 저장된 쿠키 이름을 가져온다
				String cName = c.getName();
				loginVO.setName(cName);
				// 쿠키값을 가져온다
				String cValue = c.getValue() ;
				if ("egovLatestServerTime".equals(cName)) {
					egovLatestServerTime = cValue;
				}
				if ("egovExpireSessionTime".equals(cName)) {
					egovExpireSessionTime = cValue;
				}
				map.put("tip",cName);
			}
		}
		map.put("loginVO", principal.getName());
		//map.put("vo",vo);
		map.put("egovLatestServerTime",egovLatestServerTime);
		map.put("egovExpireSessionTime",egovExpireSessionTime);
		return map;
		
	}
	
}
