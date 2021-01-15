package jaco.mp3.player;

import jaco.mp3.resources.Decoder;
import jaco.mp3.resources.Frame;
import jaco.mp3.resources.SampleBuffer;
import jaco.mp3.resources.SoundStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.FloatControl.Type;
import javax.swing.JPanel;
import me.klintos.one.One;
import me.klintos.one.utils.notifications.Notification;

public class MP3Player extends JPanel {
   private static final long serialVersionUID = 1L;
   private static final Logger LOGGER = Logger.getLogger(MP3Player.class.getName());
   private static final Random RANDOM = new Random();
   public static List playlist = new ArrayList();
   private transient boolean isPaused = false;
   private transient boolean isStopped = true;
   private transient volatile int volume = 50;
   public transient volatile boolean shuffle = false;
   private transient volatile boolean repeat = true;
   private transient volatile Thread playingThread = null;
   private transient volatile int playingIndex = 0;
   private transient volatile SourceDataLine playingSource = null;
   private transient volatile int playingSourceVolume = 0;

   public MP3Player() {
      this.init();
   }

   public MP3Player(File file) {
      this.add(file);
      this.init();
   }

   public MP3Player(File... files) {
      File[] var5 = files;
      int var4 = files.length;

      for(int var3 = 0; var3 < var4; ++var3) {
         File file = var5[var3];
         this.add(file);
      }

      this.init();
   }

   public MP3Player(URL url) {
      this.add(url);
      this.init();
   }

   public MP3Player(URL... urls) {
      URL[] var5 = urls;
      int var4 = urls.length;

      for(int var3 = 0; var3 < var4; ++var3) {
         URL url = var5[var3];
         this.add(url);
      }

      this.init();
   }

   private void init() {
      (new MP3PlayerThemeDefault()).apply(this);
   }

   public MP3Player add(File file, boolean recursively) {
      if (file.isFile()) {
         if (file.getName().endsWith(".mp3")) {
            synchronized(this) {
               playlist.add(file);
            }
         }
      } else {
         if (!file.isDirectory()) {
            throw new IllegalArgumentException("WTF is this? ( " + file + " )");
         }

         File[] files = file.listFiles();
         File[] var7 = files;
         int var6 = files.length;

         for(int var5 = 0; var5 < var6; ++var5) {
            File file2 = var7[var5];
            if (file2.isFile() || recursively) {
               this.add(file2, recursively);
            }
         }
      }

      return this;
   }

   public MP3Player add(File file) {
      this.add(file, true);
      return this;
   }

   public MP3Player add(URL url) {
      synchronized(this) {
         playlist.add(url);
         return this;
      }
   }

   public void setTheme(MP3PlayerTheme theme) {
      this.removeAll();
      theme.apply(this);
      this.revalidate();
      this.repaint();
   }

