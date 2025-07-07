package egovframework.com.egowaeyo.admin.web;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import egovframework.com.cmm.service.EgovProperties;
import egovframework.com.egowaeyo.admin.service.AdminUserService;
import egovframework.com.egowaeyo.admin.service.AdminUserVO;
import egovframework.com.egowaeyo.admin.service.DeptVO;
import egovframework.com.egowaeyo.admin.service.PosVO;



@Controller
public class AdminUserController {
	
	@Autowired AdminUserService adminuserservice;
	
	private static final String FILE_STORE_PATH = EgovProperties.getProperty("Globals.fileStorePath");
	
	// 사용자 관리 > 사용자 등록
	@GetMapping("/adUserIns.do")
	public String adminUserIns(Model model) {
	   
        List<PosVO> posilist = adminuserservice.getPos(null);
        System.out.println("Position list size: " + (posilist != null ? posilist.size() : "null"));
        model.addAttribute("posi", posilist);
       
        return "egoAdmin/adminUserInsert.html";
	   
	}
	
	// 사용자 관리 > 사용자 등록 (POST)
		@PostMapping("/adUserIns.do")
		public String adminUserIns(
				@RequestParam("empPhotoFile") MultipartFile empPhoto,
				@RequestParam("signPhotoFile") MultipartFile signPhoto,
				AdminUserVO adu,
				Model model) {
			
			System.out.println("받은 데이터 : " + adu);
			System.out.println("프로필 사진: " + empPhoto.getOriginalFilename());
			System.out.println("결재 사진: " + signPhoto.getOriginalFilename());
			
			try {
				// 파일 처리 (필요시)
				if (!empPhoto.isEmpty()) {
					// 파일 저장 로직 추가
					System.out.println("프로필 사진 업로드됨: " + empPhoto.getSize() + " bytes");
					File file = new File(FILE_STORE_PATH+"/"+ empPhoto.getOriginalFilename());
					empPhoto.transferTo(file);
					adu.setEmpPhoto(empPhoto.getOriginalFilename());
				}
				
				if (!signPhoto.isEmpty()) {
					// 파일 저장 로직 추가
					System.out.println("결재 사진 업로드됨: " + signPhoto.getSize() + " bytes");
					File file = new File(FILE_STORE_PATH+"/"+ signPhoto.getOriginalFilename());
					signPhoto.transferTo(file);
					adu.setSignPhoto(signPhoto.getOriginalFilename());
				}
				
				// 사용자 등록
				int result = adminuserservice.AdminUserIns(adu);
				System.out.println("등록 결과: " + result);
				
				if (result > 0) {
					System.out.println("사용자 등록 성공");
					// 성공 메시지 추가
					model.addAttribute("message", "사용자가 성공적으로 등록되었습니다.");
				} else {
					System.out.println("사용자 등록 실패");
					model.addAttribute("error", "사용자 등록에 실패했습니다.");
				}
				
			} catch (Exception e) {
				System.out.println("사용자 등록 중 오류 발생: " + e.getMessage());
				e.printStackTrace();
				model.addAttribute("error", "등록 중 오류가 발생했습니다: " + e.getMessage());
			}
			
			// 직급 목록 다시 조회 (페이지 표시용)
			List<PosVO> posilist = adminuserservice.getPos(null);
			model.addAttribute("posi", posilist);
			
			// 같은 페이지로 다시 이동 (redirect 대신 forward)
			return "egoAdmin/adminUserInsert.html";
		}
	
	
    // 부서 목록 조회용 (모달)
	@GetMapping("/deptlist")
	@ResponseBody
	public List<DeptVO> getDeptList() {
		return adminuserservice.getDept(null);
	}
	
	// 부서 직급 조회용 (Select option)
	@GetMapping("/deptpos")
	@ResponseBody
	public List<PosVO> getPositionList() {
		return adminuserservice.getPos(null);
	}
	
	// 조직 관리 > 조직 현황
	@RequestMapping("/adDeptMge.do")
	public String adminDeptMge() {
       
        return "egoAdmin/adminDeptManagement.html";
	   
	}
	
	// 조직 관리 > 사원통합관리
	@RequestMapping("/adUserMge.do")
	public String adminUserMge() {
       
        return "egoAdmin/adminUserManagement.html";
	   
	}
	
	// 권한 관리 > 메뉴권한관리
	@RequestMapping("/adMenuAuth.do")
	public String adminMenuAuthority() {
       
        return "egoAdmin/adminMenuAuthority.html";
	   
	}
	

}
