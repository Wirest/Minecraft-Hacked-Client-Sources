package me.xatzdevelopments.xatz.client.modules;

import me.xatzdevelopments.xatz.client.events.UpdateEvent;
import me.xatzdevelopments.xatz.client.main.Xatz;
import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.client.settings.ClientSettings;
import me.xatzdevelopments.xatz.gui.custom.clickgui.ModSetting;
import me.xatzdevelopments.xatz.gui.custom.clickgui.SliderSetting;
import me.xatzdevelopments.xatz.gui.custom.clickgui.ValueFormat;
import me.xatzdevelopments.xatz.module.Module;

public class ItemSize extends Module{

	public ItemSize() {
		super("ItemSize", 0, Category.RENDER, "Allows you to select your item size");
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
	}
	
	@Override
	public void onUpdate(UpdateEvent event) {
	}
	
	@Override
	public void onDisable() {
		Xatz.getModuleByName("ItemSize").toggle();
		super.onDisable();
	}
	
	@Override
	public ModSetting[] getModSettings() {
		SliderSetting<Number> itemSize = new SliderSetting<Number>("Item Size", ClientSettings.itemSize, 0.1, 2, 0.0, ValueFormat.DECIMAL);
		return new ModSetting[] { itemSize };
	}
}
