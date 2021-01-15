package me.xatzdevelopments.xatz.client.modules;

import java.util.ArrayList;
import java.util.Comparator;

import org.lwjgl.input.Keyboard;

import me.xatzdevelopments.xatz.client.events.UpdateEvent;
import me.xatzdevelopments.xatz.client.main.Xatz;
import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.client.modules.target.AuraUtils;
import me.xatzdevelopments.xatz.client.settings.ClientSettings;
import me.xatzdevelopments.xatz.client.tools.Utils;
import me.xatzdevelopments.xatz.gui.custom.clickgui.ModSetting;
import me.xatzdevelopments.xatz.gui.custom.clickgui.SliderSetting;
import me.xatzdevelopments.xatz.gui.custom.clickgui.ValueFormat;
import me.xatzdevelopments.xatz.module.Module;
import me.xatzdevelopments.xatz.utils.KillauraUtil;
import me.xatzdevelopments.xatz.utils.WaitTimer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.potion.Potion;
import net.minecraft.util.Vec3;

public class KillAura extends Module {
	public static WaitTimer timer = new WaitTimer();
	public static EntityLivingBase en = null;
	static ArrayList<EntityLivingBase> ens = new ArrayList<EntityLivingBase>();
	boolean attack = false;
	/**
	 * For "Switch" mode
	 */
	private ArrayList<EntityLivingBase> lastEns = new ArrayList<EntityLivingBase>();
	public static boolean walls = true;

	double x;
	double y;
	double z;

	float rotationYaw = 0;
    float rotationPitch = 0;
	
	ArrayList<Vec3> positions = new ArrayList<Vec3>();
	ArrayList<Vec3> positionsBack = new ArrayList<Vec3>();
	
	public static EntityLivingBase getCurrentTarget() {
		 return en;
	}

	@Override
	public void onEnable() {
		super.onEnable();
	}

	@Override
	public ModSetting[] getModSettings() {
		// killaruarangeslider

//		final Slider killaruarangeslider = new BasicSlider("Aura Range", ClientSettings.KillauraRange, 3.5, 7.0, 0.0,
//				ValueDisplay.DECIMAL);
//
//		killaruarangeslider.addSliderListener(new SliderListener() {
//
//			@Override
//			public void onSliderValueChanged(Slider slider) {
//
//				ClientSettings.KillauraRange = slider.getValue();
//			}
//		});
//		// killaruaspeedslider
//
//		Slider killaruaspeedslider = new BasicSlider("Aura APS", ClientSettings.KillauraAPS, 1.0, 20.0, 0.0,
//				ValueDisplay.INTEGER);
//		killaruaspeedslider.addSliderListener(new SliderListener() {
//
//			@Override
//			public void onSliderValueChanged(Slider slider) {
//
//				ClientSettings.KillauraAPS = (int) Math.round(slider.getValue());
//
//			}
//		});
		SliderSetting<Number> killaruarangeslider = new SliderSetting<Number>("Hit Range", ClientSettings.KillauraRange, 3.5, 7.0, 0.0, ValueFormat.DECIMAL);
		SliderSetting<Number> killaruaspeedslider = new SliderSetting<Number>("Hits Per Second", ClientSettings.KillauraAPS, 1.0, 20.0, 0.0, ValueFormat.DECIMAL);
		return new ModSetting[] { killaruarangeslider, killaruaspeedslider };
	}

	public KillAura() {
		super("KillAura", Keyboard.KEY_NONE, Category.COMBAT, "Automatically attacks entities in range.");
	}

	@Override
	public void onToggle() {
		en = null;
		lastEns.clear();
		ens.clear();
		AuraUtils.targets.clear();
		positions.clear();
		positionsBack.clear();
		super.onToggle();
	}

	@Override
	public void onUpdate(UpdateEvent event) {
		attack = false;
		//setRotations();
		positions.clear();
		positionsBack.clear();
		if (en != null && !event.autopot) {
			if ((this.currentMode.equals("Single") || this.currentMode.equals("Switch"))) {
				float[] rots = Utils.getFacePosEntity(en);
				event.yaw = rots[0];
				event.pitch = rots[1];
				mc.thePlayer.renderYawOffset = event.getYaw();
				mc.thePlayer.rotationYawHead = event.getYaw();
				 // event.setYaw(rotationYaw);
					//event.setPitch(rotationPitch);
			       //mc.thePlayer.rotationYawHead = rotationYaw;
			       // mc.thePlayer.rotationPitch = rotationPitch;
			}
		}
		AuraUtils.targets.clear();
		if (!AuraUtils.hasEntity(en)) {
			AuraUtils.targets.add(en);
		}
		if (!timer.hasTimeElapsed(1000 / ClientSettings.KillauraAPS, false)) {
			return;
		}
		if (this.currentMode.equals("Single")) {
			EntityLivingBase newEn = null;
			newEn = Utils.getClosestEntity((float) ClientSettings.KillauraRange);
			if (newEn == null) {							
				en = null;
				return;
			}
			if (mc.thePlayer.getDistanceToEntity(newEn) > ClientSettings.KillauraRange) {
				newEn = null;
				en = null;
				attack = false;
				return;
			}
			en = newEn;
			if (en == null) {
				return;
			}
			float[] rots = Utils.getFacePosEntity(en);
			if (!event.autopot) {
				event.yaw = rots[0];
				event.pitch = rots[1];
				mc.thePlayer.renderYawOffset = event.getYaw();
				mc.thePlayer.rotationYawHead = event.getYaw();
			}
			attack = true;
		}
		if (this.currentMode.equals("Multi")) {
			ens = Utils.getClosestEntities((float) ClientSettings.KillauraRange);
			if (ens.isEmpty()) {
				return;
			}
			for (EntityLivingBase en : ens) {
				float[] rots = Utils.getFacePosEntity(en);
				event.yaw = rots[0];
				event.pitch = rots[1];
			}
			AuraUtils.targets = ens;
			attack = true;
		}
		if (this.currentMode.equals("Switch")) {
			ens = Utils.getClosestEntities((float) ClientSettings.KillauraRange);
			if (Xatz.java8) {
				ens.sort(new Comparator<EntityLivingBase>() {
					public int compare(EntityLivingBase o1, EntityLivingBase o2) {
						if (mc.thePlayer.getDistanceToEntity(o1) > mc.thePlayer.getDistanceToEntity(o2)) {
							return 1;
						}
						if (mc.thePlayer.getDistanceToEntity(o1) < mc.thePlayer.getDistanceToEntity(o2)) {
							return -1;
						}
						if (mc.thePlayer.getDistanceToEntity(o1) == mc.thePlayer.getDistanceToEntity(o2)) {
							return 0;
						}
						return 0;
					};
				});
			}
			boolean choseEntity = false;
			for (EntityLivingBase en : ens) {
				if (!attackedEntity(en)) {
					this.en = en;
					choseEntity = true;
					lastEns.add(en);
					attack = true;
					float[] rots = Utils.getFacePosEntity(en);
					event.yaw = rots[0];
					event.pitch = rots[1];
					break;
				} else {
					continue;
				}
			}
			if (!choseEntity) {
				en = Utils.getClosestEntity((float) ClientSettings.KillauraRange);
				if (en == null) {
					return;
				}
				attack = true;
				lastEns.clear();
			}
		}
		if (attack) {
			timer.reset();
		}
		super.onUpdate();
	}
	
