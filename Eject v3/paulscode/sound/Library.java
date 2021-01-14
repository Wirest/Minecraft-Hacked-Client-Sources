package paulscode.sound;

import javax.sound.sampled.AudioFormat;
import java.util.*;

public class Library {
    protected ListenerData listener = new ListenerData(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.0F, 0.0F, 1.0F, 0.0F, 0.0F);
    protected HashMap<String, SoundBuffer> bufferMap = null;
    protected HashMap<String, Source> sourceMap = new HashMap();
    protected List<Channel> streamingChannels = new LinkedList();
    protected List<Channel> normalChannels = new LinkedList();
    protected StreamThread streamThread = new StreamThread();
    protected boolean reverseByteOrder = false;
    private SoundSystemLogger logger = SoundSystemConfig.getLogger();
    private MidiChannel midiChannel;
    private String[] streamingChannelSourceNames = new String[SoundSystemConfig.getNumberStreamingChannels()];
    private String[] normalChannelSourceNames = new String[SoundSystemConfig.getNumberNormalChannels()];
    private int nextStreamingChannel = 0;
    private int nextNormalChannel = 0;

    public Library()
            throws SoundSystemException {
        this.streamThread.start();
    }

    public static boolean libraryCompatible() {
        return true;
    }

    public static String getTitle() {
        return "No Sound";
    }

    public static String getDescription() {
        return "Silent Mode";
    }

    public void cleanup() {
        this.streamThread.kill();
        this.streamThread.interrupt();
        for (int i = 0; (i < 50) && (this.streamThread.alive()); i++) {
            try {
                Thread.sleep(100L);
            } catch (Exception localException) {
            }
        }
        if (this.streamThread.alive()) {
            errorMessage("Stream thread did not die!");
            message("Ignoring errors... continuing clean-up.");
        }
        if (this.midiChannel != null) {
            this.midiChannel.cleanup();
            this.midiChannel = null;
        }
        Channel localChannel = null;
        if (this.streamingChannels != null) {
            while (!this.streamingChannels.isEmpty()) {
                localChannel = (Channel) this.streamingChannels.remove(0);
                localChannel.close();
                localChannel.cleanup();
                localChannel = null;
            }
            this.streamingChannels.clear();
            this.streamingChannels = null;
        }
        if (this.normalChannels != null) {
            while (!this.normalChannels.isEmpty()) {
                localChannel = (Channel) this.normalChannels.remove(0);
                localChannel.close();
                localChannel.cleanup();
                localChannel = null;
            }
            this.normalChannels.clear();
            this.normalChannels = null;
        }
        Set localSet = this.sourceMap.keySet();
        Iterator localIterator = localSet.iterator();
        while (localIterator.hasNext()) {
            String str = (String) localIterator.next();
            Source localSource = (Source) this.sourceMap.get(str);
            if (localSource != null) {
                localSource.cleanup();
            }
        }
        this.sourceMap.clear();
        this.sourceMap = null;
        this.listener = null;
        this.streamThread = null;
    }

    public void init()
            throws SoundSystemException {
        Channel localChannel = null;
        for (int i = 0; i < SoundSystemConfig.getNumberStreamingChannels(); i++) {
            localChannel = createChannel(1);
            if (localChannel == null) {
                break;
            }
            this.streamingChannels.add(localChannel);
        }
        for (i = 0; i < SoundSystemConfig.getNumberNormalChannels(); i++) {
            localChannel = createChannel(0);
            if (localChannel == null) {
                break;
            }
            this.normalChannels.add(localChannel);
        }
    }

    protected Channel createChannel(int paramInt) {
        return new Channel(paramInt);
    }

    public boolean loadSound(FilenameURL paramFilenameURL) {
        return true;
    }

    public boolean loadSound(SoundBuffer paramSoundBuffer, String paramString) {
        return true;
    }

    public LinkedList<String> getAllLoadedFilenames() {
        LinkedList localLinkedList = new LinkedList();
        Set localSet = this.bufferMap.keySet();
        Iterator localIterator = localSet.iterator();
        while (localIterator.hasNext()) {
            localLinkedList.add(localIterator.next());
        }
        return localLinkedList;
    }

