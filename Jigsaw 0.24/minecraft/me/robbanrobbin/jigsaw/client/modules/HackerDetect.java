package me.robbanrobbin.jigsaw.client.modules;

import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.hackerdetect.Hacker;
import me.robbanrobbin.jigsaw.hackerdetect.checks.AntiKBCheck;
import me.robbanrobbin.jigsaw.hackerdetect.checks.Check;
import me.robbanrobbin.jigsaw.hackerdetect.checks.KillAuraCheck;
import me.robbanrobbin.jigsaw.hackerdetect.checks.KillAuraCheck3;
import me.robbanrobbin.jigsaw.hackerdetect.checks.KillAuraCheck4;
import me.robbanrobbin.jigsaw.hackerdetect.checks.NoSlowCheck;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;

public class HackerDetect extends Module {

	public static HashMap<String, Hacker> players = new HashMap<String, Hacker>();
	public static ArrayList<String> muted = new ArrayList<String>();
	public static ArrayList<Check> checks = new ArrayList<Check>();

	public HackerDetect() {
		super("HackerDetect", Keyboard.KEY_NONE, Category.HIDDEN, "Tries to catch hackers.");
	}

	@Override
	public void onClientLoad() {
		checks.add(new AntiKBCheck());
		checks.add(new KillAuraCheck());
		checks.add(new KillAuraCheck3());
		checks.add(new KillAuraCheck4());
		super.onClientLoad();
	}

	@Override
	public void onUpdate() {
		for (EntityPlayer player : mc.theWorld.playerEntities) {
			if (players.containsKey(player.getName())) {
				continue;
			}
			if (player instanceof EntityPlayerSP) {
				continue;
			}
			if(player.getName().equals(mc.thePlayer.getName())) {
				continue;
			}
			
			Hacker hc = new Hacker(player);
			players.put(player.getName(), hc);
			if (!ClientSettings.hackerDetectAutoNotify) {
				muted.add(player.getName());
			}
		}
		for (Hacker en : players.values()) {
			boolean exists = false;
			for (EntityPlayer p : mc.theWorld.playerEntities) {
				if (p.getName().equals(en.player.getName())) {
					en.player = p;
					exists = true;
					break;
				}
			}
			if (!exists) {
				continue;
			}
			en.doChecks();
		}
		super.onUpdate();
	}

	public static int getViolations(String name) {
		int i = 0;
		Hacker en = players.get(name);
		i = en.getViolations();
		return i;
	}

	public static Check getCheck(String playerName, String checkName) {
		Hacker en = players.get(playerName);
		for (Check check : en.checks) {
			if (check.getName().equals(checkName)) {
				return check;
			}
		}
		return null;
	}

	@Override
	public boolean getEnableAtStartup() {
		return true;
	}

	@Override
	public boolean dontToggleOnLoadModules() {
		return true;
	}

	public static void updateEnabledChecks() {
		for (Hacker hacker : players.values()) {
			hacker.updateEnabledChecks();
		}
	}
}
