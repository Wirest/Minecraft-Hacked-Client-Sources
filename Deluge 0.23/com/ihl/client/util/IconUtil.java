package com.ihl.client.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class IconUtil {

    public static ByteBuffer[] fav() {
        InputStream[] streams = new InputStream[] {
                IconUtil.class.getResourceAsStream("/assets/minecraft/client/icon16.png"),
                IconUtil.class.getResourceAsStream("/assets/minecraft/client/icon32.png"),
                IconUtil.class.getResourceAsStream("/assets/minecraft/client/icon48.png"),
                IconUtil.class.getResourceAsStream("/assets/minecraft/client/icon64.png"),
                IconUtil.class.getResourceAsStream("/assets/minecraft/client/icon128.png")
        };
        ByteBuffer[] buffer = new ByteBuffer[streams.length];

        try {
            for(int i = 0; i < streams.length; i++) {
                buffer[i] = readImageToBuffer(streams[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return buffer;
    }

    public static ByteBuffer readImageToBuffer(InputStream imageStream) throws IOException
    {
        BufferedImage var2 = ImageIO.read(imageStream);
        int[] var3 = var2.getRGB(0, 0, var2.getWidth(), var2.getHeight(), (int[])null, 0, var2.getWidth());
        ByteBuffer var4 = ByteBuffer.allocate(4 * var3.length);
        int[] var5 = var3;
        int var6 = var3.length;

        for (int var7 = 0; var7 < var6; ++var7)
        {
            int var8 = var5[var7];
            var4.putInt(var8 << 8 | var8 >> 24 & 255);
        }

        var4.flip();
        return var4;
    }

}
