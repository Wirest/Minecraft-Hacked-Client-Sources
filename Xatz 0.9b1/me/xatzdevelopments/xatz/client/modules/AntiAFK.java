package me.xatzdevelopments.xatz.client.modules;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;

import me.xatzdevelopments.xatz.client.events.UpdateEvent;
import me.xatzdevelopments.xatz.client.main.Xatz;
import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.client.settings.ClientSettings;
import me.xatzdevelopments.xatz.gui.custom.clickgui.CheckBtnSetting;
import me.xatzdevelopments.xatz.gui.custom.clickgui.ModSetting;
import me.xatzdevelopments.xatz.gui.custom.clickgui.SliderSetting;
import me.xatzdevelopments.xatz.gui.custom.clickgui.ValueFormat;
import me.xatzdevelopments.xatz.module.Module;
import me.xatzdevelopments.xatz.utils.WaitTimer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;

public class AntiAFK extends Module {
    
	public ModSetting[] getModSettings() {
		CheckBtnSetting antiafktoggle = new CheckBtnSetting("Disable when moving", "antiafktoggle");
		
		return new ModSetting[] { antiafktoggle };
    }

    public AntiAFK() {
        super("AntiAFK", Keyboard.KEY_NONE, Category.PLAYER, "Prevents getting you kicked for afk");
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
    	
		
		
		if(mc.thePlayer.onGround && !mc.thePlayer.isMovingXZ()) {
			Minecraft.getMinecraft().thePlayer.jump();
		}
		if(ClientSettings.antiafktoggle) {
			if(mc.thePlayer.isMovingXZ()) {
				this.toggle();
			}
		}
		
        super.onUpdate();
    }
    
    
}