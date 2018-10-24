/**
	@file	GeoCodingService.java
	@date	2017-01-01
	@author	UBCare CRM R&D TF
	@brief	
*/
package potal.core.service;

import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import potal.common.common.PotalParamMap;
import potal.core.handler.AddressHandler;
import potal.core.model.AddressItem;

/**  
 * @Class Name : GeoCodingService.java
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
public class AddressService
{
	protected Logger log  = LoggerFactory.getLogger(this.getClass());

	public final String apiKey;
	public String address;
	public String searchSe;
	
	public int currentPage;
	public int countPerPage = 10;
	
	public static final String SVC_URL = "http://www.juso.go.kr/addrlink/addrLinkApi.do?confmKey=%s&keyword=%s&currentPage=%d&countPerPage=%d&resultType=xml";
	
	
	public AddressService(String apiKey, HttpServletRequest request)
	{
		String addrTmp = request.getParameter("address");
		String searchSeTmp = request.getParameter("searchSe");
		int currentPageTmp = Integer.parseInt(request.getParameter("currentPage"));
		
		this.apiKey = apiKey;
		
		try
		{
			addrTmp = URLEncoder.encode(addrTmp, "UTF-8");
			searchSeTmp = URLEncoder.encode(searchSeTmp, "UTF-8");
		}
		catch(Exception ex)
		{
			log.info(ex.toString());
		}
		
		this.address = addrTmp;
		this.searchSe = searchSeTmp;
		this.currentPage = currentPageTmp;
	}
	
	public PotalParamMap getAddress()
	{
		try
		{			
			//String urlInfo = String.format(SVC_URL, apiKey, searchSe, address);
			String urlInfo = String.format(SVC_URL, apiKey, address, currentPage, countPerPage);
			System.out.println(urlInfo);		
			/* DOM 방식 
			URL url = new URL(urlInfo);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
	        conn.setRequestMethod("GET");
	        conn.setRequestProperty("Content-type", "application/json");
	        	        
	        InputStream inputStream = conn.getInputStream();
			StringWriter sw = new StringWriter();
			IOUtils.copy(inputStream, sw, "UTF-8");
			String returnStr = sw.toString();
						
			DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
			fact.setNamespaceAware(true);
			DocumentBuilder builder = fact.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(returnStr));
			Document doc = builder.parse(is);
			*/
			
			// SAX를 사용해서 XML 문서를 파싱하려면 먼저 DefaultHandler를 확장한 클래스가 필요하다.
			// AddressHandler는 DefaultHandlerf 클래스를 상속받는 클래스이다.
			
			SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
			AddressHandler handler = new AddressHandler();
			PotalParamMap resultList = null;

			handler.initList();
			parser.parse(urlInfo, handler);
			resultList = handler.getMap();
						
	        return resultList;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
}