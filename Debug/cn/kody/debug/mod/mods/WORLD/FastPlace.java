package cn.kody.debug.mod.mods.WORLD;

import com.darkmagician6.eventapi.EventTarget;

import cn.kody.debug.events.EventUpdate;
import cn.kody.debug.mod.Category;
import cn.kody.debug.mod.Mod;

public class FastPlace extends Mod
{
    
    public FastPlace() {
        super("FastPlace", "Fast Place", Category.WORLD);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate eventUpdate) {
        this.mc.rightClickDelayTimer = Math.min(this.mc.rightClickDelayTimer, 1);
    }
}
