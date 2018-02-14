/**
	@file	ServiceMap.java
*/
package potal.common.service;

import java.util.HashMap;
/**
 * 웹서비스에서 사용되는 모든 AJAX 코드를 정의하는 정적 클래스
 * 새로운 데이터 조회를 추가할 때는 AJAX_로 시작하는 정적 멤버를 하나 생성하되 변수값(Ajax 코드)이 중복되는 경우가 없도록 한다.
 * 그리고 static{} 블록 안에 해당 Ajax 코드와 SQL Map에 정의된 쿼리 아이디를 맵핑해주도록 한다. 
 */
public final class ServiceMap
{	
	private final static HashMap<String, String> AJAX_MAP = new HashMap<String, String>();
	
	/****************************************
	 * 공통
	 ****************************************/
	public static final String AJAX_LOGIN = "COMM01"; 			//로그인
	public static final String AJAX_GET_MENUS = "COMM02";		//메뉴목록
	public static final String AJAX_DIRECT_SP = "_DSP_";	//SP 직접호출
	
	//AJAX 호출 MAP 작성
	static
    {
		AJAX_MAP.put(AJAX_LOGIN, "common.logChk");
		AJAX_MAP.put(AJAX_GET_MENUS, "common.userMenu");
		AJAX_MAP.put(AJAX_DIRECT_SP, "common.directSP");
    }
	
	public static String getQueryId(String ajaxCode)
	{
		if(AJAX_MAP.containsKey(ajaxCode))
		{
			return AJAX_MAP.get(ajaxCode);
		}
		else
		{
			return "";
		}
	}
	
	public static boolean canExecuteSP(String sp)
	{
		try
		{
			//DirectSP.class.getField(sp); 
			return true;
		}
		catch(Exception ex)
		{
			return false;
		}
	}
}