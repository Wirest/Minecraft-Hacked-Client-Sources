// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.realmsclient.client;

import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.net.Proxy;
import java.io.IOException;
import java.net.MalformedURLException;
import com.mojang.realmsclient.exception.RealmsHttpException;
import java.net.URL;
import java.net.HttpURLConnection;

public abstract class Request<T extends Request>
{
    protected HttpURLConnection connection;
    private boolean connected;
    protected String url;
    private static final int DEFAULT_READ_TIMEOUT = 60000;
    private static final int DEFAULT_CONNECT_TIMEOUT = 5000;
    
    public Request(final String url, final int connectTimeout, final int readTimeout) {
        try {
            this.url = url;
            final Proxy proxy = RealmsClientConfig.getProxy();
            if (proxy != null) {
                this.connection = (HttpURLConnection)new URL(url).openConnection(proxy);
            }
            else {
                this.connection = (HttpURLConnection)new URL(url).openConnection();
            }
            this.connection.setConnectTimeout(connectTimeout);
            this.connection.setReadTimeout(readTimeout);
        }
        catch (MalformedURLException e) {
            throw new RealmsHttpException(e.getMessage(), e);
        }
        catch (IOException e2) {
            throw new RealmsHttpException(e2.getMessage(), e2);
        }
    }
    
    public void cookie(final String key, final String value) {
        cookie(this.connection, key, value);
    }
    
    public static void cookie(final HttpURLConnection connection, final String key, final String value) {
        final String cookie = connection.getRequestProperty("Cookie");
        if (cookie == null) {
            connection.setRequestProperty("Cookie", key + "=" + value);
        }
        else {
            connection.setRequestProperty("Cookie", cookie + ";" + key + "=" + value);
        }
    }
    
    public T header(final String name, final String value) {
        this.connection.addRequestProperty(name, value);
        return (T)this;
    }
    
    public int getRetryAfterHeader() {
        return getRetryAfterHeader(this.connection);
    }
    
    public static int getRetryAfterHeader(final HttpURLConnection connection) {
        final String pauseTime = connection.getHeaderField("Retry-After");
        try {
            return Integer.valueOf(pauseTime);
        }
        catch (Exception e) {
            return 5;
        }
    }
    
    public int responseCode() {
        try {
            this.connect();
            return this.connection.getResponseCode();
        }
        catch (Exception e) {
            throw new RealmsHttpException(e.getMessage(), e);
        }
    }
    
    public String text() {
        try {
            this.connect();
            String result = null;
            if (this.responseCode() >= 400) {
                result = this.read(this.connection.getErrorStream());
            }
            else {
                result = this.read(this.connection.getInputStream());
            }
            this.dispose();
            return result;
        }
        catch (IOException e) {
            throw new RealmsHttpException(e.getMessage(), e);
        }
    }
    
    private String read(final InputStream in) throws IOException {
        if (in == null) {
            return "";
        }
        final InputStreamReader streamReader = new InputStreamReader(in, "UTF-8");
        final StringBuilder sb = new StringBuilder();
        for (int x = streamReader.read(); x != -1; x = streamReader.read()) {
            sb.append((char)x);
        }
        return sb.toString();
    }
    
    private void dispose() {
        final byte[] bytes = new byte[1024];
        try {
            int count = 0;
            final InputStream in = this.connection.getInputStream();
            while ((count = in.read(bytes)) > 0) {}
            in.close();
        }
        catch (Exception ignore) {
            try {
                final InputStream errorStream = this.connection.getErrorStream();
                int ret = 0;
                if (errorStream == null) {
                    return;
                }
                while ((ret = errorStream.read(bytes)) > 0) {}
                errorStream.close();
            }
            catch (IOException ex) {}
        }
        finally {
            if (this.connection != null) {
                this.connection.disconnect();
            }
        }
    }
    
    protected T connect() {
        if (!this.connected) {
            final T t = this.doConnect();
            this.connected = true;
            return t;
        }
        return (T)this;
    }
    
