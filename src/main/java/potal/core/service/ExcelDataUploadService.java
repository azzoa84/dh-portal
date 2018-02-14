/**
	@file	ExcelDataUploadService.java
	@date	2017-09-25
	@author	UBCare CRM R&D TF
	@brief	엑셀 데이터 업로드
*/
package potal.core.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.web.multipart.MultipartFile;

import potal.common.common.PotalParamMap;
import potal.common.service.ComnConst;
import potal.common.service.ServiceMap;

/**  
 * @Class Name : ExcelDataUploadService.java
 * @Description : 
 * @Modification Information  
 * @
 * @  수정일      수정자              수정내용
 * @ ---------   ---------   -------------------------------
 *   2017.09.25	 JaeHwan	 신규 생성
 * @author UBCare CRM R&D TF
 * @since 2017.09.25
 * @version 1.0
 * @see
 * 
 * Copyright (C) by UBCARE All right reserved.
 */
public class ExcelDataUploadService extends AbstractXLFileUploadService 
{
	private String[] testParams = { "p1", "p2", "p3", "p4"};
	
	@Override
	protected int getNewFileId(MultipartFile file, PathInfo savePath)
	{		
		log.info("--------------> ExcelDataUploadService : getNewFileId()");
		int intResult = 0;
		/*Sheet sheet = getSheet(file, savePath, 0);
		
		String[] arsParam = (savePath.params).split(",");
		String strType = arsParam[1];
		String strFileName = file.getOriginalFilename();
		String strRowCount = String.valueOf(sheet.getLastRowNum());
		String userId = request.getSession().getAttribute("UserID").toString();
		
		PotalParamMap ubBulkParams = new PotalParamMap();
		ubBulkParams.put(ComnConst.DIRECT_SP_NAME, "P_sysBulkDataInfo_S");
		ubBulkParams.put(ComnConst.DIRECT_SP_PARAM, new String[] { strType, strFileName, strRowCount, userId });
		
		List<PotalParamMap> saveResult;
		saveResult = commonDAO.list(ServiceMap.getQueryId(ServiceMap.AJAX_DIRECT_SP), ubBulkParams);
		
		String strNewId = ((List<PotalParamMap>)saveResult.get(0)).get(0).get("new_id").toString();
		if(strNewId != null && "".equals(strNewId) == false)
		{
			intResult = saveSheetData(strNewId, arsParam, sheet);
		}*/
		return intResult;
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
	protected int saveSheetData(String strNewId, String[] arsParams, Sheet sheet)
	{
		log.info("**saveSheetData() START");
		long start = System.currentTimeMillis();
		
		//int intReturnCode = ComnConst.RETURN_UNKNOWN;
		int intReturnCode = 1;
		int intInsertCount = 0;
		int intRawSelectCount = -1;
		int intRealSelectCount = -1;		
		int intExcelRowCount = sheet.getLastRowNum();
		
		Map<String, Object> mpParam = new HashMap<String, Object>();
		Map<String, Object> mpRow = new HashMap<String, Object>();
		String[] arsColumns = getHeaderColumns(sheet);
		String yyyymm = arsParams[0];
		String strType = arsParams[1];
		String userId = request.getSession().getAttribute("UserID").toString();
		
		/*
		if(ComnConst.SAR_FORECASTING.equals(strType))		strType = "SARF";
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
		return intReturnCode;
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
	
	protected Map<String, Object> rowToMap(Row row, String[] columns)
	{
		Map<String, Object> resultMap = null;
		Cell cell;
		int cellCnt;
		Object value;
		
		if(row != null)
		{
			resultMap = new HashMap<String, Object>();

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
					resultMap.put(columns[i], value);
				}
			}
		}
		return resultMap;
	}
	
	/**
	 * Excel Data Upload Log 저장
	 * @param arsParams
	 * @return
	 */
	public String saveLog(String[] arsParams)
	{
		String strLogSeq = "";
		
		PotalParamMap ubLogParams = new PotalParamMap();
		ubLogParams.put(ComnConst.DIRECT_SP_NAME, "P_sysExcelDataLog_S");
		ubLogParams.put(ComnConst.DIRECT_SP_PARAM, arsParams);
		
		List<PotalParamMap> saveResult;
		saveResult = commonDAO.list(ServiceMap.getQueryId(ServiceMap.AJAX_DIRECT_SP), ubLogParams);
		strLogSeq = ((List<PotalParamMap>)saveResult.get(0)).get(0).get("seq").toString();
		
		return strLogSeq;
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
			buff.append("<script>\n");
			buff.append(String.format("	var result = %s;\n",  new String(result.getJsonString().getBytes("UTF-8"), "ISO-8859-1")));
			buff.append("	parent.closeCurrentWindow(result);\n");
			buff.append("</script>");
			return buff.toString();
		}
		catch(Exception ex)
		{
			return "<script> var result = {};</script>";
		}
	}
}