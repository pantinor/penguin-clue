package org.antinori.game.penguin.components;

import com.artemis.Component;

public class Expires extends Component {
	private int lifeTime;
	
	public Expires(int lifeTime) {
		this.lifeTime = lifeTime;
	}
	
	public int getLifeTime() {
		return lifeTime;
	}
	
	public void setLifeTime(int lifeTime) {
		this.lifeTime = lifeTime;
	}
	
	public void reduceLifeTime(float lifeTime) {
		this.lifeTime -= lifeTime;
	}
	
	public boolean isExpired() {
		return lifeTime <= 0;
	}

	

}
