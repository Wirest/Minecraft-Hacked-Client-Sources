// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.module.modules.movement;

import net.minecraft.network.play.client.C0BPacketEntityAction;
import me.aristhena.event.Event;
import me.aristhena.event.events.PacketSendEvent;
import me.aristhena.event.events.SprintEvent;
import me.aristhena.event.EventTarget;
import me.aristhena.utils.ClientUtils;
import me.aristhena.event.events.TickEvent;
import me.aristhena.client.option.Option;
import me.aristhena.client.module.Module;
import me.aristhena.client.module.Module.Mod;
@Mod(shown = false)
public class Sprint extends Module
{
    @Option.Op(name = "Multi-Directional")
    private boolean multiDirection;
    @Option.Op(name = "Auto Sprint")
    private boolean auto;
    @Option.Op(name = "Client Sided")
    private boolean clientSide;
    @Option.Op
    private boolean legit;
    
    public Sprint() {
        this.multiDirection = true;
        this.auto = true;
        this.clientSide = true;
    }
    
    @EventTarget
    private void onUpdate(final TickEvent event) {
        if (ClientUtils.player() != null && this.canSprint()) {
            ClientUtils.player().setSprinting(true);
        }
    }
    
    @EventTarget
    private void onSprint(final SprintEvent event) {
        if (this.canSprint()) {
            event.setSprinting(true);
        }
    }
    
    @EventTarget
    private void onPacketSend(final PacketSendEvent event) {
        if (event.getState().equals(Event.State.PRE) && this.clientSide && event.getPacket() instanceof C0BPacketEntityAction) {
            final C0BPacketEntityAction packet = (C0BPacketEntityAction)event.getPacket();
            if (packet.func_180764_b() == C0BPacketEntityAction.Action.START_SPRINTING || packet.func_180764_b() == C0BPacketEntityAction.Action.STOP_SPRINTING) {
                event.setCancelled(true);
            }
        }
    }
    
    private boolean canSprint() {
        return this.auto && (!ClientUtils.player().isCollidedHorizontally && !ClientUtils.player().isSneaking() && (!this.legit || new NoSlowdown().getInstance().isEnabled() || (this.legit && ClientUtils.player().getFoodStats().getFoodLevel() > 5 && !ClientUtils.player().isUsingItem()))) && (this.multiDirection ? (ClientUtils.player().movementInput.moveForward != 0.0f || ClientUtils.player().movementInput.moveStrafe != 0.0f) : (ClientUtils.player().movementInput.moveForward > 0.0f));
    }
}
