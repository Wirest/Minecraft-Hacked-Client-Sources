package de.iotacb.client.manager;

import java.util.ArrayList;
import java.util.List;

import de.iotacb.client.file.ClientFile;
import de.iotacb.client.file.files.AltManagerFile;
import de.iotacb.client.file.files.ClickGuiFile;
import de.iotacb.client.file.files.ConfigFile;
import de.iotacb.client.file.files.ClientConfigFile;
import de.iotacb.client.file.files.FriendsFile;
import de.iotacb.client.file.files.ModuleFile;
import de.iotacb.client.file.files.PrixGenFile;
import de.iotacb.client.file.files.VultureGenFile;

public class FileManager {
	
	private final List<ClientFile> clientFiles;
	
	public FileManager() {
		this.clientFiles = new ArrayList<ClientFile>();
		
		final String appdata = getVultureFolder();
		
		clientFiles.add(new ModuleFile(appdata + "/files/modules.txt"));
		clientFiles.add(new ConfigFile(appdata + "/files/config.txt"));
		clientFiles.add(new ClickGuiFile(appdata + "/files/click.txt"));
		clientFiles.add(new AltManagerFile(appdata + "/files/alts.txt"));
		clientFiles.add(new FriendsFile(appdata + "/files/friends.txt"));
		clientFiles.add(new ClientConfigFile(appdata + "/files/clientsettings.txt"));
		clientFiles.add(new PrixGenFile(appdata + "/files/prix.txt"));
		clientFiles.add(new VultureGenFile(appdata + "/files/vulture.txt"));
	}
	
	public ClientFile getFileByClass(final Class<? extends ClientFile> clazz) {
		return clientFiles.stream().filter(file -> file.getClass() == clazz).findFirst().orElse(null);
	}
	
	public List<ClientFile> getFiles() {
		return clientFiles;
	}
	
	public String getVultureFolder() {
		return System.getProperty("user.home").concat("/AppData/Roaming/.minecraft/vulture");
	}

}
