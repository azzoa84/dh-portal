package potal.common.service;

import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import potal.common.model.UserModel;
import potal.common.util.ApplicationContextHolder;
import potal.common.common.CommandMap;
import potal.common.common.PotalParamMap;
import potal.common.dao.CommonDAO;

@Service("userService")
public class UserService implements UserDetailsService {
	Logger log = LoggerFactory.getLogger(this.getClass());

	@Resource(name="commonDAO")
	private CommonDAO commonDAO;
	
	@Override
	@SuppressWarnings("unchecked")
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		HttpServletRequest request = ApplicationContextHolder.getRequest();
		PotalParamMap param = new PotalParamMap();
		UserModel userInfo = null;
		
		param.put("userId", request.getParameter("j_username"));
		param.put("password", request.getParameter("j_password"));
		param.put(ComnConst.ARGS_RETURN_CODE, "");
		param.put(ComnConst.ARGS_RETURN_STRING, "");
		
		try {
			List<PotalParamMap> list = (List<PotalParamMap>)commonDAO.list(ServiceMap.getQueryId(ServiceMap.AJAX_LOGIN), param);
			
			if (!list.isEmpty()) {
				log.info("----------------> Welcome DH <----------------");
				
				PotalParamMap result = list.get(0);
				createSessionInfo(request, result);
				
				Collection<? extends GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_USER");
				
				userInfo = new UserModel((String)param.get("userId"), (String)param.get("password"), authorities);
			} else {
				request.setAttribute("exceptionMsg", "입력한 로그인 정보를 통한 접속 인증에 실패 하였습니다.\\n확인 후 다시 입력해 주시기 바랍니다.");
				throw new UsernameNotFoundException("Invalid Active Directory id/password.");
			}
			
			return userInfo;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private void createSessionInfo(HttpServletRequest request, PotalParamMap map)
	{
		List<?> keys;
		HttpSession session = request.getSession();
		session.setAttribute("userid", map.get("employeeId"));
		
		/*keys = map.keyList();
		for(int i = 0; i < keys.size(); i++)
		{
			session.setAttribute(keys.get(i).toString(), map.get(keys.get(i)));
		}*/
		
		try
		{
			PotalParamMap param = new PotalParamMap();
			param.put("workType", "USER");
			param.put("userId", session.getAttribute("userid").toString());
			session.setAttribute("userMenu", commonDAO.list(ServiceMap.getQueryId(ServiceMap.AJAX_GET_MENUS), param));
			//session.setAttribute("adminMenu", commonDAO.list(ServiceMap.getQueryId(ServiceMap.AJAX_ADMIN_MENUS), param));			
		}
		catch(Exception ex)
		{
			log.info(ex.getMessage());
		}		
	}
}