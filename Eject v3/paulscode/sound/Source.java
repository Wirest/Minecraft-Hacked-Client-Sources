package paulscode.sound;

import javax.sound.sampled.AudioFormat;
import java.net.URL;
import java.util.LinkedList;
import java.util.ListIterator;

public class Source {
    private static final boolean GET = false;
    private static final boolean SET = true;
    private static final boolean XXX = false;
    protected final Object soundSequenceLock = new Object();
    public boolean rawDataStream = false;
    public AudioFormat rawDataFormat = null;
    public boolean temporary = false;
    public boolean priority = false;
    public boolean toStream = false;
    public boolean toLoop = false;
    public boolean toPlay = false;
    public String sourcename = "";
    public FilenameURL filenameURL = null;
    public Vector3D position;
    public int attModel = 0;
    public float distOrRoll = 0.0F;
    public Vector3D velocity;
    public float gain = 1.0F;
    public float sourceVolume = 1.0F;
    public float distanceFromListener = 0.0F;
    public Channel channel = null;
    public SoundBuffer soundBuffer = null;
    public boolean preLoad = false;
    protected Class libraryType = Library.class;
    protected float pitch = 1.0F;
    protected ICodec codec = null;
    protected ICodec nextCodec = null;
    protected LinkedList<SoundBuffer> nextBuffers = null;
    protected LinkedList<FilenameURL> soundSequenceQueue = null;
    protected float fadeOutGain = -1.0F;
    protected float fadeInGain = 1.0F;
    protected long fadeOutMilis = 0L;
    protected long fadeInMilis = 0L;
    protected long lastFadeCheck = 0L;
    private SoundSystemLogger logger = SoundSystemConfig.getLogger();
    private boolean active = true;
    private boolean stopped = true;
    private boolean paused = false;

    public Source(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, String paramString, FilenameURL paramFilenameURL, SoundBuffer paramSoundBuffer, float paramFloat1, float paramFloat2, float paramFloat3, int paramInt, float paramFloat4, boolean paramBoolean4) {
        this.priority = paramBoolean1;
        this.toStream = paramBoolean2;
        this.toLoop = paramBoolean3;
        this.sourcename = paramString;
        this.filenameURL = paramFilenameURL;
        this.soundBuffer = paramSoundBuffer;
        this.position = new Vector3D(paramFloat1, paramFloat2, paramFloat3);
        this.attModel = paramInt;
        this.distOrRoll = paramFloat4;
        this.velocity = new Vector3D(0.0F, 0.0F, 0.0F);
        this.temporary = paramBoolean4;
        if ((paramBoolean2) && (paramFilenameURL != null)) {
            this.codec = SoundSystemConfig.getCodec(paramFilenameURL.getFilename());
        }
    }

    public Source(Source paramSource, SoundBuffer paramSoundBuffer) {
        this.priority = paramSource.priority;
        this.toStream = paramSource.toStream;
        this.toLoop = paramSource.toLoop;
        this.sourcename = paramSource.sourcename;
        this.filenameURL = paramSource.filenameURL;
        this.position = paramSource.position.clone();
        this.attModel = paramSource.attModel;
        this.distOrRoll = paramSource.distOrRoll;
        this.velocity = paramSource.velocity.clone();
        this.temporary = paramSource.temporary;
        this.sourceVolume = paramSource.sourceVolume;
        this.rawDataStream = paramSource.rawDataStream;
        this.rawDataFormat = paramSource.rawDataFormat;
        this.soundBuffer = paramSoundBuffer;
        if ((this.toStream) && (this.filenameURL != null)) {
            this.codec = SoundSystemConfig.getCodec(this.filenameURL.getFilename());
        }
    }

