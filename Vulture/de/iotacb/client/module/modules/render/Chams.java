package de.iotacb.client.module.modules.render;

import de.iotacb.client.module.Category;
import de.iotacb.client.module.Module;
import de.iotacb.client.module.ModuleInfo;
import de.iotacb.client.utilities.values.Value;
import de.iotacb.client.utilities.values.ValueMinMax;
import de.iotacb.cu.core.mc.entity.EntityUtil;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;

@ModuleInfo(name = "Chams", description = "Draws entities through blocks", category = Category.RENDER)
public class Chams extends Module {
	
	/**
	 * Changes in the render classes are from liquid bounce
	 */

	@Override
	public void onInit() {
		addValue(new Value("ChamsColored", false));
		addValue(new Value("ChamsEntities", true));
		addValue(new Value("ChamsItems", true));
		addValue(new Value("ChamsChests", true));
		addValue(new Value("ChamsAlpha", 50, new ValueMinMax(0, 255, 1)));
	}
	
	@Override
	public void updateValueStates() {
		getValueByName("ChamsAlpha").setEnabled(!getValueByName("ChamsColored").getBooleanValue());
		super.updateValueStates();
	}
	
	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
	}

	@Override
	public void onToggle() {
	}
	
	public boolean isValid(Entity entity) {
		if (entity instanceof EntityLivingBase && !getValueByName("ChamsEntities").getBooleanValue()) return false;
		if (entity instanceof EntityItem && !getValueByName("ChamsItems").getBooleanValue()) return false;
		if (entity instanceof EntityPlayerSP) return false;
		return true;
	}

}
