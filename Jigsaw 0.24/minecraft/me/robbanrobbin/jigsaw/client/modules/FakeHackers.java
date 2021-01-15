package me.robbanrobbin.jigsaw.client.modules;

import java.util.ArrayList;
import java.util.Iterator;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.WaitTimer;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class FakeHackers extends Module {
	WaitTimer timer = new WaitTimer();
	public static ArrayList<String> fakeHackers = new ArrayList<String>();

	public FakeHackers() {
		super("FakeHackers", Keyboard.KEY_NONE, Category.WORLD, "Type .fakehacker <name> and record him! ;-) Be sure to have this hack enabled before you do the command.");
	}

	@Override
	public void onDisable() {
		if (this.currentMode.equals("Sneak")) {
			for (String name : fakeHackers) {
				EntityPlayer player = mc.theWorld.getPlayerEntityByName(name);
				player.setSneaking(false);
			}
		}
		super.onDisable();
	}

	@Override
	public void onToggle() {
		fakeHackers.clear();
		super.onToggle();
	}

	@Override
	public void onLateUpdate() {
		for (String name : fakeHackers) {
			EntityPlayer player = mc.theWorld.getPlayerEntityByName(name);
			if (player == null) {
				continue;
			}
			if (currentMode.equals("Sneak")) {
				player.setSneaking(true);
			}
			if (currentMode.equals("KillAura")) {
				EntityLivingBase toFace = Utils.getClosestEntityToEntity(6, player);
				float[] rots = Utils.getFacePosEntityRemote(player, toFace);
				player.swingItem();
				player.rotationYawHead = rots[0];
				player.rotationPitch = rots[1];
			}
		}
		super.onLateUpdate();
	}

	@Override
	public String[] getModes() {
		return new String[] { "Sneak", "KillAura" };
	}

	@Override
	public String getAddonText() {
		return this.currentMode;
	}

	public static boolean isFakeHacker(EntityPlayer player) {
		for (String name : fakeHackers) {
			EntityPlayer en = mc.theWorld.getPlayerEntityByName(name);
			if (en == null) {
				continue;
			}
			if (player.isEntityEqual(en)) {
				return true;
			}
		}
		return false;
	}

	public static void removeHacker(EntityPlayer en) {
		Iterator<String> iter = fakeHackers.iterator();

		while (iter.hasNext()) {
			String name = iter.next();
			if (mc.theWorld.getPlayerEntityByName(name) == null) {
				continue;
			}
			if (en.isEntityEqual(mc.theWorld.getPlayerEntityByName(name))) {
				mc.theWorld.getPlayerEntityByName(name).setSneaking(false);
				iter.remove();
			}
		}
	}

}
