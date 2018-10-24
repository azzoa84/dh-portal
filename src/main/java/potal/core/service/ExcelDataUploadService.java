/**
	@file	ExcelDataUploadService.java
	@date	2017-09-25
	@author	UBCare CRM R&D TF
	@brief	엑셀 데이터 업로드
*/
package potal.core.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

import potal.common.common.PotalParamMap;
import potal.common.service.ExcelHeaderConst;
import potal.core.model.UploadResult;

/**  
 * @Class Name : ExcelDataUploadService.java
 * @Description : 
 * 
 * Copyright (C) by UBCARE All right reserved.
 */
public class ExcelDataUploadService extends AbstractXLFileUploadService 
{
	@Override
	public UploadResult saveFiles() {
		log.info("--------------> Excel Upload Start!!");
		log.info("--------------> excelDataFiles()");
		
		int fileId;
		PathInfo uploadInfo;
		super.result = new UploadResult();
		
		for(int i = 0; i < files.size(); i++)
		{
			MultipartFile file = files.get(i);
			if(!file.isEmpty())
			{
				uploadInfo = getUploadPath(file.getOriginalFilename());
				uploadInfo.params = (String)request.getParameter("params");
				fileId = saveFile(file, uploadInfo);
				List<PotalParamMap> dataList = new ArrayList<PotalParamMap>();
				dataList = uploadExcel(file, uploadInfo);
				
				System.out.println(dataList);
				
				if(fileId > 0)
				{
					result.oriFileNameList.add(uploadInfo.originName);
					result.newFileNameList.add(uploadInfo.saveName);
					result.fileURLList.add(uploadInfo.getSaveURL());
					result.fileIDList.add(String.valueOf(fileId));
					result.fileSizeList.add(String.valueOf(file.getSize()));
					result.dataList.add(dataList);
				}
			}
		}
		
		return super.result;
	}
	
	protected List<PotalParamMap> uploadExcel(MultipartFile file, PathInfo savePath)
	{		
		log.info("--------------> ExcelDataUploadService : uploadExcel()");
		int intResult = 0;
		Workbook book = getBook(file, savePath);
		
		String arsParam = savePath.params;
		
		return saveSheetData(arsParam, book);
	}
	
	@Override
	protected String getSheetXML(Sheet sheet) { return null; }

	@Override
	protected boolean saveData(String strXML) { return false; }
	
