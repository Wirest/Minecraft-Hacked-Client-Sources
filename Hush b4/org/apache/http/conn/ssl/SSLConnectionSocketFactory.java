// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.conn.ssl;

import java.net.SocketAddress;
import java.net.InetSocketAddress;
import org.apache.http.HttpHost;
import javax.net.SocketFactory;
import java.net.Socket;
import org.apache.http.protocol.HttpContext;
import java.io.IOException;
import javax.net.ssl.SSLSocket;
import org.apache.http.util.Args;
import javax.net.ssl.SSLContext;
import org.apache.http.util.TextUtils;
import javax.net.ssl.SSLSocketFactory;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;

@ThreadSafe
public class SSLConnectionSocketFactory implements LayeredConnectionSocketFactory
{
    public static final String TLS = "TLS";
    public static final String SSL = "SSL";
    public static final String SSLV2 = "SSLv2";
    public static final X509HostnameVerifier ALLOW_ALL_HOSTNAME_VERIFIER;
    public static final X509HostnameVerifier BROWSER_COMPATIBLE_HOSTNAME_VERIFIER;
    public static final X509HostnameVerifier STRICT_HOSTNAME_VERIFIER;
    private final SSLSocketFactory socketfactory;
    private final X509HostnameVerifier hostnameVerifier;
    private final String[] supportedProtocols;
    private final String[] supportedCipherSuites;
    
    public static SSLConnectionSocketFactory getSocketFactory() throws SSLInitializationException {
        return new SSLConnectionSocketFactory(SSLContexts.createDefault(), SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
    }
    
    private static String[] split(final String s) {
        if (TextUtils.isBlank(s)) {
            return null;
        }
        return s.split(" *, *");
    }
    
    public static SSLConnectionSocketFactory getSystemSocketFactory() throws SSLInitializationException {
        return new SSLConnectionSocketFactory((SSLSocketFactory)SSLSocketFactory.getDefault(), split(System.getProperty("https.protocols")), split(System.getProperty("https.cipherSuites")), SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
    }
    
    public SSLConnectionSocketFactory(final SSLContext sslContext) {
        this(sslContext, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
    }
    
    public SSLConnectionSocketFactory(final SSLContext sslContext, final X509HostnameVerifier hostnameVerifier) {
        this(Args.notNull(sslContext, "SSL context").getSocketFactory(), null, null, hostnameVerifier);
    }
    
    public SSLConnectionSocketFactory(final SSLContext sslContext, final String[] supportedProtocols, final String[] supportedCipherSuites, final X509HostnameVerifier hostnameVerifier) {
        this(Args.notNull(sslContext, "SSL context").getSocketFactory(), supportedProtocols, supportedCipherSuites, hostnameVerifier);
    }
    
    public SSLConnectionSocketFactory(final SSLSocketFactory socketfactory, final X509HostnameVerifier hostnameVerifier) {
        this(socketfactory, null, null, hostnameVerifier);
    }
    
    public SSLConnectionSocketFactory(final SSLSocketFactory socketfactory, final String[] supportedProtocols, final String[] supportedCipherSuites, final X509HostnameVerifier hostnameVerifier) {
        this.socketfactory = Args.notNull(socketfactory, "SSL socket factory");
        this.supportedProtocols = supportedProtocols;
        this.supportedCipherSuites = supportedCipherSuites;
        this.hostnameVerifier = ((hostnameVerifier != null) ? hostnameVerifier : SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
    }
    
    protected void prepareSocket(final SSLSocket socket) throws IOException {
    }
    
    public Socket createSocket(final HttpContext context) throws IOException {
        return SocketFactory.getDefault().createSocket();
    }
    
    public Socket connectSocket(final int connectTimeout, final Socket socket, final HttpHost host, final InetSocketAddress remoteAddress, final InetSocketAddress localAddress, final HttpContext context) throws IOException {
        Args.notNull(host, "HTTP host");
        Args.notNull(remoteAddress, "Remote address");
        final Socket sock = (socket != null) ? socket : this.createSocket(context);
        if (localAddress != null) {
            sock.bind(localAddress);
        }
        try {
            sock.connect(remoteAddress, connectTimeout);
        }
        catch (IOException ex) {
            try {
                sock.close();
            }
            catch (IOException ex2) {}
            throw ex;
        }
        if (sock instanceof SSLSocket) {
            final SSLSocket sslsock = (SSLSocket)sock;
            sslsock.startHandshake();
            this.verifyHostname(sslsock, host.getHostName());
            return sock;
        }
        return this.createLayeredSocket(sock, host.getHostName(), remoteAddress.getPort(), context);
    }
    
    public Socket createLayeredSocket(final Socket socket, final String target, final int port, final HttpContext context) throws IOException {
        final SSLSocket sslsock = (SSLSocket)this.socketfactory.createSocket(socket, target, port, true);
        if (this.supportedProtocols != null) {
            sslsock.setEnabledProtocols(this.supportedProtocols);
        }
        if (this.supportedCipherSuites != null) {
            sslsock.setEnabledCipherSuites(this.supportedCipherSuites);
        }
        this.prepareSocket(sslsock);
        sslsock.startHandshake();
        this.verifyHostname(sslsock, target);
        return sslsock;
    }
    
    X509HostnameVerifier getHostnameVerifier() {
        return this.hostnameVerifier;
    }
    
    private void verifyHostname(final SSLSocket sslsock, final String hostname) throws IOException {
        try {
            this.hostnameVerifier.verify(hostname, sslsock);
        }
        catch (IOException iox) {
            try {
                sslsock.close();
            }
            catch (Exception ex) {}
            throw iox;
        }
    }
    
    static {
        ALLOW_ALL_HOSTNAME_VERIFIER = new AllowAllHostnameVerifier();
        BROWSER_COMPATIBLE_HOSTNAME_VERIFIER = new BrowserCompatHostnameVerifier();
        STRICT_HOSTNAME_VERIFIER = new StrictHostnameVerifier();
    }
}
