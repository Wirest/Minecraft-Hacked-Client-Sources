package net.mcleaks;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

public class MCLeaks {
	public static Session savedSession = null;
	private static String mcLeaksSession;
	private static String mcName;

	public static boolean isAltActive() {
		return mcLeaksSession != null;
	}

	public static String getMCLeaksSession() {
		return mcLeaksSession;
	}

	public static String getMCName() {
		return mcName;
	}

	public static void refresh(String session, String name) {
		mcLeaksSession = session;
		mcName = name;
	}

	public static void remove() {
		mcLeaksSession = null;
		mcName = null;
	}

	public static String getStatus() {
		String status = ChatColor.GOLD + "No Token redeemed. Using " + ChatColor.YELLOW
				+ Minecraft.getMinecraft().getSession().getUsername() + ChatColor.GOLD + " to login!";
		if (mcLeaksSession != null) {
			status = ChatColor.GREEN + "Token active. Using " + ChatColor.AQUA + mcName + ChatColor.GREEN
					+ " to login!";
		}
		return status;
	}
}
