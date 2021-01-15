package info.spicyclient.modules.movement;

import org.apache.commons.lang3.RandomUtils;
import org.lwjgl.input.Keyboard;

import info.spicyclient.SpicyClient;
import info.spicyclient.chatCommands.Command;
import info.spicyclient.events.Event;
import info.spicyclient.events.listeners.EventUpdate;
import info.spicyclient.modules.Module;
import info.spicyclient.modules.combat.Killaura;
import info.spicyclient.util.MovementUtils;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class NoSlow extends Module {

	public NoSlow() {
		super("NoSlow", Keyboard.KEY_NONE, Category.MOVEMENT);
	}

	
	public void onEnable() {
		
	}
	
	public void onDisable() {
		
	}
	
	public void onEvent(Event e) {
		
		if (e instanceof EventUpdate && e.isPre()) {
			
	        if (mc.thePlayer.isBlocking() && MovementUtils.isMoving() && MovementUtils.isOnGround(0.42D) && SpicyClient.config.killaura.target == null) {
	            if (e.isPre()) {
	                mc.getNetHandler().getNetworkManager().sendPacket(new C07PacketPlayerDigging(Action.RELEASE_USE_ITEM, new BlockPos(RandomUtils.nextDouble(4.9E-324D, 1.7976931348623157E308D), RandomUtils.nextDouble(4.9E-324D, 1.7976931348623157E308D), RandomUtils.nextDouble(4.9E-324D, 1.7976931348623157E308D)), EnumFacing.DOWN));
	            } else {
	                mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, mc.thePlayer.getHeldItem(), 0.0F, 0.0F, 0.0F));
	            }
	        }
			
		}
		
	}
	
}
