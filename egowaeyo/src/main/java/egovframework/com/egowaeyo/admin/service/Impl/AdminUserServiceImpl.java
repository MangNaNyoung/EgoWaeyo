package egovframework.com.egowaeyo.admin.service.Impl;

import java.util.List;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.idgnr.EgovIdGnrService;
import org.springframework.stereotype.Service;

import egovframework.com.egowaeyo.admin.mapper.AdminUserMapper;
import egovframework.com.egowaeyo.admin.service.AdminUserService;
import egovframework.com.egowaeyo.admin.service.AdminUserVO;
import egovframework.com.egowaeyo.admin.service.DeptVO;
import egovframework.com.egowaeyo.admin.service.EgovDeptVO;
import egovframework.com.egowaeyo.admin.service.PosVO;
import egovframework.com.sec.drm.service.DeptAuthor;
import egovframework.com.sec.drm.service.impl.DeptAuthorDAO;
import egovframework.com.utl.fcc.service.EgovStringUtil;
import egovframework.com.utl.sim.service.EgovFileScrty;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {
	
	final AdminUserMapper adminusermapper;
	
	/** egovUsrCnfrmIdGnrService */
	@Resource(name="egovUsrCnfrmIdGnrService")
	private EgovIdGnrService idgenService;
	
	@Resource(name="deptAuthorDAO")
    private DeptAuthorDAO deptAuthorDAO;
	
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
			// 1. ESNTL_ID 설정 uniqId 가져옴
			String uniqId = idgenService.getNextStringId();
			adu.setEsntlId(uniqId);
			
			// 2. 패스워드 암호화 (ESNTL_ID를 키로 사용)
			String encryptedPassword = EgovFileScrty.encryptPassword(
				adu.getPassword(), 
				adu.getEmplyrId()  // ESNTL_ID를 암호화 키로 사용
			);
			adu.setPassword(encryptedPassword);
		
			// 4. 사용자 등록 추가
			int result = adminusermapper.AdminUserIns(adu);
			
			// 5. 권한 부여
			DeptAuthor deptAuthor = new DeptAuthor();
			deptAuthor.setUniqId(uniqId);
			deptAuthor.setAuthorCode("ROLE_USER");
			deptAuthorDAO.insertDeptAuthor(deptAuthor);
			
			return result;
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("사용자 등록 중 오류가 발생했습니다: " + e.getMessage());
		}
	}

	@Override
	public List<EgovDeptVO> getEgovDept(EgovDeptVO edpt) {
		return adminusermapper.getEgovDept(edpt);
	}
}
