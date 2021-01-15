package me.robbanrobbin.jigsaw.hackerdetect;

import java.util.ArrayList;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.modules.HackerDetect;
import me.robbanrobbin.jigsaw.client.modules.Ping;
import me.robbanrobbin.jigsaw.gui.Level;
import me.robbanrobbin.jigsaw.gui.Notification;
import me.robbanrobbin.jigsaw.hackerdetect.checks.AntiKBCheck;
import me.robbanrobbin.jigsaw.hackerdetect.checks.Check;
import me.robbanrobbin.jigsaw.hackerdetect.checks.CheckState;
import me.robbanrobbin.jigsaw.hackerdetect.checks.FlyCheck;
import me.robbanrobbin.jigsaw.hackerdetect.checks.KillAuraCheck;
import me.robbanrobbin.jigsaw.hackerdetect.checks.KillAuraCheck3;
import me.robbanrobbin.jigsaw.hackerdetect.checks.KillAuraCheck4;
import me.robbanrobbin.jigsaw.hackerdetect.checks.NoSlowCheck;
import me.robbanrobbin.jigsaw.hackerdetect.checks.SpeedCheck;
import net.minecraft.entity.player.EntityPlayer;

public class Hacker {

	public EntityPlayer player;
	public ArrayList<Check> checks = new ArrayList<Check>();
	public int maxAps = 0;
	public double maxYawrate = 0;
	boolean didIntercept = false;

	public Hacker(EntityPlayer player) {
		this.player = player;
		checks.add(new AntiKBCheck());
		checks.add(new KillAuraCheck());
		checks.add(new KillAuraCheck3());
		checks.add(new KillAuraCheck4());
	}

	public void updateEnabledChecks() {
		for (Check check : HackerDetect.checks) {
			Check found = HackerDetect.getCheck(this.player.getName(), check.getName());
			found.setEnabled(check.isEnabled());
		}
	}

	public void doChecks() {
		updateEnabledChecks();
		maxAps = Math.max(maxAps, player.aps);
		maxYawrate = Math.max(maxYawrate, Math.abs(player.rotationYaw - player.prevRotationYaw));
		for (Check check : this.checks) {
			if (!check.isEnabled()) {
				continue;
			}
			if (Ping.timer.getTime() < 200) {
				if (didIntercept) {
					didIntercept = false;
					return;
				}
				CheckState state = check.check(this);
				if (state == CheckState.VIOLATION) {
					check.timer.reset();
					check.tempViolations++;
				} else if(state == CheckState.RESET) {
					if (check.timer.hasTimeElapsed(check.getDecayTime(), false)) {
						check.tempViolations = 0;
					}
				}
				else if(state == CheckState.IDLE) {
					
				}

			} else {
				didIntercept = true;
			}
			if (check.tempViolations >= check.getMaxViolations()) {
				check.violate();
				if (!HackerDetect.muted.contains(player.getName())) {
					if (check.getMentionName()) {
						Jigsaw.getNotificationManager().addNotification(new Notification(Level.WARNING, 
								"Player " + "" + player.getName() + check.getPrefix() + check.getName() + " vl=(" + check.getViolations() + ")"));
					} else {
						Jigsaw.getNotificationManager().addNotification(new Notification(Level.WARNING, 
								"Player " + "" + player.getName() + check.getPrefix() + " vl=(" + check.getViolations() + ")"));
					}

				}
			}
		}
	}

	public int getViolations() {
		int i = 0;
		for (Check check : checks) {
			if (!check.isEnabled()) {
				continue;
			}
			i += check.getViolations();
		}
		return i;
	}

}
