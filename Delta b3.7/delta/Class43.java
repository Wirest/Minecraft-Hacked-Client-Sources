/*
 * Decompiled with CFR 0.150.
 */
package delta;

import delta.Class83;
import java.io.File;

public class Class43 {
    public static File _antonio() {
        return new File(Class43._their(), "libraries");
    }

    public static File _agents() {
        File file = new File(Class83._oxygen(), "Delta");
        File file2 = new File(Class83._oxygen(), "Delta Client");
        if (file.exists() && file.isDirectory()) {
            file.renameTo(file2);
        }
        return file2;
    }

    public static File _although() {
        return new File(Class43._their(), "assets");
    }

    public static File _their() {
        return new File(Class43._agents(), "cache");
    }
}

