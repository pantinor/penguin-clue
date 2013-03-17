package org.antinori.game.penguin;

import org.antinori.game.penguin.animations.AnimationStore;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class ActorType {
	
	public enum Type {BLUE_KNIGHT, WHITE_MAGE, MUMMY};

	private AnimationStore walkingAnim;
	private AnimationStore runningAnim;
	private AnimationStore talkingAnim;
	private AnimationStore pausedAnim;
	private AnimationStore dyingAnim;
	private AnimationStore spawningAnim;
	private AnimationStore magicAnim;
	private AnimationStore attackAnim;
	private AnimationStore impactedAnim;
	
	public Type type;
	
	public ActorType(Type type) throws SlickException {
		this.type = type;		
	}
	
	public void setAnimationStore(AnimationStore.Type astype, SpriteSheet sheet) {
		
		int height = sheet.getHeight()/96;
		
		if (astype == AnimationStore.Type.WALKING) walkingAnim = new AnimationStore(astype, sheet, height);
		if (astype == AnimationStore.Type.RUNNING) runningAnim = new AnimationStore(astype, sheet, height);
		if (astype == AnimationStore.Type.TALKING) talkingAnim = new AnimationStore(astype, sheet, height);
		
		if (astype == AnimationStore.Type.PAUSED) pausedAnim = new AnimationStore(astype, sheet, height);
		if (astype == AnimationStore.Type.DYING) dyingAnim = new AnimationStore(astype, sheet, height);
		if (astype == AnimationStore.Type.SPAWNING) spawningAnim = new AnimationStore(astype, sheet, height);

		if (astype == AnimationStore.Type.IMPACTED) impactedAnim = new AnimationStore(astype, sheet, height);
		if (astype == AnimationStore.Type.MAGIC) magicAnim = new AnimationStore(astype, sheet, height);
		if (astype == AnimationStore.Type.ATTACKING) attackAnim = new AnimationStore(astype, sheet, height);
	}
	
	public Type getType() {
		return type;
	}
	
	public AnimationStore getAnimationStore(AnimationStore.Type ast) {
		
		AnimationStore as = null;
		
		switch (ast) {
		case WALKING: as = walkingAnim; break;
		case TALKING: as = talkingAnim; break;
		case RUNNING: as = runningAnim; break;

		case ATTACKING: as = attackAnim; break;
		case DYING: as = dyingAnim; break;
		case SPAWNING: as = spawningAnim; break;

		case MAGIC: as = magicAnim; break;
		case IMPACTED: as = impactedAnim; break;
		case PAUSED: as = pausedAnim; break;

		}
		
		return as;
	}
	

}
