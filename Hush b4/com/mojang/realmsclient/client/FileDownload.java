// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.realmsclient.client;

import org.apache.commons.io.output.CountingOutputStream;
import java.awt.event.ActionEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import java.util.regex.Matcher;
import java.util.Iterator;
import java.io.BufferedOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import net.minecraft.realms.Realms;
import net.minecraft.realms.RealmsLevelSummary;
import org.apache.commons.lang3.StringUtils;
import net.minecraft.realms.RealmsSharedConstants;
import java.util.regex.Pattern;
import org.apache.http.HttpResponse;
import java.io.OutputStream;
import org.apache.commons.io.IOUtils;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import net.minecraft.realms.RealmsAnvilLevelStorageSource;
import com.mojang.realmsclient.gui.screens.RealmsDownloadLatestWorldScreen;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import java.io.IOException;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import java.io.File;
import org.apache.logging.log4j.Logger;

public class FileDownload
{
    private static final Logger LOGGER;
    private volatile boolean cancelled;
    private volatile boolean finished;
    private volatile boolean error;
    private volatile boolean extracting;
    private volatile File tempFile;
    private volatile HttpGet request;
    private Thread currentThread;
    private RequestConfig requestConfig;
    private static final String[] INVALID_FILE_NAMES;
    
    public FileDownload() {
        this.cancelled = false;
        this.finished = false;
        this.error = false;
        this.extracting = false;
        this.requestConfig = RequestConfig.custom().setSocketTimeout(120000).setConnectTimeout(120000).build();
    }
    
    public long contentLength(final String downloadLink) {
        CloseableHttpClient client = null;
        HttpGet httpGet = null;
        try {
            httpGet = new HttpGet(downloadLink);
            client = HttpClientBuilder.create().setDefaultRequestConfig(this.requestConfig).build();
            final CloseableHttpResponse response = client.execute((HttpUriRequest)httpGet);
            return Long.parseLong(response.getFirstHeader("Content-Length").getValue());
        }
        catch (Throwable t) {
            FileDownload.LOGGER.error("Unable to get content length for download");
            return 0L;
        }
        finally {
            if (httpGet != null) {
                httpGet.releaseConnection();
            }
            if (client != null) {
                try {
                    client.close();
                }
                catch (IOException e) {
                    FileDownload.LOGGER.error("Could not close http client", e);
                }
            }
        }
    }
    
    public void download(final String downloadLink, final String worldName, final RealmsDownloadLatestWorldScreen.DownloadStatus downloadStatus, final RealmsAnvilLevelStorageSource levelStorageSource) {
        if (this.currentThread != null) {
            return;
        }
        (this.currentThread = new Thread() {
            @Override
            public void run() {
                CloseableHttpClient client = null;
                try {
                    FileDownload.this.tempFile = File.createTempFile("backup", ".tar.gz");
                    FileDownload.this.request = new HttpGet(downloadLink);
                    client = HttpClientBuilder.create().setDefaultRequestConfig(FileDownload.this.requestConfig).build();
                    final HttpResponse response = client.execute((HttpUriRequest)FileDownload.this.request);
                    downloadStatus.totalBytes = Long.parseLong(response.getFirstHeader("Content-Length").getValue());
                    if (response.getStatusLine().getStatusCode() != 200) {
                        FileDownload.this.error = true;
                        FileDownload.this.request.abort();
                        return;
                    }
                    final OutputStream os = new FileOutputStream(FileDownload.this.tempFile);
                    final ProgressListener progressListener = new ProgressListener(worldName.trim(), FileDownload.this.tempFile, levelStorageSource, downloadStatus);
                    final DownloadCountingOutputStream dcount = new DownloadCountingOutputStream(os);
                    dcount.setListener(progressListener);
                    IOUtils.copy(response.getEntity().getContent(), dcount);
                }
                catch (Exception e) {
                    FileDownload.LOGGER.error("Caught exception while downloading: " + e.getMessage());
                    FileDownload.this.error = true;
                    FileDownload.this.request.releaseConnection();
                    if (FileDownload.this.tempFile != null) {
                        FileDownload.this.tempFile.delete();
                    }
                    if (client != null) {
                        try {
                            client.close();
                        }
                        catch (IOException e2) {
                            FileDownload.LOGGER.error("Failed to close Realms download client");
                        }
                    }
                }
                finally {
                    FileDownload.this.request.releaseConnection();
                    if (FileDownload.this.tempFile != null) {
                        FileDownload.this.tempFile.delete();
                    }
                    if (client != null) {
                        try {
                            client.close();
                        }
                        catch (IOException e3) {
                            FileDownload.LOGGER.error("Failed to close Realms download client");
                        }
                    }
                }
            }
        }).start();
    }
    
    public void cancel() {
        if (this.request != null) {
            this.request.abort();
        }
        if (this.tempFile != null) {
            this.tempFile.delete();
        }
        this.cancelled = true;
    }
    
    public boolean isFinished() {
        return this.finished;
    }
    
    public boolean isError() {
        return this.error;
    }
    
    public boolean isExtracting() {
        return this.extracting;
    }
    
