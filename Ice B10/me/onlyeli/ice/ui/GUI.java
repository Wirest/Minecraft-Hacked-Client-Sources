package me.onlyeli.ice.ui;

import java.text.*;
import java.util.*;
import org.lwjgl.opengl.*;
import me.onlyeli.eventapi.*;
import me.onlyeli.ice.*;
import me.onlyeli.ice.events.*;
import me.onlyeli.ice.main.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.entity.player.*;

	public class GUI extends GuiScreen {

	public GUI () {
		this.mc = Minecraft.getMinecraft();
	}
	
	private static long lastUpdateTime;
	
	public void renderScreen() {
	Display.setTitle("Ice b10 [1.8]");	
	final long timeDifference = System.nanoTime() / 1000000L - this.lastUpdateTime;
    this.lastUpdateTime = System.nanoTime() / 1000000L;
    final String timeStamp = new SimpleDateFormat("hh:mm a").format(Calendar.getInstance().getTime());
    this.mc.fontRendererObj.drawStringWithShadow("§b§lI§bce b10 §8(§b" + timeStamp + "§8) " + Minecraft.getMinecraft().getSession().getUsername() , 2.0f, 2.0f, 65365);
    final int[] pos = { this.mc.thePlayer.getPosition().getX(), this.mc.thePlayer.getPosition().getY(), this.mc.thePlayer.getPosition().getZ() };
    final String[] currPos = { "§8X: §b" + pos[0] + "§8, ", "Y: §b" + pos[1] + "§8, ", "Z: §b" + pos[2] };
    //mc.fontRendererObj.drawString(a, 4, 4, 0x99ffffff);
		//mc.fontRendererObj.drawString(b, 12, 12, 0x99ffffff);	
		
		/* creates the random colors for the modules*/
		//Random random = new Random();
		
		int count = 1;
		for(Module module : Ice.getModules()) {
			
		
			
			if(!module.isToggled()) {
				continue;
			}
			
			/*displays 8 colors randomly */
			// String color = "\247" + (1 + random.nextInt(8));
			
			/*displays colors that change when a module is activate */
			 String color = "\247" + count;
			
			mc.fontRendererObj.drawString(color + module.getModName(), 625, 4 + (10 * count), 0x90ffffff);
        	count++;	   
		}	
	}
}