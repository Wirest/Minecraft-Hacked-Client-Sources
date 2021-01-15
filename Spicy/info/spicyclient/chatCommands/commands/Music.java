package info.spicyclient.chatCommands.commands;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import info.spicyclient.chatCommands.Command;
import info.spicyclient.files.FileManager;
import info.spicyclient.music.MusicManager;

public class Music extends Command {

	public Music() {
		super("music", "music play/stop/list/shuffle/volume song.mp3/volume", 1);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void commandAction(String message) {
		

		String[] splitMessage = message.split(" ");
		String musicName = "";;
		for (int i = 0; i < splitMessage.length; i++) {
			if (i >= 2) {
				musicName += splitMessage[i] + " ";
			}
		}
		
		if (musicName != "") {
			musicName = musicName.replaceFirst(".music ", "");
			musicName = musicName.substring(0, musicName.length() - 1);
		}
		
		if (splitMessage[1].equalsIgnoreCase("play") && musicName != "") {
			
			MusicManager.getMusicManager().playMp3(new File(FileManager.music + "\\" + musicName).toURI().toString().replaceAll(" ", "%20"));
			
			MusicManager.getMusicManager().shuffle = false;
			
		}
		else if (splitMessage[1].equalsIgnoreCase("stop")) {
			MusicManager.getMusicManager().stopPlaying();
			MusicManager.getMusicManager().shuffle = false;
		}
		else if (splitMessage[1].equalsIgnoreCase("volume") && musicName != "") {
			
			if (MusicManager.getMusicManager().mediaPlayer != null) {
				
				try {
					MusicManager.getMusicManager().mediaPlayer.setVolume(Double.valueOf(musicName)/ 100);
					MusicManager.getMusicManager().volume = Double.valueOf(musicName) / 100;
					sendPrivateChatMessage("Set the music volume to " + musicName);
				} catch (NumberFormatException e) {
					sendPrivateChatMessage("Please type a number between 1 and 100");
				}
				
			}
			
		}
		else if (splitMessage[1].equalsIgnoreCase("shuffle")) {
			
			File[] files = FileManager.music.listFiles();
			
			if (files == null) {
				
				sendPrivateChatMessage("You have 0 mp3 files");
				return;
				
			}
			
			try {
				MusicManager.getMusicManager().playMp3(files[new Random().nextInt(files.length)].toURI().toString().replaceAll(" ", "%20"));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				sendPrivateChatMessage("Unable to play song (No mp3 files in directory?)");
				return;
			}
			MusicManager.getMusicManager().shuffle = true;
			
		}
		else if (splitMessage[1].equalsIgnoreCase("list")) {
			File[] files = FileManager.music.listFiles();
			
			if (files == null) {
				
				sendPrivateChatMessage("You have 0 mp3 files");
				return;
				
			}
			
			sendPrivateChatMessage("You have " + files.length + " mp3 files");
			
			for (File file : files) {
			    if (file.isFile()) {
			    	
			    	sendPrivateChatMessage(" - " + file.getName());
			    	
			    }
			}
			
		}else {
			incorrectParameters();
		}
		
		
	}
	
	@Override
	public void incorrectParameters() {
		
		sendPrivateChatMessage("Please use .music play/stop/list/shuffle/volume song.mp3/volume");
		
	}
	
}
