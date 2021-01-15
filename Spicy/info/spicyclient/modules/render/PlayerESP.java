package info.spicyclient.modules.render;

import java.util.List;
import java.util.stream.Collectors;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;

import info.spicyclient.SpicyClient;
import info.spicyclient.events.Event;
import info.spicyclient.events.listeners.EventKey;
import info.spicyclient.events.listeners.EventPlayerRender;
import info.spicyclient.events.listeners.EventRenderGUI;
import info.spicyclient.events.listeners.EventUpdate;
import info.spicyclient.modules.Module;
import info.spicyclient.settings.BooleanSetting;
import info.spicyclient.settings.NumberSetting;
import info.spicyclient.util.RenderUtils;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

public class PlayerESP extends Module {
	
	public BooleanSetting boxes = new BooleanSetting("Boxes", true);
	public BooleanSetting chams = new BooleanSetting("Chams", false);
	
	public PlayerESP() {
		super("PlayerESP", Keyboard.KEY_NONE, Category.RENDER);
		resetSettings();
	}
	
	@Override
	public void resetSettings() {
		this.settings.clear();
		this.addSettings(boxes, chams);
	}
	
	public void onEnable() {
		
	}
	
	public void onDisable() {
		
	}
	
	public void onEvent(Event e) {
		
		if (e instanceof EventPlayerRender) {
			
			if (e.isPre()) {
				
				//GlStateManager.disableDepth();
				if (chams.isEnabled()) {
	                GL11.glEnable(32823);
	                GL11.glPolygonOffset(1.0f, -1100000.0f);
				}
				
				EventPlayerRender event = (EventPlayerRender) e;
				
				if (boxes.isEnabled()) {
					AbstractClientPlayer player = event.entity;
					//RenderUtils.renderAxisAlignedBB(player.boundingBox);
					RenderUtils.drawPlayerBox(player.posX, player.posY, player.posZ, player);
				}
				
				//GlStateManager.enableDepth();
				
			}
			
			if (e.isPost()) {
				
				//GlStateManager.enableDepth();
				if (chams.isEnabled()) {
	                GL11.glDisable(32823);
	                GL11.glPolygonOffset(1.0f, 1100000.0f);
				}
				
			}
			
		}
		
	}
	
}
