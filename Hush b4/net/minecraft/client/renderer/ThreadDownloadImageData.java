// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer;

import optifine.HttpResponse;
import optifine.HttpRequest;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import optifine.HttpPipeline;
import java.net.Proxy;
import org.apache.commons.io.FileUtils;
import optifine.Config;
import net.minecraft.client.Minecraft;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.IOException;
import javax.imageio.ImageIO;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.logging.log4j.Logger;
import net.minecraft.client.renderer.texture.SimpleTexture;

public class ThreadDownloadImageData extends SimpleTexture
{
    private static final Logger logger;
    private static final AtomicInteger threadDownloadCounter;
    private final File cacheFile;
    private final String imageUrl;
    private final IImageBuffer imageBuffer;
    private BufferedImage bufferedImage;
    private Thread imageThread;
    private boolean textureUploaded;
    private static final String __OBFID = "CL_00001049";
    public Boolean imageFound;
    public boolean pipeline;
    
    static {
        logger = LogManager.getLogger();
        threadDownloadCounter = new AtomicInteger(0);
    }
    
    public ThreadDownloadImageData(final File cacheFileIn, final String imageUrlIn, final ResourceLocation textureResourceLocation, final IImageBuffer imageBufferIn) {
        super(textureResourceLocation);
        this.imageFound = null;
        this.pipeline = false;
        this.cacheFile = cacheFileIn;
        this.imageUrl = imageUrlIn;
        this.imageBuffer = imageBufferIn;
    }
    
    private void checkTextureUploaded() {
        if (!this.textureUploaded && this.bufferedImage != null) {
            this.textureUploaded = true;
            if (this.textureLocation != null) {
                this.deleteGlTexture();
            }
            TextureUtil.uploadTextureImage(super.getGlTextureId(), this.bufferedImage);
        }
    }
    
    @Override
    public int getGlTextureId() {
        this.checkTextureUploaded();
        return super.getGlTextureId();
    }
    
    public void setBufferedImage(final BufferedImage bufferedImageIn) {
        this.bufferedImage = bufferedImageIn;
        if (this.imageBuffer != null) {
            this.imageBuffer.skinAvailable();
        }
        this.imageFound = (this.bufferedImage != null);
    }
    
    @Override
    public void loadTexture(final IResourceManager resourceManager) throws IOException {
        if (this.bufferedImage == null && this.textureLocation != null) {
            super.loadTexture(resourceManager);
        }
        if (this.imageThread == null) {
            if (this.cacheFile != null && this.cacheFile.isFile()) {
                ThreadDownloadImageData.logger.debug("Loading http texture from local cache ({})", this.cacheFile);
                try {
                    this.bufferedImage = ImageIO.read(this.cacheFile);
                    if (this.imageBuffer != null) {
                        this.setBufferedImage(this.imageBuffer.parseUserSkin(this.bufferedImage));
                    }
                    this.imageFound = (this.bufferedImage != null);
                }
                catch (IOException ioexception) {
                    ThreadDownloadImageData.logger.error("Couldn't load skin " + this.cacheFile, ioexception);
                    this.loadTextureFromServer();
                }
            }
            else {
                this.loadTextureFromServer();
            }
        }
    }
    
    protected void loadTextureFromServer() {
        (this.imageThread = new Thread("Texture Downloader #" + ThreadDownloadImageData.threadDownloadCounter.incrementAndGet()) {
            private static final String __OBFID = "CL_00001050";
            
            @Override
            public void run() {
                HttpURLConnection httpurlconnection = null;
                ThreadDownloadImageData.logger.debug("Downloading http texture from {} to {}", ThreadDownloadImageData.this.imageUrl, ThreadDownloadImageData.this.cacheFile);
                if (ThreadDownloadImageData.this.shouldPipeline()) {
                    ThreadDownloadImageData.this.loadPipelined();
                }
                else {
                    try {
                        httpurlconnection = (HttpURLConnection)new URL(ThreadDownloadImageData.this.imageUrl).openConnection(Minecraft.getMinecraft().getProxy());
                        httpurlconnection.setDoInput(true);
                        httpurlconnection.setDoOutput(false);
                        httpurlconnection.connect();
                        if (httpurlconnection.getResponseCode() / 100 != 2) {
                            if (httpurlconnection.getErrorStream() != null) {
                                Config.readAll(httpurlconnection.getErrorStream());
                            }
                            return;
                        }
                        BufferedImage bufferedimage;
                        if (ThreadDownloadImageData.this.cacheFile != null) {
                            FileUtils.copyInputStreamToFile(httpurlconnection.getInputStream(), ThreadDownloadImageData.this.cacheFile);
                            bufferedimage = ImageIO.read(ThreadDownloadImageData.this.cacheFile);
                        }
                        else {
                            bufferedimage = TextureUtil.readBufferedImage(httpurlconnection.getInputStream());
                        }
                        if (ThreadDownloadImageData.this.imageBuffer != null) {
                            bufferedimage = ThreadDownloadImageData.this.imageBuffer.parseUserSkin(bufferedimage);
                        }
                        ThreadDownloadImageData.this.setBufferedImage(bufferedimage);
                    }
                    catch (Exception exception) {
                        ThreadDownloadImageData.logger.error("Couldn't download http texture: " + exception.getClass().getName() + ": " + exception.getMessage());
                        return;
                    }
                    finally {
                        if (httpurlconnection != null) {
                            httpurlconnection.disconnect();
                        }
                        ThreadDownloadImageData.this.imageFound = (ThreadDownloadImageData.this.bufferedImage != null);
                    }
                    if (httpurlconnection != null) {
                        httpurlconnection.disconnect();
                    }
                    ThreadDownloadImageData.this.imageFound = (ThreadDownloadImageData.this.bufferedImage != null);
                }
            }
        }).setDaemon(true);
        this.imageThread.start();
    }
    
    private boolean shouldPipeline() {
        if (!this.pipeline) {
            return false;
        }
        final Proxy proxy = Minecraft.getMinecraft().getProxy();
        return (proxy.type() == Proxy.Type.DIRECT || proxy.type() == Proxy.Type.SOCKS) && this.imageUrl.startsWith("http://");
    }
    
    private void loadPipelined() {
        try {
            final HttpRequest httprequest = HttpPipeline.makeRequest(this.imageUrl, Minecraft.getMinecraft().getProxy());
            final HttpResponse httpresponse = HttpPipeline.executeRequest(httprequest);
            if (httpresponse.getStatus() / 100 != 2) {
                return;
            }
            final byte[] abyte = httpresponse.getBody();
            final ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(abyte);
            BufferedImage bufferedimage;
            if (this.cacheFile != null) {
                FileUtils.copyInputStreamToFile(bytearrayinputstream, this.cacheFile);
                bufferedimage = ImageIO.read(this.cacheFile);
            }
            else {
                bufferedimage = TextureUtil.readBufferedImage(bytearrayinputstream);
            }
            if (this.imageBuffer != null) {
                bufferedimage = this.imageBuffer.parseUserSkin(bufferedimage);
            }
            this.setBufferedImage(bufferedimage);
        }
        catch (Exception exception) {
            ThreadDownloadImageData.logger.error("Couldn't download http texture: " + exception.getClass().getName() + ": " + exception.getMessage());
            return;
        }
        finally {
            this.imageFound = (this.bufferedImage != null);
        }
        this.imageFound = (this.bufferedImage != null);
    }
}
