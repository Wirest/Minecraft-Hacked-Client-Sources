package me.rigamortis.faurax.module.modules.combat;

import me.rigamortis.faurax.module.helpers.*;
import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.module.modules.movement.*;
import net.minecraft.item.*;
import net.minecraft.network.*;
import net.minecraft.network.play.client.*;
import me.rigamortis.faurax.*;
import com.darkmagician6.eventapi.*;
import me.rigamortis.faurax.events.*;

public class FastBow extends Module implements PlayerHelper, WorldHelper
{
    public int delay;
    
    public FastBow() {
        this.setName("FastBow");
        this.setKey("NUMPAD2");
        this.setType(ModuleType.COMBAT);
        this.setColor(-2996409);
        this.setModInfo("");
        this.setVisible(true);
    }
    
    @EventTarget
    public void preTick(final EventPreTick e) {
        if (this.isToggled()) {
            if (Flight.enabled) {
                return;
            }
            if (FastBow.mc.thePlayer.isUsingItem() && FastBow.mc.thePlayer.onGround && FastBow.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow) {
                for (int i = 0; i < 50; ++i) {
                    FastBow.mc.gameSettings.keyBindUseItem.pressed = !FastBow.mc.gameSettings.keyBindUseItem.pressed;
                    FastBow.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer());
                }
                FastBow.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, Client.getClientHelper().getBlockPos(FastBow.mc.thePlayer.posX, FastBow.mc.thePlayer.posY, FastBow.mc.thePlayer.posZ), Client.getClientHelper().getEnumFacing((int)FastBow.mc.thePlayer.posX, (int)FastBow.mc.thePlayer.posY, (int)FastBow.mc.thePlayer.posZ)));
                FastBow.mc.thePlayer.stopUsingItem();
            }
        }
    }
    
    @EventTarget
    public void postTick(final EventPostTick e) {
        this.isToggled();
    }
}