    public Source(AudioFormat paramAudioFormat, boolean paramBoolean, String paramString, float paramFloat1, float paramFloat2, float paramFloat3, int paramInt, float paramFloat4) {
        this.priority = paramBoolean;
        this.toStream = true;
        this.toLoop = false;
        this.sourcename = paramString;
        this.filenameURL = null;
        this.soundBuffer = null;
        this.position = new Vector3D(paramFloat1, paramFloat2, paramFloat3);
        this.attModel = paramInt;
        this.distOrRoll = paramFloat4;
        this.velocity = new Vector3D(0.0F, 0.0F, 0.0F);
        this.temporary = false;
        this.rawDataStream = true;
        this.rawDataFormat = paramAudioFormat;
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

    public void queueSound(FilenameURL paramFilenameURL) {
        if (!this.toStream) {
            errorMessage("Method 'queueSound' may only be used for streaming and MIDI sources.");
            return;
        }
        if (paramFilenameURL == null) {
            errorMessage("File not specified in method 'queueSound'");
            return;
        }
        synchronized (this.soundSequenceLock) {
            if (this.soundSequenceQueue == null) {
                this.soundSequenceQueue = new LinkedList();
            }
            this.soundSequenceQueue.add(paramFilenameURL);
        }
    }

    public void dequeueSound(String paramString) {
        if (!this.toStream) {
            errorMessage("Method 'dequeueSound' may only be used for streaming and MIDI sources.");
            return;
        }
        if ((paramString == null) || (paramString.equals(""))) {
            errorMessage("Filename not specified in method 'dequeueSound'");
            return;
        }
        synchronized (this.soundSequenceLock) {
            if (this.soundSequenceQueue != null) {
                ListIterator localListIterator = this.soundSequenceQueue.listIterator();
                while (localListIterator.hasNext()) {
                    if (((FilenameURL) localListIterator.next()).getFilename().equals(paramString)) {
                        localListIterator.remove();
                    }
                }
            }
        }
    }

    public void fadeOut(FilenameURL paramFilenameURL, long paramLong) {
        if (!this.toStream) {
            errorMessage("Method 'fadeOut' may only be used for streaming and MIDI sources.");
            return;
        }
        if (paramLong < 0L) {
            errorMessage("Miliseconds may not be negative in method 'fadeOut'.");
            return;
        }
        this.fadeOutMilis = paramLong;
        this.fadeInMilis = 0L;
        this.fadeOutGain = 1.0F;
        this.lastFadeCheck = System.currentTimeMillis();
        synchronized (this.soundSequenceLock) {
            if (this.soundSequenceQueue != null) {
                this.soundSequenceQueue.clear();
            }
            if (paramFilenameURL != null) {
                if (this.soundSequenceQueue == null) {
                    this.soundSequenceQueue = new LinkedList();
                }
                this.soundSequenceQueue.add(paramFilenameURL);
            }
        }
    }

    public void fadeOutIn(FilenameURL paramFilenameURL, long paramLong1, long paramLong2) {
        if (!this.toStream) {
            errorMessage("Method 'fadeOutIn' may only be used for streaming and MIDI sources.");
            return;
        }
        if (paramFilenameURL == null) {
            errorMessage("Filename/URL not specified in method 'fadeOutIn'.");
            return;
        }
        if ((paramLong1 < 0L) || (paramLong2 < 0L)) {
            errorMessage("Miliseconds may not be negative in method 'fadeOutIn'.");
            return;
        }
        this.fadeOutMilis = paramLong1;
        this.fadeInMilis = paramLong2;
        this.fadeOutGain = 1.0F;
        this.lastFadeCheck = System.currentTimeMillis();
        synchronized (this.soundSequenceLock) {
            if (this.soundSequenceQueue == null) {
                this.soundSequenceQueue = new LinkedList();
            }
            this.soundSequenceQueue.clear();
            this.soundSequenceQueue.add(paramFilenameURL);
        }
    }

    public boolean checkFadeOut() {
        if (!this.toStream) {
            return false;
        }
        if ((this.fadeOutGain == -1.0F) && (this.fadeInGain == 1.0F)) {
            return false;
        }
        long l1 = System.currentTimeMillis();
        long l2 = l1 - this.lastFadeCheck;
        this.lastFadeCheck = l1;
        float f;
        if (this.fadeOutGain >= 0.0F) {
            if (this.fadeOutMilis == 0L) {
                this.fadeOutGain = -1.0F;
                this.fadeInGain = 0.0F;
                if (!incrementSoundSequence()) {
                    stop();
                }
                positionChanged();
                this.preLoad = true;
                return false;
            }
            f = (float) l2 / (float) this.fadeOutMilis;
            this.fadeOutGain -= f;
            if (this.fadeOutGain <= 0.0F) {
                this.fadeOutGain = -1.0F;
                this.fadeInGain = 0.0F;
                if (!incrementSoundSequence()) {
                    stop();
                }
                positionChanged();
                this.preLoad = true;
                return false;
            }
            positionChanged();
            return true;
        }
        if (this.fadeInGain < 1.0F) {
            this.fadeOutGain = -1.0F;
            if (this.fadeInMilis == 0L) {
                this.fadeOutGain = -1.0F;
                this.fadeInGain = 1.0F;
            } else {
                f = (float) l2 / (float) this.fadeInMilis;
                this.fadeInGain += f;
                if (this.fadeInGain >= 1.0F) {
                    this.fadeOutGain = -1.0F;
                    this.fadeInGain = 1.0F;
                }
            }
            positionChanged();
            return true;
        }
        return false;
    }

    public boolean incrementSoundSequence() {
        if (!this.toStream) {
            errorMessage("Method 'incrementSoundSequence' may only be used for streaming and MIDI sources.");
            return false;
        }
        synchronized (this.soundSequenceLock) {
            if ((this.soundSequenceQueue != null) && (this.soundSequenceQueue.size() > 0)) {
                this.filenameURL = ((FilenameURL) this.soundSequenceQueue.remove(0));
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
            errorMessage("Method 'readBuffersFromNextSoundInSequence' may only be used for streaming sources.");
            return false;
        }
        synchronized (this.soundSequenceLock) {
            if ((this.soundSequenceQueue != null) && (this.soundSequenceQueue.size() > 0)) {
                if (this.nextCodec != null) {
                    this.nextCodec.cleanup();
                }
                this.nextCodec = SoundSystemConfig.getCodec(((FilenameURL) this.soundSequenceQueue.get(0)).getFilename());
                this.nextCodec.initialize(((FilenameURL) this.soundSequenceQueue.get(0)).getURL());
                SoundBuffer localSoundBuffer = null;
                for (int i = 0; (i < SoundSystemConfig.getNumberStreamingBuffers()) && (!this.nextCodec.endOfStream()); i++) {
                    localSoundBuffer = this.nextCodec.read();
                    if (localSoundBuffer != null) {
                        if (this.nextBuffers == null) {
                            this.nextBuffers = new LinkedList();
                        }
                        this.nextBuffers.add(localSoundBuffer);
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

    public void setTemporary(boolean paramBoolean) {
        this.temporary = paramBoolean;
    }

    public void listenerMoved() {
    }

    public void setPosition(float paramFloat1, float paramFloat2, float paramFloat3) {
        this.position.x = paramFloat1;
        this.position.y = paramFloat2;
        this.position.z = paramFloat3;
    }

    public void positionChanged() {
    }

    public void setPriority(boolean paramBoolean) {
        this.priority = paramBoolean;
    }

    public void setLooping(boolean paramBoolean) {
        this.toLoop = paramBoolean;
    }

    public void setAttenuation(int paramInt) {
        this.attModel = paramInt;
    }

    public void setDistOrRoll(float paramFloat) {
        this.distOrRoll = paramFloat;
    }

    public void setVelocity(float paramFloat1, float paramFloat2, float paramFloat3) {
        this.velocity.x = paramFloat1;
        this.velocity.y = paramFloat2;
        this.velocity.z = paramFloat3;
    }

    public float getDistanceFromListener() {
        return this.distanceFromListener;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setPitch(float paramFloat) {
        float f = paramFloat;
        if (f < 0.5F) {
            f = 0.5F;
        } else if (f > 2.0F) {
            f = 2.0F;
        }
        this.pitch = f;
    }

    public boolean reverseByteOrder() {
        return SoundSystemConfig.reverseByteOrder(this.libraryType);
    }

    public void changeSource(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, String paramString, FilenameURL paramFilenameURL, SoundBuffer paramSoundBuffer, float paramFloat1, float paramFloat2, float paramFloat3, int paramInt, float paramFloat4, boolean paramBoolean4) {
        this.priority = paramBoolean1;
        this.toStream = paramBoolean2;
        this.toLoop = paramBoolean3;
        this.sourcename = paramString;
        this.filenameURL = paramFilenameURL;
        this.soundBuffer = paramSoundBuffer;
        this.position.x = paramFloat1;
        this.position.y = paramFloat2;
        this.position.z = paramFloat3;
        this.attModel = paramInt;
        this.distOrRoll = paramFloat4;
        this.temporary = paramBoolean4;
    }

    public int feedRawAudioData(Channel paramChannel, byte[] paramArrayOfByte) {
        if (!active(false, false)) {
            this.toPlay = true;
            return -1;
        }
        if (this.channel != paramChannel) {
            this.channel = paramChannel;
            this.channel.close();
            this.channel.setAudioFormat(this.rawDataFormat);
            positionChanged();
        }
        stopped(true, false);
        paused(true, false);
        return this.channel.feedRawAudioData(paramArrayOfByte);
    }

    public void play(Channel paramChannel) {
        if (!active(false, false)) {
            if (this.toLoop) {
                this.toPlay = true;
            }
            return;
        }
        if (this.channel != paramChannel) {
            this.channel = paramChannel;
            this.channel.close();
        }
        stopped(true, false);
        paused(true, false);
    }

    public boolean stream() {
        if (this.channel == null) {
            return false;
        }
        if (this.preLoad) {
            if (this.rawDataStream) {
                this.preLoad = false;
            } else {
                return preLoad();
            }
        }
        if (this.rawDataStream) {
            if ((stopped()) || (paused())) {
                return true;
            }
            if (this.channel.buffersProcessed() > 0) {
                this.channel.processBuffer();
            }
            return true;
        }
        if (this.codec == null) {
            return false;
        }
        if (stopped()) {
            return false;
        }
        if (paused()) {
            return true;
        }
        int i = this.channel.buffersProcessed();
        SoundBuffer localSoundBuffer = null;
        for (int j = 0; j < i; j++) {
            localSoundBuffer = this.codec.read();
            if (localSoundBuffer != null) {
                if (localSoundBuffer.audioData != null) {
                    this.channel.queueBuffer(localSoundBuffer.audioData);
                }
                localSoundBuffer.cleanup();
                localSoundBuffer = null;
                return true;
            }
            if (this.codec.endOfStream()) {
                synchronized (this.soundSequenceLock) {
                    if (SoundSystemConfig.getStreamQueueFormatsMatch()) {
                        if ((this.soundSequenceQueue != null) && (this.soundSequenceQueue.size() > 0)) {
                            if (this.codec != null) {
                                this.codec.cleanup();
                            }
                            this.filenameURL = ((FilenameURL) this.soundSequenceQueue.remove(0));
                            this.codec = SoundSystemConfig.getCodec(this.filenameURL.getFilename());
                            this.codec.initialize(this.filenameURL.getURL());
                            localSoundBuffer = this.codec.read();
                            if (localSoundBuffer != null) {
                                if (localSoundBuffer.audioData != null) {
                                    this.channel.queueBuffer(localSoundBuffer.audioData);
                                }
                                localSoundBuffer.cleanup();
                                localSoundBuffer = null;
                                return true;
                            }
                        } else if (this.toLoop) {
                            this.codec.initialize(this.filenameURL.getURL());
                            localSoundBuffer = this.codec.read();
                            if (localSoundBuffer != null) {
                                if (localSoundBuffer.audioData != null) {
                                    this.channel.queueBuffer(localSoundBuffer.audioData);
                                }
                                localSoundBuffer.cleanup();
                                localSoundBuffer = null;
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean preLoad() {
        if (this.channel == null) {
            return false;
        }
        if (this.codec == null) {
            return false;
        }
        SoundBuffer localSoundBuffer = null;
        int i = 0;
        synchronized (this.soundSequenceLock) {
            if ((this.nextBuffers == null) || (this.nextBuffers.isEmpty())) {
                i = 1;
            }
        }
        if ((this.nextCodec != null) && (i == 0)) {
            this.codec = this.nextCodec;
            this.nextCodec = null;
            synchronized (this.soundSequenceLock) {
                while (!this.nextBuffers.isEmpty()) {
                    localSoundBuffer = (SoundBuffer) this.nextBuffers.remove(0);
                    if (localSoundBuffer != null) {
                        if (localSoundBuffer.audioData != null) {
                            this.channel.queueBuffer(localSoundBuffer.audioData);
                        }
                        localSoundBuffer.cleanup();
                        localSoundBuffer = null;
                    }
                }
            }
        } else {
            this.nextCodec = null;
      ??? =this.filenameURL.getURL();
            this.codec.initialize((URL) ? ??);
            for (int j = 0; j < SoundSystemConfig.getNumberStreamingBuffers(); j++) {
                localSoundBuffer = this.codec.read();
                if (localSoundBuffer != null) {
                    if (localSoundBuffer.audioData != null) {
                        this.channel.queueBuffer(localSoundBuffer.audioData);
                    }
                    localSoundBuffer.cleanup();
                    localSoundBuffer = null;
                }
            }
        }
        return true;
    }

    public void pause() {
        this.toPlay = false;
        paused(true, true);
        if (this.channel != null) {
            this.channel.pause();
        } else {
            errorMessage("Channel null in method 'pause'");
        }
    }

    public void stop() {
        this.toPlay = false;
        stopped(true, true);
        paused(true, false);
        if (this.channel != null) {
            this.channel.stop();
        } else {
            errorMessage("Channel null in method 'stop'");
        }
    }

    public void rewind() {
        if (paused(false, false)) {
            stop();
        }
        if (this.channel != null) {
            boolean bool = playing();
            this.channel.rewind();
            if ((this.toStream) && (bool)) {
                stop();
                play(this.channel);
            }
        } else {
            errorMessage("Channel null in method 'rewind'");
        }
    }

    public void flush() {
        if (this.channel != null) {
            this.channel.flush();
        } else {
            errorMessage("Channel null in method 'flush'");
        }
    }

    public void cull() {
        if (!active(false, false)) {
            return;
        }
        if ((playing()) && (this.toLoop)) {
            this.toPlay = true;
        }
        if (this.rawDataStream) {
            this.toPlay = true;
        }
        active(true, false);
        if (this.channel != null) {
            this.channel.close();
        }
        this.channel = null;
    }

    public void activate() {
        active(true, true);
    }

    public boolean active() {
        return active(false, false);
    }

    public boolean playing() {
        if ((this.channel == null) || (this.channel.attachedSource != this)) {
            return false;
        }
        if ((paused()) || (stopped())) {
            return false;
        }
        return this.channel.playing();
    }

    public boolean stopped() {
        return stopped(false, false);
    }

    public boolean paused() {
        return paused(false, false);
    }

    public float millisecondsPlayed() {
        if (this.channel == null) {
            return -1.0F;
        }
        return this.channel.millisecondsPlayed();
    }

    private synchronized boolean active(boolean paramBoolean1, boolean paramBoolean2) {
        if (paramBoolean1 == true) {
            this.active = paramBoolean2;
        }
        return this.active;
    }

    private synchronized boolean stopped(boolean paramBoolean1, boolean paramBoolean2) {
        if (paramBoolean1 == true) {
            this.stopped = paramBoolean2;
        }
        return this.stopped;
    }

    private synchronized boolean paused(boolean paramBoolean1, boolean paramBoolean2) {
        if (paramBoolean1 == true) {
            this.paused = paramBoolean2;
        }
        return this.paused;
    }

    public String getClassName() {
        String str = SoundSystemConfig.getLibraryTitle(this.libraryType);
        if (str.equals("No Sound")) {
            return "Source";
        }
        return "Source" + str;
    }

    protected void message(String paramString) {
        this.logger.message(paramString, 0);
    }

    protected void importantMessage(String paramString) {
        this.logger.importantMessage(paramString, 0);
    }

    protected boolean errorCheck(boolean paramBoolean, String paramString) {
        return this.logger.errorCheck(paramBoolean, getClassName(), paramString, 0);
    }

    protected void errorMessage(String paramString) {
        this.logger.errorMessage(getClassName(), paramString, 0);
    }

    protected void printStackTrace(Exception paramException) {
        this.logger.printStackTrace(paramException, 1);
    }
}




