/**
	@file	FileDeleteService.java
	@date	2017-01-01
	@author	UBCare CRM R&D TF
	@brief	
*/
package potal.core.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import potal.common.common.PotalParamMap;
import potal.common.dao.CommonDAO;
import potal.common.service.ComnConst;
import potal.common.service.ServiceMap;

public class FileDeleteService
{
    protected Logger log = LoggerFactory.getLogger(this.getClass());
	
	int fileId;
	CommonDAO commonDAO;
	
	public int getFileId() { return fileId; }
	public CommonDAO getCommonDAO() { return commonDAO; }
	
	public void setFileId(int fileId) { this.fileId = fileId; }
	public void setCommonDAO(CommonDAO commonDAO) { this.commonDAO = commonDAO; }
	
	@SuppressWarnings("unchecked")
	public int deleteFile(HttpServletRequest request)
	{
		try
		{
			List<PotalParamMap> list;
			PotalParamMap param = new PotalParamMap();
			
			param.put(ComnConst.DIRECT_SP_NAME, "P_sysAttachFiles_S");
			param.put(ComnConst.DIRECT_SP_PARAM,
					new String[] {
						"D", 
						String.valueOf(fileId), 
						"", 
						"", 
						"", 
						request.getSession().getAttribute("userid").toString()} 
			);
			
			list = (List<PotalParamMap>)commonDAO.list(ServiceMap.getQueryId(ServiceMap.AJAX_DIRECT_SP), param);
			
			if(1 == list.get(0).size()) return ComnConst.RETURN_CODE_SUCCESS;
			
		}
		catch(Exception ex)
		{
			
		}
		return ComnConst.RETURN_CODE_FAIL;
	}
}