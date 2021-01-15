// 
// Decompiled by Procyon v0.5.30
// 

package cf.euphoria.euphorical.Mod.Collection.World;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.BlockCactus;
import cf.euphoria.euphorical.Events.EventBBSet;
import com.darkmagician6.eventapi.EventManager;
import cf.euphoria.euphorical.Mod.Category;
import cf.euphoria.euphorical.Mod.Mod;

public class AntiPrick extends Mod
{
    public AntiPrick() {
        super("AntiPrick", Category.WORLD);
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
    private void onBB(final EventBBSet event) {
        if (event.block instanceof BlockCactus) {
            event.boundingBox = new AxisAlignedBB(event.pos.getX(), event.pos.getY(), event.pos.getZ(), event.pos.getX() + 1, event.pos.getY() + 1, event.pos.getZ() + 1);
        }
    }
}
