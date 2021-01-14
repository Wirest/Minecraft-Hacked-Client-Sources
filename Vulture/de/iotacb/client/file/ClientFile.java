package de.iotacb.client.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ClientFile {
	
	private String path;
	
	public ClientFile(String path) {
		this.path = path;
		final File file = new File(path);
		if (!file.exists()) {
			file.getParentFile().mkdirs();
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean saveFile(String content) {
		try {
			final BufferedWriter writer = new BufferedWriter(new FileWriter(new File(path)));
			writer.write(content);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public String loadFile() {
		final StringBuilder content = new StringBuilder();
		try {
			final File file = new File(path);
			if (!file.exists()) file.createNewFile();
			final BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null) {
				content.append(line.concat("\n"));
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content.toString();
	}
	
	public String getPath() {
		return path;
	}

}
