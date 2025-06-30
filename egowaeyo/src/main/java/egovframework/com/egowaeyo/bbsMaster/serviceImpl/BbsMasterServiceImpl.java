package egovframework.com.egowaeyo.bbsMaster.serviceImpl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.com.egowaeyo.bbsMaster.VO.BoardMasterVO;
import egovframework.com.egowaeyo.bbsMaster.mapper.BbsMasterMapper;
import egovframework.com.egowaeyo.bbsMaster.service.BbsMasterService;

@Transactional
@Service
public class BbsMasterServiceImpl implements BbsMasterService {
   @Autowired
   BbsMasterMapper bbsMasterMapper;

   // 상위게시판 가져오기(모달)
   @Override
   public List<Map<String, String>> getCombbs() {
      List<Map<String, String>> result = bbsMasterMapper.selectCombbs();
      return result;
   }

   // 부서, 사원 목록 가져오기(모달)
   @Override
    public List<BoardMasterVO> getDeptEmp() {
        return bbsMasterMapper.selectDeptEmp();
    }

   
   @Override
   public String getMaxBbsTyCode() {
      return bbsMasterMapper.getMaxBbsTyCode();
   }

   @Override
   public void insertCommonDetailCode(String bbsTyCode, String bbsName, String useAt) {
      bbsMasterMapper.insertCommonDetailCode(bbsTyCode, bbsName, useAt);
   }



   @Override
   public int insertBBSMaster(BoardMasterVO boardMaster) {
      BoardMasterVO auth = new BoardMasterVO();
      bbsMasterMapper.insertBBSMaster(boardMaster);
      bbsMasterMapper.insertBBSMasterAuth(auth);
      return 0;
   }

   @Override
   public int insertBBSMasterAuth(BoardMasterVO auth) {

      return 0;
   }

   // 사이드바
   @Override
   public Map<String, List<String>> getGroupedBbsData() {
	   List<Map<String, String>> rawData = bbsMasterMapper.selectGroupedBbsData();
	   
	   // 키 이름을 소문자로 변환하여 접근
	   Map<String, List<String>> groupedData = rawData.stream().filter(item -> item.get("CODENM") != null) // null 키
			   // 필터링
			   .collect(Collectors.groupingBy(item -> item.get("CODENM"), // 대소문자 구분 문제 해결
					   Collectors.mapping(item -> item.getOrDefault("BBSNM", "UNKNOWN"), Collectors.toList())));
	   return groupedData;
   }
}
