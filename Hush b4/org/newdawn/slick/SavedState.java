// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick;

import org.newdawn.slick.util.Log;
import javax.jnlp.ServiceManager;
import java.io.IOException;
import org.newdawn.slick.muffin.FileMuffin;
import org.newdawn.slick.muffin.WebstartMuffin;
import java.util.HashMap;
import org.newdawn.slick.muffin.Muffin;

public class SavedState
{
    private String fileName;
    private Muffin muffin;
    private HashMap numericData;
    private HashMap stringData;
    
    public SavedState(final String fileName) throws SlickException {
        this.numericData = new HashMap();
        this.stringData = new HashMap();
        this.fileName = fileName;
        if (this.isWebstartAvailable()) {
            this.muffin = new WebstartMuffin();
        }
        else {
            this.muffin = new FileMuffin();
        }
        try {
            this.load();
        }
        catch (IOException e) {
            throw new SlickException("Failed to load state on startup", e);
        }
    }
    
    public double getNumber(final String nameOfField) {
        return this.getNumber(nameOfField, 0.0);
    }
    
    public double getNumber(final String nameOfField, final double defaultValue) {
        final Double value = this.numericData.get(nameOfField);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }
    
    public void setNumber(final String nameOfField, final double value) {
        this.numericData.put(nameOfField, new Double(value));
    }
    
    public String getString(final String nameOfField) {
        return this.getString(nameOfField, null);
    }
    
    public String getString(final String nameOfField, final String defaultValue) {
        final String value = this.stringData.get(nameOfField);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }
    
    public void setString(final String nameOfField, final String value) {
        this.stringData.put(nameOfField, value);
    }
    
    public void save() throws IOException {
        this.muffin.saveFile(this.numericData, String.valueOf(this.fileName) + "_Number");
        this.muffin.saveFile(this.stringData, String.valueOf(this.fileName) + "_String");
    }
    
    public void load() throws IOException {
        this.numericData = this.muffin.loadFile(String.valueOf(this.fileName) + "_Number");
        this.stringData = this.muffin.loadFile(String.valueOf(this.fileName) + "_String");
    }
    
    public void clear() {
        this.numericData.clear();
        this.stringData.clear();
    }
    
    private boolean isWebstartAvailable() {
        try {
            Class.forName("javax.jnlp.ServiceManager");
            ServiceManager.lookup("javax.jnlp.PersistenceService");
            Log.info("Webstart detected using Muffins");
        }
        catch (Exception e) {
            Log.info("Using Local File System");
            return false;
        }
        return true;
    }
}
