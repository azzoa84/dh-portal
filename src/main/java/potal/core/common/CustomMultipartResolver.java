package potal.core.common;

import java.util.LinkedHashMap;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

public class CustomMultipartResolver extends CommonsMultipartResolver {
	Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Resource(name = "systemProp")
	private Properties systemProp;

	@Override
	public MultipartHttpServletRequest resolveMultipart(HttpServletRequest request) throws MultipartException {
		log.info("------- MultipartHttpServletRequest");
		int maxFileSizeMB = Integer.parseInt(systemProp.getProperty("MAX_UPLOAD_SIZE"));
		
		setMaxUploadSize(maxFileSizeMB * 1024 * 1024);
		setMaxInMemorySize(maxFileSizeMB * 1024 * 1024);
		
		try {
			return super.resolveMultipart(request);
		} catch (MaxUploadSizeExceededException e) {
			log.info("----------------------> 파일 용량 초과");;
			request.setAttribute("exception", e);
			return new DefaultMultipartHttpServletRequest(request, new LinkedMultiValueMap<String, MultipartFile>(), new LinkedHashMap<String, String[]>(), new LinkedHashMap<String, String>());
		}
	}
}