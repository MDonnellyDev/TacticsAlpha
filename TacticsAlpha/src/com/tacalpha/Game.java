package com.tacalpha;

import java.awt.event.KeyEvent;

import com.tacalpha.grid.Direction;
import com.tacalpha.grid.Grid;

public class Game {
	// Static constants
	private static final int GRID_HOLD_TIMER = 10;

	// Input
	private boolean[] previousKeyState = new boolean[65535];
	private InputHelper upHelper = new InputHelper(Game.GRID_HOLD_TIMER * 3, Game.GRID_HOLD_TIMER);
	private InputHelper downHelper = new InputHelper(Game.GRID_HOLD_TIMER * 3, Game.GRID_HOLD_TIMER);
	private InputHelper leftHelper = new InputHelper(Game.GRID_HOLD_TIMER * 3, Game.GRID_HOLD_TIMER);
	private InputHelper rightHelper = new InputHelper(Game.GRID_HOLD_TIMER * 3, Game.GRID_HOLD_TIMER);

	// Game logic
	private Grid grid;

	public Game() {
		// this.grid = new Grid(5, 6);
		this.grid = new Grid("res/maps/test.map");
	}

	public void update(boolean[] keyStates) {
		boolean up = this.upHelper.state(keyStates[KeyEvent.VK_UP]);
		boolean down = this.downHelper.state(keyStates[KeyEvent.VK_DOWN]);
		boolean left = this.leftHelper.state(keyStates[KeyEvent.VK_LEFT]);
		boolean right = this.rightHelper.state(keyStates[KeyEvent.VK_RIGHT]);

		if (up) {
			this.grid.moveSelectedLocation(Direction.UP);
		} else if (down) {
			this.grid.moveSelectedLocation(Direction.DOWN);
		} else if (left) {
			this.grid.moveSelectedLocation(Direction.LEFT);
		} else if (right) {
			this.grid.moveSelectedLocation(Direction.RIGHT);
		}

		this.copyPreviousInput(keyStates);
	}

	// TODO: Remove this method. We should be returning an entire scene for the
	// ScreenRenderer to parse instead.
	public Grid getGrid() {
		return this.grid;
	}

	private void copyPreviousInput(boolean[] newInput) {
		for (int i = 0; i < newInput.length; i++) {
			this.previousKeyState[i] = newInput[i];
		}
	}
}
