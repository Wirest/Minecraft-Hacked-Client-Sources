package me.robbanrobbin.jigsaw.client.modules;

import me.robbanrobbin.jigsaw.client.events.EntityHitEvent;
import me.robbanrobbin.jigsaw.client.events.UpdateEvent;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.modules.target.AuraUtils;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.Vec3;

public class TpGodMode extends Module {
	
	private boolean tped;

	public TpGodMode() {
		super("TpGodMode", 0, Category.HIDDEN, "Makes you invisible and invincible");
	}

	@Override
	public void onUpdate(UpdateEvent event) {
		if(mc.thePlayer.onGround) {
			if(tped) {
				Utils.blinkToPosFromPos(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + 500, mc.thePlayer.posZ), new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ), 9.9);
			}
			Utils.blinkToPosFromPos(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ), new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + 500, mc.thePlayer.posZ), 9.9);
			tped = true;
			event.cancel();
		}
		else {
			sendPacket(new C03PacketPlayer(true));
			if(tped) {
				Utils.blinkToPosFromPos(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + 500, mc.thePlayer.posZ), new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ), 9.9);
			}
			
			tped = false;
		}
		super.onUpdate(event);
	}
	
	@Override
	public void onEntityHit(EntityHitEvent entityHitEvent) {
		Utils.blinkToPosFromPos(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + 500, mc.thePlayer.posZ), new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ), 9.9);
		tped = false;
		super.onEntityHit(entityHitEvent);
	}

	@Override
	public void onDisable() {
		Utils.blinkToPosFromPos(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + 500, mc.thePlayer.posZ), new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ), 9.9);
		tped = false;
		super.onDisable();
	}
	
	@Override
	public void onPreLateUpdate() {
		super.onPreLateUpdate();
	}
	
	@Override
	public void onEnable() {
		tped = false;
		super.onEnable();
	}
}
