// 
// Decompiled by Procyon v0.5.36
// 

package paulscode.sound;

import java.net.URL;

public class FilenameURL
{
    private SoundSystemLogger logger;
    private String filename;
    private URL url;
    
    public FilenameURL(final URL url, final String identifier) {
        this.filename = null;
        this.url = null;
        this.logger = SoundSystemConfig.getLogger();
        this.filename = identifier;
        this.url = url;
    }
    
    public FilenameURL(final String filename) {
        this.filename = null;
        this.url = null;
        this.logger = SoundSystemConfig.getLogger();
        this.filename = filename;
        this.url = null;
    }
    
    public String getFilename() {
        return this.filename;
    }
    
    public URL getURL() {
        if (this.url == null) {
            if (this.filename.matches(SoundSystemConfig.PREFIX_URL)) {
                try {
                    this.url = new URL(this.filename);
                    return this.url;
                }
                catch (Exception e) {
                    this.errorMessage("Unable to access online URL in method 'getURL'");
                    this.printStackTrace(e);
                    return null;
                }
            }
            this.url = this.getClass().getClassLoader().getResource(SoundSystemConfig.getSoundFilesPackage() + this.filename);
        }
        return this.url;
    }
    
    private void errorMessage(final String message) {
        this.logger.errorMessage("MidiChannel", message, 0);
    }
    
    private void printStackTrace(final Exception e) {
        this.logger.printStackTrace(e, 1);
    }
}
