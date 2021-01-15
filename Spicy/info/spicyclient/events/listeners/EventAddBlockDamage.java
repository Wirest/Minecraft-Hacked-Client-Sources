package info.spicyclient.events.listeners;

import info.spicyclient.events.Event;

public class EventAddBlockDamage extends Event<EventAddBlockDamage> {
	
	public int Damage = 1;

	public int getDamage() {
		return Damage;
	}

	public void setDamage(int damage) {
		Damage = damage;
	}
	
}
