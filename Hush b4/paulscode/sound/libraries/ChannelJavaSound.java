// 
// Decompiled by Procyon v0.5.36
// 

package paulscode.sound.libraries;

import javax.sound.sampled.Control;
import javax.sound.sampled.Line;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import java.util.LinkedList;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.Mixer;
import java.util.List;
import javax.sound.sampled.SourceDataLine;
import paulscode.sound.SoundBuffer;
import javax.sound.sampled.Clip;
import paulscode.sound.Channel;

public class ChannelJavaSound extends Channel
{
    public Clip clip;
    SoundBuffer soundBuffer;
    public SourceDataLine sourceDataLine;
    private List<SoundBuffer> streamBuffers;
    private int processed;
    private Mixer myMixer;
    private AudioFormat myFormat;
    private FloatControl gainControl;
    private FloatControl panControl;
    private FloatControl sampleRateControl;
    private float initialGain;
    private float initialSampleRate;
    private boolean toLoop;
    
    public ChannelJavaSound(final int type, final Mixer mixer) {
        super(type);
        this.clip = null;
        this.sourceDataLine = null;
        this.processed = 0;
        this.myMixer = null;
        this.myFormat = null;
        this.gainControl = null;
        this.panControl = null;
        this.sampleRateControl = null;
        this.initialGain = 0.0f;
        this.initialSampleRate = 0.0f;
        this.toLoop = false;
        this.libraryType = LibraryJavaSound.class;
        this.myMixer = mixer;
        this.clip = null;
        this.sourceDataLine = null;
        this.streamBuffers = new LinkedList<SoundBuffer>();
    }
    
    @Override
    public void cleanup() {
        if (this.streamBuffers != null) {
            SoundBuffer buf = null;
            while (!this.streamBuffers.isEmpty()) {
                buf = this.streamBuffers.remove(0);
                buf.cleanup();
                buf = null;
            }
            this.streamBuffers.clear();
        }
        this.clip = null;
        this.soundBuffer = null;
        this.sourceDataLine = null;
        this.streamBuffers.clear();
        this.myMixer = null;
        this.myFormat = null;
        this.streamBuffers = null;
        super.cleanup();
    }
    
    public void newMixer(final Mixer m) {
        if (this.myMixer != m) {
            try {
                if (this.clip != null) {
                    this.clip.close();
                }
                else if (this.sourceDataLine != null) {
                    this.sourceDataLine.close();
                }
            }
            catch (SecurityException ex) {}
            this.myMixer = m;
            if (this.attachedSource != null) {
                if (this.channelType == 0 && this.soundBuffer != null) {
                    this.attachBuffer(this.soundBuffer);
                }
                else if (this.myFormat != null) {
                    this.resetStream(this.myFormat);
                }
            }
        }
    }
    
    public boolean attachBuffer(final SoundBuffer buffer) {
        if (this.errorCheck(this.channelType != 0, "Buffers may only be attached to non-streaming sources")) {
            return false;
        }
        if (this.errorCheck(this.myMixer == null, "Mixer null in method 'attachBuffer'")) {
            return false;
        }
        if (this.errorCheck(buffer == null, "Buffer null in method 'attachBuffer'")) {
            return false;
        }
        if (this.errorCheck(buffer.audioData == null, "Buffer missing audio data in method 'attachBuffer'")) {
            return false;
        }
        if (this.errorCheck(buffer.audioFormat == null, "Buffer missing format information in method 'attachBuffer'")) {
            return false;
        }
        final DataLine.Info lineInfo = new DataLine.Info(Clip.class, buffer.audioFormat);
        if (this.errorCheck(!AudioSystem.isLineSupported(lineInfo), "Line not supported in method 'attachBuffer'")) {
            return false;
        }
        Clip newClip = null;
        try {
            newClip = (Clip)this.myMixer.getLine(lineInfo);
        }
        catch (Exception e) {
            this.errorMessage("Unable to create clip in method 'attachBuffer'");
            this.printStackTrace(e);
            return false;
        }
        if (this.errorCheck(newClip == null, "New clip null in method 'attachBuffer'")) {
            return false;
        }
        if (this.clip != null) {
            this.clip.stop();
            this.clip.flush();
            this.clip.close();
        }
        this.clip = newClip;
        this.soundBuffer = buffer;
        this.myFormat = buffer.audioFormat;
        newClip = null;
        try {
            this.clip.open(this.myFormat, buffer.audioData, 0, buffer.audioData.length);
        }
        catch (Exception e) {
            this.errorMessage("Unable to attach buffer to clip in method 'attachBuffer'");
            this.printStackTrace(e);
            return false;
        }
        this.resetControls();
        return true;
    }
    
