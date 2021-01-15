package me.onlyeli.ice.modules;

import org.darkstorm.minecraft.gui.component.BoundedRangeComponent.ValueDisplay;
import org.lwjgl.input.Keyboard;

import me.onlyeli.ice.Category;
import me.onlyeli.ice.Module;
import me.onlyeli.ice.utils.Wrapper;
import me.onlyeli.ice.values.Value;

public class Step extends Module{
	
	Value stepHeight = new Value("Step Height", 1, 1, 5, ValueDisplay.INTEGER);

	public Step() {
		super("Step", Keyboard.KEY_K, Category.MOVEMENT);
	}
	
	public void onUpdate(){
		if(this.isToggled()){
			Wrapper.mc.getMinecraft().thePlayer.stepHeight = (float) stepHeight.getValue();
		}else{
			Wrapper.mc.getMinecraft().thePlayer.stepHeight = 0.5f;
		}
	}

}
