package org.antinori.game.penguin.systems;

import org.antinori.game.penguin.EntityFactory;
import org.antinori.game.penguin.LevelMap;
import org.antinori.game.penguin.components.Transform;
import org.antinori.game.penguin.components.Weapon;
import org.newdawn.slick.GameContainer;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.IntervalEntitySystem;
import com.artemis.utils.ImmutableBag;

public class EnemySpawnSystem extends IntervalEntitySystem {

	private ComponentMapper<Weapon> weaponMapper;
	private ComponentMapper<Transform> transformMapper;
	private GameContainer container;
	private LevelMap map;

	public EnemySpawnSystem(int interval, GameContainer container, LevelMap map) {
		super(Aspect.getEmpty(), interval);
		this.container = container;
		this.map = map;
	}

	@Override
	public void initialize() {
		weaponMapper = world.getMapper(Weapon.class);
		transformMapper = world.getMapper(Transform.class);
	}
	
	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
		int[] coords = map.getMap().generateRandomRoomLocation();
		float x = coords[0];
		float y = coords[1];
				
		EntityFactory.createSkeleton(world, x, y).addToWorld();
	}
	
}