    @Override
    public void setAudioFormat(final AudioFormat audioFormat) {
        this.resetStream(audioFormat);
        if (this.attachedSource != null && this.attachedSource.rawDataStream && this.attachedSource.active() && this.sourceDataLine != null) {
            this.sourceDataLine.start();
        }
    }
    
    public boolean resetStream(final AudioFormat format) {
        if (this.errorCheck(this.myMixer == null, "Mixer null in method 'resetStream'")) {
            return false;
        }
        if (this.errorCheck(format == null, "AudioFormat null in method 'resetStream'")) {
            return false;
        }
        final DataLine.Info lineInfo = new DataLine.Info(SourceDataLine.class, format);
        if (this.errorCheck(!AudioSystem.isLineSupported(lineInfo), "Line not supported in method 'resetStream'")) {
            return false;
        }
        SourceDataLine newSourceDataLine = null;
        try {
            newSourceDataLine = (SourceDataLine)this.myMixer.getLine(lineInfo);
        }
        catch (Exception e) {
            this.errorMessage("Unable to create a SourceDataLine in method 'resetStream'");
            this.printStackTrace(e);
            return false;
        }
        if (this.errorCheck(newSourceDataLine == null, "New SourceDataLine null in method 'resetStream'")) {
            return false;
        }
        this.streamBuffers.clear();
        this.processed = 0;
        if (this.sourceDataLine != null) {
            this.sourceDataLine.stop();
            this.sourceDataLine.flush();
            this.sourceDataLine.close();
        }
        this.sourceDataLine = newSourceDataLine;
        this.myFormat = format;
        newSourceDataLine = null;
        try {
            this.sourceDataLine.open(this.myFormat);
        }
        catch (Exception e) {
            this.errorMessage("Unable to open the new SourceDataLine in method 'resetStream'");
            this.printStackTrace(e);
            return false;
        }
        this.resetControls();
        return true;
    }
    
    private void resetControls() {
        switch (this.channelType) {
            case 0: {
                try {
                    if (!this.clip.isControlSupported(FloatControl.Type.PAN)) {
                        this.panControl = null;
                    }
                    else {
                        this.panControl = (FloatControl)this.clip.getControl(FloatControl.Type.PAN);
                    }
                }
                catch (IllegalArgumentException iae) {
                    this.panControl = null;
                }
                try {
                    if (!this.clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                        this.gainControl = null;
                        this.initialGain = 0.0f;
                    }
                    else {
                        this.gainControl = (FloatControl)this.clip.getControl(FloatControl.Type.MASTER_GAIN);
                        this.initialGain = this.gainControl.getValue();
                    }
                }
                catch (IllegalArgumentException iae) {
                    this.gainControl = null;
                    this.initialGain = 0.0f;
                }
                try {
                    if (!this.clip.isControlSupported(FloatControl.Type.SAMPLE_RATE)) {
                        this.sampleRateControl = null;
                        this.initialSampleRate = 0.0f;
                    }
                    else {
                        this.sampleRateControl = (FloatControl)this.clip.getControl(FloatControl.Type.SAMPLE_RATE);
                        this.initialSampleRate = this.sampleRateControl.getValue();
                    }
                }
                catch (IllegalArgumentException iae) {
                    this.sampleRateControl = null;
                    this.initialSampleRate = 0.0f;
                }
                break;
            }
            case 1: {
                try {
                    if (!this.sourceDataLine.isControlSupported(FloatControl.Type.PAN)) {
                        this.panControl = null;
                    }
                    else {
                        this.panControl = (FloatControl)this.sourceDataLine.getControl(FloatControl.Type.PAN);
                    }
                }
                catch (IllegalArgumentException iae) {
                    this.panControl = null;
                }
                try {
                    if (!this.sourceDataLine.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                        this.gainControl = null;
                        this.initialGain = 0.0f;
                    }
                    else {
                        this.gainControl = (FloatControl)this.sourceDataLine.getControl(FloatControl.Type.MASTER_GAIN);
                        this.initialGain = this.gainControl.getValue();
                    }
                }
                catch (IllegalArgumentException iae) {
                    this.gainControl = null;
                    this.initialGain = 0.0f;
                }
                try {
                    if (!this.sourceDataLine.isControlSupported(FloatControl.Type.SAMPLE_RATE)) {
                        this.sampleRateControl = null;
                        this.initialSampleRate = 0.0f;
                    }
                    else {
                        this.sampleRateControl = (FloatControl)this.sourceDataLine.getControl(FloatControl.Type.SAMPLE_RATE);
                        this.initialSampleRate = this.sampleRateControl.getValue();
                    }
                }
                catch (IllegalArgumentException iae) {
                    this.sampleRateControl = null;
                    this.initialSampleRate = 0.0f;
                }
                break;
            }
            default: {
                this.errorMessage("Unrecognized channel type in method 'resetControls'");
                this.panControl = null;
                this.gainControl = null;
                this.sampleRateControl = null;
                break;
            }
        }
    }
    
