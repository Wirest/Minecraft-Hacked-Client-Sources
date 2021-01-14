package com.etb.client.command.impl;

import java.util.Random;

import com.etb.client.Client;
import com.etb.client.command.Command;
import com.etb.client.utils.Printer;

public class NoStrike extends Command {
    private Random random = new Random();
    public NoStrike() {
        super("NoStrike",new String[]{"NS","nostrike","nstrike"},"Blacklist words");
    }

    @Override
    public void onRun(final String[] args) {
        if (args.length == 1) {
            Printer.print("Wrong Usage!");
        }
        switch (args[1].toLowerCase()) {
            case "add":
            case "a":
                if (args.length > 2) {
                    Client.INSTANCE.getAntiStrike().add(args[2], args[3]);
                    Client.INSTANCE.getAntiStrike().saveFile();
                    Printer.print("Added " + args[2]);
                }
                break;
            case "list":
                if (!Client.INSTANCE.getAntiStrike().getList().isEmpty()) {
                    Printer.print("Current Strings:");
                    Client.INSTANCE.getAntiStrike().getList().values().forEach(shid -> {
                        Printer.print(shid);
                    });
                } else {
                    Printer.print("You have no saved antistrike strings!");
                }
                break;
            case "remove":
            case "del":
            case "d":
            case "r":
                if (args.length > 1) {
                    Client.INSTANCE.getAntiStrike().remove(args[2]);
                    Client.INSTANCE.getAntiStrike().saveFile();
                    Printer.print("Removed " + args[2]);
                }
                break;
            case "friend":
            case "f":
            case "friends":
                if (Client.INSTANCE.getFriendManager().getFriends().isEmpty()) {
                    Printer.print("You have no friends. LOL WHAT ARE YOU SCREAM HAHAAA!!1");
                    return;
                }
                Client.INSTANCE.getFriendManager().getFriends().forEach(friends -> {
                    String finished = "";
                    String[] letters = friends.getName().split("");
                    for (String letter : letters) {
                        finished += random.nextInt(9) + letter;
                    }
                    if (Client.INSTANCE.getAntiStrike().getList().get(friends.getName()) == null) {
                        Client.INSTANCE.getAntiStrike().getList().put(friends.getName(), friends.getAlias() != null ? friends.getAlias():finished);
                        Client.INSTANCE.getAntiStrike().saveFile();
                        Printer.print("Name protected " + friends.getName() + " under the alias " + (friends.getAlias() != null ? friends.getAlias():finished) + ".");
                        return;
                    }
                    Printer.print(friends.getName() + " is already added to the antistrike.");
                });
                break;
            case "clear":
            case "c":
                Client.INSTANCE.getAntiStrike().getList().clear();
                Client.INSTANCE.getAntiStrike().saveFile();
                Printer.print("Cleared");
                break;
        }
    }
}
