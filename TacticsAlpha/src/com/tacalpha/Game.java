package com.tacalpha;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.tacalpha.actor.Actor;
import com.tacalpha.battle.BasicAttack;
import com.tacalpha.battle.Effect;
import com.tacalpha.equip.LongSword;
import com.tacalpha.grid.Direction;
import com.tacalpha.grid.Grid;
import com.tacalpha.grid.Tile;
import com.tacalpha.input.InputHelper;
import com.tacalpha.input.InputRepeatHelper;
import com.tacalpha.input.InputSinglePressHelper;
import com.tacalpha.menu.BattleActionMenu;
import com.tacalpha.menu.Menu;

public class Game {
	// Static constants
	private static final int GRID_HOLD_TIMER = 10;

	// Input
	private InputHelper upHelper = new InputRepeatHelper(Game.GRID_HOLD_TIMER * 3, Game.GRID_HOLD_TIMER);
	private InputHelper downHelper = new InputRepeatHelper(Game.GRID_HOLD_TIMER * 3, Game.GRID_HOLD_TIMER);
	private InputHelper leftHelper = new InputRepeatHelper(Game.GRID_HOLD_TIMER * 3, Game.GRID_HOLD_TIMER);
	private InputHelper rightHelper = new InputRepeatHelper(Game.GRID_HOLD_TIMER * 3, Game.GRID_HOLD_TIMER);
	private InputHelper enterHelper = new InputSinglePressHelper();
	private InputHelper escHelper = new InputSinglePressHelper();
	private InputHelper equalsHelper = new InputSinglePressHelper();
	private InputHelper minusHelper = new InputSinglePressHelper();

	// Game logic
	private Grid grid;
	private List<Actor> actors;
	private Actor currentActor;
	private Effect currentEffect;
	private Menu activeMenu;
	private GameState state;

	// Player Display stuff
	private String message;
	private boolean showMessage;

	// Meta Information
	private int tileSize;

	public Game() {
		// TODO: Temporary testing code.
		this.grid = new Grid("res/maps/test.map");
		this.actors = new ArrayList<Actor>();
		Actor temp = new Actor(0, 0);
		temp.configure(100, 50);
		temp.prep();
		temp.damage(25);
		this.actors.add(temp);
		temp.equip(new LongSword());
		temp = new Actor(2, 3);
		temp.configure(120, 45);
		temp.prep();
		this.actors.add(temp);
		for (Actor actor : this.actors) {
			this.grid.addActorToGrid(actor, actor.getLocation());
		}
		this.currentActor = null;
		this.activeMenu = null;
		this.state = GameState.INPUT;
		this.tileSize = 50;
	}

	public void update(boolean[] keyStates) {
		this.upHelper.update(keyStates[KeyEvent.VK_UP]);
		this.downHelper.update(keyStates[KeyEvent.VK_DOWN]);
		this.leftHelper.update(keyStates[KeyEvent.VK_LEFT]);
		this.rightHelper.update(keyStates[KeyEvent.VK_RIGHT]);
		this.enterHelper.update(keyStates[KeyEvent.VK_ENTER]);
		this.escHelper.update(keyStates[KeyEvent.VK_ESCAPE]);
		this.equalsHelper.update(keyStates[KeyEvent.VK_EQUALS]);
		this.minusHelper.update(keyStates[KeyEvent.VK_MINUS]);

		this.handleMetaInput();

		if (this.activeMenu != null) {
			this.handleMenuInput();
		} else {
			this.handleGameInput();
			this.displayGameMessage();
		}
	}

	private void handleMetaInput() {
		if (this.equalsHelper.state()) {
			this.tileSize += 5;
			if (this.tileSize > 100) {
				this.tileSize = 100;
			}
		} else if (this.minusHelper.state()) {
			this.tileSize -= 5;
			if (this.tileSize < 25) {
				this.tileSize = 25;
			}
		}
	}

