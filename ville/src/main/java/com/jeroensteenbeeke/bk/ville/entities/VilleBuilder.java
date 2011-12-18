package com.jeroensteenbeeke.bk.ville.entities;

import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.jeroensteenbeeke.bk.basics.entities.BaseEntity;

public class VilleBuilder extends BaseEntity<String> {
	private static final long serialVersionUID = 1L;

	@Id
	private String player;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private VillageLocation location;

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

	public VillageLocation getLocation() {
		return location;
	}

	public void setLocation(VillageLocation location) {
		this.location = location;
	}

}