    public LinkedList<String> getAllSourcenames() {
        LinkedList localLinkedList = new LinkedList();
        Set localSet = this.sourceMap.keySet();
        Iterator localIterator = localSet.iterator();
        if (this.midiChannel != null) {
            localLinkedList.add(this.midiChannel.getSourcename());
        }
        while (localIterator.hasNext()) {
            localLinkedList.add(localIterator.next());
        }
        return localLinkedList;
    }

    public void unloadSound(String paramString) {
        this.bufferMap.remove(paramString);
    }

    public void rawDataStream(AudioFormat paramAudioFormat, boolean paramBoolean, String paramString, float paramFloat1, float paramFloat2, float paramFloat3, int paramInt, float paramFloat4) {
        this.sourceMap.put(paramString, new Source(paramAudioFormat, paramBoolean, paramString, paramFloat1, paramFloat2, paramFloat3, paramInt, paramFloat4));
    }

    public void newSource(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, String paramString, FilenameURL paramFilenameURL, float paramFloat1, float paramFloat2, float paramFloat3, int paramInt, float paramFloat4) {
        this.sourceMap.put(paramString, new Source(paramBoolean1, paramBoolean2, paramBoolean3, paramString, paramFilenameURL, null, paramFloat1, paramFloat2, paramFloat3, paramInt, paramFloat4, false));
    }

    public void quickPlay(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, String paramString, FilenameURL paramFilenameURL, float paramFloat1, float paramFloat2, float paramFloat3, int paramInt, float paramFloat4, boolean paramBoolean4) {
        this.sourceMap.put(paramString, new Source(paramBoolean1, paramBoolean2, paramBoolean3, paramString, paramFilenameURL, null, paramFloat1, paramFloat2, paramFloat3, paramInt, paramFloat4, paramBoolean4));
    }

    public void setTemporary(String paramString, boolean paramBoolean) {
        Source localSource = (Source) this.sourceMap.get(paramString);
        if (localSource != null) {
            localSource.setTemporary(paramBoolean);
        }
    }

    public void setPosition(String paramString, float paramFloat1, float paramFloat2, float paramFloat3) {
        Source localSource = (Source) this.sourceMap.get(paramString);
        if (localSource != null) {
            localSource.setPosition(paramFloat1, paramFloat2, paramFloat3);
        }
    }

    public void setPriority(String paramString, boolean paramBoolean) {
        Source localSource = (Source) this.sourceMap.get(paramString);
        if (localSource != null) {
            localSource.setPriority(paramBoolean);
        }
    }

    public void setLooping(String paramString, boolean paramBoolean) {
        Source localSource = (Source) this.sourceMap.get(paramString);
        if (localSource != null) {
            localSource.setLooping(paramBoolean);
        }
    }

    public void setAttenuation(String paramString, int paramInt) {
        Source localSource = (Source) this.sourceMap.get(paramString);
        if (localSource != null) {
            localSource.setAttenuation(paramInt);
        }
    }

    public void setDistOrRoll(String paramString, float paramFloat) {
        Source localSource = (Source) this.sourceMap.get(paramString);
        if (localSource != null) {
            localSource.setDistOrRoll(paramFloat);
        }
    }

    public void setVelocity(String paramString, float paramFloat1, float paramFloat2, float paramFloat3) {
        Source localSource = (Source) this.sourceMap.get(paramString);
        if (localSource != null) {
            localSource.setVelocity(paramFloat1, paramFloat2, paramFloat3);
        }
    }

    public void setListenerVelocity(float paramFloat1, float paramFloat2, float paramFloat3) {
        this.listener.setVelocity(paramFloat1, paramFloat2, paramFloat3);
    }

    public void dopplerChanged() {
    }

    public float millisecondsPlayed(String paramString) {
        if ((paramString == null) || (paramString.equals(""))) {
            errorMessage("Sourcename not specified in method 'millisecondsPlayed'");
            return -1.0F;
        }
        if (midiSourcename(paramString)) {
            errorMessage("Unable to calculate milliseconds for MIDI source.");
            return -1.0F;
        }
        Source localSource = (Source) this.sourceMap.get(paramString);
        if (localSource == null) {
            errorMessage("Source '" + paramString + "' not found in " + "method 'millisecondsPlayed'");
        }
        return localSource.millisecondsPlayed();
    }

