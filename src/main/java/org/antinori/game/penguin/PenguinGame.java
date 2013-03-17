package org.antinori.game.penguin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import org.antinori.game.penguin.components.Health;
import org.antinori.game.penguin.components.SpatialForm;
import org.antinori.game.penguin.components.Transform;
import org.antinori.game.penguin.components.Target;
import org.antinori.game.penguin.spatials.Actor;
import org.antinori.game.penguin.systems.CollisionSystem;
import org.antinori.game.penguin.systems.EnemyDisableEnableSystem;
import org.antinori.game.penguin.systems.EnemyHealthBarRenderSystem;
import org.antinori.game.penguin.systems.EnemyShooterSystem;
import org.antinori.game.penguin.systems.EnemySpawnSystem;
import org.antinori.game.penguin.systems.ExpirationSystem;
import org.antinori.game.penguin.systems.HealthPowerUpSpawnSystem;
import org.antinori.game.penguin.systems.HudRenderSystem;
import org.antinori.game.penguin.systems.PlayerMissileRenderSystem;
import org.antinori.game.penguin.systems.MonsterMovementSystem;
import org.antinori.game.penguin.systems.PlayerShootingControlSystem;
import org.antinori.game.penguin.systems.RenderSystem;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.Layer;
import org.newdawn.slick.tiled.TiledMapPlus;
import org.newdawn.slick.util.Log;
import org.newdawn.slick.util.pathfinding.navmesh.NavMesh;
import org.newdawn.slick.util.pathfinding.navmesh.NavMeshBuilder;
import org.newdawn.slick.util.pathfinding.navmesh.NavPath;

import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;

public class PenguinGame extends BasicGame {
	
	public static Color OFF = new Color(1, 1, 1, 0.5f);
	public static Color MAP = new Color(0, 0, 0, 0.5f);

	public static final float SPEED = 0.10f;
	public static final int ANIM_INTERVAL = 100;
	public static final int UPDATE_INTERVAL = 100;
	public static final int TILE_SIZE = 48;
	
	public static final int SECTION_HEIGHT = 50;//21;
	public static final int SECTION_WIDTH = 50;//27;
	public static final int SELECTED_PLAYER_POSITION_X = 500;
	public static final int SELECTED_PLAYER_POSITION_Y = 500;

	private int tilNextFrame = ANIM_INTERVAL;
	private int tilNextUpdate = UPDATE_INTERVAL;

	private LevelMap map;
	private NavPath currentPath;
	private NavMesh mesh;

	private float offsetx;
	private float offsety;
	private int sectionx;
	private int sectiony;

	private Actor selected;
	private ArrayList<Actor> party = new ArrayList<Actor>();
	private GameContainer container;
	
	private World world;
	private EntitySystem renderSystem;
	private EntitySystem hudRenderSystem;
	private EntitySystem healthBarRenderSystem;
	private EntitySystem missileRenderSystem;



	public PenguinGame() {
		super("PenguinGame");
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		this.container = container;
		
		//container.setClearEachFrame(true);
		container.setAlwaysRender(true);
		
		Images.init();
		ActorTypeStore.init();
		
		world = new World();
		world.setManager(new GroupManager());
		world.setManager(new TagManager());
		
		generateMap();
		
		world.setSystem(new MonsterMovementSystem(container, mesh));
		world.setSystem(new PlayerShootingControlSystem(container, this));
		world.setSystem(new EnemyShooterSystem());
		world.setSystem(new EnemyDisableEnableSystem());
		world.setSystem(new CollisionSystem());
		world.setSystem(new EnemySpawnSystem(10000, container, map));
		world.setSystem(new ExpirationSystem());
		world.setSystem(new HealthPowerUpSpawnSystem(10000, container, map));

		renderSystem = world.setSystem(new RenderSystem(container, this, map), true);
		hudRenderSystem = world.setSystem(new HudRenderSystem(container), true);
		missileRenderSystem = world.setSystem(new PlayerMissileRenderSystem(container), true);
		//healthBarRenderSystem = world.setSystem(new EnemyHealthBarRenderSystem(container), true);

		world.initialize();
		
		initPlayers();
	}
	
	

	private void initPlayers() {
		
		Log.info("Adding charactors...");

		Entity e = world.createEntity();
		e.addComponent(new Transform(map.getStartX() + 1, map.getStartY() + 1));
		e.addComponent(new SpatialForm("MAGE"));
		e.addComponent(new Target());
		e.addComponent(new Health(30));
		
		world.getManager(GroupManager.class).add(e,"PLAYERS");
		world.getManager(TagManager.class).register("MAGE", e);
		world.addEntity(e);
		
	}

