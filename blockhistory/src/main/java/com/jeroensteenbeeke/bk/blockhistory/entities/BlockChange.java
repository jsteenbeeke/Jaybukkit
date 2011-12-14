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
package com.jeroensteenbeeke.bk.blockhistory.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.jeroensteenbeeke.bk.basics.entities.BaseEntity;

@Entity
public class BlockChange extends BaseEntity<Long> {
	private static final long serialVersionUID = 1L;

	public static enum BlockChangeType {
		PLACED, DESTROYED, FADED, DECAYED, SPREAD, FORMED, PISTONED, ENDERMAN_REMOVE, ENDERMAN_PLACE, EXPLODED, GENERATED, REVERTED;
	}

	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private BlockChangeType changeType;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date changeDate;

	@Column(nullable = true)
	private String culprit;

	@Column(nullable = false)
	private String world;

	@Column(nullable = false)
	private int x;

	@Column(nullable = false)
	private int y;

	@Column(nullable = false)
	private int z;

	@Column(nullable = false)
	private int blockType;

	@ManyToOne(optional = true)
	private BlockChange overrides;

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BlockChangeType getChangeType() {
		return changeType;
	}

	public void setChangeType(BlockChangeType changeType) {
		this.changeType = changeType;
	}

	public Date getChangeDate() {
		return changeDate;
	}

	public void setChangeDate(Date date) {
		this.changeDate = date;
	}

	public String getCulprit() {
		return culprit;
	}

	public void setCulprit(String culprit) {
		this.culprit = culprit;
	}

	public String getWorld() {
		return world;
	}

	public void setWorld(String world) {
		this.world = world;
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

	public int getBlockType() {
		return blockType;
	}

	public void setBlockType(int blockType) {
		this.blockType = blockType;
	}

	public BlockChange getOverrides() {
		return overrides;
	}

	public void setOverrides(BlockChange overrides) {
		this.overrides = overrides;
	}

}
