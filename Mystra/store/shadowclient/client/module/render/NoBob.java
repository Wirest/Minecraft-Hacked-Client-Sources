package store.shadowclient.client.module.render;

import store.shadowclient.client.event.EventTarget;
import store.shadowclient.client.event.events.EventUpdate;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;
import net.minecraft.client.Minecraft;

public class NoBob extends Module {
	
    public NoBob() {
		super("NoBobbing", 0, Category.RENDER);
	}

	@EventTarget
    private void onUpdate(final EventUpdate event) {
        Minecraft.getMinecraft().thePlayer.distanceWalkedModified = 0.0f;
    }
	
	@Override
	public void onEnable() {
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
	}
}
