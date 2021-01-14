package de.iotacb.cu.core.file;

import java.io.BufferedReader;
import java.io.IOException;

public class FileReader {

    public static final StringBuilder readAsStringBuilder(final String path, final int fromLine, final int toLine) {
        StringBuilder fileContents = new StringBuilder();
        try {

            BufferedReader reader = new BufferedReader(new java.io.FileReader(path));

            String currentReadingLine;
            int readLines = 0;

            while ((currentReadingLine = reader.readLine()) != null) {
                readLines++;
                if ((toLine > 0 ? readLines > toLine : false) || readLines < fromLine) continue;
                fileContents.append(currentReadingLine).append("\n");
            }

            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContents;
    }
    
    public static final StringBuilder readAsStringBuilder(final String path) {
    	return readAsStringBuilder(path, 0, 0);
    }
	
}
