// 
// Decompiled by Procyon v0.5.36
// 

package paulscode.sound;

import java.net.URL;
import java.util.ListIterator;
import java.util.LinkedList;
import javax.sound.sampled.AudioFormat;

public class Source
{
    protected Class libraryType;
    private static final boolean GET = false;
    private static final boolean SET = true;
    private static final boolean XXX = false;
    private SoundSystemLogger logger;
    public boolean rawDataStream;
    public AudioFormat rawDataFormat;
    public boolean temporary;
    public boolean priority;
    public boolean toStream;
    public boolean toLoop;
    public boolean toPlay;
    public String sourcename;
    public FilenameURL filenameURL;
    public Vector3D position;
    public int attModel;
    public float distOrRoll;
    public Vector3D velocity;
    public float gain;
    public float sourceVolume;
    protected float pitch;
    public float distanceFromListener;
    public Channel channel;
    public SoundBuffer soundBuffer;
    private boolean active;
    private boolean stopped;
    private boolean paused;
    protected ICodec codec;
    protected ICodec nextCodec;
    protected LinkedList<SoundBuffer> nextBuffers;
    protected LinkedList<FilenameURL> soundSequenceQueue;
    protected final Object soundSequenceLock;
    public boolean preLoad;
    protected float fadeOutGain;
    protected float fadeInGain;
    protected long fadeOutMilis;
    protected long fadeInMilis;
    protected long lastFadeCheck;
    
    public Source(final boolean priority, final boolean toStream, final boolean toLoop, final String sourcename, final FilenameURL filenameURL, final SoundBuffer soundBuffer, final float x, final float y, final float z, final int attModel, final float distOrRoll, final boolean temporary) {
        this.libraryType = Library.class;
        this.rawDataStream = false;
        this.rawDataFormat = null;
        this.temporary = false;
        this.priority = false;
        this.toStream = false;
        this.toLoop = false;
        this.toPlay = false;
        this.sourcename = "";
        this.filenameURL = null;
        this.attModel = 0;
        this.distOrRoll = 0.0f;
        this.gain = 1.0f;
        this.sourceVolume = 1.0f;
        this.pitch = 1.0f;
        this.distanceFromListener = 0.0f;
        this.channel = null;
        this.soundBuffer = null;
        this.active = true;
        this.stopped = true;
        this.paused = false;
        this.codec = null;
        this.nextCodec = null;
        this.nextBuffers = null;
        this.soundSequenceQueue = null;
        this.soundSequenceLock = new Object();
        this.preLoad = false;
        this.fadeOutGain = -1.0f;
        this.fadeInGain = 1.0f;
        this.fadeOutMilis = 0L;
        this.fadeInMilis = 0L;
        this.lastFadeCheck = 0L;
        this.logger = SoundSystemConfig.getLogger();
        this.priority = priority;
        this.toStream = toStream;
        this.toLoop = toLoop;
        this.sourcename = sourcename;
        this.filenameURL = filenameURL;
        this.soundBuffer = soundBuffer;
        this.position = new Vector3D(x, y, z);
        this.attModel = attModel;
        this.distOrRoll = distOrRoll;
        this.velocity = new Vector3D(0.0f, 0.0f, 0.0f);
        this.temporary = temporary;
        if (toStream && filenameURL != null) {
            this.codec = SoundSystemConfig.getCodec(filenameURL.getFilename());
        }
    }
    
