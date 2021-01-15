package nivia.events.events;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import nivia.events.EventCancellable;

public class EventPotion extends EventCancellable {
	
	EntityLivingBase player;
	PotionEffect effect;
	int duration;
	
	public EventPotion(EntityLivingBase player, PotionEffect effect ,int duration) {
		this.player = player;
		this.duration = duration;
		this.effect = effect;
	}

	public EntityLivingBase getPlayer() {
		return player;
	}

	public void setPlayer(EntityLivingBase player) {
		this.player = player;
	}

	public PotionEffect getEffect() {
		return effect;
	}

	public void setEffect(PotionEffect effect) {
		this.effect = effect;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

}
