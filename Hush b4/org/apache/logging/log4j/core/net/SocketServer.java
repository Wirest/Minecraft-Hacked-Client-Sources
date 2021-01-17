// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.net;

import org.apache.logging.log4j.core.config.XMLConfiguration;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.File;
import org.apache.logging.log4j.core.config.Configuration;
import java.net.URI;
import org.apache.logging.log4j.core.config.XMLConfigurationFactory;
import java.io.OptionalDataException;
import java.io.EOFException;
import java.io.ObjectInputStream;
import org.apache.logging.log4j.core.LogEvent;
import java.util.Iterator;
import java.net.Socket;
import java.util.Map;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.net.ServerSocket;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.AbstractServer;

public class SocketServer extends AbstractServer implements Runnable
{
    private final Logger logger;
    private static final int MAX_PORT = 65534;
    private volatile boolean isActive;
    private final ServerSocket server;
    private final ConcurrentMap<Long, SocketHandler> handlers;
    
    public SocketServer(final int port) throws IOException {
        this.isActive = true;
        this.handlers = new ConcurrentHashMap<Long, SocketHandler>();
        this.server = new ServerSocket(port);
        this.logger = LogManager.getLogger(this.getClass().getName() + '.' + port);
    }
    
    public static void main(final String[] args) throws Exception {
        if (args.length < 1 || args.length > 2) {
            System.err.println("Incorrect number of arguments");
            printUsage();
            return;
        }
        final int port = Integer.parseInt(args[0]);
        if (port <= 0 || port >= 65534) {
            System.err.println("Invalid port number");
            printUsage();
            return;
        }
        if (args.length == 2 && args[1].length() > 0) {
            ConfigurationFactory.setConfigurationFactory(new ServerConfigurationFactory(args[1]));
        }
        final SocketServer sserver = new SocketServer(port);
        final Thread server = new Thread(sserver);
        server.start();
        final Charset enc = Charset.defaultCharset();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, enc));
        String line;
        do {
            line = reader.readLine();
        } while (line != null && !line.equalsIgnoreCase("Quit") && !line.equalsIgnoreCase("Stop") && !line.equalsIgnoreCase("Exit"));
        sserver.shutdown();
        server.join();
    }
    
    private static void printUsage() {
        System.out.println("Usage: ServerSocket port configFilePath");
    }
    
    public void shutdown() {
        this.isActive = false;
        Thread.currentThread().interrupt();
    }
    
    @Override
    public void run() {
        while (this.isActive) {
            try {
                final Socket clientSocket = this.server.accept();
                clientSocket.setSoLinger(true, 0);
                final SocketHandler handler = new SocketHandler(clientSocket);
                this.handlers.put(handler.getId(), handler);
                handler.start();
            }
            catch (IOException ioe) {
                System.out.println("Exception encountered on accept. Ignoring. Stack Trace :");
                ioe.printStackTrace();
            }
        }
        for (final Map.Entry<Long, SocketHandler> entry : this.handlers.entrySet()) {
            final SocketHandler handler2 = entry.getValue();
            handler2.shutdown();
            try {
                handler2.join();
            }
            catch (InterruptedException ex) {}
        }
    }
    
    private class SocketHandler extends Thread
    {
        private final ObjectInputStream ois;
        private boolean shutdown;
        
        public SocketHandler(final Socket socket) throws IOException {
            this.shutdown = false;
            this.ois = new ObjectInputStream(socket.getInputStream());
        }
        
        public void shutdown() {
            this.shutdown = true;
            this.interrupt();
        }
        
        @Override
        public void run() {
            boolean closed = false;
            try {
                try {
                    while (!this.shutdown) {
                        final LogEvent event = (LogEvent)this.ois.readObject();
                        if (event != null) {
                            AbstractServer.this.log(event);
                        }
                    }
                }
                catch (EOFException eof) {
                    closed = true;
                }
                catch (OptionalDataException opt) {
                    SocketServer.this.logger.error("OptionalDataException eof=" + opt.eof + " length=" + opt.length, opt);
                }
                catch (ClassNotFoundException cnfe) {
                    SocketServer.this.logger.error("Unable to locate LogEvent class", cnfe);
                }
                catch (IOException ioe) {
                    SocketServer.this.logger.error("IOException encountered while reading from socket", ioe);
                }
                if (!closed) {
                    try {
                        this.ois.close();
                    }
                    catch (Exception ex) {}
                }
            }
            finally {
                SocketServer.this.handlers.remove(this.getId());
            }
        }
    }
    
    private static class ServerConfigurationFactory extends XMLConfigurationFactory
    {
        private final String path;
        
        public ServerConfigurationFactory(final String path) {
            this.path = path;
        }
        
        @Override
        public Configuration getConfiguration(final String name, final URI configLocation) {
            if (this.path != null && this.path.length() > 0) {
                File file = null;
                ConfigurationSource source = null;
                try {
                    file = new File(this.path);
                    final FileInputStream is = new FileInputStream(file);
                    source = new ConfigurationSource(is, file);
                }
                catch (FileNotFoundException ex) {}
                if (source == null) {
                    try {
                        final URL url = new URL(this.path);
                        source = new ConfigurationSource(url.openStream(), this.path);
                    }
                    catch (MalformedURLException mue) {}
                    catch (IOException ex2) {}
                }
                try {
                    if (source != null) {
                        return new XMLConfiguration(source);
                    }
                }
                catch (Exception ex3) {}
                System.err.println("Unable to process configuration at " + this.path + ", using default.");
            }
            return super.getConfiguration(name, configLocation);
        }
    }
}
