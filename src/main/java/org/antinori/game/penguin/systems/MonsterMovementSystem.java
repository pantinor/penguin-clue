package org.antinori.game.penguin.systems;


import org.antinori.game.penguin.LevelMap;
import org.antinori.game.penguin.Map;
import org.antinori.game.penguin.components.Transform;
import org.antinori.game.penguin.components.Target;
import org.antinori.game.penguin.spatials.Monster;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.util.pathfinding.navmesh.NavMesh;
import org.newdawn.slick.util.pathfinding.navmesh.NavPath;
import org.newdawn.slick.util.pathfinding.navmesh.Space;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;


public class MonsterMovementSystem extends EntityProcessingSystem {
	private GameContainer container;
	private Graphics g;
	private ComponentMapper<Transform> transformMapper;
	private NavMesh mesh;
	



	public MonsterMovementSystem(GameContainer container, NavMesh mesh) {
		super(Aspect.getAspectFor(Transform.class));
		this.container = container;
		this.mesh = mesh;
	}

	@Override
	public void initialize() {
		transformMapper = world.getMapper(Transform.class);
	}

	@Override
	protected void process(Entity e) {
		
		Transform transform = transformMapper.get(e);
		Monster monster = transform.getMonster();
		if (monster == null) return;
		
		monster.checkUpdate();
		
		NavPath currentPath = monster.getCurrentPath();
		if (currentPath != null) return;
		
		int[] coords = monster.getMap().getMap().generateRandomRoomLocation();
		float targetX = coords[0];
		float targetY = coords[1];
		float sourceX = transform.getX();
		float sourceY = transform.getY();
		
		currentPath = mesh.findPath(sourceX, sourceY, targetX, targetY, false);
		
		if (currentPath == null) {
			targetX += 1f;
			currentPath = mesh.findPath(sourceX, sourceY, targetX, targetY, false);
		}
		if (currentPath == null) {
			targetX -= 2f;
			currentPath = mesh.findPath(sourceX, sourceY, targetX, targetY, false);
		}
		if (currentPath == null) {
			targetY += 1f;
			currentPath = mesh.findPath(sourceX, sourceY, targetX, targetY, false);
		}
		if (currentPath == null) {
			targetY -= 2f;
			currentPath = mesh.findPath(sourceX, sourceY, targetX, targetY, false);
		}
		
		Space source = mesh.findSpace(sourceX,sourceY);
		Space target = mesh.findSpace(targetX,targetY);
		
		//System.out.println("monster currentPath="+currentPath+ " sp="+source+" ts="+target);
		
		monster.setCurrentPath(currentPath, targetX, targetY);
		
	}

}
