// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.net;

import java.io.ByteArrayOutputStream;
import java.net.UnknownHostException;
import java.net.ConnectException;
import java.util.concurrent.CountDownLatch;
import org.apache.logging.log4j.Logger;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.helpers.Strings;
import org.apache.logging.log4j.core.appender.OutputStreamManager;
import java.io.Serializable;
import org.apache.logging.log4j.core.Layout;
import java.net.InetAddress;
import java.io.OutputStream;
import java.net.Socket;

public class TCPSocketManager extends AbstractSocketManager
{
    public static final int DEFAULT_RECONNECTION_DELAY = 30000;
    private static final int DEFAULT_PORT = 4560;
    private static final TCPSocketManagerFactory FACTORY;
    private final int reconnectionDelay;
    private Reconnector connector;
    private Socket socket;
    private final boolean retry;
    private final boolean immediateFail;
    
    public TCPSocketManager(final String name, final OutputStream os, final Socket sock, final InetAddress addr, final String host, final int port, final int delay, final boolean immediateFail, final Layout<? extends Serializable> layout) {
        super(name, os, addr, host, port, layout);
        this.connector = null;
        this.reconnectionDelay = delay;
        this.socket = sock;
        this.immediateFail = immediateFail;
        this.retry = (delay > 0);
        if (sock == null) {
            (this.connector = new Reconnector(this)).setDaemon(true);
            this.connector.setPriority(1);
            this.connector.start();
        }
    }
    
    public static TCPSocketManager getSocketManager(final String host, int port, int delay, final boolean immediateFail, final Layout<? extends Serializable> layout) {
        if (Strings.isEmpty(host)) {
            throw new IllegalArgumentException("A host name is required");
        }
        if (port <= 0) {
            port = 4560;
        }
        if (delay == 0) {
            delay = 30000;
        }
        return (TCPSocketManager)OutputStreamManager.getManager("TCP:" + host + ":" + port, new FactoryData(host, port, delay, immediateFail, layout), TCPSocketManager.FACTORY);
    }
    
    @Override
    protected void write(final byte[] bytes, final int offset, final int length) {
        if (this.socket == null) {
            if (this.connector != null && !this.immediateFail) {
                this.connector.latch();
            }
            if (this.socket == null) {
                final String msg = "Error writing to " + this.getName() + " socket not available";
                throw new AppenderLoggingException(msg);
            }
        }
        synchronized (this) {
            try {
                this.getOutputStream().write(bytes, offset, length);
            }
            catch (IOException ex) {
                if (this.retry && this.connector == null) {
                    (this.connector = new Reconnector(this)).setDaemon(true);
                    this.connector.setPriority(1);
                    this.connector.start();
                }
                final String msg2 = "Error writing to " + this.getName();
                throw new AppenderLoggingException(msg2, ex);
            }
        }
    }
    
    @Override
    protected synchronized void close() {
        super.close();
        if (this.connector != null) {
            this.connector.shutdown();
            this.connector.interrupt();
            this.connector = null;
        }
    }
    
    @Override
    public Map<String, String> getContentFormat() {
        final Map<String, String> result = new HashMap<String, String>(super.getContentFormat());
        result.put("protocol", "tcp");
        result.put("direction", "out");
        return result;
    }
    
    protected Socket createSocket(final InetAddress host, final int port) throws IOException {
        return this.createSocket(host.getHostName(), port);
    }
    
    protected Socket createSocket(final String host, final int port) throws IOException {
        return new Socket(host, port);
    }
    
    static {
        FACTORY = new TCPSocketManagerFactory();
    }
    
    private class Reconnector extends Thread
    {
        private final CountDownLatch latch;
        private boolean shutdown;
        private final Object owner;
        
        public Reconnector(final OutputStreamManager owner) {
            this.latch = new CountDownLatch(1);
            this.shutdown = false;
            this.owner = owner;
        }
        
        public void latch() {
            try {
                this.latch.await();
            }
            catch (InterruptedException ex) {}
        }
        
        public void shutdown() {
            this.shutdown = true;
        }
        
        @Override
        public void run() {
            while (!this.shutdown) {
                try {
                    Thread.sleep(TCPSocketManager.this.reconnectionDelay);
                    final Socket sock = TCPSocketManager.this.createSocket(TCPSocketManager.this.address, TCPSocketManager.this.port);
                    final OutputStream newOS = sock.getOutputStream();
                    synchronized (this.owner) {
                        try {
                            OutputStreamManager.this.getOutputStream().close();
                        }
                        catch (IOException ex2) {}
                        OutputStreamManager.this.setOutputStream(newOS);
                        TCPSocketManager.this.socket = sock;
                        TCPSocketManager.this.connector = null;
                        this.shutdown = true;
                    }
                    TCPSocketManager.LOGGER.debug("Connection to " + TCPSocketManager.this.host + ":" + TCPSocketManager.this.port + " reestablished.");
                }
                catch (InterruptedException ie) {
                    TCPSocketManager.LOGGER.debug("Reconnection interrupted.");
                }
                catch (ConnectException ex) {
                    TCPSocketManager.LOGGER.debug(TCPSocketManager.this.host + ":" + TCPSocketManager.this.port + " refused connection");
                }
                catch (IOException ioe) {
                    TCPSocketManager.LOGGER.debug("Unable to reconnect to " + TCPSocketManager.this.host + ":" + TCPSocketManager.this.port);
                }
                finally {
                    this.latch.countDown();
                }
            }
        }
    }
    
    private static class FactoryData
    {
        private final String host;
        private final int port;
        private final int delay;
        private final boolean immediateFail;
        private final Layout<? extends Serializable> layout;
        
        public FactoryData(final String host, final int port, final int delay, final boolean immediateFail, final Layout<? extends Serializable> layout) {
            this.host = host;
            this.port = port;
            this.delay = delay;
            this.immediateFail = immediateFail;
            this.layout = layout;
        }
    }
    
    protected static class TCPSocketManagerFactory implements ManagerFactory<TCPSocketManager, FactoryData>
    {
        @Override
        public TCPSocketManager createManager(final String name, final FactoryData data) {
            InetAddress address;
            try {
                address = InetAddress.getByName(data.host);
            }
            catch (UnknownHostException ex) {
                TCPSocketManager.LOGGER.error("Could not find address of " + data.host, ex);
                return null;
            }
            try {
                final Socket socket = new Socket(data.host, data.port);
                final OutputStream os = socket.getOutputStream();
                return new TCPSocketManager(name, os, socket, address, data.host, data.port, data.delay, data.immediateFail, data.layout);
            }
            catch (IOException ex2) {
                TCPSocketManager.LOGGER.error("TCPSocketManager (" + name + ") " + ex2);
                final OutputStream os = new ByteArrayOutputStream();
                if (data.delay == 0) {
                    return null;
                }
                return new TCPSocketManager(name, os, null, address, data.host, data.port, data.delay, data.immediateFail, data.layout);
            }
        }
    }
}
