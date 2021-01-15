// 
// Decompiled by Procyon v0.5.30
// 

package cf.euphoria.euphorical.Mod.Collection.Combat;

import cf.euphoria.euphorical.Utils.ModeUtils;
import net.minecraft.item.ItemSword;
import cf.euphoria.euphorical.Utils.RotationUtils;
import cf.euphoria.euphorical.Euphoria;
import net.minecraft.entity.Entity;
import cf.euphoria.euphorical.Utils.EntityUtils;
import cf.euphoria.euphorical.Events.EventState;
import cf.euphoria.euphorical.Events.EventUpdate;
import com.darkmagician6.eventapi.EventTarget;
import cf.euphoria.euphorical.Events.EventTick;
import com.darkmagician6.eventapi.EventManager;
import org.darkstorm.minecraft.gui.component.BoundedRangeComponent;
import cf.euphoria.euphorical.Mod.Category;
import net.minecraft.entity.EntityLivingBase;
import cf.euphoria.euphorical.Utils.TimeHelper;
import cf.euphoria.euphorical.Mod.BoolOption;
import cf.euphoria.euphorical.Mod.NumValue;
import cf.euphoria.euphorical.Mod.Mod;

public class Aura extends Mod
{
    private NumValue auraCPS;
    private NumValue auraRange;
    private BoolOption criticals;
    private TimeHelper timer;
    private EntityLivingBase target;
    
    public Aura() {
        super("Aura", Category.COMBAT);
        this.auraCPS = new NumValue("Aura CPS", 20.0, 0.0, 20.0, BoundedRangeComponent.ValueDisplay.INTEGER);
        this.auraRange = new NumValue("Aura Range", 4.0, 1.0, 6.0, BoundedRangeComponent.ValueDisplay.DECIMAL);
        this.criticals = new BoolOption("Aura Criticals");
        this.setBind(37);
    }
    
    @Override
    public void onEnable() {
        this.timer = new TimeHelper();
        EventManager.register(this);
    }
    
    @Override
    public void onDisable() {
        EventManager.unregister(this);
    }
    
    @EventTarget
    public void onTick(final EventTick event) {
        this.setRenderName(String.format("%s §7%s", this.getModName(), this.auraCPS.getValue()));
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        if (event.state == EventState.PRE) {
            this.target = EntityUtils.getClosestEntity();
            if (this.target != null && this.mc.thePlayer.getDistanceToEntity(this.target) <= this.auraRange.getValue() && !this.target.isInvisible()) {
                event.onGround = true;
                if (!Euphoria.getEuphoria().theMods.getMod(Aimbot.class).isEnabled()) {
                    event.yaw = RotationUtils.getRotations(this.target)[0];
                    event.pitch = RotationUtils.getRotations(this.target)[1];
                }
            }
            else {
                this.target = null;
            }
        }
        else if (event.state == EventState.POST && this.target != null) {
            if (this.mc.thePlayer.getHeldItem() != null && this.mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && ModeUtils.bHit) {
                this.mc.thePlayer.setItemInUse(this.mc.thePlayer.getHeldItem(), this.mc.thePlayer.getHeldItem().getMaxItemUseDuration());
            }
            if (this.timer.hasPassed(1000.0 / this.auraCPS.getValue())) {
                EntityUtils.attackEntity(this.target, this.criticals.isEnabled());
                this.timer.reset();
            }
        }
    }
}
