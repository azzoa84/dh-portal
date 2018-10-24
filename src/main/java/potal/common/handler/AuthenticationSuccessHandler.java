/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package potal.common.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;

import potal.common.model.UserModel;

/**
 * 인증 성공 핸들러
 * @author 김동환
 * 
 */
public class AuthenticationSuccessHandler implements org.springframework.security.web.authentication.AuthenticationSuccessHandler {
	Logger log = LoggerFactory.getLogger(this.getClass());
 
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		UserModel userModel = (UserModel)authentication.getPrincipal();
		Cookie cookie = new Cookie("autologinkey40", userModel.getAutoLoginKey());
		int expire = 86400 * 180; // 180day 
		cookie.setMaxAge(expire);
		cookie.setPath("/");
		response.addCookie(cookie);
		
		log.info("AuthenticationSuccessHandler: 로그인 유저 => " + userModel.getUserId());
		
		response.sendRedirect("main.do");
	}
}