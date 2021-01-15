// 
// Decompiled by Procyon v0.5.30
// 

package cf.euphoria.euphorical.Mod.Collection.Movement;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.Minecraft;
import cf.euphoria.euphorical.Events.EventUpdate;
import com.darkmagician6.eventapi.EventTarget;
import cf.euphoria.euphorical.Events.EventTick;
import com.darkmagician6.eventapi.EventManager;
import cf.euphoria.euphorical.Utils.EntityUtils;
import cf.euphoria.euphorical.Euphoria;
import org.darkstorm.minecraft.gui.component.BoundedRangeComponent;
import cf.euphoria.euphorical.Mod.Category;
import cf.euphoria.euphorical.Mod.NumValue;
import cf.euphoria.euphorical.Mod.Mod;

public class Flight extends Mod
{
    private NumValue flightSpeed;
    
    public Flight() {
        super("Flight", Category.MOVEMENT);
        this.flightSpeed = new NumValue("Fly Speed", 1.0, 1.0, 10.0, BoundedRangeComponent.ValueDisplay.INTEGER);
        this.setBind(33);
    }
    
    @Override
    public void onEnable() {
        if (Euphoria.getEuphoria().noCheatPlus.isEnabled()) {
            EntityUtils.damagePlayer(1);
        }
        EventManager.register(this);
    }
    
    @EventTarget
    public void onTick(final EventTick event) {
        if (Euphoria.getEuphoria().noCheatPlus.isEnabled()) {
            this.setRenderName(String.format("%s [NCP]", this.getModName()));
        }
        else {
            this.setRenderName(String.format("%s [%s]", this.getModName(), this.flightSpeed.getValue()));
        }
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        this.mc.thePlayer.isAirBorne = false;
        if (Euphoria.getEuphoria().noCheatPlus.isEnabled()) {
            this.mc.thePlayer.motionY = 0.0;
        }
        else {
            this.mc.thePlayer.capabilities.isFlying = false;
            this.mc.thePlayer.motionX = 0.0;
            this.mc.thePlayer.motionY = 0.0;
            this.mc.thePlayer.motionZ = 0.0;
            this.mc.thePlayer.jumpMovementFactor = (float)this.flightSpeed.getValue();
            if (Minecraft.gameSettings.keyBindJump.pressed) {
                final EntityPlayerSP thePlayer = this.mc.thePlayer;
                thePlayer.motionY += this.flightSpeed.getValue();
            }
            if (Minecraft.gameSettings.keyBindSneak.pressed) {
                final EntityPlayerSP thePlayer2 = this.mc.thePlayer;
                thePlayer2.motionY -= this.flightSpeed.getValue();
            }
        }
    }
    
    @Override
    public void onDisable() {
        this.mc.thePlayer.capabilities.isFlying = false;
        EventManager.unregister(this);
    }
}
