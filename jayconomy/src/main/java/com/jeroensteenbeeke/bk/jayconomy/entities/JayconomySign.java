package com.jeroensteenbeeke.bk.jayconomy.entities;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.jeroensteenbeeke.bk.basics.entities.BaseEntity;

@Entity
public class JayconomySign extends BaseEntity<Long> {
	private static final long serialVersionUID = 1L;

	public static enum SignMode {
		BUY, SELL, DEPOSIT;
	}

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

	@Column(nullable = true)
	private Integer materialType;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private SignMode signMode;

	@Column(nullable = true)
	private String owner;

	@Column(nullable = true)
	private BigDecimal value;

	@Column(nullable = true)
	private Integer max;

	@Column(nullable = true)
	private Integer amount;

	@Column(nullable = true)
	private Byte subtype;

	public JayconomySign() {
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

	public Integer getMaterialType() {
		return materialType;
	}

	public void setMaterialType(Integer materialType) {
		this.materialType = materialType;
	}

	public SignMode getSignMode() {
		return signMode;
	}

	public void setSignMode(SignMode signMode) {
		this.signMode = signMode;
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

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public Integer getMax() {
		return max;
	}

	public void setMax(Integer max) {
		this.max = max;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Byte getSubtype() {
		return subtype;
	}

	public void setSubtype(Byte subtype) {
		this.subtype = subtype;
	}

}
