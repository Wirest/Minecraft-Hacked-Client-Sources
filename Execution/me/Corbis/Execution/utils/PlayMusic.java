package me.Corbis.Execution.utils;

import me.Corbis.Execution.Execution;
import net.minecraft.client.Minecraft;

import java.io.File;

import java.io.IOException;

import javax.sound.sampled.AudioInputStream;

import javax.sound.sampled.AudioSystem;

import javax.sound.sampled.Clip;

import javax.sound.sampled.FloatControl;

import javax.sound.sampled.LineUnavailableException;

import javax.sound.sampled.UnsupportedAudioFileException;



public class PlayMusic {



 public static synchronized void playSound(final String url) {
  new Thread(new Runnable() {
   // The wrapper thread is unnecessary, unless it blocks on the
   // Clip finishing; see comments.
   public void run() {
    try {
     Clip clip = AudioSystem.getClip();
     AudioInputStream inputStream = AudioSystem.getAudioInputStream(
             Execution.class.getResourceAsStream("/assets/minecraft/Execution/Songs/" + url));
     clip.open(inputStream);
     clip.start();
    } catch (Exception e) {
     System.err.println(e.getMessage());
    }
   }
  }).start();
 }

 }

 



