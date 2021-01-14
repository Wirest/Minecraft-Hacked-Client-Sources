package store.shadowclient.client.module.movement;

import store.shadowclient.client.event.EventTarget;
import store.shadowclient.client.event.events.EventUpdate;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;
import net.minecraft.client.Minecraft;

public class Dolphin extends Module
{
	public Dolphin() {
		super("Dolphin", 0, Category.MOVEMENT);
	}

	public int count = 0;

	@EventTarget
	public void onUpdate(EventUpdate event) {
		count++;
		if(count >= 3)
		{
			if(mc.thePlayer.handleWaterMovement())
			{
				Minecraft.getMinecraft().gameSettings.keyBindJump.pressed = true;
			}
			count = 0;
		}
	}

	@Override
	public void onDisable(){
		Minecraft.getMinecraft().gameSettings.keyBindJump.pressed = false;
		super.onDisable();
	}
}
