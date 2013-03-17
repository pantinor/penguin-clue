package org.antinori.game.penguin.systems;


import org.antinori.game.penguin.components.Expires;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;

public class ExpirationSystem extends EntityProcessingSystem {

	private ComponentMapper<Expires> expiresMapper;

	public ExpirationSystem() {
		super(Aspect.getAspectFor(Expires.class));
	}

	@Override
	public void initialize() {
		expiresMapper = world.getMapper(Expires.class);
	}

	@Override
	protected void process(Entity e) {
		Expires expires = expiresMapper.get(e);
		expires.reduceLifeTime(world.getDelta());

		if (expires.isExpired()) {
			world.deleteEntity(e);
		}

	}
}
