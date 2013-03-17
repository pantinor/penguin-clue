package org.antinori.game.utils;

import java.awt.Font;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.newdawn.slick.Animation;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Rectangle;


public class AnimationSelection extends BasicGame {
	
	AnimationStore store;
	TrueTypeFont myFont;
	boolean highFlag = false;
	CreateAnimThread createThread;
	
	ArrayList<Zone> zones = new ArrayList<Zone>();
	ArrayList<String> selections = new ArrayList<String>();

	
	public static void main(String[] args) {
		try {
			AppGameContainer container = new AppGameContainer(new AnimationSelection());
			container.setDisplayMode(1200,900,false);
			container.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public AnimationSelection() {
		super("AnimationSelection");
	}
	
	public void init(GameContainer container) throws SlickException {
		String name = "NIRO";
		createThread = new CreateAnimThread(name);
		store = new AnimationStore(new File(BiowareBamSpriteCreator.OUTPUTDIR + name +".txt"));
		System.out.println("animations size="+store.animations.size());
		
		Font awtFont = new Font("Lucida Sans", Font.PLAIN, 10);
		myFont = new TrueTypeFont(awtFont, false);
		
		//add all selections cause its easier to remove them by clicking
		for (String key : store.animations.keySet()) {
			selections.add(key);
		}
	}
	
	public void render(GameContainer container, Graphics g) {
		
		g.setFont(myFont);
		g.drawString(store.sheetName, 100, 10);
		
		int dim = store.width;
        int y = 1;
        int count = 0;
        int index = 0;
        
        synchronized(zones) {
        
	        zones.clear();
	        
			for (String key : store.animations.keySet()) {
				Animation anim = store.animations.get(key);
				
				index ++;
				if (highFlag && index < 40) continue;
				if (!highFlag && index >= 40) continue;
				
				count ++;
				Rectangle rect = new Rectangle(count*dim, y*dim, dim, dim);
				g.setColor(Color.green);
				g.draw(rect);
				
				zones.add(new Zone(count*dim, y*dim, dim, dim,key));
	
				anim.draw(count*dim,y*dim);
				
				g.setColor(Color.pink);
		        g.drawString(key, count*dim, y*dim);
				
				if (count > 7) y++;
				if (count > 7) count = 0;
			}
        }
		
		
	}
	
	public void update(GameContainer container, int delta) {
		
	}
	
	@Override
	public void keyPressed(int key, char c) {
		if (key == Input.KEY_SPACE) {
			highFlag = !highFlag;
		}
		if (key == Input.KEY_C && !createThread.running) {
			Thread t = new Thread(createThread);
			t.start();
		}
	}
	
	@Override
	public void mouseClicked(int button, int x, int y, int clickCount) {
		String name = getAnimNameFromClick(x,y);
		if (name != null) {
			if (selections.contains(name)) {
				selections.remove(name);
				System.out.println("Removed "+name);
			} else {
				selections.add(name);
				System.out.println("Added "+name);
			}
		}
	}
	
	public String getAnimNameFromClick(int x, int y) {
		String clicked = null;
        synchronized(zones) {
        	for (Zone zone : zones) {
        		if (x >= zone.x && y >= zone.y && x < zone.x+zone.width && y < zone.y+zone.height) {
        			clicked = zone.animName;
        			break;
        		}
        	}
        }
		return clicked;
	}
	
	
	
	class Zone {
		public int x;
		public int y;
		public int width;
		public int height;
		public String animName;
		
		public Zone(int x, int y, int width, int height, String animName) {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			this.animName = animName;
		}

	}
	
	class CreateAnimThread implements Runnable {
		String sourceBAM ;
		boolean running = false;
		public CreateAnimThread(String sourceBAM) {
			this.sourceBAM = sourceBAM;
		}
		public void run() {
			try {
				running = true;
				String[] names = {sourceBAM};
				for (int i=0;i<names.length;i++) {
					String name = names[i];
					Collection<File> files = BiowareBamSpriteCreator.getFiles(BiowareBamSpriteCreator.BAMDIR, name+"*");
					BiowareBamSpriteCreator mr = new BiowareBamSpriteCreator();
					mr.setSelections(selections);
					mr.init(name, BiowareBamSpriteCreator.OUTPUTDIR+name+".png", files);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			running = false;
			
		}
		
		
		
	}
	
}