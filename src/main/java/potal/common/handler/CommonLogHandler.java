package potal.common.handler;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import potal.common.service.ServiceMap;

/**  
 * @Class Name : CommonLogHandler.java
 * @Description : CommonLogHandler Class
 * @Modification Information  
 * 
 *  Copyright (C) by MOPAS All right reserved.
 */
public class CommonLogHandler extends HandlerInterceptorAdapter
{
    protected Logger log = LoggerFactory.getLogger(this.getClass());
    
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception
    {
    	String userId = "";
    	HttpSession session  =  req.getSession(false);
    	if(session != null){
    		userId = "[" + session.getAttribute("userid").toString() + "] ";
    	}

    	log.info("Req" + userId + ") " + req.getRequestURI());
    	
		Enumeration<?> parameterNames = req.getParameterNames();
		StringBuilder paramStr = new StringBuilder();
		boolean isSP = false;
		while (parameterNames.hasMoreElements())
		{
			String parameterName = (String)parameterNames.nextElement();
			String[] parameterValues = req.getParameterValues(parameterName);
			
			if(parameterValues.length == 1)
			{
				if(parameterName.equals("sp"))
				{
					paramStr.append("EXEC " + parameterValues[0] + " ");
					isSP = true;
				}
				else if(parameterName.equals("ac"))
				{
					paramStr.append(parameterName);
					paramStr.append(":");
					paramStr.append(parameterValues[0] +"("+ServiceMap.getQueryId(parameterValues[0])+") ");
				}
				else
				{
					if(isSP)
					{
						paramStr.append("'"+parameterValues[0]+"', ");
					}
					else
					{
						paramStr.append(parameterName);
						paramStr.append(":");
						paramStr.append(parameterValues[0] +" ");
					}
				}
			}
			else
			{
				paramStr.append(parameterName);
				paramStr.append(":");
				paramStr.append(parameterValues + " ");
			}
		}
		
		String resultParams = paramStr.toString().trim();
		if(resultParams.endsWith(",")) resultParams = resultParams.substring(0, resultParams.length() - 1);
		
		log.info("Param) " + resultParams);
    	
    	return true;
    }
}