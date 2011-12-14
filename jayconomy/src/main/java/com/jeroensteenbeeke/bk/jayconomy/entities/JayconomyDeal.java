package com.jeroensteenbeeke.bk.jayconomy.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.jeroensteenbeeke.bk.basics.entities.BaseEntity;

@Entity
public class JayconomyDeal extends BaseEntity<Long> {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false)
	private int amount;

	@Column(nullable = false)
	private int materialType;

	@Column(nullable = false)
	private String player;
	
	@Column(nullable = false)
	private Byte subType;

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getMaterialType() {
		return materialType;
	}

	public void setMaterialType(int materialType) {
		this.materialType = materialType;
	}

	public String getPlayer() {
		return player;
	}

	public void setPlayer(String player) {
		this.player = player;
	}

	public Byte getSubType() {
		return subType;
	}

	public void setSubType(Byte subType) {
		this.subType = subType;
	}
	
	
}
