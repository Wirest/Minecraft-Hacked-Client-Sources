// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.net;

import org.apache.logging.log4j.core.config.XMLConfiguration;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.File;
import org.apache.logging.log4j.core.config.Configuration;
import java.net.URI;
import org.apache.logging.log4j.core.config.XMLConfigurationFactory;
import java.io.EOFException;
import java.io.OptionalDataException;
import org.apache.logging.log4j.core.LogEvent;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ByteArrayInputStream;
import java.net.DatagramPacket;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import java.net.DatagramSocket;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.AbstractServer;

public class UDPSocketServer extends AbstractServer implements Runnable
{
    private final Logger logger;
    private static final int MAX_PORT = 65534;
    private volatile boolean isActive;
    private final DatagramSocket server;
    private final int maxBufferSize = 67584;
    
    public UDPSocketServer(final int port) throws IOException {
        this.isActive = true;
        this.server = new DatagramSocket(port);
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
        final UDPSocketServer sserver = new UDPSocketServer(port);
        final Thread server = new Thread(sserver);
        server.start();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
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
                final byte[] buf = new byte[67584];
                final DatagramPacket packet = new DatagramPacket(buf, buf.length);
                this.server.receive(packet);
                final ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(packet.getData(), packet.getOffset(), packet.getLength()));
                final LogEvent event = (LogEvent)ois.readObject();
                if (event == null) {
                    continue;
                }
                this.log(event);
            }
            catch (OptionalDataException opt) {
                this.logger.error("OptionalDataException eof=" + opt.eof + " length=" + opt.length, opt);
            }
            catch (ClassNotFoundException cnfe) {
                this.logger.error("Unable to locate LogEvent class", cnfe);
            }
            catch (EOFException eofe) {
                this.logger.info("EOF encountered");
            }
            catch (IOException ioe) {
                this.logger.error("Exception encountered on accept. Ignoring. Stack Trace :", ioe);
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
