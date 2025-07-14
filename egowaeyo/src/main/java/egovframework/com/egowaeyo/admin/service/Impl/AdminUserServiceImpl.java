package egovframework.com.egowaeyo.admin.service.Impl;

import java.util.List;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.idgnr.EgovIdGnrService;
import org.springframework.stereotype.Service;

import egovframework.com.egowaeyo.admin.mapper.AdminUserMapper;
import egovframework.com.egowaeyo.admin.service.AdminUserService;
import egovframework.com.egowaeyo.admin.service.AdminUserVO;
import egovframework.com.egowaeyo.admin.service.EgovDeptVO;
import egovframework.com.egowaeyo.admin.service.PosVO;
import egovframework.com.sec.drm.service.DeptAuthor;
import egovframework.com.sec.drm.service.impl.DeptAuthorDAO;
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
			
			// 2. 사번 생성(EmplNo)
			String nextEmplNo = adminusermapper.getNextEmplNo();
			adu.setEmplNo(nextEmplNo);
			System.out.println("생성된 사번 : " + nextEmplNo);
			
			// 3. 패스워드 암호화 (ESNTL_ID를 키로 사용)
			String encryptedPassword = EgovFileScrty.encryptPassword(
				adu.getPassword(), 
				adu.getEmplyrId()  // ESNTL_ID를 암호화 키로 사용
			);
			adu.setPassword(encryptedPassword);
			
			// 4. 권한 기본값 설정
//			if (adu.getAuthorCode() == null || adu.getAuthorCode().isEmpty()) {
//				adu.setAuthorCode("ROLE_USER"); // 기본값 권한 사용자로
//			}
//			System.out.println("설정된 권한 : " + adu.getAuthorCode());
		
			// 5. 사용자 등록 추가
			int result = adminusermapper.AdminUserIns(adu);
			
			// 6. 권한 부여
			
			 DeptAuthor deptAuthor = new DeptAuthor(); deptAuthor.setUniqId(uniqId);
			 deptAuthor.setAuthorCode(adu.getAuthorCode());
			 deptAuthorDAO.insertDeptAuthor(deptAuthor);
			 
			 System.out.println("권한 등록 완료 : " + adu.getAuthorCode());
			 
			
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

	@Override
	public String getNextEmplNo() {
		return adminusermapper.getNextEmplNo();
	}

	@Override
	public String getNextOrgnztId() {
		return adminusermapper.getNextOrgnztId();
	}

	@Override
	public int DeptIns(String orgnztNm) {
		
		try {
			String NextOrgnztId = adminusermapper.getNextOrgnztId();
			System.out.println("생성된 다음 조직ID 번호 : " + NextOrgnztId);
			
			EgovDeptVO edpt = new EgovDeptVO();
			edpt.setOrgnztId(NextOrgnztId);
			edpt.setOrgnztNm(orgnztNm);
			
			return adminusermapper.DeptIns(edpt);
		} catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException("부서 등록 중 오류 발생하였어요. : " + e.getMessage());
			
		}
		
	}

	@Override
	public int DeptUdt(String orgnztId, String orgnztNm) {
		
		try {
			EgovDeptVO edpt = new EgovDeptVO();
			edpt.setOrgnztId(orgnztId);
			edpt.setOrgnztNm(orgnztNm);
			
			return adminusermapper.DeptUdt(edpt);
		} catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException("부서 수정 중 오류 발생하였어요. : " + e.getMessage());
		}
		
	}

	@Override
	public int DeptDel(String orgnztId) {
		
		try {
			return adminusermapper.DeptDel(orgnztId);
		} catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException("부서 삭제 중 오류 발생하였어요. : " + e.getMessage());
		}
	}

	// [부서관리 페이지] 전체 사용자 조회
	@Override
	public List<AdminUserVO> selectemp(AdminUserVO au) {
		return adminusermapper.selectemp(au);
	}

	// [부서관리 페이지] 사용자 삭제
	@Override
	public int EmpDel(String emplyrId) {
		return adminusermapper.EmpDel(emplyrId);
	}
	
	// [부서관리 페이지] 사용자 수정
	@Override
	public int EmpUdt(String emplyrId, String positionId) {
	    try {
	        return adminusermapper.EmpUdt(emplyrId, positionId);
	    } catch(Exception e) {
	        e.printStackTrace();
	        throw new RuntimeException("사용자 수정 중 오류 발생하였어요. : " + e.getMessage());
	    }
	}

}
