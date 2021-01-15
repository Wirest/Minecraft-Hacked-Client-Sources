package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class MobArena extends Module {
	private static EntityLivingBase e;
	private int timer;

	public MobArena() {
		super("MobArena", Keyboard.KEY_NONE, Category.WORLD, "Automatically runs around and kills mobs.");
	}

	@Override
	public void onDisable() {

		e = null;
		mc.gameSettings.keyBindForward.pressed = false;
		super.onDisable();
	}

	@Override
	public void onEnable() {

		super.onEnable();
	}

	@Override
	public void onUpdate() {

		float mindistance = 1000000;

		try {
			for (Object o : mc.theWorld.getLoadedEntityList()) {
				if (!(o instanceof EntityLivingBase)) {
					continue;
				}
				if (o instanceof EntityPlayer) {
					continue;
				}
				EntityLivingBase en = (EntityLivingBase) o;
				if (mc.thePlayer.getDistanceToEntity(en) < mindistance) {
					mindistance = mc.thePlayer.getDistanceToEntity(en);
					e = en;
				}
			}
			if (e == null) {
				setToggled(false, true);
			}
			if (e.isDead) {
				e = null;
				setToggled(false, true);
			}
			if (mc.thePlayer.isDead) {
				e = null;
				setToggled(false, true);
			}

			Utils.faceEntity(e);
			float distance = mc.thePlayer.getDistanceToEntity(e);
			if (distance > 1.5f) {
				mc.gameSettings.keyBindForward.pressed = true;
			} else {
				mc.gameSettings.keyBindForward.pressed = false;
			}
			if (mc.thePlayer.isCollidedHorizontally && mc.thePlayer.onGround) {
				mc.thePlayer.jump();
			}
			if (mc.thePlayer.isInWater()) {
				mc.gameSettings.keyBindJump.pressed = true;
			} else {
				if (mc.gameSettings.keyBindJump.isKeyDown()) {
					mc.gameSettings.keyBindJump.pressed = false;
				}
			}
			if (mc.thePlayer.getDistanceToEntity(e) <= 4.25f) {
				if (timer < 5) {
					timer++;
					return;
				}
				AutoBlock.stopBlock();
				Criticals.crit();
				timer = 0;
				mc.thePlayer.swingItem();
				mc.playerController.attackEntity(mc.thePlayer, e);

				AutoBlock.startBlock();
			}
		} catch (Exception e) {
			System.out.println(getName() + "Enountered an exception: " + e.getMessage());
		}

		super.onUpdate();
	}

}