    public Source(final Source old, final SoundBuffer soundBuffer) {
        this.libraryType = Library.class;
        this.rawDataStream = false;
        this.rawDataFormat = null;
        this.temporary = false;
        this.priority = false;
        this.toStream = false;
        this.toLoop = false;
        this.toPlay = false;
        this.sourcename = "";
        this.filenameURL = null;
        this.attModel = 0;
        this.distOrRoll = 0.0f;
        this.gain = 1.0f;
        this.sourceVolume = 1.0f;
        this.pitch = 1.0f;
        this.distanceFromListener = 0.0f;
        this.channel = null;
        this.soundBuffer = null;
        this.active = true;
        this.stopped = true;
        this.paused = false;
        this.codec = null;
        this.nextCodec = null;
        this.nextBuffers = null;
        this.soundSequenceQueue = null;
        this.soundSequenceLock = new Object();
        this.preLoad = false;
        this.fadeOutGain = -1.0f;
        this.fadeInGain = 1.0f;
        this.fadeOutMilis = 0L;
        this.fadeInMilis = 0L;
        this.lastFadeCheck = 0L;
        this.logger = SoundSystemConfig.getLogger();
        this.priority = old.priority;
        this.toStream = old.toStream;
        this.toLoop = old.toLoop;
        this.sourcename = old.sourcename;
        this.filenameURL = old.filenameURL;
        this.position = old.position.clone();
        this.attModel = old.attModel;
        this.distOrRoll = old.distOrRoll;
        this.velocity = old.velocity.clone();
        this.temporary = old.temporary;
        this.sourceVolume = old.sourceVolume;
        this.rawDataStream = old.rawDataStream;
        this.rawDataFormat = old.rawDataFormat;
        this.soundBuffer = soundBuffer;
        if (this.toStream && this.filenameURL != null) {
            this.codec = SoundSystemConfig.getCodec(this.filenameURL.getFilename());
        }
    }
    
    public Source(final AudioFormat audioFormat, final boolean priority, final String sourcename, final float x, final float y, final float z, final int attModel, final float distOrRoll) {
        this.libraryType = Library.class;
        this.rawDataStream = false;
        this.rawDataFormat = null;
        this.temporary = false;
        this.priority = false;
        this.toStream = false;
        this.toLoop = false;
        this.toPlay = false;
        this.sourcename = "";
        this.filenameURL = null;
        this.attModel = 0;
        this.distOrRoll = 0.0f;
        this.gain = 1.0f;
        this.sourceVolume = 1.0f;
        this.pitch = 1.0f;
        this.distanceFromListener = 0.0f;
        this.channel = null;
        this.soundBuffer = null;
        this.active = true;
        this.stopped = true;
        this.paused = false;
        this.codec = null;
        this.nextCodec = null;
        this.nextBuffers = null;
        this.soundSequenceQueue = null;
        this.soundSequenceLock = new Object();
        this.preLoad = false;
        this.fadeOutGain = -1.0f;
        this.fadeInGain = 1.0f;
        this.fadeOutMilis = 0L;
        this.fadeInMilis = 0L;
        this.lastFadeCheck = 0L;
        this.logger = SoundSystemConfig.getLogger();
        this.priority = priority;
        this.toStream = true;
        this.toLoop = false;
        this.sourcename = sourcename;
        this.filenameURL = null;
        this.soundBuffer = null;
        this.position = new Vector3D(x, y, z);
        this.attModel = attModel;
        this.distOrRoll = distOrRoll;
        this.velocity = new Vector3D(0.0f, 0.0f, 0.0f);
        this.temporary = false;
        this.rawDataStream = true;
        this.rawDataFormat = audioFormat;
    }
    
    public void cleanup() {
        if (this.codec != null) {
            this.codec.cleanup();
        }
        synchronized (this.soundSequenceLock) {
            if (this.soundSequenceQueue != null) {
                this.soundSequenceQueue.clear();
            }
            this.soundSequenceQueue = null;
        }
        this.sourcename = null;
        this.filenameURL = null;
        this.position = null;
        this.soundBuffer = null;
        this.codec = null;
    }
    
    public void queueSound(final FilenameURL filenameURL) {
        if (!this.toStream) {
            this.errorMessage("Method 'queueSound' may only be used for streaming and MIDI sources.");
            return;
        }
        if (filenameURL == null) {
            this.errorMessage("File not specified in method 'queueSound'");
            return;
        }
        synchronized (this.soundSequenceLock) {
            if (this.soundSequenceQueue == null) {
                this.soundSequenceQueue = new LinkedList<FilenameURL>();
            }
            this.soundSequenceQueue.add(filenameURL);
        }
    }
    
