package me.rigamortis.faurax.commands;

import me.cupboard.command.*;
import me.rigamortis.faurax.*;
import java.net.*;
import java.io.*;
import me.cupboard.command.argument.*;

public class CommandCapeCheck extends Command
{
    public CommandCapeCheck() {
        super("CheckCape", new String[] { "cc" });
        Client.getCMDS().register(this);
    }
    
    @Argument
    protected String checkCape(final String username) {
        if (username != null) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        final URL someURL = new URL("http://s.optifine.net/capes/" + username + ".png");
                        final BufferedReader input = new BufferedReader(new InputStreamReader(someURL.openStream()));
                        final String inputString = input.readLine();
                        if (inputString.equalsIgnoreCase("Not found")) {
                            System.out.println("No Texture");
                        }
                        else {
                            System.out.println("Fagg0t");
                        }
                        input.close();
                    }
                    catch (Exception ex) {}
                }
            }.start();
        }
        return "";
    }
}
