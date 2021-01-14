package store.shadowclient.client.module.combat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.clickgui.settings.Setting;
import store.shadowclient.client.event.EventManager;
import store.shadowclient.client.event.EventTarget;
import store.shadowclient.client.event.events.Event3D;
import store.shadowclient.client.event.events.EventPreMotionUpdate;
import store.shadowclient.client.event.events.EventSendPacket;
import store.shadowclient.client.event.events.EventUpdate;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;
import store.shadowclient.client.utils.TimeHelper;
import store.shadowclient.client.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.MathHelper;

public class Aura extends Module {
	private float delay;
	public static float range;
	public static EntityLivingBase ThisIsTheEntityThatThePlayerIsHittingTo;
	public boolean IsHittingEntityFakeBoolean = false;
	private TimeHelper timeHelper = new TimeHelper();
	public float yaw, pitch;

	public Aura() {
		super("Aura", Keyboard.KEY_R, Category.COMBAT);

		ArrayList<String> options = new ArrayList<>();
        options.add("Normal");
        Shadow.instance.settingsManager.rSetting(new Setting("CircleESP Mode", this, "Normal", options));
        Shadow.instance.settingsManager.rSetting(new Setting("Delay", this, 8.0, 1, 100.0, false));
        Shadow.instance.settingsManager.rSetting(new Setting("Range", this, 4.0, 1.0, 6.0, false));
        Shadow.instance.settingsManager.rSetting(new Setting("CircleESP Range", this, 1.80, 1.0, 6.0, false));
        Shadow.instance.settingsManager.rSetting(new Setting("Aura Min Speed", this, 90.0, 0, 180.0, false));
        Shadow.instance.settingsManager.rSetting(new Setting("Aura Max Speed", this, 180.0, 0, 180.0, false));
		Shadow.instance.settingsManager.rSetting(new Setting("AuraCircleESP", this, true));
		Shadow.instance.settingsManager.rSetting(new Setting("Animals", this, false));
		Shadow.instance.settingsManager.rSetting(new Setting("AutoBlock", this, true));
        Shadow.instance.settingsManager.rSetting(new Setting("Invisibles", this, false));
        Shadow.instance.settingsManager.rSetting(new Setting("Monsters", this, false));
        Shadow.instance.settingsManager.rSetting(new Setting("Players", this, true));
        Shadow.instance.settingsManager.rSetting(new Setting("Teams", this, false));
        Shadow.instance.settingsManager.rSetting(new Setting("Villagers", this, false));
        Shadow.instance.settingsManager.rSetting(new Setting("Rotations", this, true));
        Shadow.instance.settingsManager.rSetting(new Setting("Aimlock", this, false));
        Shadow.instance.settingsManager.rSetting(new Setting("Predict", this, true));
        Shadow.instance.settingsManager.rSetting(new Setting("TargetHUD", this, true));

	}

	@EventTarget
	public void onEvent(EventUpdate e) {
		delay = ((float) Shadow.instance.settingsManager.getSettingByName("Delay").getValDouble());
		range = ((float) Shadow.instance.settingsManager.getSettingByName("Range").getValDouble());
	}

	@EventTarget
	public void onTick(EventPreMotionUpdate event) {
		for (Object o : mc.theWorld.loadedEntityList) {
			if (o instanceof EntityLivingBase) {
				if (o != mc.thePlayer) {
					EntityLivingBase e = (EntityLivingBase) o;
					
					boolean block = check(e) && Shadow.instance.settingsManager.getSettingByName("AutoBlock").getValBoolean() && mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword;
					
					if(block && e.getDistanceToEntity(mc.thePlayer) < range) {
			        	this.mc.thePlayer.setItemInUse(this.mc.thePlayer.getHeldItem(), this.mc.thePlayer.getHeldItem().getMaxItemUseDuration());
					}
					
					if (check(e) && (timeHelper.isDelayComplete(1000 / delay))) {
						attack(e);
						ThisIsTheEntityThatThePlayerIsHittingTo = e;
					}
				}
			}
		}
		if(ThisIsTheEntityThatThePlayerIsHittingTo == null) {
			return;
		}
		final float[] rotationsCurrent = new float[] { Shadow.ROTATION_UTIL.serverYaw, Shadow.ROTATION_UTIL.serverPitch };
		final float[] rotationsInstant = Shadow.ROTATION_UTIL.getRotations(ThisIsTheEntityThatThePlayerIsHittingTo, Shadow.instance.settingsManager.getSettingByName("Predict").getValBoolean(), 1);
		final float[] rotationsSmooth = Shadow.ROTATION_UTIL.smoothRotation(rotationsCurrent, rotationsInstant, calculateRotationSpeed() + (int) (ThreadLocalRandom.current().nextInt(5)));
		event.setPitch(rotationsSmooth[1]);
		event.setYaw(rotationsSmooth[0]);
		/*this.yaw = rot[0];
		this.pitch = rot[1];*/
		this.IsHittingEntityFakeBoolean = true;
	}
	private boolean check(EntityLivingBase e) {
		if (e.isInvisible())
			if(Shadow.instance.settingsManager.getSettingByName("Invisibles").getValBoolean()) {
				return true;
			} else {
				return false;
			}
		if (e instanceof EntityPlayer)
			if(Shadow.instance.settingsManager.getSettingByName("Players").getValBoolean()) {
				return true;
			} else {
				return false;
			}
		if (e instanceof EntityAnimal)
			if(Shadow.instance.settingsManager.getSettingByName("Animals").getValBoolean()) {
				return true;
			} else {
				return false;
			}
		if (e instanceof EntityMob)
			if(Shadow.instance.settingsManager.getSettingByName("Monsters").getValBoolean()) {
				return true;
			} else {
				return false;
			}
		if (e instanceof EntityVillager)
			if(Shadow.instance.settingsManager.getSettingByName("Villagers").getValBoolean()) {
				return true;
			} else {
				return false;
			}
		if(e.isOnSameTeam(mc.thePlayer) && Shadow.instance.settingsManager.getSettingByName("Teams").getValBoolean())
            return false;
		/*if (e.getHealth() <= 0)
			if(Shadow.instance.settingsManager.getSettingByName("AfterDeath").getValBoolean()) {
				return true;
			} else {
				return false;
			}*/
		if (e.ticksExisted > 0)
			return false;
		if (e.getEntityId() > 1070000000)
			return false;
		return true;
	}
	
