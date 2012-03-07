package com.jeroensteenbeeke.bk.jayclaim.entities;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.jeroensteenbeeke.bk.basics.entities.BaseEntity;

public class ClaimBuilder extends BaseEntity<Long> {
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Claim claim;

	@Column(nullable = false)
	private String builderName;

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Claim getClaim() {
		return claim;
	}

	public void setClaim(Claim claim) {
		this.claim = claim;
	}

	public String getBuilderName() {
		return builderName;
	}

	public void setBuilderName(String builderName) {
		this.builderName = builderName;
	}

}
