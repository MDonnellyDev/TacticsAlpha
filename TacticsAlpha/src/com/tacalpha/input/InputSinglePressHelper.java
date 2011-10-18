package com.tacalpha.input;

public class InputSinglePressHelper implements InputHelper {
	private boolean pressed;

	public InputSinglePressHelper() {

	}

	@Override
	public boolean state(boolean currentKeyState) {
		if (!currentKeyState) {
			this.pressed = false;
			return false;
		}
		if (this.pressed) {
			return false;
		}
		this.pressed = true;
		return true;
	}
}
