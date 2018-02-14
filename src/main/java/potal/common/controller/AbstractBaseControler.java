package potal.common.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import potal.common.common.PotalParamMap;
import potal.common.dao.CommonDAO;
import potal.common.service.ServiceMap;
import potal.common.util.QueryResult;
import potal.common.util.Utility;

/**  
 * @Class Name : AbstractBaseControler.java
 * @Description : 
 * @Modification Information 
 * 
 * Copyright (C) by UBCARE All right reserved.
 */
public abstract class AbstractBaseControler
{
	protected Logger log  = LoggerFactory.getLogger(this.getClass());
	
	@Autowired protected ApplicationContext appCtx;
	@Autowired protected ServletContext svlCtx;
	
	/** CommonDAO */
    @Resource(name="commonDAO")
    protected CommonDAO commonDAO;

    protected ResponseEntity<String> requestAjax(String dao, HttpServletRequest request)
    {    	
    	ArrayList<QueryResult> resultList = requestProcCall(dao, request);
    	if(resultList == null)
    	{
    		return new ResponseEntity<String>("[[]]", HttpStatus.OK);
    	}
    	else
    	{
    		return Utility.getJSONResponse(resultList.size() == 1 ? resultList.get(0) : resultList);
    	}
    }
    
    @SuppressWarnings("unchecked")
    protected ArrayList<QueryResult> requestProcCall(String dao, HttpServletRequest request)
    {
    	PotalParamMap param = new PotalParamMap();
    	try
    	{
    		param = Utility.getRequestMap(request);
    		
    		ArrayList<QueryResult> resultList = new ArrayList<QueryResult>();
    		List<PotalParamMap> result = (List<PotalParamMap>)commonDAO.list(ServiceMap.getQueryId(ServiceMap.AJAX_DIRECT_SP), param);
    		
    		resultList.add(new QueryResult(param, result));
    		
    		return resultList;
    	}
    	catch(Exception ex)
    	{
    		StackTraceElement[] list = ex.getStackTrace();
    		
    		if(list.length > 0)
    		{
    			log.error(String.format("%s at %s - %d", ex.getClass().getName(), list[0].getFileName(), list[0].getLineNumber()));
    			ex.printStackTrace();
    		}
    		return null;
    	}
    }
}