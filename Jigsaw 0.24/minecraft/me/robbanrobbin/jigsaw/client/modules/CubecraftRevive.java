package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.network.AbstractPacket;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S06PacketUpdateHealth;

public class CubecraftRevive extends Module {

	private boolean dead;
	private double posY;

	public CubecraftRevive() {
		super("CubecraftRevive", Keyboard.KEY_NONE, Category.EXPLOITS, "Revives you in eggwars");
	}

	private void start() {
		Jigsaw.chatMessage("START");
		mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.3, mc.thePlayer.posZ);
		posY = mc.thePlayer.posY;
		//Jigsaw.sendChatMessage(".damage");
		dead = true;
	}
	
	
	@Override
	public void onPacketRecieved(AbstractPacket packetIn) {
		if(dead) {
			return;
		}
		if(packetIn instanceof S06PacketUpdateHealth) {
			if(((S06PacketUpdateHealth) packetIn).getHealth() < mc.thePlayer.getHealth()) {
				double healthDiff = ((S06PacketUpdateHealth) packetIn).getHealth() - mc.thePlayer.getHealth();
				healthDiff = Math.abs(healthDiff);
				if(mc.thePlayer.getHealth() - (healthDiff * 2.0) <= 2.0) {
					start();
				}
			} 
		}
		super.onPacketRecieved(packetIn);
	}
	
	@Override
	public void onToggle() {
		dead = false;
		super.onToggle();
	}
	
	@Override
	public void onUpdate() {
		if(this.dead) {
			if((mc.thePlayer != null && mc.thePlayer.ticksExisted < 30 && mc.thePlayer.ticksExisted > 10)) {
				this.dead = false;
				Jigsaw.chatMessage("STOP");
			}
		}
		if(mc.thePlayer.getHealth() <= 3 && !dead) {
			start();
			//Jigsaw.chatMessage("lol2");
		}
		if(dead) {
			//Jigsaw.chatMessage("lol");
			mc.thePlayer.setPosition(mc.thePlayer.posX, posY + 0.1, mc.thePlayer.posZ);
			//mc.thePlayer.motionY = 0.01;
			sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, posY + 0.1, mc.thePlayer.posZ, true));
			sendPacket(new C03PacketPlayer());
			sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, posY + 0.3, mc.thePlayer.posZ, true));
		}
//		if(dead) {
//			mc.thePlayer.motionY = -0.05;
//			sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1, mc.thePlayer.posZ, true));
//		}
		super.onUpdate();
	}
	
	@Override
	public void onDeath() {
		super.onDeath();
	}
	

}
