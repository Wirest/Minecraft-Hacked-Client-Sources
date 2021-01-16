/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.irc.pircBot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import me.razerboy420.weepcraft.irc.pircBot.PircBot;

public class IdentServer
extends Thread {
    private PircBot _bot;
    private String _login;
    private ServerSocket _ss = null;

    IdentServer(PircBot bot, String login) {
        this._bot = bot;
        this._login = login;
        try {
            this._ss = new ServerSocket(113);
            this._ss.setSoTimeout(60000);
        }
        catch (Exception e) {
            this._bot.log("*** Could not start the ident server on port 113.");
            return;
        }
        this._bot.log("*** Ident server running on port 113 for the next 60 seconds...");
        this.setName(this.getClass() + "-Thread");
        this.start();
    }

    @Override
    public void run() {
        try {
            Socket socket = this._ss.accept();
            socket.setSoTimeout(60000);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            String line = reader.readLine();
            if (line != null) {
                this._bot.log("*** Ident request received: " + line);
                line = String.valueOf(String.valueOf(line)) + " : USERID : UNIX : " + this._login;
                writer.write(String.valueOf(String.valueOf(line)) + "\r\n");
                writer.flush();
                this._bot.log("*** Ident reply sent: " + line);
                writer.close();
            }
        }
        catch (Exception socket) {
            // empty catch block
        }
        try {
            this._ss.close();
        }
        catch (Exception socket) {
            // empty catch block
        }
        this._bot.log("*** The Ident server has been shut down.");
    }
}

