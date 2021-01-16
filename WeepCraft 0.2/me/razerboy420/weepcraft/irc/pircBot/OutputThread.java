/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.irc.pircBot;

import java.io.BufferedWriter;
import me.razerboy420.weepcraft.irc.pircBot.PircBot;
import me.razerboy420.weepcraft.irc.pircBot.Queue;

public class OutputThread
extends Thread {
    private PircBot _bot = null;
    private Queue _outQueue = null;

    OutputThread(PircBot bot, Queue outQueue) {
        this._bot = bot;
        this._outQueue = outQueue;
        this.setName(this.getClass() + "-Thread");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    static void sendRawLine(PircBot bot, BufferedWriter bwriter, String line) {
        BufferedWriter bufferedWriter;
        if (line.length() > bot.getMaxLineLength() - 2) {
            line = line.substring(0, bot.getMaxLineLength() - 2);
        }
        BufferedWriter bufferedWriter2 = bufferedWriter = bwriter;
        synchronized (bufferedWriter2) {
            try {
                bwriter.write(String.valueOf(String.valueOf(line)) + "\r\n");
                bwriter.flush();
                bot.log(">>>" + line);
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }

    @Override
    public void run() {
        try {
            boolean running = true;
            while (running) {
                Thread.sleep(this._bot.getMessageDelay());
                String line = (String)this._outQueue.next();
                if (line != null) {
                    this._bot.sendRawLine(line);
                    continue;
                }
                running = false;
            }
        }
        catch (InterruptedException running) {
            // empty catch block
        }
    }
}

