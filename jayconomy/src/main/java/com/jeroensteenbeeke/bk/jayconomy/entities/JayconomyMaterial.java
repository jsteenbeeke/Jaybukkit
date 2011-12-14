package com.jeroensteenbeeke.bk.jayconomy.entities;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.jeroensteenbeeke.bk.basics.entities.BaseEntity;

@Entity
public class JayconomyMaterial extends BaseEntity<Integer> {
	private static final long serialVersionUID = 1L;

	@Id
	private int itemId;

	@Column(nullable = false)
	private BigDecimal worth;

	@Column(nullable = false)
	private boolean sellable;

	@Column(nullable = false)
	private int stackSize;

	public JayconomyMaterial() {

	}

	@Override
	public Integer getId() {
		return getItemId();
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public BigDecimal getWorth() {
		return worth;
	}

	public void setWorth(BigDecimal worth) {
		this.worth = worth;
	}

	public boolean isSellable() {
		return sellable;
	}

	public void setSellable(boolean sellable) {
		this.sellable = sellable;
	}

	public int getStackSize() {
		return stackSize;
	}

	public void setStackSize(int stackSize) {
		this.stackSize = stackSize;
	}

}
