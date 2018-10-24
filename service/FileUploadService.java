/**
	@file	FileUploadService.java
	@date	2017-01-01
	@author	UBCare CRM R&D TF
	@brief	
*/
package potal.core.service;

import org.springframework.web.multipart.MultipartFile;
import potal.core.model.UploadResult;

public class FileUploadService extends AbstractFileUploadService 
{
	@Override
	public UploadResult saveFiles() {
		log.info("--------------> File Upload Start!!");
		log.info("--------------> saveFiles()");
		
		int fileId;
		PathInfo uploadInfo;
		super.result = new UploadResult();
		
		for(int i = 0; i < files.size(); i++)
		{
			MultipartFile file = files.get(i);
			if(!file.isEmpty())
			{
				// 파일 생성
				uploadInfo = getUploadPath(file.getOriginalFilename());
				
				if((fileId = saveFile(file, uploadInfo)) > 0)
				{
					result.oriFileNameList.add(uploadInfo.originName);
					result.newFileNameList.add(uploadInfo.saveName);
					result.fileURLList.add(uploadInfo.getSaveURL());
					result.fileIDList.add(String.valueOf(fileId));
					result.fileSizeList.add(String.valueOf(file.getSize()));
				}
			}
		}
		return super.result;
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