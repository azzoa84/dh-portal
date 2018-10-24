package potal.common.resolver;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import potal.common.common.CommandMap;
import potal.common.common.PotalParamMap;

/**
 * 파라미터와 기타정보를 받아 Map에 담는 클래스
 * @author Administrator
 *
 */
public class CustomMapArgumentResolver implements HandlerMethodArgumentResolver{
	protected Logger log  = LoggerFactory.getLogger(this.getClass());
	
	// Resolver가 적용 가능한지 검사하는 역할
	@Override
    public boolean supportsParameter(MethodParameter parameter) {
		// 컨트롤러의 파라미터가 CommandMap 클래스인지 검사
        return CommandMap.class.isAssignableFrom(parameter.getParameterType());
    }
 
	// 파라미터와 기타 정보를 받아서 실제 객체를 반환
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        CommandMap commandMap = new CommandMap();
         
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        
        // getParameterNames()는 반환값이 Enumeration
        Enumeration<?> enumeration = request.getParameterNames();
         
        String key = null;
        String[] values = null;
        log.info("CustomMapArgumentResolver");
        while(enumeration.hasMoreElements()){
            key = (String) enumeration.nextElement();
            values = request.getParameterValues(key);
            
            if(values != null){
            	commandMap.put(key, (values.length > 1) ? values : values[0] );
            }
        }
        return commandMap;
    }
}
