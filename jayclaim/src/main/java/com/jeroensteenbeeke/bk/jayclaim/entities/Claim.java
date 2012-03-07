package com.jeroensteenbeeke.bk.jayclaim.entities;

import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.jeroensteenbeeke.bk.basics.entities.BaseEntity;

@Entity
public class Claim extends BaseEntity<Long> {

	private static final long serialVersionUID = 1L;

	private static final Function<ClaimBuilder, String> BUILDER_TO_PLAYER_NAME = new Function<ClaimBuilder, String>() {
		@Override
		public String apply(ClaimBuilder builder) {
			return builder.getBuilderName();
		}
	};

	@Id
	private Long id;

	@Column(nullable = true)
	private String owner;

	@Column(nullable = false)
	private String world;

	@Column(nullable = false)
	private int x;

	@Column(nullable = false)
	private int z;

	@OneToMany(mappedBy = "claim", fetch = FetchType.EAGER)
	private List<ClaimBuilder> builders;

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

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public List<ClaimBuilder> getBuilders() {
		if (builders == null)
			builders = Lists.newArrayList();

		return builders;
	}

	public void setBuilders(List<ClaimBuilder> builders) {
		this.builders = builders;
	}

	@Transient
	public Set<String> getBuilderNames() {
		return Sets.newHashSet(Iterables.transform(getBuilders(),
				BUILDER_TO_PLAYER_NAME));
	}

	@Transient
	public String getEnterMessage() {
		if (getOwner() != null)
			return String.format("&fEntering area owned by &e%s", getOwner());

		return "&fEntering &cunclaimable&f area";
	}

	@Transient
	public String getExitMessage() {
		if (getOwner() != null)
			return String.format(
					"&fExiting area owned by &e%s, now in unclaimed land",
					getOwner());

		return "&fExiting &cunclaimable&f area, now in unclaimed land";
	}

}
