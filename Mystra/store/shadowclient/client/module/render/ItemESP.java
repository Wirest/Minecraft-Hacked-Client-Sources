package store.shadowclient.client.module.render;

import java.util.ArrayList;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.clickgui.settings.Setting;
import store.shadowclient.client.event.EventTarget;
import store.shadowclient.client.event.events.Event3D;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;
import store.shadowclient.client.utils.render.RenderUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;

public class ItemESP extends Module {

	public ItemESP() {
		super("ItemESP", 0, Category.RENDER);
		
		ArrayList<String> options = new ArrayList<>();
        options.add("Outline");
        
        Shadow.instance.settingsManager.rSetting(new Setting("ItemESP Mode", this, "Outline", options));
	}
	
	@EventTarget
	public void onRender(Event3D event) {
		
		String mode = Shadow.instance.settingsManager.getSettingByName("ItemESP Mode").getValString();
		
		if(mode.equalsIgnoreCase("Outline")) {
		for(Object entity : mc.theWorld.loadedEntityList)
			if(entity instanceof EntityItem)
				RenderUtils.itemESP((Entity)entity, 2);      
		}
		
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
