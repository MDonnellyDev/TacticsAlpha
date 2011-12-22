package com.tacalpha.input;

public class InputRepeatHelper implements InputHelper {
	private int initialDelay;
	private int repeatDelay;
	private int ticksHeld = 0;

	public InputRepeatHelper(int initialDelay, int repeatDelay) {
		this.initialDelay = initialDelay;
		this.repeatDelay = repeatDelay;
	}

	@Override
	public void update(boolean currentState) {
		if (currentState) {
			this.ticksHeld++;
		} else {
			this.ticksHeld = 0;
		}

	}

	@Override
	public boolean state() {
		if (this.ticksHeld == 0) {
			return false;
		}
		if (this.ticksHeld == 1) {
			return true;
		}
		if (this.ticksHeld < this.initialDelay) {
			return false;
		}
		if (this.ticksHeld == this.initialDelay) {
			return true;
		}
		if ((this.ticksHeld - this.initialDelay) % this.repeatDelay == 0) {
			return true;
		} else {
			return false;
		}
	}
}
