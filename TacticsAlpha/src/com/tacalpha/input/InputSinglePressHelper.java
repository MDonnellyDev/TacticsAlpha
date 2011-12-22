package com.tacalpha.input;

public class InputSinglePressHelper implements InputHelper {
	private boolean held;
	private boolean pressed;

	public InputSinglePressHelper() {

	}

	@Override
	public void update(boolean currentKeyState) {
		if (!currentKeyState) {
			this.held = false;
			this.pressed = false;
		} else {
			if (this.pressed) {
				this.held = true;
			}
			this.pressed = true;
		}
	}

	@Override
	public boolean state() {
		return this.pressed && !this.held;
	}
}
