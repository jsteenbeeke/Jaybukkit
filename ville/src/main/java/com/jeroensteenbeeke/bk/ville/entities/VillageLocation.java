/**
 * This file is part of Jaybukkit.
 *
 * Jaybukkit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Jaybukkit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Jaybukkit.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jeroensteenbeeke.bk.ville.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

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

	@Column(nullable = false)
	private boolean restricted;

	@Column(nullable = false)
	private boolean entryLevel;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "location")
	private List<VilleBuilder> builders;

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

	public boolean isRestricted() {
		return restricted;
	}

	public void setRestricted(boolean restricted) {
		this.restricted = restricted;
	}

	public boolean isEntryLevel() {
		return entryLevel;
	}

	public void setEntryLevel(boolean entryLevel) {
		this.entryLevel = entryLevel;
	}

	public List<VilleBuilder> getBuilders() {
		if (builders == null)
			builders = new ArrayList<VilleBuilder>(0);

		return builders;
	}

	public void setBuilders(List<VilleBuilder> builders) {
		this.builders = builders;
	}

}