	private void handleMenuInput() {
		if (this.upHelper.state()) {
			this.activeMenu.up();
		} else if (this.downHelper.state()) {
			this.activeMenu.down();
		} else if (this.enterHelper.state()) {
			if (this.activeMenu instanceof BattleActionMenu) {
				BattleActionMenu.Action action = (BattleActionMenu.Action) this.activeMenu.choose();
				switch (action) {
					case MOVE:
						this.currentActor = this.grid.getSelectedTile().getOccupant();
						this.state = GameState.MOVING;
						this.grid.setMoveRadius(this.grid.getSelectedLocation(), this.currentActor.getMoveSpeed());
						this.message = "Moving. Press ENTER to place or ESC to cancel.";
						this.showMessage = true;
						break;
					case ATTACK:
						this.currentActor = this.grid.getSelectedTile().getOccupant();
						this.currentEffect = new BasicAttack(this.currentActor);
						this.currentEffect.setupTargeting(this.grid);
						this.grid.setAreaOfEffect(this.currentEffect.getAreaOfEffect());
						this.state = GameState.ATTACKING;
						this.message = "Attacking. Press ENTER to choose your target or ESC to cancel.";
						this.showMessage = true;
						break;
					case DELETE:
						this.actors.remove(this.grid.getSelectedTile().getOccupant());
						this.grid.removeSelectedActor();
						break;
					case CANCEL:
					default:
						break;
				}
			}
			this.activeMenu = null;
		}
		if (this.activeMenu != null) {
			this.message = this.activeMenu.getDescription();
		}
	}

	private void handleGameInput() {
		if (this.upHelper.state()) {
			this.grid.moveSelectedLocation(Direction.UP);
		} else if (this.downHelper.state()) {
			this.grid.moveSelectedLocation(Direction.DOWN);
		} else if (this.leftHelper.state()) {
			this.grid.moveSelectedLocation(Direction.LEFT);
		} else if (this.rightHelper.state()) {
			this.grid.moveSelectedLocation(Direction.RIGHT);
		} else if (this.enterHelper.state()) {
			if (this.state.equals(GameState.INPUT)) {
				if (this.grid.getSelectedTile().getOccupant() != null) {
					this.activeMenu = new BattleActionMenu();
				}
			} else if (this.state.equals(GameState.MOVING)) {
				if (this.grid.moveActorIfPossible(this.currentActor, this.grid.getSelectedLocation())) {
					this.clearGameState();
				} else {
					this.message = "Cannot move there.";
				}
			} else if (this.state.equals(GameState.ATTACKING)) {
				Set<Actor> targets = this.grid.getTargetsOfCurrentEffect();
				this.currentEffect.applyEffect(targets);
				for (Actor target : targets) {
					if (target.getCurrentHealth() <= 0) {
						this.actors.remove(target);
						this.grid.getTile(target.getLocation()).setOccupant(null);
					}
				}
				this.clearGameState();
			}
		} else if (this.escHelper.state()) {
			if (this.state.equals(GameState.MOVING)) {
				this.clearGameState();
			}
		}
	}

	private void displayGameMessage() {
		// Give some info on the current square if there's nothing else to say.
		if (!this.showMessage) {
			Tile tile = this.grid.getSelectedTile();
			if (tile.getOccupant() != null) {
				this.message = "Press ENTER to interact with this unit.";
			} else if (tile.isImpassable()) {
				this.message = "Impassable terrain.";
			} else {
				this.message = "Normal square.";
			}
		}
	}

	private void clearGameState() {
		this.message = null;
		this.showMessage = false;
		this.currentActor = null;
		this.currentEffect = null;
		this.grid.clearTargetingLayer();
		this.state = GameState.INPUT;
	}

	// TODO: Remove this method. We should be returning an entire scene for the
	// ScreenRenderer to parse instead.
	public Grid getGrid() {
		return this.grid;
	}

	public Collection<Actor> getActors() {
		return this.actors;
	}

	public Menu getActiveMenu() {
		return this.activeMenu;
	}

	public String getMessage() {
		return this.message;
	}

	public int getTileSize() {
		return this.tileSize;
	}
}
