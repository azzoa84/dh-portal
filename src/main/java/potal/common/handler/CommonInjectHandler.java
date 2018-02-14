package potal.common.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import potal.common.model.UserModel;
import potal.common.util.Utility;

/**  
 * @Class Name : CommonInjectHandler.java
 * @Description : CommonInjectHandler Class
 * @Modification Information  
 * 
 *  Copyright (C) by MOPAS All right reserved.
 */
public class CommonInjectHandler extends HandlerInterceptorAdapter
{
    protected Logger log = LoggerFactory.getLogger(this.getClass());
    
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception
    {
    	try
    	{
    		if(SecurityContextHolder.getContext().getAuthentication() != null)
    		{
    			UserModel userSession = Utility.getPrincipal();
    			req.setAttribute("userSession", userSession);
    		}
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	return true;
    }
}