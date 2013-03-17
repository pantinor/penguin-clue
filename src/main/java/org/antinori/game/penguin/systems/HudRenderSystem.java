package org.antinori.game.penguin.systems;

import org.antinori.game.penguin.Images;
import org.antinori.game.penguin.components.Health;
import org.antinori.game.penguin.components.Transform;
import org.antinori.game.penguin.spatials.Actor;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.managers.TagManager;
import com.artemis.utils.ImmutableBag;

public class HudRenderSystem extends EntitySystem {
	private GameContainer container;
	private Graphics g;
	private ComponentMapper<Health> healthMapper;
	private ComponentMapper<Transform> transformMapper;


	public HudRenderSystem(GameContainer container) {
		super(Aspect.getEmpty());
		this.container = container;
		this.g = container.getGraphics();
	}

	@Override
	public void initialize() {
		healthMapper = world.getMapper(Health.class);
		transformMapper = world.getMapper(Transform.class);
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
			
		Entity entity = world.getManager(TagManager.class).getEntity("MAGE");
		if(entity != null && entity.isEnabled()) {
			
			Health health = healthMapper.get(entity);
			Transform transform = transformMapper.get(entity);
			Actor actor = transform.getActor();
			
			if (actor == null) return;
			
			int playerIndex = 0;
			
			Color col = Color.white;
			//Images.FACES.getSubImage(actor.getType().getIndex(), 0).draw(container.getWidth() - 50, 5 + (playerIndex * 60), col);
			
			g.setColor(Color.green.darker());
			g.fillRect(container.getWidth() - 50, 43 + (playerIndex * 60), 44, 4);
						
			g.setColor(Color.green);
			int h = Math.round((health.getHealth()/health.getMaximumHealth()) * 44);
			g.fillRect(container.getWidth() - 50, 43 + (playerIndex * 60), h, 4);
		}
	}

	@Override
	protected boolean checkProcessing() {
		return true;
	}
	
}
