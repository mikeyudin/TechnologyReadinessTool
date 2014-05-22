package net.techreadiness.navigation.taskflow.youraccount;

import java.util.ArrayList;
import java.util.List;

import net.techreadiness.service.object.User;
import net.techreadiness.ui.task.TaskFlowData;

import org.springframework.stereotype.Component;

@Component
@org.springframework.context.annotation.Scope("session")
public class YourAccountTaskFlowData extends TaskFlowData {

	private static final long serialVersionUID = 1L;

	private List<User> users;

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public List<User> getUsers() {
		return users;
	}

	public List<Long> getUserIds() {
		List<Long> ids = new ArrayList<>();
		if (users != null) {
			for (User user : users) {
				ids.add(user.getUserId());
			}
		}
		return ids;
	}
}