// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import net.minecraft.client.Minecraft;

public class FileDownloadThread extends Thread
{
    private String urlString;
    private IFileDownloadListener listener;
    
    public FileDownloadThread(final String p_i41_1_, final IFileDownloadListener p_i41_2_) {
        this.urlString = null;
        this.listener = null;
        this.urlString = p_i41_1_;
        this.listener = p_i41_2_;
    }
    
    @Override
    public void run() {
        try {
            final byte[] abyte = HttpPipeline.get(this.urlString, Minecraft.getMinecraft().getProxy());
            this.listener.fileDownloadFinished(this.urlString, abyte, null);
        }
        catch (Exception exception) {
            this.listener.fileDownloadFinished(this.urlString, null, exception);
        }
    }
    
    public String getUrlString() {
        return this.urlString;
    }
    
    public IFileDownloadListener getListener() {
        return this.listener;
    }
}
