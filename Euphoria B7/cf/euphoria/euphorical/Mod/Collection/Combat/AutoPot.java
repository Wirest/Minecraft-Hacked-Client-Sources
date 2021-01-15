// 
// Decompiled by Procyon v0.5.30
// 

package cf.euphoria.euphorical.Mod.Collection.Combat;

import cf.euphoria.euphorical.Events.EventMove;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.Packet;
import cf.euphoria.euphorical.Utils.NetUtils;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import cf.euphoria.euphorical.Events.EventState;
import cf.euphoria.euphorical.Events.EventUpdate;
import com.darkmagician6.eventapi.EventTarget;
import cf.euphoria.euphorical.Utils.InvUtils;
import cf.euphoria.euphorical.Events.EventTick;
import com.darkmagician6.eventapi.EventManager;
import org.darkstorm.minecraft.gui.component.BoundedRangeComponent;
import cf.euphoria.euphorical.Mod.Category;
import cf.euphoria.euphorical.Utils.TimeHelper;
import cf.euphoria.euphorical.Mod.NumValue;
import cf.euphoria.euphorical.Mod.Mod;

public class AutoPot extends Mod
{
    private NumValue autoPot;
    private NumValue autoPotDelay;
    private TimeHelper timer;
    private int lockedTicks;
    private int potSlot;
    
    public AutoPot() {
        super("Auto Pot.", Category.COMBAT);
        this.autoPot = new NumValue("Pot Health", 4.0, 1.0, 10.0, BoundedRangeComponent.ValueDisplay.DECIMAL);
        this.autoPotDelay = new NumValue("Pot Delay (MS)", 250.0, 1.0, 1000.0, BoundedRangeComponent.ValueDisplay.INTEGER);
        this.setName("AutoPot");
    }
    
    @Override
    public void onEnable() {
        this.timer = new TimeHelper();
        this.lockedTicks = -1;
        EventManager.register(this);
    }
    
    @Override
    public void onDisable() {
        this.lockedTicks = -1;
        EventManager.unregister(this);
    }
    
    @EventTarget
    public void onTick(final EventTick event) {
        this.setRenderName(String.format("%s [%s]", this.getModName(), InvUtils.getPotsInInventory()));
    }
    
    @EventTarget(4)
    public void onUpdate(final EventUpdate event) {
        if (event.state == EventState.PRE) {
            if (this.lockedTicks >= 0) {
                if (this.lockedTicks == 0) {
                    event.y = 1.3;
                }
                else {
                    event.setCancelled(true);
                }
                --this.lockedTicks;
            }
            else {
                final int potSlot = InvUtils.getPotFromInventory();
                if (potSlot != -1 && this.mc.thePlayer.getHealth() <= this.autoPot.getValue() * 2.0 && this.timer.hasPassed(this.autoPotDelay.getValue())) {
                    event.yaw = this.mc.thePlayer.rotationYaw;
                    event.pitch = 90.0f;
                }
            }
        }
        else if (event.state == EventState.POST && this.lockedTicks < 0) {
            final int potSlot = InvUtils.getPotFromInventory();
            if (potSlot != -1 && this.mc.thePlayer.getHealth() <= this.autoPot.getValue() * 2.0 && this.timer.hasPassed(this.autoPotDelay.getValue())) {
                final int prevSlot = this.mc.thePlayer.inventory.currentItem;
                if (potSlot < 9) {
                    NetUtils.sendPacket(new C09PacketHeldItemChange(potSlot));
                    NetUtils.sendPacket(new C08PacketPlayerBlockPlacement(this.mc.thePlayer.inventory.getCurrentItem()));
                    NetUtils.sendPacket(new C09PacketHeldItemChange(prevSlot));
                    this.mc.thePlayer.inventory.currentItem = prevSlot;
                }
                else {
                    InvUtils.swap(potSlot, 6);
                    InvUtils.swapShift(this.potSlot = potSlot);
                    NetUtils.sendPacket(new C09PacketHeldItemChange(6));
                    NetUtils.sendPacket(new C08PacketPlayerBlockPlacement(this.mc.thePlayer.inventory.getCurrentItem()));
                    NetUtils.sendPacket(new C09PacketHeldItemChange(prevSlot));
                }
                this.timer.reset();
            }
        }
    }
    
    @EventTarget
    private void onMove(final EventMove event) {
        if (this.lockedTicks >= 0) {
            event.x = 0.0;
            event.z = 0.0;
            event.y = 0.0;
        }
    }
}
