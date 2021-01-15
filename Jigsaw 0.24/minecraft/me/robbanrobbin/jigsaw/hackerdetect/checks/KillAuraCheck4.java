package me.robbanrobbin.jigsaw.hackerdetect.checks;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.hackerdetect.Hacker;
import net.minecraft.entity.Entity;

public class KillAuraCheck4 extends Check {
	public KillAuraCheck4() {
		super();
	}
	@Override
	public CheckState check(Hacker en) {
		Entity entity = Utils.getClosestEntityToEntity(5f, en.player);
		if(entity == null) {
			return CheckState.RESET;
		}
		if(en.player.swingProgress < 2f && en.player.swingProgress != 0f) {
			float[] rots = Utils.getFacePosEntityRemote(en.player, entity);
			boolean highYawRate = false;
			if(Math.abs((en.player.rotationYaw - en.player.prevRotationYaw)) > 40) {
				highYawRate = true;
			}
			if(Math.abs((en.player.rotationYaw - rots[0])) < 2) {
				if(highYawRate) {
					tempViolations += 50;
					//Jigsaw.chatMessage("highYawRate");
				}
				return CheckState.VIOLATION;
			}
			else {
				return CheckState.RESET;
			}
		}
		return CheckState.RESET;
	}
	
	@Override
	public String getPrefix() {
		return " may be using ";
	}

	@Override
	public String getName() {
		return "KillAura/Aimbot (Accuracy)";
	}

	@Override
	public int getMaxViolations() {
		return 100;
	}
	@Override
	public int getDecayTime() {
		return 5000;
	}
}
