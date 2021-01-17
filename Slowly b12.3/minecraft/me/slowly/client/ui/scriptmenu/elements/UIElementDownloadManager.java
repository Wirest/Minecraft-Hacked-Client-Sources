package me.slowly.client.ui.scriptmenu.elements;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import me.slowly.client.Client;
import me.slowly.client.util.Colors;
import me.slowly.client.util.FlatColors;
import me.slowly.client.util.RenderUtil;
import me.slowly.client.util.fontmanager.UnicodeFontRenderer;
import net.minecraft.client.renderer.RenderHelper;

public class UIElementDownloadManager {
   private double percent;
   private double animationX;
   private boolean downloading;

   public void draw(int x, int y, int mouseX, int mouseY, int width) {
      int height = 11;
      this.animationX = RenderUtil.getAnimationState(this.animationX, this.percent / 100.0D, 10.0D);
      RenderHelper.drawBorderedRect((float)x, (float)y, (float)(x + width), (float)(y + height), 0.5F, Colors.GREY.c, Colors.BLACK.c);
      RenderHelper.drawRect((float)x, (float)y, (float)((double)x + (double)width * this.animationX), (float)(y + height), FlatColors.BLUE.c);
      UnicodeFontRenderer font = Client.getInstance().getFontManager().consolasbold14;
      font.drawCenteredString(String.valueOf(this.percent) + "%", (float)(x + width / 2), (float)(y + height / 4), -1);
   }

   public void download(String urlStr, String dir) throws IOException {
      if (!this.downloading) {
         this.percent = 0.0D;
         this.animationX = 0.0D;
         BufferedInputStream in = null;
         FileOutputStream out = null;

         try {
            URL url = new URL(urlStr);
            URLConnection conn = url.openConnection();
            int size = conn.getContentLength();
            in = new BufferedInputStream(url.openStream());
            out = new FileOutputStream(dir);
            byte[] data = new byte[1024];
            double sumCount = 0.0D;
            this.downloading = true;

            int count;
            while((count = in.read(data, 0, 1024)) != -1) {
               out.write(data, 0, count);
               sumCount += (double)count;
               if (size > 0) {
                  this.percent = sumCount / (double)size * 100.0D;
               }
            }

            this.downloading = false;
         } catch (MalformedURLException var27) {
            var27.printStackTrace();
            this.downloading = false;
         } catch (IOException var28) {
            var28.printStackTrace();
            this.downloading = false;
         } finally {
            if (in != null) {
               try {
                  in.close();
               } catch (IOException var26) {
                  var26.printStackTrace();
               }
            }

            if (out != null) {
               try {
                  out.close();
               } catch (IOException var25) {
                  var25.printStackTrace();
               }
            }

         }

      }
   }
}
