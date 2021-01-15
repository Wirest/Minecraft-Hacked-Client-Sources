package me.robbanrobbin.jigsaw.hackerdetect.checks;

import me.robbanrobbin.jigsaw.hackerdetect.Hacker;

public class NoSlowCheck extends Check {

	@Override
	public CheckState check(Hacker en) {
		if (en.player.isBlocking() && (Math.abs(en.player.posX - en.player.lastTickPosX) > 0.3
				|| Math.abs(en.player.posZ - en.player.lastTickPosZ) > 0.3)) {
			return CheckState.VIOLATION;
		}
		return CheckState.RESET;
	}

	@Override
	public String getName() {
		return "NoSlowDown";
	}

	@Override
	public int getMaxViolations() {
		return 30;
	}

	@Override
	public int getDecayTime() {
		return 1000;
	}
}
