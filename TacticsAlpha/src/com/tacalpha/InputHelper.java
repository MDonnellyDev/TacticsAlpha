package com.tacalpha;

public class InputHelper {
	private int initialDelay;
	private int repeatDelay;
	private int ticksHeld = 0;

	public InputHelper(int initialDelay, int repeatDelay) {
		this.initialDelay = initialDelay;
		this.repeatDelay = repeatDelay;
	}

	public boolean state(boolean currentState) {
		if (!currentState) {
			this.ticksHeld = 0;
			return false;
		}

		try {
			if (this.ticksHeld == 0) {
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
		} finally {
			this.ticksHeld++;
		}
	}
}
