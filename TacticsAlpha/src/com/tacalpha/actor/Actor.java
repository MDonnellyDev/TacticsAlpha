package com.tacalpha.actor;

import java.util.HashMap;
import java.util.Map;

import com.tacalpha.equip.Equipment;
import com.tacalpha.equip.Slot;
import com.tacalpha.grid.GridPoint;

public class Actor {
	// Battle
	private int xLocation;
	private int yLocation;
	private int currentHealth;
	private int currentMana;
	private int moveSpeed;
	private int actionSpeed;

	// Static
	private int maxHealth;
	private int maxMana;
	private int strength;
	private int defense;

	// Equipment
	private Map<Slot, Equipment> equipment = new HashMap<Slot, Equipment>();

	// Configuration Methods
	public Actor(int x, int y) {
		this.xLocation = x;
		this.yLocation = y;
		this.moveSpeed = 5;
		this.actionSpeed = 5;
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
	 * @return The item that was un-equipped in order to equip this item, or
	 *         null if no item was equipped.
	 */
	public Equipment equip(Equipment item) {
		switch (item.getSlot()) {
			case HAND:
				if (this.equipment.get(Slot.MAINHAND) == null) {
					return this.equip(item, Slot.MAINHAND);
				} else {
					return this.equip(item, Slot.OFFHAND);
				}
			default:
				return this.equip(item, item.getSlot());
		}
	}

	/**
	 * Equip an item to a specific slot, if possible.
	 * 
	 * @param item
	 *            The item to equip.
	 * @param slot
	 *            The slot to equip it to. HAND is not a valid slot; this method
	 *            should be given a specific slot.
	 * @return The old item that was equipped in that slot, if there was an item
	 *         to replace. Null, if there was no item to replace. The item
	 *         passed into the method, if no replacement was made.
	 */
	public Equipment equip(Equipment item, Slot slot) {
		if (Slot.HAND.equals(slot)) {
			return item;
		}
		if (item.fitsSlot(slot)) {
			Equipment old = this.equipment.get(slot);
			this.removeStats(old);
			this.applyStats(item);
			this.equipment.put(slot, item);
			return old;
		} else {
			return item;
		}
	}

	public Equipment getEquipment(Slot slot) {
		return this.equipment.get(slot);
	}

	private void applyStats(Equipment item) {
		if (item != null) {
			this.maxHealth += item.getHpModifier();
			this.maxMana += item.getMpModifier();
			this.moveSpeed += item.getMoveSpeedModifier();
			this.actionSpeed += item.getActionSpeedModifier();
			this.strength += item.getStrengthModifier();
			this.defense += item.getDefenseModifier();
		}
	}

	private void removeStats(Equipment item) {
		if (item != null) {
			this.maxHealth -= item.getHpModifier();
			this.maxMana -= item.getMpModifier();
			this.moveSpeed -= item.getMoveSpeedModifier();
			this.actionSpeed -= item.getActionSpeedModifier();
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

	public boolean spendMpIfAble(int amount) {
		if (this.currentMana < amount) {
			return false;
		}
		this.currentMana -= amount;
		return true;
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

	public int getActionSpeed() {
		return this.actionSpeed;
	}

	public int getStrength() {
		return this.strength;
	}

	public int getDefense() {
		return this.defense;
	}
}
