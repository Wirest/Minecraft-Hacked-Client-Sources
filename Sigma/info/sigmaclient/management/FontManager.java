package info.sigmaclient.management;

import info.sigmaclient.util.render.TTFFontRenderer;
import info.sigmaclient.util.render.TextureData;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

public class FontManager {

    private ResourceLocation darrow = new ResourceLocation("SF-UI-Display-Regular.otf");

    private TTFFontRenderer defaultFont;

    public FontManager getInstance() {
        return instance;
    }

    public TTFFontRenderer getFont(String key) {
        return fonts.getOrDefault(key, defaultFont);
    }

    private FontManager instance;

    private HashMap<String, TTFFontRenderer> fonts = new HashMap<>();

    public FontManager() {
        instance = this;
        ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(8);
        ConcurrentLinkedQueue<TextureData> textureQueue = new ConcurrentLinkedQueue<>();
        defaultFont = new TTFFontRenderer(executorService, textureQueue, new Font("Verdana", Font.PLAIN, 18));
        try {
            for (int i : new int[]{6, 7, 8, 10, 11, 12, 14}) {
                InputStream istream = getClass().getResourceAsStream("/assets/minecraft/SF-UI-Display-Regular.otf");
                Font myFont = Font.createFont(Font.PLAIN, istream);
                myFont = myFont.deriveFont(Font.PLAIN, i);
                fonts.put("SFR " + i, new TTFFontRenderer(executorService, textureQueue, myFont));
            }
            for (int i : new int[]{6, 7, 8,9, 11}) {
                InputStream istream = getClass().getResourceAsStream("/assets/minecraft/SF-UI-Display-Bold.otf");
                Font myFont = Font.createFont(Font.PLAIN, istream);
                myFont = myFont.deriveFont(Font.PLAIN, i);
                fonts.put("SFB " + i, new TTFFontRenderer(executorService, textureQueue, myFont));
            }
            for (int i : new int[]{6, 7, 8, 9, 11, 12}) {
                InputStream istream = getClass().getResourceAsStream("/assets/minecraft/SF-UI-Display-Medium.otf");
                Font myFont = Font.createFont(Font.PLAIN, istream);
                myFont = myFont.deriveFont(Font.PLAIN, i);
                fonts.put("SFM " + i, new TTFFontRenderer(executorService, textureQueue, myFont));
            }
            for (int i : new int[]{17, 10, 9, 8, 7, 6}) {
                InputStream istream = getClass().getResourceAsStream("/assets/minecraft/SF-UI-Display-Light.otf");
                Font myFont = Font.createFont(Font.PLAIN, istream);
                myFont = myFont.deriveFont(Font.PLAIN, i);
                fonts.put("SFL " + i, new TTFFontRenderer(executorService, textureQueue, myFont));
            }
            for (int i : new int[]{19}) {
                InputStream istream = getClass().getResourceAsStream("/assets/minecraft/Jigsaw-Regular.otf");
                Font myFont = Font.createFont(Font.PLAIN, istream);
                myFont = myFont.deriveFont(Font.PLAIN, i);
                fonts.put("JIGR " + i, new TTFFontRenderer(executorService, textureQueue, myFont));
            }
            fonts.put("Verdana 12", new TTFFontRenderer(executorService, textureQueue, new Font("Verdana", Font.PLAIN, 12)));

            fonts.put("Verdana Bold 16", new TTFFontRenderer(executorService, textureQueue, new Font("Verdana Bold", Font.PLAIN, 16)));
            fonts.put("Verdana Bold 20", new TTFFontRenderer(executorService, textureQueue, new Font("Verdana Bold", Font.PLAIN, 20)));
        } catch (Exception ignored) {

        }
        executorService.shutdown();
        while (!executorService.isTerminated()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (!textureQueue.isEmpty()) {
                TextureData textureData = textureQueue.poll();
                GlStateManager.bindTexture(textureData.getTextureId());

                // Sets the texture parameter stuff.
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

                // Uploads the texture to opengl.
                GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, textureData.getWidth(), textureData.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, textureData.getBuffer());
            }
        }
    }
}
