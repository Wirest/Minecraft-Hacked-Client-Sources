/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.irc.pircBot;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import me.razerboy420.weepcraft.irc.pircBot.DccManager;
import me.razerboy420.weepcraft.irc.pircBot.PircBot;

public class DccFileTransfer {
    public static final int BUFFER_SIZE = 1024;
    private PircBot _bot;
    private DccManager _manager;
    private String _nick;
    private String _login = null;
    private String _hostname = null;
    private String _type;
    private long _address;
    private int _port;
    private long _size;
    private boolean _received;
    private Socket _socket = null;
    private long _progress = 0;
    private File _file = null;
    private int _timeout = 0;
    private boolean _incoming;
    private long _packetDelay = 0;
    private long _startTime = 0;

    DccFileTransfer(PircBot bot, DccManager manager, String nick, String login, String hostname, String type, String filename, long address, int port, long size) {
        this._bot = bot;
        this._manager = manager;
        this._nick = nick;
        this._login = login;
        this._hostname = hostname;
        this._type = type;
        this._file = new File(filename);
        this._address = address;
        this._port = port;
        this._size = size;
        this._received = false;
        this._incoming = true;
    }

    DccFileTransfer(PircBot bot, DccManager manager, File file, String nick, int timeout) {
        this._bot = bot;
        this._manager = manager;
        this._nick = nick;
        this._file = file;
        this._size = file.length();
        this._timeout = timeout;
        this._received = true;
        this._incoming = false;
    }

    public synchronized void receive(File file, boolean resume) {
        if (!this._received) {
            this._received = true;
            this._file = file;
            if (this._type.equals("SEND") && resume) {
                this._progress = file.length();
                if (this._progress == 0) {
                    this.doReceive(file, false);
                } else {
                    this._bot.sendCTCPCommand(this._nick, "DCC RESUME file.ext " + this._port + " " + this._progress);
                    this._manager.addAwaitingResume(this);
                }
            } else {
                this._progress = file.length();
                this.doReceive(file, resume);
            }
        }
    }

    void doReceive(final File file, final boolean resume) {
        new Thread(){

            @Override
            public void run() {
                Exception exception;
                block14 : {
                    FilterOutputStream foutput;
                    foutput = null;
                    exception = null;
                    try {
                        try {
                            int[] ip = DccFileTransfer.this._bot.longToIp(DccFileTransfer.this._address);
                            String ipStr = String.valueOf(String.valueOf(ip[0])) + "." + ip[1] + "." + ip[2] + "." + ip[3];
                            DccFileTransfer.access$4(DccFileTransfer.this, new Socket(ipStr, DccFileTransfer.this._port));
                            DccFileTransfer.this._socket.setSoTimeout(30000);
                            DccFileTransfer.access$5(DccFileTransfer.this, System.currentTimeMillis());
                            DccFileTransfer.this._manager.removeAwaitingResume(DccFileTransfer.this);
                            BufferedInputStream input = new BufferedInputStream(DccFileTransfer.this._socket.getInputStream());
                            BufferedOutputStream output = new BufferedOutputStream(DccFileTransfer.this._socket.getOutputStream());
                            foutput = new BufferedOutputStream(new FileOutputStream(file.getCanonicalPath(), resume));
                            byte[] inBuffer = new byte[1024];
                            byte[] outBuffer = new byte[4];
                            int bytesRead = 0;
                            while ((bytesRead = input.read(inBuffer, 0, inBuffer.length)) != -1) {
                                foutput.write(inBuffer, 0, bytesRead);
                                DccFileTransfer dccFileTransfer = DccFileTransfer.this;
                                DccFileTransfer.access$8(dccFileTransfer, dccFileTransfer._progress + (long)bytesRead);
                                outBuffer[0] = (byte)(DccFileTransfer.this._progress >> 24 & 255);
                                outBuffer[1] = (byte)(DccFileTransfer.this._progress >> 16 & 255);
                                outBuffer[2] = (byte)(DccFileTransfer.this._progress >> 8 & 255);
                                outBuffer[3] = (byte)(DccFileTransfer.this._progress >> 0 & 255);
                                output.write(outBuffer);
                                output.flush();
                                DccFileTransfer.this.delay();
                            }
                            foutput.flush();
                        }
                        catch (Exception e) {
                            exception = e;
                            try {
                                foutput.close();
                                DccFileTransfer.this._socket.close();
                            }
                            catch (Exception ipStr) {}
                            break block14;
                        }
                    }
                    catch (Throwable var10_14) {
                        try {
                            foutput.close();
                            DccFileTransfer.this._socket.close();
                        }
                        catch (Exception ipStr) {
                            // empty catch block
                        }
                        try {
                            throw var10_14;
                        }
                        catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        foutput.close();
                        DccFileTransfer.this._socket.close();
                    }
                    catch (Exception var10_14) {
                        // empty catch block
                    }
                }
                DccFileTransfer.this._bot.onFileTransferFinished(DccFileTransfer.this, exception);
            }
        }.start();
    }

