package net.techreadiness.security;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.jasig.cas.client.util.CommonUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

public class CasAuthenticationEntryPoint implements AuthenticationEntryPoint, InitializingBean {
	RequestCache cache;
	Pattern p = Pattern.compile("^.*/account/([a-zA-Z]+).*");
	org.springframework.security.cas.web.CasAuthenticationEntryPoint wrappedCasAuthEntryPoint;

	public CasAuthenticationEntryPoint() {
		wrappedCasAuthEntryPoint = new org.springframework.security.cas.web.CasAuthenticationEntryPoint();
		cache = new HttpSessionRequestCache();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		wrappedCasAuthEntryPoint.afterPropertiesSet();
	}

	@Override
	public final void commence(final HttpServletRequest servletRequest, final HttpServletResponse response,
			final AuthenticationException authenticationException) throws IOException, ServletException {

		final String urlEncodedService = getUrlEncodedService(response);

		final String redirectUrl = getRedirectUrl(urlEncodedService, servletRequest);

		response.sendRedirect(redirectUrl);
	}

	protected String getRedirectUrl(final String urlEncodedService, final HttpServletRequest request) {
		ServiceProperties sp = wrappedCasAuthEntryPoint.getServiceProperties();
		String url = CommonUtils.constructRedirectUrl(wrappedCasAuthEntryPoint.getLoginUrl(), sp.getServiceParameter(),
				urlEncodedService, sp.isSendRenew(), false);

		if (request.getRequestURI().endsWith("/logoutcas")) {
			url = url + "&so=yes";
		}

		String account = parseAccount(getSavedRequestRedirectUrl(request));
		if (StringUtils.isEmpty(account)) {
			return url;
		}
		return url + "&account=" + account;
	}

	protected final String getUrlEncodedService(final HttpServletResponse response) {
		ServiceProperties sp = wrappedCasAuthEntryPoint.getServiceProperties();
		return CommonUtils.constructServiceUrl(null, response, sp.getService(), null, sp.getArtifactParameter(), true);
	}

	protected String getSavedRequestRedirectUrl(final HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			SavedRequest savedRequest = cache.getRequest(request, null);
			if (savedRequest != null) {
				return savedRequest.getRedirectUrl();
			}
		}

		/* return a sane default in case data isn't there */
		return request.getContextPath() + "/";
	}

	protected String parseAccount(String redirectUrl) {
		Matcher m = p.matcher(redirectUrl);

		if (m.find()) {
			return m.group(1);
		}
		return "";
	}

	/* wrapped methods */

	public final String getLoginUrl() {
		return wrappedCasAuthEntryPoint.getLoginUrl();
	}

	public final ServiceProperties getServiceProperties() {
		return wrappedCasAuthEntryPoint.getServiceProperties();
	}

	public final void setLoginUrl(final String loginUrl) {
		wrappedCasAuthEntryPoint.setLoginUrl(loginUrl);
	}

	public final void setServiceProperties(final ServiceProperties serviceProperties) {
		wrappedCasAuthEntryPoint.setServiceProperties(serviceProperties);
	}

	@Deprecated
	public final void setEncodeServiceUrlWithSessionId(final boolean encodeServiceUrlWithSessionId) {
		wrappedCasAuthEntryPoint.setEncodeServiceUrlWithSessionId(encodeServiceUrlWithSessionId);
	}

}
