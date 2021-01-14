package net.minecraft.optifine;

public class FileDownloadThread extends Thread {
    private String urlString = null;
    private IFileDownloadListener listener = null;

    public FileDownloadThread(String urlString, IFileDownloadListener listener) {
        this.urlString = urlString;
        this.listener = listener;
    }

    @Override
    public void run() {
        try {
            byte[] e = HttpUtils.get(urlString);
            listener.fileDownloadFinished(urlString, e, (Throwable) null);
        } catch (Exception var2) {
            listener.fileDownloadFinished(urlString, (byte[]) null, var2);
        }
    }

    public String getUrlString() {
        return urlString;
    }

    public IFileDownloadListener getListener() {
        return listener;
    }
}
