package info.sigmaclient.management;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import info.sigmaclient.Client;
import info.sigmaclient.json.Json;
import info.sigmaclient.json.JsonArray;
import info.sigmaclient.json.JsonObject;
import info.sigmaclient.json.JsonValue;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Resource;
import javax.net.ssl.HttpsURLConnection;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class MusicManager {
    private static MusicManager instance;
    private String clientId = "QyPi1UIiAXHektIfaZyKDQSp25ZaerWL";
    private HashMap<Long, ResourceLocation> artsLocations = new HashMap<>();
    private Track currentTrack = null;
    private ArrayList<Track> lastResearch = null;
    private MediaPlayer mediaPlayer;
    private File musicFolder;
    private Thread loadingThread = null;
    private double volume = 0.2;
    private ArrayList<Track> playlist = new ArrayList<>();

    public MusicManager() {
        musicFolder = new File(String.valueOf(Minecraft.getMinecraft().mcDataDir.toString()) + File.separator + Client.clientName + File.separator + "music");
        if (!musicFolder.exists()) {
            musicFolder.mkdirs();
        } else {
            for (File f : musicFolder.listFiles()) {
                if (f.getName().endsWith(".mp3") || f.getName().endsWith(".mp3.part")) {
                    f.delete();
                }
            }
        }

        File playlistFile = new File(musicFolder, "playlist.json");
        if (playlistFile.exists()) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(playlistFile)))) {
                String line;
                String result = "";
                while ((line = br.readLine()) != null) {
                    result += line;
                }
                JsonArray playlistArr = Json.parse(result).asArray();
                for (Iterator<JsonValue> it = playlistArr.iterator(); it.hasNext(); ) {
                    JsonObject trackJson = it.next().asObject();
                    Track track = new Track(trackJson);
                    playlist.add(track);
                    if (track.getArtworkUrl() != null) {
                        loadArt(track.getId(), track.getArtworkUrl());
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        final CountDownLatch latch = new CountDownLatch(1);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new JFXPanel(); // initializes JavaFX environment
                latch.countDown();
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void addToPlaylist(Track track) {
        for (Track t : playlist) {
            if (t.getId() == track.getId()) {
                return;
            }
        }
        playlist.add(track);
        savePlaylist();
    }

    public void removeFromPlaylist(Track track) {
        playlist.remove(track);
        savePlaylist();
    }

    private void savePlaylist() {
        File playlistFile = new File(musicFolder, "playlist.json");
        JsonArray playlistJson = new JsonArray();
        for (Track t : playlist) {
            playlistJson.add(t.toJson());
        }
        try (FileOutputStream fos = new FileOutputStream(playlistFile)) {
            fos.write(playlistJson.toString().getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static MusicManager getInstance() {
        if (instance == null) {
            instance = new MusicManager();
        }
        return instance;
    }

    public void loadArt(long id, String url) {
        if (artsLocations.containsKey(id)) {
            return;
        }
        artsLocations.put(id, null);
        MinecraftProfileTexture mpt = new MinecraftProfileTexture(url, new HashMap());
        final ResourceLocation rl = new ResourceLocation("musicart/" + mpt.getHash());
        IImageBuffer iib = new IImageBuffer() {
            ImageBufferDownload ibd = new ImageBufferDownload();

            public BufferedImage parseUserSkin(BufferedImage var1) {
                return var1;
            }

            public void func_152634_a() {
                artsLocations.put(id, rl);
            }
        };
        ThreadDownloadImageData textureArt = new ThreadDownloadImageData((File) null, mpt.getUrl(), (ResourceLocation) null, iib);
        Minecraft.getMinecraft().getTextureManager().loadTexture(rl, textureArt);
    }

    public ResourceLocation getArt(long id) {
        return artsLocations.get(id);
    }

    private JsonValue request(String url) {
        try {
            HttpsURLConnection con = (HttpsURLConnection) new URL(url).openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line = null;
            StringBuilder result = new StringBuilder();
            while ((line = br.readLine()) != null) {
                result.append(line);
            }
            return Json.parse(result.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Track> search(String q) {
        ArrayList<Track> tracks = new ArrayList<>();

        try {
            JsonArray arr = request("https://api.soundcloud.com/tracks?client_id=" + clientId + "&q=" + URLEncoder.encode(q, "UTF-8")).asArray();
            if (arr != null) {

                for (Iterator<JsonValue> rsltArrIterator = arr.iterator(); rsltArrIterator.hasNext(); ) {
                    JsonObject trackJson = rsltArrIterator.next().asObject();
                    long trackId = trackJson.getLong("id", 0);
                    String name = trackJson.getString("title", null);
                    JsonValue artworkUrlJson = trackJson.get("artwork_url");
                    String artworkUrl = artworkUrlJson.isNull() ? null : artworkUrlJson.asString();
                    tracks.add(new Track(trackId, name, artworkUrl));
                    if (artworkUrl != null) {
                        loadArt(trackId, artworkUrl);
                    }
                }

            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        this.lastResearch = tracks;
        return tracks;
    }

    public void play(Track track) {
        this.currentTrack = track;
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        File file = new File(musicFolder, track.getId() + ".mp3");
        if (!file.exists()) {
            if (loadingThread != null) {
                loadingThread.interrupt();
            }
            loadingThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    JsonObject obj = request("https://api.soundcloud.com/i1/tracks/" + track.getId() + "/streams?client_id=" + clientId).asObject();
                    if (obj != null) {
                        String url = obj.getString("http_mp3_128_url", null);
                        File tempFile = new File(musicFolder, track.getId() + ".mp3.part");
                        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                            HttpsURLConnection con = (HttpsURLConnection) new URL(url).openConnection();
                            byte[] buff = new byte[1024];
                            int readed = 0;
                            while ((readed = con.getInputStream().read(buff)) > 0) {
                                fos.write(buff, 0, readed);
                            }
                            fos.close();

                            Files.copy(tempFile.toPath(), file.toPath());
                            tempFile.delete();
                            play(track);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    loadingThread = null;
                }
            });
            loadingThread.start();
        } else {
            Media hit = new Media(file.toURI().toString());
            mediaPlayer = new MediaPlayer(hit);
            mediaPlayer.setVolume(volume);
            mediaPlayer.setAutoPlay(true);
            /*mediaPlayer.setOnStopped(new Runnable() {
                @Override
                public void run() {
                    System.out.println("Stopped");
                }
            });
            mediaPlayer.setOnReady(new Runnable() {
                @Override
                public void run() {
                    System.out.println("Ready");
                }
            });
            mediaPlayer.setOnError(new Runnable() {
                @Override
                public void run() {
                    System.out.println("Error");
                }
            });*/
            mediaPlayer.setOnEndOfMedia(new Runnable() {
                @Override
                public void run() {
                    next();
                }
            });
        }
    }

    public Track getCurrentTrack() {
        return currentTrack;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public Thread getLoadingThread() {
        return loadingThread;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public void next() {
        if (!playlist.isEmpty()) {
            if (currentTrack == null) {
                play(playlist.get(0));
                return;
            } else {
                boolean playNext = false;
                for (Track t : playlist) {
                    if (playNext) {
                        play(t);
                        return;
                    } else if (t.getId() == currentTrack.getId()) {
                        playNext = true;
                    }
                }
            }
        }
        if (lastResearch != null && !lastResearch.isEmpty()) {
            if (currentTrack == null) {
                play(lastResearch.get(0));
            } else {
                boolean playNext = false;
                for (Track t : lastResearch) {
                    if (playNext) {
                        play(t);
                        break;
                    } else if (t.getId() == currentTrack.getId()) {
                        playNext = true;
                    }
                }
            }
        }
    }

    public List<Track> getPlaylist() {
        return playlist;
    }

    public static class Track {
        private long id;
        private String name;
        private String artworkUrl;

        public Track(long id, String name, String artworkUrl) {
            this.id = id;
            this.name = name;
            this.artworkUrl = artworkUrl;
        }

        public Track(JsonObject trackJson) {
            id = trackJson.getLong("id", 0);
            name = trackJson.getString("title", null);
            JsonValue artworkUrlJson = trackJson.get("artwork_url");
            artworkUrl = artworkUrlJson.isNull() ? null : artworkUrlJson.asString();
        }

        public JsonObject toJson() {
            JsonObject trackJson = new JsonObject();
            trackJson.set("id", id);
            trackJson.set("title", name);
            trackJson.set("artwork_url", artworkUrl);
            return trackJson;
        }

        public long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getArtworkUrl() {
            return artworkUrl;
        }
    }

    public enum Icon {
        PLAY(new ResourceLocation("textures/icons/play2.png"));

        ResourceLocation rl;

        Icon(ResourceLocation rl) {
            this.rl = rl;
        }

        public ResourceLocation getRl() {
            return rl;
        }
    }
}
