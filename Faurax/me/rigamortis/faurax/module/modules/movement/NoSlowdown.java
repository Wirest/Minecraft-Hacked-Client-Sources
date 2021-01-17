package me.rigamortis.faurax.module.modules.movement;

import me.rigamortis.faurax.module.helpers.*;
import me.rigamortis.faurax.module.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.network.*;
import com.darkmagician6.eventapi.*;
import me.rigamortis.faurax.events.*;
import net.minecraft.network.play.client.*;

public class NoSlowdown extends Module implements MovementHelper
{
    public static boolean enabled;
    
    public NoSlowdown() {
        this.setName("NoSlowdown");
        this.setKey("");
        this.setType(ModuleType.MOVEMENT);
        this.setColor(-13448726);
        this.setModInfo("");
        this.setVisible(true);
    }
    
    @Override
    public void onEnabled() {
        super.onEnabled();
        NoSlowdown.enabled = true;
    }
    
    @Override
    public void onDisabled() {
        super.onDisabled();
        NoSlowdown.enabled = false;
    }
    
    @EventTarget
    public void preTick(final EventPreTick e) {
        if (this.isToggled() && NoSlowdown.mc.thePlayer.isBlocking() && NoSlowdown.mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword) {
            NoSlowdown.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-1, -1, -1), EnumFacing.fromAngle(-1.0)));
        }
    }
    
    @EventTarget
    public void postTick(final EventPostTick e) {
        if (this.isToggled() && NoSlowdown.mc.thePlayer.isBlocking() && NoSlowdown.mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword) {
            NoSlowdown.mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(NoSlowdown.mc.thePlayer.inventory.getCurrentItem()));
        }
    }
}
