package com.jeroensteenbeeke.bk.ville.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.jeroensteenbeeke.bk.basics.entities.BaseEntity;

@Entity
public class VillageLocation extends BaseEntity<String> {
	private static final long serialVersionUID = 1L;

	@Id
	private String name;

	@Column(nullable = false)
	private String world;

	@Column(nullable = false)
	private String owner;

	@Column(nullable = false)
	private int x;

	@Column(nullable = false)
	private int y;

	@Column(nullable = false)
	private int z;

	@Override
	public String getId() {
		return getName();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWorld() {
		return world;
	}

	public void setWorld(String world) {
		this.world = world;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

}
