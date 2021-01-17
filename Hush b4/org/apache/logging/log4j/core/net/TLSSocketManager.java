// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.net;

import javax.net.ssl.SSLSocket;
import java.net.UnknownHostException;
import java.io.ByteArrayOutputStream;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import javax.net.ssl.SSLSocketFactory;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.appender.OutputStreamManager;
import org.apache.logging.log4j.core.helpers.Strings;
import java.io.Serializable;
import org.apache.logging.log4j.core.Layout;
import java.net.InetAddress;
import java.net.Socket;
import java.io.OutputStream;
import org.apache.logging.log4j.core.net.ssl.SSLConfiguration;

public class TLSSocketManager extends TCPSocketManager
{
    public static final int DEFAULT_PORT = 6514;
    private static final TLSSocketManagerFactory FACTORY;
    private SSLConfiguration sslConfig;
    
    public TLSSocketManager(final String name, final OutputStream os, final Socket sock, final SSLConfiguration sslConfig, final InetAddress addr, final String host, final int port, final int delay, final boolean immediateFail, final Layout layout) {
        super(name, os, sock, addr, host, port, delay, immediateFail, layout);
        this.sslConfig = sslConfig;
    }
    
    public static TLSSocketManager getSocketManager(final SSLConfiguration sslConfig, final String host, int port, int delay, final boolean immediateFail, final Layout layout) {
        if (Strings.isEmpty(host)) {
            throw new IllegalArgumentException("A host name is required");
        }
        if (port <= 0) {
            port = 6514;
        }
        if (delay == 0) {
            delay = 30000;
        }
        return (TLSSocketManager)OutputStreamManager.getManager("TLS:" + host + ":" + port, new TLSFactoryData(sslConfig, host, port, delay, immediateFail, layout), TLSSocketManager.FACTORY);
    }
    
    @Override
    protected Socket createSocket(final String host, final int port) throws IOException {
        final SSLSocketFactory socketFactory = createSSLSocketFactory(this.sslConfig);
        return socketFactory.createSocket(host, port);
    }
    
    private static SSLSocketFactory createSSLSocketFactory(final SSLConfiguration sslConf) {
        SSLSocketFactory socketFactory;
        if (sslConf != null) {
            socketFactory = sslConf.getSSLSocketFactory();
        }
        else {
            socketFactory = (SSLSocketFactory)SSLSocketFactory.getDefault();
        }
        return socketFactory;
    }
    
    static {
        FACTORY = new TLSSocketManagerFactory();
    }
    
    private static class TLSFactoryData
    {
        protected SSLConfiguration sslConfig;
        private final String host;
        private final int port;
        private final int delay;
        private final boolean immediateFail;
        private final Layout layout;
        
        public TLSFactoryData(final SSLConfiguration sslConfig, final String host, final int port, final int delay, final boolean immediateFail, final Layout layout) {
            this.host = host;
            this.port = port;
            this.delay = delay;
            this.immediateFail = immediateFail;
            this.layout = layout;
            this.sslConfig = sslConfig;
        }
    }
    
    private static class TLSSocketManagerFactory implements ManagerFactory<TLSSocketManager, TLSFactoryData>
    {
        @Override
        public TLSSocketManager createManager(final String name, final TLSFactoryData data) {
            InetAddress address = null;
            OutputStream os = null;
            Socket socket = null;
            try {
                address = this.resolveAddress(data.host);
                socket = this.createSocket(data);
                os = socket.getOutputStream();
                this.checkDelay(data.delay, os);
            }
            catch (IOException e) {
                TLSSocketManager.LOGGER.error("TLSSocketManager (" + name + ") " + e);
                os = new ByteArrayOutputStream();
            }
            catch (TLSSocketManagerFactoryException e2) {
                return null;
            }
            return this.createManager(name, os, socket, data.sslConfig, address, data.host, data.port, data.delay, data.immediateFail, data.layout);
        }
        
        private InetAddress resolveAddress(final String hostName) throws TLSSocketManagerFactoryException {
            InetAddress address;
            try {
                address = InetAddress.getByName(hostName);
            }
            catch (UnknownHostException ex) {
                TLSSocketManager.LOGGER.error("Could not find address of " + hostName, ex);
                throw new TLSSocketManagerFactoryException();
            }
            return address;
        }
        
        private void checkDelay(final int delay, final OutputStream os) throws TLSSocketManagerFactoryException {
            if (delay == 0 && os == null) {
                throw new TLSSocketManagerFactoryException();
            }
        }
        
        private Socket createSocket(final TLSFactoryData data) throws IOException {
            final SSLSocketFactory socketFactory = createSSLSocketFactory(data.sslConfig);
            final SSLSocket socket = (SSLSocket)socketFactory.createSocket(data.host, data.port);
            return socket;
        }
        
        private TLSSocketManager createManager(final String name, final OutputStream os, final Socket socket, final SSLConfiguration sslConfig, final InetAddress address, final String host, final int port, final int delay, final boolean immediateFail, final Layout layout) {
            return new TLSSocketManager(name, os, socket, sslConfig, address, host, port, delay, immediateFail, layout);
        }
        
        private class TLSSocketManagerFactoryException extends Exception
        {
        }
    }
}
