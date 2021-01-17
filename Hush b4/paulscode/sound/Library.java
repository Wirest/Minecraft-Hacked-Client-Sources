// 
// Decompiled by Procyon v0.5.36
// 

package paulscode.sound;

import javax.sound.sampled.AudioFormat;
import java.util.Iterator;
import java.util.Set;
import java.util.LinkedList;
import java.util.List;
import java.util.HashMap;

public class Library
{
    private SoundSystemLogger logger;
    protected ListenerData listener;
    protected HashMap<String, SoundBuffer> bufferMap;
    protected HashMap<String, Source> sourceMap;
    private MidiChannel midiChannel;
    protected List<Channel> streamingChannels;
    protected List<Channel> normalChannels;
    private String[] streamingChannelSourceNames;
    private String[] normalChannelSourceNames;
    private int nextStreamingChannel;
    private int nextNormalChannel;
    protected StreamThread streamThread;
    protected boolean reverseByteOrder;
    
    public Library() throws SoundSystemException {
        this.bufferMap = null;
        this.nextStreamingChannel = 0;
        this.nextNormalChannel = 0;
        this.reverseByteOrder = false;
        this.logger = SoundSystemConfig.getLogger();
        this.bufferMap = new HashMap<String, SoundBuffer>();
        this.sourceMap = new HashMap<String, Source>();
        this.listener = new ListenerData(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f, 0.0f);
        this.streamingChannels = new LinkedList<Channel>();
        this.normalChannels = new LinkedList<Channel>();
        this.streamingChannelSourceNames = new String[SoundSystemConfig.getNumberStreamingChannels()];
        this.normalChannelSourceNames = new String[SoundSystemConfig.getNumberNormalChannels()];
        (this.streamThread = new StreamThread()).start();
    }
    
    public void cleanup() {
        this.streamThread.kill();
        this.streamThread.interrupt();
        for (int i = 0; i < 50 && this.streamThread.alive(); ++i) {
            try {
                Thread.sleep(100L);
            }
            catch (Exception ex) {}
        }
        if (this.streamThread.alive()) {
            this.errorMessage("Stream thread did not die!");
            this.message("Ignoring errors... continuing clean-up.");
        }
        if (this.midiChannel != null) {
            this.midiChannel.cleanup();
            this.midiChannel = null;
        }
        Channel channel = null;
        if (this.streamingChannels != null) {
            while (!this.streamingChannels.isEmpty()) {
                channel = this.streamingChannels.remove(0);
                channel.close();
                channel.cleanup();
                channel = null;
            }
            this.streamingChannels.clear();
            this.streamingChannels = null;
        }
        if (this.normalChannels != null) {
            while (!this.normalChannels.isEmpty()) {
                channel = this.normalChannels.remove(0);
                channel.close();
                channel.cleanup();
                channel = null;
            }
            this.normalChannels.clear();
            this.normalChannels = null;
        }
        final Set<String> keys = this.sourceMap.keySet();
        for (final String sourcename : keys) {
            final Source source = this.sourceMap.get(sourcename);
            if (source != null) {
                source.cleanup();
            }
        }
        this.sourceMap.clear();
        this.sourceMap = null;
        this.listener = null;
        this.streamThread = null;
    }
    
    public void init() throws SoundSystemException {
        Channel channel = null;
        for (int x = 0; x < SoundSystemConfig.getNumberStreamingChannels(); ++x) {
            channel = this.createChannel(1);
            if (channel == null) {
                break;
            }
            this.streamingChannels.add(channel);
        }
        for (int x = 0; x < SoundSystemConfig.getNumberNormalChannels(); ++x) {
            channel = this.createChannel(0);
            if (channel == null) {
                break;
            }
            this.normalChannels.add(channel);
        }
    }
    
    public static boolean libraryCompatible() {
        return true;
    }
    
    protected Channel createChannel(final int type) {
        return new Channel(type);
    }
    
    public boolean loadSound(final FilenameURL filenameURL) {
        return true;
    }
    
    public boolean loadSound(final SoundBuffer buffer, final String identifier) {
        return true;
    }
    
