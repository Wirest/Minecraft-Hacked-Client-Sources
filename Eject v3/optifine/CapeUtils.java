package optifine;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.FilenameUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;

public class CapeUtils {
    public static void downloadCape(AbstractClientPlayer paramAbstractClientPlayer) {
        String str1 = paramAbstractClientPlayer.getNameClear();
        if ((str1 != null) && (!str1.isEmpty())) {
            String str2 = "http://s.optifine.net/capes/" + str1 + ".png";
            String str3 = FilenameUtils.getBaseName(str2);
            final ResourceLocation localResourceLocation = new ResourceLocation("capeof/" + str3);
            TextureManager localTextureManager = Minecraft.getMinecraft().getTextureManager();
            ITextureObject localITextureObject = localTextureManager.getTexture(localResourceLocation);
            if ((localITextureObject != null) && ((localITextureObject instanceof ThreadDownloadImageData))) {
                localObject = (ThreadDownloadImageData) localITextureObject;
                if (((ThreadDownloadImageData) localObject).imageFound != null) {
                    if (((ThreadDownloadImageData) localObject).imageFound.booleanValue()) {
                        paramAbstractClientPlayer.setLocationOfCape(localResourceLocation);
                    }
                    return;
                }
            }
            Object localObject = new IImageBuffer() {
                ImageBufferDownload ibd = new ImageBufferDownload();

                public BufferedImage parseUserSkin(BufferedImage paramAnonymousBufferedImage) {
                    return CapeUtils.parseCape(paramAnonymousBufferedImage);
                }

                public void skinAvailable() {
                    this.val$p_downloadCape_0_.setLocationOfCape(localResourceLocation);
                }
            };
            ThreadDownloadImageData localThreadDownloadImageData = new ThreadDownloadImageData((File) null, str2, (ResourceLocation) null, (IImageBuffer) localObject);
            localThreadDownloadImageData.pipeline = true;
            localTextureManager.loadTexture(localResourceLocation, localThreadDownloadImageData);
        }
    }

    public static BufferedImage parseCape(BufferedImage paramBufferedImage) {
        int i = 64;
        int j = 32;
        int k = paramBufferedImage.getWidth();
        int m = paramBufferedImage.getHeight();
        while ((i < k) || (j < m)) {
            i *= 2;
            j *= 2;
        }
        BufferedImage localBufferedImage = new BufferedImage(i, j, 2);
        Graphics localGraphics = localBufferedImage.getGraphics();
        localGraphics.drawImage(paramBufferedImage, 0, 0, (ImageObserver) null);
        localGraphics.dispose();
        return localBufferedImage;
    }
}




