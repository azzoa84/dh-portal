package potal.common.service;

/**  
 * @Class Name : ComnConst.java
 * @Description : 상수
 * @Modification Information  
 * @
 * @  수정일      수정자              수정내용
 * @ ---------   ---------   -------------------------------
 * 
 * Copyright (C) by UBCARE All right reserved.
 */
public class ComnConst
{	
	public static final String ARGS_RETURN_CODE = "errorCode";
	public static final String ARGS_RETURN_STRING = "returnStr";
	public static final String ARGS_ERROR_MSG = "errorMsg";
	public static final String RESULT_LIST = "resultList";

	public static final String ARGS_AJAX_CODE = "ac";
	public final static String ARGS_SP_NAME = "sp";	
	public final static String DIRECT_SP_NAME = "spCall";
	public final static String DIRECT_SP_PARAM = "params";
	
	public final static String SESS_USER_ID = "userid";
	public final static String SESS_DEPT_ID = "deptcode";
	
	public final static String CRYPTO_SEPARATOR_CHAR = "¿";
	
	public final static String PARAM_TYPE_BASE = "BASE";
	public final static String PARAM_TYPE_UBSALES = "UBSALES";
	
	public static final int RETURN_CODE_AUTHENTICATE_FAIL	= -1;				/**< 응답코드:인증실패 **/	
	public static final int RETURN_CODE_FAIL				= 0;				/**< 응답코드:실패 **/
	public static final int RETURN_CODE_SUCCESS				= 1;				/**< 응답코드:성공**/
	public static final int RETURN_CODE_LOADING				= 2;				/**< 응답코드:로딩중 **/
	public static final int RETURN_CODE_PK_VIOLATION		= 23000;			/**< 응답코드:중복키 에러 **/
}