    public LinkedList<String> getAllLoadedFilenames() {
        final LinkedList<String> filenames = new LinkedList<String>();
        final Set<String> keys = this.bufferMap.keySet();
        final Iterator<String> iter = keys.iterator();
        while (iter.hasNext()) {
            filenames.add(iter.next());
        }
        return filenames;
    }
    
    public LinkedList<String> getAllSourcenames() {
        final LinkedList<String> sourcenames = new LinkedList<String>();
        final Set<String> keys = this.sourceMap.keySet();
        final Iterator<String> iter = keys.iterator();
        if (this.midiChannel != null) {
            sourcenames.add(this.midiChannel.getSourcename());
        }
        while (iter.hasNext()) {
            sourcenames.add(iter.next());
        }
        return sourcenames;
    }
    
    public void unloadSound(final String filename) {
        this.bufferMap.remove(filename);
    }
    
    public void rawDataStream(final AudioFormat audioFormat, final boolean priority, final String sourcename, final float posX, final float posY, final float posZ, final int attModel, final float distOrRoll) {
        this.sourceMap.put(sourcename, new Source(audioFormat, priority, sourcename, posX, posY, posZ, attModel, distOrRoll));
    }
    
    public void newSource(final boolean priority, final boolean toStream, final boolean toLoop, final String sourcename, final FilenameURL filenameURL, final float posX, final float posY, final float posZ, final int attModel, final float distOrRoll) {
        this.sourceMap.put(sourcename, new Source(priority, toStream, toLoop, sourcename, filenameURL, null, posX, posY, posZ, attModel, distOrRoll, false));
    }
    
    public void quickPlay(final boolean priority, final boolean toStream, final boolean toLoop, final String sourcename, final FilenameURL filenameURL, final float posX, final float posY, final float posZ, final int attModel, final float distOrRoll, final boolean tmp) {
        this.sourceMap.put(sourcename, new Source(priority, toStream, toLoop, sourcename, filenameURL, null, posX, posY, posZ, attModel, distOrRoll, tmp));
    }
    
    public void setTemporary(final String sourcename, final boolean temporary) {
        final Source mySource = this.sourceMap.get(sourcename);
        if (mySource != null) {
            mySource.setTemporary(temporary);
        }
    }
    
    public void setPosition(final String sourcename, final float x, final float y, final float z) {
        final Source mySource = this.sourceMap.get(sourcename);
        if (mySource != null) {
            mySource.setPosition(x, y, z);
        }
    }
    
    public void setPriority(final String sourcename, final boolean pri) {
        final Source mySource = this.sourceMap.get(sourcename);
        if (mySource != null) {
            mySource.setPriority(pri);
        }
    }
    
    public void setLooping(final String sourcename, final boolean lp) {
        final Source mySource = this.sourceMap.get(sourcename);
        if (mySource != null) {
            mySource.setLooping(lp);
        }
    }
    
    public void setAttenuation(final String sourcename, final int model) {
        final Source mySource = this.sourceMap.get(sourcename);
        if (mySource != null) {
            mySource.setAttenuation(model);
        }
    }
    
    public void setDistOrRoll(final String sourcename, final float dr) {
        final Source mySource = this.sourceMap.get(sourcename);
        if (mySource != null) {
            mySource.setDistOrRoll(dr);
        }
    }
    
    public void setVelocity(final String sourcename, final float x, final float y, final float z) {
        final Source mySource = this.sourceMap.get(sourcename);
        if (mySource != null) {
            mySource.setVelocity(x, y, z);
        }
    }
    
    public void setListenerVelocity(final float x, final float y, final float z) {
        this.listener.setVelocity(x, y, z);
    }
    
    public void dopplerChanged() {
    }
    
    public float millisecondsPlayed(final String sourcename) {
        if (sourcename == null || sourcename.equals("")) {
            this.errorMessage("Sourcename not specified in method 'millisecondsPlayed'");
            return -1.0f;
        }
        if (this.midiSourcename(sourcename)) {
            this.errorMessage("Unable to calculate milliseconds for MIDI source.");
            return -1.0f;
        }
        final Source source = this.sourceMap.get(sourcename);
        if (source == null) {
            this.errorMessage("Source '" + sourcename + "' not found in " + "method 'millisecondsPlayed'");
        }
        return source.millisecondsPlayed();
    }
    
