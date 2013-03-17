package org.antinori.game.penguin.systems;

import java.util.Random;

import org.antinori.game.penguin.EntityFactory;
import org.antinori.game.penguin.LevelMap;
import org.antinori.game.penguin.PenguinGame;
import org.newdawn.slick.GameContainer;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.systems.IntervalEntitySystem;
import com.artemis.utils.ImmutableBag;

public class HealthPowerUpSpawnSystem extends IntervalEntitySystem {

	private GameContainer container;
	private LevelMap map;

	public HealthPowerUpSpawnSystem(int interval, GameContainer container, LevelMap map) {
		super(Aspect.getEmpty(), interval);
		this.container = container;
		this.map = map;
	}
	
	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
		
		int[] coords = map.getMap().generateRandomRoomLocation();
		
		float targetX = coords[0]*PenguinGame.TILE_SIZE;
		float targetY = coords[1]*PenguinGame.TILE_SIZE;
		
		EntityFactory.createHealthPowerUp(world, coords[0], coords[1], targetX, targetY).addToWorld();
	}
	
}
