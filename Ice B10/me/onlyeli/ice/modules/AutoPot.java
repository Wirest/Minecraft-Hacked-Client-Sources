package me.onlyeli.ice.modules;

import org.darkstorm.minecraft.gui.component.BoundedRangeComponent.ValueDisplay;
import org.lwjgl.input.Keyboard;

import me.onlyeli.ice.events.EventMove;
import me.onlyeli.eventapi.EventManager;
import me.onlyeli.eventapi.EventTarget;
import me.onlyeli.eventapi.types.Priority;
import me.onlyeli.ice.Category;
import me.onlyeli.ice.Module;
import me.onlyeli.ice.events.Event2;
import me.onlyeli.ice.events.EventTick;
import me.onlyeli.ice.events.EventUpdate;
import me.onlyeli.ice.events.NumValue;
import me.onlyeli.ice.utils.InvUtils;
import me.onlyeli.ice.utils.NetUtils;
import me.onlyeli.ice.utils.TimeHelper;
import me.onlyeli.ice.utils.Wrapper;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;

public class AutoPot extends Module {

	private NumValue autoPot = new NumValue("Pot Health", 4, 1, 10, ValueDisplay.DECIMAL);
	private NumValue autoPotDelay = new NumValue("Pot Delay (MS)", 250, 1, 1000, ValueDisplay.INTEGER);
	private TimeHelper timer;
	private int lockedTicks;
	private int potSlot;

	public AutoPot() {
		super("AutoPot", Keyboard.KEY_NONE, Category.COMBAT);
		
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
	public void onTick(EventTick event) {
		this.setModName(String.format("%s [%s]", this.getModName(), InvUtils.getPotsInInventory()));
	}

	@EventTarget(Priority.LOWEST)
	public void onUpdate(EventUpdate event) {
		if (event.state == Event2.State.PRE) {
			if (this.lockedTicks >= 0) {
				if (this.lockedTicks == 0) {
					event.y = 1.3;
				} else {
					event.setCancelled(true);
				}
				--this.lockedTicks;
			} else {
				int potSlot = InvUtils.getPotFromInventory();
				if (potSlot != -1) {
					if (Wrapper.mc.thePlayer.getHealth() <= this.autoPot.getValue() * 2.0
							&& this.timer.hasPassed(this.autoPotDelay.getValue())) {
						event.yaw = Wrapper.mc.thePlayer.rotationYaw;
						event.pitch = 90;
					}
				}
			}
		} else if (event.state == Event2.State.POST && this.lockedTicks < 0) {
			int potSlot = InvUtils.getPotFromInventory();
			if (potSlot != -1) {
				if (Wrapper.mc.thePlayer.getHealth() <= this.autoPot.getValue() * 2.0
						&& this.timer.hasPassed(this.autoPotDelay.getValue())) {
					int prevSlot = Wrapper.mc.thePlayer.inventory.currentItem;
					if (potSlot < 9) {
						NetUtils.sendPacket(new C09PacketHeldItemChange(potSlot));
						NetUtils.sendPacket(
								new C08PacketPlayerBlockPlacement(Wrapper.mc.thePlayer.inventory.getCurrentItem()));
						NetUtils.sendPacket(new C09PacketHeldItemChange(prevSlot));
						Wrapper.mc.thePlayer.inventory.currentItem = prevSlot;
					} else {
						InvUtils.swap(potSlot, 6);
						this.potSlot = potSlot;
						InvUtils.swapShift(potSlot);
						NetUtils.sendPacket(new C09PacketHeldItemChange(6));
						NetUtils.sendPacket(
								new C08PacketPlayerBlockPlacement(Wrapper.mc.thePlayer.inventory.getCurrentItem()));
						NetUtils.sendPacket(new C09PacketHeldItemChange(prevSlot));
					}
					this.timer.reset();
				}
			}
		}
	}

	@EventTarget
	private void onMove(EventMove event) {
		if (this.lockedTicks >= 0) {
			event.x = 0.0;
			event.z = 0.0;
			event.y = 0.0;
		}
	}
}
