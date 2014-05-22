package net.techreadiness.plugin.service.reports;

public enum MinimumRecommendedFlag {

	MINIMUM("minimum"), RECOMMENDED("recommended");

	private String flagValue;

	private MinimumRecommendedFlag(String flagValue) {
		this.flagValue = flagValue;
	}

	public String getFlagValue() {
		return flagValue;
	}

}
