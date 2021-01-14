package com.etb.client.gui;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import com.etb.client.Client;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class  AntiStrike {
	private static File antistrikeFile;
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	private HashMap<String, String> list;
	
	public AntiStrike(File dir) {
		antistrikeFile = new File(dir + File.separator + "antistrike.json");
		list = new HashMap();
		list.put("velt","ETB");
		list.put("arcane","ETB");
		list.put("faithful","ETB");
		setup();
	}
	public void setup() {
		try {
			if (!antistrikeFile.exists()) {
				antistrikeFile.createNewFile();
				return;
			}
			loadFile();
		} catch (IOException exception) {
		}
	}
	public void loadFile() {
		if (!antistrikeFile.exists()) {
			return;
		}
		try (FileReader inFile = new FileReader(antistrikeFile)) {
			Client.INSTANCE.getAntiStrike().setList(GSON.fromJson(inFile, new TypeToken<HashMap<String, String>>() {
			}.getType()));

			if (Client.INSTANCE.getAntiStrike().getList() == null)
				Client.INSTANCE.getAntiStrike().setList(new HashMap<String, String>());

		} catch (Exception e) {
		}
	}

	public void saveFile() {
		if (antistrikeFile.exists()) {
			try (PrintWriter writer = new PrintWriter(antistrikeFile)) {
				writer.print(GSON.toJson(Client.INSTANCE.getAntiStrike().getList()));
			} catch (Exception e) {
			}
		}
	}

	public HashMap<String, String> getList() {
		return list;
	}
	public void setList(HashMap<String, String> list) {
		this.list = list;
	}

	public void add(String original, String replace) {
		if (!list.containsKey(original)) {
		list.put(original, replace);
		}
	}

	public void remove(String remove) {
		if (list.containsKey(remove)) {
			list.remove(remove);
		} else if (list.containsValue(remove)) {
			list.values().remove(remove);
		}
	}
}
