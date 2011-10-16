package com.tacalpha;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputHandler implements KeyListener, FocusListener {
	public boolean[] keys = new boolean[65535];

	@Override
	public void keyPressed(KeyEvent event) {
		int code = event.getKeyCode();
		if (code > 0 && code < this.keys.length) {
			this.keys[code] = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent event) {
		int code = event.getKeyCode();
		if (code > 0 && code < this.keys.length) {
			this.keys[code] = false;
		}

	}

	@Override
	public void keyTyped(KeyEvent arg0) {

	}

	@Override
	public void focusGained(FocusEvent e) {

	}

	@Override
	public void focusLost(FocusEvent e) {
		for (int i = 0; i < this.keys.length; i++) {
			this.keys[i] = false;
		}
	}
}
