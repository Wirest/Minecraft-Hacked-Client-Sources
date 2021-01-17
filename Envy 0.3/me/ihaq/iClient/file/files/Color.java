package me.ihaq.iClient.file.files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import me.ihaq.iClient.Envy;
import me.ihaq.iClient.file.FileManager;
import me.ihaq.iClient.file.FileManager.CustomFile;
import me.ihaq.iClient.utils.Colors;
import net.minecraft.client.Minecraft;

public class Color extends FileManager.CustomFile {

	public Color(String name, boolean Module, boolean loadOnStart) {
		super(name, Module, loadOnStart);
	}

	@Override
	public void loadFile() throws IOException {
		Scanner inFile;
		try {
			inFile = new Scanner(new BufferedReader(new FileReader(
					Minecraft.getMinecraft().mcDataDir + "\\" + Envy.Client_Name + "\\" + "Color" + ".txt")));
			while (inFile.hasNextLine()) {
				String f = inFile.nextLine();
				Colors.r = Float.parseFloat(f.split(":")[0]);
				Colors.g = Float.parseFloat(f.split(":")[1]);
				Colors.b = Float.parseFloat(f.split(":")[2]);
				Colors.rainbow = Boolean.parseBoolean(f.split(":")[3]);
			}
			inFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void saveFile() throws IOException {
		try {
			PrintWriter printWriter = new PrintWriter(new FileWriter(new File(
					Minecraft.getMinecraft().mcDataDir + "\\" + Envy.Client_Name + "\\" + "Color" + ".txt")));
			printWriter.println(String.valueOf(Colors.r + ":" + Colors.g + ":" + Colors.b + ":" + Colors.rainbow));
			printWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