    public void setLooping(final boolean value) {
        this.toLoop = value;
    }
    
    public void setPan(final float p) {
        if (this.panControl == null) {
            return;
        }
        float pan = p;
        if (pan < -1.0f) {
            pan = -1.0f;
        }
        if (pan > 1.0f) {
            pan = 1.0f;
        }
        this.panControl.setValue(pan);
    }
    
    public void setGain(final float g) {
        if (this.gainControl == null) {
            return;
        }
        float gain = g;
        if (gain < 0.0f) {
            gain = 0.0f;
        }
        if (gain > 1.0f) {
            gain = 1.0f;
        }
        final double minimumDB = this.gainControl.getMinimum();
        final double maximumDB = this.initialGain;
        final double ampGainDB = 0.5 * maximumDB - minimumDB;
        final double cste = Math.log(10.0) / 20.0;
        final float valueDB = (float)(minimumDB + 1.0 / cste * Math.log(1.0 + (Math.exp(cste * ampGainDB) - 1.0) * gain));
        this.gainControl.setValue(valueDB);
    }
    
    public void setPitch(final float p) {
        if (this.sampleRateControl == null) {
            return;
        }
        float sampleRate = p;
        if (sampleRate < 0.5f) {
            sampleRate = 0.5f;
        }
        if (sampleRate > 2.0f) {
            sampleRate = 2.0f;
        }
        sampleRate *= this.initialSampleRate;
        this.sampleRateControl.setValue(sampleRate);
    }
    
    @Override
    public boolean preLoadBuffers(final LinkedList<byte[]> bufferList) {
        if (this.errorCheck(this.channelType != 1, "Buffers may only be queued for streaming sources.")) {
            return false;
        }
        if (this.errorCheck(this.sourceDataLine == null, "SourceDataLine null in method 'preLoadBuffers'.")) {
            return false;
        }
        this.sourceDataLine.start();
        if (bufferList.isEmpty()) {
            return true;
        }
        final byte[] preLoad = bufferList.remove(0);
        if (this.errorCheck(preLoad == null, "Missing sound-bytes in method 'preLoadBuffers'.")) {
            return false;
        }
        while (!bufferList.isEmpty()) {
            this.streamBuffers.add(new SoundBuffer(bufferList.remove(0), this.myFormat));
        }
        this.sourceDataLine.write(preLoad, 0, preLoad.length);
        this.processed = 0;
        return true;
    }
    
    @Override
    public boolean queueBuffer(final byte[] buffer) {
        if (this.errorCheck(this.channelType != 1, "Buffers may only be queued for streaming sources.")) {
            return false;
        }
        if (this.errorCheck(this.sourceDataLine == null, "SourceDataLine null in method 'queueBuffer'.")) {
            return false;
        }
        if (this.errorCheck(this.myFormat == null, "AudioFormat null in method 'queueBuffer'")) {
            return false;
        }
        this.streamBuffers.add(new SoundBuffer(buffer, this.myFormat));
        this.processBuffer();
        this.processed = 0;
        return true;
    }
    
    @Override
    public boolean processBuffer() {
        if (this.errorCheck(this.channelType != 1, "Buffers are only processed for streaming sources.")) {
            return false;
        }
        if (this.errorCheck(this.sourceDataLine == null, "SourceDataLine null in method 'processBuffer'.")) {
            return false;
        }
        if (this.streamBuffers == null || this.streamBuffers.isEmpty()) {
            return false;
        }
        SoundBuffer nextBuffer = this.streamBuffers.remove(0);
        this.sourceDataLine.write(nextBuffer.audioData, 0, nextBuffer.audioData.length);
        if (!this.sourceDataLine.isActive()) {
            this.sourceDataLine.start();
        }
        nextBuffer.cleanup();
        nextBuffer = null;
        return true;
    }
    
