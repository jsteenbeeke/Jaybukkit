package com.jeroensteenbeeke.bk.ville.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.jeroensteenbeeke.bk.basics.entities.BaseEntity;

@Entity
public class ApprovedPlayer extends BaseEntity<String> {
	private static final long serialVersionUID = 1L;

	@Id
	private String player;

	@Override
	public String getId() {
		return player;
	}

	public String getPlayer() {
		return player;
	}

	public void setPlayer(String player) {
		this.player = player;
	}

}
