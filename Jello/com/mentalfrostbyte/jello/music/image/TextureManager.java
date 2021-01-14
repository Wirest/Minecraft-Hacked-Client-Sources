package com.mentalfrostbyte.jello.music.image;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TextureManager {

	public static List<TextureImage> cache = new CopyOnWriteArrayList<TextureImage>();

	public static boolean exists(String location) {
		return get(location) != null;
	}

	public static TextureImage get(String location) {
		for(TextureImage ti : cache) {
			if (ti.location.equalsIgnoreCase(location)) {
				return ti;
			}
		}
		return null;
	}
}
