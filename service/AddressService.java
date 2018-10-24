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
	
	public static final String SVC_URL = "http://openapi.epost.go.kr/postal/retrieveNewAdressAreaCdService/retrieveNewAdressAreaCdService/getNewAddressListAreaCd?ServiceKey=%s&searchSe=%s&srchwrd=%s";
	
	public AddressService(String apiKey, HttpServletRequest request)
	{
		String addrTmp = request.getParameter("address");
		String searchSeTmp = request.getParameter("searchSe");
		
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
	}
	
	public String getGeoCode()
	{
		try
		{			
			String urlInfo = String.format(SVC_URL, apiKey, searchSe, address);
			
			System.out.println("urlInfo " + urlInfo);
			URL url = new URL(urlInfo);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
	        conn.setRequestMethod("GET");
	        conn.setRequestProperty("Content-type", "application/json");
	        	        
	        InputStream inputStream = conn.getInputStream();
			StringWriter sw = new StringWriter();
			IOUtils.copy(inputStream, sw, "UTF-8");
			String returnStr = sw.toString();
			
			log.info(returnStr);
			SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
	        /*
	        BufferedReader rd;
	        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
	            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        } else {
	            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
	        }
	        StringBuilder sb = new StringBuilder();
	        String line;
	        while ((line = rd.readLine()) != null) {
	            sb.append(line);
	        }
	        rd.close();
	        conn.disconnect();
	        System.out.println(sb.toString());
	        */
	        return null;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return ex.toString();
		}
	}
}