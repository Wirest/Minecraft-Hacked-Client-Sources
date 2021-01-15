// 
// Decompiled by Procyon v0.5.30
// 

package cf.euphoria.euphorical.Mod.Collection.World;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.entity.EntityPlayerSP;
import cf.euphoria.euphorical.Utils.BlockUtils;
import cf.euphoria.euphorical.Events.EventUpdate;
import com.darkmagician6.eventapi.EventManager;
import cf.euphoria.euphorical.Mod.Category;
import cf.euphoria.euphorical.Mod.Mod;

public class Jesus extends Mod
{
    public Jesus() {
        super("Jesus", Category.WORLD);
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
        if (BlockUtils.isInLiquid() && !this.mc.thePlayer.isSneaking()) {
            this.mc.thePlayer.motionY = 0.1;
            final EntityPlayerSP thePlayer = this.mc.thePlayer;
            thePlayer.jumpMovementFactor *= 1.12f;
        }
    }
}