	private void attack(EntityLivingBase e) {
		if (e.getDistanceToEntity(mc.thePlayer) <= range) {
			if (IsHittingEntityFakeBoolean) {
				mc.thePlayer.swingItem();
				mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(e, C02PacketUseEntity.Action.ATTACK));
				timeHelper.setLastMS();
				ThisIsTheEntityThatThePlayerIsHittingTo = null;
				IsHittingEntityFakeBoolean = false;
			}
		}
	}
	
	public static float[] getRotationsNeeded(Entity entity) {
		float yaw;
		float pitch;
		double diffX = entity.posX - Minecraft.getMinecraft().thePlayer.posX;
		double diffY;
		if ((entity instanceof EntityLivingBase)) {
			EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
			diffY = entityLivingBase.posY + entityLivingBase.getEyeHeight() * 0.9D - (Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight());
		} else {
			diffY = (entity.boundingBox.minY + entity.boundingBox.maxY) / 2.0D - (Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight());
		}
		double diffZ = entity.posZ - Minecraft.getMinecraft().thePlayer.posZ;
		double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
		yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / 3.141592653589793D) - 90.0F;
		pitch = (float) -(Math.atan2(diffY, dist) * 180.0D / 3.141592653589793D);
		return new float[] {
				Minecraft.getMinecraft().thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - Minecraft.getMinecraft().thePlayer.rotationYaw), Minecraft.getMinecraft().thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - Minecraft.getMinecraft().thePlayer.rotationPitch) 
		};
	}
	@EventTarget
	public void onSendPacket(EventSendPacket event) {
		Packet packet = event.getPacket();
		if(ThisIsTheEntityThatThePlayerIsHittingTo == null) {
			return;
		}
		if(ThisIsTheEntityThatThePlayerIsHittingTo.getDistanceToEntity(mc.thePlayer) <= range) {
			final float[] rotationsCurrent = new float[] { Shadow.ROTATION_UTIL.serverYaw, Shadow.ROTATION_UTIL.serverPitch };
			final float[] rotationsInstant = Shadow.ROTATION_UTIL.getRotations(ThisIsTheEntityThatThePlayerIsHittingTo, Shadow.instance.settingsManager.getSettingByName("Predict").getValBoolean(), 1);
			final float[] rotationsSmooth = Shadow.ROTATION_UTIL.smoothRotation(rotationsCurrent, rotationsInstant, calculateRotationSpeed() + (int) (ThreadLocalRandom.current().nextInt(5)));
			
			if(packet instanceof C03PacketPlayer)
	            if(Shadow.instance.settingsManager.getSettingByName("Rotations").getValBoolean()) {
					final float yaw = rotationsSmooth[0];
					final float pitch = rotationsSmooth[1];
					//Shadow.ROTATION_UTIL.setRotations(yaw, pitch);
					if (!Shadow.instance.settingsManager.getSettingByName("Aimlock").getValBoolean()) {
						Shadow.ROTATION_UTIL.setRotations(yaw, pitch);
						event.setRotation(yaw, pitch);
					} else {
						Shadow.ROTATION_UTIL.setRotations(yaw, pitch);
						getMc().thePlayer.setRotations(yaw, pitch);
					}

					getMc().thePlayer.setHeadRotations(yaw, pitch);
					getMc().thePlayer.renderYawOffset = yaw;

					if (ThisIsTheEntityThatThePlayerIsHittingTo == null)
						return;

					getMc().thePlayer.setHeadRotations(yaw, pitch);
			}
		}
	}
	
	@EventTarget
	public void onRender3D(Event3D event) {
		if(this.isToggled()) {
			if(Aura.ThisIsTheEntityThatThePlayerIsHittingTo == null) {
				return;
			}
			if(Shadow.instance.settingsManager.getSettingByName("AuraCircleESP").getValBoolean() && !Shadow.instance.settingsManager.getSettingByName("CircleESP").getValBoolean()) {
				if(Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getDistanceToEntity(mc.thePlayer) < Shadow.instance.settingsManager.getSettingByName("Range").getValDouble()) {
					RenderUtils.drawCircle(Aura.ThisIsTheEntityThatThePlayerIsHittingTo, event.getPartialTicks(), Shadow.instance.settingsManager.getSettingByName("CircleESP Range").getValDouble() + 0.002, novoline(300));
					RenderUtils.drawCircle(Aura.ThisIsTheEntityThatThePlayerIsHittingTo, event.getPartialTicks(), Shadow.instance.settingsManager.getSettingByName("CircleESP Range").getValDouble() + 0.006, novoline(300));
					RenderUtils.drawCircle(Aura.ThisIsTheEntityThatThePlayerIsHittingTo, event.getPartialTicks(), Shadow.instance.settingsManager.getSettingByName("CircleESP Range").getValDouble(), novoline(300));
				}
			}
		}
	}
	
	private float calculateRotationSpeed() {
		if (Shadow.instance.settingsManager.getSettingByName("Aura Min Speed").getValDouble() == Shadow.instance.settingsManager.getSettingByName("Aura Max Speed").getValDouble()) {
			return 180;
		}
		return (float) ThreadLocalRandom.current().nextDouble(Shadow.instance.settingsManager.getSettingByName("Aura Min Speed").getValDouble(), Shadow.instance.settingsManager.getSettingByName("Aura Max Speed").getValDouble());
	}
	
	public static void drawCircleESP(Entity entity, float partialTicks, double rad) {
		String mode = Shadow.instance.settingsManager.getSettingByName("CircleESP Mode").getValString();
        GL11.glPushMatrix();
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glEnable(2881);
        GL11.glEnable(2832);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        GL11.glHint(3153, 4354);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glLineWidth(1.0F);
        GL11.glBegin(3);
        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks - Minecraft.getMinecraft().getRenderManager().viewerPosX;
        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks - Minecraft.getMinecraft().getRenderManager().viewerPosY;
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks - Minecraft.getMinecraft().getRenderManager().viewerPosZ;
        float r = 0.003921569F * Color.WHITE.getRed();
        float g = 0.003921569F * Color.WHITE.getGreen();
        float b = 0.003921569F * Color.WHITE.getBlue();
        double pix2 = 6.283185307179586D;
        int[] counter = {1};
        for (int i = 0; i <= 120; i++) {
            GL11.glColor4f(255, 255, 255, 255);
            if (mode.equalsIgnoreCase("Normal")) {
            	GL11.glVertex3d(x + rad * Math.cos(i * 6.283185307179586D / 45.0D), y, z + rad * Math.sin(i * 6.283185307179586D / 45.0D));
            } 
            counter[0] -= 1;
        }
        GL11.glEnd();
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        GL11.glEnable(2848);
        GL11.glEnable(2881);
        GL11.glEnable(2832);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        GL11.glHint(3153, 4354);
        GL11.glEnable(3553);
        GL11.glPopMatrix();
    }
	
	public static int novoline(int delay) {
	      double novolineState = Math.ceil((System.currentTimeMillis() + delay) / 10.0);
	      novolineState %= 360;
	      return Color.getHSBColor((float) (novolineState / 180.0f), 0.3f, 1.0f).getRGB();
	}
	
	public final boolean isTeamMate(EntityLivingBase entity) {
		if (!(entity instanceof EntityPlayer))
			return false;
		if (mc.thePlayer.getTeam() != null && entity.getTeam() != null) {
			if (mc.thePlayer.isOnSameTeam(entity)) {
				return true;
			}
		}

		if (mc.thePlayer.getDisplayName() != null && entity.getDisplayName() != null) {
			final String playerName = mc.thePlayer.getDisplayName().getFormattedText().replace("§r", "");
			final String entityName = entity.getDisplayName().getFormattedText().replace("§r", "");
			if (playerName.isEmpty() || entityName.isEmpty())
				return false;
			return playerName.charAt(1) == entityName.charAt(1);
		}

		return false;
	}
	
	public void onEnable(){
		EventManager.register(this);
		TimeHelper.reset();
	}
	
	public void onDisable(){
		EventManager.unregister(this);
		ThisIsTheEntityThatThePlayerIsHittingTo = null;
		IsHittingEntityFakeBoolean = false;
		TimeHelper.reset();
	}
}