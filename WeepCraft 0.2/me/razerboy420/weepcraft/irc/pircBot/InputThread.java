/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.irc.pircBot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InterruptedIOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.StringTokenizer;
import me.razerboy420.weepcraft.irc.pircBot.OutputThread;
import me.razerboy420.weepcraft.irc.pircBot.PircBot;

public class InputThread
extends Thread {
    private PircBot _bot = null;
    private Socket _socket = null;
    private BufferedReader _breader = null;
    private BufferedWriter _bwriter = null;
    private boolean _isConnected = true;
    private boolean _disposed = false;
    public static final int MAX_LINE_LENGTH = 512;

    InputThread(PircBot bot, Socket socket, BufferedReader breader, BufferedWriter bwriter) {
        this._bot = bot;
        this._socket = socket;
        this._breader = breader;
        this._bwriter = bwriter;
        this.setName(this.getClass() + "-Thread");
    }

    void sendRawLine(String line) {
        OutputThread.sendRawLine(this._bot, this._bwriter, line);
    }

    boolean isConnected() {
        return this._isConnected;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void run() {
        try {
            boolean running = true;
            while (running) {
                try {
                    String line = null;
                    while ((line = this._breader.readLine()) != null) {
                        try {
                            this._bot.handleLine(line);
                        }
                        catch (Throwable t) {
                            PircBot pircBot;
                            StringWriter sw = new StringWriter();
                            PrintWriter pw = new PrintWriter(sw);
                            t.printStackTrace(pw);
                            pw.flush();
                            StringTokenizer tokenizer = new StringTokenizer(sw.toString(), "\r\n");
                            PircBot pircBot2 = pircBot = this._bot;
                            synchronized (pircBot2) {
                                this._bot.log("### Your implementation of PircBot is faulty and you have");
                                this._bot.log("### allowed an uncaught Exception or Error to propagate in your");
                                this._bot.log("### code. It may be possible for PircBot to continue operating");
                                this._bot.log("### normally. Here is the stack trace that was produced: -");
                                this._bot.log("### ");
                                while (tokenizer.hasMoreTokens()) {
                                    this._bot.log("### " + tokenizer.nextToken());
                                }
                            }
                        }
                    }
                    if (line != null) continue;
                    running = false;
                }
                catch (InterruptedIOException iioe) {
                    this.sendRawLine("PING " + System.currentTimeMillis() / 1000);
                }
            }
        }
        catch (Exception running) {
            // empty catch block
        }
        try {
            this._socket.close();
        }
        catch (Exception running) {
            // empty catch block
        }
        if (!this._disposed) {
            this._bot.log("*** Disconnected.");
            this._isConnected = false;
            this._bot.onDisconnect();
        }
    }

    public void dispose() {
        try {
            this._disposed = true;
            this._socket.close();
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}