    public void dequeueSound(final String filename) {
        if (!this.toStream) {
            this.errorMessage("Method 'dequeueSound' may only be used for streaming and MIDI sources.");
            return;
        }
        if (filename == null || filename.equals("")) {
            this.errorMessage("Filename not specified in method 'dequeueSound'");
            return;
        }
        synchronized (this.soundSequenceLock) {
            if (this.soundSequenceQueue != null) {
                final ListIterator<FilenameURL> i = this.soundSequenceQueue.listIterator();
                while (i.hasNext()) {
                    if (i.next().getFilename().equals(filename)) {
                        i.remove();
                        break;
                    }
                }
            }
        }
    }
    
    public void fadeOut(final FilenameURL filenameURL, final long milis) {
        if (!this.toStream) {
            this.errorMessage("Method 'fadeOut' may only be used for streaming and MIDI sources.");
            return;
        }
        if (milis < 0L) {
            this.errorMessage("Miliseconds may not be negative in method 'fadeOut'.");
            return;
        }
        this.fadeOutMilis = milis;
        this.fadeInMilis = 0L;
        this.fadeOutGain = 1.0f;
        this.lastFadeCheck = System.currentTimeMillis();
        synchronized (this.soundSequenceLock) {
            if (this.soundSequenceQueue != null) {
                this.soundSequenceQueue.clear();
            }
            if (filenameURL != null) {
                if (this.soundSequenceQueue == null) {
                    this.soundSequenceQueue = new LinkedList<FilenameURL>();
                }
                this.soundSequenceQueue.add(filenameURL);
            }
        }
    }
    
    public void fadeOutIn(final FilenameURL filenameURL, final long milisOut, final long milisIn) {
        if (!this.toStream) {
            this.errorMessage("Method 'fadeOutIn' may only be used for streaming and MIDI sources.");
            return;
        }
        if (filenameURL == null) {
            this.errorMessage("Filename/URL not specified in method 'fadeOutIn'.");
            return;
        }
        if (milisOut < 0L || milisIn < 0L) {
            this.errorMessage("Miliseconds may not be negative in method 'fadeOutIn'.");
            return;
        }
        this.fadeOutMilis = milisOut;
        this.fadeInMilis = milisIn;
        this.fadeOutGain = 1.0f;
        this.lastFadeCheck = System.currentTimeMillis();
        synchronized (this.soundSequenceLock) {
            if (this.soundSequenceQueue == null) {
                this.soundSequenceQueue = new LinkedList<FilenameURL>();
            }
            this.soundSequenceQueue.clear();
            this.soundSequenceQueue.add(filenameURL);
        }
    }
    
    public boolean checkFadeOut() {
        if (!this.toStream) {
            return false;
        }
        if (this.fadeOutGain == -1.0f && this.fadeInGain == 1.0f) {
            return false;
        }
        final long currentTime = System.currentTimeMillis();
        final long milisPast = currentTime - this.lastFadeCheck;
        this.lastFadeCheck = currentTime;
        if (this.fadeOutGain >= 0.0f) {
            if (this.fadeOutMilis == 0L) {
                this.fadeOutGain = -1.0f;
                this.fadeInGain = 0.0f;
                if (!this.incrementSoundSequence()) {
                    this.stop();
                }
                this.positionChanged();
                this.preLoad = true;
                return false;
            }
            final float fadeOutReduction = milisPast / (float)this.fadeOutMilis;
            this.fadeOutGain -= fadeOutReduction;
            if (this.fadeOutGain <= 0.0f) {
                this.fadeOutGain = -1.0f;
                this.fadeInGain = 0.0f;
                if (!this.incrementSoundSequence()) {
                    this.stop();
                }
                this.positionChanged();
                this.preLoad = true;
                return false;
            }
            this.positionChanged();
            return true;
        }
        else {
            if (this.fadeInGain < 1.0f) {
                this.fadeOutGain = -1.0f;
                if (this.fadeInMilis == 0L) {
                    this.fadeOutGain = -1.0f;
                    this.fadeInGain = 1.0f;
                }
                else {
                    final float fadeInIncrease = milisPast / (float)this.fadeInMilis;
                    this.fadeInGain += fadeInIncrease;
                    if (this.fadeInGain >= 1.0f) {
                        this.fadeOutGain = -1.0f;
                        this.fadeInGain = 1.0f;
                    }
                }
                this.positionChanged();
                return true;
            }
            return false;
        }
    }
    
