package com.tacalpha.equip;

public abstract class Equipment {
	public enum Slot {
		HAND, HEAD, BODY, FEET, OTHER
	}

	protected int strengthModifier = 0;
	protected int defenseModifier = 0;
	protected int speedModifier = 0;
	protected int hpModifier = 0;
	protected int mpModifier = 0;

	protected Slot slot = Slot.HAND;

	public int getStrengthModifier() {
		return this.strengthModifier;
	}

	public int getDefenseModifier() {
		return this.defenseModifier;
	}

	public int getSpeedModifier() {
		return this.speedModifier;
	}

	public int getHpModifier() {
		return this.hpModifier;
	}

	public int getMpModifier() {
		return this.mpModifier;
	}

	public Slot getSlot() {
		return this.slot;
	}
}
