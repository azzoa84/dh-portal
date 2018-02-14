/**
	@file	FileController.java
	@date	2014-08-19
	@author	UBCare CRM R&D TF
	@brief	
*/
package potal.core.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.View;

import potal.common.controller.AbstractBaseControler;
import potal.common.model.UserModel;
import potal.core.common.FileDownload;
import potal.core.model.FileUploadForm;
import potal.core.service.AbstractFileUploadService;
import potal.core.service.AbstractXLFileUploadService;
import potal.core.service.ExcelDataUploadService;
import potal.core.service.FileDeleteService;
import potal.core.service.FileUploadService;

 /**  
 * @Class Name : FileController.java
 * @Description : 파일 컨트롤러
 * @Modification Information  
 * @
 * @  수정일      수정자              수정내용
 * @ ---------   ---------   -------------------------------
 * 
 * @author 김성현
 * @since 2014.08.19
 * @version 1.0
 * @see
 * 
 *  Copyright (C) by MOPAS All right reserved.
 */
@Controller
@SessionAttributes(types = UserModel.class)
public class FileController extends AbstractBaseControler
{
	@Resource(name = "systemProp")
	private Properties systemProp;		/** SystemProperties */
	
	@RequestMapping(value = {"/fileDownload.do"})
    public FileDownload fileDownload(HttpServletRequest request, ModelMap model) throws Exception
	{
		String strUploadDir = systemProp.getProperty("UPLOAD.DIR");
		
		model.put("DAO", commonDAO);
		model.put("uploadDir",  strUploadDir);
		
    	return new FileDownload();
    }
	
	@RequestMapping(value="/fileDelete.do")
	@ResponseBody
	public String fileDelete(HttpServletRequest request)
	{
		FileDeleteService svc = new FileDeleteService();
		svc.setFileId(Integer.valueOf(request.getParameter("file_id")));
		svc.setCommonDAO(commonDAO);
		return String.valueOf(svc.deleteFile(request));
	}
	
	@RequestMapping(value="/fileUpload.do")
	@ResponseBody
	public void fileUpload(@ModelAttribute("uploadForm")FileUploadForm uploadForm, HttpServletResponse res, HttpServletRequest req)
	{
		try
		{
			/*Exception e = (Exception)req.getAttribute("exception");
			if( e instanceof MaxUploadSizeExceededException ) {
				test(res, req);
			} else {
				test(res, req);
			}*/
			
			uploadFileProc(uploadForm, res, req, new FileUploadService());
		}
		catch(Exception ex)
		{
			System.out.println("catch11");
			log.info(ex.toString());
		}
	}

	private void uploadFileProc(FileUploadForm uploadForm,
								HttpServletResponse res,
								HttpServletRequest req,
								AbstractFileUploadService svc) throws IOException, UnsupportedEncodingException
	{
		String filePath = systemProp.getProperty("UPLOAD.DIR");
		ServletOutputStream stm;
		svc.setFiles(uploadForm.getFiles());
		svc.setFilePath(filePath);
		svc.setCommonDAO(commonDAO);
		svc.setRequest(req);
		svc.setSubPath("");
		svc.saveFiles();
		
		res.setHeader("Content-Type", "text/html; charset=utf-8");
		
		// ServletOutputStream 은 추상클래스이기 떄문에 getOutputStream()이라는 함수를 통해 servletOutputStream 인스턴스를 받아서 사용해야한다.
		stm = res.getOutputStream();
		stm.println(svc.getResultScript());
		stm.close();
	}
	
	@RequestMapping(value="/excelUpload.do")
	@ResponseBody
	public void excelUpload(@ModelAttribute("uploadForm")FileUploadForm uploadForm, HttpServletResponse res, HttpServletRequest req)
	{
		try
		{
			uploadExcelProc(uploadForm, res, req, new ExcelDataUploadService());
		}
		catch(Exception ex)
		{
			log.info(ex.toString());
		}
	}
	
	private void uploadExcelProc(FileUploadForm uploadForm,
				 				HttpServletResponse res,
		 						HttpServletRequest req,
		 						AbstractFileUploadService svc) throws IOException, UnsupportedEncodingException
	{
		String filePath = systemProp.getProperty("UPLOAD.DIR");
		ServletOutputStream stm;
		svc.setFiles(uploadForm.getFiles());
		svc.setFilePath(filePath);
		svc.setCommonDAO(commonDAO);
		svc.setRequest(req);
		svc.setSubPath("excel");
		
		svc.excelDataFiles();
		
		res.setHeader("Content-Type", "text/html; charset=utf-8");
		
		// ServletOutputStream 은 추상클래스이기 떄문에 getOutputStream()이라는 함수를 통해 servletOutputStream 인스턴스를 받아서 사용해야한다.
		stm = res.getOutputStream();
		stm.println(svc.getResultScript());
		stm.close();
	}
}