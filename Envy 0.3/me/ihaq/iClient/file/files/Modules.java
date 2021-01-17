package me.ihaq.iClient.file.files;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import me.ihaq.iClient.Envy;
import me.ihaq.iClient.event.EventManager;
import me.ihaq.iClient.file.FileManager;
import me.ihaq.iClient.file.FileManager.CustomFile;
import me.ihaq.iClient.modules.Module;

public class Modules extends FileManager.CustomFile {
	public Modules(String name, boolean Module, boolean loadOnStart) {
		super(name, Module, loadOnStart);
	}

	@Override
	public void loadFile() throws IOException {
		BufferedReader variable9 = new BufferedReader(new FileReader(getFile()));
		String line;
		while ((line = variable9.readLine()) != null) {
			int i = line.indexOf(":");
			if (i >= 0) {
				String module = line.substring(0, i).trim();
				String enabled = line.substring(i + 1).trim();
				Module m = Module.getModuleByString(module);
				m.setToggled(Boolean.valueOf(enabled).booleanValue());
			}
		}
		variable9.close();
	}

	@Override
	public void saveFile() throws IOException {
		PrintWriter variable9 = new PrintWriter(new FileWriter(getFile()));
		for (Module m : Envy.MODULE_MANAGER.getModules()) {
			variable9.println(m.getName() + ":" + m.isToggled());
		}
		variable9.close();
	}
}
