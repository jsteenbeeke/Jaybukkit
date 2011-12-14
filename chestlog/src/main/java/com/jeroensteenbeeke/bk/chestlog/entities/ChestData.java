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