   public void play() {
      synchronized(this) {
         if (this.isPaused) {
            this.isPaused = false;
            this.notifyAll();
            return;
         }
      }

      this.stop();
      if (playlist.size() != 0) {
         synchronized(this) {
            this.isStopped = false;
         }

         if (this.playingThread == null) {
            this.playingThread = new Thread() {
               public void run() {
                  Object inputStream = null;

                  try {
                     Object playlistObject;
                     synchronized(MP3Player.this) {
                        playlistObject = MP3Player.playlist.get(MP3Player.this.playingIndex);
                     }

                     if (playlistObject instanceof File) {
                        inputStream = new FileInputStream((File)playlistObject);
                     } else {
                        if (!(playlistObject instanceof URL)) {
                           throw new IOException("this is impossible; how come the play list contains this kind of object? :: " + playlistObject.getClass());
                        }

                        inputStream = ((URL)playlistObject).openStream();
                     }

                     SoundStream soundStream = new SoundStream((InputStream)inputStream);
                     Decoder decoder = new Decoder();

                     label307:
                     while(true) {
                        while(true) {
                           synchronized(MP3Player.this) {
                              if (MP3Player.this.isStopped) {
                                 break label307;
                              }

                              if (!MP3Player.this.isPaused) {
                                 break;
                              }

                              if (MP3Player.this.playingSource != null) {
                                 MP3Player.this.playingSource.flush();
                              }

                              MP3Player.this.playingSourceVolume = MP3Player.this.volume;

                              try {
                                 MP3Player.this.wait();
                              } catch (InterruptedException var28) {
                                 MP3Player.LOGGER.log(Level.SEVERE, "wait() failed", var28);
                              }
                           }
                        }

                        try {
                           Frame frame = soundStream.readFrame();
                           if (frame == null) {
                              break;
                           }

                           MP3Player var10000;
                           if (MP3Player.this.playingSource == null) {
                              int frequency = frame.frequency();
                              int channels = frame.mode() == 3 ? 1 : 2;
                              AudioFormat format = new AudioFormat((float)frequency, 16, channels, true, false);
                              Line line = AudioSystem.getLine(new Info(SourceDataLine.class, format));
                              MP3Player.this.playingSource = (SourceDataLine)line;
                              MP3Player.this.playingSource.open(format);
                              MP3Player.this.playingSource.start();
                              var10000 = MP3Player.this;
                              SourceDataLine var10001 = MP3Player.this.playingSource;
                              MP3Player.this.playingSourceVolume = 0;
                              var10000.setVolume(var10001, 0);
                           }

                           SampleBuffer output = (SampleBuffer)decoder.decodeFrame(frame, soundStream);
                           short[] buffer = output.getBuffer();
                           int offs = 0;
                           int len = output.getBufferLength();
                           if (MP3Player.this.playingSourceVolume != MP3Player.this.volume) {
                              if (MP3Player.this.playingSourceVolume > MP3Player.this.volume) {
                                 var10000 = MP3Player.this;
                                 var10000.playingSourceVolume = var10000.playingSourceVolume - 10;
                                 if (MP3Player.this.playingSourceVolume < MP3Player.this.volume) {
                                    MP3Player.this.playingSourceVolume = MP3Player.this.volume;
                                 }
                              } else {
                                 var10000 = MP3Player.this;
                                 var10000.playingSourceVolume = var10000.playingSourceVolume + 10;
                                 if (MP3Player.this.playingSourceVolume > MP3Player.this.volume) {
                                    MP3Player.this.playingSourceVolume = MP3Player.this.volume;
                                 }
                              }

                              MP3Player.this.setVolume(MP3Player.this.playingSource, MP3Player.this.playingSourceVolume);
                           }

                           MP3Player.this.playingSource.write(MP3Player.this.toByteArray(buffer, offs, len), 0, len * 2);
                           soundStream.closeFrame();
                        } catch (Exception var30) {
                           MP3Player.LOGGER.log(Level.WARNING, "unexpected problems while playing " + this.toString(), var30);
                           break;
                        }
                     }

                     if (MP3Player.this.playingSource == null) {
                        MP3Player.LOGGER.log(Level.INFO, "source is null because first frame is null, so probably the file is not a mp3");
                     } else {
                        synchronized(MP3Player.this) {
                           if (!MP3Player.this.isStopped) {
                              MP3Player.this.playingSource.drain();
                           } else {
                              MP3Player.this.playingSource.flush();
                           }
                        }

                        MP3Player.this.playingSource.stop();
                        MP3Player.this.playingSource.close();
                        MP3Player.this.playingSource = null;
                     }

                     try {
                        soundStream.close();
                     } catch (Exception var26) {
                        MP3Player.LOGGER.log(Level.WARNING, "error closing the sound stream", var26);
                     }
                  } catch (IOException var32) {
                     MP3Player.LOGGER.log(Level.SEVERE, "unable to open the input stream", var32);
                  } finally {
                     if (inputStream != null) {
                        try {
                           ((InputStream)inputStream).close();
                        } catch (Exception var24) {
                           MP3Player.LOGGER.log(Level.WARNING, "error closing the input stream", var24);
                        }
                     }

                  }

                  boolean skipForwardAllowed;
                  synchronized(MP3Player.this) {
                     skipForwardAllowed = !MP3Player.this.isStopped;
                     MP3Player.this.isPaused = false;
                     MP3Player.this.isStopped = true;
                  }

                  MP3Player.this.playingThread = null;
                  if (skipForwardAllowed) {
                     MP3Player.this.skipForward();
                     MP3Player.this.play();
                     String name = MP3Player.this.getCurrentSong().getName().replaceAll(".mp3", "");
                     One.getNotificationHandler().addNotification(new Notification("Now playing " + name + ".", -11184641));
                  }

               }
            };
            this.playingThread.start();
         }

      }
   }

