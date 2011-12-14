package com.jeroensteenbeeke.bk.jayconomy.entities;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.jeroensteenbeeke.bk.basics.entities.BaseEntity;

@Entity
public class Balance extends BaseEntity<String> {

	private static final long serialVersionUID = 1L;

	@Id
	private String playerName;

	@Column(nullable = false)
	private BigDecimal balance;

	public Balance() {
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

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

}
