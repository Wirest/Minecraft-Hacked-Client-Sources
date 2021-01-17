package me.rigamortis.faurax.module.modules.player;

import me.rigamortis.faurax.module.helpers.*;
import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.events.*;
import me.rigamortis.faurax.module.modules.movement.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import net.minecraft.potion.*;
import com.darkmagician6.eventapi.*;

public class Zoot extends Module implements PlayerHelper, MovementHelper
{
    public Zoot() {
        this.setName("Zoot");
        this.setKey("");
        this.setType(ModuleType.PLAYER);
        this.setColor(-15104089);
        this.setModInfo("");
        this.setVisible(true);
    }
    
    @EventTarget
    public void preTick(final EventPreTick e) {
        if (this.isToggled()) {
            if (Flight.enabled) {
                return;
            }
            if (Zoot.mc.thePlayer.onGround && !Zoot.mc.thePlayer.isSwingInProgress) {
                Potion[] potionTypes;
                for (int length = (potionTypes = Potion.potionTypes).length, i = 0; i < length; ++i) {
                    final Potion potion = potionTypes[i];
                    if (potion != null) {
                        final PotionEffect effect = Zoot.mc.thePlayer.getActivePotionEffect(potion);
                        if (effect != null && potion.isBadEffect()) {
                            for (int j = 0; j < effect.getDuration() / 20; ++j) {
                                Zoot.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Zoot.mc.thePlayer.posX, Zoot.mc.thePlayer.posY, Zoot.mc.thePlayer.posZ, true));
                            }
                        }
                    }
                }
                if (Zoot.mc.thePlayer.isBurning() && !Zoot.movementUtils.isInLiquid()) {
                    for (int k = 0; k < 20; ++k) {
                        Zoot.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Zoot.mc.thePlayer.posX, Zoot.mc.thePlayer.posY, Zoot.mc.thePlayer.posZ, true));
                    }
                }
            }
        }
    }
}
