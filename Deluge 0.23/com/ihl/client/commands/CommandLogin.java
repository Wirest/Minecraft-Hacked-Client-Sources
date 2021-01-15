package com.ihl.client.commands;

import com.ihl.client.Auth;
import com.ihl.client.commands.exceptions.CommandException;
import com.ihl.client.commands.exceptions.SyntaxException;
import com.ihl.client.util.ChatUtil;

import java.util.List;

public class CommandLogin extends Command {

    public CommandLogin(String base, List<String> usages) {
        super(base, usages);
    }

    public void execute(final String[] args) throws CommandException {
        if (args.length > 0 && args.length < 3) {
            ChatUtil.send(String.format("Authenticating: [v]%s", args[0]));
            new Thread() {
                public void run() {
                    response(Auth.setSessionData(args[0], args.length == 2 ? args[1] : ""), args[0]);
                }
            }.start();
        } else {
            throw new SyntaxException();
        }
    }

    private void response(int code, String user) {
        String message = "";
        switch (code) {
            case 0:
                message = "[e]Authentication failed: [v]%s";
                break;
            case 1:
                message = "Authentication successful: [v]%s";
                break;
            case 2:
                message = "Username changed: [v]%s";
                break;
        }
        ChatUtil.send(String.format(message, user));
    }
}
