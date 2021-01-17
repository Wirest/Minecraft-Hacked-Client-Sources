// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.openal;

import org.lwjgl.Sys;
import java.net.URL;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.IOException;
import org.newdawn.slick.util.ResourceLoader;
import org.lwjgl.openal.OpenALException;
import java.security.AccessController;
import org.lwjgl.openal.AL;
import java.security.PrivilegedAction;
import org.newdawn.slick.util.Log;
import org.lwjgl.openal.AL10;
import org.lwjgl.BufferUtils;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;

public class SoundStore
{
    private static SoundStore store;
    private boolean sounds;
    private boolean music;
    private boolean soundWorks;
    private int sourceCount;
    private HashMap loaded;
    private int currentMusic;
    private IntBuffer sources;
    private int nextSource;
    private boolean inited;
    private MODSound mod;
    private OpenALStreamPlayer stream;
    private float musicVolume;
    private float soundVolume;
    private float lastCurrentMusicVolume;
    private boolean paused;
    private boolean deferred;
    private FloatBuffer sourceVel;
    private FloatBuffer sourcePos;
    private int maxSources;
    
    static {
        SoundStore.store = new SoundStore();
    }
    
    private SoundStore() {
        this.loaded = new HashMap();
        this.currentMusic = -1;
        this.inited = false;
        this.musicVolume = 1.0f;
        this.soundVolume = 1.0f;
        this.lastCurrentMusicVolume = 1.0f;
        this.sourceVel = BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f });
        this.sourcePos = BufferUtils.createFloatBuffer(3);
        this.maxSources = 64;
    }
    
    public void clear() {
        SoundStore.store = new SoundStore();
    }
    
    public void disable() {
        this.inited = true;
    }
    
    public void setDeferredLoading(final boolean deferred) {
        this.deferred = deferred;
    }
    
    public boolean isDeferredLoading() {
        return this.deferred;
    }
    
    public void setMusicOn(final boolean music) {
        if (this.soundWorks) {
            this.music = music;
            if (music) {
                this.restartLoop();
                this.setMusicVolume(this.musicVolume);
            }
            else {
                this.pauseLoop();
            }
        }
    }
    
    public boolean isMusicOn() {
        return this.music;
    }
    
    public void setMusicVolume(float volume) {
        if (volume < 0.0f) {
            volume = 0.0f;
        }
        if (volume > 1.0f) {
            volume = 1.0f;
        }
        this.musicVolume = volume;
        if (this.soundWorks) {
            AL10.alSourcef(this.sources.get(0), 4106, this.lastCurrentMusicVolume * this.musicVolume);
        }
    }
    
    public float getCurrentMusicVolume() {
        return this.lastCurrentMusicVolume;
    }
    
    public void setCurrentMusicVolume(float volume) {
        if (volume < 0.0f) {
            volume = 0.0f;
        }
        if (volume > 1.0f) {
            volume = 1.0f;
        }
        if (this.soundWorks) {
            this.lastCurrentMusicVolume = volume;
            AL10.alSourcef(this.sources.get(0), 4106, this.lastCurrentMusicVolume * this.musicVolume);
        }
    }
    
    public void setSoundVolume(float volume) {
        if (volume < 0.0f) {
            volume = 0.0f;
        }
        this.soundVolume = volume;
    }
    
    public boolean soundWorks() {
        return this.soundWorks;
    }
    
    public boolean musicOn() {
        return this.music;
    }
    
    public float getSoundVolume() {
        return this.soundVolume;
    }
    
    public float getMusicVolume() {
        return this.musicVolume;
    }
    
    public int getSource(final int index) {
        if (!this.soundWorks) {
            return -1;
        }
        if (index < 0) {
            return -1;
        }
        return this.sources.get(index);
    }
    
    public void setSoundsOn(final boolean sounds) {
        if (this.soundWorks) {
            this.sounds = sounds;
        }
    }
    
    public boolean soundsOn() {
        return this.sounds;
    }
    
    public void setMaxSources(final int max) {
        this.maxSources = max;
    }
    
    public void init() {
        if (this.inited) {
            return;
        }
        Log.info("Initialising sounds..");
        this.inited = true;
        AccessController.doPrivileged((PrivilegedAction<Object>)new PrivilegedAction() {
            @Override
            public Object run() {
                try {
                    AL.create();
                    SoundStore.access$0(SoundStore.this, true);
                    SoundStore.access$1(SoundStore.this, true);
                    SoundStore.access$2(SoundStore.this, true);
                    Log.info("- Sound works");
                }
                catch (Exception e) {
                    Log.error("Sound initialisation failure.");
                    Log.error(e);
                    SoundStore.access$0(SoundStore.this, false);
                    SoundStore.access$1(SoundStore.this, false);
                    SoundStore.access$2(SoundStore.this, false);
                }
                return null;
            }
        });
        if (this.soundWorks) {
            this.sourceCount = 0;
            this.sources = BufferUtils.createIntBuffer(this.maxSources);
            while (AL10.alGetError() == 0) {
                final IntBuffer temp = BufferUtils.createIntBuffer(1);
                try {
                    AL10.alGenSources(temp);
                    if (AL10.alGetError() != 0) {
                        continue;
                    }
                    ++this.sourceCount;
                    this.sources.put(temp.get(0));
                    if (this.sourceCount <= this.maxSources - 1) {
                        continue;
                    }
                }
                catch (OpenALException e) {}
                break;
            }
            Log.info("- " + this.sourceCount + " OpenAL source available");
            if (AL10.alGetError() != 0) {
                this.sounds = false;
                this.music = false;
                this.soundWorks = false;
                Log.error("- AL init failed");
            }
            else {
                final FloatBuffer listenerOri = BufferUtils.createFloatBuffer(6).put(new float[] { 0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f });
                final FloatBuffer listenerVel = BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f });
                final FloatBuffer listenerPos = BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f });
                listenerPos.flip();
                listenerVel.flip();
                listenerOri.flip();
                AL10.alListener(4100, listenerPos);
                AL10.alListener(4102, listenerVel);
                AL10.alListener(4111, listenerOri);
                Log.info("- Sounds source generated");
            }
        }
    }
    
    void stopSource(final int index) {
        AL10.alSourceStop(this.sources.get(index));
    }
    
    int playAsSound(final int buffer, final float pitch, final float gain, final boolean loop) {
        return this.playAsSoundAt(buffer, pitch, gain, loop, 0.0f, 0.0f, 0.0f);
    }
    
    int playAsSoundAt(final int buffer, final float pitch, float gain, final boolean loop, final float x, final float y, final float z) {
        gain *= this.soundVolume;
        if (gain == 0.0f) {
            gain = 0.001f;
        }
        if (!this.soundWorks || !this.sounds) {
            return -1;
        }
        final int nextSource = this.findFreeSource();
        if (nextSource == -1) {
            return -1;
        }
        AL10.alSourceStop(this.sources.get(nextSource));
        AL10.alSourcei(this.sources.get(nextSource), 4105, buffer);
        AL10.alSourcef(this.sources.get(nextSource), 4099, pitch);
        AL10.alSourcef(this.sources.get(nextSource), 4106, gain);
        AL10.alSourcei(this.sources.get(nextSource), 4103, loop ? 1 : 0);
        this.sourcePos.clear();
        this.sourceVel.clear();
        this.sourceVel.put(new float[] { 0.0f, 0.0f, 0.0f });
        this.sourcePos.put(new float[] { x, y, z });
        this.sourcePos.flip();
        this.sourceVel.flip();
        AL10.alSource(this.sources.get(nextSource), 4100, this.sourcePos);
        AL10.alSource(this.sources.get(nextSource), 4102, this.sourceVel);
        AL10.alSourcePlay(this.sources.get(nextSource));
        return nextSource;
    }
    
    boolean isPlaying(final int index) {
        final int state = AL10.alGetSourcei(this.sources.get(index), 4112);
        return state == 4114;
    }
    
    private int findFreeSource() {
        for (int i = 1; i < this.sourceCount - 1; ++i) {
            final int state = AL10.alGetSourcei(this.sources.get(i), 4112);
            if (state != 4114 && state != 4115) {
                return i;
            }
        }
        return -1;
    }
    
    void playAsMusic(final int buffer, final float pitch, final float gain, final boolean loop) {
        this.paused = false;
        this.setMOD(null);
        if (this.soundWorks) {
            if (this.currentMusic != -1) {
                AL10.alSourceStop(this.sources.get(0));
            }
            this.getMusicSource();
            AL10.alSourcei(this.sources.get(0), 4105, buffer);
            AL10.alSourcef(this.sources.get(0), 4099, pitch);
            AL10.alSourcei(this.sources.get(0), 4103, loop ? 1 : 0);
            this.currentMusic = this.sources.get(0);
            if (!this.music) {
                this.pauseLoop();
            }
            else {
                AL10.alSourcePlay(this.sources.get(0));
            }
        }
    }
    
    private int getMusicSource() {
        return this.sources.get(0);
    }
    
    public void setMusicPitch(final float pitch) {
        if (this.soundWorks) {
            AL10.alSourcef(this.sources.get(0), 4099, pitch);
        }
    }
    
    public void pauseLoop() {
        if (this.soundWorks && this.currentMusic != -1) {
            this.paused = true;
            AL10.alSourcePause(this.currentMusic);
        }
    }
    
    public void restartLoop() {
        if (this.music && this.soundWorks && this.currentMusic != -1) {
            this.paused = false;
            AL10.alSourcePlay(this.currentMusic);
        }
    }
    
    boolean isPlaying(final OpenALStreamPlayer player) {
        return this.stream == player;
    }
    
    public Audio getMOD(final String ref) throws IOException {
        return this.getMOD(ref, ResourceLoader.getResourceAsStream(ref));
    }
    
    public Audio getMOD(final InputStream in) throws IOException {
        return this.getMOD(in.toString(), in);
    }
    
    public Audio getMOD(final String ref, final InputStream in) throws IOException {
        if (!this.soundWorks) {
            return new NullAudio();
        }
        if (!this.inited) {
            throw new RuntimeException("Can't load sounds until SoundStore is init(). Use the container init() method.");
        }
        if (this.deferred) {
            return new DeferredSound(ref, in, 3);
        }
        return new MODSound(this, in);
    }
    
    public Audio getAIF(final String ref) throws IOException {
        return this.getAIF(ref, ResourceLoader.getResourceAsStream(ref));
    }
    
    public Audio getAIF(final InputStream in) throws IOException {
        return this.getAIF(in.toString(), in);
    }
    
    public Audio getAIF(final String ref, InputStream in) throws IOException {
        in = new BufferedInputStream(in);
        if (!this.soundWorks) {
            return new NullAudio();
        }
        if (!this.inited) {
            throw new RuntimeException("Can't load sounds until SoundStore is init(). Use the container init() method.");
        }
        if (this.deferred) {
            return new DeferredSound(ref, in, 4);
        }
        int buffer = -1;
        if (this.loaded.get(ref) != null) {
            buffer = this.loaded.get(ref);
        }
        else {
            try {
                final IntBuffer buf = BufferUtils.createIntBuffer(1);
                final AiffData data = AiffData.create(in);
                AL10.alGenBuffers(buf);
                AL10.alBufferData(buf.get(0), data.format, data.data, data.samplerate);
                this.loaded.put(ref, new Integer(buf.get(0)));
                buffer = buf.get(0);
            }
            catch (Exception e) {
                Log.error(e);
                final IOException x = new IOException("Failed to load: " + ref);
                x.initCause(e);
                throw x;
            }
        }
        if (buffer == -1) {
            throw new IOException("Unable to load: " + ref);
        }
        return new AudioImpl(this, buffer);
    }
    
    public Audio getWAV(final String ref) throws IOException {
        return this.getWAV(ref, ResourceLoader.getResourceAsStream(ref));
    }
    
    public Audio getWAV(final InputStream in) throws IOException {
        return this.getWAV(in.toString(), in);
    }
    
    public Audio getWAV(final String ref, final InputStream in) throws IOException {
        if (!this.soundWorks) {
            return new NullAudio();
        }
        if (!this.inited) {
            throw new RuntimeException("Can't load sounds until SoundStore is init(). Use the container init() method.");
        }
        if (this.deferred) {
            return new DeferredSound(ref, in, 2);
        }
        int buffer = -1;
        if (this.loaded.get(ref) != null) {
            buffer = this.loaded.get(ref);
        }
        else {
            try {
                final IntBuffer buf = BufferUtils.createIntBuffer(1);
                final WaveData data = WaveData.create(in);
                AL10.alGenBuffers(buf);
                AL10.alBufferData(buf.get(0), data.format, data.data, data.samplerate);
                this.loaded.put(ref, new Integer(buf.get(0)));
                buffer = buf.get(0);
            }
            catch (Exception e) {
                Log.error(e);
                final IOException x = new IOException("Failed to load: " + ref);
                x.initCause(e);
                throw x;
            }
        }
        if (buffer == -1) {
            throw new IOException("Unable to load: " + ref);
        }
        return new AudioImpl(this, buffer);
    }
    
    public Audio getOggStream(final String ref) throws IOException {
        if (!this.soundWorks) {
            return new NullAudio();
        }
        this.setMOD(null);
        this.setStream(null);
        if (this.currentMusic != -1) {
            AL10.alSourceStop(this.sources.get(0));
        }
        this.getMusicSource();
        this.currentMusic = this.sources.get(0);
        return new StreamSound(new OpenALStreamPlayer(this.currentMusic, ref));
    }
    
    public Audio getOggStream(final URL ref) throws IOException {
        if (!this.soundWorks) {
            return new NullAudio();
        }
        this.setMOD(null);
        this.setStream(null);
        if (this.currentMusic != -1) {
            AL10.alSourceStop(this.sources.get(0));
        }
        this.getMusicSource();
        this.currentMusic = this.sources.get(0);
        return new StreamSound(new OpenALStreamPlayer(this.currentMusic, ref));
    }
    
    public Audio getOgg(final String ref) throws IOException {
        return this.getOgg(ref, ResourceLoader.getResourceAsStream(ref));
    }
    
    public Audio getOgg(final InputStream in) throws IOException {
        return this.getOgg(in.toString(), in);
    }
    
    public Audio getOgg(final String ref, final InputStream in) throws IOException {
        if (!this.soundWorks) {
            return new NullAudio();
        }
        if (!this.inited) {
            throw new RuntimeException("Can't load sounds until SoundStore is init(). Use the container init() method.");
        }
        if (this.deferred) {
            return new DeferredSound(ref, in, 1);
        }
        int buffer = -1;
        if (this.loaded.get(ref) != null) {
            buffer = this.loaded.get(ref);
        }
        else {
            try {
                final IntBuffer buf = BufferUtils.createIntBuffer(1);
                final OggDecoder decoder = new OggDecoder();
                final OggData ogg = decoder.getData(in);
                AL10.alGenBuffers(buf);
                AL10.alBufferData(buf.get(0), (ogg.channels > 1) ? 4355 : 4353, ogg.data, ogg.rate);
                this.loaded.put(ref, new Integer(buf.get(0)));
                buffer = buf.get(0);
            }
            catch (Exception e) {
                Log.error(e);
                Sys.alert("Error", "Failed to load: " + ref + " - " + e.getMessage());
                throw new IOException("Unable to load: " + ref);
            }
        }
        if (buffer == -1) {
            throw new IOException("Unable to load: " + ref);
        }
        return new AudioImpl(this, buffer);
    }
    
    void setMOD(final MODSound sound) {
        if (!this.soundWorks) {
            return;
        }
        this.currentMusic = this.sources.get(0);
        this.stopSource(0);
        if ((this.mod = sound) != null) {
            this.stream = null;
        }
        this.paused = false;
    }
    
    void setStream(final OpenALStreamPlayer stream) {
        if (!this.soundWorks) {
            return;
        }
        this.currentMusic = this.sources.get(0);
        if ((this.stream = stream) != null) {
            this.mod = null;
        }
        this.paused = false;
    }
    
    public void poll(final int delta) {
        if (!this.soundWorks) {
            return;
        }
        if (this.paused) {
            return;
        }
        if (this.music) {
            if (this.mod != null) {
                try {
                    this.mod.poll();
                }
                catch (OpenALException e) {
                    Log.error("Error with OpenGL MOD Player on this this platform");
                    Log.error(e);
                    this.mod = null;
                }
            }
            if (this.stream != null) {
                try {
                    this.stream.update();
                }
                catch (OpenALException e) {
                    Log.error("Error with OpenGL Streaming Player on this this platform");
                    Log.error(e);
                    this.mod = null;
                }
            }
        }
    }
    
    public boolean isMusicPlaying() {
        if (!this.soundWorks) {
            return false;
        }
        final int state = AL10.alGetSourcei(this.sources.get(0), 4112);
        return state == 4114 || state == 4115;
    }
    
    public static SoundStore get() {
        return SoundStore.store;
    }
    
    public void stopSoundEffect(final int id) {
        AL10.alSourceStop(id);
    }
    
    public int getSourceCount() {
        return this.sourceCount;
    }
    
    static /* synthetic */ void access$0(final SoundStore soundStore, final boolean soundWorks) {
        soundStore.soundWorks = soundWorks;
    }
    
    static /* synthetic */ void access$1(final SoundStore soundStore, final boolean sounds) {
        soundStore.sounds = sounds;
    }
    
    static /* synthetic */ void access$2(final SoundStore soundStore, final boolean music) {
        soundStore.music = music;
    }
}
