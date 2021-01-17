// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.archivers;

import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.File;

public final class Lister
{
    private static final ArchiveStreamFactory factory;
    
    public static void main(final String[] args) throws Exception {
        if (args.length == 0) {
            usage();
            return;
        }
        System.out.println("Analysing " + args[0]);
        final File f = new File(args[0]);
        if (!f.isFile()) {
            System.err.println(f + " doesn't exist or is a directory");
        }
        final InputStream fis = new BufferedInputStream(new FileInputStream(f));
        ArchiveInputStream ais;
        if (args.length > 1) {
            ais = Lister.factory.createArchiveInputStream(args[1], fis);
        }
        else {
            ais = Lister.factory.createArchiveInputStream(fis);
        }
        System.out.println("Created " + ais.toString());
        ArchiveEntry ae;
        while ((ae = ais.getNextEntry()) != null) {
            System.out.println(ae.getName());
        }
        ais.close();
        fis.close();
    }
    
    private static void usage() {
        System.out.println("Parameters: archive-name [archive-type]");
    }
    
    static {
        factory = new ArchiveStreamFactory();
    }
}
