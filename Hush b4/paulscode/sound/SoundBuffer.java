// 
// Decompiled by Procyon v0.5.36
// 

package paulscode.sound;

import javax.sound.sampled.AudioFormat;

public class SoundBuffer
{
    public byte[] audioData;
    public AudioFormat audioFormat;
    
    public SoundBuffer(final byte[] audioData, final AudioFormat audioFormat) {
        this.audioData = audioData;
        this.audioFormat = audioFormat;
    }
    
    public void cleanup() {
        this.audioData = null;
        this.audioFormat = null;
    }
    
    public void trimData(final int maxLength) {
        if (this.audioData == null || maxLength == 0) {
            this.audioData = null;
        }
        else if (this.audioData.length > maxLength) {
            final byte[] trimmedArray = new byte[maxLength];
            System.arraycopy(this.audioData, 0, trimmedArray, 0, maxLength);
            this.audioData = trimmedArray;
        }
    }
}
