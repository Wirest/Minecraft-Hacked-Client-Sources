package cn.kody.debug.utils.file;

import cn.kody.debug.Client;
import cn.kody.debug.friend.FriendsManager;
import cn.kody.debug.mod.Mod;
import cn.kody.debug.mod.ModManager;
import cn.kody.debug.utils.Friend;
import cn.kody.debug.value.Value;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class FileUtils {
    private Minecraft mc = Minecraft.getMinecraft();
    private String fileDir;

    public FileUtils() {
        this.fileDir = String.valueOf(this.mc.mcDataDir.getAbsolutePath()) + "/" + Client.CLIENT_NAME;
        File fileFolder = new File(this.fileDir);
        if (!fileFolder.exists()) {
            fileFolder.mkdirs();
        }
        try {
            this.loadKeys();
            this.loadValues();
            this.loadMods();
            this.loadFriends();
        }
        catch (IOException var3) {
            var3.printStackTrace();
        }
    }

    public void loadFriends() throws IOException {
        final File file = new File(this.fileDir + "/friends.txt");
        if (!file.exists()) {
            file.createNewFile();
        }
        else {
            String line;
            while ((line = new BufferedReader(new FileReader(file)).readLine()) != null) {
                FriendsManager.getFriends().add(new Friend(line));
            }
        }
    }
    
    
    public void saveKeys() {
        File f = new File(String.valueOf(this.fileDir) + "/keys.txt");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            PrintWriter e = new PrintWriter(f);
            for (Mod m : ModManager.getModList()) {
                String keyName = m.getKey() < 0 ? "None" : Keyboard.getKeyName((int)m.getKey());
                e.write(String.valueOf(m.getName()) + ":" + keyName + "\n");
            }
            e.close();
        }
        catch (Exception var6) {
            var6.printStackTrace();
        }
    }

    public void loadKeys() throws IOException {
        File f = new File(String.valueOf(this.fileDir) + "/keys.txt");
        if (!f.exists()) {
            f.createNewFile();
        } else {
            String line;
            BufferedReader br = new BufferedReader(new FileReader(f));
            while ((line = br.readLine()) != null) {
                if (!line.contains(":")) continue;
                String[] split = line.split(":");
                Mod m = ModManager.getModByName(split[0]);
                int key = Keyboard.getKeyIndex((String)split[1]);
                if (m == null || key == -1) continue;
                m.setKey(key);
            }
        }
    }

    public void saveMods() {
        File f = new File(String.valueOf(this.fileDir) + "/mods.txt");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            PrintWriter e = new PrintWriter(f);
            for (Mod m : ModManager.getModList()) {
                e.print(String.valueOf(m.getName()) + ":" + m.isEnabled() + "\n");
            }
            e.close();
        }
        catch (Exception var5) {
            var5.printStackTrace();
        }
    }

    public void loadMods() throws IOException {
        File f = new File(String.valueOf(this.fileDir) + "/mods.txt");
        if (!f.exists()) {
            f.createNewFile();
        } else {
            String line;
            BufferedReader br = new BufferedReader(new FileReader(f));
            while ((line = br.readLine()) != null) {
                if (!line.contains(":")) continue;
                String[] split = line.split(":");
                Mod m = ModManager.getModByName(split[0]);
                boolean state = Boolean.parseBoolean(split[1]);
                if (m == null) continue;
                try {
                    m.set(state, false);
                }
                catch (Exception var8) {
                    var8.printStackTrace();
                }
            }
        }
    }

    public void saveValues() {
        File f = new File(String.valueOf(this.fileDir) + "/values.txt");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            PrintWriter e = new PrintWriter(f);
            for (Value value : Value.list) {
                String valueName = value.getValueName();
                if (value.isValueBoolean) {
                    e.print(String.valueOf(valueName) + ":b:" + value.getValueState() + "\n");
                    continue;
                }
                if (value.isValueDouble) {
                    e.print(String.valueOf(valueName) + ":d:" + value.getValueState() + "\n");
                    continue;
                }
                if (!value.isValueMode) continue;
                e.print(String.valueOf(valueName) + ":s:" + value.getModeTitle() + ":" + value.getCurrentMode() + "\n");
            }
            e.close();
        }
        catch (Exception var6) {
            var6.printStackTrace();
        }
    }

    public void loadValues() throws IOException {
        File f = new File(this.fileDir + "/values.txt");
        if (!f.exists()) {
           f.createNewFile();
        } else {
           BufferedReader br = new BufferedReader(new FileReader(f));
           label51:
           while(true) {
              String line;
              do {
                 if ((line = br.readLine()) == null) {
                    return;
                 }
              } while(!line.contains(":"));

              String[] split = line.split(":");
              Iterator var6 = Value.list.iterator();

              while(true) {
                 while(true) {
                    Value value;
                    do {
                       if (!var6.hasNext()) {
                          continue label51;
                       }

                       value = (Value)var6.next();
                    } while(!split[0].equalsIgnoreCase(value.getValueName()));

                    if (value.isValueBoolean && split[1].equalsIgnoreCase("b")) {
                       value.setValueState(Boolean.parseBoolean(split[2]));
                    } else if (value.isValueDouble && split[1].equalsIgnoreCase("d")) {
                       value.setValueState(Double.parseDouble(split[2]));
                    } else if (value.isValueMode && split[1].equalsIgnoreCase("s") && split[2].equalsIgnoreCase(value.getModeTitle())) {
                       value.setCurrentMode(Integer.parseInt(split[3]));
                    }
                 }
              }
           }
        }
     }

    public void saveFriends() {
        final File file = new File(this.fileDir + "/friend.txt");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            final PrintWriter printWriter = new PrintWriter(file);
            final Iterator<Friend> iterator = FriendsManager.getFriends().iterator();
            while (iterator.hasNext()) {
                printWriter.print(iterator.next().getName() + "\n");
            }
            printWriter.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}

