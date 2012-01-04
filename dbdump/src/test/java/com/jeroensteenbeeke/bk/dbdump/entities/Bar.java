package com.jeroensteenbeeke.bk.dbdump.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.jeroensteenbeeke.bk.basics.entities.BaseEntity;

@Entity
public class Bar extends BaseEntity<String> {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Override
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
