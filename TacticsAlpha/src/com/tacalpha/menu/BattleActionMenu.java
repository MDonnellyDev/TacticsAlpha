package com.tacalpha.menu;

public class BattleActionMenu extends Menu {
	public static enum Action {
		MOVE, ATTACK, CAST, CANCEL;
	}

	public BattleActionMenu() {
		this.addOption("Move");
		this.addOption("Attack");
		this.addOption("Spell");
		this.addOption("Cancel");
	}

	@Override
	public Action choose() {
		switch (this.getCurrent()) {
			case 0:
				return Action.MOVE;
			case 1:
				return Action.ATTACK;
			case 2:
				return Action.CAST;
			case 3:
			default:
				return Action.CANCEL;
		}
	}

	@Override
	public String getDescription() {
		switch (this.getCurrent()) {
			case 0:
				return "Move this unit to another location.";
			case 1:
				return "Delete this unit permanently.";
			case 2:
			default:
				return "Take no action.";
		}
	}
}
