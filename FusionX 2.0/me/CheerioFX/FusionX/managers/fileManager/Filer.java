// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.managers.fileManager;

import java.util.Iterator;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.io.File;
import net.minecraft.client.Minecraft;

public class Filer
{
    protected Minecraft mc;
    private String fileName;
    private File path;
    
    public Filer(String fileName, final String clientName) {
        this.mc = Minecraft.getMinecraft();
        fileName = String.valueOf(fileName) + ".txt";
        this.fileName = fileName;
        this.path = new File(String.valueOf(this.mc.mcDataDir.getAbsolutePath()) + File.separator + clientName + File.separator);
        if (!this.path.exists()) {
            try {
                this.path.mkdir();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public final ArrayList<String> read() {
        final ArrayList<String> list = new ArrayList<String>();
        try {
            final BufferedReader br = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(new File(this.path, this.fileName).getAbsolutePath()))));
            while (true) {
                final String text = br.readLine();
                if (text == null) {
                    break;
                }
                list.add(text.trim());
            }
            br.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public void write(final String text) {
        this.write(new String[] { text });
    }
    
    public void write(final String[] text) {
        if (text == null || text.length == 0 || text[0].trim() == "") {
            return;
        }
        try {
            final BufferedWriter bw = new BufferedWriter(new FileWriter(new File(this.path, this.fileName), true));
            for (final String line : text) {
                bw.write(line);
                bw.write("\r\n");
            }
            bw.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void remove(final int line) {
        final ArrayList<String> file = this.read();
        if (file.size() < line) {
            return;
        }
        this.clear();
        int loop = 1;
        for (final String text : file) {
            if (loop != line) {
                this.write(text);
            }
            ++loop;
        }
    }
    
    public void clear() {
        try {
            final BufferedWriter bw = new BufferedWriter(new FileWriter(new File(this.path, this.fileName)));
            bw.write("");
            bw.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
