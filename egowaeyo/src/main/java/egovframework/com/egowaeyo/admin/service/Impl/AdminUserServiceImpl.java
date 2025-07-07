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
		// 패스워드 암호화
		try {
			String pass = EgovFileScrty.encryptPassword(adu.getPassword(), EgovStringUtil.isNullToString(adu.getEmplyrId()));
			adu.setPassword(pass);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//KISA 보안약점 조치 (2018-10-29, 윤창원)	
		
		return adminusermapper.AdminUserIns(adu);
	}

}
