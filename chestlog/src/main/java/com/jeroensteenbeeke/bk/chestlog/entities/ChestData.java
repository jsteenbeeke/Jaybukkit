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
package com.jeroensteenbeeke.bk.chestlog.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import com.jeroensteenbeeke.bk.basics.entities.BaseEntity;

@Entity
public class ChestData extends BaseEntity<Long> {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "chest")
	private List<ChestLocation> locations;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "chest")
	@OrderBy("date desc")
	private List<ChestLog> logs;

	@Column(nullable = false)
	private String owner;

	public ChestData() {
	}

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public List<ChestLocation> getLocations() {
		return locations;
	}

	public void setLocations(List<ChestLocation> locations) {
		this.locations = locations;
	}

	public List<ChestLog> getLogs() {
		return logs;
	}

	public void setLogs(List<ChestLog> logs) {
		this.logs = logs;
	}

}
