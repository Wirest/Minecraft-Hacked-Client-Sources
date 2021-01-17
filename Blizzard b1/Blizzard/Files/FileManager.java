/*
 * Decompiled with CFR 0_122.
 */
package Blizzard.Files;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import net.minecraft.client.Minecraft;

public class FileManager {
	public static ArrayList<CustomFile> Files = new ArrayList();
	private static File directory = new File(
			String.valueOf(String.valueOf(String.valueOf(Minecraft.getMinecraft().mcDataDir.toString()))) + "\\"
					+ "Blizzard");
	private static File moduleDirectory = new File(
			String.valueOf(String.valueOf(String.valueOf(Minecraft.getMinecraft().mcDataDir.toString()))) + "\\"
					+ "Blizzard" + "\\" + "Modules");

	public FileManager() {
		this.makeDirectories();
		Files.add(new Friends("Friends", false, true));
		Files.add(new Alts("alts", false, true));
		Files.add(new Binds("Binds", true, true));
	}

	public void loadFiles() {
		for (CustomFile f : Files) {
			try {
				if (!f.loadOnStart())
					continue;
				f.loadFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void saveFiles() {
		for (CustomFile f : Files) {
			try {
				f.saveFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public CustomFile getFile(Class<? extends CustomFile> clazz) {
		for (CustomFile file : Files) {
			if (file.getClass() != clazz)
				continue;
			return file;
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
			this.file = Module
					? new File(moduleDirectory, String.valueOf(String.valueOf(String.valueOf(name))) + ".txt")
					: new File(directory, String.valueOf(String.valueOf(String.valueOf(name))) + ".txt");
			if (!this.file.exists()) {
				try {
					this.saveFile();
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
