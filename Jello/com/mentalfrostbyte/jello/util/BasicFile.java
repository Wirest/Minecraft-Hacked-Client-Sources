package com.mentalfrostbyte.jello.util;

import java.io.File;




public abstract class BasicFile
{
    private final File file;
    private final String name;
    private static final File BIND_DIR = FileUtils.getConfigFile("Alts");
    public BasicFile(final String name) {
        this.name = name;
        this.file = new File(BIND_DIR, "." + name);
        if (!this.file.exists()) {
            this.saveFile();
        }
    }
    
    public abstract void saveFile();
    
    public abstract void loadFile();
    
    public abstract void load();
    
    public final String getName() {
        return this.name;
    }
    
    public final File getFile() {
        return this.file;
    }
}
