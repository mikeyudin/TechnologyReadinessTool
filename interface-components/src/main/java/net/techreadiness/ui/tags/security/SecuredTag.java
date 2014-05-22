package net.techreadiness.ui.tags.security;

import java.util.Set;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import net.techreadiness.security.PermissionCode;
import net.techreadiness.security.PermissionCodeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * An implementation of {@link Tag} that allows its body through if permissions are granted to the request's principal.
 * <p>
 * One or more comma separate {@link PermissionCode}s are specified via the <tt>hasPermission</tt> attribute using a
 * {@link Set}.
 */
public class SecuredTag extends AbstractSecurityTag {

	private static final long serialVersionUID = 1L;

	protected static final Log logger = LogFactory.getLog(SecuredTag.class);

	private PermissionCodeSet permissionCodes;

	@Override
	public int doStartTag() throws JspException {
		if (null == permissionCodes || permissionCodes.size() < 1) {
			return skipBody();
		}

		initializeIfRequired();

		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("SecurityContextHolder did not return a non-null Authentication object, so skipping tag body");
			}
			return skipBody();
		}

		if (userService.hasPermission(getServiceContext(), permissionCodes.toArray())) {
			return evalBody();
		}
		return skipBody();
	}

	public PermissionCodeSet getHasPermission() {
		return permissionCodes;
	}

	public void setHasPermission(PermissionCodeSet permissionCodes) {
		this.permissionCodes = permissionCodes;
	}
}