    @Override
    public int feedRawAudioData(final byte[] buffer) {
        if (this.errorCheck(this.channelType != 1, "Raw audio data can only be processed by streaming sources.")) {
            return -1;
        }
        if (this.errorCheck(this.streamBuffers == null, "StreamBuffers queue null in method 'feedRawAudioData'.")) {
            return -1;
        }
        this.streamBuffers.add(new SoundBuffer(buffer, this.myFormat));
        return this.buffersProcessed();
    }
    
    @Override
    public int buffersProcessed() {
        this.processed = 0;
        if (this.errorCheck(this.channelType != 1, "Buffers may only be queued for streaming sources.")) {
            if (this.streamBuffers != null) {
                this.streamBuffers.clear();
            }
            return 0;
        }
        if (this.sourceDataLine == null) {
            if (this.streamBuffers != null) {
                this.streamBuffers.clear();
            }
            return 0;
        }
        if (this.sourceDataLine.available() > 0) {
            this.processed = 1;
        }
        return this.processed;
    }
    
    @Override
    public void flush() {
        if (this.channelType != 1) {
            return;
        }
        if (this.errorCheck(this.sourceDataLine == null, "SourceDataLine null in method 'flush'.")) {
            return;
        }
        this.sourceDataLine.stop();
        this.sourceDataLine.flush();
        this.sourceDataLine.drain();
        this.streamBuffers.clear();
        this.processed = 0;
    }
    
    @Override
    public void close() {
        switch (this.channelType) {
            case 0: {
                if (this.clip != null) {
                    this.clip.stop();
                    this.clip.flush();
                    this.clip.close();
                    break;
                }
                break;
            }
            case 1: {
                if (this.sourceDataLine != null) {
                    this.flush();
                    this.sourceDataLine.close();
                    break;
                }
                break;
            }
        }
    }
    
    @Override
    public void play() {
        switch (this.channelType) {
            case 0: {
                if (this.clip == null) {
                    break;
                }
                if (this.toLoop) {
                    this.clip.stop();
                    this.clip.loop(-1);
                    break;
                }
                this.clip.stop();
                this.clip.start();
                break;
            }
            case 1: {
                if (this.sourceDataLine != null) {
                    this.sourceDataLine.start();
                    break;
                }
                break;
            }
        }
    }
    
    @Override
    public void pause() {
        switch (this.channelType) {
            case 0: {
                if (this.clip != null) {
                    this.clip.stop();
                    break;
                }
                break;
            }
            case 1: {
                if (this.sourceDataLine != null) {
                    this.sourceDataLine.stop();
                    break;
                }
                break;
            }
        }
    }
    
    @Override
    public void stop() {
        switch (this.channelType) {
            case 0: {
                if (this.clip != null) {
                    this.clip.stop();
                    this.clip.setFramePosition(0);
                    break;
                }
                break;
            }
            case 1: {
                if (this.sourceDataLine != null) {
                    this.sourceDataLine.stop();
                    break;
                }
                break;
            }
        }
    }
    
    @Override
    public void rewind() {
        switch (this.channelType) {
            case 0: {
                if (this.clip != null) {
                    final boolean rePlay = this.clip.isRunning();
                    this.clip.stop();
                    this.clip.setFramePosition(0);
                    if (rePlay) {
                        if (this.toLoop) {
                            this.clip.loop(-1);
                        }
                        else {
                            this.clip.start();
                        }
                    }
                    break;
                }
                break;
            }
        }
    }
    
    @Override
    public float millisecondsPlayed() {
        switch (this.channelType) {
            case 0: {
                if (this.clip == null) {
                    return -1.0f;
                }
                return this.clip.getMicrosecondPosition() / 1000.0f;
            }
            case 1: {
                if (this.sourceDataLine == null) {
                    return -1.0f;
                }
                return this.sourceDataLine.getMicrosecondPosition() / 1000.0f;
            }
            default: {
                return -1.0f;
            }
        }
    }
    
    @Override
    public boolean playing() {
        switch (this.channelType) {
            case 0: {
                return this.clip != null && this.clip.isActive();
            }
            case 1: {
                return this.sourceDataLine != null && this.sourceDataLine.isActive();
            }
            default: {
                return false;
            }
        }
    }
}
