package com.etb.client.module.modules.player;

import com.etb.client.event.events.player.UpdateEvent;
import com.etb.client.module.Module;
import com.etb.client.utils.value.impl.EnumValue;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import org.greenrobot.eventbus.Subscribe;

import java.awt.*;

public class Zoot extends Module {
    public Zoot() {
        super("Zoot", Category.PLAYER, new Color(170, 220, 255).getRGB());
        setDescription("Remove bad effects");
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        for (Potion potion : Potion.potionTypes) {
            PotionEffect effect;
            if (event.isPre() && (potion != null) && ((((effect = mc.thePlayer.getActivePotionEffect(potion)) != null) && (potion.isBadEffect())) || ((mc.thePlayer.isBurning()) && (!mc.thePlayer.isInWater()) && (mc.thePlayer.onGround)))) {
                for (int i = 0; mc.thePlayer.isBurning() ? i < 20 : i < effect.getDuration() / 20; i++) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
                }
            }
        }
    }
}