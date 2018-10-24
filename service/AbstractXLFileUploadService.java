/**
	@file	AbstractXLFileUploadService.java
	@date	2017-01-01
	@author	UBCare CRM R&D TF
	@brief	
*/
package potal.core.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dom4j.dom.DOMDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

/**  
 * @Class Name : AbstractXLFileUploadService.java
 * @Description : 
 * @Modification Information  
 * @
 * @  수정일      수정자              수정내용
 * @ ---------   ---------   -------------------------------
 * 
 * @author UBCare CRM R&D TF
 * @since 2017.01.01
 * @version 1.0
 * @see
 * 
 * Copyright (C) by UBCARE All right reserved.
 */
public abstract class AbstractXLFileUploadService extends AbstractFileUploadService
{	
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	
	protected Workbook getBook(MultipartFile file, PathInfo savePath)
	{
		Workbook book = null;
		
		// FileInputStream - 파일로 부터 바이트로 입력받아, 바이트 단위로 출력할 수 있는 클래스
		FileInputStream fis = null;
		File fileObj = new File(savePath.getSavePath());
		try
		{
			fis = new FileInputStream(fileObj);
			
			book = checkExcelExt(fis, savePath.originName);
			if(book == null)
			{
				fis.close();
				return book;
			}
			
			fis.close();
			
			// 파일 삭제
			//fileObj.delete();
		}
		catch(IOException ex)
		{
			log.info(String.format("Excel upload failed - %s", ex.toString()));
			return book;
		}
		catch(Exception ex)
		{
			log.info(String.format("Excel upload failed - %s", ex.toString()));
			return book;
		}
		
		return book;
	}
	
	protected Workbook checkExcelExt(FileInputStream fis, String fileName)
	{
		Workbook book = null;
		
		try
		{
			if(fileName.toLowerCase().endsWith(".xls"))
			{
				book = new HSSFWorkbook(fis);
			}
			else if(fileName.toLowerCase().endsWith(".xlsx"))
			{
				book = new XSSFWorkbook(fis);
			}
		}
		catch(IOException ex)
		{
			log.info(String.format("Excel upload failed - %s", ex.toString()));
			return null;
		}
		catch(Exception ex)
		{
			log.info(String.format("Excel upload failed - %s", ex.toString()));
			return null;
		}
		return book;
	}
	
	protected String convertDOM2XML(DOMDocument doc)
	{
		StringWriter sw = new StringWriter();
		try
		{
			doc.write(sw);
			return sw.getBuffer().toString().replaceAll("\n|\r", "");
		}
		catch(Exception ex)
		{
			return "";
		}
	}

	abstract protected String getSheetXML(Sheet sheet);
	
	abstract protected boolean saveData(String strXML);	
}