package me.robbanrobbin.jigsaw.client.modules.target;

import java.util.ArrayList;

import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class AuraUtils {

	public static ArrayList<EntityLivingBase> targets = new ArrayList<EntityLivingBase>();
	public static ArrayList<EntityLivingBase> blackList = new ArrayList<EntityLivingBase>();

	public static boolean hasEntity(Entity en) {
		if (en == null) {
			return false;
		}
		if (!AuraUtils.targets.isEmpty()) {
			for (EntityLivingBase en1 : AuraUtils.targets) {
				if (en1 == null) {
					continue;
				}
				if (en1.isEntityEqual(en)) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean blackEntity(Entity en) {
		if (en == null) {
			return false;
		}
		if (!AuraUtils.blackList.isEmpty()) {
			for (EntityLivingBase en1 : AuraUtils.blackList) {
				if (en1 == null) {
					continue;
				}
				if (en1.isEntityEqual(en)) {
					return true;
				}
			}
		}
		return false;
	}

	private static boolean disableAura = false;
	private static boolean reachExploit = false;
	private static int timercap = 15;
	private static double range = 7;
	private static boolean headsnap = false;
	private static double chargerange = 8.0;
	
	public static boolean getDisableAura() {
		return disableAura;
	}

	public static void setDisableAura(boolean disableAura) {
		AuraUtils.disableAura = disableAura;
	}

	public static void setReachExploit(boolean reachExploit) {
		AuraUtils.reachExploit = reachExploit;
	}

	public static boolean isReachExploit() {
		return reachExploit;
	}

	private static double packetTPRange = 10;

	public static double getPacketTPRange() {
		return packetTPRange;
	}

	public static void setPacketTPRange(double packetTPRange) {
		AuraUtils.packetTPRange = packetTPRange;
	}

	public static double getRange() {
		return range;
	}

	public static boolean getHeadsnap() {
		return headsnap;
	}

	public static int getAPS() {
		return timercap;
	}

	public static void setTimer(int set) {
		timercap = set;
	}

	public static void setRange(double value) {
		range = value;
	}

	public static void setHeadSnap(boolean selected) {
		headsnap = selected;
	}

	public static double getChargeRange() {
		return chargerange;
	}

	public static void setChargeRange(double chargerange) {
		AuraUtils.chargerange = chargerange;
	}

	public static double getSmoothAimSpeed() {
		return ClientSettings.smoothAimSpeed;
	}

	public static void setSmoothAimSpeed(double smoothAimSpeed) {
		ClientSettings.smoothAimSpeed = smoothAimSpeed;
	}

}
