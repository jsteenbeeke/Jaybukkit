package com.jeroensteenbeeke.bk.jayop.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.jeroensteenbeeke.bk.basics.entities.BaseEntity;

@Entity
public class Suspension extends BaseEntity<String> {
	private static final long serialVersionUID = 1L;

	@Id
	private String playerName;

	@Column(nullable = false)
	private long duration;

	@Column(nullable = false)
	private long start;

	@Column(nullable = false)
	private String suspendedBy;

	public Suspension() {
	}

	@Override
	public String getId() {
		return getPlayerName();
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public String getSuspendedBy() {
		return suspendedBy;
	}

	public void setSuspendedBy(String suspendedBy) {
		this.suspendedBy = suspendedBy;
	}
}
