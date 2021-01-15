// 
// Decompiled by Procyon v0.5.30
// 

package cf.euphoria.euphorical.Mod.Collection.Movement;

import com.darkmagician6.eventapi.EventManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import cf.euphoria.euphorical.Events.EventState;
import cf.euphoria.euphorical.Events.EventUpdate;
import com.darkmagician6.eventapi.EventTarget;
import cf.euphoria.euphorical.Euphoria;
import cf.euphoria.euphorical.Events.EventTick;
import cf.euphoria.euphorical.Mod.Category;
import cf.euphoria.euphorical.Mod.Mod;

public class Step extends Mod
{
    public Step() {
        super("Step", Category.MOVEMENT);
    }
    
    @EventTarget
    public void onTick(final EventTick event) {
        if (Euphoria.getEuphoria().noCheatPlus.isEnabled()) {
            this.setRenderName(String.format("%s [NCP]", this.getModName()));
        }
        else {
            this.setRenderName(String.format("%s", this.getModName()));
        }
    }
    
    @EventTarget
    public void onStep(final EventUpdate event) {
        if (event.state == EventState.PRE) {
            this.mc.thePlayer.stepHeight = 0.5f;
            if (this.mc.thePlayer.movementInput != null && !this.mc.thePlayer.movementInput.jump && this.mc.thePlayer.isCollidedHorizontally) {
                this.mc.thePlayer.stepHeight = 1.0f;
                if (Euphoria.getEuphoria().noCheatPlus.isEnabled()) {
                    this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.42, this.mc.thePlayer.posZ, this.mc.thePlayer.onGround));
                    event.y = this.mc.thePlayer.posY + 0.75;
                }
            }
        }
    }
    
    @Override
    public void onEnable() {
        EventManager.register(this);
    }
    
    @Override
    public void onDisable() {
        this.mc.thePlayer.stepHeight = 0.5f;
        EventManager.unregister(this);
    }
}
