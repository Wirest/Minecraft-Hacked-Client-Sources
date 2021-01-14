package com.etb.client.command.impl;

import com.etb.client.Client;
import com.etb.client.command.Command;
import com.etb.client.module.modules.player.BanWave;
import com.etb.client.utils.Printer;

public class BanWaveCMD extends Command {

    public BanWaveCMD() {
        super("BanWave", new String[]{"bw", "banwave"}, "Edit the ban command");
    }

    @Override
    public void onRun(final String[] s) {
        BanWave bw = (BanWave) Client.INSTANCE.getModuleManager().getModule("banwave");
        if (s.length > 1) {
            if (s[1].equals("cmd")) {
                bw.setCmd(s[2]);
                Printer.print("Set banwave command to " + s[2]);
            }
        } else {
            Printer.print("BW ");
        }
    }
}
