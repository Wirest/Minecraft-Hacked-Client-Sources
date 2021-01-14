package de.iotacb.client.minimap.interfaces;

import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import de.iotacb.client.minimap.Minimap;
import de.iotacb.client.minimap.XaeroMinimap;
import de.iotacb.client.minimap.settings.ModOptions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;

public class InterfaceHandler
{
    public static int actionTimer;
    public static ArrayList<Preset> presets;
    public static ArrayList<Interface> list;
    public static final ResourceLocation invtextures;
    public static Minecraft mc;
    public static int selectedId;
    public static int draggingId;
    public static int draggingOffX;
    public static int draggingOffY;
    public static Minimap minimap;
    public static final ResourceLocation guiTextures;
    public static final ResourceLocation mapTextures;
    public static long lastFlip;
    public static final Color disabled;
    public static final Color enabled;
    public static final Color selected;
    
    public static void loadPresets() {
        new Preset("gui.xaero_preset_topleft", new int[][] { { 0, 0 }, { 0, 10000 }, { 0, 0 }, { 0, 36 }, { 0, 0 } }, new boolean[][] { { true, false }, { true, false }, { false, true }, { true, false }, { false, false } });
        new Preset("gui.xaero_preset_topright", new int[][] { { 0, 0 }, { 0, 135 }, { 120, 0 }, { 0, 50 }, { 0, 0 } }, new boolean[][] { { false, true }, { false, false }, { true, false }, { true, false }, { false, true } });
        new Preset("gui.xaero_preset_bottom_left", new int[][] { { 0, 0 }, { 0, 135 }, { 120, 0 }, { 0, 50 }, { 0, 10000 } }, new boolean[][] { { false, true }, { false, false }, { true, false }, { true, false }, { false, false } });
        new Preset("gui.xaero_preset_bottom_right", new int[][] { { 0, 0 }, { 0, 135 }, { 120, 0 }, { 0, 50 }, { 0, 10000 } }, new boolean[][] { { false, true }, { false, false }, { true, false }, { true, false }, { false, true } });
    }
    
