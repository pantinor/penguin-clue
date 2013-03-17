package org.antinori.game.penguin.components;

import com.artemis.Component;

public class Target extends Component {
	private float x;
	private float y;

	public Target() {
	}

	public Target(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
	
	public void addX(float x) {
		this.x += x;
	}

	public void addY(float y) {
		this.y += y;
	}

}
