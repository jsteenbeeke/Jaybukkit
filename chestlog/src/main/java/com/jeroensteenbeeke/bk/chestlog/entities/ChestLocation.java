package com.jeroensteenbeeke.bk.chestlog.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.jeroensteenbeeke.bk.basics.entities.BaseEntity;

@Entity
public class ChestLocation extends BaseEntity<Long> {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false)
	private int x;

	@Column(nullable = false)
	private int y;

	@Column(nullable = false)
	private int z;

	@Column(nullable = false)
	private String world;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private ChestData chest;

	public ChestLocation() {
	}

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getWorld() {
		return world;
	}

	public void setWorld(String world) {
		this.world = world;
	}

	public ChestData getChest() {
		return chest;
	}

	public void setChest(ChestData chest) {
		this.chest = chest;
	}

}