    public static void load() {
        new Interface("", 0, 0, ModOptions.DEFAULT) {};
        new Interface("", 0, 0, ModOptions.DEFAULT) {};
        new Interface("", 0, 0, ModOptions.DEFAULT) {};
        new Interface("", 0, 0, ModOptions.DEFAULT) {};
        InterfaceHandler.minimap = new Minimap(new Interface("gui.xaero_minimap", 128, 128, ModOptions.MINIMAP) {
            long lastFBOTry = 0L;
            int lastMinimapSize = 0;
            private ArrayList<String> underText = new ArrayList<String>();
            
            @Override
            public int getW(final int scale) {
                return this.getSize() / scale;
            }
            
            @Override
            public int getH(final int scale) {
                return this.getW(scale);
            }
            
            @Override
            public int getWC(final int scale) {
                return this.getW(scale);
            }
            
            @Override
            public int getHC(final int scale) {
                return this.getH(scale);
            }
            
            @Override
            public int getW0(final int scale) {
                return this.getW(scale);
            }
            
            @Override
            public int getH0(final int scale) {
                return this.getH(scale);
            }
            
            @Override
            public int getSize() {
                return InterfaceHandler.minimap.getMinimapWidth() + 36 + 2;
            }
            
            public void translatePosition(final int specW, final int specH, final double ps, final double pc, final double offx, final double offy) {
                final double Y = (pc * offx + ps * offy) * Minimap.zoom;
                double borderedX;
                final double X = borderedX = (ps * offx - pc * offy) * Minimap.zoom;
                double borderedY = Y;
                if (borderedX > specW) {
                    borderedX = specW;
                    borderedY = Y * specW / X;
                }
                else if (borderedX < -specW) {
                    borderedX = -specW;
                    borderedY = -Y * specW / X;
                }
                if (borderedY > specH) {
                    borderedY = specH;
                    borderedX = X * specH / Y;
                }
                else if (borderedY < -specH) {
                    borderedY = -specH;
                    borderedX = -X * specH / Y;
                }
                GL11.glPushMatrix();
                GlStateManager.translate(borderedX, borderedY, 0.0);
            }
            
            @Override
            public void drawInterface(final int width, final int height, final int scale, final float partial) {
                if (Minimap.loadedFBO && !OpenGlHelper.isFramebufferEnabled()) {
                    Minimap.loadedFBO = false;
                    Minimap.scalingFrameBuffer.deleteFramebuffer();
                    Minimap.rotationFrameBuffer.deleteFramebuffer();
                    Minimap.resetImage();
                }
                if (!Minimap.loadedFBO && !XaeroMinimap.getSettings().mapSafeMode && System.currentTimeMillis() - this.lastFBOTry > 1000L) {
                    this.lastFBOTry = System.currentTimeMillis();
                    Minimap.loadFrameBuffer();
                }
                if (XaeroMinimap.getSettings().getMinimapSize() != this.lastMinimapSize) {
                    this.lastMinimapSize = XaeroMinimap.getSettings().getMinimapSize();
                    Minimap.resetImage();
                    Minimap.frameUpdateNeeded = Minimap.usingFBO();
                }
                final long before = System.currentTimeMillis();
                int bufferSize;
                if (Minimap.usingFBO()) {
                    bufferSize = InterfaceHandler.minimap.getFBOBufferSize();
                }
                else {
                    bufferSize = InterfaceHandler.minimap.getBufferSize();
                }
                final float mapScale = scale / 2.0f;
                final Minimap minimap = InterfaceHandler.minimap;
                final int minimapWidth = InterfaceHandler.minimap.getMinimapWidth();
                minimap.minimapWidth = minimapWidth;
                final int mapW = minimapWidth;
                final Minimap minimap2 = InterfaceHandler.minimap;
                final int minimapHeight = mapW;
                minimap2.minimapHeight = minimapHeight;
                final int mapH = minimapHeight;
                Minimap.frameUpdatePartialTicks = partial;
                InterfaceHandler.minimap.updateZoom();
                RenderHelper.disableStandardItemLighting();
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                float sizeFix = bufferSize / 512.0f;
                if (Minimap.usingFBO()) {
                    InterfaceHandler.minimap.renderFrameToFBO(bufferSize, mapW, sizeFix, partial, true);
                    Minecraft.getMinecraft().getFramebuffer().bindFramebuffer(true);
                    Minimap.rotationFrameBuffer.bindFramebufferTexture();
                    sizeFix = 1.0f;
                }
                else {
                    InterfaceHandler.minimap.updateMapFrame(bufferSize, partial);
                    GL11.glScalef(sizeFix, sizeFix, 1.0f);
                    Minimap.bindTextureBuffer(Minimap.mapTexture.getBuffer(bufferSize), bufferSize, bufferSize, Minimap.mapTexture.getGlTextureId());
                    GL11.glColor4f(1.0f, 1.0f, 1.0f, XaeroMinimap.getSettings().minimapOpacity / 100.0f);
                }
                GL11.glEnable(3008);
                GlStateManager.enableAlpha();
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(770, 771);
                GL11.glScalef(1.0f / mapScale, 1.0f / mapScale, 1.0f);
                final int scaledX = (int)(this.x * mapScale);
                final int scaledY = (int)(this.y * mapScale);
                InterfaceHandler.mc.ingameGUI.drawTexturedModalRect((int)((scaledX + 9) / sizeFix), (int)((scaledY + 9) / sizeFix), 0, 0, (int)((mapW / 2 + 1) / sizeFix), (int)((mapH / 2 + 1) / sizeFix));
                if (!Minimap.usingFBO()) {
                    GL11.glScalef(1.0f / sizeFix, 1.0f / sizeFix, 1.0f);
                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                }
                final int specH;
                final int specW = specH = mapW / 2 + 6;
                final double angle = Math.toRadians(Minimap.getRenderAngle());
                final double ps = Math.sin(3.141592653589793 - angle);
                final double pc = Math.cos(3.141592653589793 - angle);
                GL11.glPushMatrix();
                GlStateManager.scale(0.5f, 0.5f, 1.0f);
                GlStateManager.translate((float)(2 * scaledX + specW - 6 + 18), (float)(2 * scaledY + specH - 6 + 18), 0.0f);
                GL11.glPopMatrix();
                if (XaeroMinimap.getSettings().getShowCoords()) {
                    final int interfaceSize = this.getSize() / 2;
                    String coords = Minimap.myFloor(InterfaceHandler.mc.thePlayer.posX) + ", " + Minimap.myFloor(InterfaceHandler.mc.thePlayer.posY) + ", " + Minimap.myFloor(InterfaceHandler.mc.thePlayer.posZ);
                    if (InterfaceHandler.mc.fontRendererObj.getStringWidth(coords) >= interfaceSize) {
                        final String stringLevel = "" + Minimap.myFloor(InterfaceHandler.mc.thePlayer.posY);
                        coords = Minimap.myFloor(InterfaceHandler.mc.thePlayer.posX) + ", " + Minimap.myFloor(InterfaceHandler.mc.thePlayer.posZ);
                        this.underText.add(coords);
                        this.underText.add(stringLevel);
                    }
                    else {
                        this.underText.add(coords);
                    }
                }
                if (XaeroMinimap.getSettings().showBiome) {
                    final BlockPos pos = new BlockPos(InterfaceHandler.mc.getRenderViewEntity().posX, InterfaceHandler.mc.getRenderViewEntity().getEntityBoundingBox().minY, InterfaceHandler.mc.getRenderViewEntity().posZ);
                    this.underText.add(InterfaceHandler.mc.theWorld.getChunkFromBlockCoords(pos).getBiome(pos, InterfaceHandler.mc.theWorld.getWorldChunkManager()).biomeName);
                }
                GL11.glScalef(mapScale, mapScale, 1.0f);
                super.drawInterface(width, height, scale, partial);
            }
            
            private void drawArrow(final double arrowX, final double arrowY, final float[] colour) {
                GL11.glPushMatrix();
                GL11.glTranslated(arrowX, arrowY, 0.0);
                GlStateManager.rotate(InterfaceHandler.mc.thePlayer.rotationYaw, 0.0f, 0.0f, 1.0f);
                GL11.glScalef(0.5f * XaeroMinimap.getSettings().arrowScale, 0.5f * XaeroMinimap.getSettings().arrowScale, 1.0f);
                GL11.glTranslated(-13.0, -6.0, 0.0);
                GL11.glColor4f(colour[0], colour[1], colour[2], colour[3]);
                InterfaceHandler.mc.ingameGUI.drawTexturedModalRect(0, 0, 49, 0, 26, 27);
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                GL11.glPopMatrix();
            }
            
            public void drawTextUnderMinimap(final int scaledX, final int scaledY, final int height, final float mapScale) {
                final int interfaceSize = this.getSize() / 2;
                final int scaledHeight = (int)(height * mapScale);
                for (int i = 0; i < this.underText.size(); ++i) {
                    final String s = this.underText.get(i);
                    final int stringWidth = InterfaceHandler.mc.fontRendererObj.getStringWidth(s);
                    final boolean under = scaledY + interfaceSize / 2 < scaledHeight / 2;
                    final int stringY = scaledY + (under ? interfaceSize : -9) + i * 9 * (under ? 1 : -1);
                    Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(s, (float)(scaledX + interfaceSize / 2 - stringWidth / 2), (float)stringY, Minimap.radarPlayers.hashCode());
                }
                this.underText.clear();
            }
            
            
            private void drawCompass(final int specW, final int specH, final double ps, final double pc) {
                final String[] nesw = { "N", "E", "S", "W" };
                for (int i = 0; i < 4; ++i) {
                    final double offx = (i == 0 || i == 2) ? 0.0 : ((i == 1) ? 10000 : -10000);
                    final double offy = (i == 1 || i == 3) ? 0.0 : ((i == 2) ? 10000 : -10000);
                    this.translatePosition(specW, specH, ps, pc, offx, offy);
                    GlStateManager.scale(2.0f, 2.0f, 1.0f);
                    Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(nesw[i], (float)(1 - InterfaceHandler.mc.fontRendererObj.getStringWidth(nesw[i]) / 2), -3.0f, Minimap.radarPlayers.hashCode());
                    GL11.glPopMatrix();
                }
            }
        });
    }
    
