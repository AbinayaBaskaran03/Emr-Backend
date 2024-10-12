package com.emrheathgroup.backend.Enumeration;

public enum TimeInterval {

	FIFTEEN_MINUTES(15), THIRTY_MINUTES(30), FOURTY_FIVE_MINUTES(45), ONE_HOUR(60);

	private final int minutes;

	TimeInterval(int minutes) {
		this.minutes = minutes;
	}

	public int getMinutes() {
		return minutes;
	}
}
