// 
// Decompiled by Procyon v0.5.36
// 

package paulscode.sound;

import javax.sound.sampled.AudioFormat;
import java.net.URL;

public interface ICodec
{
    void reverseByteOrder(final boolean p0);
    
    boolean initialize(final URL p0);
    
    boolean initialized();
    
    SoundBuffer read();
    
    SoundBuffer readAll();
    
    boolean endOfStream();
    
    void cleanup();
    
    AudioFormat getAudioFormat();
}
