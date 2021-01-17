package me.slowly.client.mod.mods.combat;

import java.io.IOException;
import java.net.URL;

import me.slowly.client.mod.Mod;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.util.Music;

public class MusicHack extends Mod {
	
	private Music music = new Music();

	public MusicHack() {
		super("Music", Mod.Category.COMBAT, Colors.GREEN.c);
	}
	
	@Override
	public void onEnable()
	{
			try {
				music.setStream(new URL("http://stream01.iloveradio.de/iloveradio2.mp3").openStream());
				music.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
	        ClientUtil.sendClientMessage("Music Enable", ClientNotification.Type.SUCCESS);
	}
	
	@Override
	public void onDisable()
	{
		music.stop();
        ClientUtil.sendClientMessage("Music Disable", ClientNotification.Type.ERROR);
	}
	

}
