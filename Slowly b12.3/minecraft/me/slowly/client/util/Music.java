package me.slowly.client.util;

import java.io.InputStream;
import java.util.Objects;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class Music {
	
	private Player player;
	private Thread thread;
	
	public void setStream(InputStream inputStream) {
		try {
			player = new Player(inputStream);
		} catch (JavaLayerException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isRunning() {
		return thread != null;
	}
	
	public void stop() {
		if (isRunning()) {
			thread.interrupt();
			thread = null;
			
			if (player != null) {
				player.close();
			}
		}
	}
	
	public void start() {
		Objects.requireNonNull(player);
		
		thread = new Thread(() -> {
			 try {
				 player.play();
			 } catch (JavaLayerException e) {
				 e.printStackTrace();
			 }
		});
		thread.start();
	}                                               
}
