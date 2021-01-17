package me.rigamortis.faurax.module.modules.movement;

import me.rigamortis.faurax.module.helpers.*;
import me.rigamortis.faurax.module.*;
import com.darkmagician6.eventapi.*;
import me.rigamortis.faurax.events.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;

public class SuicideTeleport extends Module implements PlayerHelper, WorldHelper, MovementHelper
{
    public static int dist;
    private int delay;
    
    static {
        SuicideTeleport.dist = 9;
    }
    
    public SuicideTeleport() {
        this.setName("SuicideTeleport");
        this.setType(ModuleType.PLAYER);
        this.setKey("");
        this.setColor(-15104089);
        this.setModInfo("");
        this.setVisible(true);
    }
    
    @Override
    public void onEnabled() {
        super.onEnabled();
        SuicideTeleport.dist = 12;
    }
    
    @Override
    public void onDisabled() {
        super.onDisabled();
        this.delay = 0;
    }
    
    @EventTarget
    public void setPlayerReach(final EventPlayerReach e) {
        if (this.isToggled()) {
            e.setRange(SuicideTeleport.dist);
        }
    }
    
    @EventTarget
    public void preTick(final EventPreTick e) {
        if (this.isToggled()) {
            this.setModInfo(" §7" + SuicideTeleport.dist);
        }
    }
    
    @EventTarget
    public void onDeath(final EventOnDeath e) {
        if (this.isToggled() && this.delay >= 5) {
            SuicideTeleport.mc.thePlayer.sendChatMessage("/sethome");
            SuicideTeleport.mc.thePlayer.respawnPlayer();
            SuicideTeleport.mc.thePlayer.sendChatMessage("/home");
            this.delay = 0;
        }
    }
    
    @EventTarget
    public void onDamageBlock(final EventDamageBlock e) {
        this.isToggled();
    }
    
    @EventTarget
    public void onClickBlock(final EventClickBlock e) {
        if (this.isToggled()) {
            SuicideTeleport.mc.thePlayer.sendChatMessage("/suicide");
            SuicideTeleport.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(e.getBlockPos().getX(), e.getBlockPos().getY() + 0.5f, e.getBlockPos().getZ(), true));
            SuicideTeleport.mc.thePlayer.setPosition(e.getBlockPos().getX(), e.getBlockPos().getY() + 0.5f, e.getBlockPos().getZ());
            ++this.delay;
        }
    }
}
