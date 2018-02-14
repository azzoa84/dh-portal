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
	
	@Override
	protected int getNewFileId(MultipartFile file, PathInfo savePath) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	protected Sheet getSheet(MultipartFile file, PathInfo savePath, int intSheetNum)
	{
		Sheet sheet = null;
		
		Workbook book = null;
		
		FileInputStream fis = null;
		File fileObj = new File(savePath.getSavePath());
		try
		{
			fis = new FileInputStream(fileObj);
			
			book = checkExcelExt(fis, savePath.originName);
			if(book == null)
			{
				fis.close();
				return sheet;
			}
			
			sheet = book.getSheetAt(intSheetNum);
			
			fis.close();
			fileObj.delete();
		}
		catch(IOException ex)
		{
			log.info(String.format("Excel upload failed - %s", ex.toString()));
			return sheet;
		}
		catch(Exception ex)
		{
			log.info(String.format("Excel upload failed - %s", ex.toString()));
			return sheet;
		}
		
		return sheet;
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