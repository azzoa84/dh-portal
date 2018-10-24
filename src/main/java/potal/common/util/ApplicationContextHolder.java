package potal.common.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**	
 * 스프링 빈에 단축된 접근을 위한 클래스
 * 
 */
public class ApplicationContextHolder implements ApplicationContextAware {
	private static ApplicationContext appCtx;
	
	public ApplicationContextHolder() {}
	
	@Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		appCtx = arg0;
	}
	
	/**
	 * 스프링 ApplicationContext 반환
	 * @return ApplicationContext
	 */
	public static ApplicationContext getContext() {
		return appCtx;
	}
	
	/**
	 * HttpServletRequest 반환
	 * @return HttpServletRequest
	 */
	public static HttpServletRequest getRequest() {
		/*
		 *  Spring에서 HttpServletRequest를 메소드 파라미터로 선언하지 않고 가져올 수 있는 방법
			RequestContextHolder 클래스와 ServletRequestAttributes 클래스를 이용
	
			org.springframework.web.context.request.RequestContextHolder 클래스는 ThreadLocal를 사용해서
			현재 쓰레드에 RequestAttributes 인스턴스를 바인딩 해두었다가 요청을 하면 이 인스턴스를 돌려주는 역할을 합니다.
	
			그런데 RequestContextHolder 클래스를 살펴보니 request를 가져올 수 있는 메소드가 2개가 존재했습니다.
			바로 currentRequestAttributes()와 getRequestAttributes() 메소드인데 이 메소드들의 차이는 바로!
			
			두 메소드 모두 현재 스레드에 바인딩된 RequestAttributes를 가져온다는 것은 동일하나
	
			getRequestAttributes()는 RequestAttributes가 없으면 널을 반환하고,
			currentRequestAttributes()는  RequestAttributes가 없으면 예외를 발생합니다.
			
			web.xml에서 리스너를 설정해주어야 한다
		*/
		
		return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
	}
	
	/**
	 * HttpSession 반환
	 * @return HttpSession
	 */
	public static HttpSession getSession() {
		return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getSession(false);
	}
	
	/**
	 * Servlet Real Path 반환
	 * @return HttpSession
	 */
	public static String getServletRealPath() {
		return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getSession(false).getServletContext().getRealPath("");
	}	
	
	/**
	 * HttpServletResponse 반환
	 * @return HttpServletResponse
	 */
	public static final String RESPONSE_NAME_AT_ATTRIBUTES = ServletRequestAttributes.class.getName() + ".ATTRIBUTE_NAME";
	public static HttpServletResponse getResponse() {
		return (HttpServletResponse) ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getAttribute(RESPONSE_NAME_AT_ATTRIBUTES, RequestAttributes.SCOPE_REQUEST); 
	}
}
