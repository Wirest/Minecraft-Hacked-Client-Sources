package store.shadowclient.client.hud.draggablehud.hudmods;

import store.shadowclient.client.hud.draggablehud.HUDManager;
import store.shadowclient.client.hud.draggablehud.hudmods.impl.ModArmorStatus;
import store.shadowclient.client.hud.draggablehud.hudmods.impl.ModCPS;
import store.shadowclient.client.hud.draggablehud.hudmods.impl.ModFPS;
import store.shadowclient.client.hud.draggablehud.hudmods.impl.ModKeystrokes;
import store.shadowclient.client.hud.draggablehud.hudmods.impl.ModPerspective;
import store.shadowclient.client.hud.draggablehud.hudmods.impl.TargetHud;

public class ModInstances {
	private static ModFPS modFPS;

	private static ModKeystrokes modKeystrokes;

	private static ModCPS modCPS;

	private static ModArmorStatus modArmorStatus;

	private static ModPerspective modPerspective;
	
	private static TargetHud targetHUD;
	

	public static void register(HUDManager api) {
		modFPS = new ModFPS();
		api.register(modFPS);

		modKeystrokes = new ModKeystrokes();
		api.register(modKeystrokes);

		modCPS = new ModCPS();
		api.register(modCPS);

		modArmorStatus = new ModArmorStatus();
		api.register(modArmorStatus);

		modPerspective = new ModPerspective();
		api.register(modPerspective);
		
		targetHUD = new TargetHud();
		api.register(targetHUD);
	}

	public static ModPerspective getModPerspective() {
		return modPerspective;
	}
}
