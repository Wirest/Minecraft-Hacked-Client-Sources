package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.WaitTimer;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.modules.target.AuraUtils;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.AbstractPacket;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.Vec3;

public class AntiHunger extends Module {
	
	private double lastDist = 0;
	
	public AntiHunger() {
		super("AntiHunger", Keyboard.KEY_NONE, Category.PLAYER, "Note: Requires you to stand still! Makes you less hungry and makes potions stay longer. Also enables you to stay in water for longer. It disables natural regen.");
	}
	
	@Override
	public void onUpdate() {
		
		super.onUpdate();
	}
	
	@Override
	public void onPacketSent(AbstractPacket packet) {
		if(packet instanceof C03PacketPlayer
				&& !mc.thePlayer.isUsingItem()) {
			if(packet instanceof C03PacketPlayer.C04PacketPlayerPosition) {
				
			}
			else if(packet instanceof C03PacketPlayer.C06PacketPlayerPosLook) {
				
			}
			else if(packet instanceof C03PacketPlayer.C05PacketPlayerLook) {
	
			}
			else {
				packet.cancel();
			}
		}
		super.onPacketSent(packet);
	}
	
}
