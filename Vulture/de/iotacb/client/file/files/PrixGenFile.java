package de.iotacb.client.file.files;

import java.util.Base64;

import de.iotacb.client.file.ClientFile;

public class PrixGenFile extends ClientFile {

	public PrixGenFile(String path) {
		super(path);
	}
	
	public void savePrix(String username, String password) {
		final StringBuilder content = new StringBuilder();
		password = new String(Base64.getEncoder().encode(password.getBytes()));
		content.append(username.concat(":").concat(password));
		saveFile(content.toString());
	}
	
	public String[] readPrix() {
		final String content = loadFile().trim();
		if (content.isEmpty() || !content.contains(":")) return null;
		final String username = content.split(":")[0];
		final String password = new String(Base64.getDecoder().decode(content.split(":")[1].getBytes()));
		return new String[] {username, password};
	}

}
