// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.potion.PotionEffect;
import java.util.Objects;
import net.minecraft.potion.Potion;
import me.CheerioFX.FusionX.events.EventTick;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import me.CheerioFX.FusionX.utils.Wrapper;
import me.CheerioFX.FusionX.module.Category;
import me.CheerioFX.FusionX.module.Module;

public class AntiFire extends Module
{
    public static boolean enabled;
    
    static {
        AntiFire.enabled = false;
    }
    
    public AntiFire() {
        super("AntiFire", 0, Category.PLAYER);
    }
    
    @Override
    public void onEnable() {
        if (AntiFire.mc.isSingleplayer()) {
            AntiFire.enabled = true;
        }
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        AntiFire.enabled = false;
        super.onDisable();
    }
    
    @Override
    public void onUpdate() {
        if (this.getState() && Wrapper.mc.thePlayer.isBurning()) {
            for (int x = 0; x < 100; ++x) {
                Wrapper.mc.getNetHandler().addToSendQueue(new C03PacketPlayer());
            }
        }
        super.onUpdate();
    }
    
    @EventTarget
    public void onTick(final EventTick event) {
        if (Wrapper.isOnLiquid()) {
            return;
        }
        if (Wrapper.mc.thePlayer.isPotionActive(Potion.blindness.getId())) {
            Wrapper.mc.thePlayer.removePotionEffect(Potion.blindness.getId());
        }
        if (Wrapper.mc.thePlayer.isPotionActive(Potion.confusion.getId())) {
            Wrapper.mc.thePlayer.removePotionEffect(Potion.confusion.getId());
        }
        if (Wrapper.mc.thePlayer.isPotionActive(Potion.digSlowdown.getId())) {
            Wrapper.mc.thePlayer.removePotionEffect(Potion.digSlowdown.getId());
        }
        if (Wrapper.mc.thePlayer.isBurning()) {
            for (int x = 0; x < 100; ++x) {
                Wrapper.mc.getNetHandler().addToSendQueue(new C03PacketPlayer());
            }
        }
        Potion[] potionTypes;
        for (int length = (potionTypes = Potion.potionTypes).length, i = 0; i < length; ++i) {
            final Potion potion = potionTypes[i];
            if (Objects.nonNull(potion) && potion.isBadEffect() && Wrapper.mc.thePlayer.isPotionActive(potion)) {
                final PotionEffect effect = Wrapper.mc.thePlayer.getActivePotionEffect(potion);
                for (int x2 = 0; x2 < effect.getDuration() / 20; ++x2) {
                    Wrapper.mc.getNetHandler().addToSendQueue(new C03PacketPlayer());
                }
            }
        }
    }
}