    public boolean incrementSoundSequence() {
        if (!this.toStream) {
            this.errorMessage("Method 'incrementSoundSequence' may only be used for streaming and MIDI sources.");
            return false;
        }
        synchronized (this.soundSequenceLock) {
            if (this.soundSequenceQueue != null && this.soundSequenceQueue.size() > 0) {
                this.filenameURL = this.soundSequenceQueue.remove(0);
                if (this.codec != null) {
                    this.codec.cleanup();
                }
                this.codec = SoundSystemConfig.getCodec(this.filenameURL.getFilename());
                return true;
            }
        }
        return false;
    }
    
    public boolean readBuffersFromNextSoundInSequence() {
        if (!this.toStream) {
            this.errorMessage("Method 'readBuffersFromNextSoundInSequence' may only be used for streaming sources.");
            return false;
        }
        synchronized (this.soundSequenceLock) {
            if (this.soundSequenceQueue != null && this.soundSequenceQueue.size() > 0) {
                if (this.nextCodec != null) {
                    this.nextCodec.cleanup();
                }
                (this.nextCodec = SoundSystemConfig.getCodec(this.soundSequenceQueue.get(0).getFilename())).initialize(this.soundSequenceQueue.get(0).getURL());
                SoundBuffer buffer = null;
                for (int i = 0; i < SoundSystemConfig.getNumberStreamingBuffers() && !this.nextCodec.endOfStream(); ++i) {
                    buffer = this.nextCodec.read();
                    if (buffer != null) {
                        if (this.nextBuffers == null) {
                            this.nextBuffers = new LinkedList<SoundBuffer>();
                        }
                        this.nextBuffers.add(buffer);
                    }
                }
                return true;
            }
        }
        return false;
    }
    
    public int getSoundSequenceQueueSize() {
        if (this.soundSequenceQueue == null) {
            return 0;
        }
        return this.soundSequenceQueue.size();
    }
    
    public void setTemporary(final boolean tmp) {
        this.temporary = tmp;
    }
    
    public void listenerMoved() {
    }
    
    public void setPosition(final float x, final float y, final float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }
    
    public void positionChanged() {
    }
    
    public void setPriority(final boolean pri) {
        this.priority = pri;
    }
    
    public void setLooping(final boolean lp) {
        this.toLoop = lp;
    }
    
    public void setAttenuation(final int model) {
        this.attModel = model;
    }
    
    public void setDistOrRoll(final float dr) {
        this.distOrRoll = dr;
    }
    
    public void setVelocity(final float x, final float y, final float z) {
        this.velocity.x = x;
        this.velocity.y = y;
        this.velocity.z = z;
    }
    
    public float getDistanceFromListener() {
        return this.distanceFromListener;
    }
    
    public void setPitch(final float value) {
        float newPitch = value;
        if (newPitch < 0.5f) {
            newPitch = 0.5f;
        }
        else if (newPitch > 2.0f) {
            newPitch = 2.0f;
        }
        this.pitch = newPitch;
    }
    
    public float getPitch() {
        return this.pitch;
    }
    
    public boolean reverseByteOrder() {
        return SoundSystemConfig.reverseByteOrder(this.libraryType);
    }
    
