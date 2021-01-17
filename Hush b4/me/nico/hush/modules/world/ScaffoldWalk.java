// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.modules.world;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.BlockPos;
import net.minecraft.client.Minecraft;
import me.nico.hush.events.EventUpdate;
import me.nico.hush.modules.Category;
import me.nico.hush.modules.Module;

public class ScaffoldWalk extends Module
{
    public ScaffoldWalk() {
        super("ScaffoldWalk", "ScaffoldWalk", 14620696, 35, Category.WORLD);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        final Minecraft mc = ScaffoldWalk.mc;
        if (Minecraft.thePlayer != null && ScaffoldWalk.mc.theWorld != null) {
            final Minecraft mc2 = ScaffoldWalk.mc;
            final ItemStack i = Minecraft.thePlayer.getCurrentEquippedItem();
            final Minecraft mc3 = ScaffoldWalk.mc;
            final double posX = Minecraft.thePlayer.posX;
            final Minecraft mc4 = ScaffoldWalk.mc;
            final double y = Minecraft.thePlayer.posY - 1.0;
            final Minecraft mc5 = ScaffoldWalk.mc;
            final BlockPos BP = new BlockPos(posX, y, Minecraft.thePlayer.posZ);
            if (i != null) {
                if (i.getItem() instanceof ItemBlock) {
                    ScaffoldWalk.mc.gameSettings.keyBindSneak.pressed = false;
                    if (ScaffoldWalk.mc.theWorld.getBlockState(BP).getBlock() == Blocks.air) {
                        ScaffoldWalk.mc.gameSettings.keyBindSneak.pressed = true;
                        ScaffoldWalk.mc.rightClickDelayTimer = 1;
                    }
                    else {
                        ScaffoldWalk.mc.gameSettings.keyBindSneak.pressed = false;
                        final Minecraft mc6 = ScaffoldWalk.mc;
                        if (Minecraft.thePlayer.getItemInUse() == null) {
                            final Minecraft mc7 = ScaffoldWalk.mc;
                            Minecraft.thePlayer.setSneaking(true);
                        }
                    }
                }
            }
            else {
                final Minecraft mc8 = ScaffoldWalk.mc;
                Minecraft.thePlayer.setSneaking(true);
            }
            final Minecraft mc9 = ScaffoldWalk.mc;
            Minecraft.thePlayer.setSneaking(false);
        }
    }
    
    @Override
    public void onEnable() {
        EventManager.register(this);
    }
    
    @Override
    public void onDisable() {
        EventManager.unregister(this);
        ScaffoldWalk.mc.gameSettings.keyBindSneak.pressed = false;
    }
}
