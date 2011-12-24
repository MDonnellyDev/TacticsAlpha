package com.tacalpha.actor;

import com.tacalpha.equip.Equipment;
import com.tacalpha.grid.GridPoint;

public class Actor {
	// Battle
	private int xLocation;
	private int yLocation;
	private int currentHealth;
	private int currentMana;
	private int moveSpeed;

	// Static
	private int maxHealth;
	private int maxMana;
	private int strength;
	private int defense;

	// Equipment
	private Equipment leftHand;
	private Equipment rightHand;
	private Equipment head;
	private Equipment body;
	private Equipment feet;
	private Equipment other;

	// Configuration Methods
	public Actor(int x, int y) {
		this.xLocation = x;
		this.yLocation = y;
		this.moveSpeed = 5;
		this.strength = 10;
		this.defense = 10;
	}

	public void configure(int maxHealth, int maxMana) {
		this.maxHealth = maxHealth;
		this.maxMana = maxMana;
	}

	public int getMaxHealth() {
		return this.maxHealth;
	}

	public int getMaxMana() {
		return this.maxMana;
	}

	/**
	 * @param item
	 *            The item to equip. It will automatically be placed into an
	 *            appropriate slot, replacing whatever was already there.
	 * @return The item that was un-equipped in order to equip this item.
	 */
	public Equipment equip(Equipment item) {
		Equipment old = null;
		switch (item.getSlot()) {
			case HAND:
				if (this.rightHand == null) {
					this.applyStats(item);
					this.rightHand = item;
					return null;
				} else {
					old = this.leftHand;
					this.removeStats(old);
					this.applyStats(item);
					this.leftHand = item;
					return old;
				}
			case HEAD:
				old = this.head;
				this.removeStats(old);
				this.applyStats(item);
				this.head = item;
				return old;
			case BODY:
				old = this.body;
				this.removeStats(old);
				this.applyStats(item);
				this.body = item;
				return old;
			case FEET:
				old = this.feet;
				this.removeStats(old);
				this.applyStats(item);
				this.feet = item;
				return old;
			case OTHER:
				old = this.other;
				this.removeStats(old);
				this.applyStats(item);
				this.other = item;
				return old;
			default:
				return null;
		}
	}

	private void applyStats(Equipment item) {
		if (item != null) {
			this.maxHealth += item.getHpModifier();
			this.maxMana += item.getMpModifier();
			this.moveSpeed += item.getSpeedModifier();
			this.strength += item.getStrengthModifier();
			this.defense += item.getDefenseModifier();
		}
	}

	private void removeStats(Equipment item) {
		if (item != null) {
			this.maxHealth -= item.getHpModifier();
			this.maxMana -= item.getMpModifier();
			this.moveSpeed -= item.getSpeedModifier();
			this.strength -= item.getStrengthModifier();
			this.defense -= item.getDefenseModifier();
		}
	}

	// Battle Methods
	public void updateLocation(int x, int y) {
		this.xLocation = x;
		this.yLocation = y;
	}

	public GridPoint getLocation() {
		return new GridPoint(this.xLocation, this.yLocation);
	}

	public void prep() {
		this.currentHealth = this.maxHealth;
		this.currentMana = this.maxMana;
	}

	public void damage(int amount) {
		this.currentHealth -= amount;
		if (this.currentHealth < 0) {
			this.currentHealth = 0;
		}
	}

	public void heal(int amount) {
		this.currentHealth += amount;
		if (this.currentHealth > this.maxHealth) {
			this.currentHealth = this.maxHealth;
		}
	}

	public int getCurrentHealth() {
		return this.currentHealth;
	}

	public int getCurrentMana() {
		return this.currentMana;
	}

	public int getMoveSpeed() {
		return this.moveSpeed;
	}

	public int getStrength() {
		return this.strength;
	}

	public int getDefense() {
		return this.defense;
	}
}
