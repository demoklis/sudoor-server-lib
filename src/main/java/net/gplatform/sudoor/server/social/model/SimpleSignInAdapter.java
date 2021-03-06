/*
 * Copyright 2014 the original author or authors.
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
package net.gplatform.sudoor.server.social.model;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.gplatform.sudoor.server.security.model.auth.SSAuth;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;

@Component
public class SimpleSignInAdapter implements SignInAdapter {

	private final RequestCache requestCache;
	
	public static final String CONNECTION_KEY = "sudoor_signIn_connection";

	@Autowired
	private SSAuth SSAuth;

	@Inject
	public SimpleSignInAdapter(RequestCache requestCache) {
		this.requestCache = requestCache;
	}

	@Override
	public String signIn(String localUserId, Connection<?> connection, NativeWebRequest request) {
		SSAuth.signin(localUserId, null);
		
		HttpServletRequest nativeReq = request.getNativeRequest(HttpServletRequest.class);
		HttpSession session = nativeReq.getSession();
		session.setAttribute(CONNECTION_KEY, connection);
		
		String providerId = connection.getKey().getProviderId();
		String parameter = "?entryPoint=oauth_" + providerId;
		
		String targetUrl = "/";
		String originalUrl = extractOriginalUrl(request);
		if (StringUtils.isNotEmpty(originalUrl)) {
			targetUrl = StringUtils.trimToEmpty(originalUrl);
		}
		return targetUrl + parameter;
	}

	private String extractOriginalUrl(NativeWebRequest request) {
		HttpServletRequest nativeReq = request.getNativeRequest(HttpServletRequest.class);
		HttpServletResponse nativeRes = request.getNativeResponse(HttpServletResponse.class);
		SavedRequest saved = requestCache.getRequest(nativeReq, nativeRes);
		if (saved == null) {
			return null;
		}
		requestCache.removeRequest(nativeReq, nativeRes);
		removeAutheticationAttributes(nativeReq.getSession(false));
		return saved.getRedirectUrl();
	}

	private void removeAutheticationAttributes(HttpSession session) {
		if (session == null) {
			return;
		}
		session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	}

}
