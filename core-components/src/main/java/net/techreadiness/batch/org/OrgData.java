package net.techreadiness.batch.org;

import java.util.Map;

import net.techreadiness.batch.BaseData;
import net.techreadiness.service.object.Contact;
import net.techreadiness.service.object.Org;

import com.google.common.collect.Maps;

public class OrgData extends BaseData {
	private Org org;
	private Map<String, Contact> contacts;

	public OrgData() {
		org = new Org();
		contacts = Maps.newHashMap();
		contacts.put("primary", new Contact());
		contacts.put("secondary", new Contact());
	}

	public Org getOrg() {
		return org;
	}

	public void setOrg(Org org) {
		this.org = org;
	}

	public Map<String, Contact> getContacts() {
		return contacts;
	}

	public void setContacts(Map<String, Contact> contacts) {
		this.contacts = contacts;
	}
}
