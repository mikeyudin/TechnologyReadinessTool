package net.techreadiness.persistence;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@EntityListeners(value = AuditedEntityListener.class)
public class AuditedBaseEntity extends BaseEntity {

	@Column(name = "change_user", length = 50)
	private String changeUser;

	@Column(name = "change_date")
	private Date changeDate;

	public String getChangeUser() {
		return changeUser;
	}

	public void setChangeUser(String changeUser) {
		this.changeUser = changeUser;
	}

	public Date getChangeDate() {
		return changeDate;
	}

	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}
}
