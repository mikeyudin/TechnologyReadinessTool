package net.techreadiness.service;

import java.io.Serializable;

import net.techreadiness.annotation.CoreDataModificationStatus.ModificationType;

import com.google.common.base.Objects;

public class DataModificationStatus implements Serializable {

	private static final long serialVersionUID = 1L;

	public enum ModificationState {
		NONE, REQUESTED, SUCCESS, FAILURE
	}

	private ModificationState modificationState = ModificationState.NONE;

	private String message;

	// TODO: is this necessary?
	private ModificationType modificationType = ModificationType.NONE;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ModificationState getModificationState() {
		return modificationState;
	}

	public void setModificationState(ModificationState modificationState) {
		// This is used somewhat as state. You should only be able to go to a
		// "higher" type state (eg. can't go from failure to success). This
		// works due to being reset() once displayed to the user.
		if (this.modificationState.ordinal() < modificationState.ordinal()) {
			this.modificationState = modificationState;
		}
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("message", message).add("modificationState", modificationState.toString())
				.toString();
	}

	public void reset() {
		message = "";
		modificationState = ModificationState.NONE;
		modificationType = ModificationType.NONE;
	}

	public ModificationType getModificationType() {
		return modificationType;
	}

	public void setModificationType(ModificationType modificationType) {
		this.modificationType = modificationType;
	}
}
