package com.tacalpha;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.tacalpha.graphics.ScreenRenderer;

public class GameRunner extends Canvas implements Runnable {
	private static final long serialVersionUID = -7093576847463991329L;

	public static final int WIDTH = 1024;
	public static final int HEIGHT = 768;

	private Game game;

	private boolean running;
	private Thread thread;

	private ScreenRenderer screen;
	private BufferedImage image;
	private int[] pixels;
	private InputHandler inputHandler;

	public GameRunner() {
		Dimension size = new Dimension(GameRunner.WIDTH, GameRunner.HEIGHT);
		this.setSize(size);
		this.setPreferredSize(size);
		this.setMinimumSize(size);
		this.setMaximumSize(size);

		this.game = new Game();
		this.screen = new ScreenRenderer(GameRunner.WIDTH, GameRunner.HEIGHT);

		this.image = new BufferedImage(GameRunner.WIDTH, GameRunner.HEIGHT, BufferedImage.TYPE_INT_RGB);
		this.pixels = ((DataBufferInt) this.image.getRaster().getDataBuffer()).getData();

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
			this.createBufferStrategy(2);
			return;
		}

		this.screen.render(this.game, this.hasFocus());

		for (int i = 0; i < GameRunner.WIDTH * GameRunner.HEIGHT; i++) {
			this.pixels[i] = this.screen.pixels[i];
		}

		Graphics graphics = bufferStrategy.getDrawGraphics();
		// TODO: Why is this visible on the right and bottom of the screen?
		graphics.fillRect(0, 0, this.getWidth() - 10, this.getHeight());
		graphics.drawImage(this.image, 0, 0, GameRunner.WIDTH, GameRunner.HEIGHT, null);
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

		JFrame frame = new JFrame("Tactics Alpha");
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(runner);

		frame.setContentPane(panel);
		frame.setSize(GameRunner.WIDTH, GameRunner.HEIGHT);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		runner.start();
	}

}
