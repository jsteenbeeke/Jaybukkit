package com.jeroensteenbeeke.bk.premium;

public enum PlayerType {
	GUEST, REGULAR(GUEST), PREMIUM(REGULAR);

	private final PlayerType lowerType;

	private final String kickMessage;

	private PlayerType() {
		this.lowerType = null;
		this.kickMessage = "No slots of type %s available, sorry :-(";
	}

	private PlayerType(PlayerType lower) {
		this.lowerType = lower;
		this.kickMessage = "No slots of type %s or lower available, sorry :-(";
	}

	public PlayerType getLowerType() {
		return this.lowerType;
	}

	public String getKickMessage() {
		return String.format(this.kickMessage, name());
	}
}
