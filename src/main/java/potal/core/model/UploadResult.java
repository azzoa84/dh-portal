/**
	@file	UploadResult.java
	@date	2017-01-01
	@author	UBCare CRM R&D TF
	@brief	
*/
package potal.core.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import potal.common.common.PotalParamMap;
import potal.common.util.Utility;

public class UploadResult implements Serializable
{
	private static final long serialVersionUID = -2635215403904667374L;
	private final HashMap<String, Object> json;
	public final ArrayList<String> oriFileNameList, newFileNameList, fileURLList, fileIDList, contentList, fileSizeList;
	public final ArrayList<List<PotalParamMap>> dataList;
	
	public UploadResult()
	{
		oriFileNameList = new ArrayList<String>();
		newFileNameList = new ArrayList<String>();
		fileURLList = new ArrayList<String>();
		fileIDList = new ArrayList<String>();
		fileSizeList = new ArrayList<String>();
		contentList = null;
		dataList = new ArrayList<List<PotalParamMap>>(); 
		
		json = new HashMap<String, Object>();
		json.put("oriFileNameList", oriFileNameList);
		json.put("newFileNameList", newFileNameList);
		json.put("fileURLList", fileURLList);
		json.put("fileIDList", fileIDList);
		json.put("fileSizeList", fileSizeList);
		json.put("contentList", contentList);
		json.put("dataList", dataList);
	}
	
	public String getJsonString()
	{
		return Utility.getJSONString(json);
	}	
}