    public static void drawInterfaces(final float partial) {
        final ScaledResolution scaledresolution = new ScaledResolution(XaeroMinimap.mc);
        final int width = scaledresolution.getScaledWidth();
        final int height = scaledresolution.getScaledHeight();
        final int scale = scaledresolution.getScaleFactor();
        final int mouseX = Mouse.getX() * width / XaeroMinimap.mc.displayWidth;
        final int mouseY = height - Mouse.getY() * height / XaeroMinimap.mc.displayHeight - 1;
        for (final Interface l : InterfaceHandler.list) {
            if (XaeroMinimap.settings.getBooleanValue(l.option)) {
            	l.drawInterface(width, height, scale, partial);
            }
        }
    }
    
    public static void confirm() {
        for (final Interface l : InterfaceHandler.list) {
            l.bx = l.actualx;
            l.by = l.actualy;
            l.bcentered = l.centered;
            l.bflipped = l.flipped;
            l.bfromRight = l.fromRight;
        }
    }
    
    public static void cancel() {
        for (final Interface l : InterfaceHandler.list) {
            l.actualx = l.bx;
            l.actualy = l.by;
            l.centered = l.bcentered;
            l.flipped = l.bflipped;
            l.fromRight = l.bfromRight;
        }
    }
    
    public static void applyPreset(final int id) {
        for (final Interface l : InterfaceHandler.list) {
            InterfaceHandler.actionTimer = 10;
            l.actualx = InterfaceHandler.presets.get(id).coords[l.id][0];
            l.actualy = InterfaceHandler.presets.get(id).coords[l.id][1];
            l.centered = InterfaceHandler.presets.get(id).types[l.id][0];
            l.flipped = l.cflipped;
            l.fromRight = InterfaceHandler.presets.get(id).types[l.id][1];
        }
    }
    
    static {
        InterfaceHandler.actionTimer = 0;
        InterfaceHandler.presets = new ArrayList<Preset>();
        InterfaceHandler.list = new ArrayList<Interface>();
        invtextures = new ResourceLocation("textures/gui/container/inventory.png");
        InterfaceHandler.mc = XaeroMinimap.mc;
        InterfaceHandler.selectedId = -1;
        InterfaceHandler.draggingId = -1;
        InterfaceHandler.draggingOffX = 0;
        InterfaceHandler.draggingOffY = 0;
        guiTextures = new ResourceLocation("xaerobetterpvp", "gui/guis.png");
        mapTextures = new ResourceLocation("xaeromaptexture");
        InterfaceHandler.lastFlip = 0L;
        disabled = new Color(189, 189, 189, 80);
        enabled = new Color(255, 255, 255, 100);
        selected = new Color(255, 255, 255, 130);
    }
}