	/**
	 * Sheet내에 Data를 저장한다.
	 * @param sheet
	 * @return
	 */
	protected List<PotalParamMap> saveSheetData(String arsParams, Workbook book)
	{
		log.info("**saveSheetData() START");
		long start = System.currentTimeMillis();
		
		Sheet sheet = null;
		//Map<String, Object> mpParam = new HashMap<String, Object>();
		PotalParamMap mpParam = new PotalParamMap();
		PotalParamMap mpRow = new PotalParamMap();
		
		List<PotalParamMap> list = new ArrayList<PotalParamMap>();
		
		String userId = request.getSession().getAttribute("userid").toString();
		
		int intReturnCode = 1;
		int intInsertCount = 0;
		int intRawSelectCount = -1;
		int intRealSelectCount = -1;		
						
		try 
		{
			String[][] arsColumns = getExcelHeader(arsParams);
			
			for(int sheetNum = 0; sheetNum < book.getNumberOfSheets(); sheetNum++) 
			{
				sheet = book.getSheetAt(sheetNum);
				
				if (sheet == null) continue;
				
				int nRowEndIndex   = sheet.getLastRowNum();			// ROW 개수
				String sheetNm = sheet.getSheetName();				// sheet 이름 가져오기
								
				for(int intRowNum = 1; intRowNum <= nRowEndIndex ; intRowNum++)
				{
					mpRow = rowToMap(sheet.getRow(intRowNum), arsColumns[sheetNum]);
					
					if(mpRow != null)
					{
						// yyyymm rowToMap에서 데이터 가공 시 number로 인식하여 소수점이 추가되어 소수점 제거 후 비교
						//String yyyymmStr = mpRow.get("yyyymm") == null ? "" : (mpRow.get("yyyymm").toString()).substring(0, 6);
						//mpRow.put("yyyymm", yyyymmStr);
						list.add(mpRow);
					}
				}
			}
			
			//int intReturnCode = ComnConst.RETURN_UNKNOWN;
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			log.info(String.format("--Excel Sales Data Save failed - %s", ex.toString()));
		}
		/*
		if(ComnConst.EXCEL_TEST.equals(strType))		strType = "SARF";
		else if(ComnConst.SAR_ALL_DIFF.equals(strType))		strType = "SARAD";
		else if(ComnConst.SAR_POINT_SYSTEM.equals(strType))	strType = "SARPS";
		else if(ComnConst.SAR_UBIST.equals(strType))		strType = "SARUB";
		
		SqlMapClient smc = commonDAO.getSqlMapClient();
		
		try
		{
			smc.startTransaction();
			smc.startBatch();
			
			if(intExcelRowCount > 0)
			{
				log.info("* RAW INSERT START");
				int intBatchNum = 1;
				
				intReturnCode = ComnConst.RETURN_RAW_INSERT_ERROR;
				
				for(int intRowNum = 1; intRowNum <= intExcelRowCount; intRowNum++)
				{
					mpRow = rowToMap(sheet.getRow(intRowNum), arsColumns);
					if(mpRow != null)
					{
						// yyyymm rowToMap에서 데이터 가공 시 number로 인식하여 소수점이 추가되어 소수점 제거 후 비교
						String yyyymmStr = mpRow.get("yyyymm") == null ? "" : (mpRow.get("yyyymm").toString()).substring(0, 6);
						mpRow.put("yyyymm", yyyymmStr);
						if(mpRow.get("yyyymm") != null && yyyymm.equals(mpRow.get("yyyymm")))
						{
							if("SARUB".equals(strType))
							{
								mpRow.put("brand", arsParams[2]);
							}
							mpRow.put("bulk_id", strNewId);
							mpRow.put("user_id", userId);
							smc.update("sar.insertRawData"+strType, mpRow);
							intInsertCount += commonDAO.update("sar.insertRawData"+strType, mpRow);
						}
					}
					
					if(intRowNum >= intBatchNum*1000)
					{
						smc.executeBatch();
						smc.startBatch();
						intBatchNum++;
					}
				}
				smc.executeBatch();
				log.info("* Elapsed Time for Raw Insert : " + (System.currentTimeMillis() - start)/1000.0 + " Sec.");
				start = System.currentTimeMillis();
				
				if(intInsertCount > 0 && intExcelRowCount == intInsertCount)
				{
					mpParam.put("yyyymm", yyyymm);
					mpParam.put("bulk_id", strNewId);
					if("SARUB".equals(strType))
					{
						mpParam.put("brand", arsParams[2]);
					}					
					intRawSelectCount = (Integer)commonDAO.selectByPk("sar.selectRawData"+strType, mpParam);
					
					if(intRawSelectCount > 0 && intInsertCount == intRawSelectCount)
					{
						if("SARF".equals(strType) || "SARUB".equals(strType))
						{
							log.info("* REAL INSERT START");
							PotalParamMap ubRealParams = new PotalParamMap();
							ubRealParams.put(ComnConst.DIRECT_SP_NAME, "P_sarExcelDataUpload_S");
							
							if("SARUB".equals(strType))
							{
								ubRealParams.put(ComnConst.DIRECT_SP_PARAM, new String[] { strType, yyyymm, strNewId, arsParams[2], userId });
							}
							else
							{
								ubRealParams.put(ComnConst.DIRECT_SP_PARAM, new String[] { strType, yyyymm, strNewId, "", userId });
							}
							
							List<PotalParamMap> saveRealInsertResult;
							saveRealInsertResult = commonDAO.list(ServiceMap.getQueryId(ServiceMap.AJAX_DIRECT_SP), ubRealParams);
							intRealSelectCount = Integer.valueOf(((List<PotalParamMap>)saveRealInsertResult.get(0)).get(0).get("cnt").toString());							
							log.info("* Elapsed Time for Real Insert : " + (System.currentTimeMillis() - start)/1000.0 + " Sec.");
							start = System.currentTimeMillis();
							
							if(intRealSelectCount > 0 && intRawSelectCount == intRealSelectCount)
							{
								log.info("* RAW & REAL DELETE START");
								commonDAO.delete("sar.deleteRawData"+strType, mpParam);
								commonDAO.delete("sar.deleteRealData"+strType, mpParam);
								log.info("* Elapsed Time for Raw & Real Delete : " + (System.currentTimeMillis() - start)/1000.0 + " Sec.");
								start = System.currentTimeMillis();
								
								log.info("* REAL SUMMARY START");
								int intRealSummaryCount = 0;
								ubRealParams.remove(ComnConst.DIRECT_SP_PARAM);
								
								if("SARUB".equals(strType))
								{
									ubRealParams.put(ComnConst.DIRECT_SP_PARAM, new String[] { strType+"_SUM", yyyymm, strNewId, arsParams[2], userId });
								}
								else
								{
									ubRealParams.put(ComnConst.DIRECT_SP_PARAM, new String[] { strType+"_SUM", yyyymm, strNewId, "", userId });
								}
								
								List<PotalParamMap> saveRealSummaryResult;
								saveRealSummaryResult = commonDAO.list(ServiceMap.getQueryId(ServiceMap.AJAX_DIRECT_SP), ubRealParams);
								intRealSummaryCount = Integer.valueOf(((List<PotalParamMap>)saveRealSummaryResult.get(0)).get(0).get("cnt").toString());
								log.info("* Elapsed Time for Real Summary : " + (System.currentTimeMillis() - start)/1000.0 + " Sec.");
								
								if(intRealSummaryCount > 0 && intRealSelectCount == intRealSummaryCount)
								{
									intReturnCode = ComnConst.RETURN_CODE_SUCCESS;
								}
								else
								{
									intReturnCode = ComnConst.RETURN_REAL_SUMMARY_DIFF;
								}
							}
							else
							{
								log.info("* RAW & REAL CLEAR START");
								commonDAO.delete("sar.clearRawData"+strType, mpParam);
								commonDAO.delete("sar.clearRealData"+strType, mpParam);
								log.info("* Elapsed Time for Raw & Real Clear : " + (System.currentTimeMillis() - start)/1000.0 + " Sec.");
								
								intReturnCode = ComnConst.RETURN_REAL_SELECT_DIFF;
							}
						}
						else
						{
							log.info("* RAW DELETE START");
							commonDAO.delete("sar.deleteRawData"+strType, mpParam);
							log.info("* Elapsed Time for Raw Delete : " + (System.currentTimeMillis() - start)/1000.0 + " Sec.");
							
							intReturnCode = ComnConst.RETURN_CODE_SUCCESS;
						}
					}
					else
					{
						log.info("* RAW CLEAR START");
						commonDAO.delete("sar.clearRawData"+strType, mpParam);
						log.info("* Elapsed Time for Raw Clear : " + (System.currentTimeMillis() - start)/1000.0 + " Sec.");
						
						intReturnCode = ComnConst.RETURN_RAW_SELECT_DIFF;
					}
				}
				else
				{
					log.info("* RAW CLEAR START");
					commonDAO.delete("sar.clearRawData"+strType, mpParam);
					log.info("* Elapsed Time for Raw Clear : " + (System.currentTimeMillis() - start)/1000.0 + " Sec.");
					
					intReturnCode = ComnConst.RETURN_RAW_INSERT_DIFF;
				}
				smc.endTransaction();
			}
			else
			{
				intReturnCode = ComnConst.RETURN_NO_DATA;
			}
			
			log.info("-------------------- RESULT --------------------");
			log.info("* Raw   Insert Count  =  "+intInsertCount);
			log.info("* Raw   Select Count  =  "+intRawSelectCount);
			log.info("* Real  Select Count  =  "+intRealSelectCount);
			log.info("* Excel Row    Count  =  "+intExcelRowCount);
			log.info("* Return Code = "+intReturnCode);
			log.info("* Bulk ID = "+strNewId);
			log.info("------------------------------------------------");
		}
		catch(Exception ex)
		{
			if(intReturnCode < 1100) intReturnCode = ComnConst.RETURN_EXCEPTION_ERROR;
			
			try
			{
				smc.endTransaction();
			}
			catch(Exception e)
			{
				
			}
			ex.printStackTrace();
			log.info(String.format("--Excel Sales Data Save failed - %s", ex.toString()));
		}
		
		*/
		log.info("**saveSheetData() END");
		return list;
	}
	
	
	/**
	 * Get Excel Header Columns
	 * @param sheet
	 * @return arsColumns
	 */
	protected String[] getHeaderColumns(Sheet sheet)
	{
		String[] arsColumns = null;
		
		try
		{
			int intColIndex = 0;
			Row clsRow = sheet.getRow(0);
			while(clsRow.cellIterator().hasNext() && clsRow.getCell(intColIndex) != null && "".equals(clsRow.getCell(intColIndex).toString().trim()) == false)
			{
				intColIndex++;
			}
			
			arsColumns = new String[intColIndex];
			for(int intDataIndex = 0; intDataIndex < intColIndex; intDataIndex++)
			{
				arsColumns[intDataIndex] = clsRow.getCell(intDataIndex).toString().trim();
			}
		}
		catch(Exception ex)
		{
			log.info(String.format("--Get Excel Header Columns failed - %s", ex.toString()));
		}
		
		return arsColumns;
	}
	
