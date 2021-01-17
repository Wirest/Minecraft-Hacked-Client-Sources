package me.slowly.client.util.command.cmds;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import me.slowly.client.util.command.Command;
import me.slowly.client.mod.mods.misc.Spammer;
import me.slowly.client.util.ClientUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C01PacketChatMessage;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;

public class CommandSpammer extends Command {
	
	private static String fileDir = String.valueOf(Minecraft.getMinecraft().mcDataDir.getAbsolutePath()) + "/" + "Slowly";

    public CommandSpammer(String[] commands) {
        super(commands);
        this.setArgs(".spammer <Text>");
    }

    @Override
    public void onCmd(String[] args) {
        if (args.length != 2) {
        	ClientUtil.sendClientMessage(this.getArgs(), ClientNotification.Type.WARNING);
            return;
        }
        Spammer.message = args[1];
        CommandSpammer.saveMessage();
        ClientUtil.sendClientMessage("Changed message " + args[1], ClientNotification.Type.SUCCESS);
        super.onCmd(args);
    }

    public static void saveMessage() {
        File f = new File(String.valueOf(fileDir) + "/spammer.txt");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            PrintWriter pw = new PrintWriter(f);
            pw.print(Spammer.message);
            pw.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadMessage() throws IOException {
        File f = new File(String.valueOf(fileDir) + "/spammer.txt");
        if (!f.exists()) {
            f.createNewFile();
        } else {
            String line;
            BufferedReader br = new BufferedReader(new FileReader(f));
            while ((line = br.readLine()) != null) {
                try {
                    String message;
                    Spammer.message = message = String.valueOf(line);
                }
                catch (Exception message) {
                }
            }
        }
    }

}
