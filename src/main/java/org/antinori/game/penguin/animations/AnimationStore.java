package org.antinori.game.penguin.animations;


import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class AnimationStore {
	
	public static final int NORTH = 0;
	public static final int NORTH_EAST = 1;
	public static final int EAST = 2;
	public static final int SOUTH_EAST = 3;
	public static final int SOUTH = 4;
	public static final int SOUTH_WEST = 5;
	public static final int WEST = 6;
	public static final int NORTH_WEST = 7;
	
	public static final int DURATION =  150;

	public enum Type {WALKING, TALKING, RUNNING, ATTACKING, MAGIC, PAUSED, IMPACTED, SPAWNING, DYING};
			
	private Animation east;
	private Animation west;
	private Animation north;
	private Animation south;
	
	private Animation southeast;
	private Animation southwest;
	private Animation northeast;
	private Animation northwest;
	
	private Type type;
	
	public AnimationStore(Type type, SpriteSheet sheet, int max) {
		
		this.type = type;

		this.east = new Animation();
		this.west = new Animation();
		this.north = new Animation();
		this.south = new Animation();
		this.southeast = new Animation();
		this.southwest = new Animation();
		this.northwest = new Animation();
		this.northeast = new Animation();
				
		for (int i=0;i<max;i++) east.addFrame(sheet.getSprite(0, i), DURATION);
		for (int i=0;i<max;i++) north.addFrame(sheet.getSprite(1, i), DURATION);
		for (int i=0;i<max;i++) northeast.addFrame(sheet.getSprite(2, i), DURATION);
		for (int i=0;i<max;i++) northwest.addFrame(sheet.getSprite(3, i), DURATION);
		for (int i=0;i<max;i++) south.addFrame(sheet.getSprite(4, i), DURATION);
		for (int i=0;i<max;i++) southeast.addFrame(sheet.getSprite(5, i), DURATION);
		for (int i=0;i<max;i++) southwest.addFrame(sheet.getSprite(6, i), DURATION);
		for (int i=0;i<max;i++) west.addFrame(sheet.getSprite(7, i), DURATION);
		
	}


	public Type getType() {
		return type;
	}
	
	public void render(int direction, float x, float y) {
		
		Animation anim = south;
		
		switch(direction) {
		case NORTH:anim = north;break;
		case NORTH_WEST:anim = northwest;break;
		case NORTH_EAST:anim = northeast;break;
		case SOUTH:anim = south;break;
		case SOUTH_EAST:anim = southeast;break;
		case SOUTH_WEST:anim = southwest;break;
		case EAST:anim = east;break;
		case WEST:anim = west;break;
		}
		
		anim.draw(x,y);
	}
	
}
