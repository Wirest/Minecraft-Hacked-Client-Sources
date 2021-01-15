package me.onlyeli.ice.modules;

import org.lwjgl.input.Keyboard;

import me.onlyeli.eventapi.EventManager;
import me.onlyeli.eventapi.EventTarget;
import me.onlyeli.eventapi.types.Priority;
import me.onlyeli.ice.Category;
import me.onlyeli.ice.Module;
import me.onlyeli.ice.events.Event2;
import me.onlyeli.ice.events.EventUpdate;
import me.onlyeli.ice.utils.NetUtils;
import me.onlyeli.ice.utils.Wrapper;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class NoSlow extends Module {

	public NoSlow() {
		super("NoSlow", Keyboard.KEY_NONE, Category.MOVEMENT);
	
	}

	public void onEnable() {
		EventManager.register(this);
	}

	public void onDisable() {
		EventManager.unregister(this);
	}

	@EventTarget(Priority.LOW)
	public void onUpdate(EventUpdate event) {
		if (Wrapper.mc.thePlayer.isBlocking() && (Wrapper.mc.thePlayer.motionX != 0.0 || Wrapper.mc.thePlayer.motionZ != 0.0)) {
			if (event.state == Event2.State.PRE) {
				NetUtils.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM,
						BlockPos.ORIGIN, EnumFacing.DOWN));
			} else if (event.state == Event2.State.POST) {
				NetUtils.sendPacket(new C08PacketPlayerBlockPlacement(Wrapper.mc.thePlayer.inventory.getCurrentItem()));
			}
		}
	}
}
