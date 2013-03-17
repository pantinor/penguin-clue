package org.antinori.game.penguin;



import org.antinori.game.penguin.components.Expires;
import org.antinori.game.penguin.components.Health;
import org.antinori.game.penguin.components.SpatialForm;
import org.antinori.game.penguin.components.Transform;
import org.antinori.game.penguin.components.Target;
import org.antinori.game.penguin.components.Weapon;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;


public class EntityFactory {
	public static Entity createEnemyMissile(World world, float x, float y, float tx, float ty) {
		Entity e = world.createEntity();
		
		e.addComponent(new Transform(x,y));
		e.addComponent(new SpatialForm("Missile"));
		e.addComponent(new Target(tx, ty));
		e.addComponent(new Expires(2000));

		world.getManager(GroupManager.class).add(e,"ENEMY_BULLETS");

		return e;
	}
	
	public static Entity createPlayerMissile(World world, float x, float y, float tx, float ty) {
				
		Entity e = world.createEntity();
		
		e.addComponent(new Transform(x,y));
		e.addComponent(new SpatialForm("Missile"));
		e.addComponent(new Target(tx, ty));
		e.addComponent(new Expires(2000));

		world.getManager(GroupManager.class).add(e,"PLAYER_BULLETS");

		return e;
	}
	
	public static Entity createSkeleton(World world, float x, float y) {
		Entity e = world.createEntity();
		
		e.addComponent(new Transform(x, y));
		e.addComponent(new SpatialForm("SKELETON"));
		e.addComponent(new Health(10));
		e.addComponent(new Weapon());
		
		world.getManager(GroupManager.class).add(e,"ENEMIES");
		
		return e;
	}
	
	public static Entity createBulletExplosion(World world, float x, float y) {
		Entity e = world.createEntity();
		
		e.addComponent(new Transform(x, y));
		e.addComponent(new SpatialForm("BulletExplosion"));
		e.addComponent(new Expires(1000));
		
		world.getManager(GroupManager.class).add(e, "EFFECTS");

		return e;
	}
	
	public static Entity createShipExplosion(World world, float x, float y) {
		Entity e = world.createEntity();
		
		e.addComponent(new Transform(x, y));
		e.addComponent(new SpatialForm("ShipExplosion"));
		e.addComponent(new Expires(1000));
		
		world.getManager(GroupManager.class).add(e, "EFFECTS");
		
		return e;
	}

	public static Entity createHealthPowerUp(World world, float x, float y, float tx, float ty) {
		Entity e = world.createEntity();
		
		e.addComponent(new Transform(x, y));
		e.addComponent(new Target(tx, ty));
		e.addComponent(new SpatialForm("HealthPowerUp"));
		
		world.getManager(GroupManager.class).add(e,"HEALTH_POWERUPS");
		
		return e;
	}

}