    public void changeSource(final boolean priority, final boolean toStream, final boolean toLoop, final String sourcename, final FilenameURL filenameURL, final SoundBuffer soundBuffer, final float x, final float y, final float z, final int attModel, final float distOrRoll, final boolean temporary) {
        this.priority = priority;
        this.toStream = toStream;
        this.toLoop = toLoop;
        this.sourcename = sourcename;
        this.filenameURL = filenameURL;
        this.soundBuffer = soundBuffer;
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
        this.attModel = attModel;
        this.distOrRoll = distOrRoll;
        this.temporary = temporary;
    }
    
    public int feedRawAudioData(final Channel c, final byte[] buffer) {
        if (!this.active(false, false)) {
            this.toPlay = true;
            return -1;
        }
        if (this.channel != c) {
            (this.channel = c).close();
            this.channel.setAudioFormat(this.rawDataFormat);
            this.positionChanged();
        }
        this.stopped(true, false);
        this.paused(true, false);
        return this.channel.feedRawAudioData(buffer);
    }
    
    public void play(final Channel c) {
        if (!this.active(false, false)) {
            if (this.toLoop) {
                this.toPlay = true;
            }
            return;
        }
        if (this.channel != c) {
            (this.channel = c).close();
        }
        this.stopped(true, false);
        this.paused(true, false);
    }
    
