package potal.common.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

/**
 * 인증 실패 핸들러
 * 
 * 
 */
public class AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
	Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) throws IOException, ServletException {
		/*
			HttpServletRequest 클래스의 getRequestDispatcher 메소드를 이용해서 보여줘야 할 화면으로 forward를 해주면 된다. 
			forward로 해줘야 jstl을 이용해서 setAttribute로 저장한 값을 가져올 수 있다. 
			HttpServletResponse 클래스의 sendRedirect 메소드를 이용해서 redirect를 하면 안된다.
		*/
		//request.getRequestDispatcher("/login.do").forward(request, response);
		//response.sendRedirect("login.jsp");
		
		log.info("AuthenticationFailureHandler: login Fail.");
		
		request.setAttribute("loginException", "Y");
		super.onAuthenticationFailure(request, response, ex);
	}
}
