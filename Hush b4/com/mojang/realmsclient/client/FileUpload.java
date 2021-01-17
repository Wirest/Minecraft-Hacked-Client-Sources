// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.realmsclient.client;

import org.apache.http.util.Args;
import java.io.OutputStream;
import org.apache.http.entity.InputStreamEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import java.io.IOException;
import com.google.gson.JsonParser;
import org.apache.http.util.EntityUtils;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.HttpEntity;
import java.io.InputStream;
import java.io.FileInputStream;
import com.mojang.realmsclient.RealmsVersion;
import org.apache.http.impl.client.HttpClientBuilder;
import com.mojang.realmsclient.dto.UploadInfo;
import java.io.File;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.logging.log4j.Logger;

public class FileUpload
{
    private static final Logger LOGGER;
    private static final String UPLOAD_PATH = "/upload";
    private static final String PORT = "8080";
    private volatile boolean cancelled;
    private volatile boolean finished;
    private HttpPost request;
    private int statusCode;
    private String errorMessage;
    private RequestConfig requestConfig;
    private Thread currentThread;
    
    public FileUpload() {
        this.cancelled = false;
        this.finished = false;
        this.statusCode = -1;
        this.requestConfig = RequestConfig.custom().setSocketTimeout(120000).setConnectTimeout(120000).build();
    }
    
    public void upload(final File file, final long worldId, final int slotId, final UploadInfo uploadInfo, final String sessionId, final String username, final String clientVersion, final UploadStatus uploadStatus) {
        if (this.currentThread != null) {
            return;
        }
        (this.currentThread = new Thread() {
            @Override
            public void run() {
                FileUpload.this.request = new HttpPost("http://" + uploadInfo.getUploadEndpoint() + ":" + "8080" + "/upload" + "/" + String.valueOf(worldId) + "/" + String.valueOf(slotId));
                CloseableHttpClient client = null;
                try {
                    client = HttpClientBuilder.create().setDefaultRequestConfig(FileUpload.this.requestConfig).build();
                    final String realmsVersion = RealmsVersion.getVersion();
                    if (realmsVersion != null) {
                        FileUpload.this.request.setHeader("Cookie", "sid=" + sessionId + ";token=" + uploadInfo.getToken() + ";user=" + username + ";version=" + clientVersion + ";realms_version=" + realmsVersion);
                    }
                    else {
                        FileUpload.this.request.setHeader("Cookie", "sid=" + sessionId + ";token=" + uploadInfo.getToken() + ";user=" + username + ";version=" + clientVersion);
                    }
                    uploadStatus.totalBytes = file.length();
                    final CustomInputStreamEntity entity = new CustomInputStreamEntity(new FileInputStream(file), file.length(), uploadStatus);
                    entity.setContentType("application/octet-stream");
                    FileUpload.this.request.setEntity(entity);
                    final HttpResponse response = client.execute((HttpUriRequest)FileUpload.this.request);
                    final int statusCode = response.getStatusLine().getStatusCode();
                    if (statusCode == 401) {
                        FileUpload.LOGGER.debug("Realms server returned 401: " + response.getFirstHeader("WWW-Authenticate"));
                    }
                    FileUpload.this.statusCode = statusCode;
                    final String json = EntityUtils.toString(response.getEntity(), "UTF-8");
                    if (json != null) {
                        try {
                            final JsonParser parser = new JsonParser();
                            FileUpload.this.errorMessage = parser.parse(json).getAsJsonObject().get("errorMsg").getAsString();
                        }
                        catch (Exception ex) {}
                    }
                }
                catch (Exception e) {
                    FileUpload.LOGGER.error("Caught exception while uploading: " + e.getMessage());
                    FileUpload.this.request.releaseConnection();
                    FileUpload.this.finished = true;
                    if (client != null) {
                        try {
                            client.close();
                        }
                        catch (IOException e2) {
                            FileUpload.LOGGER.error("Failed to close Realms upload client");
                        }
                    }
                }
                finally {
                    FileUpload.this.request.releaseConnection();
                    FileUpload.this.finished = true;
                    if (client != null) {
                        try {
                            client.close();
                        }
                        catch (IOException e3) {
                            FileUpload.LOGGER.error("Failed to close Realms upload client");
                        }
                    }
                }
            }
        }).start();
    }
    
    public void cancel() {
        this.cancelled = true;
        if (this.request != null) {
            this.request.abort();
        }
    }
    
    public boolean isFinished() {
        return this.finished;
    }
    
    public int getStatusCode() {
        return this.statusCode;
    }
    
    public String getErrorMessage() {
        return this.errorMessage;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    private static class CustomInputStreamEntity extends InputStreamEntity
    {
        private final long length;
        private final InputStream content;
        private final UploadStatus uploadStatus;
        
        public CustomInputStreamEntity(final InputStream instream, final long length, final UploadStatus uploadStatus) {
            super(instream);
            this.content = instream;
            this.length = length;
            this.uploadStatus = uploadStatus;
        }
        
        @Override
        public void writeTo(final OutputStream outstream) throws IOException {
            Args.notNull(outstream, "Output stream");
            final InputStream instream = this.content;
            try {
                final byte[] buffer = new byte[4096];
                if (this.length < 0L) {
                    int l;
                    while ((l = instream.read(buffer)) != -1) {
                        outstream.write(buffer, 0, l);
                        final UploadStatus uploadStatus = this.uploadStatus;
                        uploadStatus.bytesWritten += (Long)l;
                    }
                }
                else {
                    long remaining = this.length;
                    while (remaining > 0L) {
                        final int l = instream.read(buffer, 0, (int)Math.min(4096L, remaining));
                        if (l == -1) {
                            break;
                        }
                        outstream.write(buffer, 0, l);
                        final UploadStatus uploadStatus2 = this.uploadStatus;
                        uploadStatus2.bytesWritten += (Long)l;
                        remaining -= l;
                        outstream.flush();
                    }
                }
            }
            finally {
                instream.close();
            }
        }
    }
}
