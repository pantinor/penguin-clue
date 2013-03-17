package org.antinori.game.penguin.spatials;

import org.antinori.game.penguin.LevelMap;
import org.antinori.game.penguin.PenguinGame;
import org.antinori.game.penguin.components.Target;
import org.antinori.game.penguin.components.Transform;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.World;

public class HealthPowerUp extends Spatial {

	private Transform transform;
	private Target target;


	public HealthPowerUp(World world, Entity owner, PenguinGame game, GameContainer container, LevelMap map) {
		super(world, owner, game, container, map);
	}

	@Override
	public void initialize() {
		ComponentMapper<Transform> transformMapper = world.getMapper(Transform.class);
		transform = transformMapper.get(owner);
		
		ComponentMapper<Target> targetMapper = world.getMapper(Target.class);
		target = targetMapper.get(owner);
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(target.getX()-10, target.getY()-10, 20, 20);
		
		g.setColor(Color.red);
		g.fillRect(target.getX()-8, target.getY()-2, 16, 4);
		g.fillRect(target.getX()-2, target.getY()-8, 4, 16);
	}

}
