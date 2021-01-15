package me.robbanrobbin.jigsaw.hackerdetect.checks;

import me.robbanrobbin.jigsaw.hackerdetect.Hacker;

public class FlyCheck extends Check {

	@Override
	public CheckState check(Hacker en) {
		if ((!mc.theWorld.checkBlockCollision(en.player.boundingBox.offset(0, -1.1, 0)))
				&& en.player.prevPosY < en.player.posY) {
			return CheckState.VIOLATION;
		}
		return CheckState.RESET;
	}

	@Override
	public String getName() {
		return "Flight/Hover";
	}
}
