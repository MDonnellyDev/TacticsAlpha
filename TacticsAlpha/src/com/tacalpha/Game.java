package com.tacalpha;

import java.awt.event.KeyEvent;

import com.tacalpha.grid.Direction;
import com.tacalpha.grid.Grid;

public class Game {
	private boolean[] previousKeyState = new boolean[65535];
	private Grid grid;

	public Game() {
		// this.grid = new Grid(5, 6);
		this.grid = new Grid("res/maps/test.map");
	}

	public void update(boolean[] keyStates) {
		boolean up = keyStates[KeyEvent.VK_UP] && !this.previousKeyState[KeyEvent.VK_UP];
		boolean down = keyStates[KeyEvent.VK_DOWN] && !this.previousKeyState[KeyEvent.VK_DOWN];
		boolean left = keyStates[KeyEvent.VK_LEFT] && !this.previousKeyState[KeyEvent.VK_LEFT];
		boolean right = keyStates[KeyEvent.VK_RIGHT] && !this.previousKeyState[KeyEvent.VK_RIGHT];

		if (up) {
			this.grid.moveSelected(Direction.UP);
		} else if (down) {
			this.grid.moveSelected(Direction.DOWN);
		} else if (left) {
			this.grid.moveSelected(Direction.LEFT);
		} else if (right) {
			this.grid.moveSelected(Direction.RIGHT);
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
