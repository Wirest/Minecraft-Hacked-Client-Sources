package paulscode.sound;

import java.net.URL;

public class FilenameURL {
    private SoundSystemLogger logger = SoundSystemConfig.getLogger();
    private String filename = null;
    private URL url = null;

    public FilenameURL(URL paramURL, String paramString) {
        this.filename = paramString;
        this.url = paramURL;
    }

    public FilenameURL(String paramString) {
        this.filename = paramString;
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
                } catch (Exception localException) {
                    errorMessage("Unable to access online URL in method 'getURL'");
                    printStackTrace(localException);
                    return null;
                }
            } else {
                this.url = getClass().getClassLoader().getResource(SoundSystemConfig.getSoundFilesPackage() + this.filename);
            }
        }
        return this.url;
    }

    private void errorMessage(String paramString) {
        this.logger.errorMessage("MidiChannel", paramString, 0);
    }

    private void printStackTrace(Exception paramException) {
        this.logger.printStackTrace(paramException, 1);
    }
}




