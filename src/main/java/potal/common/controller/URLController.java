package potal.common.controller;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import potal.common.model.UserModel;
import potal.common.util.Utility;
import potal.core.service.AddressService;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@SessionAttributes(types = UserModel.class)
public class URLController {
	Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Resource(name = "systemProp")
	private Properties systemProp;		/** SystemProperties */
	
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
	
	@RequestMapping(value = "/address.do")
	public ResponseEntity<String> getAddress(HttpServletRequest request, HttpServletResponse response, HttpSession session){
		log.info("Address start");
		String key = systemProp.getProperty("ADDRESS.API.KEY");
		AddressService svc = new AddressService(key, request);
		
		// Utility.getJSONResponse(commonDAO.list("hanall", "hanall.selectCardList", map))
		//return new ResponseEntity<String>(svc.getGeoCode(), HttpStatus.OK);
		return Utility.getJSONResponse(svc.getAddress());
	}
	
	@RequestMapping(value = "/test.do")
	@SuppressWarnings("rawtypes")
	public void test(HttpServletRequest request, HttpServletResponse response){
		String jsonData = request.getParameter("p1");	// var param = {data: [{a1: "에이", a2: "에이투"}, {a1: "비원", a2: "비투"}]};
		try {
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject)jsonParser.parse(jsonData);
			JSONArray bookInfoArray = (JSONArray) jsonObject.get("data");
			System.out.println(bookInfoArray.size());
			
			for(int i=0; i<bookInfoArray.size(); i++){
				JSONObject bookObject = (JSONObject) bookInfoArray.get(i);
				
				for (Object e : bookObject.entrySet()) {
					Map.Entry entry = (Map.Entry) e;
					System.out.println(entry.getKey() + " : " +  entry.getValue());
				}
            }
			
	
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
