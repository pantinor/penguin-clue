package org.antinori.game.utils;

import java.util.ArrayList;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;

public class BulletTest extends BasicGame {
	private ArrayList<Bullet> bulletList = new ArrayList<Bullet>();
	private Rectangle playerLocation;

	public static void main(String[] args) throws SlickException {
		AppGameContainer app = new AppGameContainer(new BulletTest(), 800, 600, false);

		app.setTargetFrameRate(60);
		app.setMaximumLogicUpdateInterval(1000 / 60);
		app.setMinimumLogicUpdateInterval(1000 / 60);
		app.start();
	}

	public BulletTest() {
		super("Bullet Test");

	}

	public void init(GameContainer container) throws SlickException {
		// Draw the "player"/"gun"/whatever at the bottom of the screen.
		// This is where the bullet comes out of it
		playerLocation = new Rectangle(400, 500, 32, 32);
	}

	public void render(GameContainer container, Graphics g) throws SlickException {
		// Draw the player
		g.setColor(Color.green);
		g.fill(playerLocation);

		// Draw the bullets
		g.setColor(Color.red);
		for (int i = 0; i < bulletList.size(); i++) {
			Bullet bullet = bulletList.get(i);

			g.fillRect(bullet.location.getX(), bullet.location.getY(), 5, 5);
		}
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		// Update the bullet's position.
		for (int i = 0; i < bulletList.size(); i++) {
			Bullet bullet = bulletList.get(i);

			bullet.move();

			// NOTE: Will need to determine if this hit something or went off
			// the screen. Or otherwise, the list will get filled with invalid
			// bullets.
			// You'll have to add that yourself.
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.newdawn.slick.BasicGame#mousePressed(int, int, int)
	 */
	@Override
	public void mousePressed(int button, int x, int y) {
		addNewBullet(x, y);
	}

	/**
	 * Adds a new bullet, flying towards where ever the player clicked.
	 * 
	 * @param x
	 * @param y
	 */
	private void addNewBullet(int x, int y) {
		bulletList.add(new Bullet((int) playerLocation.getCenterX(), (int) playerLocation.getCenterY(), x, y));
	}

	class Bullet {
		int startX = 0;
		int startY = 0;
		int destX = 0;
		int destY = 0;
		Point location = new Point(0, 0);
		float speed = 30; // how fast this moves.
		float deg;
		float dx;
		float dy;

		public Bullet(int startX, int startY, int destX, int destY) {
			this.startX = startX;
			this.startY = startY;
			location.setLocation(startX, startY);
			this.destX = destX;
			this.destY = destY;
			recalculateVector(destX, destY);

		}

		/**
		 * Calculates a new vector based on the input destination X and Y.
		 * 
		 * @param destX
		 * @param destY
		 */
		public void recalculateVector(int destX, int destY) {
			float rad = (float) (Math.atan2(destX - startX, startY - destY));

			// Can set different speeds here, if you wanted.
			speed = 5;

			this.dx = (float) Math.sin(rad) * speed;
			this.dy = -(float) Math.cos(rad) * speed;
		}

		/**
		 * Recalculates the vector for this bullet based on the current
		 * destination.
		 */
		public void recalculateVector() {
			recalculateVector(destX, destY);
		}

		/**
		 * Moves this bullet.
		 */
		public void move() {
			float x = location.getX();
			float y = location.getY();

			x += dx;
			y += dy;

			location.setLocation(x, y);
		}

	}
}