    public int feedRawAudioData(String paramString, byte[] paramArrayOfByte) {
        if ((paramString == null) || (paramString.equals(""))) {
            errorMessage("Sourcename not specified in method 'feedRawAudioData'");
            return -1;
        }
        if (midiSourcename(paramString)) {
            errorMessage("Raw audio data can not be fed to the MIDI channel.");
            return -1;
        }
        Source localSource = (Source) this.sourceMap.get(paramString);
        if (localSource == null) {
            errorMessage("Source '" + paramString + "' not found in " + "method 'feedRawAudioData'");
        }
        return feedRawAudioData(localSource, paramArrayOfByte);
    }

    public int feedRawAudioData(Source paramSource, byte[] paramArrayOfByte) {
        if (paramSource == null) {
            errorMessage("Source parameter null in method 'feedRawAudioData'");
            return -1;
        }
        if (!paramSource.toStream) {
            errorMessage("Only a streaming source may be specified in method 'feedRawAudioData'");
            return -1;
        }
        if (!paramSource.rawDataStream) {
            errorMessage("Streaming source already associated with a file or URL in method'feedRawAudioData'");
            return -1;
        }
        if ((!paramSource.playing()) || (paramSource.channel == null)) {
            Channel localChannel;
            if ((paramSource.channel != null) && (paramSource.channel.attachedSource == paramSource)) {
                localChannel = paramSource.channel;
            } else {
                localChannel = getNextChannel(paramSource);
            }
            int i = paramSource.feedRawAudioData(localChannel, paramArrayOfByte);
            localChannel.attachedSource = paramSource;
            this.streamThread.watch(paramSource);
            this.streamThread.interrupt();
            return i;
        }
        return paramSource.feedRawAudioData(paramSource.channel, paramArrayOfByte);
    }

    public void play(String paramString) {
        if ((paramString == null) || (paramString.equals(""))) {
            errorMessage("Sourcename not specified in method 'play'");
            return;
        }
        if (midiSourcename(paramString)) {
            this.midiChannel.play();
        } else {
            Source localSource = (Source) this.sourceMap.get(paramString);
            if (localSource == null) {
                errorMessage("Source '" + paramString + "' not found in " + "method 'play'");
            }
            play(localSource);
        }
    }

    public void play(Source paramSource) {
        if (paramSource == null) {
            return;
        }
        if (paramSource.rawDataStream) {
            return;
        }
        if (!paramSource.active()) {
            return;
        }
        if (!paramSource.playing()) {
            Channel localChannel = getNextChannel(paramSource);
            if ((paramSource != null) && (localChannel != null)) {
                if ((paramSource.channel != null) && (paramSource.channel.attachedSource != paramSource)) {
                    paramSource.channel = null;
                }
                localChannel.attachedSource = paramSource;
                paramSource.play(localChannel);
                if (paramSource.toStream) {
                    this.streamThread.watch(paramSource);
                    this.streamThread.interrupt();
                }
            }
        }
    }

    public void stop(String paramString) {
        if ((paramString == null) || (paramString.equals(""))) {
            errorMessage("Sourcename not specified in method 'stop'");
            return;
        }
        if (midiSourcename(paramString)) {
            this.midiChannel.stop();
        } else {
            Source localSource = (Source) this.sourceMap.get(paramString);
            if (localSource != null) {
                localSource.stop();
            }
        }
    }

    public void pause(String paramString) {
        if ((paramString == null) || (paramString.equals(""))) {
            errorMessage("Sourcename not specified in method 'stop'");
            return;
        }
        if (midiSourcename(paramString)) {
            this.midiChannel.pause();
        } else {
            Source localSource = (Source) this.sourceMap.get(paramString);
            if (localSource != null) {
                localSource.pause();
            }
        }
    }

    public void rewind(String paramString) {
        if (midiSourcename(paramString)) {
            this.midiChannel.rewind();
        } else {
            Source localSource = (Source) this.sourceMap.get(paramString);
            if (localSource != null) {
                localSource.rewind();
            }
        }
    }

    public void flush(String paramString) {
        if (midiSourcename(paramString)) {
            errorMessage("You can not flush the MIDI channel");
        } else {
            Source localSource = (Source) this.sourceMap.get(paramString);
            if (localSource != null) {
                localSource.flush();
            }
        }
    }

