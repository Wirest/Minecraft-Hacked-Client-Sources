// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import java.util.Iterator;
import java.util.Map;
import java.io.IOException;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class HttpPipelineSender extends Thread
{
    private HttpPipelineConnection httpPipelineConnection;
    private static final String CRLF = "\r\n";
    private static Charset ASCII;
    
    static {
        HttpPipelineSender.ASCII = Charset.forName("ASCII");
    }
    
    public HttpPipelineSender(final HttpPipelineConnection p_i59_1_) {
        super("HttpPipelineSender");
        this.httpPipelineConnection = null;
        this.httpPipelineConnection = p_i59_1_;
    }
    
    @Override
    public void run() {
        HttpPipelineRequest httppipelinerequest = null;
        try {
            this.connect();
            while (!Thread.interrupted()) {
                httppipelinerequest = this.httpPipelineConnection.getNextRequestSend();
                final HttpRequest httprequest = httppipelinerequest.getHttpRequest();
                final OutputStream outputstream = this.httpPipelineConnection.getOutputStream();
                this.writeRequest(httprequest, outputstream);
                this.httpPipelineConnection.onRequestSent(httppipelinerequest);
            }
        }
        catch (InterruptedException var4) {}
        catch (Exception exception) {
            this.httpPipelineConnection.onExceptionSend(httppipelinerequest, exception);
        }
    }
    
    private void connect() throws IOException {
        final String s = this.httpPipelineConnection.getHost();
        final int i = this.httpPipelineConnection.getPort();
        final Proxy proxy = this.httpPipelineConnection.getProxy();
        final Socket socket = new Socket(proxy);
        socket.connect(new InetSocketAddress(s, i), 5000);
        this.httpPipelineConnection.setSocket(socket);
    }
    
    private void writeRequest(final HttpRequest p_writeRequest_1_, final OutputStream p_writeRequest_2_) throws IOException {
        this.write(p_writeRequest_2_, String.valueOf(p_writeRequest_1_.getMethod()) + " " + p_writeRequest_1_.getFile() + " " + p_writeRequest_1_.getHttp() + "\r\n");
        final Map<String, String> map = p_writeRequest_1_.getHeaders();
        for (final String s : map.keySet()) {
            final String s2 = p_writeRequest_1_.getHeaders().get(s);
            this.write(p_writeRequest_2_, String.valueOf(s) + ": " + s2 + "\r\n");
        }
        this.write(p_writeRequest_2_, "\r\n");
    }
    
    private void write(final OutputStream p_write_1_, final String p_write_2_) throws IOException {
        final byte[] abyte = p_write_2_.getBytes(HttpPipelineSender.ASCII);
        p_write_1_.write(abyte);
    }
}