    public int feedRawAudioData(final String sourcename, final byte[] buffer) {
        if (sourcename == null || sourcename.equals("")) {
            this.errorMessage("Sourcename not specified in method 'feedRawAudioData'");
            return -1;
        }
        if (this.midiSourcename(sourcename)) {
            this.errorMessage("Raw audio data can not be fed to the MIDI channel.");
            return -1;
        }
        final Source source = this.sourceMap.get(sourcename);
        if (source == null) {
            this.errorMessage("Source '" + sourcename + "' not found in " + "method 'feedRawAudioData'");
        }
        return this.feedRawAudioData(source, buffer);
    }
    
    public int feedRawAudioData(final Source source, final byte[] buffer) {
        if (source == null) {
            this.errorMessage("Source parameter null in method 'feedRawAudioData'");
            return -1;
        }
        if (!source.toStream) {
            this.errorMessage("Only a streaming source may be specified in method 'feedRawAudioData'");
            return -1;
        }
        if (!source.rawDataStream) {
            this.errorMessage("Streaming source already associated with a file or URL in method'feedRawAudioData'");
            return -1;
        }
        if (!source.playing() || source.channel == null) {
            Channel channel;
            if (source.channel != null && source.channel.attachedSource == source) {
                channel = source.channel;
            }
            else {
                channel = this.getNextChannel(source);
            }
            final int processed = source.feedRawAudioData(channel, buffer);
            channel.attachedSource = source;
            this.streamThread.watch(source);
            this.streamThread.interrupt();
            return processed;
        }
        return source.feedRawAudioData(source.channel, buffer);
    }
    
    public void play(final String sourcename) {
        if (sourcename == null || sourcename.equals("")) {
            this.errorMessage("Sourcename not specified in method 'play'");
            return;
        }
        if (this.midiSourcename(sourcename)) {
            this.midiChannel.play();
        }
        else {
            final Source source = this.sourceMap.get(sourcename);
            if (source == null) {
                this.errorMessage("Source '" + sourcename + "' not found in " + "method 'play'");
            }
            this.play(source);
        }
    }
    
    public void play(final Source source) {
        if (source == null) {
            return;
        }
        if (source.rawDataStream) {
            return;
        }
        if (!source.active()) {
            return;
        }
        if (!source.playing()) {
            final Channel channel = this.getNextChannel(source);
            if (source != null && channel != null) {
                if (source.channel != null && source.channel.attachedSource != source) {
                    source.channel = null;
                }
                (channel.attachedSource = source).play(channel);
                if (source.toStream) {
                    this.streamThread.watch(source);
                    this.streamThread.interrupt();
                }
            }
        }
    }
    
    public void stop(final String sourcename) {
        if (sourcename == null || sourcename.equals("")) {
            this.errorMessage("Sourcename not specified in method 'stop'");
            return;
        }
        if (this.midiSourcename(sourcename)) {
            this.midiChannel.stop();
        }
        else {
            final Source mySource = this.sourceMap.get(sourcename);
            if (mySource != null) {
                mySource.stop();
            }
        }
    }
    
    public void pause(final String sourcename) {
        if (sourcename == null || sourcename.equals("")) {
            this.errorMessage("Sourcename not specified in method 'stop'");
            return;
        }
        if (this.midiSourcename(sourcename)) {
            this.midiChannel.pause();
        }
        else {
            final Source mySource = this.sourceMap.get(sourcename);
            if (mySource != null) {
                mySource.pause();
            }
        }
    }
    
    public void rewind(final String sourcename) {
        if (this.midiSourcename(sourcename)) {
            this.midiChannel.rewind();
        }
        else {
            final Source mySource = this.sourceMap.get(sourcename);
            if (mySource != null) {
                mySource.rewind();
            }
        }
    }
    
