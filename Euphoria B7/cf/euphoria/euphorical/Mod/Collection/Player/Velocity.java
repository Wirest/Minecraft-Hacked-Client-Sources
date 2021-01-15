// 
// Decompiled by Procyon v0.5.30
// 

package cf.euphoria.euphorical.Mod.Collection.Player;

import net.minecraft.network.play.server.S12PacketEntityVelocity;
import cf.euphoria.euphorical.Events.EventPacketTake;
import com.darkmagician6.eventapi.EventTarget;
import cf.euphoria.euphorical.Events.EventTick;
import com.darkmagician6.eventapi.EventManager;
import org.darkstorm.minecraft.gui.component.BoundedRangeComponent;
import cf.euphoria.euphorical.Mod.Category;
import cf.euphoria.euphorical.Mod.NumValue;
import cf.euphoria.euphorical.Mod.Mod;

public class Velocity extends Mod
{
    private NumValue percent;
    
    public Velocity() {
        super("Velocity", Category.PLAYER);
        this.percent = new NumValue("Velocity", 0.0, 0.0, 200.0, BoundedRangeComponent.ValueDisplay.INTEGER);
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
        this.setRenderName(String.format("%s §7%s%", this.getModName(), this.percent.getValue()));
    }
    
    @EventTarget
    public void onPacketTake(final EventPacketTake event) {
        if (event.packet instanceof S12PacketEntityVelocity) {
            final S12PacketEntityVelocity packet = (S12PacketEntityVelocity)event.packet;
            if (this.mc.theWorld.getEntityByID(packet.func_149412_c()) == this.mc.thePlayer) {
                if (this.percent.getValue() > 0.0) {
                    final S12PacketEntityVelocity s12PacketEntityVelocity = packet;
                    s12PacketEntityVelocity.field_149415_b *= (int)(this.percent.getValue() / 100.0);
                    final S12PacketEntityVelocity s12PacketEntityVelocity2 = packet;
                    s12PacketEntityVelocity2.field_149416_c *= (int)(this.percent.getValue() / 100.0);
                    final S12PacketEntityVelocity s12PacketEntityVelocity3 = packet;
                    s12PacketEntityVelocity3.field_149414_d *= (int)(this.percent.getValue() / 100.0);
                }
                else {
                    event.setCancelled(true);
                }
            }
        }
    }
}
