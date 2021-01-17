// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.compressors.pack200;

import java.io.OutputStream;
import java.util.jar.JarOutputStream;
import java.util.jar.JarFile;
import java.util.jar.Pack200;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.io.IOException;
import java.util.Map;
import java.io.File;

public class Pack200Utils
{
    private Pack200Utils() {
    }
    
    public static void normalize(final File jar) throws IOException {
        normalize(jar, jar, null);
    }
    
    public static void normalize(final File jar, final Map<String, String> props) throws IOException {
        normalize(jar, jar, props);
    }
    
    public static void normalize(final File from, final File to) throws IOException {
        normalize(from, to, null);
    }
    
    public static void normalize(final File from, final File to, Map<String, String> props) throws IOException {
        if (props == null) {
            props = new HashMap<String, String>();
        }
        props.put("pack.segment.limit", "-1");
        final File f = File.createTempFile("commons-compress", "pack200normalize");
        f.deleteOnExit();
        try {
            OutputStream os = new FileOutputStream(f);
            JarFile j = null;
            try {
                final Pack200.Packer p = Pack200.newPacker();
                p.properties().putAll((Map<?, ?>)props);
                p.pack(j = new JarFile(from), os);
                j = null;
                os.close();
                os = null;
                final Pack200.Unpacker u = Pack200.newUnpacker();
                os = new JarOutputStream(new FileOutputStream(to));
                u.unpack(f, (JarOutputStream)os);
            }
            finally {
                if (j != null) {
                    j.close();
                }
                if (os != null) {
                    os.close();
                }
            }
        }
        finally {
            f.delete();
        }
    }
}
