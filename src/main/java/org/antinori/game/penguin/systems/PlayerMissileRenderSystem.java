package org.antinori.game.penguin.systems;

import org.antinori.game.penguin.components.SpatialForm;
import org.antinori.game.penguin.components.Transform;
import org.antinori.game.penguin.components.Target;
import org.antinori.game.penguin.spatials.Missile;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.managers.GroupManager;
import com.artemis.utils.ImmutableBag;



public class PlayerMissileRenderSystem extends EntitySystem {
	
	private GameContainer container;
	private Graphics g;
	private ComponentMapper<Transform> transformMapper;

	public PlayerMissileRenderSystem(GameContainer container) {
		super(Aspect.getAspectFor(Transform.class));
		this.container = container;
		this.g = container.getGraphics();
	}

	@Override
	public void initialize() {
		transformMapper = world.getMapper(Transform.class);
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
		
		ImmutableBag<Entity> mbag = world.getManager(GroupManager.class).getEntities("PLAYER_BULLETS");

		for(int a = 0; mbag.size() > a; a++) {
			
			Entity em = mbag.get(a);
			
			Transform transform = transformMapper.get(em);
			
			Missile missile = transform.getMissile();
			if (missile == null) continue;
			
			if (!missile.active) missile.fire(Color.blue);
			missile.update(world.getDelta());			

		}
	}

	@Override
	protected boolean checkProcessing() {
		return true;
	}
	
}
