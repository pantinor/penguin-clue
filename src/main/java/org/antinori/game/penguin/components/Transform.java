package org.antinori.game.penguin.components;

import org.antinori.game.penguin.spatials.Actor;
import org.antinori.game.penguin.spatials.Missile;
import org.antinori.game.penguin.spatials.Monster;
import org.newdawn.slick.geom.Vector2f;

import com.artemis.Component;
import com.artemis.utils.Utils;

public class Transform extends Component {
	private float x;
	private float y;
	private float rotation;
	private float width;
	private float height;
	
	private Actor actor;
	private Monster monster;
	private Missile missile;
	
	/** speed vector (x,y): specifies x and y movement per update call in pixels */
	private Vector2f speed = new Vector2f(2, 2);

	public Transform() {
	}

	public Transform(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Transform(float x, float y, float rotation) {
		this(x, y);
		this.rotation = rotation;
	}

	public void addX(float x) {
		this.x += x;
	}

	public void addY(float y) {
		this.y += y;
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

	public void setLocation(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	public void addRotation(float angle) {
		rotation = (rotation + angle) % 360;
	}

	public float getRotationAsRadians() {
		return (float) Math.toRadians(rotation);
	}
	
	public float getDistanceTo(Transform t) {
		return Utils.distance(t.getX(), t.getY(), x, y);
	}

	public Actor getActor() {
		return actor;
	}

	public Monster getMonster() {
		return monster;
	}

	public void setActor(Actor actor) {
		this.actor = actor;
	}

	public void setMonster(Monster monster) {
		this.monster = monster;
	}

	public Missile getMissile() {
		return missile;
	}

	public void setMissile(Missile missile) {
		this.missile = missile;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public Vector2f getSpeed() {
		return speed;
	}

	public void setSpeed(Vector2f speed) {
		this.speed = speed;
	}

}
