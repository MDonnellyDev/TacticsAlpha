package com.tacalpha.actor;

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
