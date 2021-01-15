// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.hero.settings.Setting;

import com.darkmagician6.eventapi.EventTarget;

import me.CheerioFX.FusionX.FusionX;
import me.CheerioFX.FusionX.GUI.clickgui.Restrictions;
import me.CheerioFX.FusionX.events.EventPostMotionUpdates;
import me.CheerioFX.FusionX.events.EventPreMotionUpdates;
import me.CheerioFX.FusionX.module.Category;
import me.CheerioFX.FusionX.module.Module;
import me.CheerioFX.FusionX.utils.EntityUtils;
import me.CheerioFX.FusionX.utils.RotationUtils;
import me.CheerioFX.FusionX.utils.TimeHelper2;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemSword;

public class KillAura extends Module {
	private TimeHelper2 timer;
	private EntityLivingBase target;

	public KillAura() {
		super("KillAura", 19, Category.COMBAT);
	}

	@Override
	public void onEnable() {
		this.timer = new TimeHelper2();
		super.onEnable();
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}

	@Override
	public void setup() {
		FusionX.theClient.setmgr.rSetting(new Setting("Reach", this, 4.39, 1.0, 6.5, false));
		FusionX.theClient.setmgr.rSetting(new Setting("CPS", this, 13.0, 1.0, 20.0, false));
		FusionX.theClient.setmgr.rSetting(new Setting("BlockHit", this, true));
		FusionX.theClient.setmgr.rSetting(new Setting("LockView", this, false));
		FusionX.theClient.setmgr.rSetting(new Setting("Multi-Target", this, false));
	}

	private double getReach() {
		return FusionX.theClient.setmgr.getSetting(this, "reach").getValDouble();
	}

	private double getCPS() {
		final double cps = FusionX.theClient.setmgr.getSetting(this, "cps").getValDouble();
		final double max = cps + 0.7234567925344132;
		final double min = cps - 1.323469123648;
		final Random rand = new Random();
		final double random = ThreadLocalRandom.current().nextDouble(min, max);
		return random;
	}

	private boolean isbHit() {
		return FusionX.theClient.setmgr.getSetting(this, "blockhit").getValBoolean();
	}

	private boolean isLockView() {
		return FusionX.theClient.setmgr.getSetting(this, "lockview").getValBoolean();
	}

	private boolean isMultiTarget() {
		return FusionX.theClient.setmgr.getSetting(this, "multi-target").getValBoolean();
	}

	@EventTarget
	public void onPreMotionUpdate(final EventPreMotionUpdates event) {
		if (FusionX.theClient.moduleManager.getModuleByName("Flight").getState()
				&& (Flight.getMode() == "Cubecraft-Fast" || Flight.getMode() == "Cubecraft-Slow")) {
			this.setState(false);
		}
		if (!this.isMultiTarget()) {
			this.target = EntityUtils.getClosestEntity();
			if (this.target != null && KillAura.mc.thePlayer.getDistanceToEntity(this.target) <= this.getReach()) {
				event.onGround = true;
				if (this.isLockView()) {
					KillAura.mc.thePlayer.rotationYaw = RotationUtils.getRotations(this.target)[0];
					KillAura.mc.thePlayer.rotationPitch = RotationUtils.getRotations(this.target)[1];
				} else {
					event.yaw = RotationUtils.getRotations(this.target)[0];
					event.pitch = RotationUtils.getRotations(this.target)[1];
				}
			} else {
				this.target = null;
			}
		} else {
			if (!this.timer.hasPassed(1000.0 / this.getCPS())) {
				return;
			}
			final ArrayList<EntityLivingBase> entities = EntityUtils.getValidEntities();
			for (EntityLivingBase entity : entities) {
				if (entity != null && KillAura.mc.thePlayer.getDistanceToEntity(entity) <= this.getReach()) {
					event.onGround = true;
					event.yaw = RotationUtils.getRotations(entity)[0];
					event.pitch = RotationUtils.getRotations(entity)[1];
					EntityUtils.attackEntity(entity,
							FusionX.theClient.moduleManager.getModuleByName("criticals").getState());
				} else {
					entity = null;
				}
			}
			this.timer.reset();
		}
	}

	@EventTarget
	public void onPostMotionUpdate(final EventPostMotionUpdates event) {
		if (!this.isMultiTarget()) {
			if (this.target != null) {
				if (KillAura.mc.thePlayer.getHeldItem() != null
						&& KillAura.mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && this.isbHit()) {
					KillAura.mc.thePlayer.setItemInUse(KillAura.mc.thePlayer.getHeldItem(),
							KillAura.mc.thePlayer.getHeldItem().getMaxItemUseDuration());
				}
				if (this.timer.hasPassed(1000.0 / this.getCPS())
						&& KillAura.mc.thePlayer.canEntityBeSeen(this.target)) {
					EntityUtils.attackEntity(this.target,
							FusionX.theClient.moduleManager.getModuleByName("criticals").getState());
					this.timer.reset();
				}
			}
		} else if (EntityUtils.getClosestEntity() != null
				&& KillAura.mc.thePlayer.getDistanceToEntity(EntityUtils.getClosestEntity()) <= this.getReach()
				&& KillAura.mc.thePlayer.getHeldItem() != null
				&& KillAura.mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && this.isbHit()) {
			KillAura.mc.thePlayer.setItemInUse(KillAura.mc.thePlayer.getHeldItem(),
					KillAura.mc.thePlayer.getHeldItem().getMaxItemUseDuration());
		}
	}

	@Override
	public void onUpdate() {
		if (!Restrictions.gcheat()) {
			EntityUtils.getClosestEntity();
		}
		if (this.getState()) {
			if (!this.isMultiTarget()) {
				this.setExtraInfo("- Switch");
			} else {
				this.setExtraInfo("- Multi");
			}
		}
		super.onUpdate();
	}
}
