package paulscode.sound;

import javax.sound.sampled.AudioFormat;
import java.net.URL;

public abstract interface ICodec {
    public abstract void reverseByteOrder(boolean paramBoolean);

    public abstract boolean initialize(URL paramURL);

    public abstract boolean initialized();

    public abstract SoundBuffer read();

    public abstract SoundBuffer readAll();

    public abstract boolean endOfStream();

    public abstract void cleanup();

    public abstract AudioFormat getAudioFormat();
}




