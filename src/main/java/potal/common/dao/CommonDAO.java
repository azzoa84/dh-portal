package potal.common.dao;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

import potal.common.common.CommandMap;
import potal.common.dao.AbstractDAO;

@Repository("commonDAO")
public class CommonDAO extends AbstractDAO{
	
	/**
	 * 로그인 아이디, 비밀번호 체크
	 * 
	 */
	public String logInfoCheck(Map<String, Object> map) throws Exception{
		return (String)selectOne("common.logChk", map);
	}
	
	/**
	 * 파일 다운로드
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> selectFileInfo(Map<String, Object> map) throws Exception{
	    return (Map<String, Object>)selectOne("common.selectFileInfo", map);
	}
	
	//@SuppressWarnings("unchecked")
	@SuppressWarnings("rawtypes")
	public List list(String queryId, Object map)
    {
		return selectList(queryId, map);
    }
}
