package me.onlyeli.ice.modules;

import org.darkstorm.minecraft.gui.component.BoundedRangeComponent.ValueDisplay;
import org.lwjgl.input.Keyboard;

import com.ibm.icu.impl.ICUService.Key;

import me.onlyeli.ice.Category;
import me.onlyeli.ice.Module;
import me.onlyeli.ice.values.Value;

public class Timer extends Module {

	Value TimerSpeed = new Value("Timer Speed", 1, 1, 5, ValueDisplay.INTEGER);

	public Timer() {
		super("Timer", Keyboard.KEY_NONE, Category.WORLD);
	}

	public void onUpdate() {
		if (this.isToggled()) {
			net.minecraft.util.Timer.timerSpeed = (float) TimerSpeed.getValue();
		}
	}
	
	public void onDisable(){
		net.minecraft.util.Timer.timerSpeed = 1.0f;
	}

}
