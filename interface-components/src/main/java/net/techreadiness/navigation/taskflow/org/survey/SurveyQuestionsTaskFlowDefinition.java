package net.techreadiness.navigation.taskflow.org.survey;

import static net.techreadiness.security.CorePermissionCodes.READY_CUSTOMER_FILE_ORG_INFO;
import net.techreadiness.ui.task.Task;
import net.techreadiness.ui.task.TaskFlowDefinition;

import org.springframework.stereotype.Component;

@Component
public class SurveyQuestionsTaskFlowDefinition extends TaskFlowDefinition {

	public SurveyQuestionsTaskFlowDefinition() {
		addExternalTask(new Task("/task/batch/org/info", "org-info-batch-task", "task.filebatch.orgInfo",
				READY_CUSTOMER_FILE_ORG_INFO));
	}

	@Override
	public String getNamespace() {
		return "/task/org/survey";
	}

	@Override
	public String getStartAction() {
		return "surveyQuestionsTaskFlowBegin";
	}

	@Override
	public String getReturnUrl() {
		return "/org/survey/list";
	}

	@Override
	public String getEndTaskFlowUrl() {
		return "task/survey/end";
	}

}
