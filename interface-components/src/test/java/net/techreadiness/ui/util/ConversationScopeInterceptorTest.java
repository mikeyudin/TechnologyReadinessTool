package net.techreadiness.ui.util;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Maps;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;

@RunWith(MockitoJUnitRunner.class)
public class ConversationScopeInterceptorTest {
	ConversationScopeInterceptor interceptor;

	@Mock
	ActionInvocation invocation;
	@Mock
	ActionContext context;
	Map<String, Object> session;
	ExampleActionClass action;

	public static class BaseExcampleAction {
		@ConversationScoped
		private Object baseObject;

		public Object getBaseObject() {
			return baseObject;
		}
	}

	public static class ExampleActionClass extends BaseExcampleAction implements ConversationAware {

		@ConversationScoped
		private Object object;

		@ConversationScoped("otherObject")
		private Object myObject;

		private Conversation conversation;

		public void setObject(Object object) {
			this.object = object;
		}

		public Object getObject() {
			return object;
		}

		public void setMyObject(Object myObject) {
			this.myObject = myObject;
		}

		public Object getMyObject() {
			return myObject;
		}

		@Override
		public void setConversation(Conversation conversation) {
			this.conversation = conversation;
		}

		public Conversation getConversation() {
			return conversation;
		}
	}

	@Before
	public void setup() {
		interceptor = new ConversationScopeInterceptor();
		session = Maps.newHashMap();
		action = new ExampleActionClass();
		when(invocation.getAction()).thenReturn(action);
		when(invocation.getInvocationContext()).thenReturn(context);
		when(context.getSession()).thenReturn(session);
	}

	@Test
	public void testIntercept() throws Exception {
		interceptor.intercept(invocation);

		assertNotNull("the action's object should be created", action.getObject());
		assertSame("the action's object should be stored in the session", action.getObject(),
				getConversation().get(Object.class, "object"));
	}

	@Test
	public void testConversationLookup() throws Exception {
		Object object = new Object();
		getConversation().put("object", object);

		interceptor.intercept(invocation);

		assertSame("the object previously stored in the conversation should be set on the action", object,
				action.getObject());
	}

	@Test
	public void testAnnotationName() throws Exception {
		Object expected = new Object();
		getConversation().put("otherObject", expected);

		interceptor.intercept(invocation);

		assertSame("the object should have been injected based on the value of the annotation", expected,
				action.getMyObject());

	}

	@Test
	public void testConversationAware() throws Exception {
		interceptor.intercept(invocation);

		assertNotNull(action.getConversation());
	}

	@Test
	public void testInjectionOnBaseProperties() throws Exception {
		Object expected = new Object();
		getConversation().put("baseObject", expected);

		interceptor.intercept(invocation);

		assertSame("The object should be injected on the base class", expected, action.getBaseObject());
	}

	private Conversation getConversation() {
		return interceptor.getDefaultConversation(action, session);
	}
}
