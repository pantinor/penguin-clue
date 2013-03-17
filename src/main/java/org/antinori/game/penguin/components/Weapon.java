package org.antinori.game.penguin.components;

import com.artemis.Component;

public class Weapon extends Component {
	private long shotAt;

	public Weapon() {
	}

	public void setShotAt(long shotAt) {
		this.shotAt = shotAt;
	}

	public long getShotAt() {
		return shotAt;
	}

}