    public boolean stream() {
        if (this.channel == null) {
            return false;
        }
        if (this.preLoad) {
            if (!this.rawDataStream) {
                return this.preLoad();
            }
            this.preLoad = false;
        }
        if (this.rawDataStream) {
            if (this.stopped() || this.paused()) {
                return true;
            }
            if (this.channel.buffersProcessed() > 0) {
                this.channel.processBuffer();
            }
            return true;
        }
        else {
            if (this.codec == null) {
                return false;
            }
            if (this.stopped()) {
                return false;
            }
            if (this.paused()) {
                return true;
            }
            final int processed = this.channel.buffersProcessed();
            SoundBuffer buffer = null;
            for (int i = 0; i < processed; ++i) {
                buffer = this.codec.read();
                if (buffer != null) {
                    if (buffer.audioData != null) {
                        this.channel.queueBuffer(buffer.audioData);
                    }
                    buffer.cleanup();
                    buffer = null;
                    return true;
                }
                if (this.codec.endOfStream()) {
                    synchronized (this.soundSequenceLock) {
                        if (SoundSystemConfig.getStreamQueueFormatsMatch()) {
                            if (this.soundSequenceQueue != null && this.soundSequenceQueue.size() > 0) {
                                if (this.codec != null) {
                                    this.codec.cleanup();
                                }
                                this.filenameURL = this.soundSequenceQueue.remove(0);
                                (this.codec = SoundSystemConfig.getCodec(this.filenameURL.getFilename())).initialize(this.filenameURL.getURL());
                                buffer = this.codec.read();
                                if (buffer != null) {
                                    if (buffer.audioData != null) {
                                        this.channel.queueBuffer(buffer.audioData);
                                    }
                                    buffer.cleanup();
                                    buffer = null;
                                    return true;
                                }
                            }
                            else if (this.toLoop) {
                                this.codec.initialize(this.filenameURL.getURL());
                                buffer = this.codec.read();
                                if (buffer != null) {
                                    if (buffer.audioData != null) {
                                        this.channel.queueBuffer(buffer.audioData);
                                    }
                                    buffer.cleanup();
                                    buffer = null;
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
            return false;
        }
    }
    
    public boolean preLoad() {
        if (this.channel == null) {
            return false;
        }
        if (this.codec == null) {
            return false;
        }
        SoundBuffer buffer = null;
        boolean noNextBuffers = false;
        synchronized (this.soundSequenceLock) {
            if (this.nextBuffers == null || this.nextBuffers.isEmpty()) {
                noNextBuffers = true;
            }
        }
        if (this.nextCodec != null && !noNextBuffers) {
            this.codec = this.nextCodec;
            this.nextCodec = null;
            synchronized (this.soundSequenceLock) {
                while (!this.nextBuffers.isEmpty()) {
                    buffer = this.nextBuffers.remove(0);
                    if (buffer != null) {
                        if (buffer.audioData != null) {
                            this.channel.queueBuffer(buffer.audioData);
                        }
                        buffer.cleanup();
                        buffer = null;
                    }
                }
            }
        }
        else {
            this.nextCodec = null;
            final URL url = this.filenameURL.getURL();
            this.codec.initialize(url);
            for (int i = 0; i < SoundSystemConfig.getNumberStreamingBuffers(); ++i) {
                buffer = this.codec.read();
                if (buffer != null) {
                    if (buffer.audioData != null) {
                        this.channel.queueBuffer(buffer.audioData);
                    }
                    buffer.cleanup();
                    buffer = null;
                }
            }
        }
        return true;
    }
    
    public void pause() {
        this.toPlay = false;
        this.paused(true, true);
        if (this.channel != null) {
            this.channel.pause();
        }
        else {
            this.errorMessage("Channel null in method 'pause'");
        }
    }
    
    public void stop() {
        this.toPlay = false;
        this.stopped(true, true);
        this.paused(true, false);
        if (this.channel != null) {
            this.channel.stop();
        }
        else {
            this.errorMessage("Channel null in method 'stop'");
        }
    }
    
    public void rewind() {
        if (this.paused(false, false)) {
            this.stop();
        }
        if (this.channel != null) {
            final boolean rePlay = this.playing();
            this.channel.rewind();
            if (this.toStream && rePlay) {
                this.stop();
                this.play(this.channel);
            }
        }
        else {
            this.errorMessage("Channel null in method 'rewind'");
        }
    }
    
    public void flush() {
        if (this.channel != null) {
            this.channel.flush();
        }
        else {
            this.errorMessage("Channel null in method 'flush'");
        }
    }
    
    public void cull() {
        if (!this.active(false, false)) {
            return;
        }
        if (this.playing() && this.toLoop) {
            this.toPlay = true;
        }
        if (this.rawDataStream) {
            this.toPlay = true;
        }
        this.active(true, false);
        if (this.channel != null) {
            this.channel.close();
        }
        this.channel = null;
    }
    
    public void activate() {
        this.active(true, true);
    }
    
    public boolean active() {
        return this.active(false, false);
    }
    
    public boolean playing() {
        return this.channel != null && this.channel.attachedSource == this && !this.paused() && !this.stopped() && this.channel.playing();
    }
    
    public boolean stopped() {
        return this.stopped(false, false);
    }
    
    public boolean paused() {
        return this.paused(false, false);
    }
    
    public float millisecondsPlayed() {
        if (this.channel == null) {
            return -1.0f;
        }
        return this.channel.millisecondsPlayed();
    }
    
    private synchronized boolean active(final boolean action, final boolean value) {
        if (action) {
            this.active = value;
        }
        return this.active;
    }
    
    private synchronized boolean stopped(final boolean action, final boolean value) {
        if (action) {
            this.stopped = value;
        }
        return this.stopped;
    }
    
    private synchronized boolean paused(final boolean action, final boolean value) {
        if (action) {
            this.paused = value;
        }
        return this.paused;
    }
    
    public String getClassName() {
        final String libTitle = SoundSystemConfig.getLibraryTitle(this.libraryType);
        if (libTitle.equals("No Sound")) {
            return "Source";
        }
        return "Source" + libTitle;
    }
    
    protected void message(final String message) {
        this.logger.message(message, 0);
    }
    
    protected void importantMessage(final String message) {
        this.logger.importantMessage(message, 0);
    }
    
    protected boolean errorCheck(final boolean error, final String message) {
        return this.logger.errorCheck(error, this.getClassName(), message, 0);
    }
    
    protected void errorMessage(final String message) {
        this.logger.errorMessage(this.getClassName(), message, 0);
    }
    
    protected void printStackTrace(final Exception e) {
        this.logger.printStackTrace(e, 1);
    }
}
