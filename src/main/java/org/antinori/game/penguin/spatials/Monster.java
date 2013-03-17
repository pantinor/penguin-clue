package org.antinori.game.penguin.spatials;

import org.antinori.game.penguin.ActorType;
import org.antinori.game.penguin.DungeonElement;
import org.antinori.game.penguin.LevelMap;
import org.antinori.game.penguin.PenguinGame;
import org.antinori.game.penguin.animations.AnimationStore;
import org.antinori.game.penguin.components.Transform;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.util.pathfinding.Mover;
import org.newdawn.slick.util.pathfinding.navmesh.NavPath;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.World;

public class Monster extends Spatial implements Mover, DungeonElement {
		
	private ActorType type;

	private int direction;	
	private AnimationStore.Type action = AnimationStore.Type.PAUSED;
	
	private int tilNextFrame = PenguinGame.ANIM_INTERVAL;
	private int tilNextUpdate = PenguinGame.UPDATE_INTERVAL;
	
	private NavPath currentPath;
	private int pathStep;
	private int moveIterations = 0;
	private float dx;
	private float dy;
	private float finalx;
	private float finaly;
	private float tx;
	private float ty;
	
	private Transform transform;
	
	public Monster(World world, Entity owner, PenguinGame game, GameContainer container, LevelMap map, ActorType type, int direction) {
		super(world, owner, game, container, map);
		this.type = type;
		this.direction = direction;
	}
	
	public ActorType getType() {
		return type;
	}
	
	public void initialize() {
		ComponentMapper<Transform> transformMapper = world.getMapper(Transform.class);
		transform = transformMapper.get(owner);
	}
	
	public NavPath getCurrentPath() {
		return currentPath;
	}
	
	public void setCurrentPath(NavPath currentPath, float finalx, float finaly) {
		this.currentPath = currentPath;
		pathStep = 0;
		this.finalx = finalx;
		this.finaly = finaly;
		nextStep();
	} 

	private boolean considerNextStep() {
		if (pathStep < currentPath.length()-1) {
			tx = currentPath.getX(pathStep+1);
			ty = currentPath.getY(pathStep+1);
			
			if (map.hasLos(transform.getX(),transform.getY(),tx,ty,0.25f)) {
				nextStep();
				return true;
			}
		}
		
		return false;
	}
	
	private void nextStep() {
		if (currentPath == null) {
			return;
		}
		
		pathStep++;
		if (pathStep >= currentPath.length()) {
			currentPath = null;
			dx = 0;
			dy = 0;
			return;
		}
		
		tx = currentPath.getX(pathStep);
		ty = currentPath.getY(pathStep);
		if (pathStep == currentPath.length() - 1) {
			tx = finalx;
			ty = finaly;
		}
		
		dx = (tx - transform.getX());
		dy = (ty - transform.getY());
		
		moveIterations = (int) (Math.sqrt((dx*dx)+(dy*dy)) / PenguinGame.SPEED);
		dx = dx / moveIterations;
		dy = dy / moveIterations;
		setDirection(dx,dy);
	}
	
	private void setDirection(float dx, float dy) {
		float ang = (float) Math.toDegrees(Math.atan2(dy, dx));
		if (ang < 0) {
			ang = 360 + ang;
		}
		ang = (ang + 90 + 22.5f) % 360;
		ang /= 45f;
		direction = (int) ang;
	}
	
	public void checkUpdate() {
		
		tilNextFrame -= world.getDelta();
		tilNextUpdate -= world.getDelta();

		boolean updateLogic = tilNextUpdate < 0;
		
		if (updateLogic) {
			update();
		}

		if (tilNextFrame < 0) {
			tilNextFrame += PenguinGame.ANIM_INTERVAL;
		}
		if (tilNextUpdate < 0) {
			tilNextUpdate += PenguinGame.UPDATE_INTERVAL;
		}
	}
	
	private void update() {
		if (currentPath != null) {
			if (!considerNextStep()) {
				moveIterations--;
				
				float x = transform.getX();
				transform.setX(x += dx);
				
				float y = transform.getY();
				transform.setY(y += dy);

				if (moveIterations <= 0) {
					nextStep();
				}
			}
			
			action = AnimationStore.Type.WALKING;
		} else {
			action = AnimationStore.Type.PAUSED;
		}
	}
	
	public void render(Graphics g) {
		AnimationStore ast = type.getAnimationStore(action);
		
		float imageOX = PenguinGame.TILE_SIZE/2;
		float imageOY = PenguinGame.TILE_SIZE/2;
		
		ast.render(direction, (transform.getX()-0.5f)*PenguinGame.TILE_SIZE+imageOX, (transform.getY()-0.5f)*PenguinGame.TILE_SIZE+imageOY);
	}

	public float getY() {
		return transform.getY();
	}
	
	public float getX() {
		return transform.getX();
	}

	public void setLocation(float x, float y) {
		currentPath = null;
		transform.setX(x);
		transform.setY(y);
	}

	public void addedToMap(LevelMap map) {
		
	}

	public void removedFromMap(LevelMap map) {
		
	}

	public float distance2(float x2, float y2) {
		float dx = x2 - transform.getX();
		float dy = y2 - transform.getY();
		return (dx*dx)+(dy*dy);
	}



}