    public void cull(String paramString) {
        Source localSource = (Source) this.sourceMap.get(paramString);
        if (localSource != null) {
            localSource.cull();
        }
    }

    public void activate(String paramString) {
        Source localSource = (Source) this.sourceMap.get(paramString);
        if (localSource != null) {
            localSource.activate();
            if (localSource.toPlay) {
                play(localSource);
            }
        }
    }

    public void setMasterVolume(float paramFloat) {
        SoundSystemConfig.setMasterGain(paramFloat);
        if (this.midiChannel != null) {
            this.midiChannel.resetGain();
        }
    }

    public void setVolume(String paramString, float paramFloat) {
        if (midiSourcename(paramString)) {
            this.midiChannel.setVolume(paramFloat);
        } else {
            Source localSource = (Source) this.sourceMap.get(paramString);
            if (localSource != null) {
                float f = paramFloat;
                if (f < 0.0F) {
                    f = 0.0F;
                } else if (f > 1.0F) {
                    f = 1.0F;
                }
                localSource.sourceVolume = f;
                localSource.positionChanged();
            }
        }
    }

    public float getVolume(String paramString) {
        if (midiSourcename(paramString)) {
            return this.midiChannel.getVolume();
        }
        Source localSource = (Source) this.sourceMap.get(paramString);
        if (localSource != null) {
            return localSource.sourceVolume;
        }
        return 0.0F;
    }

    public void setPitch(String paramString, float paramFloat) {
        if (!midiSourcename(paramString)) {
            Source localSource = (Source) this.sourceMap.get(paramString);
            if (localSource != null) {
                float f = paramFloat;
                if (f < 0.5F) {
                    f = 0.5F;
                } else if (f > 2.0F) {
                    f = 2.0F;
                }
                localSource.setPitch(f);
                localSource.positionChanged();
            }
        }
    }

    public float getPitch(String paramString) {
        if (!midiSourcename(paramString)) {
            Source localSource = (Source) this.sourceMap.get(paramString);
            if (localSource != null) {
                return localSource.getPitch();
            }
        }
        return 1.0F;
    }

    public void moveListener(float paramFloat1, float paramFloat2, float paramFloat3) {
        setListenerPosition(this.listener.position.x + paramFloat1, this.listener.position.y + paramFloat2, this.listener.position.z + paramFloat3);
    }

    public void setListenerPosition(float paramFloat1, float paramFloat2, float paramFloat3) {
        this.listener.setPosition(paramFloat1, paramFloat2, paramFloat3);
        Set localSet = this.sourceMap.keySet();
        Iterator localIterator = localSet.iterator();
        while (localIterator.hasNext()) {
            String str = (String) localIterator.next();
            Source localSource = (Source) this.sourceMap.get(str);
            if (localSource != null) {
                localSource.positionChanged();
            }
        }
    }

    public void turnListener(float paramFloat) {
        setListenerAngle(this.listener.angle + paramFloat);
        Set localSet = this.sourceMap.keySet();
        Iterator localIterator = localSet.iterator();
        while (localIterator.hasNext()) {
            String str = (String) localIterator.next();
            Source localSource = (Source) this.sourceMap.get(str);
            if (localSource != null) {
                localSource.positionChanged();
            }
        }
    }

    public void setListenerAngle(float paramFloat) {
        this.listener.setAngle(paramFloat);
        Set localSet = this.sourceMap.keySet();
        Iterator localIterator = localSet.iterator();
        while (localIterator.hasNext()) {
            String str = (String) localIterator.next();
            Source localSource = (Source) this.sourceMap.get(str);
            if (localSource != null) {
                localSource.positionChanged();
            }
        }
    }

    public void setListenerOrientation(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6) {
        this.listener.setOrientation(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6);
        Set localSet = this.sourceMap.keySet();
        Iterator localIterator = localSet.iterator();
        while (localIterator.hasNext()) {
            String str = (String) localIterator.next();
            Source localSource = (Source) this.sourceMap.get(str);
            if (localSource != null) {
                localSource.positionChanged();
            }
        }
    }

