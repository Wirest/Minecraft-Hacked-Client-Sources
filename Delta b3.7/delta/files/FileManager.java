/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 */
package delta.files;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import delta.Class155;
import delta.Class169;
import delta.Class170;
import delta.Class40;
import delta.Class43;
import delta.Class70;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private File server$ = Class43._agents();
    private List<Class170> retrieve$;
    private Gson concert$ = new GsonBuilder().serializeNulls().setPrettyPrinting().create();

    public FileManager() {
        this.retrieve$ = new ArrayList<Class170>();
    }

    public void _medicine() {
        this._visiting();
        this._terrace();
    }

    public void _watts() {
        this._laura();
        this._visiting();
        this._theatre();
    }

    private void _visiting() {
        if (!this.server$.exists()) {
            this.server$.mkdir();
        }
        for (Class170 class170 : this.retrieve$) {
            class170._region();
        }
    }

    private void _laura() {
        this.retrieve$.add(new Class40(this.concert$, new File(this.server$, "modules")));
        this.retrieve$.add(new Class155(this.concert$, new File(this.server$, "xrayconfig.json")));
        this.retrieve$.add(new Class169(this.concert$, new File(this.server$, "friends.json")));
        this.retrieve$.add(new Class70(this.concert$, new File(this.server$, "spammermsg.txt")));
    }

    public void _theatre() {
        try {
            for (Class170 class170 : this.retrieve$) {
                class170._redhead();
            }
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }

    private void _terrace() {
        try {
            for (Class170 class170 : this.retrieve$) {
                class170._segment();
            }
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }
}

