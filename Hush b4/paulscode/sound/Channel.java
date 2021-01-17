// 
// Decompiled by Procyon v0.5.36
// 

package paulscode.sound;

import javax.sound.sampled.AudioFormat;
import java.util.LinkedList;

public class Channel
{
    protected Class libraryType;
    public int channelType;
    private SoundSystemLogger logger;
    public Source attachedSource;
    public int buffersUnqueued;
    
    public Channel(final int type) {
        this.libraryType = Library.class;
        this.attachedSource = null;
        this.buffersUnqueued = 0;
        this.logger = SoundSystemConfig.getLogger();
        this.channelType = type;
    }
    
    public void cleanup() {
        this.logger = null;
    }
    
    public boolean preLoadBuffers(final LinkedList<byte[]> bufferList) {
        return true;
    }
    
    public boolean queueBuffer(final byte[] buffer) {
        return false;
    }
    
    public int feedRawAudioData(final byte[] buffer) {
        return 1;
    }
    
    public int buffersProcessed() {
        return 0;
    }
    
    public float millisecondsPlayed() {
        return -1.0f;
    }
    
    public boolean processBuffer() {
        return false;
    }
    
    public void setAudioFormat(final AudioFormat audioFormat) {
    }
    
    public void flush() {
    }
    
    public void close() {
    }
    
    public void play() {
    }
    
    public void pause() {
    }
    
    public void stop() {
    }
    
    public void rewind() {
    }
    
    public boolean playing() {
        return false;
    }
    
    public String getClassName() {
        final String libTitle = SoundSystemConfig.getLibraryTitle(this.libraryType);
        if (libTitle.equals("No Sound")) {
            return "Channel";
        }
        return "Channel" + libTitle;
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
