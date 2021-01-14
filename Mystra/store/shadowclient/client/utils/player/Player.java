package store.shadowclient.client.utils.player;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class Player {

	private static Minecraft mc = Minecraft.getMinecraft();

	public static void sendMessage(String message) {
		sendMessage(message, true);
	}

	public static void sendMessage(String message, boolean prefix) {
		mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(translateColors((prefix ? "" : "") + message)));
	}
	
	public static String translateColors(String string) {
		String[] raw = string.split(" ");

		String result = "";

		List<String> styles = new ArrayList<String>();
		String lastColor = "";
		for (int wordIndex = 0; wordIndex < raw.length; wordIndex++) {
			String word = raw[wordIndex];
			char[] chars = word.toCharArray();
			for (int charIndex = 0; charIndex < chars.length; charIndex++) {
				char c = chars[charIndex];
				if (!(isColorSign(c)))
					continue;

				if (charIndex + 1 >= chars.length)
					continue;

				char nextChar = chars[charIndex + 1];
				String color = String.valueOf(c) + String.valueOf(nextChar);
				if (!(isColor(color)))
					continue;

				if ((nextChar == 'l' || nextChar == 'o' || nextChar == 'm' || nextChar == 'n')) {
					if (!(styles.contains(String.valueOf(nextChar))))
						styles.add(String.valueOf(nextChar));
				} else {
					lastColor = color;
					styles.clear();
				}
			}

			String styleCodes = "";

			for (int i = 0; i < styles.size(); i++)
				styleCodes += "&" + styles.get(i);

			result += (wordIndex == 0 ? "" : " ") + word + lastColor + styleCodes;
		}

		return simpleTranslateColors(result);
	}
	
	public static String simpleTranslateColors(String string) {
		return string.replace("&", "\u00A7");
	}
	
	public static boolean isColorSign(char c) {
		return simpleTranslateColors(String.valueOf(c)).equals("\u00A7");
	}
	
	public static boolean isColor(String text) {
		return COLOR_CODE_PATTERN.matcher(simpleTranslateColors(text)).matches();
	}
	
	public static final Pattern COLOR_CODE_PATTERN = Pattern.compile("(?i)\\u00A7[0-9A-FK-OR]");

}