   public boolean isPlaying() {
      synchronized(this) {
         return !this.isPaused && !this.isStopped;
      }
   }

   public void pause() {
      if (this.isPlaying()) {
         synchronized(this) {
            this.isPaused = true;
            this.notifyAll();
         }
      }
   }

   public boolean isPaused() {
      synchronized(this) {
         return this.isPaused;
      }
   }

   public void stop() {
      synchronized(this) {
         this.isPaused = false;
         this.isStopped = true;
         this.notifyAll();
      }

      if (this.playingThread != null) {
         try {
            this.playingThread.join();
         } catch (InterruptedException var2) {
            LOGGER.log(Level.SEVERE, "join() failed", var2);
         }
      }

   }

   public boolean isStopped() {
      synchronized(this) {
         return this.isStopped;
      }
   }

   public void skipForward() {
      this.skip(1);
   }

   public void skipBackward() {
      this.skip(-1);
   }

   private void skip(int items) {
      if (playlist.size() != 0) {
         boolean playAllowed = this.isPlaying();
         if (this.shuffle) {
            int rand = (new Random()).nextInt(playlist.size());
            this.playingIndex = rand;
         } else {
            this.playingIndex += items;
            if (this.playingIndex > playlist.size() - 1) {
               if (this.repeat) {
                  this.playingIndex = 0;
               } else {
                  this.playingIndex = playlist.size() - 1;
                  playAllowed = false;
               }
            } else if (this.playingIndex < 0) {
               if (this.repeat) {
                  this.playingIndex = playlist.size() - 1;
               } else {
                  this.playingIndex = 0;
                  playAllowed = false;
               }
            }
         }

         this.stop();
         if (playAllowed) {
            this.play();
         }

      }
   }

   public MP3Player setVolume(int volume) {
      if (volume >= 0 && volume <= 100) {
         this.volume = volume;
         return this;
      } else {
         throw new IllegalArgumentException("Wrong value for volume, must be in interval [0..100].");
      }
   }

   public int getVolume() {
      return this.volume;
   }

   public MP3Player setShuffle(boolean shuffle) {
      this.shuffle = shuffle;
      return this;
   }

   public boolean isShuffle() {
      return this.shuffle;
   }

   public MP3Player setRepeat(boolean repeat) {
      this.repeat = repeat;
      return this;
   }

   public boolean isRepeat() {
      return this.repeat;
   }

   private void setVolume(SourceDataLine source, int volume) {
      try {
         FloatControl gainControl = (FloatControl)source.getControl(Type.MASTER_GAIN);
         BooleanControl muteControl = (BooleanControl)source.getControl(javax.sound.sampled.BooleanControl.Type.MUTE);
         if (volume == 0) {
            muteControl.setValue(true);
         } else {
            muteControl.setValue(false);
            gainControl.setValue((float)(Math.log((double)volume / 100.0D) / Math.log(10.0D) * 20.0D));
         }
      } catch (Exception var5) {
         LOGGER.log(Level.WARNING, "unable to set the volume to the provided source", var5);
      }

   }

   public int getPosition() {
      int pos = 0;
      if (this.playingSource != null) {
         pos = (int)(this.playingSource.getMicrosecondPosition() / 1000L);
      }

      return pos;
   }

   private byte[] toByteArray(short[] ss, int offs, int len) {
      byte[] bb = new byte[len * 2];

      short s;
      for(int var5 = 0; len-- > 0; bb[var5++] = (byte)(s >>> 8)) {
         s = ss[offs++];
         bb[var5++] = (byte)s;
      }

      return bb;
   }

   private void readObject(ObjectInputStream objectInputStream) throws ClassNotFoundException, IOException {
      objectInputStream.defaultReadObject();
   }

   public File getCurrentSong() {
      return (File)playlist.get(this.playingIndex);
   }

   public void addToPlayList(File paramFile) {
      try {
         playlist.add(paramFile.toURI().toURL());
      } catch (Exception var3) {
         throw new RuntimeException(var3);
      }
   }
}
