package com.mentalfrostbyte.jello.util;

import java.util.ArrayList;
import java.util.List;




public class FileManager
{
    private List<BasicFile> files;
    
    public BasicFile getFileByName(final String filename) {
        if (this.files == null || this.files.isEmpty()) {
            return null;
        }
        for (final BasicFile file : this.files) {
            if (file.getName().equalsIgnoreCase(filename)) {
                return file;
            }
        }
        return null;
    }
    
    public void setupFiles() {
    }
    
    public void loadFiles() {
        if (!this.files.isEmpty()) {
            for (final BasicFile file : this.files) {
                file.loadFile();
            }
        }
    }
    
    public List<BasicFile> getFiles() {
        return this.files;
    }
    
}
