/**
	@file	AbstractFileUploadService.java
	@date	2017-01-01
	@author	UBCare CRM R&D TF
	@brief	
*/
package potal.core.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import potal.common.common.PotalParamMap;
import potal.common.dao.CommonDAO;
import potal.common.service.ComnConst;
import potal.common.service.ServiceMap;
import potal.core.model.UploadResult;

public abstract class AbstractFileUploadService
{
	protected UploadResult result;
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	
	private final static HashMap<String, String> FILE_SUB_CATEGORY = new HashMap<String, String>();
	static
    {
		FILE_SUB_CATEGORY.put("excel", "/Excel");
    }
	
	protected class PathInfo
	{
		final String originName, logicalPath, physicalPath, saveName;
		String params;
		
		PathInfo(String logical, String physical, String originName)
		{
			this.logicalPath = logical;
			this.physicalPath = physical;
			this.originName = originName;
			this.saveName = java.util.UUID.randomUUID().toString();
		}
		
		String getSaveURL() { return String.format("%s%s", logicalPath, saveName); }
		String getSavePath() { return String.format("%s/%s", physicalPath, saveName); }
		String getOriginalName() { return this.originName; }
	}
	
	CommonDAO commonDAO;
	List<MultipartFile> files;
	HttpServletRequest request;
	String filePath;
	String category;
	
	public List<MultipartFile> getFiles() { return files; }
	
	public void setRequest(HttpServletRequest request) { this.request = request; }
	public void setCommonDAO(CommonDAO commonDAO) { this.commonDAO = commonDAO; } 
	public void setFiles(List<MultipartFile> files) { this.files = files; }
	public void setFilePath(String filePath) { this.filePath = filePath; }
	public void setSubPath(String category) { this.category = category; }
	
	public AbstractFileUploadService() { }
	
	public PathInfo getUploadPath(String originName)
	{
		log.info("--------------> getUploadPath() 파일을 저장 경로 설정");
		File fDir; 
		Calendar today = Calendar.getInstance();
		String logicalPath, physicalPath;
		
		String subDir = FILE_SUB_CATEGORY.get(category) == null ? "" : FILE_SUB_CATEGORY.get(category);
		String uploadRoot = subDir;
		// DB에 저장될 경로
		logicalPath = String.format("%s/%d/%02d/%02d/"
				, uploadRoot
				, today.get(Calendar.YEAR)
				, today.get(Calendar.MONTH) + 1
				, today.get(Calendar.DAY_OF_MONTH));
			
		// 실제 파일이 저장될 경로
		/*physicalPath = String.format("%s/%d/%02d/%02d/"
				, filePath + subDir
				, today.get(Calendar.YEAR)
				, today.get(Calendar.MONTH) + 1
				, today.get(Calendar.DAY_OF_MONTH));*/
		
		
		physicalPath = filePath + logicalPath;
		System.out.println(physicalPath);
		fDir = new File(physicalPath);
		if(!fDir.exists()) fDir.mkdirs();
		
		return new PathInfo(logicalPath, physicalPath, originName);
	}
	
	public int saveFile(MultipartFile file, PathInfo savePath)
	{
		log.info("--------------> saveFile() 파일 생성 ");
		try
		{
			int iRead;
			byte[] buff = new byte[4096];
			InputStream in = file.getInputStream();
			File saveFile = new File(savePath.getSavePath());
			FileOutputStream out = new FileOutputStream(saveFile);
			while((iRead = in.read(buff)) > 0) out.write(buff, 0, iRead);
			out.close();
			in.close();
			return getNewFileId(file, savePath);
		}
		catch(Exception ex)
		{
			log.info(ex.toString());
			log.info(ex.getMessage());
		}
		return 0;
	}
	
	@SuppressWarnings("unchecked")
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
		System.out.println("---> " + saveResult);
		if(saveResult.size() > 0) return Integer.valueOf(saveResult.get(0).get("newId").toString());
		else return ComnConst.RETURN_CODE_FAIL;
	}

	/**
	 * 파일 저장
	 * @return
	 */
	public abstract UploadResult saveFiles();	
	
	public abstract String getResultScript();	
}