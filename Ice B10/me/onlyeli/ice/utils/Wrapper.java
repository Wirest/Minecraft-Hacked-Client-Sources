package me.onlyeli.ice.utils;

import java.awt.Font;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.multiplayer.WorldClient;

public class Wrapper {

	public static Minecraft mc = Minecraft.getMinecraft();
	public static FontRenderer fr = mc.fontRendererObj;
	public static Object getRenderArrayList() {
		
		return null;
	}
	public static WorldClient getWorld(){
		return mc.theWorld;
	}
}
