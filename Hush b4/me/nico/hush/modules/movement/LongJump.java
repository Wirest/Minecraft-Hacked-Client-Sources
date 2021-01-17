// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.modules.movement;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.Minecraft;
import me.nico.hush.events.EventUpdate;
import me.nico.hush.modules.Category;
import me.nico.hush.utils.MoveUtils;
import me.nico.hush.modules.Module;

public class LongJump extends Module
{
    protected boolean boosted;
    protected double startY;
    protected MoveUtils move;
    protected double motionVa;
    
    public LongJump() {
        super("LongJump", "LongJump", 10243519, 33, Category.MOVEMENT);
        this.boosted = false;
        this.startY = 0.0;
        this.motionVa = 2.8;
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        final Minecraft mc = LongJump.mc;
        if (Minecraft.thePlayer.hurtTime > 0) {
            if (!this.boosted) {
                final Minecraft mc2 = LongJump.mc;
                if (Minecraft.thePlayer.onGround) {
                    final Minecraft mc3 = LongJump.mc;
                    Minecraft.thePlayer.jump();
                }
                else {
                    final MoveUtils moveUtils = (MoveUtils)MoveUtils.instance;
                    final double x = MoveUtils.getPosForSetPosX(this.motionVa);
                    final MoveUtils moveUtils2 = (MoveUtils)MoveUtils.instance;
                    final double z = MoveUtils.getPosForSetPosZ(this.motionVa);
                    final Minecraft mc4 = LongJump.mc;
                    if (Minecraft.thePlayer.posY >= this.startY - 1.7) {
                        LongJump.mc.timer.timerSpeed = 0.2f;
                        final Minecraft mc5 = LongJump.mc;
                        final EntityPlayerSP thePlayer = Minecraft.thePlayer;
                        final Minecraft mc6 = LongJump.mc;
                        final double x2 = Minecraft.thePlayer.posX + x;
                        final Minecraft mc7 = LongJump.mc;
                        final double posY = Minecraft.thePlayer.posY;
                        final Minecraft mc8 = LongJump.mc;
                        thePlayer.setPosition(x2, posY, Minecraft.thePlayer.posZ + z);
                    }
                    else {
                        LongJump.mc.timer.timerSpeed = 1.0f;
                        this.toggle();
                    }
                }
                this.boosted = true;
            }
            if (this.boosted) {
                if (this.motionVa >= 0.5) {
                    this.motionVa -= 0.15;
                }
                this.boosted = false;
            }
        }
    }
    
    @Override
    public void onEnable() {
        EventManager.register(this);
        final Minecraft mc = LongJump.mc;
        this.startY = Minecraft.thePlayer.posY;
        final Minecraft mc2 = LongJump.mc;
        final EntityPlayerSP thePlayer = Minecraft.thePlayer;
        final Minecraft mc3 = LongJump.mc;
        final double posX = Minecraft.thePlayer.posX;
        final Minecraft mc4 = LongJump.mc;
        final double y = Minecraft.thePlayer.posY - 4.0;
        final Minecraft mc5 = LongJump.mc;
        thePlayer.setPosition(posX, y, Minecraft.thePlayer.posZ);
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        EventManager.unregister(this);
        this.boosted = false;
        LongJump.mc.timer.timerSpeed = 1.0f;
        this.motionVa = 2.8;
        final Minecraft mc = LongJump.mc;
        Minecraft.thePlayer.motionY = 0.0;
        super.onDisable();
    }
}
