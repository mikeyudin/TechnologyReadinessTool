package net.techreadiness.ui;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import net.techreadiness.security.CorePermissionCodes;
import net.techreadiness.security.PermissionCode;
import net.techreadiness.security.PermissionCodeSet;
import net.techreadiness.security.PermissionCodeSetImpl;
import net.techreadiness.security.UserAware;
import net.techreadiness.service.PermissionService;
import net.techreadiness.service.ServiceContext;
import net.techreadiness.service.UserService;
import net.techreadiness.service.exception.AuthorizationException;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.util.ValueStack;

public class BaseAction extends ActionSupport implements SessionAware, ServletRequestAware, UserAware {

	public static final String CORE_DEBUG_MODE = "core.debug.mode";

	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(BaseAction.class);

	private Map<String, Object> session;
	private HttpServletRequest request;

	@Inject
	protected UserService userService;

	@Inject
	protected PermissionService permissionService;

	public static final String SERVICE_CONTEXT = "serviceContext";

	public PermissionCodeSet getCoreChangeGlobalScopePermission() {
		return new PermissionCodeSetImpl(CorePermissionCodes.CORE_CUSTOMER_CHANGE_GLOBAL_SCOPE);
	}

	public PermissionCodeSet getCoreOrgPartUpdatePermission() {
		return new PermissionCodeSetImpl(CorePermissionCodes.CORE_CUSTOMER_ORG_PART_UPDATE);
	}

	public PermissionCodeSet getCoreOrgUpdatePermission() {
		return new PermissionCodeSetImpl(CorePermissionCodes.CORE_CUSTOMER_ORG_UPDATE);
	}

	@Override
	public String getText(String key, String defaultValue, List<?> args, ValueStack stack) {
		String text = super.getText(key, defaultValue, args, stack);
		if (text == null) {
			String errorMessage = String.format("unknown text key: %s", key);
			logger.error(errorMessage);
			return errorMessage;
		}
		return StringEscapeUtils.escapeHtml4(text);
	}

	public ServiceContext getServiceContext() {
		return (ServiceContext) getSession().get(SERVICE_CONTEXT);
	}

	public Map<String, Object> getSession() {
		return session;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	@Override
	public boolean userActive() {
		return userService.userActive(getServiceContext());
	}

	@Override
	public boolean hasPermission(PermissionCode[] permissionCodes) {
		return userService.hasPermission(getServiceContext(), permissionCodes);
	}

	@Override
	public boolean hasPermission(String permissionCode) throws Exception {
		PermissionCode permission = CorePermissionCodes.fromString(permissionCode);
		if (permission == null) {
			throw new Exception("Unknown permission code '" + permissionCode + "' referenced!");
		}
		return userService.hasPermission(getServiceContext(), permission);
	}

	@Override
	public boolean hasPermission(PermissionCode permissionCode) {
		PermissionCode[] permissionCodes = { permissionCode };
		return hasPermission(permissionCodes);
	}

	@Override
	public void throwNotAuthorized(Object o) throws AuthorizationException {
		throw new AuthorizationException("User: " + SecurityContextHolder.getContext().getAuthentication().getName()
				+ " denied access to: " + o.getClass().getCanonicalName());
	}

	public ValueStack getValueStack() {
		return ActionContext.getContext().getValueStack();
	}

}