package nivia.commands.commands;

import nivia.commands.Command;
import nivia.utils.Logger;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class getIP extends Command {
    public getIP() {
        super("getIP", "Copies the server's non-number ip to clipboard.", null, false);
    }
    @Override
    public void execute(String commandName, String[] arguments) {
        if(mc.isSingleplayer()){
            Logger.logChat("You are on singleplayer fucking retard it has no ip want to get fuckingk rekt or what the fuck u tryna crash pandora or what?");
            return;
        }
        final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection ip = new StringSelection(mc.getCurrentServerData().serverIP);
        clipboard.setContents(ip, null);
        Logger.logChat("Copied server's IP to clipboard.");
    }
}
