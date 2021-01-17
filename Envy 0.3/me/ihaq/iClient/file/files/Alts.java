package me.ihaq.iClient.file.files;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import me.ihaq.iClient.file.FileManager;
import me.ihaq.iClient.file.FileManager.CustomFile;
import me.ihaq.iClient.gui.GuiWindows.altmanager.Alt;
import me.ihaq.iClient.gui.GuiWindows.altmanager.AltManager;

public class Alts extends FileManager.CustomFile {

	public Alts(String name, boolean Module, boolean loadOnStart) {
		super(name, Module, loadOnStart);
	}

	public void loadFile() throws IOException {
		BufferedReader variable9 = new BufferedReader(new FileReader(getFile()));
		String line;
		while ((line = variable9.readLine()) != null) {
			String[] arguments = line.split(":");
			for (int i = 0; i < 2; i++) {
				arguments[i].replace(" ", "");
			}
			if (arguments.length > 2) {
				AltManager.registry.add(new Alt(arguments[0], arguments[1], arguments[2]));
			} else {
				AltManager.registry.add(new Alt(arguments[0], arguments[1]));
			}
		}
		variable9.close();
	}

	public void saveFile() throws IOException {
		PrintWriter alts = new PrintWriter(new FileWriter(getFile()));
		for (Alt alt : AltManager.registry) {
			if (alt.getMask().equals("")) {
				alts.println(alt.getUsername() + ":" + alt.getPassword());
			} else {
				alts.println(alt.getUsername() + ":" + alt.getPassword() + ":" + alt.getMask());
			}
		}
		alts.close();
	}
}