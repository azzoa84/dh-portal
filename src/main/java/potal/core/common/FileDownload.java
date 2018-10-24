/**
	@file	FileDownload.java
	@date	2017-01-01
	@author	UBCare CRM R&D TF
	@brief	
*/
package potal.core.common;

import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.view.AbstractView;

import potal.common.common.PotalParamMap;
import potal.common.dao.CommonDAO;
import potal.common.service.ComnConst;
import potal.common.service.ServiceMap;

public class FileDownload extends AbstractView
{
    protected Logger log = LoggerFactory.getLogger(this.getClass());
	
	@SuppressWarnings("unchecked")
	@Override
	protected void renderMergedOutputModel(	Map<String, Object> model, HttpServletRequest req, HttpServletResponse res) throws Exception
	{		
		List<PotalParamMap> list;
		PotalParamMap param = new PotalParamMap();
		CommonDAO commonDAO = (CommonDAO)model.get("DAO");
		List<PotalParamMap> result = null;
		String strUploadDir = model.get("uploadDir").toString();
		
		param.put(ComnConst.DIRECT_SP_NAME, "P_sysAttachFiles_Q");
		param.put(ComnConst.DIRECT_SP_PARAM, new String[] { 
					"Q",  
					req.getParameter("file_id"),
					""
				});
		list = (List<PotalParamMap>)commonDAO.list(ServiceMap.getQueryId(ServiceMap.AJAX_DIRECT_SP), param);
		
		if(list.size() > 0) result = list;
		
		if(result == null || result.size() == 0) fileNotExists(res);
		else fileDownload(result, req, res, strUploadDir);
	}
	
	private void fileNotExists(HttpServletResponse res)
	{
		try
		{
			res.setContentType("text/html; Charset=UTF-8");
			PrintStream out = new PrintStream(res.getOutputStream());
			out.println("<script>alert('요청된 파일을 찾을 수 없습니다.');window.close();</script>");
			out.close();
		}
		catch(Exception ex)
		{
			log.info(ex.toString());
			log.info(ex.getMessage());
		}
	}
	
	private void fileDownload(List<PotalParamMap> map, HttpServletRequest req, HttpServletResponse res, String strUploadDir)
	{
		OutputStream out = null;
		FileInputStream read = null;
		String filePath = strUploadDir + map.get(0).get("filePath").toString();
		log.info("filePath="+filePath);
		try
		{
			read = new FileInputStream(filePath);
		}
		catch(Exception ex)
		{
			fileNotExists(res);
			return;
		}
		
		try
		{
			int iRead = 0;
			byte[] buff = new byte[4096];
			res.setContentType("application/octet-stream; charset=UTF-8");
			res.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode((String)map.get(0).get("fileName"), "utf-8") + "\";");
			log.info( (String) map.get(0).get("fileName"));
			out = res.getOutputStream();
			while(read != null && (iRead = read.read(buff)) > 0)
			{
				out.write(buff, 0, iRead);
			}
			read.close();
			out.flush();
			out.close();
		}
		catch(Exception ex)
		{
			
		}
	}	
}