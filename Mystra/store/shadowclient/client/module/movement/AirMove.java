package store.shadowclient.client.module.movement;

import store.shadowclient.client.event.EventTarget;
import store.shadowclient.client.event.events.EventUpdate;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;
import net.minecraft.client.Minecraft;

public class AirMove extends Module {

	private boolean onGround;
	
	public AirMove() {
		super("AirMove", 0, Category.MOVEMENT);
	}
	
	@EventTarget
	   public void onUpdate(EventUpdate event) {
	      this.onGround = Minecraft.getMinecraft().thePlayer.onGround;
	      Minecraft.getMinecraft().thePlayer.onGround = true;
	   }

	   public boolean isOnGround() {
	      return this.onGround;
	   }

}