    void doSend(final boolean allowResume) {
        new Thread(){

            @Override
            public void run() {
                Exception exception;
                block25 : {
                    BufferedInputStream finput;
                    finput = null;
                    exception = null;
                    try {
                        try {
                            ServerSocket ss = null;
                            int[] ports = DccFileTransfer.this._bot.getDccPorts();
                            if (ports == null) {
                                ss = new ServerSocket(0);
                            } else {
                                int i = 0;
                                while (i < ports.length) {
                                    try {
                                        ss = new ServerSocket(ports[i]);
                                        break;
                                    }
                                    catch (Exception var6_8) {
                                        ++i;
                                    }
                                }
                                if (ss == null) {
                                    throw new IOException("All ports returned by getDccPorts() are in use.");
                                }
                            }
                            ss.setSoTimeout(DccFileTransfer.this._timeout);
                            DccFileTransfer.access$11(DccFileTransfer.this, ss.getLocalPort());
                            InetAddress inetAddress = DccFileTransfer.this._bot.getDccInetAddress();
                            if (inetAddress == null) {
                                inetAddress = DccFileTransfer.this._bot.getInetAddress();
                            }
                            byte[] ip = inetAddress.getAddress();
                            long ipNum = DccFileTransfer.this._bot.ipToLong(ip);
                            String safeFilename = DccFileTransfer.this._file.getName().replace(' ', '_');
                            safeFilename = safeFilename.replace('\t', '_');
                            if (allowResume) {
                                DccFileTransfer.this._manager.addAwaitingResume(DccFileTransfer.this);
                            }
                            DccFileTransfer.this._bot.sendCTCPCommand(DccFileTransfer.this._nick, "DCC SEND " + safeFilename + " " + ipNum + " " + DccFileTransfer.this._port + " " + DccFileTransfer.this._file.length());
                            DccFileTransfer.access$4(DccFileTransfer.this, ss.accept());
                            DccFileTransfer.this._socket.setSoTimeout(30000);
                            DccFileTransfer.access$5(DccFileTransfer.this, System.currentTimeMillis());
                            if (allowResume) {
                                DccFileTransfer.this._manager.removeAwaitingResume(DccFileTransfer.this);
                            }
                            ss.close();
                            BufferedOutputStream output = new BufferedOutputStream(DccFileTransfer.this._socket.getOutputStream());
                            BufferedInputStream input = new BufferedInputStream(DccFileTransfer.this._socket.getInputStream());
                            finput = new BufferedInputStream(new FileInputStream(DccFileTransfer.this._file));
                            if (DccFileTransfer.this._progress > 0) {
                                long bytesSkipped = 0;
                                while (bytesSkipped < DccFileTransfer.this._progress) {
                                    bytesSkipped += finput.skip(DccFileTransfer.this._progress - bytesSkipped);
                                }
                            }
                            byte[] outBuffer = new byte[1024];
                            byte[] inBuffer = new byte[4];
                            int bytesRead = 0;
                            while ((bytesRead = finput.read(outBuffer, 0, outBuffer.length)) != -1) {
                                output.write(outBuffer, 0, bytesRead);
                                output.flush();
                                input.read(inBuffer, 0, inBuffer.length);
                                DccFileTransfer dccFileTransfer = DccFileTransfer.this;
                                DccFileTransfer.access$8(dccFileTransfer, dccFileTransfer._progress + (long)bytesRead);
                                DccFileTransfer.this.delay();
                            }
                        }
                        catch (Exception e) {
                            exception = e;
                            try {
                                finput.close();
                                DccFileTransfer.this._socket.close();
                            }
                            catch (Exception ports) {}
                            break block25;
                        }
                    }
                    catch (Throwable var15_21) {
                        try {
                            finput.close();
                            DccFileTransfer.this._socket.close();
                        }
                        catch (Exception ports) {
                            // empty catch block
                        }
                        try {
                            throw var15_21;
                        }
                        catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        finput.close();
                        DccFileTransfer.this._socket.close();
                    }
                    catch (Exception var15_21) {
                        // empty catch block
                    }
                }
                DccFileTransfer.this._bot.onFileTransferFinished(DccFileTransfer.this, exception);
            }
        }.start();
    }

    void setProgress(long progress) {
        this._progress = progress;
    }

    private void delay() {
        if (this._packetDelay > 0) {
            try {
                Thread.sleep(this._packetDelay);
            }
            catch (InterruptedException interruptedException) {
                // empty catch block
            }
        }
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

    public File getFile() {
        return this._file;
    }

    public int getPort() {
        return this._port;
    }

    public boolean isIncoming() {
        return this._incoming;
    }

    public boolean isOutgoing() {
        return !this.isIncoming();
    }

    public void setPacketDelay(long millis) {
        this._packetDelay = millis;
    }

    public long getPacketDelay() {
        return this._packetDelay;
    }

    public long getSize() {
        return this._size;
    }

    public long getProgress() {
        return this._progress;
    }

    public double getProgressPercentage() {
        return 100.0 * ((double)this.getProgress() / (double)this.getSize());
    }

    public void close() {
        try {
            this._socket.close();
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public long getTransferRate() {
        long time = (System.currentTimeMillis() - this._startTime) / 1000;
        if (time <= 0) {
            return 0;
        }
        return this.getProgress() / time;
    }

    public long getNumericalAddress() {
        return this._address;
    }

    static void access$4(DccFileTransfer dccFileTransfer, Socket socket) {
        dccFileTransfer._socket = socket;
    }

    static void access$5(DccFileTransfer dccFileTransfer, long l) {
        dccFileTransfer._startTime = l;
    }

    static void access$8(DccFileTransfer dccFileTransfer, long l) {
        dccFileTransfer._progress = l;
    }

    static void access$11(DccFileTransfer dccFileTransfer, int n) {
        dccFileTransfer._port = n;
    }

}

