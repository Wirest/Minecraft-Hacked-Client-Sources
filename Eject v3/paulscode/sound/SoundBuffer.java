package paulscode.sound;

import javax.sound.sampled.AudioFormat;

public class SoundBuffer {
    public byte[] audioData;
    public AudioFormat audioFormat;

    public SoundBuffer(byte[] paramArrayOfByte, AudioFormat paramAudioFormat) {
        this.audioData = paramArrayOfByte;
        this.audioFormat = paramAudioFormat;
    }

    public void cleanup() {
        this.audioData = null;
        this.audioFormat = null;
    }

    public void trimData(int paramInt) {
        if ((this.audioData == null) || (paramInt == 0)) {
            this.audioData = null;
        } else if (this.audioData.length > paramInt) {
            byte[] arrayOfByte = new byte[paramInt];
            System.arraycopy(this.audioData, 0, arrayOfByte, 0, paramInt);
            this.audioData = arrayOfByte;
        }
    }
}