    public static String findAvailableFolderName(String folder) {
        folder = folder.replaceAll("[\\./\"]", "_");
        for (final String invalidName : FileDownload.INVALID_FILE_NAMES) {
            if (folder.equalsIgnoreCase(invalidName)) {
                folder = "_" + folder + "_";
            }
        }
        return folder;
    }
    
    private void untarGzipArchive(String name, final File file, final RealmsAnvilLevelStorageSource levelStorageSource) throws IOException {
        final Pattern namePattern = Pattern.compile(".*-([0-9]+)$");
        int number = 1;
        for (final char replacer : RealmsSharedConstants.ILLEGAL_FILE_CHARACTERS) {
            name = name.replace(replacer, '_');
        }
        if (StringUtils.isEmpty(name)) {
            name = "Realm";
        }
        name = findAvailableFolderName(name);
        try {
            for (final RealmsLevelSummary summary : levelStorageSource.getLevelList()) {
                if (summary.getLevelId().toLowerCase().startsWith(name.toLowerCase())) {
                    final Matcher matcher = namePattern.matcher(summary.getLevelId());
                    if (matcher.matches()) {
                        if (Integer.valueOf(matcher.group(1)) <= number) {
                            continue;
                        }
                        number = Integer.valueOf(matcher.group(1));
                    }
                    else {
                        ++number;
                    }
                }
            }
        }
        catch (Exception e) {
            this.error = true;
            return;
        }
        String finalName;
        if (!levelStorageSource.isNewLevelIdAcceptable(name) || number > 1) {
            finalName = name + ((number == 1) ? "" : ("-" + number));
            if (!levelStorageSource.isNewLevelIdAcceptable(finalName)) {
                for (boolean foundName = false; !foundName; foundName = true) {
                    ++number;
                    finalName = name + ((number == 1) ? "" : ("-" + number));
                    if (levelStorageSource.isNewLevelIdAcceptable(finalName)) {}
                }
            }
        }
        else {
            finalName = name;
        }
        TarArchiveInputStream tarIn = null;
        final File saves = new File(Realms.getGameDirectoryPath(), "saves");
        try {
            saves.mkdir();
            tarIn = new TarArchiveInputStream(new GzipCompressorInputStream(new BufferedInputStream(new FileInputStream(file))));
            for (TarArchiveEntry tarEntry = tarIn.getNextTarEntry(); tarEntry != null; tarEntry = tarIn.getNextTarEntry()) {
                final File destPath = new File(saves, tarEntry.getName().replace("world", finalName));
                if (tarEntry.isDirectory()) {
                    destPath.mkdirs();
                }
                else {
                    destPath.createNewFile();
                    byte[] btoRead = new byte[1024];
                    final BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(destPath));
                    int len = 0;
                    while ((len = tarIn.read(btoRead)) != -1) {
                        bout.write(btoRead, 0, len);
                    }
                    bout.close();
                    btoRead = null;
                }
            }
        }
        catch (Exception e2) {
            this.error = true;
        }
        finally {
            if (tarIn != null) {
                tarIn.close();
            }
            if (file != null) {
                file.delete();
            }
            final RealmsAnvilLevelStorageSource levelSource = levelStorageSource;
            levelSource.renameLevel(finalName, finalName.trim());
            this.finished = true;
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
        INVALID_FILE_NAMES = new String[] { "CON", "COM", "PRN", "AUX", "CLOCK$", "NUL", "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9", "LPT1", "LPT2", "LPT3", "LPT4", "LPT5", "LPT6", "LPT7", "LPT8", "LPT9" };
    }
    
    private class ProgressListener implements ActionListener
    {
        private volatile String worldName;
        private volatile File tempFile;
        private volatile RealmsAnvilLevelStorageSource levelStorageSource;
        private volatile RealmsDownloadLatestWorldScreen.DownloadStatus downloadStatus;
        
        private ProgressListener(final String worldName, final File tempFile, final RealmsAnvilLevelStorageSource levelStorageSource, final RealmsDownloadLatestWorldScreen.DownloadStatus downloadStatus) {
            this.worldName = worldName;
            this.tempFile = tempFile;
            this.levelStorageSource = levelStorageSource;
            this.downloadStatus = downloadStatus;
        }
        
        @Override
        public void actionPerformed(final ActionEvent e) {
            this.downloadStatus.bytesWritten = ((DownloadCountingOutputStream)e.getSource()).getByteCount();
            if (this.downloadStatus.bytesWritten >= this.downloadStatus.totalBytes && !FileDownload.this.cancelled) {
                try {
                    FileDownload.this.extracting = true;
                    FileDownload.this.untarGzipArchive(this.worldName, this.tempFile, this.levelStorageSource);
                }
                catch (IOException e2) {
                    FileDownload.this.error = true;
                }
            }
        }
    }
    
    private class DownloadCountingOutputStream extends CountingOutputStream
    {
        private ActionListener listener;
        
        public DownloadCountingOutputStream(final OutputStream out) {
            super(out);
            this.listener = null;
        }
        
        public void setListener(final ActionListener listener) {
            this.listener = listener;
        }
        
        @Override
        protected void afterWrite(final int n) throws IOException {
            super.afterWrite(n);
            if (this.listener != null) {
                this.listener.actionPerformed(new ActionEvent(this, 0, null));
            }
        }
    }
}
