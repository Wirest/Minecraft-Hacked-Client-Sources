package me.robbanrobbin.jigsaw.hackerdetect.checks;

import me.robbanrobbin.jigsaw.client.WaitTimer;
import me.robbanrobbin.jigsaw.hackerdetect.Hacker;
import net.minecraft.client.Minecraft;

public abstract class Check {

	public WaitTimer timer = new WaitTimer();
	protected Minecraft mc = Minecraft.getMinecraft();
	private int violations = 0;
	public int tempViolations = 0;
	private boolean enabled = true;

	public abstract CheckState check(Hacker en);

	public int getMaxViolations() {
		return 20;
	}

	public abstract String getName();

	public int getViolations() {
		return violations;
	}

	public void violate() {
		violations++;
		tempViolations = 0;
	}

	public void violate(int amount) {
		violations += amount;
		tempViolations = 0;
	}

	public String getPrefix() {
		return " used ";
	}

	public boolean getMentionName() {
		return true;
	}

	public int getDecayTime() {
		return 0;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return enabled;
	}
}
