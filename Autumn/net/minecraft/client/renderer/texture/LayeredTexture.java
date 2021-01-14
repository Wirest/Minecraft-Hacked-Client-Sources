package net.minecraft.client.renderer.texture;

import com.google.common.collect.Lists;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LayeredTexture extends AbstractTexture {
   private static final Logger logger = LogManager.getLogger();
   public final List layeredTextureNames;

   public LayeredTexture(String... textureNames) {
      this.layeredTextureNames = Lists.newArrayList(textureNames);
   }

   public void loadTexture(IResourceManager resourceManager) throws IOException {
      this.deleteGlTexture();
      BufferedImage bufferedimage = null;

      try {
         Iterator var3 = this.layeredTextureNames.iterator();

         while(var3.hasNext()) {
            String s = (String)var3.next();
            if (s != null) {
               InputStream inputstream = resourceManager.getResource(new ResourceLocation(s)).getInputStream();
               BufferedImage bufferedimage1 = TextureUtil.readBufferedImage(inputstream);
               if (bufferedimage == null) {
                  bufferedimage = new BufferedImage(bufferedimage1.getWidth(), bufferedimage1.getHeight(), 2);
               }

               bufferedimage.getGraphics().drawImage(bufferedimage1, 0, 0, (ImageObserver)null);
            }
         }
      } catch (IOException var7) {
         logger.error("Couldn't load layered image", var7);
         return;
      }

      TextureUtil.uploadTextureImage(this.getGlTextureId(), bufferedimage);
   }
}
