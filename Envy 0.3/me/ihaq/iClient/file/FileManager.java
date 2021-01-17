package me.ihaq.iClient.file;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import me.ihaq.iClient.Envy;
import me.ihaq.iClient.file.files.Alts;
import me.ihaq.iClient.file.files.Binds;
import me.ihaq.iClient.file.files.Color;
import me.ihaq.iClient.file.files.Modules;
import net.minecraft.client.Minecraft;

public class FileManager {

	private static Minecraft mc = Minecraft.getMinecraft();

	public static ArrayList<CustomFile> Files = new ArrayList();
	private static File directory = new File(String.valueOf(mc.mcDataDir.toString()) + "\\" + Envy.Client_Name);
	private static File moduleDirectory = new File(
			String.valueOf(mc.mcDataDir.toString()) + "\\" + Envy.Client_Name + "\\" + "Modules");

	public FileManager() {
		makeDirectories();
		Files.add(new Modules("Modules", true, true));
		Files.add(new Binds("Binds", true, true));
		Files.add(new Alts("Alts", false, true));
		Files.add(new Color("Color", false, true));
	}

	public void loadFiles() {
		for (CustomFile f : Files) {
			try {
				if (f.loadOnStart()) {
					f.loadFile();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void saveFiles() {
		for (CustomFile f : Files) {
			try {
				if (f.getClass() != Modules.class) {
					f.saveFile();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static CustomFile getFile(Class<? extends CustomFile> clazz) {
		for (CustomFile file : Files) {
			if (file.getClass() == clazz) {
				return file;
			}
		}
		return null;
	}

	public void makeDirectories() {
		if (!directory.exists()) {
			if (directory.mkdir()) {
				System.out.println("Directory is created!");
			} else {
				System.out.println("Failed to create directory!");
			}
		}
		if (!moduleDirectory.exists()) {
			if (moduleDirectory.mkdir()) {
				System.out.println("Directory is created!");
			} else {
				System.out.println("Failed to create directory!");
			}
		}
	}

	public static abstract class CustomFile {
		private final File file;
		private final String name;
		private boolean load;

		public CustomFile(String name, boolean Module, boolean loadOnStart) {
			this.name = name;
			this.load = loadOnStart;
			if (Module) {
				this.file = new File(FileManager.moduleDirectory, String.valueOf(name) + ".txt");
			} else {
				this.file = new File(FileManager.directory, String.valueOf(name) + ".txt");
			}
			if (!this.file.exists()) {
				try {
					saveFile();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		public final File getFile() {
			return this.file;
		}

		private boolean loadOnStart() {
			return this.load;
		}

		public final String getName() {
			return this.name;
		}

		public abstract void loadFile() throws IOException;

		public abstract void saveFile() throws IOException;
	}
}