    protected abstract T doConnect();
    
    public static Request<?> get(final String url) {
        return new Get(url, 5000, 60000);
    }
    
    public static Request<?> get(final String url, final int connectTimeoutMillis, final int readTimeoutMillis) {
        return new Get(url, connectTimeoutMillis, readTimeoutMillis);
    }
    
    public static Request<?> post(final String uri, final String content) {
        return new Post(uri, content.getBytes(), 5000, 60000);
    }
    
    public static Request<?> post(final String uri, final String content, final int connectTimeoutMillis, final int readTimeoutMillis) {
        return new Post(uri, content.getBytes(), connectTimeoutMillis, readTimeoutMillis);
    }
    
    public static Request<?> delete(final String url) {
        return new Delete(url, 5000, 60000);
    }
    
    public static Request<?> put(final String url, final String content) {
        return new Put(url, content.getBytes(), 5000, 60000);
    }
    
    public static Request<?> put(final String url, final String content, final int connectTimeoutMillis, final int readTimeoutMillis) {
        return new Put(url, content.getBytes(), connectTimeoutMillis, readTimeoutMillis);
    }
    
    public String getHeader(final String header) {
        return getHeader(this.connection, header);
    }
    
    public static String getHeader(final HttpURLConnection connection, final String header) {
        try {
            return connection.getHeaderField(header);
        }
        catch (Exception e) {
            return "";
        }
    }
    
    public static class Delete extends Request<Delete>
    {
        public Delete(final String uri, final int connectTimeout, final int readTimeout) {
            super(uri, connectTimeout, readTimeout);
        }
        
        public Delete doConnect() {
            try {
                this.connection.setDoOutput(true);
                this.connection.setRequestMethod("DELETE");
                this.connection.connect();
                return this;
            }
            catch (Exception e) {
                throw new RealmsHttpException(e.getMessage(), e);
            }
        }
    }
    
    public static class Get extends Request<Get>
    {
        public Get(final String uri, final int connectTimeout, final int readTimeout) {
            super(uri, connectTimeout, readTimeout);
        }
        
        public Get doConnect() {
            try {
                this.connection.setDoInput(true);
                this.connection.setDoOutput(true);
                this.connection.setUseCaches(false);
                this.connection.setRequestMethod("GET");
                return this;
            }
            catch (Exception e) {
                throw new RealmsHttpException(e.getMessage(), e);
            }
        }
    }
    
    public static class Put extends Request<Put>
    {
        private byte[] content;
        
        public Put(final String uri, final byte[] content, final int connectTimeout, final int readTimeout) {
            super(uri, connectTimeout, readTimeout);
            this.content = content;
        }
        
        public Put doConnect() {
            try {
                if (this.content != null) {
                    this.connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                }
                this.connection.setDoOutput(true);
                this.connection.setDoInput(true);
                this.connection.setRequestMethod("PUT");
                final OutputStream os = this.connection.getOutputStream();
                os.write(this.content);
                os.flush();
                return this;
            }
            catch (Exception e) {
                throw new RealmsHttpException(e.getMessage(), e);
            }
        }
    }
    
    public static class Post extends Request<Post>
    {
        private byte[] content;
        
        public Post(final String uri, final byte[] content, final int connectTimeout, final int readTimeout) {
            super(uri, connectTimeout, readTimeout);
            this.content = content;
        }
        
        public Post doConnect() {
            try {
                if (this.content != null) {
                    this.connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                }
                this.connection.setDoInput(true);
                this.connection.setDoOutput(true);
                this.connection.setUseCaches(false);
                this.connection.setRequestMethod("POST");
                final OutputStream out = this.connection.getOutputStream();
                out.write(this.content);
                out.flush();
                return this;
            }
            catch (Exception e) {
                throw new RealmsHttpException(e.getMessage(), e);
            }
        }
    }
}