	private void generateMap() {
		Log.info("Building map....");
		map = new LevelMap(60,40);
		
		try {
			TiledMapPlus tiledmap = new TiledMapPlus("./map.tmx");
			for (int x=0;x<60;x++) {
				for (int y=0;y<40;y++) {
					int tile = map.getTile(x, y, 0);
					tiledmap.setTileId(x, y, 0, tile);
				}
			}
			for (int x=0;x<60;x++) {
				for (int y=0;y<40;y++) {
					int tile = map.getTile(x, y, 1);
					tiledmap.setTileId(x, y, 1, tile);
				}
			}
			for (int x=0;x<60;x++) {
				for (int y=0;y<40;y++) {
					int tile = map.getTile(x, y, 2);
					tiledmap.setTileId(x, y, 2, tile);
				}
			}
			OutputStream out = new FileOutputStream(new File("generated-map.tmx"));
			tiledmap.write(out);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Log.info("Building navmesh...");
		NavMeshBuilder builder = new NavMeshBuilder();
		mesh = builder.build(map, false);
	}
	
	

	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		
		world.setDelta(delta);
		world.process();
		
		tilNextFrame -= delta;
		tilNextUpdate -= delta;
		
		//map.update(true, true, sectionx, sectiony, SECTION_WIDTH, SECTION_HEIGHT);
		map.update(tilNextFrame < 0, tilNextUpdate < 0, sectionx, sectiony, SECTION_WIDTH, SECTION_HEIGHT);

		if (tilNextFrame < 0) {
			tilNextFrame += ANIM_INTERVAL;
		}
		if (tilNextUpdate < 0) {
			tilNextUpdate += UPDATE_INTERVAL;
		}

	}

	public void render(GameContainer container, Graphics g) throws SlickException {
				
		if (selected != null) {
					
			if (offsetx == 0) {
				offsetx = SELECTED_PLAYER_POSITION_X - (selected.getX() * TILE_SIZE);
				offsety = SELECTED_PLAYER_POSITION_Y - (selected.getY() * TILE_SIZE);
			} else {
				offsetx = (offsetx * 0.8f) + ((SELECTED_PLAYER_POSITION_X - (selected.getX() * TILE_SIZE)) * 0.2f);
				offsety = (offsety * 0.8f) + ((SELECTED_PLAYER_POSITION_Y - (selected.getY() * TILE_SIZE)) * 0.2f);
			}
	
			g.translate((int) offsetx, (int) offsety);
			
			sectionx = (int) (selected.getX() - 15);
			sectiony = (int) (selected.getY() - 13);
			
			map.render(g, sectionx, sectiony, SECTION_WIDTH, SECTION_HEIGHT);
			
			renderSystem.process();
			missileRenderSystem.process();

			g.translate(-(int) offsetx, -(int) offsety);
		}

		g.translate(container.getWidth() - 25 - (map.getWidthInTiles()) * 3, container.getHeight() - 25 - (map.getHeightInTiles()) * 3);
		drawMiniMap(g);
		g.translate(-(container.getWidth() - 25 - (map.getWidthInTiles() * 3)), -(container.getHeight() - 25 - (map.getHeightInTiles() * 3)));
		
		//healthBarRenderSystem.process();
		
		hudRenderSystem.process();
		
		
		g.setColor(Color.white);
		g.drawString("FPS: " + container.getFPS(), 10, 10);
		g.drawString("Active entities: " + world.getEntityManager().getActiveEntityCount(), 10, 25);

	}

	public void drawMiniMap(Graphics g) {
		Images.MAP.draw(-5, -10);
		g.setColor(MAP);
		for (int x = 0; x < map.getWidthInTiles(); x++) {
			for (int y = 0; y < map.getHeightInTiles(); y++) {
				if (map.discovered(x, y)) {
					if (map.blocked(x, y)) {
						g.fillRect(5 + (x * 3), 5 + (y * 3), 3, 3);
					}
				}
			}
		}
	}

	private Actor getActorAt(float x, float y) {
		return map.getActorAt(x, y);
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		super.mousePressed(button, x, y);

		if (x > container.getWidth() - 50) {
			int index = (y - 5) / 60;
			if (index < party.size()) {
				selected = party.get(index);
				return;
			}
		}

		float targetX = (x - offsetx) / TILE_SIZE;
		float targetY = (y - offsety) / TILE_SIZE;


		if ((x > container.getWidth() - 10 - (map.getWidthInTiles()) * 3) && (y > container.getHeight() - 10 - (map.getHeightInTiles()) * 3)) {
			targetX = (x - (container.getWidth() - 10 - (map.getWidthInTiles()) * 3) - 5) / 3.0f;
			targetY = (y - (container.getHeight() - 10 - (map.getHeightInTiles()) * 3) - 5) / 3.0f;
		} else {
			Actor actor = getActorAt(targetX, targetY);
			if (actor != null) {
				selected = actor;
				return;
			}
		}

		currentPath = mesh.findPath(selected.getX(), selected.getY(), targetX, targetY, false);
		if (currentPath == null) {
			targetX += 0.5f;
			currentPath = mesh.findPath(selected.getX(), selected.getY(), targetX, targetY, false);
		}
		if (currentPath == null) {
			targetX -= 1f;
			currentPath = mesh.findPath(selected.getX(), selected.getY(), targetX, targetY, false);
		}

		selected.setCurrentPath(currentPath, targetX, targetY);
	}

	public static void main(String[] args) {
		
		try {
						
			AppGameContainer container = new AppGameContainer(new PenguinGame(), 640, 480, false);
			container.setDisplayMode(container.getScreenWidth(), container.getScreenHeight(), false);
			container.setShowFPS(true);
			container.start();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Actor getSelected() {
		return selected;
	}

	public void addActor(Actor actor) {
				
		if (this.selected == null) this.selected = actor;
		map.addActor(actor);
		party.add(actor);
	}

	public float getOffsetx() {
		return offsetx;
	}

	public float getOffsety() {
		return offsety;
	}

}
