package optifine;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.FilenameUtils;

public class CapeUtils {
    public static void downloadCape(final AbstractClientPlayer player) {
        String username = player.getNameClear();

        if (username != null && !username.isEmpty()) {
            String ofCapeUrl = "http://s.optifine.net/capes/" + username + ".png";
            String mptHash = FilenameUtils.getBaseName(ofCapeUrl);
            final ResourceLocation rl = new ResourceLocation("capeof/" + mptHash);
            TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
            ITextureObject tex = textureManager.getTexture(rl);

            if (tex != null && tex instanceof ThreadDownloadImageData) {
                ThreadDownloadImageData thePlayer = (ThreadDownloadImageData) tex;

                if (thePlayer.imageFound != null) {
                    if (thePlayer.imageFound.booleanValue()) {
                        player.setLocationOfCape(rl);
                    }

                    return;
                }
            }

            IImageBuffer iib = new IImageBuffer() {
                ImageBufferDownload ibd = new ImageBufferDownload();

                public BufferedImage parseUserSkin(BufferedImage var1) {
                    return CapeUtils.parseCape(var1);
                }

                public void func_152634_a() {
                    player.setLocationOfCape(rl);
                }
            };
            ThreadDownloadImageData textureCape = new ThreadDownloadImageData((File) null, ofCapeUrl, (ResourceLocation) null, iib);
            textureCape.pipeline = true;
            textureManager.loadTexture(rl, textureCape);
        }
    }

    public static BufferedImage parseCape(BufferedImage img) {
        int imageWidth = 64;
        int imageHeight = 32;
        int srcWidth = img.getWidth();

        for (int srcHeight = img.getHeight(); imageWidth < srcWidth || imageHeight < srcHeight; imageHeight *= 2) {
            imageWidth *= 2;
        }

        BufferedImage imgNew = new BufferedImage(imageWidth, imageHeight, 2);
        Graphics g = imgNew.getGraphics();
        g.drawImage(img, 0, 0, (ImageObserver) null);
        g.dispose();
        return imgNew;
    }
}
