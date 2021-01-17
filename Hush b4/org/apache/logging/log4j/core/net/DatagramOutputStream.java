// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.net;

import org.apache.logging.log4j.status.StatusLogger;
import java.net.DatagramPacket;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import java.net.InetAddress;
import java.net.DatagramSocket;
import org.apache.logging.log4j.Logger;
import java.io.OutputStream;

public class DatagramOutputStream extends OutputStream
{
    protected static final Logger LOGGER;
    private static final int SHIFT_1 = 8;
    private static final int SHIFT_2 = 16;
    private static final int SHIFT_3 = 24;
    private DatagramSocket ds;
    private final InetAddress address;
    private final int port;
    private byte[] data;
    private final byte[] header;
    private final byte[] footer;
    
    public DatagramOutputStream(final String host, final int port, final byte[] header, final byte[] footer) {
        this.port = port;
        this.header = header;
        this.footer = footer;
        try {
            this.address = InetAddress.getByName(host);
        }
        catch (UnknownHostException ex) {
            final String msg = "Could not find host " + host;
            DatagramOutputStream.LOGGER.error(msg, ex);
            throw new AppenderLoggingException(msg, ex);
        }
        try {
            this.ds = new DatagramSocket();
        }
        catch (SocketException ex2) {
            final String msg = "Could not instantiate DatagramSocket to " + host;
            DatagramOutputStream.LOGGER.error(msg, ex2);
            throw new AppenderLoggingException(msg, ex2);
        }
    }
    
    @Override
    public synchronized void write(final byte[] bytes, final int offset, final int length) throws IOException {
        this.copy(bytes, offset, length);
    }
    
    @Override
    public synchronized void write(final int i) throws IOException {
        this.copy(new byte[] { (byte)(i >>> 24), (byte)(i >>> 16), (byte)(i >>> 8), (byte)i }, 0, 4);
    }
    
    @Override
    public synchronized void write(final byte[] bytes) throws IOException {
        this.copy(bytes, 0, bytes.length);
    }
    
    @Override
    public synchronized void flush() throws IOException {
        try {
            if (this.data != null && this.ds != null && this.address != null) {
                if (this.footer != null) {
                    this.copy(this.footer, 0, this.footer.length);
                }
                final DatagramPacket packet = new DatagramPacket(this.data, this.data.length, this.address, this.port);
                this.ds.send(packet);
            }
        }
        finally {
            this.data = null;
            if (this.header != null) {
                this.copy(this.header, 0, this.header.length);
            }
        }
    }
    
    @Override
    public synchronized void close() throws IOException {
        if (this.ds != null) {
            if (this.data != null) {
                this.flush();
            }
            this.ds.close();
            this.ds = null;
        }
    }
    
    private void copy(final byte[] bytes, final int offset, final int length) {
        final int index = (this.data == null) ? 0 : this.data.length;
        final byte[] copy = new byte[length + index];
        if (this.data != null) {
            System.arraycopy(this.data, 0, copy, 0, this.data.length);
        }
        System.arraycopy(bytes, offset, copy, index, length);
        this.data = copy;
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
