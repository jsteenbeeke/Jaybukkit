package com.jeroensteenbeeke.bk.dbdump.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.jeroensteenbeeke.bk.basics.entities.BaseEntity;

@Entity
public class Foo extends BaseEntity<Long> {
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private Bar bar;

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Bar getBar() {
		return bar;
	}

	public void setBar(Bar bar) {
		this.bar = bar;
	}

}
