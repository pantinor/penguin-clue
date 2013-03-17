package org.antinori.game.penguin.spatials;

import org.antinori.game.penguin.LevelMap;
import org.antinori.game.penguin.PenguinGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import com.artemis.Entity;
import com.artemis.World;

public abstract class Spatial {
	
	protected World world;
	protected Entity owner;
	protected GameContainer container;
	protected LevelMap map;
	protected PenguinGame game;


	public Spatial(World world, Entity owner, PenguinGame game, GameContainer container, LevelMap map) {
		this.world = world;
		this.owner = owner;
		this.container = container;
		this.map = map;
		this.game = game;
	}

	public abstract void initialize();

	public abstract void render(Graphics g);
	
	public LevelMap getMap() {
		return map;
	}
	
	
}
