package me.xatzdevelopments.xatz.hackerdetect.checks;

import me.xatzdevelopments.xatz.hackerdetect.Hacker;

public class KillAuraCheck extends Check {

	@Override
	public CheckState check(Hacker en) {

		if (en.player.aps >= 10) {
			return CheckState.VIOLATION;
		}
		return CheckState.RESET;
	}

	@Override
	public String getName() {
		return "AutoClicker/KillAura (High APS)";
	}

	@Override
	public int getMaxViolations() {
		return 40;
	}
}
