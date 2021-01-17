package me.rigamortis.faurax.module.modules.player;

import me.rigamortis.faurax.module.helpers.*;
import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.events.*;
import me.rigamortis.faurax.module.modules.movement.*;
import net.minecraft.item.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import com.darkmagician6.eventapi.*;

public class FastUse extends Module implements WorldHelper
{
    public FastUse() {
        this.setName("FastUse");
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
            if (FastUse.mc.thePlayer.isUsingItem() && !(FastUse.mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword) && !(FastUse.mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemBow) && FastUse.mc.thePlayer.onGround && FastUse.mc.thePlayer.isCollidedVertically && FastUse.mc.thePlayer.getItemInUseDuration() >= 16) {
                for (int i = 0; i < 40; ++i) {
                    FastUse.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer());
                }
                FastUse.mc.thePlayer.stopUsingItem();
            }
        }
    }
    
    @Override
    public void onDisabled() {
        super.onDisabled();
    }
}
