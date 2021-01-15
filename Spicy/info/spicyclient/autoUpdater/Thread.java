package info.spicyclient.autoUpdater;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import info.spicyclient.SpicyClient;

public class Thread extends java.lang.Thread {
	
	@Override
	public void run() {
		
		File file = new File(Referances.CLIENT_FOLDER);
		if(!file.exists()){
			System.out.println("Creating file " + Referances.CLIENT_FOLDER);
			file.mkdir();
		}
		
		try {
			Installer.saveFile(Referances.JSON_LINK, Referances.JSON_FILE);
			System.out.println("Searching file " + Referances.JSON_FILE);
			String content = FileUtils.readFileToString(new File(Referances.JSON_FILE), "UTF-8");
			System.out.println("Replacing content (Replacing \"ClientName\" for \"" + Referances.ClientName + "\"");
		    content = content.replaceAll("ClientName", Referances.ClientName);
		    System.out.println("Setting tempFile...");
		    File tempFile = new File(Referances.JSON_FILE);
		    System.out.println("Replacing file " + Referances.JSON_FILE + " with New one!");
		    FileUtils.writeStringToFile(tempFile, content, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			Installer.saveFile(Referances.JarLink, Referances.JAR_FILE);
		} catch (IOException e){
			e.printStackTrace();
		}
		
		System.out.println("Client updated");
		
		SpicyClient.shutdown();
		
		System.exit(0);
		
	}
	
}
