/*
 * Decompiled with CFR 0.150.
 */
package delta;

import delta.Class126;
import delta.Class68;
import java.io.File;

public class Class83 {
    public static Class126 _disclose() {
        String string = System.getProperty("os.name").toLowerCase();
        return string.contains("win") ? Class126.baseball$ : (string.contains("mac") ? Class126.versus$ : (string.contains("solaris") ? Class126.factor$ : (string.contains("sunos") ? Class126.factor$ : (string.contains("linux") ? Class126.equal$ : (string.contains("unix") ? Class126.equal$ : Class126.imagine$)))));
    }

    public static File _oxygen() {
        File file;
        String string = System.getProperty("user.home", ".");
        switch (Class68.superior$[Class83._disclose().ordinal()]) {
            case 1: {
                file = new File(string);
                break;
            }
            case 2: {
                String string2 = System.getenv("APPDATA");
                String string3 = string2 != null ? string2 : string;
                file = new File(string3);
                break;
            }
            case 3: {
                file = new File(string, "Library/Application Support");
                break;
            }
            default: {
                file = new File(string);
            }
        }
        return file;
    }
}

