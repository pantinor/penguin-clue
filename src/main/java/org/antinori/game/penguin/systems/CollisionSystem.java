package org.antinori.game.penguin.systems;


import org.antinori.game.penguin.EntityFactory;
import org.antinori.game.penguin.PenguinGame;
import org.antinori.game.penguin.components.Health;
import org.antinori.game.penguin.components.Transform;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.Manager;
import com.artemis.managers.GroupManager;
import com.artemis.utils.Bag;
import com.artemis.utils.ImmutableBag;

public class CollisionSystem extends EntitySystem {
	private ComponentMapper<Transform> transformMapper;
	private ComponentMapper<Health> healthMapper;
	private ImmutableBag<Entity> bullets;
	private ImmutableBag<Entity> ships;
	private Bag<CollisionGroup> collisionGroups;

	public CollisionSystem() {
		super(Aspect.getAspectFor(Transform.class));
	}

	@Override
	public void initialize() {
		transformMapper = world.getMapper(Transform.class);
		healthMapper = world.getMapper(Health.class);
		
		collisionGroups = new Bag<CollisionGroup>();
		
		collisionGroups.add(new CollisionGroup("PLAYER_BULLETS", "ENEMIES", new CollisionHandler() {

			public void handleCollision(Entity bullet, Entity enemy) {
				Transform te = transformMapper.get(enemy);
				Transform tb = transformMapper.get(bullet);
				EntityFactory.createBulletExplosion(world, tb.getX()*PenguinGame.TILE_SIZE, tb.getY()*PenguinGame.TILE_SIZE).addToWorld();
				world.deleteEntity(bullet);
				
				Health health = healthMapper.get(enemy);
				health.addDamage(5);
				
				if(!health.isAlive()) {
					Transform ts = transformMapper.get(enemy);
					EntityFactory.createShipExplosion(world, ts.getX()*PenguinGame.TILE_SIZE, ts.getY()*PenguinGame.TILE_SIZE).addToWorld();
					world.deleteEntity(enemy);
				}
			}
		}));

		collisionGroups.add(new CollisionGroup("ENEMY_BULLETS", "PLAYERS", new CollisionHandler() {
			
			public void handleCollision(Entity bullet, Entity player) {
				Transform tp = transformMapper.get(player);
				Transform tb = transformMapper.get(bullet);
				EntityFactory.createBulletExplosion(world, tb.getX()*PenguinGame.TILE_SIZE, tb.getY()*PenguinGame.TILE_SIZE).addToWorld();
				world.deleteEntity(bullet);
				
				Health health = healthMapper.get(player);
				health.addDamage(1);
				
				if(!health.isAlive()) {
					Transform ts = transformMapper.get(player);
					EntityFactory.createShipExplosion(world, ts.getX()*PenguinGame.TILE_SIZE, ts.getY()*PenguinGame.TILE_SIZE).addToWorld();
					world.deleteEntity(player);
				}
			}
		}));
		
		collisionGroups.add(new CollisionGroup("ENEMIES", "PLAYERS", new CollisionHandler() {
			
			public void handleCollision(Entity enemy, Entity player) {
				Transform tp = transformMapper.get(player);
				Transform tb = transformMapper.get(enemy);
				
				Health health = healthMapper.get(player);
				health.addDamage(2);
				
				if(!health.isAlive()) {
					Transform ts = transformMapper.get(player);
					EntityFactory.createShipExplosion(world, ts.getX()*PenguinGame.TILE_SIZE, ts.getY()*PenguinGame.TILE_SIZE).addToWorld();
					world.deleteEntity(player);
				}
				
				Health healthe = healthMapper.get(enemy);
				healthe.addDamage(5);
				
				if(!healthe.isAlive()) {
					Transform ts = transformMapper.get(enemy);
					EntityFactory.createShipExplosion(world, ts.getX()*PenguinGame.TILE_SIZE, ts.getY()*PenguinGame.TILE_SIZE).addToWorld();
					world.deleteEntity(enemy);
				}
			}
		}));
		
		collisionGroups.add(new CollisionGroup("HEALTH_POWERUPS", "PLAYERS", new CollisionHandler() {
			
			public void handleCollision(Entity powerup, Entity player) {
				
				Transform th = transformMapper.get(powerup);
				Transform tp = transformMapper.get(player);
				
				Health health = healthMapper.get(player);
				health.addHealth(5);
				powerup.deleteFromWorld();
			}
		}));
	}
	
	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
		
		for(int i = 0; collisionGroups.size() > i; i++) {
			collisionGroups.get(i).checkForCollisions();
		}
	}

	private boolean collisionExists(Entity e1, Entity e2) {
		Transform t1 = transformMapper.get(e1);
		Transform t2 = transformMapper.get(e2);
		float distance = t1.getDistanceTo(t2);
		return distance < 1;
	}

	@Override
	protected boolean checkProcessing() {
		return true;
	}
	
	
	private class CollisionGroup {
		private ImmutableBag<Entity> groupEntitiesA;
		private ImmutableBag<Entity> groupEntitiesB;
		private CollisionHandler handler;

		public CollisionGroup(String group1, String group2, CollisionHandler handler) {
			groupEntitiesA = world.getManager(GroupManager.class).getEntities(group1);
			groupEntitiesB = world.getManager(GroupManager.class).getEntities(group2);
			this.handler = handler;
		}

		public void checkForCollisions() {
			for(int a = 0; groupEntitiesA.size() > a; a++) {
				for(int b = 0; groupEntitiesB.size() > b; b++) {
					Entity entityA = groupEntitiesA.get(a);
					Entity entityB = groupEntitiesB.get(b);
					if(collisionExists(entityA, entityB)) {
						handler.handleCollision(entityA, entityB);
					}
				}
			}
			
		}
	}
	
	private interface CollisionHandler {
		void handleCollision(Entity a, Entity b);
	}

}