	protected PotalParamMap rowToMap(Row row, String[] columns)
	{
		PotalParamMap resultMap = null;
		Cell cell;
		int cellCnt;
		int nullCnt = 0;
		Object value;
		
		if(row != null)
		{
			resultMap = new PotalParamMap();

			cellCnt = columns.length;

			for(int i = 0; i < cellCnt; i++)
			{
				cell = row.getCell(i);
				if(cell != null)
				{
					switch (cell.getCellType())
					{
					case Cell.CELL_TYPE_STRING:
						value = (cell.getStringCellValue().replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")).trim();
						break;
					case Cell.CELL_TYPE_NUMERIC:
						value = (String.format("%1.8f", cell.getNumericCellValue())).trim();
						break;
					case Cell.CELL_TYPE_BOOLEAN:
						value = cell.getBooleanCellValue() ? "1" : "0";
						break;
					case Cell.CELL_TYPE_BLANK:
						value = null;
						break;
					case Cell.CELL_TYPE_ERROR:
						value = (cell.getErrorCellValue() + "").trim();
						break;
					default:
						value = "";
						break;
					}
					
					if (value != null)
					{
						nullCnt++;
					}
					
					resultMap.put(columns[i], value);
				}
			}
			
			if (nullCnt != cellCnt)		resultMap = null;	
		}
		return resultMap;
	}
	
	public String[][] getExcelHeader(String param)
    {
    	String[][] excelHeader = null;
    	
    	if (param.equals("TEST"))
    	{
    		excelHeader = ExcelHeaderConst.TEST;
    	}
    	
    	return excelHeader;
    }
		
	/**
	 * Upload 결과 값 Script 반환
	 */
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
			return "<script> var result = {};</script>";
		}
	}
}