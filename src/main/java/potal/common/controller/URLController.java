package potal.common.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import potal.common.model.UserModel;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@SessionAttributes(types = UserModel.class)
public class URLController {
	Logger log = LoggerFactory.getLogger(this.getClass());
	
	@RequestMapping(value = "/main.do")
    public String main(HttpServletRequest request, ModelMap model, HttpSession session) throws Exception
	{
		String strReturnUrl = "";
		if(session != null)
		{
			strReturnUrl = "redirect:/index.jsp";
		}
		else
		{
			strReturnUrl = "redirect:/login.jsp";
		}
        return strReturnUrl;
    }
	
	/**
	 * 로그인 화면으로 이동
	 * @param request
	 * @param response
	 * @param model
	 * @throws Exception
	 */
	/*@RequestMapping(value = "/login.do")
    public void expiredSession(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		request.setAttribute("expiredSession", true);
		request.setAttribute("p1", "session_logout_div");
		
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		request.getRequestDispatcher("/login.jsp").forward(request, response);
    }*/
	
	/**
	 * 로그 아웃
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/logout.do")
	public String logout(HttpServletRequest request, HttpServletResponse response, HttpSession session)
	{
		// 세션 만료 처리
		session.invalidate();
		
		// 쿠키에서 자동로그인키 삭제 처리
		if(request.getCookies() != null)
		{
			for(int i = 0; i < request.getCookies().length; i++)
			{
				if(request.getCookies()[i].getName().equals("autologinkey"))
				{
					Cookie cookie = request.getCookies()[i];
					cookie.setValue("");
					cookie.setPath("/");
					cookie.setMaxAge(0);
					response.addCookie(cookie);
				}
			}
		}
		return "redirect:/login.jsp";
	}
}
