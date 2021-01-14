package de.iotacb.client.manager;

import java.util.ArrayList;
import java.util.List;

import de.iotacb.client.Client;
import de.iotacb.client.file.files.AltManagerFile;
import de.iotacb.client.gui.alt.Alt;

public class AltManager {

	private final List<Alt> alts;
	
	public AltManager() {
		this.alts = new ArrayList<Alt>();
	}
	
	public List<Alt> getAlts() {
		return alts;
	}
	
	public void addAlt(final String combo) {
		alts.add(new Alt(combo.split(":")[0], combo.split(":").length > 1 ? combo.split(":")[1] : ""));
	}
	
	public Alt getAlt(final String email) {
		return getAlts().stream().filter(alt -> alt.getEmail().equals(email)).findFirst().orElse(null);
	}
	
	public void removeAlt(final String email) {
		getAlts().removeIf(alt -> alt.getEmail().equals(email));
	}
	
}