    public void flush(final String sourcename) {
        if (this.midiSourcename(sourcename)) {
            this.errorMessage("You can not flush the MIDI channel");
        }
        else {
            final Source mySource = this.sourceMap.get(sourcename);
            if (mySource != null) {
                mySource.flush();
            }
        }
    }
    
    public void cull(final String sourcename) {
        final Source mySource = this.sourceMap.get(sourcename);
        if (mySource != null) {
            mySource.cull();
        }
    }
    
    public void activate(final String sourcename) {
        final Source mySource = this.sourceMap.get(sourcename);
        if (mySource != null) {
            mySource.activate();
            if (mySource.toPlay) {
                this.play(mySource);
            }
        }
    }
    
    public void setMasterVolume(final float value) {
        SoundSystemConfig.setMasterGain(value);
        if (this.midiChannel != null) {
            this.midiChannel.resetGain();
        }
    }
    
    public void setVolume(final String sourcename, final float value) {
        if (this.midiSourcename(sourcename)) {
            this.midiChannel.setVolume(value);
        }
        else {
            final Source mySource = this.sourceMap.get(sourcename);
            if (mySource != null) {
                float newVolume = value;
                if (newVolume < 0.0f) {
                    newVolume = 0.0f;
                }
                else if (newVolume > 1.0f) {
                    newVolume = 1.0f;
                }
                mySource.sourceVolume = newVolume;
                mySource.positionChanged();
            }
        }
    }
    
    public float getVolume(final String sourcename) {
        if (this.midiSourcename(sourcename)) {
            return this.midiChannel.getVolume();
        }
        final Source mySource = this.sourceMap.get(sourcename);
        if (mySource != null) {
            return mySource.sourceVolume;
        }
        return 0.0f;
    }
    
    public void setPitch(final String sourcename, final float value) {
        if (!this.midiSourcename(sourcename)) {
            final Source mySource = this.sourceMap.get(sourcename);
            if (mySource != null) {
                float newPitch = value;
                if (newPitch < 0.5f) {
                    newPitch = 0.5f;
                }
                else if (newPitch > 2.0f) {
                    newPitch = 2.0f;
                }
                mySource.setPitch(newPitch);
                mySource.positionChanged();
            }
        }
    }
    
    public float getPitch(final String sourcename) {
        if (!this.midiSourcename(sourcename)) {
            final Source mySource = this.sourceMap.get(sourcename);
            if (mySource != null) {
                return mySource.getPitch();
            }
        }
        return 1.0f;
    }
    
    public void moveListener(final float x, final float y, final float z) {
        this.setListenerPosition(this.listener.position.x + x, this.listener.position.y + y, this.listener.position.z + z);
    }
    
    public void setListenerPosition(final float x, final float y, final float z) {
        this.listener.setPosition(x, y, z);
        final Set<String> keys = this.sourceMap.keySet();
        for (final String sourcename : keys) {
            final Source source = this.sourceMap.get(sourcename);
            if (source != null) {
                source.positionChanged();
            }
        }
    }
    
    public void turnListener(final float angle) {
        this.setListenerAngle(this.listener.angle + angle);
        final Set<String> keys = this.sourceMap.keySet();
        for (final String sourcename : keys) {
            final Source source = this.sourceMap.get(sourcename);
            if (source != null) {
                source.positionChanged();
            }
        }
    }
    
    public void setListenerAngle(final float angle) {
        this.listener.setAngle(angle);
        final Set<String> keys = this.sourceMap.keySet();
        for (final String sourcename : keys) {
            final Source source = this.sourceMap.get(sourcename);
            if (source != null) {
                source.positionChanged();
            }
        }
    }
    
    public void setListenerOrientation(final float lookX, final float lookY, final float lookZ, final float upX, final float upY, final float upZ) {
        this.listener.setOrientation(lookX, lookY, lookZ, upX, upY, upZ);
        final Set<String> keys = this.sourceMap.keySet();
        for (final String sourcename : keys) {
            final Source source = this.sourceMap.get(sourcename);
            if (source != null) {
                source.positionChanged();
            }
        }
    }
    
