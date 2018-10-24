package potal.core.common;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;

public class CustomExceptionResolver extends org.springframework.web.servlet.handler.SimpleMappingExceptionResolver {
	Logger log = LoggerFactory.getLogger(this.getClass());

	private String exceptionAttribute = DEFAULT_EXCEPTION_ATTRIBUTE; // DEFAULT_EXCEPTION_ATTRIBUTE의
																		// 값은
																		// "exception"이다.
	private int defaultStatusCode = 40;

	@Override
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		// Expose ModelAndView for chosen error view.
		/*
		 * String viewName = determineViewName(ex, request); System.out.println(
		 * "---------------> " + request.getHeader("AJAX"));
		 * 
		 * return null;
		 */
		
		if (ex instanceof MaxUploadSizeExceededException) {
			//ModelAndView mv = new ModelAndView();
			System.out.println("MaxUploadSizeExceededException Exception");
			Map<String, String> resultMap = new HashMap<String, String>();
			resultMap.put("result1", "test1");
			resultMap.put("result2", "test222");
			ModelAndView modelAndView = new ModelAndView("jsonView", resultMap);


			//modelAndView.addObject( "errorCode" , 1);
			//modelAndView.addObject( "errorMessage" , String.format( "Exception : %s( %s )", ex.getClass().getName(), ex.getMessage()));
			//response.setStatus(defaultStatusCode);
			return modelAndView;
		}

		return null;
		/*
		 * String viewName = determineViewName(ex, request); System.out.println(
		 * "--------------> " + viewName); if (viewName != null) { log.info(
		 * "Ajax 호출인가?" + request.getHeader("AJAX"));
		 * 
		 * // Ajax호출일 경우에만 statusCode를 설정한다. if (request.getHeader("AJAX") !=
		 * null && request.getHeader("AJAX").equals("true")) { // Apply HTTP
		 * status code for error views, if specified. // Only apply it if we're
		 * processing a top-level request. Integer statusCode =
		 * determineStatusCode(request, viewName); if (statusCode != null) {
		 * applyStatusCodeIfPossible(request, response, statusCode); } } return
		 * getModelAndView(viewName, ex, request); } else { return null; }
		 */
	}

	// request에 담긴 정보도 view로 보내고 싶다면 아래와 같이 getModelAndView 메서드도 오버라이딩 하도록 하자.
	/// 오버라이딩 하지 않을 경우 ModelAndView에는 Exception객체만 담아 view로 리턴한다.
	/*
	 * @Override protected ModelAndView getModelAndView(String viewName,
	 * Exception ex, HttpServletRequest request) { ModelAndView mv = new
	 * ModelAndView(viewName); if (this.exceptionAttribute != null) { log.info(
	 * "Exposing Exception as model attribute '" + this.exceptionAttribute +
	 * "'");
	 * 
	 * mv.addObject(this.exceptionAttribute, ex); mv.addObject("url",
	 * request.getRequestURL()); } return mv; }
	 */
}
