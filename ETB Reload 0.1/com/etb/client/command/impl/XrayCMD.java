package com.etb.client.command.impl;

import com.etb.client.Client;
import com.etb.client.command.Command;
import com.etb.client.gui.notification.Notification;
import com.etb.client.module.modules.render.Xray;
import com.etb.client.utils.MathUtils;
import com.etb.client.utils.Printer;

import java.util.Arrays;

public class XrayCMD extends Command {

	public XrayCMD() {
		super("Ores",new String[]{"ores","xr"},"Add / remove ores from xray");
	}

	@Override
	public void onRun(final String[] args) {
		Xray xray = (Xray)Client.INSTANCE.getModuleManager().getModule("xray");
		if(args.length == 2) {
			if(MathUtils.parsable(args[1], MathUtils.NumberType.DOUBLE)) {
				int id = Integer.parseInt(args[1]);
				if (args[0].equalsIgnoreCase("add")) {
					xray.blocks.add(id);
					Printer.print("Added Block ID " + id);
				} else if(args[0].equalsIgnoreCase("remove")) {
					xray.blocks.remove(id);
					Printer.print("Removed Block ID " + id);
				} else {
					Printer.print("Invalid syntax");
				}
			} else {
				Printer.print("Invalid block ID");
			}
		} else if(args.length == 1) {
			if (args[0].equalsIgnoreCase("list")) {
				Arrays.toString(xray.blocks.toArray());
			}
		}
	}
}
