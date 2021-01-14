package de.iotacb.client.module.modules.combat;

import de.iotacb.client.module.Category;
import de.iotacb.client.module.Module;
import de.iotacb.client.module.ModuleInfo;
import de.iotacb.client.utilities.values.Value;
import de.iotacb.client.utilities.values.ValueMinMax;

@ModuleInfo(name = "Reach", description = "Extends your range", category = Category.COMBAT)
public class Reach extends Module {

	@Override
	public void onInit() {
		addValue(new Value("ReachAttack range", 3.5, new ValueMinMax(3, 7, .1)));
		addValue(new Value("ReachInteract range", 5, new ValueMinMax(4.5, 7, .1)));
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
	
	public double getAttackRange() {
		return getValueByName("ReachAttack range").getNumberValue();
	}
	
	public double getInteractRange() {
		return getValueByName("ReachInteract range").getNumberValue();
	}
	
	public double getMaxRange() {
		final double a = getAttackRange();
		final double i = getInteractRange();
		return (a > i ? a : i);
	}

}
