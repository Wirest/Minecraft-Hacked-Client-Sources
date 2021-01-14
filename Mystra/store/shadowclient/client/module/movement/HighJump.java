package store.shadowclient.client.module.movement;

import store.shadowclient.client.event.EventTarget;
import store.shadowclient.client.event.events.EventUpdate;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class HighJump extends Module
{
	public static float jump = 2;
	public HighJump() {
		super("HighJump", 0, Category.MOVEMENT);
	}

	@EventTarget
    public void onUpdate(EventUpdate event) {
            mc.thePlayer.removePotionEffect(Potion.jump.id);
            mc.thePlayer.addPotionEffect(new PotionEffect(Potion.jump.id, 999, (int) jump));
    }

    @Override
    public void onDisable() {
            mc.thePlayer.removePotionEffect(Potion.jump.id);
            super.onDisable();
    }

    public static float getJump() {
            return jump;
    }
}
