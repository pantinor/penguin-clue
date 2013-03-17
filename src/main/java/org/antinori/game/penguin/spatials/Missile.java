package org.antinori.game.penguin.spatials;


import org.antinori.game.penguin.LevelMap;
import org.antinori.game.penguin.PenguinGame;
import org.antinori.game.penguin.components.Expires;
import org.antinori.game.penguin.components.Target;
import org.antinori.game.penguin.components.Transform;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.World;


public class Missile extends Spatial {

	private final float SPEED = 150.0f;
	
	private Color color = Color.red;
		
	private float dx;
	private float dy;
	
	public boolean active = false;
	private float destx;
	private float desty;
		
	private Transform transform;
	private Expires expires;
	private Target target;
	
	int WIDTH = 6;
	int HEIGHT = 6;

	public Missile(World world, Entity owner, PenguinGame game, GameContainer container, LevelMap map) {
		super(world, owner, game, container, map);
	}

	@Override
	public void initialize() {
		ComponentMapper<Transform> transformMapper = world.getMapper(Transform.class);
		transform = transformMapper.get(owner);
		
		ComponentMapper<Expires> expiresMapper = world.getMapper(Expires.class);
		expires = expiresMapper.get(owner);
		
		ComponentMapper<Target> targetMapper = world.getMapper(Target.class);
		target = targetMapper.get(owner);
	}

	@Override
	public void render(Graphics g) {
		
		g.setColor(color);
		g.setAntiAlias(true);
		
		//System.out.println("render line x="+transform.getX()+" y="+transform.getY() +" dx="+destX+" dy="+destY);
		//Line line = new Line(transform.getX(), transform.getY(), destX, destY);
		//g.draw(line);
		
		float x1 = target.getX() - WIDTH/2;
		float y1 = target.getY() - HEIGHT/2;
		
		if (map.blocksSight((int)transform.getX(),(int)transform.getY())) {
			expires.setLifeTime(0);
			return;
		}
		
		g.fillRect(x1,y1, WIDTH, HEIGHT);		
		
	}
	
	
	public void fire(Color color) {
		
		this.active = true;
		this.color = color;
		
		this.destx = target.getX();
		this.desty = target.getY();
		
		target.setX(transform.getX());
		target.setY(transform.getY());

	}
	

	public void update (float delta) {
				
		float distanceX = destx - target.getX();
		float distanceY = target.getY() - desty;

		//To get the degree angle between those two vectors:	
		//http://slick.javaunlimited.net/viewtopic.php?f=3&t=5721&p=31934#p31934
		float directionInRadians = (float)(Math.atan2(distanceX, distanceY));
		this.dx = (float) Math.sin(directionInRadians) * SPEED;
		this.dy = -(float) Math.cos(directionInRadians) * SPEED;

		target.addX(dx*delta/1000);
		target.addY(dy*delta/1000);
		
		transform.setX(target.getX()/PenguinGame.TILE_SIZE);
		transform.setY(target.getY()/PenguinGame.TILE_SIZE);
				
	}
	

}
