package com.jeroensteenbeeke.bk.jaycore.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.jeroensteenbeeke.bk.basics.entities.BaseEntity;

@Entity
public class DeathBan extends BaseEntity<String> {
	private static final long serialVersionUID = 1L;

	@Id
	private String playerName;

	@Column(nullable = false)
	private long banTime;

	@Override
	public String getId() {
		return playerName;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public long getBanTime() {
		return banTime;
	}

	public void setBanTime(long banTime) {
		this.banTime = banTime;
	}

}
