package potal.common.controller;

import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**  
 * @Class Name : AjaxControler.java
 * @Description : Ajax 컨트롤러
 * @Modification Information  
 * 
 *  Copyright (C) by MOPAS All right reserved.
 */
@Controller
public class AjaxControler extends AbstractBaseControler
{    
	/** SystemProperties */
	@Resource(name = "systemProp")
	private Properties systemProp;
	
	@RequestMapping(value = {"/SWNAjaxL.do"})
    public ResponseEntity<String> callAjaxList(HttpServletRequest request, ModelMap model) throws Exception
	{
		return super.requestAjax("", request);
    }
}