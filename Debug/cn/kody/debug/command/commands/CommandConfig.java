package cn.kody.debug.command.commands;

import java.io.PrintWriter;

import cn.kody.debug.Client;
import cn.kody.debug.command.Command;
import cn.kody.debug.ui.Notification;
import cn.kody.debug.utils.HttpUtils;
import cn.kody.debug.utils.Type;

import java.io.IOException;
import net.minecraft.client.Minecraft;

public class CommandConfig extends Command
{
    private String fileDir;
    
    public CommandConfig(final String[] array) {
        super(array);
        this.fileDir = String.valueOf(Minecraft.getMinecraft().mcDataDir.getAbsolutePath()) + "/" + Client.CLIENT_FILEMANAGER_FOLDERNAME;
        this.setArgs(".config <server>");
    }
    
    @Override
    public void onCmd(final String[] array) {
        if (array.length == 2) {
            Label_0208: {
                if (!array[1].toLowerCase().equals("hypixel")) {
                    if (!array[1].toLowerCase().contains("hypixelcn")) {
                        break Label_0208;
                    }
                }
                try {
                    writetxtfile(sendGet("https://gitee.com/K_-_ody/Debug-Config/raw/master/config/" + array[1].toLowerCase() + "/values.txt"), String.valueOf(this.fileDir) + "/values.txt");
                    writetxtfile(sendGet("https://gitee.com/K_-_ody/Debug-Config/raw/master/config/" + array[1].toLowerCase() + "/mods.txt"), String.valueOf(this.fileDir) + "/mods.txt");
                    Client.instance.fileMgr.loadMods();
                    Client.instance.fileMgr.loadValues();
                    return;
                }
                catch (IOException ex) {
                    Notification.tellPlayer("Write Config Failed!", Type.ERROR);
                    ex.printStackTrace();
                    return;
                }
            }
            Notification.tellPlayer("We do not have this config!", Type.WARN);
        }
        else {
            Notification.tellPlayer(this.getArgs(), Type.INFO);
        }
    }
    
    public static boolean writetxtfile(final String s, final String s2) {
        boolean b = false;
        try {
            final PrintWriter printWriter = new PrintWriter(s2);
            printWriter.write(s);
            printWriter.flush();
            printWriter.close();
            b = true;
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return b;
    }
    
    public static String sendGet(final String p_hookHttpClient_0_) {
        return HttpUtils.hookHttpClient(p_hookHttpClient_0_);
    }
}
