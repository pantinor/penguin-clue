package org.antinori.game.penguin.components;

import com.artemis.Component;

public class Health extends Component {
	private float health;
	private float maximumHealth;

	public Health(float health) {
		this.health = this.maximumHealth = health;
	}

	public float getHealth() {
		return health;
	}

	public float getMaximumHealth() {
		return maximumHealth;
	}

	public int getHealthPercentage() {
		return Math.round(health / maximumHealth * 100f);
	}
	
	public float getHealthFactor() {
		return health/maximumHealth;
	}

	public void addDamage(int damage) {
		health -= damage;
		if(health < 0)
			health = 0;
	}
	
	public boolean isAlive() {
		return health > 0;
	}

	public void addHealth(int health) {
		this.health += health;
		if(this.health > maximumHealth)
			this.health = maximumHealth;
	}

}
