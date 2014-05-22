package net.techreadiness.customer.action.task.org.create;

import javax.inject.Inject;

import net.techreadiness.service.OrganizationService;
import net.techreadiness.service.object.Org;
import net.techreadiness.ui.action.filters.FilterSelectionHandler;
import net.techreadiness.ui.action.task.org.OrgTaskFlowAction;
import net.techreadiness.ui.util.ConversationAware;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.opensymphony.xwork2.Preparable;

public class BaseOrgAction extends OrgTaskFlowAction implements Preparable, ApplicationContextAware {
	private static final long serialVersionUID = 1L;

	@Inject
	protected OrganizationService organizationService;

	protected Org organization;
	protected Long parentOrganizationId;
	private ApplicationContext applicationContext;

	@Override
	public void prepare() throws Exception {
		organization = new Org();
	}

	public void setOrganization(Org org) {
		organization = org;
	}

	public Org getOrganization() {
		return organization;
	}

	public void setParentOrganizationId(Long parentOrganizationId) {
		this.parentOrganizationId = parentOrganizationId;
	}

	public Long getParentOrganizationId() {
		return parentOrganizationId;
	}

	protected FilterSelectionHandler<Org> getOrgSelectionHandler() {
		String beanName = conversation.get(String.class, "orgFilterSelectionHandler");
		FilterSelectionHandler<Org> selectionHandler = applicationContext.getBean(beanName, FilterSelectionHandler.class);
		if (selectionHandler instanceof ConversationAware) {
			((ConversationAware) selectionHandler).setConversation(conversation);
		}
		return selectionHandler;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;

	}

}
