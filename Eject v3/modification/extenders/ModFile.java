package modification.extenders;

import modification.main.Modification;
import modification.managers.FileManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

public abstract class ModFile {
    protected static final String SPLIT = "~";
    public final String name;
    public final File file = new File(Modification.DIRECTORY, (this.name = paramString).concat(".txt"));

    protected ModFile(String paramString) {
        FileManager.FILES.add(this);
    }

    protected final void writeLine(BufferedWriter paramBufferedWriter, String paramString) {
        try {
            paramBufferedWriter.write(paramString);
            paramBufferedWriter.newLine();
        } catch (IOException localIOException) {
            Modification.LOG_UTIL.sendConsoleMessage("Error: Couldn't write line");
        }
    }

    public abstract void write();

    public abstract void read();
}




