// 
// Decompiled by Procyon v0.5.36
// 

package com.jcraft.jorbis;

import java.io.InputStream;
import com.jcraft.jogg.Packet;
import com.jcraft.jogg.Page;
import com.jcraft.jogg.StreamState;
import com.jcraft.jogg.SyncState;
import java.io.FileInputStream;

class DecodeExample
{
    static int convsize;
    static byte[] convbuffer;
    
    public static void main(final String[] arg) {
        InputStream input = System.in;
        if (arg.length > 0) {
            try {
                input = new FileInputStream(arg[0]);
            }
            catch (Exception e) {
                System.err.println(e);
            }
        }
        final SyncState oy = new SyncState();
        final StreamState os = new StreamState();
        final Page og = new Page();
        final Packet op = new Packet();
        final Info vi = new Info();
        final Comment vc = new Comment();
        final DspState vd = new DspState();
        final Block vb = new Block(vd);
        int bytes = 0;
        oy.init();
        while (true) {
            int eos = 0;
            int index = oy.buffer(4096);
            byte[] buffer = oy.data;
            try {
                bytes = input.read(buffer, index, 4096);
            }
            catch (Exception e2) {
                System.err.println(e2);
                System.exit(-1);
            }
            oy.wrote(bytes);
            if (oy.pageout(og) != 1) {
                if (bytes < 4096) {
                    break;
                }
                System.err.println("Input does not appear to be an Ogg bitstream.");
                System.exit(1);
            }
            os.init(og.serialno());
            vi.init();
            vc.init();
            if (os.pagein(og) < 0) {
                System.err.println("Error reading first page of Ogg bitstream data.");
                System.exit(1);
            }
            if (os.packetout(op) != 1) {
                System.err.println("Error reading initial header packet.");
                System.exit(1);
            }
            if (vi.synthesis_headerin(vc, op) < 0) {
                System.err.println("This Ogg bitstream does not contain Vorbis audio data.");
                System.exit(1);
            }
            int i = 0;
            while (i < 2) {
                while (i < 2) {
                    int result = oy.pageout(og);
                    if (result == 0) {
                        break;
                    }
                    if (result != 1) {
                        continue;
                    }
                    os.pagein(og);
                    while (i < 2) {
                        result = os.packetout(op);
                        if (result == 0) {
                            break;
                        }
                        if (result == -1) {
                            System.err.println("Corrupt secondary header.  Exiting.");
                            System.exit(1);
                        }
                        vi.synthesis_headerin(vc, op);
                        ++i;
                    }
                }
                index = oy.buffer(4096);
                buffer = oy.data;
                try {
                    bytes = input.read(buffer, index, 4096);
                }
                catch (Exception e3) {
                    System.err.println(e3);
                    System.exit(1);
                }
                if (bytes == 0 && i < 2) {
                    System.err.println("End of file before finding all Vorbis headers!");
                    System.exit(1);
                }
                oy.wrote(bytes);
            }
            final byte[][] ptr = vc.user_comments;
            for (int j = 0; j < ptr.length && ptr[j] != null; ++j) {
                System.err.println(new String(ptr[j], 0, ptr[j].length - 1));
            }
            System.err.println("\nBitstream is " + vi.channels + " channel, " + vi.rate + "Hz");
            System.err.println("Encoded by: " + new String(vc.vendor, 0, vc.vendor.length - 1) + "\n");
            DecodeExample.convsize = 4096 / vi.channels;
            vd.synthesis_init(vi);
            vb.init(vd);
            final float[][][] _pcm = { null };
            final int[] _index = new int[vi.channels];
            while (eos == 0) {
                while (eos == 0) {
                    int result2 = oy.pageout(og);
                    if (result2 == 0) {
                        break;
                    }
                    if (result2 == -1) {
                        System.err.println("Corrupt or missing data in bitstream; continuing...");
                    }
                    else {
                        os.pagein(og);
                        while (true) {
                            result2 = os.packetout(op);
                            if (result2 == 0) {
                                break;
                            }
                            if (result2 == -1) {
                                continue;
                            }
                            if (vb.synthesis(op) == 0) {
                                vd.synthesis_blockin(vb);
                            }
                            int samples;
                            while ((samples = vd.synthesis_pcmout(_pcm, _index)) > 0) {
                                final float[][] pcm = _pcm[0];
                                final int bout = (samples < DecodeExample.convsize) ? samples : DecodeExample.convsize;
                                for (i = 0; i < vi.channels; ++i) {
                                    int ptr2 = i * 2;
                                    final int mono = _index[i];
                                    for (int k = 0; k < bout; ++k) {
                                        int val = (int)(pcm[i][mono + k] * 32767.0);
                                        if (val > 32767) {
                                            val = 32767;
                                        }
                                        if (val < -32768) {
                                            val = -32768;
                                        }
                                        if (val < 0) {
                                            val |= 0x8000;
                                        }
                                        DecodeExample.convbuffer[ptr2] = (byte)val;
                                        DecodeExample.convbuffer[ptr2 + 1] = (byte)(val >>> 8);
                                        ptr2 += 2 * vi.channels;
                                    }
                                }
                                System.out.write(DecodeExample.convbuffer, 0, 2 * vi.channels * bout);
                                vd.synthesis_read(bout);
                            }
                        }
                        if (og.eos() == 0) {
                            continue;
                        }
                        eos = 1;
                    }
                }
                if (eos == 0) {
                    index = oy.buffer(4096);
                    buffer = oy.data;
                    try {
                        bytes = input.read(buffer, index, 4096);
                    }
                    catch (Exception e4) {
                        System.err.println(e4);
                        System.exit(1);
                    }
                    oy.wrote(bytes);
                    if (bytes != 0) {
                        continue;
                    }
                    eos = 1;
                }
            }
            os.clear();
            vb.clear();
            vd.clear();
            vi.clear();
        }
        oy.clear();
        System.err.println("Done.");
    }
    
    static {
        DecodeExample.convsize = 8192;
        DecodeExample.convbuffer = new byte[DecodeExample.convsize];
    }
}
