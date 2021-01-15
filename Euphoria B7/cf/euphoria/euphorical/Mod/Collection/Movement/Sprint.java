// 
// Decompiled by Procyon v0.5.30
// 

package cf.euphoria.euphorical.Mod.Collection.Movement;

import com.darkmagician6.eventapi.EventTarget;
import cf.euphoria.euphorical.Events.EventUpdate;
import com.darkmagician6.eventapi.EventManager;
import cf.euphoria.euphorical.Mod.Category;
import cf.euphoria.euphorical.Mod.BoolOption;
import cf.euphoria.euphorical.Mod.Mod;

public class Sprint extends Mod
{
    private BoolOption omniSprint;
    
    public Sprint() {
        super("Sprint", Category.MOVEMENT);
        this.omniSprint = new BoolOption("Omni-Sprint");
        this.setBind(25);
    }
    
    @Override
    public void onEnable() {
        EventManager.register(this);
    }
    
    @Override
    public void onDisable() {
        EventManager.unregister(this);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        if (this.omniSprint.isEnabled()) {
            if ((this.mc.thePlayer.movementInput.moveForward != 0.0f || this.mc.thePlayer.movementInput.moveStrafe != 0.0f) && this.mc.thePlayer.getFoodStats().getFoodLevel() > 6) {
                this.mc.thePlayer.setSprinting(true);
            }
        }
        else if (this.mc.thePlayer.movementInput.moveForward > 0.0f && this.mc.thePlayer.getFoodStats().getFoodLevel() > 6) {
            this.mc.thePlayer.setSprinting(true);
        }
    }
}
