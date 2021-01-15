// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.module.modules.render;

import me.aristhena.event.EventTarget;
import me.aristhena.utils.ClientUtils;
import me.aristhena.event.Event;
import me.aristhena.event.events.UpdateEvent;
import me.aristhena.client.option.Option;
import me.aristhena.client.module.Module;
import me.aristhena.client.module.Module.Mod;

@Mod(displayName = "Anti-Effects", shown = false)
public class AntiEffects extends Module
{
    private static final int NAUSEA_ID = 9;
    @Option.Op
    private boolean nausea;
    @Option.Op
    private boolean blindness;
    
    public AntiEffects() {
        this.nausea = true;
        this.blindness = true;
    }
    
    @EventTarget
    private void onTick(final UpdateEvent event) {
        if (event.getState() == Event.State.POST && ClientUtils.player().isPotionActive(9) && this.nausea) {
            ClientUtils.player().removePotionEffect(NAUSEA_ID);
        }
        if (event.getState() == Event.State.POST && ClientUtils.player().isPotionActive(9) && this.blindness) {
            ClientUtils.player().removePotionEffect(15);
        }
    }
    
    public boolean isBlindness() {
        return this.blindness;
    }
}
