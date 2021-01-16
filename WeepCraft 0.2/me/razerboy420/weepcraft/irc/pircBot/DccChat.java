/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.irc.pircBot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;
import me.razerboy420.weepcraft.irc.pircBot.PircBot;

public class DccChat {
    private PircBot _bot;
    private String _nick;
    private String _login = null;
    private String _hostname = null;
    private BufferedReader _reader;
    private BufferedWriter _writer;
    private Socket _socket;
    private boolean _acceptable;
    private long _address = 0;
    private int _port = 0;

    DccChat(PircBot bot, String nick, String login, String hostname, long address, int port) {
        this._bot = bot;
        this._address = address;
        this._port = port;
        this._nick = nick;
        this._login = login;
        this._hostname = hostname;
        this._acceptable = true;
    }

    DccChat(PircBot bot, String nick, Socket socket) throws IOException {
        this._bot = bot;
        this._nick = nick;
        this._socket = socket;
        this._reader = new BufferedReader(new InputStreamReader(this._socket.getInputStream()));
        this._writer = new BufferedWriter(new OutputStreamWriter(this._socket.getOutputStream()));
        this._acceptable = false;
    }

    public synchronized void accept() throws IOException {
        if (this._acceptable) {
            this._acceptable = false;
            int[] ip = this._bot.longToIp(this._address);
            String ipStr = String.valueOf(String.valueOf(ip[0])) + "." + ip[1] + "." + ip[2] + "." + ip[3];
            this._socket = new Socket(ipStr, this._port);
            this._reader = new BufferedReader(new InputStreamReader(this._socket.getInputStream()));
            this._writer = new BufferedWriter(new OutputStreamWriter(this._socket.getOutputStream()));
        }
    }

    public String readLine() throws IOException {
        if (this._acceptable) {
            throw new IOException("You must call the accept() method of the DccChat request before you can use it.");
        }
        return this._reader.readLine();
    }

    public void sendLine(String line) throws IOException {
        if (this._acceptable) {
            throw new IOException("You must call the accept() method of the DccChat request before you can use it.");
        }
        this._writer.write(String.valueOf(String.valueOf(line)) + "\r\n");
        this._writer.flush();
    }

    public void close() throws IOException {
        if (this._acceptable) {
            throw new IOException("You must call the accept() method of the DccChat request before you can use it.");
        }
        this._socket.close();
    }

    public String getNick() {
        return this._nick;
    }

    public String getLogin() {
        return this._login;
    }

    public String getHostname() {
        return this._hostname;
    }

    public BufferedReader getBufferedReader() {
        return this._reader;
    }

    public BufferedWriter getBufferedWriter() {
        return this._writer;
    }

    public Socket getSocket() {
        return this._socket;
    }

    public long getNumericalAddress() {
        return this._address;
    }
}

