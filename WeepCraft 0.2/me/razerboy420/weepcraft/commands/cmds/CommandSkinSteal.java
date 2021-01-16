/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.commands.cmds;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import me.razerboy420.weepcraft.Weepcraft;
import me.razerboy420.weepcraft.commands.Command;
import me.razerboy420.weepcraft.util.FileUtils;
import me.razerboy420.weepcraft.util.Wrapper;

public class CommandSkinSteal
extends Command {
    public CommandSkinSteal() {
        super(new String[]{"skinsteal", "steal"}, "Steal player skins", ".steal <Name>");
    }

    @Override
    public boolean runCommand(String command, String[] args) {
        try {
            int i;
            String name = args[1];
            URL url = new URL("https://crafatar.com/skins/" + name + ".png");
            URLConnection connection = url.openConnection();
            BufferedInputStream input = new BufferedInputStream(connection.getInputStream());
            File dir = new File(Weepcraft.weepcraftDir + File.separator + "Skins");
            if (!dir.exists()) {
                dir.mkdir();
            }
            File file = new File(dir, String.valueOf(String.valueOf(name)) + ".png");
            FileOutputStream output = new FileOutputStream(file);
            while ((i = input.read()) != -1) {
                output.write(i);
            }
            output.close();
            input.close();
            Wrapper.tellPlayer(String.valueOf(String.valueOf(name)) + "'s skin saved in the skins folder.");
            FileUtils.openFile(dir);
            return true;
        }
        catch (UnknownHostException e) {
            Wrapper.tellPlayer("Unable to download skin.");
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            Wrapper.tellPlayer("Unable to download skin.");
            return true;
        }
    }
}