	 public void setRotations() {
	        if (en == null)
	            return;
	        float[] smoothRotations = aurautil.faceEntitySmooth(rotationYaw, rotationPitch, aurautil.rotations(en)[0], aurautil.rotations(en)[1], 90, 90);
	        rotationYaw = aurautil.updateRotation(mc.thePlayer.rotationYaw, smoothRotations[0], 180);
	        rotationPitch = smoothRotations[1];
	        if(rotationPitch > 90) {
	            rotationPitch = 90;
	        } else if (rotationPitch < -90) {
	            rotationPitch = -90;
	        }
	    }
	 
	 KillauraUtil aurautil = new KillauraUtil();

	@Override
	public void onLateUpdate() {
		if (!attack) {
			return;
		}
		attack = false;
		if (this.currentMode.equals("Multi")) {
			AutoBlock.stopBlock();
			for (EntityLivingBase en : ens) {
				mc.thePlayer.swingItem();
				sendPacket(new C02PacketUseEntity(en, Action.ATTACK));
				float sharpLevel = EnchantmentHelper.func_152377_a(mc.thePlayer.getHeldItem(),
						en.getCreatureAttribute());
				boolean vanillaCrit = (mc.thePlayer.fallDistance > 0.0F) && (!mc.thePlayer.onGround)
						&& (!mc.thePlayer.isOnLadder()) && (!mc.thePlayer.isInWater())
						&& (!mc.thePlayer.isPotionActive(Potion.blindness)) && (mc.thePlayer.ridingEntity == null);
				if ((Xatz.getModuleByName("Criticals").isToggled()) || (vanillaCrit)) {
					mc.thePlayer.onCriticalHit(en);
				}
				if (sharpLevel > 0.0F) {
					mc.thePlayer.onEnchantmentCritical(en);
				}
			}
			AutoBlock.startBlock();
		} else {
			AutoBlock.stopBlock();
			mc.thePlayer.swingItem();
			sendPacket(new C02PacketUseEntity(en, Action.ATTACK));
			AutoBlock.startBlock();
			float sharpLevel = EnchantmentHelper.func_152377_a(mc.thePlayer.getHeldItem(),
					en.getCreatureAttribute());
			boolean vanillaCrit = (mc.thePlayer.fallDistance > 0.0F) && (!mc.thePlayer.onGround)
					&& (!mc.thePlayer.isOnLadder()) && (!mc.thePlayer.isInWater())
					&& (!mc.thePlayer.isPotionActive(Potion.blindness)) && (mc.thePlayer.ridingEntity == null);
			if ((Xatz.getModuleByName("Criticals").isToggled()) || (vanillaCrit)) {
				mc.thePlayer.onCriticalHit(en);
			}
			if (sharpLevel > 0.0F) {
				mc.thePlayer.onEnchantmentCritical(en);
			}
		}
		super.onLateUpdate();
	}

	public boolean attackedEntity(EntityLivingBase en) {
		for (EntityLivingBase entity : lastEns) {
			if (entity.isEntityEqual(en)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String[] getModes() {

		return new String[] { "Single", "Switch", "Multi" };
	}
	
	public String getModeName() {
		return "Mode: ";
	}

	@Override
	protected void onModeChanged(String modeBefore, String newMode) {
		en = null;
		ens.clear();
		attack = false;
		super.onModeChanged(modeBefore, newMode);
	}

	// public static boolean doBlock() {
	// return en != null || !ens.isEmpty();
	// }
	//
	public static boolean doBlock() {
		return en != null || (Xatz.getModuleByName("KillAura").getCurrentMode().equals("Multi") && !ens.isEmpty());
	}

	public static boolean getShouldChangePackets() {
		return en != null && !Xatz.getModuleByName("KillAura").getCurrentMode().equals("Multi")
				&& !ClientSettings.KillauraHeadsnap;
	}

}
