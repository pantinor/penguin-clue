package org.antinori.game.penguin.systems;


import org.antinori.game.penguin.ActorType;
import org.antinori.game.penguin.ActorTypeStore;
import org.antinori.game.penguin.LevelMap;
import org.antinori.game.penguin.PenguinGame;
import org.antinori.game.penguin.animations.AnimationStore;
import org.antinori.game.penguin.components.SpatialForm;
import org.antinori.game.penguin.components.Transform;
import org.antinori.game.penguin.spatials.Actor;
import org.antinori.game.penguin.spatials.Explosion;
import org.antinori.game.penguin.spatials.HealthPowerUp;
import org.antinori.game.penguin.spatials.Missile;
import org.antinori.game.penguin.spatials.Monster;
import org.antinori.game.penguin.spatials.Spatial;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.Bag;


public class RenderSystem extends EntityProcessingSystem {
	private Graphics graphics;
	private Bag<Spatial> spatials;
	private ComponentMapper<SpatialForm> spatialFormMapper;
	private ComponentMapper<Transform> transformMapper;
	private GameContainer container;
	private PenguinGame game;
	private LevelMap map;

	public RenderSystem(GameContainer container, PenguinGame game, LevelMap map) {
		super(Aspect.getAspectFor(Transform.class, SpatialForm.class));
		this.container = container;
		this.graphics = container.getGraphics();
		this.game = game;
		this.map = map;
		spatials = new Bag<Spatial>();
	}

	@Override
	public void initialize() {
		spatialFormMapper = world.getMapper(SpatialForm.class);
		transformMapper = world.getMapper(Transform.class);
	}

	@Override
	protected void process(Entity e) {
		Spatial spatial = spatials.get(e.getId());
		Transform transform = transformMapper.get(e);

		if (transform.getX() >= 0 && transform.getY() >= 0 && transform.getX() < container.getWidth() && transform.getY() < container.getHeight() && spatial != null) {
			spatial.render(graphics);
		}
	}
	
	@Override
	protected void end() {

	}

	
	protected void inserted(Entity e) {
		Spatial spatial = createSpatial(e);
		if (spatial != null) {
			spatial.initialize();
			spatials.set(e.getId(), spatial);
			
			if (spatial instanceof Actor) 
				game.addActor((Actor)spatial);
		}
	}
	
	@Override
	protected void removed(Entity e) {
		spatials.set(e.getId(), null);
	}

	private Spatial createSpatial(Entity e) {
		SpatialForm spatialForm = spatialFormMapper.get(e);
		String spatialFormFile = spatialForm.getSpatialFormFile();
		
		//System.out.println("createSpatial "+spatialFormFile );

		if ("MAGE".equalsIgnoreCase(spatialFormFile)) {
			Actor actor = new Actor(world, e, game, container, map, ActorTypeStore.BLUE_KNIGHT, AnimationStore.SOUTH);
			Transform transform = e.getComponent(Transform.class);
			transform.setActor(actor);
			return actor;
		} else if ("Missile".equalsIgnoreCase(spatialFormFile)) {
			Missile missile = new Missile(world, e, game, container, map);
			Transform transform = e.getComponent(Transform.class);
			transform.setMissile(missile);
			return missile;
		} else if ("SKELETON".equalsIgnoreCase(spatialFormFile)) {
			Monster skeleton = new Monster(world, e, game, container, map, ActorTypeStore.MUMMY, AnimationStore.SOUTH);
			Transform transform = e.getComponent(Transform.class);
			transform.setMonster(skeleton);
			return skeleton;
		} else if ("BulletExplosion".equalsIgnoreCase(spatialFormFile)) {
			return new Explosion(world, e, game, container, map, 10);
		} else if ("ShipExplosion".equalsIgnoreCase(spatialFormFile)) {
			return new Explosion(world, e, game, container, map, 30);
		} else if ("HealthPowerUp".equalsIgnoreCase(spatialFormFile)) {
			return new HealthPowerUp(world, e, game, container, map);
		}

		return null;
	}

}
