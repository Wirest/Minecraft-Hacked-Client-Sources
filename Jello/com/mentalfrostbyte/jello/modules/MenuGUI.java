package com.mentalfrostbyte.jello.modules;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Keyboard;

import com.mentalfrostbyte.jello.main.Jello;
import com.mentalfrostbyte.jello.main.Module;
import com.mentalfrostbyte.jello.util.BooleanValue;
import com.mentalfrostbyte.jello.util.TimerUtil;

import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;

public class MenuGUI extends Module {

	public Random rand = new Random();
	public BooleanValue blur;
	public TimerUtil timer = new TimerUtil();
	
	public MenuGUI() {
        super("Menu", Keyboard.KEY_RSHIFT);
        blur = new BooleanValue("Blur", true);
        addValue(blur);
    }
   
	public void onEnable(){
		if (mc.theWorld != null) {
             this.mc.displayGuiScreen(Jello.jgui);
            this.toggle();
        }

	}
	
	
   
	
}
