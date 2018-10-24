/**
	@file	HiraParseHandler.java
	@date	2017-01-01
	@author	UBCare CRM R&D TF
	@brief	
*/
package potal.core.handler;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import potal.common.common.PotalParamMap;
import potal.core.model.AddressItem;

/**  
 * @Class Name : HiraParseHandler.java
 * @Description : 
 *  
 * Copyright (C) by UBCARE All right reserved.
 */
public class AddressHandler extends DefaultHandler
{
	private List<AddressItem> tempList = null;
	private PotalParamMap map = null;
	
	private AddressItem addrItem = null;
	private AddressItem msgItem = null;
	
	private boolean roadAddr = false, jibunAddr = false, zipNo = false, detBdNmList = false;
	private boolean totalCount = false, currentPage = false, countPerPage = false;
		
	public List<AddressItem> getlist()
	{
		return tempList;
	}
	
	public PotalParamMap getMap()
	{
		return map;
	}
	
	public AddressItem getMsg()
	{
		return msgItem;
	}
	
	public void initList()
	{
		tempList= null;
		map = null;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
	{
		if (qName.equalsIgnoreCase("common"))
		{
			addrItem = new AddressItem();
			map = new PotalParamMap();
			if (map == null) map = new PotalParamMap();
		}
		else if (qName.equalsIgnoreCase("juso"))		
		{ 
			addrItem = new AddressItem();
			if (tempList == null) tempList = new ArrayList<AddressItem>();
			if (map == null)
			{
				map = new PotalParamMap();
			}
			
			map.put("resultList", tempList);
		}
		
		else if (qName.equalsIgnoreCase("roadAddr"))		{ roadAddr = true; }
		else if (qName.equalsIgnoreCase("jibunAddr"))		{ jibunAddr = true; }
		else if (qName.equalsIgnoreCase("zipNo"))			{ zipNo = true; }
		else if (qName.equalsIgnoreCase("detBdNmList"))		{ detBdNmList = true; }
		
		else if (qName.equalsIgnoreCase("totalCount"))		{ totalCount = true; }
		else if (qName.equalsIgnoreCase("currentPage"))		{ currentPage = true; }
		else if (qName.equalsIgnoreCase("countPerPage"))	{ countPerPage = true; }		
	}

	@Override
	@SuppressWarnings (value="unchecked")
	public void endElement(String uri, String localName, String qName) throws SAXException
	{
		if (qName.equalsIgnoreCase("common"))
		{
			map.put("common", addrItem);
		}
		else if (qName.equalsIgnoreCase("juso"))
		{
			//tempList.add(addrItem);
			((List<AddressItem>)map.get("resultList")).add(addrItem);
		}
	}

	@Override
	public void characters(char ch[], int start, int length) throws SAXException
	{
		String value = new String(ch, start, length);

		if (roadAddr)			{ addrItem.setRoadAddr(value);		roadAddr = false; }
		else if (jibunAddr)		{ addrItem.setJibunAddr(value);		jibunAddr = false; }
		else if (zipNo)			{ addrItem.setZipNo(value);			zipNo = false; }	
		else if (detBdNmList)	{ addrItem.setDetBdNmList(value);	detBdNmList = false; }
		
		else if (totalCount)	{ addrItem.setTotalCount(Integer.parseInt(value));		totalCount = false; }
		else if (currentPage)	{ addrItem.setCurrentPage(Integer.parseInt(value));		currentPage = false; }
		else if (countPerPage)	{ addrItem.setCountPerPage(Integer.parseInt(value));		countPerPage = false; }
	}
}