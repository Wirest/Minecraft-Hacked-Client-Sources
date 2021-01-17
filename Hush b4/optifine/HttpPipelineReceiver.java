// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import java.io.EOFException;
import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.LinkedHashMap;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class HttpPipelineReceiver extends Thread
{
    private HttpPipelineConnection httpPipelineConnection;
    private static final Charset ASCII;
    private static final String HEADER_CONTENT_LENGTH = "Content-Length";
    private static final char CR = '\r';
    private static final char LF = '\n';
    
    static {
        ASCII = Charset.forName("ASCII");
    }
    
    public HttpPipelineReceiver(final HttpPipelineConnection p_i57_1_) {
        super("HttpPipelineReceiver");
        this.httpPipelineConnection = null;
        this.httpPipelineConnection = p_i57_1_;
    }
    
    @Override
    public void run() {
        while (!Thread.interrupted()) {
            HttpPipelineRequest httppipelinerequest = null;
            try {
                httppipelinerequest = this.httpPipelineConnection.getNextRequestReceive();
                final InputStream inputstream = this.httpPipelineConnection.getInputStream();
                final HttpResponse httpresponse = this.readResponse(inputstream);
                this.httpPipelineConnection.onResponseReceived(httppipelinerequest, httpresponse);
            }
            catch (InterruptedException var4) {}
            catch (Exception exception) {
                this.httpPipelineConnection.onExceptionReceive(httppipelinerequest, exception);
            }
        }
    }
    
    private HttpResponse readResponse(final InputStream p_readResponse_1_) throws IOException {
        final String s = this.readLine(p_readResponse_1_);
        final String[] astring = Config.tokenize(s, " ");
        if (astring.length < 3) {
            throw new IOException("Invalid status line: " + s);
        }
        final String s2 = astring[0];
        final int i = Config.parseInt(astring[1], 0);
        final String s3 = astring[2];
        final Map<String, String> map = new LinkedHashMap<String, String>();
        while (true) {
            final String s4 = this.readLine(p_readResponse_1_);
            if (s4.length() <= 0) {
                break;
            }
            final int j = s4.indexOf(":");
            if (j <= 0) {
                continue;
            }
            final String s5 = s4.substring(0, j).trim();
            final String s6 = s4.substring(j + 1).trim();
            map.put(s5, s6);
        }
        byte[] abyte = null;
        final String s7 = map.get("Content-Length");
        if (s7 != null) {
            final int k = Config.parseInt(s7, -1);
            if (k > 0) {
                abyte = new byte[k];
                this.readFull(abyte, p_readResponse_1_);
            }
        }
        else {
            final String s8 = map.get("Transfer-Encoding");
            if (Config.equals(s8, "chunked")) {
                abyte = this.readContentChunked(p_readResponse_1_);
            }
        }
        return new HttpResponse(i, s, map, abyte);
    }
    
    private byte[] readContentChunked(final InputStream p_readContentChunked_1_) throws IOException {
        final ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
        int i;
        do {
            final String s = this.readLine(p_readContentChunked_1_);
            final String[] astring = Config.tokenize(s, "; ");
            i = Integer.parseInt(astring[0], 16);
            final byte[] abyte = new byte[i];
            this.readFull(abyte, p_readContentChunked_1_);
            bytearrayoutputstream.write(abyte);
            this.readLine(p_readContentChunked_1_);
        } while (i != 0);
        return bytearrayoutputstream.toByteArray();
    }
    
    private void readFull(final byte[] p_readFull_1_, final InputStream p_readFull_2_) throws IOException {
        int j;
        for (int i = 0; i < p_readFull_1_.length; i += j) {
            j = p_readFull_2_.read(p_readFull_1_, i, p_readFull_1_.length - i);
            if (j < 0) {
                throw new EOFException();
            }
        }
    }
    
    private String readLine(final InputStream p_readLine_1_) throws IOException {
        final ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
        int i = -1;
        boolean flag = false;
        while (true) {
            final int j = p_readLine_1_.read();
            if (j < 0) {
                break;
            }
            bytearrayoutputstream.write(j);
            if (i == 13 && j == 10) {
                flag = true;
                break;
            }
            i = j;
        }
        final byte[] abyte = bytearrayoutputstream.toByteArray();
        String s = new String(abyte, HttpPipelineReceiver.ASCII);
        if (flag) {
            s = s.substring(0, s.length() - 2);
        }
        return s;
    }
}
