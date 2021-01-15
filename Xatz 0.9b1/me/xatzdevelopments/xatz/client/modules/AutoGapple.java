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
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;

public class AutoGapple extends Module {
    
	 public ModSetting[] getModSettings() {
	    	SliderSetting<Number> autogapplehealth = new SliderSetting<Number>("Health", ClientSettings.autogapplehealth, 1, 10, 0.0, ValueFormat.INT);
			
			return new ModSetting[] { autogapplehealth };
	    }

    public AutoGapple() {
        super("AutoGapple", Keyboard.KEY_NONE, Category.COMBAT, "Automatically eats gapples for you");
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
    	 if (mc.thePlayer.getHealth() <= ClientSettings.autogapplehealth) {
             if (!mc.thePlayer.isPotionActive(Potion.regeneration)) { 
                  for (int n = 0; n <= 8; n++) {
		if (mc.thePlayer.inventoryContainer.getSlot(n+36).getStack() != null  ? Item.getIdFromItem(mc.thePlayer.inventoryContainer.getSlot(n+36).getStack().getItem()) == 322 : false) {
                           mc.thePlayer.inventory.currentItem = n;
			        mc.gameSettings.keyBindUseItem.pressed = true;
                            }
                         }
                      }
                   }
                   if (mc.thePlayer.isPotionActive(Potion.regeneration)) { 
                    mc.thePlayer.inventory.currentItem = 0;
                    
                   }
                   if (mc.thePlayer.isUsingItem() == true) {
		        
			}
                   
                   
        super.onUpdate();
    }
    
    
}