package org.antinori.game.penguin;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class Images {
	public static SpriteSheet TILES_SHEET;
	public static SpriteSheet FACES;
	public static Image MAP;
	
	public static void init() throws SlickException {
		TILES_SHEET = new SpriteSheet("./tiles48.png", PenguinGame.TILE_SIZE, PenguinGame.TILE_SIZE);
		FACES = new SpriteSheet("./faces.png", 46, 36);
		MAP = new Image("./map.png");
	}
}
