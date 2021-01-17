/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 */
package delta;

import com.google.gson.Gson;
import delta.Class170;
import delta.Class93;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Class70
extends Class170 {
    @Override
    public void _segment() throws IOException {
        FileWriter fileWriter = new FileWriter(this._duncan());
        fileWriter.write(Class93.basin$);
        fileWriter.close();
    }

    @Override
    public void _region() {
        if (this._duncan() != null && !this._duncan().exists()) {
            try {
                this._duncan().createNewFile();
            }
            catch (IOException iOException) {
                iOException.printStackTrace();
            }
        }
    }

    @Override
    public void _redhead() throws IOException {
        if (this._duncan().exists()) {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(this._duncan()));
            String string = bufferedReader.readLine();
            if (string != null) {
                Class93.basin$ = string;
            }
            bufferedReader.close();
        }
    }

    public Class70(Gson gson, File file) {
        super(gson, file);
    }
}

