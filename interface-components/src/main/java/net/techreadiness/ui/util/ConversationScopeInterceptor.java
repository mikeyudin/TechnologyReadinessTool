package net.techreadiness.ui.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class ConversationScopeInterceptor extends AbstractInterceptor {
	private static final long serialVersionUID = 1L;
	private static final String CONVERSATION_PARAM = "__conversation__";

	private static final Logger log = LoggerFactory.getLogger(ConversationScopeInterceptor.class);

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		Object action = invocation.getAction();
		Map<String, Object> session = invocation.getInvocationContext().getSession();
		Conversation conversation = getDefaultConversation(action, session);

		if (action instanceof ConversationAware) {
			((ConversationAware) action).setConversation(conversation);
		}

		for (Field field : getInheritedPrivateFields(action.getClass())) {
			if (field.isAnnotationPresent(ConversationScoped.class)) {
				ConversationScoped annotation = field.getAnnotation(ConversationScoped.class);
				String name = StringUtils.defaultIfEmpty(annotation.value(), field.getName());
				log.debug("setting value for {}", name);
				Object instance = conversation.get(field.getType(), name);
				field.setAccessible(true);
				field.set(action, instance);
			}
		}
		return invocation.invoke();
	}

	Conversation getDefaultConversation(Object action, Map<String, Object> session) {
		Conversation conversation = (Conversation) session.get(CONVERSATION_PARAM);
		if (conversation == null) {
			conversation = new Conversation();
			session.put(CONVERSATION_PARAM, conversation);
		}
		return conversation;
	}

	private static List<Field> getInheritedPrivateFields(Class<?> type) {
		List<Field> result = new ArrayList<>();

		Class<?> i = type;
		while (i != null && i != Object.class) {
			result.addAll(Arrays.asList(i.getDeclaredFields()));
			i = i.getSuperclass();
		}

		return result;
	}
}
