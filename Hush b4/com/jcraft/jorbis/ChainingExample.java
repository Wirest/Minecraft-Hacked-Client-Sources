// 
// Decompiled by Procyon v0.5.36
// 

package com.jcraft.jorbis;

class ChainingExample
{
    public static void main(final String[] arg) {
        VorbisFile ov = null;
        try {
            if (arg.length > 0) {
                ov = new VorbisFile(arg[0]);
            }
            else {
                ov = new VorbisFile(System.in, null, -1);
            }
        }
        catch (Exception e) {
            System.err.println(e);
            return;
        }
        if (ov.seekable()) {
            System.out.println("Input bitstream contained " + ov.streams() + " logical bitstream section(s).");
            System.out.println("Total bitstream playing time: " + ov.time_total(-1) + " seconds\n");
        }
        else {
            System.out.println("Standard input was not seekable.");
            System.out.println("First logical bitstream information:\n");
        }
        for (int i = 0; i < ov.streams(); ++i) {
            final Info vi = ov.getInfo(i);
            System.out.println("\tlogical bitstream section " + (i + 1) + " information:");
            System.out.println("\t\t" + vi.rate + "Hz " + vi.channels + " channels bitrate " + ov.bitrate(i) / 1000 + "kbps serial number=" + ov.serialnumber(i));
            System.out.print("\t\tcompressed length: " + ov.raw_total(i) + " bytes ");
            System.out.println(" play time: " + ov.time_total(i) + "s");
            final Comment vc = ov.getComment(i);
            System.out.println(vc);
        }
    }
}