    public void setListenerData(final ListenerData l) {
        this.listener.setData(l);
    }
    
    public void copySources(final HashMap<String, Source> srcMap) {
        if (srcMap == null) {
            return;
        }
        final Set<String> keys = srcMap.keySet();
        final Iterator<String> iter = keys.iterator();
        this.sourceMap.clear();
        while (iter.hasNext()) {
            final String sourcename = iter.next();
            final Source srcData = srcMap.get(sourcename);
            if (srcData != null) {
                this.loadSound(srcData.filenameURL);
                this.sourceMap.put(sourcename, new Source(srcData, null));
            }
        }
    }
    
    public void removeSource(final String sourcename) {
        final Source mySource = this.sourceMap.get(sourcename);
        if (mySource != null) {
            mySource.cleanup();
        }
        this.sourceMap.remove(sourcename);
    }
    
    public void removeTemporarySources() {
        final Set<String> keys = this.sourceMap.keySet();
        final Iterator<String> iter = keys.iterator();
        while (iter.hasNext()) {
            final String sourcename = iter.next();
            final Source srcData = this.sourceMap.get(sourcename);
            if (srcData != null && srcData.temporary && !srcData.playing()) {
                srcData.cleanup();
                iter.remove();
            }
        }
    }
    
    private Channel getNextChannel(final Source source) {
        if (source == null) {
            return null;
        }
        final String sourcename = source.sourcename;
        if (sourcename == null) {
            return null;
        }
        int nextChannel;
        List<Channel> channelList;
        String[] sourceNames;
        if (source.toStream) {
            nextChannel = this.nextStreamingChannel;
            channelList = this.streamingChannels;
            sourceNames = this.streamingChannelSourceNames;
        }
        else {
            nextChannel = this.nextNormalChannel;
            channelList = this.normalChannels;
            sourceNames = this.normalChannelSourceNames;
        }
        final int channels = channelList.size();
        for (int x = 0; x < channels; ++x) {
            if (sourcename.equals(sourceNames[x])) {
                return channelList.get(x);
            }
        }
        int n = nextChannel;
        for (int x = 0; x < channels; ++x) {
            final String name = sourceNames[n];
            Source src;
            if (name == null) {
                src = null;
            }
            else {
                src = this.sourceMap.get(name);
            }
            if (src == null || !src.playing()) {
                if (source.toStream) {
                    this.nextStreamingChannel = n + 1;
                    if (this.nextStreamingChannel >= channels) {
                        this.nextStreamingChannel = 0;
                    }
                }
                else {
                    this.nextNormalChannel = n + 1;
                    if (this.nextNormalChannel >= channels) {
                        this.nextNormalChannel = 0;
                    }
                }
                sourceNames[n] = sourcename;
                return channelList.get(n);
            }
            if (++n >= channels) {
                n = 0;
            }
        }
        n = nextChannel;
        for (int x = 0; x < channels; ++x) {
            final String name = sourceNames[n];
            Source src;
            if (name == null) {
                src = null;
            }
            else {
                src = this.sourceMap.get(name);
            }
            if (src == null || !src.playing() || !src.priority) {
                if (source.toStream) {
                    this.nextStreamingChannel = n + 1;
                    if (this.nextStreamingChannel >= channels) {
                        this.nextStreamingChannel = 0;
                    }
                }
                else {
                    this.nextNormalChannel = n + 1;
                    if (this.nextNormalChannel >= channels) {
                        this.nextNormalChannel = 0;
                    }
                }
                sourceNames[n] = sourcename;
                return channelList.get(n);
            }
            if (++n >= channels) {
                n = 0;
            }
        }
        return null;
    }
    
    public void replaySources() {
        final Set<String> keys = this.sourceMap.keySet();
        for (final String sourcename : keys) {
            final Source source = this.sourceMap.get(sourcename);
            if (source != null && source.toPlay && !source.playing()) {
                this.play(sourcename);
                source.toPlay = false;
            }
        }
    }
    
