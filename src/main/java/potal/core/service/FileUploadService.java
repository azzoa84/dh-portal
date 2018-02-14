/**
	@file	FileUploadService.java
	@date	2017-01-01
	@author	UBCare CRM R&D TF
	@brief	
*/
package potal.core.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import potal.common.common.PotalParamMap;
import potal.common.service.ComnConst;
import potal.common.service.ServiceMap;

@SuppressWarnings("unchecked")
public class FileUploadService extends AbstractFileUploadService 
{
	@Override
	protected int getNewFileId(MultipartFile file, PathInfo savePath)
	{
		List<PotalParamMap> saveResult;
		PotalParamMap param = new PotalParamMap();
		param.put(ComnConst.DIRECT_SP_NAME, "P_sysAttachFiles_S");
		param.put(ComnConst.DIRECT_SP_PARAM,
				new String[] {
					"N", 
					"0", 
					file.getOriginalFilename(), 
					savePath.getSaveURL(), 
					String.valueOf(file.getSize()), 
					request.getSession().getAttribute("userid").toString()} 
		);
		
		saveResult = (List<PotalParamMap>)commonDAO.list(ServiceMap.getQueryId(ServiceMap.AJAX_DIRECT_SP), param);
				
		if(saveResult.size() > 0) return Integer.valueOf(saveResult.get(0).get("newId").toString());
		else return ComnConst.RETURN_CODE_FAIL;
	}

	@Override
	public String getResultScript()
	{
		StringBuffer buff = new StringBuffer();
		
		try
		{
			buff.append(String.format("%s\n",  new String(result.getJsonString().getBytes("UTF-8"), "ISO-8859-1")));
			return buff.toString();
		}
		catch(Exception ex)
		{
			return "{}";
		}
	}
}