    public void copySources(HashMap<String, Source> paramHashMap) {
        if (paramHashMap == null) {
            return;
        }
        Set localSet = paramHashMap.keySet();
        Iterator localIterator = localSet.iterator();
        this.sourceMap.clear();
        while (localIterator.hasNext()) {
            String str = (String) localIterator.next();
            Source localSource = (Source) paramHashMap.get(str);
            if (localSource != null) {
                loadSound(localSource.filenameURL);
                this.sourceMap.put(str, new Source(localSource, null));
            }
        }
    }

    public void removeSource(String paramString) {
        Source localSource = (Source) this.sourceMap.get(paramString);
        if (localSource != null) {
            localSource.cleanup();
        }
        this.sourceMap.remove(paramString);
    }

    public void removeTemporarySources() {
        Set localSet = this.sourceMap.keySet();
        Iterator localIterator = localSet.iterator();
        while (localIterator.hasNext()) {
            String str = (String) localIterator.next();
            Source localSource = (Source) this.sourceMap.get(str);
            if ((localSource != null) && (localSource.temporary) && (!localSource.playing())) {
                localSource.cleanup();
                localIterator.remove();
            }
        }
    }

    private Channel getNextChannel(Source paramSource) {
        if (paramSource == null) {
            return null;
        }
        String str1 = paramSource.sourcename;
        if (str1 == null) {
            return null;
        }
        int k;
        List localList;
        String[] arrayOfString;
        if (paramSource.toStream) {
            k = this.nextStreamingChannel;
            localList = this.streamingChannels;
            arrayOfString = this.streamingChannelSourceNames;
        } else {
            k = this.nextNormalChannel;
            localList = this.normalChannels;
            arrayOfString = this.normalChannelSourceNames;
        }
        int j = localList.size();
        for (int i = 0; i < j; i++) {
            if (str1.equals(arrayOfString[i])) {
                return (Channel) localList.get(i);
            }
        }
        int m = k;
        String str2;
        Source localSource;
        for (i = 0; i < j; i++) {
            str2 = arrayOfString[m];
            if (str2 == null) {
                localSource = null;
            } else {
                localSource = (Source) this.sourceMap.get(str2);
            }
            if ((localSource == null) || (!localSource.playing())) {
                if (paramSource.toStream) {
                    this.nextStreamingChannel = (m | 0x1);
                    if (this.nextStreamingChannel >= j) {
                        this.nextStreamingChannel = 0;
                    }
                } else {
                    this.nextNormalChannel = (m | 0x1);
                    if (this.nextNormalChannel >= j) {
                        this.nextNormalChannel = 0;
                    }
                }
                arrayOfString[m] = str1;
                return (Channel) localList.get(m);
            }
            m++;
            if (m >= j) {
                m = 0;
            }
        }
        m = k;
        for (i = 0; i < j; i++) {
            str2 = arrayOfString[m];
            if (str2 == null) {
                localSource = null;
            } else {
                localSource = (Source) this.sourceMap.get(str2);
            }
            if ((localSource == null) || (!localSource.playing()) || (!localSource.priority)) {
                if (paramSource.toStream) {
                    this.nextStreamingChannel = (m | 0x1);
                    if (this.nextStreamingChannel >= j) {
                        this.nextStreamingChannel = 0;
                    }
                } else {
                    this.nextNormalChannel = (m | 0x1);
                    if (this.nextNormalChannel >= j) {
                        this.nextNormalChannel = 0;
                    }
                }
                arrayOfString[m] = str1;
                return (Channel) localList.get(m);
            }
            m++;
            if (m >= j) {
                m = 0;
            }
        }
        return null;
    }

    public void replaySources() {
        Set localSet = this.sourceMap.keySet();
        Iterator localIterator = localSet.iterator();
        while (localIterator.hasNext()) {
            String str = (String) localIterator.next();
            Source localSource = (Source) this.sourceMap.get(str);
            if ((localSource != null) && (localSource.toPlay) && (!localSource.playing())) {
                play(str);
                localSource.toPlay = false;
            }
        }
    }

    public void queueSound(String paramString, FilenameURL paramFilenameURL) {
        if (midiSourcename(paramString)) {
            this.midiChannel.queueSound(paramFilenameURL);
        } else {
            Source localSource = (Source) this.sourceMap.get(paramString);
            if (localSource != null) {
                localSource.queueSound(paramFilenameURL);
            }
        }
    }

