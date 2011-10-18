package com.tacalpha;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.tacalpha.actor.Actor;
import com.tacalpha.grid.Direction;
import com.tacalpha.grid.Grid;
import com.tacalpha.input.InputHelper;
import com.tacalpha.input.InputRepeatHelper;
import com.tacalpha.input.InputSinglePressHelper;

public class Game {
	// Static constants
	private static final int GRID_HOLD_TIMER = 10;

	// Input
	private InputHelper upHelper = new InputRepeatHelper(Game.GRID_HOLD_TIMER * 3, Game.GRID_HOLD_TIMER);
	private InputHelper downHelper = new InputRepeatHelper(Game.GRID_HOLD_TIMER * 3, Game.GRID_HOLD_TIMER);
	private InputHelper leftHelper = new InputRepeatHelper(Game.GRID_HOLD_TIMER * 3, Game.GRID_HOLD_TIMER);
	private InputHelper rightHelper = new InputRepeatHelper(Game.GRID_HOLD_TIMER * 3, Game.GRID_HOLD_TIMER);
	private InputHelper enterHelper = new InputSinglePressHelper();

	// Game logic
	private Grid grid;
	private List<Actor> actors;
	private Actor currentActor;

	public Game() {
		// TODO: Temporary testing code.
		this.grid = new Grid("res/maps/test.map");
		this.actors = new ArrayList<Actor>();
		this.actors.add(new Actor(0, 0));
		this.actors.add(new Actor(2, 3));
		for (Actor actor : this.actors) {
			this.grid.addActorToGrid(actor, actor.getLocation());
		}
		this.currentActor = null;
	}

	public void update(boolean[] keyStates) {
		boolean up = this.upHelper.state(keyStates[KeyEvent.VK_UP]);
		boolean down = this.downHelper.state(keyStates[KeyEvent.VK_DOWN]);
		boolean left = this.leftHelper.state(keyStates[KeyEvent.VK_LEFT]);
		boolean right = this.rightHelper.state(keyStates[KeyEvent.VK_RIGHT]);
		boolean enter = this.enterHelper.state(keyStates[KeyEvent.VK_ENTER]);

		if (up) {
			this.grid.moveSelectedLocation(Direction.UP);
		} else if (down) {
			this.grid.moveSelectedLocation(Direction.DOWN);
		} else if (left) {
			this.grid.moveSelectedLocation(Direction.LEFT);
		} else if (right) {
			this.grid.moveSelectedLocation(Direction.RIGHT);
		} else if (enter) {
			if (this.currentActor == null) {
				this.currentActor = this.grid.getActor(this.grid.getSelectedLocation());
			} else {
				if (this.grid.moveActorIfPossible(this.currentActor, this.grid.getSelectedLocation())) {
					this.currentActor = null;
				}
			}
		}
	}

	// TODO: Remove this method. We should be returning an entire scene for the
	// ScreenRenderer to parse instead.
	public Grid getGrid() {
		return this.grid;
	}

	public Collection<Actor> getActors() {
		return this.actors;
	}
}