    public void queueSound(final String sourcename, final FilenameURL filenameURL) {
        if (this.midiSourcename(sourcename)) {
            this.midiChannel.queueSound(filenameURL);
        }
        else {
            final Source mySource = this.sourceMap.get(sourcename);
            if (mySource != null) {
                mySource.queueSound(filenameURL);
            }
        }
    }
    
    public void dequeueSound(final String sourcename, final String filename) {
        if (this.midiSourcename(sourcename)) {
            this.midiChannel.dequeueSound(filename);
        }
        else {
            final Source mySource = this.sourceMap.get(sourcename);
            if (mySource != null) {
                mySource.dequeueSound(filename);
            }
        }
    }
    
    public void fadeOut(final String sourcename, final FilenameURL filenameURL, final long milis) {
        if (this.midiSourcename(sourcename)) {
            this.midiChannel.fadeOut(filenameURL, milis);
        }
        else {
            final Source mySource = this.sourceMap.get(sourcename);
            if (mySource != null) {
                mySource.fadeOut(filenameURL, milis);
            }
        }
    }
    
    public void fadeOutIn(final String sourcename, final FilenameURL filenameURL, final long milisOut, final long milisIn) {
        if (this.midiSourcename(sourcename)) {
            this.midiChannel.fadeOutIn(filenameURL, milisOut, milisIn);
        }
        else {
            final Source mySource = this.sourceMap.get(sourcename);
            if (mySource != null) {
                mySource.fadeOutIn(filenameURL, milisOut, milisIn);
            }
        }
    }
    
    public void checkFadeVolumes() {
        if (this.midiChannel != null) {
            this.midiChannel.resetGain();
        }
        for (int x = 0; x < this.streamingChannels.size(); ++x) {
            final Channel c = this.streamingChannels.get(x);
            if (c != null) {
                final Source s = c.attachedSource;
                if (s != null) {
                    s.checkFadeOut();
                }
            }
        }
        final Channel c = null;
        final Source s = null;
    }
    
    public void loadMidi(final boolean toLoop, final String sourcename, final FilenameURL filenameURL) {
        if (filenameURL == null) {
            this.errorMessage("Filename/URL not specified in method 'loadMidi'.");
            return;
        }
        if (!filenameURL.getFilename().matches(SoundSystemConfig.EXTENSION_MIDI)) {
            this.errorMessage("Filename/identifier doesn't end in '.mid' or'.midi' in method loadMidi.");
            return;
        }
        if (this.midiChannel == null) {
            this.midiChannel = new MidiChannel(toLoop, sourcename, filenameURL);
        }
        else {
            this.midiChannel.switchSource(toLoop, sourcename, filenameURL);
        }
    }
    
    public void unloadMidi() {
        if (this.midiChannel != null) {
            this.midiChannel.cleanup();
        }
        this.midiChannel = null;
    }
    
    public boolean midiSourcename(final String sourcename) {
        return this.midiChannel != null && sourcename != null && this.midiChannel.getSourcename() != null && !sourcename.equals("") && sourcename.equals(this.midiChannel.getSourcename());
    }
    
    public Source getSource(final String sourcename) {
        return this.sourceMap.get(sourcename);
    }
    
    public MidiChannel getMidiChannel() {
        return this.midiChannel;
    }
    
    public void setMidiChannel(final MidiChannel c) {
        if (this.midiChannel != null && this.midiChannel != c) {
            this.midiChannel.cleanup();
        }
        this.midiChannel = c;
    }
    
    public void listenerMoved() {
        final Set<String> keys = this.sourceMap.keySet();
        for (final String sourcename : keys) {
            final Source srcData = this.sourceMap.get(sourcename);
            if (srcData != null) {
                srcData.listenerMoved();
            }
        }
    }
    
    public HashMap<String, Source> getSources() {
        return this.sourceMap;
    }
    
    public ListenerData getListenerData() {
        return this.listener;
    }
    
    public boolean reverseByteOrder() {
        return this.reverseByteOrder;
    }
    
    public static String getTitle() {
        return "No Sound";
    }
    
    public static String getDescription() {
        return "Silent Mode";
    }
    
    public String getClassName() {
        return "Library";
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
