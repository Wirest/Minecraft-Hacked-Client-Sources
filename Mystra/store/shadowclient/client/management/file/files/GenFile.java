package store.shadowclient.client.management.file.files;

import java.util.Base64;

import store.shadowclient.client.management.file.ClientFile;

public class GenFile extends ClientFile {

	public GenFile(String path) {
		super(path);
	}
	
//	public void saveMystra(String username, String password) {
//		final StringBuilder content = new StringBuilder();
//		password = new String(Base64.getEncoder().encode(password.getBytes()));
//		content.append(username.concat(":").concat(password));
//		saveFile(content.toString());
//	}
	
//	public String[] readMystra() {
//		final String content = loadFile().trim();
//		if (content.isEmpty() || !content.contains(":")) return null;
//		final String username = content.split(":")[0];
//		final String password = new String(Base64.getDecoder().decode(content.split(":")[1].getBytes()));
//		return new String[] {username, password};
//	}

}
