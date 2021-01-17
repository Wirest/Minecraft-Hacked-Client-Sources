// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.modules.player;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.client.entity.EntityPlayerSP;
import java.util.Iterator;
import net.minecraft.item.ItemSword;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.Entity;
import me.nico.hush.events.EventUpdate;
import me.nico.hush.modules.Category;
import me.nico.hush.modules.Module;

public class TNTBlock extends Module
{
    private static final float distance = 5.0f;
    
    public TNTBlock() {
        super("TNTBlock", "TNTBlock", 2821493, 0, Category.PLAYER);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        for (final Entity e : TNTBlock.mc.theWorld.loadedEntityList) {
            if (e instanceof EntityTNTPrimed) {
                final Minecraft mc = TNTBlock.mc;
                if (Minecraft.thePlayer.getDistanceToEntity(e) > 5.0f) {
                    continue;
                }
                final Minecraft mc2 = TNTBlock.mc;
                if (Minecraft.thePlayer.getHeldItem() == null) {
                    continue;
                }
                final Minecraft mc3 = TNTBlock.mc;
                if (Minecraft.thePlayer.getHeldItem().getItem() == null) {
                    continue;
                }
                final Minecraft mc4 = TNTBlock.mc;
                if (!(Minecraft.thePlayer.getHeldItem().getItem() instanceof ItemSword)) {
                    continue;
                }
                final Minecraft mc5 = TNTBlock.mc;
                final EntityPlayerSP thePlayer = Minecraft.thePlayer;
                final Minecraft mc6 = TNTBlock.mc;
                final ItemStack heldItem = Minecraft.thePlayer.getHeldItem();
                final Minecraft mc7 = TNTBlock.mc;
                final Item item = Minecraft.thePlayer.getHeldItem().getItem();
                final Minecraft mc8 = TNTBlock.mc;
                thePlayer.setItemInUse(heldItem, item.getMaxItemUseDuration(Minecraft.thePlayer.getHeldItem()));
            }
        }
    }
    
    @Override
    public void onEnable() {
        EventManager.register(this);
    }
    
    @Override
    public void onDisable() {
        EventManager.unregister(this);
    }
}
