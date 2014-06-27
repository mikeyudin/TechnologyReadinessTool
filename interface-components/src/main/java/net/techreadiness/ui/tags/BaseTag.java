package net.techreadiness.ui.tags;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.DynamicAttributes;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.JspTag;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.io.output.NullWriter;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Maps;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.TextParseUtil;
import com.opensymphony.xwork2.util.ValueStack;

public abstract class BaseTag extends SimpleTagSupport implements DynamicAttributes {
	private final Map<String, Object> dynamicAttributes = Maps.newHashMap();
	private Map<String, Object> actionContext = null;

	public BaseTag() {
		ActionContext.getContext().getContainer().inject(this);
	}

	/**
	 * perform any logic prior to rendering tag jsp
	 *
	 * @return path to tag's jsp, return null to render nothing.
	 * @throws Exception
	 */
	protected String execute() throws Exception {
		return null;
	}

	@Override
	public void doTag() throws JspException, IOException {
		try {
			JspTag parent = getOptionalParentTag(ParentTag.class);
			Map<String, Object> actionContext = ActionContext.getContext().getValueStack().getContext();
			if (parent != null && parent instanceof ParentTag && ((ParentTag) parent).isCollectingChildren()) {
				this.actionContext = new HashMap<>(actionContext);
				((ParentTag) parent).getChildren().add(this);
				if (this instanceof ParentTag) {
					((ParentTag) this).collectChildren();
				}
			} else {
				if (this instanceof ParentTag) {
					((ParentTag) this).collectChildren();
				}
				String jspResult = execute();
				if (jspResult != null) {
					renderPage(jspResult);
				}
			}
		} catch (Exception e) {
			printStackTrace(e);
		}
	}

	protected String evaluateOgnl(final String ognl) {
		return executeWithinOriginalContext(new Callable<String>() {

			@Override
			public String call() throws Exception {
				return TextParseUtil.translateVariables('%', StringUtils.defaultString(ognl), getValueStack());
			}
		});
	}

	protected <T> T evaluateOgnl(final String ognl, final Class<T> clazz) {
		return executeWithinOriginalContext(new Callable<T>() {
			@SuppressWarnings("unchecked")
			@Override
			public T call() throws Exception {
				return (T) TextParseUtil.translateVariables('%', StringUtils.defaultString(ognl), getValueStack(), clazz);
			}
		});
	}

	private void printStackTrace(Exception e) throws IOException {
		getJspContext().getOut().println("<textarea style=\"width:100%;height:400\">");
		e.printStackTrace(new PrintWriter(getJspContext().getOut()));
		e.printStackTrace();
		getJspContext().getOut().println("</textarea>");
	}

	private void renderPage(String location) throws IOException, ServletException {
		Object prevTag = getPageContext().getRequest().getAttribute("tag");
		ValueStack stack = getValueStack();
		try {
			getPageContext().getRequest().setAttribute("tag", this);
			if (stack != null) {
				stack.push(this);
			}
			getPageContext().include(location);
		} finally {
			getPageContext().getRequest().setAttribute("tag", prevTag);
			if (stack != null) {
				stack.pop();
			}
		}
	}

	public ValueStack getValueStack() {
		return (ValueStack) getRequest().getAttribute("struts.valueStack");
	}

	/**
	 * Evaluate the body of this tag, but do not write the results to any page.
	 * @throws JspException If there is an error invoking the body.
	 * @throws IOException If there is an error writing to the stream.
	 */
	public final void invokeBody() throws JspException, IOException {
		if (getJspBody() != null) {
			try (Writer writer = new NullWriter()) {
				getJspBody().invoke(writer);
			}
		}
	}

	/**
	 * Write the body of this tag to a jspWriter. This is exposed so that tags can be written multiple times.
	 *
	 * @param writer
	 *            The writer that the body of the tag is written to.
	 * @throws JspException
	 *             Thrown if an error occurred while invoking the tag body.
	 * @throws IOException
	 *             If there was an error writing to the {@code writer}.
	 */
	public final void writeBody(final Writer writer) throws JspException, IOException {
		executeWithinOriginalContext(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				if (getJspBody() != null) {
					getJspBody().invoke(writer);
				}
				return null;
			}
		});
	}

	public final <T> T getRequiredParentTag(Class<T> parentTagClass) {
		@SuppressWarnings("unchecked")
		T parentTag = (T) findAncestorWithClass(this, parentTagClass);

		if (parentTag == null) {
			throw new RuntimeException(this.getClass().getSimpleName() + " must be surrounded by a "
					+ parentTagClass.getSimpleName());
		}
		return parentTag;
	}

	public final <T> T getOptionalParentTag(Class<T> parentTagClass) {
		@SuppressWarnings("unchecked")
		T parentTag = (T) findAncestorWithClass(this, parentTagClass);
		return parentTag;
	}

	protected PageContext getPageContext() {
		return (PageContext) getJspContext();
	}

	@Override
	public JspFragment getJspBody() {
		return super.getJspBody();
	}

	public HttpServletRequest getRequest() {
		return (HttpServletRequest) getPageContext().getRequest();
	}

	public HttpServletResponse getResponse() {
		return (HttpServletResponse) getPageContext().getResponse();
	}

	public ServletContext getServletContext() {
		return getPageContext().getServletContext();
	}

	public HttpSession getSession() {
		return getRequest().getSession();
	}

	@Override
	public void setDynamicAttribute(String uri, String localName, Object value) throws JspException {
		dynamicAttributes.put(localName, value);
	}

	public Map<String, Object> getDynamicAttributes() {
		return dynamicAttributes;
	}

	public Map<String, Object> getActionContext() {
		return actionContext;
	}

	/**
	 * Execute a block of code within the original context of this tag. When the tag is a parent tag, we must restore it's
	 * original context for certain operations that require the ognl context.
	 */
	private <T> T executeWithinOriginalContext(Callable<T> execution) {
		Map<String, Object> contextBak = Maps.newHashMap(ActionContext.getContext().getValueStack().getContext());
		try {
			if (actionContext != null) {
				ActionContext.getContext().getValueStack().getContext().putAll(actionContext);
			}
			return execution.call();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (actionContext != null) {
				ActionContext.getContext().getValueStack().getContext().putAll(contextBak);
			}
		}

	}
}
