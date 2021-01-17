package skyline.specc.mods.combat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;

import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.EventMotion;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.eventtypes.EventTick;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventListener;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventType;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModData;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModType;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.Module;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.value.BooleanValue;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.value.RestrictedValue;
import skyline.specc.SkyLine;
import skyline.specc.helper.loc.Loc;
import skyline.specc.utils.AimUtils;
import skyline.specc.utils.MathUtil;
import skyline.specc.utils.TimerUtils;

public class AimAssist extends Module {

	private List<EntityLivingBase> loaded = new ArrayList<>();
	public RestrictedValue<Double> reach = new RestrictedValue<Double>("Reach", 4.5d, 1d, 5d);
	public BooleanValue invis = new BooleanValue("Invis", true);
	private TimerUtils timer = new TimerUtils();
	private EntityLivingBase target;
	public int random;
	public AimAssist() {
		super(new ModData("AimAssist", 0, new Color(255, 255, 255)), ModType.COMBAT);
		addValue(invis);
	}

	@EventListener
	public void onMotion(EventMotion event) {
		target = this.getBestEntity();
		if (target == null)
			return;
		if (event.getType() == EventType.PRE) {
			double distance = 1.147483647E11;
			for (Object object : mc.theWorld.loadedEntityList) {
		if (object instanceof EntityLivingBase && object != null && object != p) {
			EntityLivingBase entity = (EntityLivingBase) object;
			float[] direction = AimUtils.getRotations(entity);
			float a3 = direction[0];
			float yawDif = getAngleDifference(a3, p.rotationYaw);
			float a4 = direction[0];
			float pitchDif = getAngleDifference(a4, p.rotationPitch);
			if (!entity.isDead && entity != null && entity != p) {
				if (p.getDistanceToEntity(entity) < KillAuraMod.reach.getValue() && entity.isEntityAlive()) {
					EntityLivingBase entitylivingbase2 = entity;
					if (entitylivingbase2 != p && yawDif < 360.0f
						&& distance > Math.sqrt(yawDif * yawDif + pitchDif * pitchDif)) {
						distance = Math.sqrt(yawDif * yawDif + pitchDif * pitchDif);
						this.target = entity;
						}
					}
				}
			}
		if (p.getDistanceToEntity(target) < reach.getValue()
			&& this.target != null && !this.target.isInvisible()
			&& target != p) {
			Loc loc = event.getLocation();
			float yawChange = AimUtils.getYawChangeToEntity(this.target);
			float pitchChange = AimUtils.getPitchChangeToEntity(this.target);
			Loc location2 = loc;
			location2.setYaw(p.rotationYaw + yawChange);
			Loc location3 = loc;
			location3.setPitch(p.rotationPitch + pitchChange);
			p.setLocation(loc);
			}
		}
	}
}
public static float getAngleDifference(final float a, final float b) {
	float dist = (a - b + 360.0f) % 360.0f;
	if (dist > 180.0f) {
		dist = 360.0f - dist;
	}
	return Math.abs(dist);
	}

	@EventListener
	public void Tick(EventTick e) {
		target = this.getBestEntity();
		if (mc.theWorld == null) {
			return;
		}
		if (p == null) {
			return;
		}

		this.random = MathUtil.getRandomInRange(2, 39);
		if (timer.hasReached((1000 / (15) + this.random)) && target != null && target.getDistanceToEntity(p) < 6) {
			mc.leftClickCounter = 0;
			p.swingItem();
			if (mc.objectMouseOver.entityHit != null) {
				p.sendQueue.addToSendQueue(
						new C02PacketUseEntity(mc.objectMouseOver.entityHit, C02PacketUseEntity.Action.ATTACK));
			}
			mc.playerController.isHittingBlock = false;
			timer.reset();
		}

	}
	
	public boolean isEntityInFov(final EntityLivingBase entity, double angle) {
		angle *= 0.5;
		final double angleDifference = MathUtil.getAngleDifference(p.rotationYaw, AimUtils.getRotations(entity)[0]);
		return (angleDifference > 0.0 && angleDifference < angle)
				|| (-angle < angleDifference && angleDifference < 0.0);
	}
	
	public boolean isValid(EntityLivingBase entity) {

		return (entity != null && entity.isEntityAlive() && this.isEntityInFov(entity, 360)
				&& entity != p
				&& entity instanceof EntityPlayer
				&& entity.getDistanceToEntity(p) <= 5.5
				&& (!entity.isInvisible() || this.invis.getValue()) && entity.ticksExisted > 20
				&& !SkyLine.getManagers().getFriendManager().hasFriend(entity.getName()));
	}


	public EntityLivingBase getBestEntity() {
		if (loaded != null) {
			loaded.clear();
		}
		for (Object object : mc.theWorld.loadedEntityList) {
			if (object instanceof EntityLivingBase) {
				EntityLivingBase e = (EntityLivingBase) object;
				if (isValid(e)) {
					loaded.add(e);
				}
			}
		}
		if (loaded.isEmpty()) {
			return null;
		}
		loaded.sort((o1, o2) -> {
			float[] rot1 = AimUtils.getRotations(o1);
			float[] rot2 = AimUtils.getRotations(o2);
			return (int) ((mc.thePlayer.rotationYaw - rot1[0]) % 360 - (mc.thePlayer.rotationYaw - rot2[0]) % 360);
		});
		return loaded.get(0);
	}
}
