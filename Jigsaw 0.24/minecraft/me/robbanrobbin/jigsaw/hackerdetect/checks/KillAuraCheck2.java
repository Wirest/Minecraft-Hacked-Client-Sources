package me.robbanrobbin.jigsaw.hackerdetect.checks;

import me.robbanrobbin.jigsaw.hackerdetect.Hacker;

public class KillAuraCheck2 extends Check {

	@Override
	public CheckState check(Hacker en) {
		if (en.player.aps > 2 && en.player.aps == en.player.preAps && en.player.aps != 0) {
			return CheckState.VIOLATION;
		}
		return CheckState.RESET;
	}

	@Override
	public String getName() {
		return "AutoClicker/KillAura (Constant APS)";
	}

	@Override
	public int getMaxViolations() {
		return 100;
	}

	@Override
	public String getPrefix() {
		return "§7 has a §cstrange §cAPS §cpattern. §rHas the player been mining a block for a long time?";
	}

	@Override
	public boolean getMentionName() {
		return false;
	}
}
