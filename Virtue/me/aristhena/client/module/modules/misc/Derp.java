// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.module.modules.misc;

import me.aristhena.event.EventTarget;
import net.minecraft.network.Packet;
import me.aristhena.utils.ClientUtils;
import net.minecraft.network.play.client.C0APacketAnimation;
import me.aristhena.event.Event;
import me.aristhena.event.events.UpdateEvent;
import me.aristhena.client.option.Option;
import me.aristhena.client.module.Module;
import me.aristhena.client.module.Module.Mod;
@Mod
public class Derp extends Module
{
    @Option.Op
    private boolean spinny;
    @Option.Op
    private boolean headless;
    @Option.Op(name = "Spin Increment", min = 1.0, max = 42.0, increment = 1.0)
    private double increment;
    private double serverYaw;
    
    public Derp() {
        this.increment = 25.0;
    }
    
    @EventTarget(0)
    private void onUpdate(final UpdateEvent event) {
        if (event.getState() == Event.State.PRE) {
            if (this.spinny) {
                this.serverYaw += this.increment;
                event.setYaw((float)this.serverYaw);
            }
            if (this.headless) {
                event.setPitch(180.0f);
            }
            else if (!this.headless && !this.spinny) {
                event.setYaw((float)(Math.random() * 360.0));
                event.setPitch((float)(Math.random() * 360.0));
                ClientUtils.packet(new C0APacketAnimation());
            }
        }
    }
}
