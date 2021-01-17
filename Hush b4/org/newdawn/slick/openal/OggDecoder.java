// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.openal;

import java.nio.ByteBuffer;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class OggDecoder
{
    private int convsize;
    private byte[] convbuffer;
    
    public OggDecoder() {
        this.convsize = 16384;
        this.convbuffer = new byte[this.convsize];
    }
    
    public OggData getData(final InputStream input) throws IOException {
        if (input == null) {
            throw new IOException("Failed to read OGG, source does not exist?");
        }
        final ByteArrayOutputStream dataout = new ByteArrayOutputStream();
        final OggInputStream oggInput = new OggInputStream(input);
        final boolean done = false;
        while (!oggInput.atEnd()) {
            dataout.write(oggInput.read());
        }
        final OggData ogg = new OggData();
        ogg.channels = oggInput.getChannels();
        ogg.rate = oggInput.getRate();
        final byte[] data = dataout.toByteArray();
        (ogg.data = ByteBuffer.allocateDirect(data.length)).put(data);
        ogg.data.rewind();
        return ogg;
    }
}
