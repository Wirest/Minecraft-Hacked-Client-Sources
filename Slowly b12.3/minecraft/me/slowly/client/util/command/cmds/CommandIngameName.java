/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.slowly.client.util.command.cmds;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.command.Command;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

public class CommandIngameName extends Command {
    public CommandIngameName(String[] commands) {
        super(commands);
        this.setArgs("Copies IngameName to Clipboard");
    }

    @Override
    public void onCmd(String[] args) {
        StringSelection stringSelection = new StringSelection(Minecraft.getMinecraft().session.getUsername());
        Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
        clpbrd.setContents(stringSelection, null);
        ClientUtil.sendClientMessage("Saved to Clipboard!", ClientNotification.Type.INFO);
        super.onCmd(args);
    }
}

