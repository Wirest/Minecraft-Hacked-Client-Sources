package optifine;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;

public class HttpPipelineSender
        extends Thread {
    private static final String CRLF = "\r\n";
    private static Charset ASCII = Charset.forName("ASCII");
    private HttpPipelineConnection httpPipelineConnection = null;

    public HttpPipelineSender(HttpPipelineConnection paramHttpPipelineConnection) {
        super("HttpPipelineSender");
        this.httpPipelineConnection = paramHttpPipelineConnection;
    }

    public void run() {
        HttpPipelineRequest localHttpPipelineRequest = null;
        try {
            connect();
            while (!Thread.interrupted()) {
                localHttpPipelineRequest = this.httpPipelineConnection.getNextRequestSend();
                HttpRequest localHttpRequest = localHttpPipelineRequest.getHttpRequest();
                OutputStream localOutputStream = this.httpPipelineConnection.getOutputStream();
                writeRequest(localHttpRequest, localOutputStream);
                this.httpPipelineConnection.onRequestSent(localHttpPipelineRequest);
            }
        } catch (InterruptedException localInterruptedException) {
        } catch (Exception localException) {
            this.httpPipelineConnection.onExceptionSend(localHttpPipelineRequest, localException);
        }
    }

    private void connect()
            throws IOException {
        String str = this.httpPipelineConnection.getHost();
        int i = this.httpPipelineConnection.getPort();
        Proxy localProxy = this.httpPipelineConnection.getProxy();
        Socket localSocket = new Socket(localProxy);
        localSocket.connect(new InetSocketAddress(str, i), 5000);
        this.httpPipelineConnection.setSocket(localSocket);
    }

    private void writeRequest(HttpRequest paramHttpRequest, OutputStream paramOutputStream)
            throws IOException {
        write(paramOutputStream, paramHttpRequest.getMethod() + " " + paramHttpRequest.getFile() + " " + paramHttpRequest.getHttp() + "\r\n");
        Map localMap = paramHttpRequest.getHeaders();
        Iterator localIterator = localMap.keySet().iterator();
        while (localIterator.hasNext()) {
            String str1 = (String) localIterator.next();
            String str2 = (String) paramHttpRequest.getHeaders().get(str1);
            write(paramOutputStream, str1 + ": " + str2 + "\r\n");
        }
        write(paramOutputStream, "\r\n");
    }

    private void write(OutputStream paramOutputStream, String paramString)
            throws IOException {
        byte[] arrayOfByte = paramString.getBytes(ASCII);
        paramOutputStream.write(arrayOfByte);
    }
}




