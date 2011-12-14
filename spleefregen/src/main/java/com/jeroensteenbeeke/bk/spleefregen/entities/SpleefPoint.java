package com.jeroensteenbeeke.bk.spleefregen.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class SpleefPoint {
	@Id
	private String name;

	@Column(nullable = false)
	private int material;

	@Column(nullable = false)
	private String world;

	@Column(nullable = false)
	private boolean locked;

	@OneToMany(mappedBy = "point", fetch = FetchType.LAZY)
	private List<SpleefLocation> locations;

	public SpleefPoint() {
	}

	public String getName() {
		return name;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
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

	public int getMaterial() {
		return material;
	}

	public void setMaterial(int material) {
		this.material = material;
	}

	public List<SpleefLocation> getLocations() {
		return locations;
	}

	public void setLocations(List<SpleefLocation> locations) {
		this.locations = locations;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SpleefPoint other = (SpleefPoint) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
