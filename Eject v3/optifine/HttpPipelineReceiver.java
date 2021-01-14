package optifine;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;

public class HttpPipelineReceiver
        extends Thread {
    private static final Charset ASCII = Charset.forName("ASCII");
    private static final String HEADER_CONTENT_LENGTH = "Content-Length";
    private static final char CR = '\r';
    private static final char LF = '\n';
    private HttpPipelineConnection httpPipelineConnection = null;

    public HttpPipelineReceiver(HttpPipelineConnection paramHttpPipelineConnection) {
        super("HttpPipelineReceiver");
        this.httpPipelineConnection = paramHttpPipelineConnection;
    }

    public void run() {
        while (!Thread.interrupted()) {
            HttpPipelineRequest localHttpPipelineRequest = null;
            try {
                localHttpPipelineRequest = this.httpPipelineConnection.getNextRequestReceive();
                InputStream localInputStream = this.httpPipelineConnection.getInputStream();
                HttpResponse localHttpResponse = readResponse(localInputStream);
                this.httpPipelineConnection.onResponseReceived(localHttpPipelineRequest, localHttpResponse);
            } catch (InterruptedException localInterruptedException) {
                return;
            } catch (Exception localException) {
                this.httpPipelineConnection.onExceptionReceive(localHttpPipelineRequest, localException);
            }
        }
    }

    private HttpResponse readResponse(InputStream paramInputStream)
            throws IOException {
        String str1 = readLine(paramInputStream);
        String[] arrayOfString = Config.tokenize(str1, " ");
        if (arrayOfString.length < 3) {
            throw new IOException("Invalid status line: " + str1);
        }
        String str2 = arrayOfString[0];
        int i = Config.parseInt(arrayOfString[1], 0);
        String str3 = arrayOfString[2];
        LinkedHashMap localLinkedHashMap = new LinkedHashMap();
        for (; ; ) {
            String str4 = readLine(paramInputStream);
            String str5;
            String str6;
            if (str4.length() <= 0) {
                byte[] arrayOfByte = null;
                str5 = (String) localLinkedHashMap.get("Content-Length");
                if (str5 != null) {
                    int k = Config.parseInt(str5, -1);
                    if (k > 0) {
                        arrayOfByte = new byte[k];
                        readFull(arrayOfByte, paramInputStream);
                    }
                } else {
                    str6 = (String) localLinkedHashMap.get("Transfer-Encoding");
                    if (Config.equals(str6, "chunked")) {
                        arrayOfByte = readContentChunked(paramInputStream);
                    }
                }
                return new HttpResponse(i, str1, localLinkedHashMap, arrayOfByte);
            }
            int j = str4.indexOf(":");
            if (j > 0) {
                str5 = str4.substring(0, j).trim();
                str6 = str4.substring(j | 0x1).trim();
                localLinkedHashMap.put(str5, str6);
            }
        }
    }

    private byte[] readContentChunked(InputStream paramInputStream)
            throws IOException {
        ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
        for (; ; ) {
            String str = readLine(paramInputStream);
            String[] arrayOfString = Config.tokenize(str, "; ");
            int i = Integer.parseInt(arrayOfString[0], 16);
            byte[] arrayOfByte = new byte[i];
            readFull(arrayOfByte, paramInputStream);
            localByteArrayOutputStream.write(arrayOfByte);
            readLine(paramInputStream);
            if (i == 0) {
                break;
            }
        }
        return localByteArrayOutputStream.toByteArray();
    }

    private void readFull(byte[] paramArrayOfByte, InputStream paramInputStream)
            throws IOException {
        int j = 0;
        while (j < paramArrayOfByte.length) {
            int i = paramInputStream.read(paramArrayOfByte, j, paramArrayOfByte.length - j);
            if (i < 0) {
                throw new EOFException();
            }
            j |= i;
        }
    }

    private String readLine(InputStream paramInputStream)
            throws IOException {
        ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
        int i = -1;
        int j = 0;
        for (; ; ) {
            int k = paramInputStream.read();
            if (k < 0) {
                break;
            }
            localByteArrayOutputStream.write(k);
            if ((i == 13) && (k == 10)) {
                j = 1;
                break;
            }
            i = k;
        }
        byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
        String str = new String(arrayOfByte, ASCII);
        if (j != 0) {
            str = str.substring(0, str.length() - 2);
        }
        return str;
    }
}




