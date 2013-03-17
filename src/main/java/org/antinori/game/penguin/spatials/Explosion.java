package org.antinori.game.penguin.spatials;


import org.antinori.game.penguin.LevelMap;
import org.antinori.game.penguin.PenguinGame;
import org.antinori.game.penguin.components.Expires;
import org.antinori.game.penguin.components.Transform;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.World;

public class Explosion extends Spatial {
	private Transform transform;
	private Expires expires;
	private int initialLifeTime;
	private Color color;
	private int radius;

	public Explosion(World world, Entity owner, PenguinGame game, GameContainer container, LevelMap map, int radius) {
		super(world, owner, game, container, map);
		this.radius = radius; 
	}

	@Override
	public void initialize() {
		ComponentMapper<Transform> transformMapper = world.getMapper(Transform.class);
		transform = transformMapper.get(owner);
		
		ComponentMapper<Expires> expiresMapper = world.getMapper(Expires.class);
		expires = expiresMapper.get(owner);
		initialLifeTime = expires.getLifeTime();
		
		color = new Color(Color.yellow);
	}

	@Override
	public void render(Graphics g) {
		
		color.a = (float)expires.getLifeTime()/(float)initialLifeTime;
		
		g.setColor(color);
		g.setAntiAlias(true);
		g.fillOval(transform.getX() - radius, transform.getY() - radius, radius*2, radius*2);
	}

}
