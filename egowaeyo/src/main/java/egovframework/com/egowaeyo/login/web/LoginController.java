package egovframework.com.egowaeyo.login.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.LoginVO;
import egovframework.com.uat.uia.service.EgovLoginService;
import egovframework.com.utl.sim.service.EgovClntInfo;

@Controller
public class LoginController {
	
	/** EgovLoginService */
	@Resource(name = "loginService")
	private EgovLoginService loginService;
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/*
	 * 로그인 페이지 이동
	 */
	@RequestMapping("/goToLogin")
	public String goToLogin(@ModelAttribute("loginVO")LoginVO loginVO) {
		return "login/login.html";
	}
	/*
	 * 일반 로그인 진행
	 */
	@RequestMapping("/loginAction")
	public String actionsLogin(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, ModelMap model) throws Exception {
		// 2. 로그인 처리
		LoginVO resultVO = loginService.actionLogin(loginVO);
		String userIp = EgovClntInfo.getClntIP(request);
		resultVO.setIp(userIp);
		
		// 3. 일반 로그인 처리
		// 2022.11.11 시큐어코딩 처리
		if (resultVO.getId() != null && !resultVO.getId().equals("")) {

			// 3-1. 로그인 정보를 세션에 저장
			request.getSession().setAttribute("loginVO", resultVO);
			// 2019.10.01 로그인 인증세션 추가
			request.getSession().setAttribute("accessUser", resultVO.getUserSe().concat(resultVO.getId()));

			return "test.html";

		} else {
			model.addAttribute("loginMessage", egovMessageSource.getMessage("fail.common.login",request.getLocale()));
			return "redirect:/uat/uia/egovLoginUsr.do";
		}
	}
	@RequestMapping("/tests")
	public String pagetest() {
		return "pinggu/adminUserInsert.html";
	}
	
}
