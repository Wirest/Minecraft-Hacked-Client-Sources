package info.spicyclient.autoUpdater;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import info.spicyclient.SpicyClient;
import info.spicyclient.files.FileManager;

public class Updater {
	
	public static Updater updater;
	
	public static Updater getUpdater() {
		
		if (updater == null) {
			updater = new Updater();
		}
		
		return updater;
		
	}
	
	public boolean outdated = false, checkedForUpdate = false;
	
	public boolean ClientOutdated() {
		
		if (!checkedForUpdate) {
			
			try {
				if (SpicyClient.currentVersionNum < getCurrentVersion()) {
					
					//System.out.println("You are using an outdated version than is released!");
					outdated = true;
					
				}
				else if (SpicyClient.currentVersionNum > getCurrentVersion()) {
					
					//System.out.println("You are running a newer version than is released!");
					
				}else {
					
					//System.out.println("You are using the current version than is released!");
					
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			checkedForUpdate = true;
			
		}
		
		return outdated;
		
	}
	
	public void update() {
		
		System.out.println("Updating the client...");
		new Thread().start();
		
	}
	
	public int getCurrentVersion() throws Exception {
		
	     String url = "http://spicyclient.info/api/Updater.php";
	     URL obj = new URL(url);
	     HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	     // optional default is GET
	     con.setRequestMethod("GET");
	     //add request header
	     con.setRequestProperty("User-Agent", "Mozilla/5.0");
	     int responseCode = con.getResponseCode();
	     //System.out.println("\nSending 'GET' request to URL : " + url);
	     //System.out.println("Response Code : " + responseCode);
	     BufferedReader in = new BufferedReader(
	             new InputStreamReader(con.getInputStream()));
	     String inputLine;
	     StringBuffer response = new StringBuffer();
	     while ((inputLine = in.readLine()) != null) {
	     	response.append(inputLine);
	     }
	     in.close();
	     //print in String
	     //System.out.println(response.toString());
	     //Read JSON response and print
	     JSONObject myResponse = new JSONObject(response.toString());
	     
	     return myResponse.getInt("currentVersion");
	     
	}
	
}
