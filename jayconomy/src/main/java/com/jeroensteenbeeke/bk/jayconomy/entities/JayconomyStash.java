package com.jeroensteenbeeke.bk.jayconomy.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.jeroensteenbeeke.bk.basics.entities.BaseEntity;

@Entity
public class JayconomyStash extends BaseEntity<Integer> {

	private static final long serialVersionUID = 1L;

	@Id
	private int materialType;

	@Column(nullable = false)
	private int supply;

	@Override
	public Integer getId() {
		return getMaterialType();
	}

	public int getMaterialType() {
		return materialType;
	}

	public void setMaterialType(int materialType) {
		this.materialType = materialType;
	}

	public int getSupply() {
		return supply;
	}

	public void setSupply(int supply) {
		this.supply = supply;
	}

}
