package me.xatzdevelopments.xatz.hackerdetect.checks;

import me.xatzdevelopments.xatz.hackerdetect.Hacker;

public class SpeedCheck extends Check {

	@Override
	public CheckState check(Hacker en) {
		if (Math.abs(en.player.posX - en.player.lastTickPosX) > 0.42
				|| Math.abs(en.player.posZ - en.player.lastTickPosZ) > 0.42) {
			return CheckState.VIOLATION;
		}
		return CheckState.RESET;
	}

	@Override
	public String getName() {
		return "Speed";
	}
}