    public void dequeueSound(String paramString1, String paramString2) {
        if (midiSourcename(paramString1)) {
            this.midiChannel.dequeueSound(paramString2);
        } else {
            Source localSource = (Source) this.sourceMap.get(paramString1);
            if (localSource != null) {
                localSource.dequeueSound(paramString2);
            }
        }
    }

    public void fadeOut(String paramString, FilenameURL paramFilenameURL, long paramLong) {
        if (midiSourcename(paramString)) {
            this.midiChannel.fadeOut(paramFilenameURL, paramLong);
        } else {
            Source localSource = (Source) this.sourceMap.get(paramString);
            if (localSource != null) {
                localSource.fadeOut(paramFilenameURL, paramLong);
            }
        }
    }

    public void fadeOutIn(String paramString, FilenameURL paramFilenameURL, long paramLong1, long paramLong2) {
        if (midiSourcename(paramString)) {
            this.midiChannel.fadeOutIn(paramFilenameURL, paramLong1, paramLong2);
        } else {
            Source localSource = (Source) this.sourceMap.get(paramString);
            if (localSource != null) {
                localSource.fadeOutIn(paramFilenameURL, paramLong1, paramLong2);
            }
        }
    }

    public void checkFadeVolumes() {
        if (this.midiChannel != null) {
            this.midiChannel.resetGain();
        }
        for (int i = 0; i < this.streamingChannels.size(); i++) {
            localChannel = (Channel) this.streamingChannels.get(i);
            if (localChannel != null) {
                localSource = localChannel.attachedSource;
                if (localSource != null) {
                    localSource.checkFadeOut();
                }
            }
        }
        Channel localChannel = null;
        Source localSource = null;
    }

    public void loadMidi(boolean paramBoolean, String paramString, FilenameURL paramFilenameURL) {
        if (paramFilenameURL == null) {
            errorMessage("Filename/URL not specified in method 'loadMidi'.");
            return;
        }
        if (!paramFilenameURL.getFilename().matches(SoundSystemConfig.EXTENSION_MIDI)) {
            errorMessage("Filename/identifier doesn't end in '.mid' or'.midi' in method loadMidi.");
            return;
        }
        if (this.midiChannel == null) {
            this.midiChannel = new MidiChannel(paramBoolean, paramString, paramFilenameURL);
        } else {
            this.midiChannel.switchSource(paramBoolean, paramString, paramFilenameURL);
        }
    }

    public void unloadMidi() {
        if (this.midiChannel != null) {
            this.midiChannel.cleanup();
        }
        this.midiChannel = null;
    }

    public boolean midiSourcename(String paramString) {
        if ((this.midiChannel == null) || (paramString == null)) {
            return false;
        }
        if ((this.midiChannel.getSourcename() == null) || (paramString.equals(""))) {
            return false;
        }
        return paramString.equals(this.midiChannel.getSourcename());
    }

    public Source getSource(String paramString) {
        return (Source) this.sourceMap.get(paramString);
    }

    public MidiChannel getMidiChannel() {
        return this.midiChannel;
    }

    public void setMidiChannel(MidiChannel paramMidiChannel) {
        if ((this.midiChannel != null) && (this.midiChannel != paramMidiChannel)) {
            this.midiChannel.cleanup();
        }
        this.midiChannel = paramMidiChannel;
    }

    public void listenerMoved() {
        Set localSet = this.sourceMap.keySet();
        Iterator localIterator = localSet.iterator();
        while (localIterator.hasNext()) {
            String str = (String) localIterator.next();
            Source localSource = (Source) this.sourceMap.get(str);
            if (localSource != null) {
                localSource.listenerMoved();
            }
        }
    }

    public HashMap<String, Source> getSources() {
        return this.sourceMap;
    }

    public ListenerData getListenerData() {
        return this.listener;
    }

    public void setListenerData(ListenerData paramListenerData) {
        this.listener.setData(paramListenerData);
    }

    public boolean reverseByteOrder() {
        return this.reverseByteOrder;
    }

    public String getClassName() {
        return "Library";
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




