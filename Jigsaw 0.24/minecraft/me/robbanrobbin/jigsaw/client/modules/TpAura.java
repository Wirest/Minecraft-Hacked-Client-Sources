package me.robbanrobbin.jigsaw.client.modules;

import java.util.ArrayList;
import java.util.Comparator;

import org.darkstorm.minecraft.gui.component.BoundedRangeComponent.ValueDisplay;
import org.darkstorm.minecraft.gui.component.Button;
import org.darkstorm.minecraft.gui.component.Component;
import org.darkstorm.minecraft.gui.component.Slider;
import org.darkstorm.minecraft.gui.component.basic.BasicCheckButton;
import org.darkstorm.minecraft.gui.component.basic.BasicSlider;
import org.darkstorm.minecraft.gui.listener.ButtonListener;
import org.darkstorm.minecraft.gui.listener.SliderListener;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import me.robbanrobbin.jigsaw.client.WaitTimer;
import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.modules.target.AuraUtils;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.client.tools.RenderTools;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.ModSetting;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.SliderSetting;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.ValueFormat;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.AbstractPacket;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class TpAura extends Module {
	WaitTimer timer = new WaitTimer();
	private static EntityLivingBase en = null;
	boolean attack = false;
	double x;
	double y;
	double z;
	double xPreEn;
	double yPreEn;
	double zPreEn;
	double xPre;
	double yPre;
	double zPre;
	int stage = 0;
	ArrayList<Vec3> positions = new ArrayList<Vec3>();
	ArrayList<Vec3> positionsBack = new ArrayList<Vec3>();
	public static final double maxXZTP = 9.5;
	public static final int maxYTP = 9;

	@Override
	public ModSetting[] getModSettings() {
//		final BasicCheckButton box1 = new BasicCheckButton("Infinite Mode Multi-Target");
//		box1.setSelected(ClientSettings.TpAuramulti);
//		box1.addButtonListener(new ButtonListener() {
//
//			@Override
//			public void onRightButtonPress(Button button) {
//
//			}
//
//			@Override
//			public void onButtonPress(Button button) {
//				ClientSettings.TpAuramulti = box1.isSelected();
//			}
//		});
		ModSetting slider2 = new SliderSetting<Integer>("Max Targets", ClientSettings.TpAuramaxTargets, 1.0, 50.0, ValueFormat.INT);
		ModSetting slider3 = new SliderSetting<Number>("Hit Range", ClientSettings.TpAurarange, 6, 200, 0.0, ValueFormat.DECIMAL);
		ModSetting slider1 = new SliderSetting<Number>("APS", ClientSettings.TpAuraAPS, 1, 20, 0.0, ValueFormat.DECIMAL);
		return new ModSetting[] { slider2, slider3, slider1 };
	}

	public TpAura() {
		super("TpAura", Keyboard.KEY_NONE, Category.COMBAT,
				"The original infinite reach :)");
	}

	@Override
	public void onToggle() {
		en = null;
		AuraUtils.targets.clear();
		this.stage = 0;
		this.x = 0;
		this.y = 0;
		this.z = 0;
		this.xPreEn = 0;
		this.yPreEn = 0;
		this.zPreEn = 0;
		this.attack = false;
		super.onToggle();
	}

	@Override
	public void onUpdate() {
		if (!timer.hasTimeElapsed(1000 / (int)Math.round(ClientSettings.TpAuraAPS), true)) {
			return;
		}
		AuraUtils.targets.clear();
		en = Utils.getClosestEntity((float) ClientSettings.TpAurarange);
		if (en == null) {
			return;
		}
		if (!AuraUtils.hasEntity(en)) {
			AuraUtils.targets.add(en);
		}
		updateStages();
		super.onUpdate();
	}

	public void updateStages() {
		positions.clear();
		positionsBack.clear();
		int targets = 0;
		ArrayList<EntityLivingBase> list = Utils.getClosestEntities((float) ClientSettings.TpAurarange);
		if (Jigsaw.java8) {
			list.sort(new Comparator<EntityLivingBase>() {
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
		for (EntityLivingBase en : list) {
			AuraUtils.targets.add(en);
			boolean up = false;
			positions.clear();
			positionsBack.clear();
			this.en = en;
			doReach(mc.thePlayer.getDistanceToEntity(this.en), up, list);
			stage = 0;
			targets++;
			if (targets >= ClientSettings.TpAuramaxTargets) {
				// Jigsaw.chatMessage(targets);
				break;
			}
		}

	}

	public void doReach(double range, boolean up, ArrayList<EntityLivingBase> list) {
		if (mc.thePlayer.getDistanceToEntity(en) <= 4) {
			attack(en);
			return;
		}
		attack = Utils.infiniteReach(range, maxXZTP, maxYTP, positionsBack, positions, en);
	}

	@Override
	public void onLateUpdate() {
		if (!attack) {
			return;
		}
		attack = false;
		super.onLateUpdate();
	}

	@Override
	public void onRender() {

//		 GL11.glPushMatrix();
//		 GL11.glEnable(GL11.GL_BLEND);
//		 GL11.glEnable(GL11.GL_LINE_SMOOTH);
//		 GL11.glDisable(GL11.GL_TEXTURE_2D);
//		 GL11.glDisable(GL11.GL_DEPTH_TEST);
//		 GL11.glBlendFunc(770, 771);
//		 GL11.glEnable(GL11.GL_BLEND);
//		 RenderTools.lineWidth(2);
//		 RenderTools.color4f(0.3f, 1f, 0.3f, 1f);
//		 RenderTools.glBegin(3);
//		 int i = 0;
//		 for (Vec3 vec : positions) {
//		 RenderTools.putVertex3d(RenderTools.getRenderPos(vec.xCoord,
//		 vec.yCoord, vec.zCoord));
//		 i++;
//		 }
//		 RenderTools.glEnd();
//		 RenderTools.color4f(0.3f, 0.3f, 1f, 1f);
//		 RenderTools.glBegin(3);
//		 i = 0;
//		 for (Vec3 vec : positionsBack) {
//		 RenderTools.putVertex3d(RenderTools.getRenderPos(vec.xCoord,
//		 vec.yCoord, vec.zCoord));
//		 i++;
//		 }
//		 RenderTools.glEnd();
//		 GL11.glDisable(GL11.GL_BLEND);
//		 GL11.glEnable(GL11.GL_TEXTURE_2D);
//		 GL11.glDisable(GL11.GL_LINE_SMOOTH);
//		 GL11.glDisable(GL11.GL_BLEND);
//		 GL11.glEnable(GL11.GL_DEPTH_TEST);
//		 GL11.glPopMatrix();
//		 RenderTools.lineWidth(3);
//		 for (Vec3 vec : positions) {
//		 drawESP(1f, 0.3f, 0.3f, 1f, vec.xCoord, vec.yCoord, vec.zCoord);
//		 }
//		 RenderTools.lineWidth(1.5f);
//		 for (Vec3 vec : positionsBack) {
//		 drawESP(0.3f, 0.3f, 1f, 1f, vec.xCoord, vec.yCoord, vec.zCoord);
//		 }
		super.onRender();
	}

	public void drawESP(float red, float green, float blue, float alpha, double x, double y, double z) {
		double xPos = x - mc.getRenderManager().renderPosX;
		double yPos = y - mc.getRenderManager().renderPosY;
		double zPos = z - mc.getRenderManager().renderPosZ;
		RenderTools.drawOutlinedEntityESP(xPos, yPos, zPos, mc.thePlayer.width / 2, mc.thePlayer.height, red, green,
				blue, alpha);
	}

	public static boolean doBlock() {
		return en != null;
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

	private void attackInf(EntityLivingBase en) {
		AutoBlock.stopBlock();
		mc.thePlayer.swingItem();
		Criticals.crit(x, y, z);
		Criticals.disable = true;
		sendPacketFinal(new C02PacketUseEntity(en, Action.ATTACK));
		Criticals.disable = false;
		AutoBlock.startBlock();

		float sharpLevel = EnchantmentHelper.func_152377_a(mc.thePlayer.getHeldItem(), en.getCreatureAttribute());
		boolean vanillaCrit = (mc.thePlayer.fallDistance > 0.0F) && (!mc.thePlayer.onGround)
				&& (!mc.thePlayer.isOnLadder()) && (!mc.thePlayer.isInWater())
				&& (!mc.thePlayer.isPotionActive(Potion.blindness)) && (mc.thePlayer.ridingEntity == null);
		if ((Jigsaw.getModuleByName("Criticals").isToggled()) || (vanillaCrit)) {
			mc.thePlayer.onCriticalHit(en);
		}
		if (sharpLevel > 0.0F) {
			mc.thePlayer.onEnchantmentCritical(en);
		}
	}

	private void attack(EntityLivingBase en) {
		AutoBlock.stopBlock();
		mc.thePlayer.swingItem();
		sendPacket(new C02PacketUseEntity(en, Action.ATTACK));
		AutoBlock.startBlock();

		float sharpLevel = EnchantmentHelper.func_152377_a(mc.thePlayer.getHeldItem(), en.getCreatureAttribute());
		boolean vanillaCrit = (mc.thePlayer.fallDistance > 0.0F) && (!mc.thePlayer.onGround)
				&& (!mc.thePlayer.isOnLadder()) && (!mc.thePlayer.isInWater())
				&& (!mc.thePlayer.isPotionActive(Potion.blindness)) && (mc.thePlayer.ridingEntity == null);
		if ((Jigsaw.getModuleByName("Criticals").isToggled()) || (vanillaCrit)) {
			mc.thePlayer.onCriticalHit(en);
		}
		if (sharpLevel > 0.0F) {
			mc.thePlayer.onEnchantmentCritical(en);
		}
	}
}