package egovframework.com.egowaeyo.bbsMaster.mapper;

import java.util.List;
import java.util.Map;

import egovframework.com.egowaeyo.bbsMaster.VO.BoardMasterVO;

public interface BbsMasterMapper {
   List<Map<String, String>> selectCombbs();
   
   List<BoardMasterVO> selectDeptEmp();

   List<Map<String, String>> selectGroupedBbsData();

   public int insertBBSMaster(BoardMasterVO boardMaster);

   public int insertBBSMasterAuth(BoardMasterVO auth);

   void insertCommonDetailCode(String bbsTyCode, String bbsName, String useAt);

   String getMaxBbsTyCode();

}
