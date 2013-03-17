package org.antinori.game.penguin.systems;


import org.antinori.game.penguin.EntityFactory;
import org.antinori.game.penguin.PenguinGame;
import org.antinori.game.penguin.components.Transform;
import org.antinori.game.penguin.spatials.Actor;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.MouseListener;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.managers.TagManager;
import com.artemis.systems.IntervalEntitySystem;
import com.artemis.utils.ImmutableBag;

public class PlayerShootingControlSystem extends IntervalEntitySystem implements KeyListener, MouseListener {
	
	private PenguinGame game;
	private GameContainer container;
	private boolean shoot;
	private ComponentMapper<Transform> transformMapper;

	private static final long THROTTLE = 500;
	private long lastShotTime = System.currentTimeMillis();
	
	private float tx;
	private float ty;

	public PlayerShootingControlSystem(GameContainer container, PenguinGame game) {
		super(Aspect.getEmpty(), 50);
		this.container = container;
		this.game = game;
	}

	@Override
	public void initialize() {
		transformMapper = world.getMapper(Transform.class);
		container.getInput().addKeyListener(this);
		container.getInput().addMouseListener(this);
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
		Entity entity = world.getManager(TagManager.class).getEntity("MAGE");
		if (entity != null && entity.isEnabled()) {
			if (shoot) {
				
				long now = System.currentTimeMillis();
				if (now-lastShotTime<THROTTLE) return;
				lastShotTime = now;
				
				Transform transform = transformMapper.get(entity);
				Actor actor = transform.getActor();
				if (actor == null) return;
				
				float sx = PenguinGame.SELECTED_PLAYER_POSITION_X - game.getOffsetx();
				float sy = PenguinGame.SELECTED_PLAYER_POSITION_Y - game.getOffsety();
				float otx = tx - game.getOffsetx();
				float oty = ty - game.getOffsety();
				
				EntityFactory.createPlayerMissile(world, sx, sy, otx, oty).addToWorld();
			}
		}
	}

	
	public void keyPressed(int key, char c) {
		if (key == Input.KEY_SPACE) {
			shoot = true;
		}
	}

	
	public void keyReleased(int key, char c) {
		if (key == Input.KEY_SPACE) {
			shoot = false;
		}
	}

	
	public void inputEnded() {
	}

	
	public void inputStarted() {
	}

	
	public boolean isAcceptingInput() {
		return true;
	}

	
	public void setInput(Input input) {
	}

	public void mouseWheelMoved(int change) {
		
	}

	public void mouseClicked(int button, int x, int y, int clickCount) {
		
	}

	public void mousePressed(int button, int x, int y) {
		
	}

	public void mouseReleased(int button, int x, int y) {
		
	}

	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		tx = newx;
		ty = newy;
	}

	public void mouseDragged(int oldx, int oldy, int newx, int newy) {
		
	}

}
