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
import me.xatzdevelopments.xatz.gui.custom.clickgui.ModSetting;
import me.xatzdevelopments.xatz.gui.custom.clickgui.SliderSetting;
import me.xatzdevelopments.xatz.gui.custom.clickgui.ValueFormat;
import me.xatzdevelopments.xatz.module.Module;
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
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

public class Eagle extends Module {
    
    

    public Eagle() {
        super("Eagle", Keyboard.KEY_NONE, Category.MOVEMENT, "Automate speed bridge");
    }

    @Override
    public void onDisable() {
    	 mc.gameSettings.keyBindSneak.pressed = false;
        super.onDisable();
    }

    @Override
    public void onEnable() {
        
        super.onEnable();
    }

    @Override
    public void onUpdate(UpdateEvent event) {
    	mc.gameSettings.keyBindSneak.pressed = false;
		  mc.gameSettings.keyBindJump.pressed = false;
			if(this.isToggled()){
			
				if(mc.thePlayer != null && mc.theWorld != null) {
					ItemStack i = mc.thePlayer.getCurrentEquippedItem();
					BlockPos Bp = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1D, mc.thePlayer.posZ);
					if(i != null) {
						if(i.getItem() instanceof ItemBlock) {
							mc.gameSettings.keyBindSneak.pressed = false;
							if(mc.theWorld.getBlockState(Bp).getBlock() == Blocks.air) {
								mc.gameSettings.keyBindSneak.pressed = true;
							}
							
						}
			        }
		       }			
	       }
        super.onUpdate();
    }
    
    
}