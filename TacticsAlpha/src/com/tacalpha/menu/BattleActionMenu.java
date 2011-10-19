package com.tacalpha.menu;

public class BattleActionMenu extends Menu {
	public static enum Action {
		MOVE, DELETE, CANCEL;
	}

	public BattleActionMenu() {
		this.addOption("Move");
		this.addOption("Delete");
		this.addOption("Cancel");
	}

	@Override
	public Action choose() {
		switch (this.getCurrent()) {
			case 0:
				return Action.MOVE;
			case 1:
				return Action.DELETE;
			case 2:
			default:
				return Action.CANCEL;
		}
	}
}
