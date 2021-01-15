// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import net.minecraft.item.ItemStack;
import me.CheerioFX.FusionX.utils.BlockUtils2;
import net.minecraft.util.BlockPos;
import me.CheerioFX.FusionX.events.EventPreMotionUpdates;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemBlock;
import me.CheerioFX.FusionX.events.MoveEvent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import me.CheerioFX.FusionX.module.Category;
import org.hero.settings.Setting;
import me.CheerioFX.FusionX.FusionX;
import me.CheerioFX.FusionX.module.Module;

public class Scaffold extends Module
{
    public int mode;
    private int count;
    
    @Override
    public void setup() {
        FusionX.theClient.setmgr.rSetting(new Setting("SafeMode", this, true));
    }
    
    public boolean isSafeMode() {
        return FusionX.theClient.setmgr.getSetting(this, "SafeMode").getValBoolean();
    }
    
    public Scaffold() {
        super("Scaffold", 0, Category.MOVEMENT);
        this.mode = 0;
        this.count = 0;
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
    }
    
    @Override
    public void onUpdate() {
        if (!this.isSafeMode() || !this.getState()) {
            return;
        }
        if (Scaffold.mc.thePlayer.fallDistance > 2.0f && !Scaffold.mc.thePlayer.onGround) {
            Scaffold.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
            Scaffold.mc.thePlayer.onGround = true;
        }
    }
    
    @EventTarget
    public void onPlayerMove(final MoveEvent event) {
        if (!this.isSafeMode()) {
            return;
        }
        if (!(Scaffold.mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock) && this.mode == 0) {
            return;
        }
        double x = event.getX();
        final double y = event.getY();
        double z = event.getZ();
        if (Scaffold.mc.thePlayer.onGround) {
            final double increment = 0.05;
            while (x != 0.0) {
                if (!Scaffold.mc.theWorld.getCollidingBoundingBoxes(Scaffold.mc.thePlayer, Scaffold.mc.thePlayer.getEntityBoundingBox().offset(x, -1.0, 0.0)).isEmpty()) {
                    break;
                }
                if (x < increment && x >= -increment) {
                    x = 0.0;
                }
                else if (x > 0.0) {
                    x -= increment;
                }
                else {
                    x += increment;
                }
            }
            while (z != 0.0) {
                if (!Scaffold.mc.theWorld.getCollidingBoundingBoxes(Scaffold.mc.thePlayer, Scaffold.mc.thePlayer.getEntityBoundingBox().offset(0.0, -1.0, z)).isEmpty()) {
                    break;
                }
                if (z < increment && z >= -increment) {
                    z = 0.0;
                }
                else if (z > 0.0) {
                    z -= increment;
                }
                else {
                    z += increment;
                }
            }
            while (x != 0.0 && z != 0.0 && Scaffold.mc.theWorld.getCollidingBoundingBoxes(Scaffold.mc.thePlayer, Scaffold.mc.thePlayer.getEntityBoundingBox().offset(x, -1.0, z)).isEmpty()) {
                if (x < increment && x >= -increment) {
                    x = 0.0;
                }
                else if (x > 0.0) {
                    x -= increment;
                }
                else {
                    x += increment;
                }
                if (z < increment && z >= -increment) {
                    z = 0.0;
                }
                else if (z > 0.0) {
                    z -= increment;
                }
                else {
                    z += increment;
                }
            }
        }
        event.setX(x);
        event.setY(y);
        event.setZ(z);
    }
    
    @EventTarget
    public void onPreMotionUpdates(final EventPreMotionUpdates event) {
        switch (this.mode = 1) {
            case 0:
            case 1: {
                event.setPitch(90.0f);
                final BlockPos belowPlayer = new BlockPos(Scaffold.mc.thePlayer).offsetDown();
                if (!BlockUtils2.getMaterial(belowPlayer).isReplaceable()) {
                    return;
                }
                int newSlot = -1;
                for (int i = 0; i < 9; ++i) {
                    final ItemStack stack = Scaffold.mc.thePlayer.inventory.getStackInSlot(i);
                    if (!isEmptySlot(stack) && stack.getItem() instanceof ItemBlock) {
                        newSlot = i;
                        break;
                    }
                }
                if (newSlot == -1) {
                    return;
                }
                final int oldSlot = Scaffold.mc.thePlayer.inventory.currentItem;
                Scaffold.mc.thePlayer.inventory.currentItem = newSlot;
                BlockUtils2.placeBlockLegit(belowPlayer, event);
                Scaffold.mc.thePlayer.inventory.currentItem = oldSlot;
                break;
            }
        }
    }
    
    @EventTarget
    public void onPostMotionUpdates(final EventPreMotionUpdates event) {
    }
    
    public static boolean isEmptySlot(final ItemStack slot) {
        return slot == null;
    }
}
