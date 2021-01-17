package me.rigamortis.faurax.module.modules.player;

import me.rigamortis.faurax.module.helpers.*;
import me.rigamortis.faurax.module.*;
import com.darkmagician6.eventapi.*;
import me.rigamortis.faurax.events.*;
import me.rigamortis.faurax.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;

public class Reach extends Module implements PlayerHelper, WorldHelper, MovementHelper
{
    public static int dist;
    
    static {
        Reach.dist = 9;
    }
    
    public Reach() {
        this.setName("Reach");
        this.setType(ModuleType.PLAYER);
        this.setKey("");
        this.setColor(-15104089);
        this.setModInfo("");
        this.setVisible(true);
    }
    
    @Override
    public void onEnabled() {
        super.onEnabled();
    }
    
    @Override
    public void onDisabled() {
        super.onDisabled();
    }
    
    @EventTarget
    public void setPlayerReach(final EventPlayerReach e) {
        if (this.isToggled()) {
            e.setRange(Reach.dist);
        }
    }
    
    @EventTarget
    public void preTick(final EventPreTick e) {
        if (this.isToggled()) {
            this.setModInfo(" §7" + Reach.dist);
        }
    }
    
    @EventTarget
    public void onClickBlock(final EventClickBlock e) {
        if (this.isToggled() && Client.getClientHelper().getDistance(e.getBlockPos().getX(), e.getBlockPos().getY(), e.getBlockPos().getZ()) >= 4.0f) {
            Reach.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(e.getBlockPos().getX(), e.getBlockPos().getY() + 2, e.getBlockPos().getZ(), true));
            Client.getClientHelper().breakBlock(e.getBlockPos().getX(), e.getBlockPos().getY(), e.getBlockPos().getZ());
            Reach.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Reach.mc.thePlayer.posX, Reach.mc.thePlayer.posY, Reach.mc.thePlayer.posZ, true));
        }
    }
}
