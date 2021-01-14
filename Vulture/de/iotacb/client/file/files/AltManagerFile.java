package de.iotacb.client.file.files;

import de.iotacb.client.Client;
import de.iotacb.client.file.ClientFile;
import de.iotacb.client.gui.alt.Alt;

public class AltManagerFile extends ClientFile {

	public AltManagerFile(String path) {
		super(path);
	}
	
	public void saveAlts() {
		final StringBuilder content = new StringBuilder();
		for (Alt alt : Client.INSTANCE.getAltManager().getAlts()) {
			content.append(alt.getEmail().concat(":").concat(alt.getPassword()).concat("\n"));
		}
		saveFile(content.toString());
	}
	
	public void readAlts() {
		Client.INSTANCE.getAltManager().getAlts().clear();
		final String content = loadFile();
		if (content.isEmpty()) return;
		final String[] lines = content.split("\n");
		
		for (String line : lines) {
			if (line.contains(":")) {
				final String email = line.split(":")[0];
				String password = "";
				if (line.split(":").length > 1) {
					password = line.split(":")[1];
				}
				Client.INSTANCE.getAltManager().addAlt(email.concat(":").concat(password));
			} else {
				Client.INSTANCE.getAltManager().addAlt(line);
			}
		}
	}

}
