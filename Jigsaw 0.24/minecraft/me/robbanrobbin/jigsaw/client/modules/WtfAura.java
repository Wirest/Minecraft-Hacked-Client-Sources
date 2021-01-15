package me.robbanrobbin.jigsaw.client.modules;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.modules.target.AuraUtils;
import me.robbanrobbin.jigsaw.client.tools.RenderTools;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class WtfAura extends Module {

	double x;
	double y;
	double z;
	
	EntityLivingBase en = null;
	
	ArrayList<Vec3> positions = new ArrayList<Vec3>();
	ArrayList<Vec3> positionsBack = new ArrayList<Vec3>();
	
	public WtfAura() {
		super("WtfAura", Keyboard.KEY_NONE, Category.COMBAT,
				"testing");
	}

	@Override
	public void onDisable() {
		positions.clear();
		positionsBack.clear();
		super.onDisable();
	}

	@Override
	public void onEnable() {
		sendPacket(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
		en = Utils.getClosestEntity(7);
		double range = mc.thePlayer.getDistanceToEntity(en);
		double maxTP = 0.31;
		double step = maxTP / range;
		int steps = 0;
		int ind = 0;
		for (int i = 0; i < 100; i++) {
			steps++;
			if (maxTP * steps > range) {
				break;
			}
		}
		double angleA = Math.toRadians(Math.toDegrees(Math.atan2(mc.thePlayer.posX - en.posX, mc.thePlayer.posZ - en.posZ)) + 180);
		for (int i = 0; i < steps; i++) {
			ind++;
			double difX = mc.thePlayer.posX - en.posX;
			double difZ = mc.thePlayer.posZ - en.posZ;
			double divider = step * i;
			x = mc.thePlayer.posX - difX * divider;
			y = mc.thePlayer.posY;
			z = mc.thePlayer.posZ - difZ * divider;
			difX = x - en.posX;
			difZ = z - en.posZ;
			double dist = Math.sqrt(difX * difX + difZ * difZ);
			if(dist < 3) {
				break;
			}
			sendPacket(false);
		}
		mc.thePlayer.swingItem();
		Criticals.crit(x, y, z);
		Criticals.disable = true;
		sendPacket(new C02PacketUseEntity(en, Action.ATTACK));
		Criticals.disable = false;
		// Go back!
		for (int i = positions.size() - 2; i > -1; i--) {
			{
				x = positions.get(i).xCoord;
				y = positions.get(i).yCoord;
				z = positions.get(i).zCoord;
			}
			sendPacket(true);
		}
		sendPacket(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
		super.onEnable();
	}
	
	@Override
	public void onRender() {
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(GL11.GL_BLEND);
		RenderTools.lineWidth(2);
		RenderTools.color4f(0.3f, 1f, 0.3f, 1f);
		RenderTools.glBegin(3);
		int i = 0;
		for (Vec3 vec : positions) {
			RenderTools.putVertex3d(RenderTools.getRenderPos(vec.xCoord, vec.yCoord, vec.zCoord));
			i++;
		}
		RenderTools.glEnd();
		RenderTools.color4f(0.3f, 0.3f, 1f, 1f);
		RenderTools.glBegin(3);
		i = 0;
		for (Vec3 vec : positionsBack) {
			RenderTools.putVertex3d(RenderTools.getRenderPos(vec.xCoord, vec.yCoord, vec.zCoord));
			i++;
		}
		RenderTools.glEnd();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glPopMatrix();
		RenderTools.lineWidth(3);
		for (Vec3 vec : positions) {
			drawESP(1f, 0.3f, 0.3f, 1f, vec.xCoord, vec.yCoord, vec.zCoord);
		}
		RenderTools.lineWidth(1.5f);
		for (Vec3 vec : positionsBack) {
			drawESP(0.3f, 0.3f, 1f, 1f, vec.xCoord, vec.yCoord, vec.zCoord);
		}
		super.onRender();
	}
	
	@Override
	public void onUpdate() {
		
		super.onUpdate();
	}
	
	public void sendPacket(boolean goingBack) {
		C04PacketPlayerPosition playerPacket = new C04PacketPlayerPosition(x, y, z, true);
		sendPacketFinal(playerPacket);
		if (goingBack) {
			positionsBack.add(new Vec3(x, y, z));
			return;
		}
		positions.add(new Vec3(x, y, z));
	}
	
	public void drawESP(float red, float green, float blue, float alpha, double x, double y, double z) {
		double xPos = x - mc.getRenderManager().renderPosX;
		double yPos = y - mc.getRenderManager().renderPosY;
		double zPos = z - mc.getRenderManager().renderPosZ;
		RenderTools.drawOutlinedEntityESP(xPos, yPos, zPos, mc.thePlayer.width / 2, mc.thePlayer.height, red, green,
				blue, alpha);
	}
	
}
