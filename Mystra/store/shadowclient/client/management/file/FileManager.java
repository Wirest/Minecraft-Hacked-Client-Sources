package store.shadowclient.client.management.file;

import java.util.ArrayList;
import java.util.List;

import store.shadowclient.client.management.file.files.FriendsFile;
import store.shadowclient.client.management.file.files.ModuleFile;
import store.shadowclient.client.management.file.files.GenFile;

public class FileManager {
	
	private final List<ClientFile> clientFiles;
	
	public FileManager() {
		this.clientFiles = new ArrayList<ClientFile>();
		
		final String appdata = getShadowFolder();
		
		clientFiles.add(new ModuleFile(appdata + "/files/modules.txt"));
		clientFiles.add(new FriendsFile(appdata + "/files/friends.txt"));
		clientFiles.add(new GenFile(appdata + "/files/shadow.txt"));
	}
	
	public ClientFile getFileByClass(final Class<? extends ClientFile> clazz) {
		return clientFiles.stream().filter(file -> file.getClass() == clazz).findFirst().orElse(null);
	}
	
	public List<ClientFile> getFiles() {
		return clientFiles;
	}
	
	public static String getShadowFolder() {
		return System.getProperty("user.home").concat("/AppData/Roaming/.minecraft/Shadow");
	}
}
