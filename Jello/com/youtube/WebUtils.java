package com.youtube;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.mentalfrostbyte.jello.hud.NotificationManager;
import com.mentalfrostbyte.jello.main.Jello;
import com.mentalfrostbyte.jello.music.music.Player;

public class WebUtils {
	public static String agent1 = "User-Agent";
    public static String agent2 = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36";
	
	public static String visitSiteThreaded(final String urly){
		final List<String> lines = new ArrayList<String>();
		String stuff = "";
		    (new Thread(new Runnable()
            {
                public void run()
                {
		URL url;
        try {
            String line;
            
            		
            url = new URL(urly);
            
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.addRequestProperty(agent1, agent2);
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((line = in.readLine()) != null) {
            	lines.add(line);
            }
            
        }
        
        catch (Exception e) {
        	
        }
		
                }
            })).start();
        for(String s : lines){
        	stuff += s;
        }
		return stuff;
		
	}
	
	public void playMusicLink(final String urly){
		List<URL> lines = new ArrayList<URL>();
		String stuff = "";
		    (new Thread(new Runnable()
            {
                public void run()
                {
		URL url;
        try {
            String line;
            System.out.println("Attempting to play video with ID " + urly);
            String s = WebUtils.visitSite("https://www.convertmp3.io/fetch/?format=text&video=https://www.youtube.com/watch?v="+urly);
            System.out.println(s);
            if(s.contains("meta")){
            	System.out.println("Sorry, this video has not yet been converted & cached. It is being converted now, come back later and it will be ready.");
            }
            url = new URL(s.split("Link: ")[1]);
            String length = s.split("Length: ")[1];
            String realLength = length.split(" <br")[0];
            System.out.println("Length: " + realLength);
            Jello.jgui.music.currentSongLength = Integer.valueOf(realLength);
            
            System.out.println("Successfully got virtual storage location"/* + url.toString()*/);
            
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.addRequestProperty(agent1, agent2);
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
           
            String actualSongURL = connection.getURL().toString();
            System.out.println("Successfully got physical location of song"/* + actualSongURL*/);
            Player.play(actualSongURL);
            System.out.println("Now Playing");
            
        }
        
        catch (Exception e) {
        	
        }
		
                }
            })).start();
        
		
	}
	
	public static List<String> visitSiteThreadedFriends(final String urly){
		final List<String> lines = new ArrayList<String>();
		try
        {	
            (new Thread(new Runnable()
            {
                public void run()
                {
		URL url;
        try {
            String line;
            url = new URL(urly);
            
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.addRequestProperty(agent1, agent2);
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((line = in.readLine()) != null) {
            	if(!line.isEmpty() && !line.equals(" ") && !line.equals("   ")){
                lines.add(line.contains(" ") ? line.replace(" ", "") : line);
              
              //  for(char c : line.toCharArray()){
              //  	Jello.addChatMessage(String.valueOf(c));
             //   }
            	}
                
            }
            
        }
        
        catch (Exception e) {
        	
        }
		
                }
            })).start();
        }
        catch (RuntimeException runtimeexception)
        {
        }
		
		return lines;
		
	}
	
	public static String visitSite(String urly){
		ArrayList<String> lines = new ArrayList<String>();
		String stuff = "";
		URL url;
        try {
            String line;
            url = new URL(urly);
            
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.addRequestProperty(agent1, agent2);
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            //System.out.println("HEY");
            while ((line = in.readLine()) != null) {
                lines.add(line);
            }
            
        }
        
        catch (Exception e) {
        	
        }
        for(String s : lines){
        	stuff += s;
        }
		return stuff;
		
	}
	
}
