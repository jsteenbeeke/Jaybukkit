package com.jeroensteenbeeke.bk.jaycore.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.jeroensteenbeeke.bk.basics.entities.BaseEntity;

@Entity
public class TorchGiven extends BaseEntity<String> {
	private static final long serialVersionUID = 1L;

	@Id
	private String playername;

	@Override
	public String getId() {
		return playername;
	}

	public void setPlayername(String playername) {
		this.playername = playername;
	}

	public String getPlayername() {
		return playername;
	}

}
