package nivia.commands.commands;

import nivia.commands.Command;
import nivia.utils.Logger;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class getName extends Command {
    public getName() {
        super("getName", "Copies your username to clipboard. xd", null, false);
    }
    @Override
    public void execute(String commandName, String[] arguments) {
        final Clipboard clipboard = Toolkit.getDefaultToolkit()
                .getSystemClipboard();
        StringSelection name = new StringSelection(mc.getSession().getUsername());
        clipboard.setContents(name, null);
        Logger.logChat("Copied your username to clipboard.");
    }

}
