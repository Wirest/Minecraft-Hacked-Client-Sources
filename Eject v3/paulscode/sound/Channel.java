package paulscode.sound;

import javax.sound.sampled.AudioFormat;
import java.util.LinkedList;

public class Channel {
    public int channelType;
    public Source attachedSource = null;
    public int buffersUnqueued = 0;
    protected Class libraryType = Library.class;
    private SoundSystemLogger logger = SoundSystemConfig.getLogger();

    public Channel(int paramInt) {
        this.channelType = paramInt;
    }

    public void cleanup() {
        this.logger = null;
    }

    public boolean preLoadBuffers(LinkedList<byte[]> paramLinkedList) {
        return true;
    }

    public boolean queueBuffer(byte[] paramArrayOfByte) {
        return false;
    }

    public int feedRawAudioData(byte[] paramArrayOfByte) {
        return 1;
    }

    public int buffersProcessed() {
        return 0;
    }

    public float millisecondsPlayed() {
        return -1.0F;
    }

    public boolean processBuffer() {
        return false;
    }

    public void setAudioFormat(AudioFormat paramAudioFormat) {
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
        String str = SoundSystemConfig.getLibraryTitle(this.libraryType);
        if (str.equals("No Sound")) {
            return "Channel";
        }
        return "Channel" + str;
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




