package info.spicyclient.music;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.SwingUtilities;

import info.spicyclient.chatCommands.Command;
import info.spicyclient.events.listeners.EventUpdate;
import info.spicyclient.files.FileManager;
import info.spicyclient.notifications.Color;
import info.spicyclient.notifications.Notification;
import info.spicyclient.notifications.NotificationManager;
import info.spicyclient.notifications.Type;
import info.spicyclient.util.Timer;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.scene.SceneBuilder;
import javafx.scene.control.LabelBuilder;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.stage.StageBuilder;
import javafx.stage.WindowEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class MusicManager {
	
	public static MusicManager musicManager;
	
	public static MusicManager getMusicManager() {
		
		if (musicManager == null) {
			musicManager = new MusicManager();
			
			SwingUtilities.invokeLater(new Runnable() {
	            @Override
	            public void run() {
	                new JFXPanel(); // this will prepare JavaFX toolkit and environment
	                Platform.runLater(new Runnable() {
	                    @Override
	                    public void run() {
	                        StageBuilder.create()
	                                .scene(SceneBuilder.create()
	                                        .width(320)
	                                        .height(240)
	                                        .root(LabelBuilder.create()
	                                                .font(new javafx.scene.text.Font("Arial", 54d))
	                                                .text("Music Player")
	                                                .build())
	                                        .build())
	                                .onCloseRequest(new EventHandler<WindowEvent>() {
	                                    @Override
	                                    public void handle(WindowEvent windowEvent) {
	                                        System.exit(0);
	                                    }
	                                })
	                                .build();
	                    }
	                });
	            }
	        });
			
		}
		
		return musicManager;
		
	}
	
	public MediaPlayer mediaPlayer;
	public Notification musicNotification;
	public boolean playingMusic = false, shuffle = false;
	public Double volume = 1.0;
	
	public void playMp3(String filepath) {
		
		if (musicNotification != null) {
			
			musicNotification.timeOnScreen = 0;
			stopPlaying();
			
		}
		
		musicNotification = new Notification("Playing - " + ((filepath.split("/")[filepath.split("/").length - 1]).contains(".mp3") ? (filepath.split("/")[filepath.split("/").length - 1]) : (filepath.split("/")[filepath.split("/").length - 1]) + ".mp3").replaceAll("%20", " ").replaceAll("%5B", "[").replaceAll("%5D", "]"), "", true, 2000L, Type.INFO, Color.values()[new Random().nextInt(Color.values().length)], NotificationManager.getNotificationManager().defaultTargetX, NotificationManager.getNotificationManager().defaultTargetY, NotificationManager.getNotificationManager().defaultStartingX, NotificationManager.getNotificationManager().defaultStartingY, NotificationManager.getNotificationManager().defaultSpeed);
		musicNotification.setDefaultY = true;
		//NotificationManager.getNotificationManager().createNotification(musicNotification);
		
		new Thread("Music Player - " + filepath) {
			
			public void run() {
				
				try {
					
					Media hit;
					
					if (filepath.contains(".mp3")) {
						
						hit = new Media(filepath.replaceAll("\\\\", "/"));
						mediaPlayer = new MediaPlayer(hit);
						mediaPlayer.play();
						
					}else {
						
						hit = new Media((filepath + ".mp3").replaceAll("\\\\", "/"));
						mediaPlayer = new MediaPlayer(hit);
						mediaPlayer.play();
						
					}
					
					playingMusic = true;
					
					mediaPlayer.setVolume(volume);
					
					mediaPlayer.setOnReady(new Runnable() {

				        @Override
				        public void run() {

				            System.out.println("Duration: "+ hit.getDuration().toSeconds());

				            // display media's metadata
				            for (Map.Entry<String, Object> entry : hit.getMetadata().entrySet()){
				                System.out.println(entry.getKey() + ": " + entry.getValue());
				            }

				            // play if you want
				            //mediaPlayer.play();
				            
							musicNotification = new Notification("Playing - " + ((filepath.split("/")[filepath.split("/").length - 1]).contains(".mp3") ? (filepath.split("/")[filepath.split("/").length - 1]) : (filepath.split("/")[filepath.split("/").length - 1]) + ".mp3").replaceAll("%20", " ").replaceAll("%5B", "[").replaceAll("%5D", "]"), "", true, (long) (mediaPlayer.getMedia().getDuration().toMillis()), Type.INFO, Color.values()[new Random().nextInt(Color.values().length)], NotificationManager.getNotificationManager().defaultTargetX, NotificationManager.getNotificationManager().defaultTargetY, NotificationManager.getNotificationManager().defaultStartingX, NotificationManager.getNotificationManager().defaultStartingY, NotificationManager.getNotificationManager().defaultSpeed);
							musicNotification.setDefaultY = true;
							NotificationManager.getNotificationManager().createNotification(musicNotification);
				            
				        }
				    });
					
				} catch (MediaException | IllegalStateException | IllegalArgumentException | NullPointerException e) {
					
					musicNotification.timeOnScreen = 0;
					e.printStackTrace();
					Command.sendPrivateChatMessage("Failed to play song (Wrong file name?)");
					
				}
				
			};
			
		}.start();
		
	}
	
	public void stopPlaying() {
		
		if (mediaPlayer != null || musicNotification != null) {
			mediaPlayer.stop();
			musicNotification.timeOnScreen = 0;
		}
		
	}
	
	public Timer timer = new Timer();
	
	public void changeNotificationColor(EventUpdate e) {
		
		if (shuffle && musicNotification.left && musicNotification.joined) {
			Command.sendPrivateChatMessage("Playing next song...");
			File[] files = FileManager.music.listFiles();
			
			if (files == null) {
				
				Command.sendPrivateChatMessage("You have 0 mp3 files");
				return;
				
			}
			
			MusicManager.getMusicManager().playMp3(files[new Random().nextInt(files.length)].toURI().toString().replaceAll(" ", "%20"));
			
		}
		
		if (playingMusic && timer.hasTimeElapsed((long) 504.201682, true)) {
			
			//musicNotification.color = Color.values()[new Random().nextInt(Color.values().length)];
			List<Enum> colors = Arrays.asList(Color.values());
			musicNotification.color = Color.values()[colors.indexOf(musicNotification.color) >= colors.size() - 1 ? 0 : colors.indexOf(musicNotification.color) + 1];
			
		}
		
	}
	
}
