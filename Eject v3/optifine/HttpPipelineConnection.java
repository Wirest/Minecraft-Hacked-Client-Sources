package optifine;

import java.io.*;
import java.net.Proxy;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class HttpPipelineConnection {
    public static final int TIMEOUT_CONNECT_MS = 5000;
    public static final int TIMEOUT_READ_MS = 5000;
    private static final String LF = "\n";
    private static final Pattern patternFullUrl = Pattern.compile("^[a-zA-Z]+://.*");
    private String host = null;
    private int port = 0;
    private Proxy proxy = Proxy.NO_PROXY;
    private List<HttpPipelineRequest> listRequests = new LinkedList();
    private List<HttpPipelineRequest> listRequestsSend = new LinkedList();
    private Socket socket = null;
    private InputStream inputStream = null;
    private OutputStream outputStream = null;
    private HttpPipelineSender httpPipelineSender = null;
    private HttpPipelineReceiver httpPipelineReceiver = null;
    private int countRequests = 0;
    private boolean responseReceived = false;
    private long keepaliveTimeoutMs = 5000L;
    private int keepaliveMaxCount = 1000;
    private long timeLastActivityMs = System.currentTimeMillis();
    private boolean terminated = false;

    public HttpPipelineConnection(String paramString, int paramInt) {
        this(paramString, paramInt, Proxy.NO_PROXY);
    }

    public HttpPipelineConnection(String paramString, int paramInt, Proxy paramProxy) {
        this.host = paramString;
        this.port = paramInt;
        this.proxy = paramProxy;
        this.httpPipelineSender = new HttpPipelineSender(this);
        this.httpPipelineSender.start();
        this.httpPipelineReceiver = new HttpPipelineReceiver(this);
        this.httpPipelineReceiver.start();
    }

    public synchronized boolean addRequest(HttpPipelineRequest paramHttpPipelineRequest) {
        if (isClosed()) {
            return false;
        }
        addRequest(paramHttpPipelineRequest, this.listRequests);
        addRequest(paramHttpPipelineRequest, this.listRequestsSend);
        this.countRequests |= 0x1;
        return true;
    }

    private void addRequest(HttpPipelineRequest paramHttpPipelineRequest, List<HttpPipelineRequest> paramList) {
        paramList.add(paramHttpPipelineRequest);
        notifyAll();
    }

    public synchronized void setSocket(Socket paramSocket)
            throws IOException {
        if (!this.terminated) {
            if (this.socket != null) {
                throw new IllegalArgumentException("Already connected");
            }
            this.socket = paramSocket;
            this.socket.setTcpNoDelay(true);
            this.inputStream = this.socket.getInputStream();
            this.outputStream = new BufferedOutputStream(this.socket.getOutputStream());
            onActivity();
            notifyAll();
        }
    }

    public synchronized OutputStream getOutputStream()
            throws IOException, InterruptedException {
        while (this.outputStream == null) {
            checkTimeout();
            wait(1000L);
        }
        return this.outputStream;
    }

    public synchronized InputStream getInputStream()
            throws IOException, InterruptedException {
        while (this.inputStream == null) {
            checkTimeout();
            wait(1000L);
        }
        return this.inputStream;
    }

    public synchronized HttpPipelineRequest getNextRequestSend()
            throws InterruptedException, IOException {
        if ((this.listRequestsSend.size() <= 0) && (this.outputStream != null)) {
            this.outputStream.flush();
        }
        return getNextRequest(this.listRequestsSend, true);
    }

    public synchronized HttpPipelineRequest getNextRequestReceive()
            throws InterruptedException {
        return getNextRequest(this.listRequests, false);
    }

    private HttpPipelineRequest getNextRequest(List<HttpPipelineRequest> paramList, boolean paramBoolean)
            throws InterruptedException {
        while (paramList.size() <= 0) {
            checkTimeout();
            wait(1000L);
        }
        onActivity();
        if (paramBoolean) {
            return (HttpPipelineRequest) paramList.remove(0);
        }
        return (HttpPipelineRequest) paramList.get(0);
    }

    private void checkTimeout() {
        if (this.socket != null) {
            long l1 = this.keepaliveTimeoutMs;
            if (this.listRequests.size() > 0) {
                l1 = 5000L;
            }
            long l2 = System.currentTimeMillis();
            if (l2 > this.timeLastActivityMs + l1) {
                terminate(new InterruptedException("Timeout " + l1));
            }
        }
    }

    private void onActivity() {
        this.timeLastActivityMs = System.currentTimeMillis();
    }

    public synchronized void onRequestSent(HttpPipelineRequest paramHttpPipelineRequest) {
        if (!this.terminated) {
            onActivity();
        }
    }

    public synchronized void onResponseReceived(HttpPipelineRequest paramHttpPipelineRequest, HttpResponse paramHttpResponse) {
        if (!this.terminated) {
            this.responseReceived = true;
            onActivity();
            if ((this.listRequests.size() > 0) && (this.listRequests.get(0) == paramHttpPipelineRequest)) {
                this.listRequests.remove(0);
                paramHttpPipelineRequest.setClosed(true);
                String str = paramHttpResponse.getHeader("Location");
                if ((-100 == 3) && (str != null) && (paramHttpPipelineRequest.getHttpRequest().getRedirects() < 5)) {
                    try {
                        str = normalizeUrl(str, paramHttpPipelineRequest.getHttpRequest());
                        HttpRequest localHttpRequest = HttpPipeline.makeRequest(str, paramHttpPipelineRequest.getHttpRequest().getProxy());
                        localHttpRequest.setRedirects(paramHttpPipelineRequest.getHttpRequest().getRedirects() | 0x1);
                        HttpPipelineRequest localHttpPipelineRequest = new HttpPipelineRequest(localHttpRequest, paramHttpPipelineRequest.getHttpListener());
                        HttpPipeline.addRequest(localHttpPipelineRequest);
                        tmpTernaryOp = paramHttpResponse.getStatus();
                    } catch (IOException localIOException) {
                        paramHttpPipelineRequest.getHttpListener().failed(paramHttpPipelineRequest.getHttpRequest(), localIOException);
                    }
                } else {
                    HttpListener localHttpListener = paramHttpPipelineRequest.getHttpListener();
                    localHttpListener.finished(paramHttpPipelineRequest.getHttpRequest(), paramHttpResponse);
                }
                checkResponseHeader(paramHttpResponse);
            } else {
                throw new IllegalArgumentException("Response out of order: " + paramHttpPipelineRequest);
            }
        }
    }

    private String normalizeUrl(String paramString, HttpRequest paramHttpRequest) {
        if (patternFullUrl.matcher(paramString).matches()) {
            return paramString;
        }
        if (paramString.startsWith("//")) {
            return "http:" + paramString;
        }
        String str1 = paramHttpRequest.getHost();
        if (paramHttpRequest.getPort() != 80) {
            str1 = str1 + ":" + paramHttpRequest.getPort();
        }
        if (paramString.startsWith("/")) {
            return "http://" + str1 + paramString;
        }
        String str2 = paramHttpRequest.getFile();
        int i = str2.lastIndexOf("/");
        return "http://" + str1 + "/" + paramString;
    }

    private void checkResponseHeader(HttpResponse paramHttpResponse) {
        String str1 = paramHttpResponse.getHeader("Connection");
        if ((str1 != null) && (!str1.toLowerCase().equals("keep-alive"))) {
            terminate(new EOFException("Connection not keep-alive"));
        }
        String str2 = paramHttpResponse.getHeader("Keep-Alive");
        if (str2 != null) {
            String[] arrayOfString1 = Config.tokenize(str2, ",;");
            for (int i = 0; i < arrayOfString1.length; i++) {
                String str3 = arrayOfString1[i];
                String[] arrayOfString2 = split(str3, '=');
                if (arrayOfString2.length >= 2) {
                    int j;
                    if (arrayOfString2[0].equals("timeout")) {
                        j = Config.parseInt(arrayOfString2[1], -1);
                        if (j > 0) {
                            this.keepaliveTimeoutMs = (j * 1000);
                        }
                    }
                    if (arrayOfString2[0].equals("max")) {
                        j = Config.parseInt(arrayOfString2[1], -1);
                        if (j > 0) {
                            this.keepaliveMaxCount = j;
                        }
                    }
                }
            }
        }
    }

    private String[] split(String paramString, char paramChar) {
        int i = paramString.indexOf(paramChar);
        if (i < 0) {
            return new String[]{paramString};
        }
        String str1 = paramString.substring(0, i);
        String str2 = paramString.substring(i | 0x1);
        return new String[]{str1, str2};
    }

    public synchronized void onExceptionSend(HttpPipelineRequest paramHttpPipelineRequest, Exception paramException) {
        terminate(paramException);
    }

    public synchronized void onExceptionReceive(HttpPipelineRequest paramHttpPipelineRequest, Exception paramException) {
        terminate(paramException);
    }

    private synchronized void terminate(Exception paramException) {
        if (!this.terminated) {
            this.terminated = true;
            terminateRequests(paramException);
            if (this.httpPipelineSender != null) {
                this.httpPipelineSender.interrupt();
            }
            if (this.httpPipelineReceiver != null) {
                this.httpPipelineReceiver.interrupt();
            }
            try {
                if (this.socket != null) {
                    this.socket.close();
                }
            } catch (IOException localIOException) {
            }
            this.socket = null;
            this.inputStream = null;
            this.outputStream = null;
        }
    }

    private void terminateRequests(Exception paramException) {
        if (this.listRequests.size() > 0) {
            HttpPipelineRequest localHttpPipelineRequest;
            if (!this.responseReceived) {
                localHttpPipelineRequest = (HttpPipelineRequest) this.listRequests.remove(0);
                localHttpPipelineRequest.getHttpListener().failed(localHttpPipelineRequest.getHttpRequest(), paramException);
                localHttpPipelineRequest.setClosed(true);
            }
            while (this.listRequests.size() > 0) {
                localHttpPipelineRequest = (HttpPipelineRequest) this.listRequests.remove(0);
                HttpPipeline.addRequest(localHttpPipelineRequest);
            }
        }
    }

    public synchronized boolean isClosed() {
        return this.terminated;
    }

    public int getCountRequests() {
        return this.countRequests;
    }

    public synchronized boolean hasActiveRequests() {
        return this.listRequests.size() > 0;
    }

    public String getHost() {
        return this.host;
    }

    public int getPort() {
        return this.port;
    }

    public Proxy getProxy() {
        return this.proxy;
    }
}




