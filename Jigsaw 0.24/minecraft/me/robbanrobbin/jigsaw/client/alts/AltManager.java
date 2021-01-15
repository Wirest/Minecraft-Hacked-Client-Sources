package me.robbanrobbin.jigsaw.client.alts;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.ModuleState;
import me.robbanrobbin.jigsaw.module.Module;

public class AltManager {
	
	private ArrayList<Alt> alts = new ArrayList<Alt>();
	
	public ArrayList<Alt> getAlts() {
		return alts;
	}
	
	public void addAlt(Alt alt) {
		alts.add(alt);
	}

	public void saveAlts() {
		System.out.println("Saving alts...");
		JsonObject json = new JsonObject();
		int i = 0;
		for (Alt alt : alts) {
			JsonObject altJson = new JsonObject();
			altJson = alt.saveToJson(altJson);
			json.add(String.valueOf(i), altJson);
			i++;
		}
		try {
			FileWriter fw = new FileWriter(Jigsaw.getFileMananger().altsDir);
			fw.write(json.toString());
			fw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadAlts() {
		JSONParser parser = Jigsaw.getFileMananger().parser;
		Object obj = null;
		try {
			obj = parser.parse(new FileReader(Jigsaw.getFileMananger().altsDir));
			JSONObject json = (JSONObject) obj;
			for(int i = 0; i < 9999; i++) {
				JSONObject altObject = (JSONObject) json.get(String.valueOf(i));
				String email = (String) altObject.get("email");
				String name = (String) altObject.get("name");
				String password = (String) altObject.get("password");
				if((Boolean)altObject.get("cracked")) {
					Alt newAlt = new Alt(name, null);
					Jigsaw.getAltManager().addAlt(newAlt);
				}
				else {
					Alt newAlt = new Alt(email, password);
					Jigsaw.getAltManager().addAlt(newAlt);
				}
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
	}
	
}
