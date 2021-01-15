package me.xatzdevelopments.xatz.client.modules;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import me.xatzdevelopments.xatz.client.Colors;
import me.xatzdevelopments.xatz.client.MathUtils;
import me.xatzdevelopments.xatz.client.events.UpdateEvent;
import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.client.modules.target.AuraUtils;
import me.xatzdevelopments.xatz.module.Module;

import me.xatzdevelopments.xatz.utils.RenderingUtil;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;

public class TargetHUD extends Module {
	
	ScaledResolution sr = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
	
	 private EntityPlayer getActiveTarget() {
		 
		return (EntityPlayer) KillAura.getCurrentTarget();
	 }
	 
	        
	    
	public TargetHUD() {
		super("TargetHUD", Keyboard.KEY_NONE, Category.RENDER, "test");
	}

	@Override
	public void onDisable() {
		
		super.onDisable();
	}

	@Override
	public void onEnable() {
		
		super.onEnable();
	}

	@Override
	public void onUpdate(UpdateEvent event) {
		 
	      EntityPlayer player = this.getActiveTarget();
	      if (player != null) {
	          GlStateManager.pushMatrix();
	          GlStateManager.translate((float)(sr.getScaledWidth() / 2 + 10), (float)(sr.getScaledHeight() - 90), 0.0F);
	          RenderingUtil.rectangle(0.0D, 0.0D, 125.0D, 36.0D, Colors.getColor(0, 150));
	          mc.fontRendererObj.drawStringWithShadow(player.getName(), 38.0F, 2.0F, -1);
	          float health = player.getHealth();
	          float[] fractions = new float[]{0.0F, 0.5F, 1.0F};
	          Color[] colors = new Color[]{Color.RED, Color.YELLOW, Color.GREEN};
	          float progress = health / player.getMaxHealth();
	          
	          double width = (double)mc.fontRendererObj.getStringWidth(player.getName());
	          width = MathUtils.getIncremental(width, 10.0D);
	          if (width < 50.0D) {
	             width = 50.0D;
	          }
           super.onUpdate();
	}
	
	
  } 
}
