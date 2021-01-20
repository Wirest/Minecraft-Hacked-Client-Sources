package optifine;

import java.util.Map;

public class FileUploadThread extends Thread
{
    private String urlString;
    private Map headers;
    private byte[] content;
    private IFileUploadListener listener;

    public FileUploadThread(String p_i42_1_, Map p_i42_2_, byte[] p_i42_3_, IFileUploadListener p_i42_4_)
    {
        this.urlString = p_i42_1_;
        this.headers = p_i42_2_;
        this.content = p_i42_3_;
        this.listener = p_i42_4_;
    }

    public void run()
    {
        try
        {
            HttpUtils.post(this.urlString, this.headers, this.content);
            this.listener.fileUploadFinished(this.urlString, this.content, (Throwable)null);
        }
        catch (Exception exception)
        {
            this.listener.fileUploadFinished(this.urlString, this.content, exception);
        }
    }

    public String getUrlString()
    {
        return this.urlString;
    }

    public byte[] getContent()
    {
        return this.content;
    }

    public IFileUploadListener getListener()
    {
        return this.listener;
    }
}
