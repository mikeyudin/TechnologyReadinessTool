package net.techreadiness.batch.user;

import java.util.List;

import net.techreadiness.batch.BaseData;
import net.techreadiness.service.object.User;

import com.google.common.collect.Lists;

public class UserData extends BaseData {
	private String action;
	private String stateCode;
	private User user;
	private List<String> orgCodes;
	private List<String> roleCodes;

	public UserData() {
		user = new User();
		orgCodes = Lists.newArrayList();
		roleCodes = Lists.newArrayList();
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<String> getOrgCodes() {
		return orgCodes;
	}

	public void setOrgCodes(List<String> orgCodes) {
		this.orgCodes = orgCodes;
	}

	public List<String> getRoleCodes() {
		return roleCodes;
	}

	public void setRoleCodes(List<String> roleCodes) {
		this.roleCodes = roleCodes;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

}
