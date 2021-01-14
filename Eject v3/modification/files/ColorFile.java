package modification.files;

import modification.extenders.ModFile;
import modification.main.Modification;

import java.awt.*;
import java.io.*;

public final class ColorFile
        extends ModFile {
    public ColorFile(String paramString) {
        super(paramString);
    }

    public void write() {
        try {
            BufferedWriter localBufferedWriter = new BufferedWriter(new FileWriter(this.file));
            writeLine(localBufferedWriter, Integer.toString(Modification.color.getRed()).concat("~").concat(Integer.toString(Modification.color.getGreen())).concat("~").concat(Integer.toString(Modification.color.getBlue())));
            localBufferedWriter.close();
        } catch (IOException localIOException) {
            Modification.LOG_UTIL.sendConsoleMessage("Error: Couldn't write file");
        }
    }

    public void read() {
        try {
            BufferedReader localBufferedReader = new BufferedReader(new FileReader(this.file));
            String str;
            while ((str = localBufferedReader.readLine()) != null) {
                String[] arrayOfString = str.split("~");
                if (arrayOfString.length == 3) {
                    Modification.color = new Color(Integer.parseInt(arrayOfString[0]), Integer.parseInt(arrayOfString[1]), Integer.parseInt(arrayOfString[2]), 255);
                }
            }
            localBufferedReader.close();
        } catch (IOException localIOException) {
            Modification.LOG_UTIL.sendConsoleMessage("Error: Couldn't read file");
        }
    }
}




