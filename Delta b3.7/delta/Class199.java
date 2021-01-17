/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper
 *  delta.OVYt$968L
 *  org.objectweb.asm.ClassReader
 *  org.objectweb.asm.ClassVisitor
 *  org.objectweb.asm.ClassWriter
 */
package delta;

import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import delta.Class103;
import delta.Class119;
import delta.Class121;
import delta.Class127;
import delta.Class162;
import delta.Class71;
import delta.OVYt;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

public class Class199 {
    private final ClassLoader minus$;
    private static String[] silicon$;
    private final HashMap<String, Class162> thirty$ = new HashMap();
    private static final Class162 armed$;

    public Class162 _budget(String string) {
        return this._tractor(string, 62 - 74 + 58 + -46);
    }

    public ClassWriter _travel() {
        return new Class71(this, 35 - 65 + 50 + -17);
    }

    public Class199(ClassLoader classLoader) {
        this.thirty$.put(OVYt.968L.FS1x((String)silicon$[6], (int)-638870550), armed$);
        this.minus$ = classLoader == null ? ClassLoader.getSystemClassLoader() : classLoader;
    }

    public void _young(Map<String, byte[]> map) {
        for (Map.Entry<String, byte[]> entry : map.entrySet()) {
            if (!entry.getKey().endsWith(OVYt.968L.FS1x((String)silicon$[0], (int)-1430843182))) continue;
            String string = entry.getKey().substring(118 - 220 + 126 + -24, entry.getKey().length() - (218 - 267 + 233 + -178));
            Class127 class127 = new Class127(this, string, null);
            try {
                ClassReader classReader = new ClassReader(entry.getValue());
                classReader.accept((ClassVisitor)new Class121(this, 175 - 257 + 217 - 41 + 327586, class127), 109 - 147 + 53 - 43 + 29);
            }
            catch (Exception exception) {
                System.err.println(OVYt.968L.FS1x((String)silicon$[1], (int)-453624890) + string);
                class127.v4AI = OVYt.968L.FS1x((String)silicon$[2], (int)-772322405);
            }
            this.thirty$.put(string, class127);
        }
    }

    private Class162 _tractor(String string, boolean bl) {
        Class162 class162 = this.thirty$.get(string = string.replace(181 - 227 + 44 - 43 + 91, 85 - 110 + 75 + -3));
        if (class162 != null) {
            return class162;
        }
        class162 = new Class127(this, string, null);
        try {
            ClassReader classReader = new ClassReader(Objects.requireNonNull(this.minus$.getResourceAsStream(string + OVYt.968L.FS1x((String)silicon$[3], (int)-127594428))));
            Class127 class127 = (Class127)class162;
            classReader.accept((ClassVisitor)new Class103(this, 118 - 203 + 99 + 327666, class127), 196 - 233 + 163 + -125);
        }
        catch (Exception exception) {
            if (bl) {
                System.err.println(OVYt.968L.FS1x((String)silicon$[4], (int)-2145249075) + string);
                class162.gotta$ = OVYt.968L.FS1x((String)silicon$[5], (int)296208873);
            }
            String string2 = FMLDeobfuscatingRemapper.INSTANCE.unmap(string);
            return this._tractor(string2, 92 - 132 + 78 - 16 + -21);
        }
        this.thirty$.put(string, class162);
        return class162;
    }

    static {
        Class199._prompt();
        armed$ = new Class119(null);
    }

    private static void _prompt() {
        silicon$ = new String[]{"\u10fc\u10b1\u10be\u10b3\u10a1\u10a1", "\u3b8f\u3ba8\u3bb0\u3ba7\u3baa\u3baf\u3ba2\u3be6\u3b85\u3baa\u3ba7\u3bb5\u3bb5\u3be6\u3bfb\u3bf8\u3be6", "\u4bf1\u4bfa\u4bed\u4bfa\u4bb4\u4bf7\u4bfa\u4bf5\u4bfc\u4bb4\u4bd4\u4bf9\u4bf1\u4bfe\u4bf8\u4bef", "\u106a\u1027\u1028\u1025\u1037\u1037", "\u1880\u18a4\u18be\u18be\u18a4\u18a3\u18aa\u18ed\u188e\u18a1\u18ac\u18be\u18be\u18ed\u18f0\u18f3\u18ed", "\uc983\uc988\uc99f\uc988\uc9c6\uc985\uc988\uc987\uc98e\uc9c6\uc9a6\uc98b\uc983\uc98c\uc98a\uc99d", "\u9b80\u9b8b\u9b9c\u9b8b\u9bc5\u9b86\u9b8b\u9b84\u9b8d\u9bc5\u9ba5\u9b88\u9b80\u9b8f\u9b89\u9b9e"};
    }
}

