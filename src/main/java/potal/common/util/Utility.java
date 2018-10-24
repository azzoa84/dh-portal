/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package potal.common.util;

import java.util.ArrayList;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.sf.json.JSONSerializer;
import potal.common.common.CommandMap;
import potal.common.common.PotalParamMap;
import potal.common.dao.CommonDAO;
import potal.common.model.UserModel;
import potal.common.service.ComnConst;
import potal.common.service.ExcelHeaderConst;

/**  
 * @Class Name : Utility.java
 * @Description : Utility Class
 * @Modification Information  
 * 
 *  Copyright (C) by MOPAS All right reserved.
 */
public class Utility
{
    @Resource(name="commonDAO")
    protected CommonDAO commonDAO;
    
    private final static HttpHeaders _jsonHeader = new HttpHeaders();
    static
    {
    	_jsonHeader.add("Content-Type", "application/json; charset=UTF-8");
    }    

    public static String serialUID = "6723434363565852261R";
	static protected Logger log = LoggerFactory.getLogger(Utility.class);
			
	private static String getSessionAttribute(HttpSession session, String key)
	{
		String result = (String)session.getAttribute(key);
		return result == null ? "" : result;
	}
	
	public static UserModel getPrincipal()
    {
    	try
    	{
    		return (UserModel)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	}
    	catch(Exception ex)
    	{
    		return null;
    	}
    }
	
	/**
	 *  JSON VIEW 설정
	 *  객체(자주사용하는 List, Map, VO 등등)를 JSON형태로 Serializer(String으로 변환)한 뒤
	 *	Header에 JSON형태라 명시해주면, JSON View로 인식하게 된다.
	 *  
	 *  JSON변환 후 ResponseEntity로 반환.
	 *  또다른 방법으로는 @ResponseBody 가 있다
	 */
    public static ResponseEntity<String> getJSONResponse(Object obj)
    {
    	return new ResponseEntity<String>(getJSONString(obj), _jsonHeader, HttpStatus.OK);
    }
     
    public static String getJSONString(Object obj)
    {
        return JSONSerializer.toJSON(obj).toString();
    }
        
	public static String randomString(int len)
	{
		String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		
		StringBuilder sb = new StringBuilder(len);
		for(int i = 0; i < len; i++)
		{
			sb.append(AB.charAt(new Random().nextInt(AB.length())));
		}
		return sb.toString();
	}
	
	public static JsonObject convertGoogleJsonObject(String jsonParameter)
	{
		String evalJson = jsonParameter.replaceAll("&quot;", "\"").replaceAll("&apos;", "\'");		
		return (evalJson != null && !evalJson.equals("")) ? (JsonObject) new JsonParser().parse(evalJson) : null;
	}

    public static long sublong(String value, int beginIndex, int endIndex)
    {
        String substring = value.substring(beginIndex, endIndex);
        return (substring.length() > 0) ? Long.parseLong(substring) : -1;
    }
    
    public static PotalParamMap getRequestMap(HttpServletRequest request)
	{
    	int iParams = 1;
    	PotalParamMap pMap = null;
		if(request.getParameter(ComnConst.ARGS_SP_NAME) != null)
		{
			pMap = new PotalParamMap();
			String spName = "", prmName, prmValues;
			ArrayList<String> paramList = new ArrayList<String>();
			
			
			spName = request.getParameter(ComnConst.ARGS_SP_NAME).toString();
			prmName = "p1";
			
			while((prmValues = request.getParameter(prmName)) != null)
			{
				paramList.add(prmValues);
				prmName = String.format("p%d", ++iParams);
			}
			
			pMap.put(ComnConst.DIRECT_SP_NAME, spName);
			pMap.put(ComnConst.DIRECT_SP_PARAM, paramList);
			
			if(pMap != null)
			{
				pMap.put(ComnConst.ARGS_RETURN_CODE, "");
				pMap.put(ComnConst.ARGS_RETURN_STRING, "");
			}
		}
		
		return pMap;
	}    
}