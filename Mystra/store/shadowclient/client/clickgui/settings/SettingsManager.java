package store.shadowclient.client.clickgui.settings;

import java.util.ArrayList;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.module.Module;

public class SettingsManager {
	private ArrayList<Setting> settings;

	public SettingsManager(){
		this.settings = new ArrayList<>();
	}

	public void rSetting(Setting in){
		this.settings.add(in);
	}

	public ArrayList<Setting> getSettings(){
		return this.settings;
	}

	public ArrayList<Setting> getSettingsByMod(Module mod){
		ArrayList<Setting> out = new ArrayList<>();
		for(Setting s : getSettings()){
			if(s.getParentMod().equals(mod)){
				out.add(s);
			}
		}
		if(out.isEmpty()){
			return null;
		}
		return out;
	}

	public Setting getSettingByName(String name){
		for(Setting set : getSettings()){
			if(set.getName().equalsIgnoreCase(name)){
				return set;
			}
		}
		System.err.println("["+ Shadow.instance.name + "] Error Setting NOT found: '" + name +"'!");
		return null;
	}
}