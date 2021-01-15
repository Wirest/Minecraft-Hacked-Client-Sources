// 
// Decompiled by Procyon v0.5.30
// 

package cf.euphoria.euphorical.Mod.Collection.Combat;

import net.minecraft.entity.EntityLivingBase;
import cf.euphoria.euphorical.Utils.RotationUtils;
import net.minecraft.entity.Entity;
import cf.euphoria.euphorical.Utils.EntityUtils;
import cf.euphoria.euphorical.Events.EventUpdate;
import com.darkmagician6.eventapi.EventTarget;
import cf.euphoria.euphorical.Events.EventTick;
import com.darkmagician6.eventapi.EventManager;
import org.darkstorm.minecraft.gui.component.BoundedRangeComponent;
import cf.euphoria.euphorical.Mod.Category;
import cf.euphoria.euphorical.Mod.NumValue;
import cf.euphoria.euphorical.Mod.Mod;

public class Aimbot extends Mod
{
    NumValue aimbotRange;
    
    public Aimbot() {
        super("Aimbot", Category.COMBAT);
        this.aimbotRange = new NumValue("Aimbot Range", 4.0, 1.0, 6.0, BoundedRangeComponent.ValueDisplay.DECIMAL);
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
    public void onTick(final EventTick event) {
        this.setRenderName(String.format("%s §7%s", this.getModName(), this.aimbotRange.getValue()));
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        final EntityLivingBase entity = EntityUtils.getClosestEntity();
        if (entity != null && this.mc.thePlayer.getDistanceToEntity(entity) <= this.aimbotRange.getValue() && !entity.isInvisible()) {
            this.mc.thePlayer.rotationYaw = RotationUtils.getRotations(entity)[0];
            this.mc.thePlayer.rotationPitch = RotationUtils.getRotations(entity)[1];
        }
    }
}
