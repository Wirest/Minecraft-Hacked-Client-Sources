package me.memewaredevs.client.module.misc;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class SpamUtil {

	private static ResourceBundle jakeSpamResource = ResourceBundle.getBundle("me.memewaredevs.client.module.misc.spammer.jakepaul");
	private static ResourceBundle memewareResource = ResourceBundle.getBundle("me.memewaredevs.client.module.misc.spammer.fart");

	public static String getAids(String key) {
		try {
			return jakeSpamResource.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}

	public static String getMemeware(String key) {
		try {
			return memewareResource.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
