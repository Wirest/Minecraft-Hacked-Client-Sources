// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.potion.PotionEffect;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import me.CheerioFX.FusionX.utils.Wrapper;
import net.minecraft.potion.Potion;
import me.CheerioFX.FusionX.events.EventPreMotionUpdates;
import me.CheerioFX.FusionX.FusionX;
import me.CheerioFX.FusionX.module.Category;
import me.CheerioFX.FusionX.module.Module;

public class Zoot extends Module
{
    private boolean potion;
    
    public Zoot() {
        super("Zoot", 0, Category.PLAYER);
        this.potion = true;
    }
    
    @Override
    public void onEnable() {
        if (Zoot.mc.isSingleplayer()) {
            FusionX.addChatMessage("This Hack Does not work on SinglePlayer.");
            this.setState(false);
        }
        super.onEnable();
    }
    
    @Override
    public void onUpdate() {
        if (this.getState() && Zoot.mc.isSingleplayer()) {
            this.setState(false);
        }
        super.onUpdate();
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
    }
    
    @EventTarget
    public void onPre(final EventPreMotionUpdates event) {
        Potion[] potionTypes;
        for (int length = (potionTypes = Potion.potionTypes).length, k = 0; k < length; ++k) {
            final Potion potion = potionTypes[k];
            if (potion != null) {
                final PotionEffect effect = Wrapper.mc.thePlayer.getActivePotionEffect(potion);
                if (effect != null && potion.isBadEffect() && this.potion) {
                    for (int i = 0; i < effect.getDuration() / 20; ++i) {
                        Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
                    }
                }
            }
        }
        if (Wrapper.mc.thePlayer.moveForward == 0.0f && Wrapper.mc.thePlayer.moveStrafing == 0.0f && Wrapper.mc.thePlayer.isBurning() && !Wrapper.isInLiquid() && !Wrapper.isOnLiquid()) {
            for (int j = 0; j < 50; ++j) {
                Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
            }
        }
    }
}
