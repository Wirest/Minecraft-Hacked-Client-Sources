// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.modules.movement;

import net.minecraft.init.Blocks;
import com.darkmagician6.eventapi.EventManager;
import me.nico.hush.modules.Category;
import me.nico.hush.modules.Module;

public class IceSpeed extends Module
{
    public IceSpeed() {
        super("IceSpeed", "IceSpeed", 2567990, 0, Category.MOVEMENT);
    }
    
    @Override
    public void onEnable() {
        EventManager.register(this);
        Blocks.ice.slipperiness = 0.4f;
        Blocks.packed_ice.slipperiness = 0.4f;
    }
    
    @Override
    public void onDisable() {
        EventManager.unregister(this);
        Blocks.ice.slipperiness = 0.89f;
        Blocks.packed_ice.slipperiness = 0.89f;
    }
}
