package optifine;

import net.minecraft.client.Minecraft;

public class FileDownloadThread
        extends Thread {
    private String urlString = null;
    private IFileDownloadListener listener = null;

    public FileDownloadThread(String paramString, IFileDownloadListener paramIFileDownloadListener) {
        this.urlString = paramString;
        this.listener = paramIFileDownloadListener;
    }

    public void run() {
        try {
            byte[] arrayOfByte = HttpPipeline.get(this.urlString, Minecraft.getMinecraft().getProxy());
            this.listener.fileDownloadFinished(this.urlString, arrayOfByte, (Throwable) null);
        } catch (Exception localException) {
            this.listener.fileDownloadFinished(this.urlString, (byte[]) null, localException);
        }
    }

    public String getUrlString() {
        return this.urlString;
    }

    public IFileDownloadListener getListener() {
        return this.listener;
    }
}




