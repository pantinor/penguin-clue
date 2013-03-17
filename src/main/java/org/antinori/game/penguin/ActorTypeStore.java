package org.antinori.game.penguin;

import org.antinori.game.penguin.animations.AnimationStore;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class ActorTypeStore {
	
	public static ActorType BLUE_KNIGHT;
	public static ActorType WHITE_MAGE;
	public static ActorType MUMMY;
		
	public static void init() throws SlickException {
		
		BLUE_KNIGHT = new ActorType(ActorType.Type.BLUE_KNIGHT);
		BLUE_KNIGHT.setAnimationStore(AnimationStore.Type.WALKING, new SpriteSheet("blue-knight-walking.png", 96, 96));
		BLUE_KNIGHT.setAnimationStore(AnimationStore.Type.TALKING, new SpriteSheet("blue-knight-talking.png", 96, 96));
		BLUE_KNIGHT.setAnimationStore(AnimationStore.Type.RUNNING, new SpriteSheet("blue-knight-running.png", 96, 96));
		BLUE_KNIGHT.setAnimationStore(AnimationStore.Type.ATTACKING, new SpriteSheet("blue-knight-attack.png", 96, 96));
		BLUE_KNIGHT.setAnimationStore(AnimationStore.Type.PAUSED, new SpriteSheet("blue-knight-paused.png", 96, 96));
		BLUE_KNIGHT.setAnimationStore(AnimationStore.Type.IMPACTED, new SpriteSheet("blue-knight-impacted.png", 96, 96));
		
		WHITE_MAGE = new ActorType(ActorType.Type.WHITE_MAGE);
		WHITE_MAGE.setAnimationStore(AnimationStore.Type.WALKING, new SpriteSheet("white-mage-walking.png", 96, 96));
		WHITE_MAGE.setAnimationStore(AnimationStore.Type.TALKING, new SpriteSheet("white-mage-talking.png", 96, 96));
		WHITE_MAGE.setAnimationStore(AnimationStore.Type.RUNNING, new SpriteSheet("white-mage-running.png", 96, 96));
		WHITE_MAGE.setAnimationStore(AnimationStore.Type.MAGIC, new SpriteSheet("white-mage-magic-spelling.png", 96, 96));
		WHITE_MAGE.setAnimationStore(AnimationStore.Type.PAUSED, new SpriteSheet("white-mage-paused.png", 96, 96));
		WHITE_MAGE.setAnimationStore(AnimationStore.Type.IMPACTED, new SpriteSheet("white-mage-impacted.png", 96, 96));
		WHITE_MAGE.setAnimationStore(AnimationStore.Type.ATTACKING, new SpriteSheet("white-mage-attack.png", 96, 96));
		
		MUMMY = new ActorType(ActorType.Type.MUMMY);
		MUMMY.setAnimationStore(AnimationStore.Type.WALKING, new SpriteSheet("mummy-walking.png", 96, 96));
		MUMMY.setAnimationStore(AnimationStore.Type.TALKING, new SpriteSheet("mummy-talking.png", 96, 96));
		MUMMY.setAnimationStore(AnimationStore.Type.ATTACKING, new SpriteSheet("mummy-attack.png", 96, 96));
		MUMMY.setAnimationStore(AnimationStore.Type.PAUSED, new SpriteSheet("mummy-knitting.png", 96, 96));
		MUMMY.setAnimationStore(AnimationStore.Type.RUNNING, new SpriteSheet("mummy-running.png", 96, 96));
		MUMMY.setAnimationStore(AnimationStore.Type.DYING, new SpriteSheet("mummy-dying.png", 96, 96));
		MUMMY.setAnimationStore(AnimationStore.Type.IMPACTED, new SpriteSheet("mummy-impacted.png", 96, 96));


	}
}
