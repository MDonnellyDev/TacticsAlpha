package com.tacalpha;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.tacalpha.graphics.ScreenRenderer;
import com.tacalpha.input.InputHandler;

public class GameRunner extends Canvas implements Runnable {
	private static final long serialVersionUID = -7093576847463991329L;

	private static final int WIDTH = 1024;
	private static final int HEIGHT = 768;
	private static JFrame FRAME;

	private Game game;

	private boolean running;
	private Thread thread;

	private ScreenRenderer screen;
	private InputHandler inputHandler;

	public GameRunner() {
		Dimension size = new Dimension(GameRunner.WIDTH, GameRunner.HEIGHT);
		this.setSize(size);

		this.game = new Game();
		this.screen = new ScreenRenderer();

		this.inputHandler = new InputHandler();

		this.addKeyListener(this.inputHandler);
		this.addFocusListener(this.inputHandler);
	}

	@Override
	public void run() {
		double unprocessedSeconds = 0;
		long lastTime = System.nanoTime();
		double secondsPerTick = 1.0 / 60.0;

		this.requestFocus();

		while (this.running) {
			long now = System.nanoTime();
			long passedTime = now - lastTime;
			lastTime = now;
			if (passedTime < 0) {
				passedTime = 0;
			}
			if (passedTime > 100000000) {
				passedTime = 100000000;
			}

			unprocessedSeconds += passedTime / 1000000000.0;

			boolean ticked = false;
			while (unprocessedSeconds > secondsPerTick) {
				this.tick();
				unprocessedSeconds -= secondsPerTick;
				ticked = true;
			}

			if (ticked) {
				this.render();
			} else {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void tick() {
		if (this.hasFocus()) {
			this.game.update(this.inputHandler.keys);
		}
	}

	private void render() {
		BufferStrategy bufferStrategy = this.getBufferStrategy();
		if (bufferStrategy == null) {
			this.createBufferStrategy(3);
			return;
		}

		this.screen.render(this.game, this.hasFocus(), this.getWidth(), this.getHeight());

		Graphics graphics = bufferStrategy.getDrawGraphics();
		graphics.fillRect(0, 0, this.getWidth(), this.getHeight());
		graphics.drawImage(this.screen.getImage(), 0, 0, this.getWidth(), this.getHeight(), null);
		graphics.dispose();
		bufferStrategy.show();
	}

	public synchronized void start() {
		if (this.running) {
			return;
		}
		this.running = true;
		this.thread = new Thread(this);
		this.thread.start();
	}

	public synchronized void stop() {
		if (!this.running) {
			return;
		}
		this.running = false;
		try {
			this.thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		GameRunner runner = new GameRunner();

		GameRunner.FRAME = new JFrame("Tactics Alpha");
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(runner);

		GameRunner.FRAME.setContentPane(panel);
		GameRunner.FRAME.setSize(GameRunner.WIDTH, GameRunner.HEIGHT);
		GameRunner.FRAME.setMinimumSize(new Dimension(GameRunner.WIDTH, GameRunner.HEIGHT));
		GameRunner.FRAME.setLocationRelativeTo(null);
		GameRunner.FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GameRunner.FRAME.setVisible(true);

		runner.start();
	}

}
