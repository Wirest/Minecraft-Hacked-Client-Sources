// 
// Decompiled by Procyon v0.5.30
// 

package cf.euphoria.euphorical.Mod.Collection.Player;

import com.darkmagician6.eventapi.EventTarget;
import cf.euphoria.euphorical.Events.EventRightClick;
import com.darkmagician6.eventapi.EventManager;
import cf.euphoria.euphorical.Mod.Category;
import cf.euphoria.euphorical.Mod.Mod;

public class FastPlace extends Mod
{
    public FastPlace() {
        super("FastPlace", Category.PLAYER);
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
    public void onRightClick(final EventRightClick event) {
        this.mc.rightClickDelayTimer = 0;
    }
}
