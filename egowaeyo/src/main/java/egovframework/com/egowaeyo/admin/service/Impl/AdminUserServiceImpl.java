package egovframework.com.egowaeyo.admin.service.Impl;

import java.util.List;

import org.springframework.stereotype.Service;

import egovframework.com.egowaeyo.admin.mapper.AdminUserMapper;
import egovframework.com.egowaeyo.admin.service.AdminUserService;
import egovframework.com.egowaeyo.admin.service.AdminUserVO;
import egovframework.com.egowaeyo.admin.service.DeptVO;
import egovframework.com.egowaeyo.admin.service.PosVO;
import egovframework.com.utl.fcc.service.EgovStringUtil;
import egovframework.com.utl.sim.service.EgovFileScrty;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {
	
	final AdminUserMapper adminusermapper;
	
	@Override
	public List<DeptVO> getDept(DeptVO dept){
		return adminusermapper.getDept(dept);
	}

	@Override
	public List<PosVO> getPos(PosVO pos) {
		return adminusermapper.getPos(pos);
	}
	
    // 관리자 사용자 등록
	@Override
	public int AdminUserIns(AdminUserVO adu) {
		try {
			// 1. ESNTL_ID 설정 (USR + 사용자ID)
			String esntlId = "USR" + adu.getEmplyrId();
			adu.setEsntlId(esntlId);
			
			// 2. 패스워드 암호화 (ESNTL_ID를 키로 사용)
			String encryptedPassword = EgovFileScrty.encryptPassword(
				adu.getPassword(), 
				esntlId  // ESNTL_ID를 암호화 키로 사용
			);
			adu.setPassword(encryptedPassword);
			
			// 3. 사용자 상태 코드 설정
			if (adu.getEmplyrSttusCode() == null) {
				adu.setEmplyrSttusCode("P"); // 승인상태
			}
			
			// 4. 두 테이블에 각각 저장
			int result1 = adminusermapper.AdminUserIns(adu);
			int result2 = adminusermapper.insertUserMaster(adu);
			
			return result1 + result2;
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("사용자 등록 중 오류가 발생했습니다: " + e.getMessage());
		}
	}
}
