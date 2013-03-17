package org.antinori.game.penguin.systems;



import org.antinori.game.penguin.EntityFactory;
import org.antinori.game.penguin.components.Transform;
import org.antinori.game.penguin.components.Weapon;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;


public class EnemyShooterSystem extends EntityProcessingSystem {

	private ComponentMapper<Weapon> weaponMapper;
	private long now;
	private ComponentMapper<Transform> transformMapper;

	public EnemyShooterSystem() {
		super(Aspect.getAspectFor(Transform.class, Weapon.class));
	}

	@Override
	public void initialize() {
		weaponMapper = world.getMapper(Weapon.class);
		transformMapper = world.getMapper(Transform.class);
	}

	@Override
	protected void begin() {
		now = System.currentTimeMillis();
	}

	@Override
	protected void process(Entity e) {
		process(e, weaponMapper.get(e), transformMapper.get(e));
	}

	private void process(Entity e, Weapon weapon, Transform transform) {
		if (weapon.getShotAt() + 2000 < now) {
			//EntityFactory.createEnemyMissile(world, transform.getX(), transform.getY() + 20).addToWorld();
			//weapon.setShotAt(now);
		}
	}
}
