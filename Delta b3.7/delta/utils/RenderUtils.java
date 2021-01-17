/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  delta.OVYt$968L
 *  net.minecraft.block.Block
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityClientPlayerMP
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.renderer.RenderGlobal
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.ThreadDownloadImageData
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.client.renderer.texture.DynamicTexture
 *  net.minecraft.client.renderer.texture.ITextureObject
 *  net.minecraft.client.settings.GameSettings
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.util.AxisAlignedBB
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.Timer
 *  net.minecraft.world.World
 *  org.lwjgl.opengl.GL11
 */
package delta.utils;

import com.google.common.collect.Maps;
import delta.Class172;
import delta.OVYt;
import delta.utils.BoundingBox;
import delta.utils.Wrapper;
import java.awt.Color;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.imageio.ImageIO;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Timer;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class RenderUtils {
    private static String[] score$;
    private static Minecraft things$;
    private static Field selected$;
    private static Map<Class172, DynamicTexture> tribunal$;
    private static final Map<Integer, Boolean> exists$;
    public static Map<UUID, ResourceLocation> johnson$;

    public static void _might(double d, Color color, double d2) {
        double d3 = d - RenderManager.renderPosY;
        RenderUtils._lawrence(AxisAlignedBB.getBoundingBox((double)d2, (double)(d3 + 0.02), (double)d2, (double)(-d2), (double)d3, (double)(-d2)), color);
    }

    private static DynamicTexture _defining(Class172 class172) {
        try {
            return new DynamicTexture(ImageIO.read(class172._horror()));
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
            return null;
        }
    }

    public static ResourceLocation _dance() {
        if (johnson$ == null) {
            johnson$ = new HashMap<UUID, ResourceLocation>();
        }
        if (johnson$.containsKey(things$.getSession().func_148256_e().getId())) {
            return johnson$.get(things$.getSession().func_148256_e().getId());
        }
        ResourceLocation resourceLocation = new ResourceLocation(OVYt.968L.FS1x((String)score$[1], (int)-737134791) + things$.getSession().func_148256_e().getName());
        Object[] arrobject = new Object[178 - 254 + 149 + -72];
        arrobject[128 - 129 + 115 - 68 + -46] = things$.getSession().func_148256_e().getName();
        ThreadDownloadImageData threadDownloadImageData = new ThreadDownloadImageData(null, String.format(OVYt.968L.FS1x((String)score$[2], (int)1028780783), arrobject), null, null);
        Minecraft.getMinecraft().getTextureManager().loadTexture(resourceLocation, (ITextureObject)threadDownloadImageData);
        johnson$.put(things$.getSession().func_148256_e().getId(), resourceLocation);
        return resourceLocation;
    }

    private static void _agent() {
        score$ = new String[]{"\u631d\u6314\u6334\u6313\u6301\u6306\u6320\u6317\u631c\u6316\u6317\u6300", "\u3751\u375c\u3758\u375d\u374a\u3716", "\uf287\uf29b\uf29b\uf29f\uf29c\uf2d5\uf2c0\uf2c0\uf282\uf286\uf281\uf280\uf29b\uf28e\uf29d\uf2c1\uf281\uf28a\uf29b\uf2c0\uf287\uf28a\uf283\uf282\uf2c0\uf2ca\uf29c\uf2c0\uf2d9\uf2db\uf2c1\uf29f\uf281\uf288"};
    }

    public static int _being(Class172 class172) {
        return tribunal$.computeIfAbsent(class172, RenderUtils::_defining).func_110552_b();
    }

    public static int _immune(Color color) {
        return color.getRGB();
    }

    public static void _showing() {
        GL11.glEnable((int)(77 - 89 + 5 + 3560));
        GL11.glDisable((int)(162 - 163 + 65 - 53 + 3031));
        GL11.glEnable((int)(217 - 299 + 158 + 2853));
        GL11.glDisable((int)(162 - 321 + 250 + 2757));
        GL11.glHint((int)(228 - 282 + 157 - 81 + 3132), (int)(185 - 304 + 42 + 4429));
        GL11.glHint((int)(179 - 353 + 143 + 3186), (int)(34 - 51 + 6 - 6 + 4369));
    }

    public static void _matrix(BoundingBox boundingBox, Color color, boolean bl) {
        Timer timer = Wrapper.timer;
        double d = boundingBox._talented() - RenderManager.renderPosX;
        double d2 = boundingBox._adelaide() - RenderManager.renderPosY;
        double d3 = boundingBox._produce() - RenderManager.renderPosZ;
        AxisAlignedBB axisAlignedBB = AxisAlignedBB.getBoundingBox((double)d, (double)d2, (double)d3, (double)(d + 1.0), (double)(d2 + 1.0), (double)(d3 + 1.0));
        Block block = boundingBox._maria();
        if (block != null) {
            EntityClientPlayerMP entityClientPlayerMP = RenderUtils.things$.thePlayer;
            double d4 = entityClientPlayerMP.field_70142_S + (entityClientPlayerMP.field_70165_t - entityClientPlayerMP.field_70142_S) * (double)timer.renderPartialTicks;
            double d5 = entityClientPlayerMP.field_70137_T + (entityClientPlayerMP.field_70163_u - entityClientPlayerMP.field_70137_T) * (double)timer.renderPartialTicks;
            double d6 = entityClientPlayerMP.field_70136_U + (entityClientPlayerMP.field_70161_v - entityClientPlayerMP.field_70136_U) * (double)timer.renderPartialTicks;
            axisAlignedBB = block.getSelectedBoundingBoxFromPool((World)RenderUtils.things$.theWorld, (int)boundingBox._talented(), (int)boundingBox._adelaide(), (int)boundingBox._produce()).expand((double)0.002f, (double)0.002f, (double)0.002f).offset(-d4, -d5, -d6);
        }
        GL11.glBlendFunc((int)(98 - 117 + 21 + 768), (int)(233 - 401 + 375 + 564));
        RenderUtils._profiles(46 - 76 + 26 - 6 + 3052);
        int[] arrn = new int[133 - 140 + 51 + -42];
        arrn[76 - 151 + 101 - 86 + 60] = 200 - 284 + 46 - 19 + 3610;
        arrn[196 - 258 + 232 - 196 + 27] = 22 - 35 + 30 + 2912;
        RenderUtils._crest(arrn);
        GL11.glDepthMask((boolean)(170 - 261 + 199 + -108));
        GL11.glColor4d((double)(color.getRed() / (92 - 109 + 9 - 1 + 264)), (double)(color.getGreen() / (194 - 353 + 57 - 6 + 363)), (double)(color.getBlue() / (136 - 222 + 204 + 137)), (double)(color.getAlpha() != 60 - 95 + 71 + 219 ? (double)(color.getAlpha() / (215 - 395 + 155 - 148 + 428)) : (bl ? 26.0 : 35.0)));
        RenderUtils._reaching(axisAlignedBB);
        if (bl) {
            GL11.glLineWidth((float)1.0f);
            RenderUtils._profiles(97 - 124 + 41 + 2834);
            GL11.glColor4d((double)(color.getRed() / (216 - 335 + 23 - 15 + 366)), (double)(color.getGreen() / (138 - 153 + 146 - 128 + 252)), (double)(color.getBlue() / (61 - 62 + 58 + 198)), (double)(color.getAlpha() / (231 - 434 + 130 + 328)));
            RenderGlobal.drawOutlinedBoundingBox((AxisAlignedBB)axisAlignedBB, (int)color.getRGB());
        }
        GL11.glDepthMask((boolean)(139 - 243 + 129 - 112 + 88));
        RenderUtils._brave();
    }

    public static void _helpful() {
        GL11.glDisable((int)(76 - 143 + 72 - 42 + 2966));
        GL11.glEnable((int)(202 - 403 + 157 - 41 + 3127));
        GL11.glDisable((int)(220 - 389 + 56 - 7 + 3673));
        GL11.glBlendFunc((int)(169 - 179 + 46 - 15 + 749), (int)(265 - 336 + 177 + 665));
        GL11.glDepthMask((boolean)(174 - 228 + 78 - 61 + 38));
        GL11.glEnable((int)(61 - 101 + 46 + 2842));
        GL11.glHint((int)(60 - 108 + 72 - 49 + 3179), (int)(87 - 128 + 31 - 12 + 4376));
        GL11.glHint((int)(187 - 308 + 122 + 3154), (int)(251 - 353 + 274 - 14 + 4196));
    }

    public static void _bolivia(double d, double d2, double d3, double d4, int n) {
        Gui.drawRect((int)((int)d), (int)((int)d2), (int)((int)d + (int)d3), (int)((int)d2 + (int)d4), (int)n);
    }

    public static void _terror(EntityLivingBase entityLivingBase, float f) {
        GL11.glPushMatrix();
        GL11.glTranslated((double)((double)((float)entityLivingBase.field_70142_S) + (entityLivingBase.field_70165_t - entityLivingBase.field_70142_S) * (double)f - RenderManager.renderPosX), (double)(entityLivingBase.field_70137_T + (entityLivingBase.field_70163_u - entityLivingBase.field_70137_T) * (double)f - RenderManager.renderPosY), (double)(entityLivingBase.field_70136_U + (entityLivingBase.field_70161_v - entityLivingBase.field_70136_U) * (double)f - RenderManager.renderPosZ));
        GL11.glRotated((double)(-RenderUtils.things$.thePlayer.field_70177_z), (double)0.0, (double)1.0, (double)0.0);
        GL11.glRotated((double)RenderUtils.things$.thePlayer.field_70125_A, (double)1.0, (double)0.0, (double)0.0);
        GL11.glScaled((double)-0.018f, (double)-0.018f, (double)0.018f);
        GL11.glDisable((int)(188 - 347 + 303 - 293 + 3078));
        GL11.glDisable((int)(86 - 126 + 98 + 2838));
    }

    public static void _alike(Entity entity, Color color, boolean bl) {
        Timer timer = Wrapper.timer;
        GL11.glBlendFunc((int)(206 - 340 + 87 + 817), (int)(128 - 226 + 208 - 33 + 694));
        RenderUtils._profiles(152 - 254 + 62 - 7 + 3089);
        int[] arrn = new int[212 - 226 + 77 - 44 + -17];
        arrn[132 - 249 + 108 + 9] = 236 - 258 + 179 + 3396;
        arrn[251 - 381 + 79 - 4 + 56] = 218 - 360 + 216 - 5 + 2860;
        RenderUtils._crest(arrn);
        GL11.glDepthMask((boolean)(206 - 355 + 260 + -111));
        double d = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)timer.renderPartialTicks - RenderManager.renderPosX;
        double d2 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)timer.renderPartialTicks - RenderManager.renderPosY;
        double d3 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)timer.renderPartialTicks - RenderManager.renderPosZ;
        AxisAlignedBB axisAlignedBB = entity.boundingBox;
        AxisAlignedBB axisAlignedBB2 = AxisAlignedBB.getBoundingBox((double)(axisAlignedBB.minX - entity.posX + d - 0.05), (double)(axisAlignedBB.minY - entity.posY + d2), (double)(axisAlignedBB.minZ - entity.posZ + d3 - 0.05), (double)(axisAlignedBB.maxX - entity.posX + d + 0.05), (double)(axisAlignedBB.maxY - entity.posY + d2 + 0.15), (double)(axisAlignedBB.maxZ - entity.posZ + d3 + 0.05));
        if (bl) {
            GL11.glLineWidth((float)1.0f);
            RenderUtils._profiles(148 - 224 + 149 - 44 + -28);
            GL11.glColor4d((double)(color.getRed() / (50 - 64 + 34 - 15 + 250)), (double)(color.getGreen() / (158 - 190 + 93 - 75 + 269)), (double)(color.getBlue() / (244 - 259 + 156 + 114)), (double)0.0);
            RenderGlobal.drawOutlinedBoundingBox((AxisAlignedBB)axisAlignedBB2, (int)color.getRGB());
        }
        GL11.glColor4d((double)(color.getRed() / (241 - 378 + 161 + 231)), (double)(color.getGreen() / (50 - 82 + 69 + 218)), (double)(color.getBlue() / (47 - 55 + 51 - 45 + 257)), (double)(bl ? 0.0 : 0.0));
        RenderUtils._reaching(axisAlignedBB2);
        GL11.glDepthMask((boolean)(54 - 97 + 15 + 29));
        RenderUtils._brave();
    }

    public static void _realtor(int n, int n2, float f, int n3) {
        float f2 = (float)(n3 >> 133 - 258 + 202 - 155 + 102 & 55 - 80 + 74 + 206) / 255.0f;
        float f3 = (float)(n3 >> 173 - 319 + 46 + 116 & 131 - 164 + 95 - 15 + 208) / 255.0f;
        float f4 = (float)(n3 >> 105 - 204 + 3 + 104 & 142 - 249 + 229 + 133) / 255.0f;
        float f5 = (float)(n3 & 217 - 224 + 86 + 176) / 255.0f;
        GL11.glColor4f((float)f3, (float)f4, (float)f5, (float)f2);
        GL11.glEnable((int)(209 - 397 + 70 + 3160));
        GL11.glDisable((int)(25 - 31 + 28 + 3531));
        GL11.glBlendFunc((int)(80 - 128 + 29 + 789), (int)(157 - 271 + 231 - 200 + 854));
        GL11.glEnable((int)(179 - 280 + 113 + 2836));
        GL11.glPushMatrix();
        GL11.glLineWidth((float)1.0f);
        GL11.glBegin((int)(106 - 151 + 97 + -43));
        for (int i = 116 - 167 + 99 + -48; i <= 147 - 221 + 47 - 9 + 396; ++i) {
            GL11.glVertex2d((double)((double)n + Math.sin((double)i * Math.PI / 180.0) * (double)f), (double)((double)n2 + Math.cos((double)i * Math.PI / 180.0) * (double)f));
        }
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)(201 - 215 + 26 + 3541));
        GL11.glDisable((int)(265 - 371 + 305 + 2843));
        GL11.glDisable((int)(216 - 431 + 61 + 3002));
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void _brave() {
        exists$.forEach(RenderUtils::_sexually);
    }

    public static Color _memory(int n) {
        int n2 = (n & 231 - 324 + 65 + 16711708) >> 105 - 157 + 151 - 127 + 44;
        int n3 = (n & 256 - 337 + 262 - 85 + 65184) >> 139 - 176 + 145 + -100;
        int n4 = n & 215 - 387 + 4 + 423;
        return new Color(n2, n3, n4);
    }

    public static void _trade(Class172 class172) {
        GL11.glBindTexture((int)(222 - 361 + 227 - 199 + 3664), (int)RenderUtils._being(class172));
    }

    public static void _beauty(int ... arrn) {
        int[] arrn2 = arrn;
        int n = arrn2.length;
        for (int i = 222 - 300 + 205 + -127; i < n; ++i) {
            int n2 = arrn2[i];
            RenderUtils._climb(n2, 94 - 108 + 102 - 15 + -72);
        }
    }

    public static void _profiles(int n) {
        RenderUtils._climb(n, 153 - 161 + 33 - 23 + -1);
    }

    public static void _hired() {
        GL11.glDisable((int)(130 - 170 + 77 + 3516));
        GL11.glDisable((int)(173 - 242 + 35 + 2963));
        GL11.glDisable((int)(166 - 174 + 3 + 2889));
        GL11.glDepthMask((boolean)(162 - 210 + 80 + -32));
        GL11.glEnable((int)(51 - 90 + 1 + 3080));
        GL11.glBlendFunc((int)(83 - 91 + 56 + 722), (int)(182 - 344 + 171 + 762));
        GL11.glLineWidth((float)1.0f);
    }

    public static void _reaching(AxisAlignedBB axisAlignedBB) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawing(44 - 49 + 5 - 2 + 3);
        tessellator.addVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
        tessellator.addVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ);
        tessellator.addVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ);
        tessellator.addVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ);
        tessellator.addVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ);
        tessellator.addVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
        tessellator.addVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ);
        tessellator.addVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
        tessellator.draw();
        tessellator.startDrawing(92 - 163 + 144 + -72);
        tessellator.addVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ);
        tessellator.addVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ);
        tessellator.addVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ);
        tessellator.addVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
        tessellator.addVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
        tessellator.addVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ);
        tessellator.addVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
        tessellator.addVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ);
        tessellator.draw();
        tessellator.startDrawing(204 - 238 + 209 - 182 + 8);
        tessellator.addVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ);
        tessellator.addVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ);
        tessellator.addVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
        tessellator.addVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
        tessellator.addVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ);
        tessellator.addVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
        tessellator.addVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
        tessellator.addVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ);
        tessellator.draw();
        tessellator.startDrawing(53 - 97 + 54 - 25 + 16);
        tessellator.addVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
        tessellator.addVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ);
        tessellator.addVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ);
        tessellator.addVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ);
        tessellator.addVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
        tessellator.addVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ);
        tessellator.addVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ);
        tessellator.addVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ);
        tessellator.draw();
        tessellator.startDrawing(257 - 311 + 183 - 139 + 11);
        tessellator.addVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
        tessellator.addVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ);
        tessellator.addVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ);
        tessellator.addVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
        tessellator.addVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ);
        tessellator.addVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
        tessellator.addVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ);
        tessellator.addVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ);
        tessellator.draw();
        tessellator.startDrawing(153 - 234 + 15 + 67);
        tessellator.addVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
        tessellator.addVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ);
        tessellator.addVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ);
        tessellator.addVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
        tessellator.addVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ);
        tessellator.addVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ);
        tessellator.addVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
        tessellator.addVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ);
        tessellator.draw();
    }

    public static int _coupons(int n, int n2, int n3) {
        n = n << 170 - 269 + 124 - 119 + 110 & 121 - 141 + 84 + 16711616;
        n2 = n2 << 230 - 338 + 267 - 187 + 36 & 143 - 258 + 142 - 73 + 65326;
        return 251 - 441 + 359 + -16777385 | n | n2 | (n3 &= 93 - 111 + 75 + 198);
    }

    public static void _donated(float f, EntityLivingBase entityLivingBase, int n) {
        float f2 = (float)RenderUtils.things$.thePlayer.field_70165_t;
        float f3 = (float)RenderUtils.things$.thePlayer.field_70163_u;
        float f4 = (float)RenderUtils.things$.thePlayer.field_70161_v;
        float f5 = (float)RenderUtils.things$.thePlayer.field_70169_q;
        float f6 = (float)RenderUtils.things$.thePlayer.field_70167_r;
        float f7 = (float)RenderUtils.things$.thePlayer.field_70166_s;
        float f8 = f5 + (f2 - f5) * f;
        float f9 = f6 + (f3 - f6) * f;
        float f10 = f7 + (f4 - f7) * f;
        double d = entityLivingBase.field_70165_t - (double)(entityLivingBase.field_70130_N / 2.0f);
        double d2 = entityLivingBase.field_70163_u;
        double d3 = entityLivingBase.field_70161_v - (double)(entityLivingBase.field_70130_N / 2.0f);
        float f11 = 0.0f;
        float f12 = entityLivingBase.field_70130_N;
        float f13 = entityLivingBase.field_70131_O;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawing(214 - 295 + 191 - 154 + 45);
        tessellator.setColorRGBA_I(n, 24 - 29 + 17 + 243);
        tessellator.setBrightness(214 - 280 + 209 + 57);
        tessellator.addVertex(d - (double)f8 + (double)f11, d2 - (double)f9 + (double)f13, d3 - (double)f10 + (double)f11);
        tessellator.addVertex(d - (double)f8 + (double)f12, d2 - (double)f9 + (double)f13, d3 - (double)f10 + (double)f11);
        tessellator.addVertex(d - (double)f8 + (double)f12, d2 - (double)f9 + (double)f13, d3 - (double)f10 + (double)f11);
        tessellator.addVertex(d - (double)f8 + (double)f12, d2 - (double)f9 + (double)f13, d3 - (double)f10 + (double)f12);
        tessellator.addVertex(d - (double)f8 + (double)f12, d2 - (double)f9 + (double)f13, d3 - (double)f10 + (double)f12);
        tessellator.addVertex(d - (double)f8 + (double)f11, d2 - (double)f9 + (double)f13, d3 - (double)f10 + (double)f12);
        tessellator.addVertex(d - (double)f8 + (double)f11, d2 - (double)f9 + (double)f13, d3 - (double)f10 + (double)f12);
        tessellator.addVertex(d - (double)f8 + (double)f11, d2 - (double)f9 + (double)f13, d3 - (double)f10 + (double)f11);
        tessellator.addVertex(d - (double)f8 + (double)f12, d2 - (double)f9 + (double)f11, d3 - (double)f10 + (double)f11);
        tessellator.addVertex(d - (double)f8 + (double)f12, d2 - (double)f9 + (double)f11, d3 - (double)f10 + (double)f12);
        tessellator.addVertex(d - (double)f8 + (double)f12, d2 - (double)f9 + (double)f11, d3 - (double)f10 + (double)f12);
        tessellator.addVertex(d - (double)f8 + (double)f11, d2 - (double)f9 + (double)f11, d3 - (double)f10 + (double)f12);
        tessellator.addVertex(d - (double)f8 + (double)f11, d2 - (double)f9 + (double)f11, d3 - (double)f10 + (double)f12);
        tessellator.addVertex(d - (double)f8 + (double)f11, d2 - (double)f9 + (double)f11, d3 - (double)f10 + (double)f11);
        tessellator.addVertex(d - (double)f8 + (double)f11, d2 - (double)f9 + (double)f11, d3 - (double)f10 + (double)f11);
        tessellator.addVertex(d - (double)f8 + (double)f12, d2 - (double)f9 + (double)f11, d3 - (double)f10 + (double)f11);
        tessellator.addVertex(d - (double)f8 + (double)f12, d2 - (double)f9 + (double)f11, d3 - (double)f10 + (double)f12);
        tessellator.addVertex(d - (double)f8 + (double)f12, d2 - (double)f9 + (double)f13, d3 - (double)f10 + (double)f12);
        tessellator.addVertex(d - (double)f8 + (double)f12, d2 - (double)f9 + (double)f11, d3 - (double)f10 + (double)f11);
        tessellator.addVertex(d - (double)f8 + (double)f12, d2 - (double)f9 + (double)f13, d3 - (double)f10 + (double)f11);
        tessellator.addVertex(d - (double)f8 + (double)f11, d2 - (double)f9 + (double)f11, d3 - (double)f10 + (double)f12);
        tessellator.addVertex(d - (double)f8 + (double)f11, d2 - (double)f9 + (double)f13, d3 - (double)f10 + (double)f12);
        tessellator.addVertex(d - (double)f8 + (double)f11, d2 - (double)f9 + (double)f11, d3 - (double)f10 + (double)f11);
        tessellator.addVertex(d - (double)f8 + (double)f11, d2 - (double)f9 + (double)f13, d3 - (double)f10 + (double)f11);
        tessellator.draw();
    }

    public static void _boating(int n) {
        RenderUtils._climb(n, 157 - 228 + 181 - 11 + -98);
    }

    public static void _heard() {
        try {
            if (selected$ != null) {
                if (!selected$.isAccessible()) {
                    selected$.setAccessible(231 - 391 + 278 - 77 + -40);
                }
                selected$.setBoolean((Object)RenderUtils.things$.gameSettings, 92 - 122 + 29 + 1);
                RenderUtils.things$.gameSettings.saveOptions();
            }
        }
        catch (IllegalAccessException illegalAccessException) {
            // empty catch block
        }
    }

    public static void _sexually(int n, boolean bl) {
        if (bl) {
            GL11.glEnable((int)n);
        } else {
            GL11.glDisable((int)n);
        }
    }

    public static void _happened(float f, double d, double d2, double d3, int n) {
        float f2 = (float)RenderUtils.things$.thePlayer.field_70165_t;
        float f3 = (float)RenderUtils.things$.thePlayer.field_70163_u;
        float f4 = (float)RenderUtils.things$.thePlayer.field_70161_v;
        float f5 = (float)RenderUtils.things$.thePlayer.field_70169_q;
        float f6 = (float)RenderUtils.things$.thePlayer.field_70167_r;
        float f7 = (float)RenderUtils.things$.thePlayer.field_70166_s;
        float f8 = f5 + (f2 - f5) * f;
        float f9 = f6 + (f3 - f6) * f;
        float f10 = f7 + (f4 - f7) * f;
        float f11 = 0.0f;
        float f12 = 1.0f;
        float f13 = 1.0f;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawing(31 - 40 + 29 - 6 + -13);
        tessellator.setColorRGBA_I(n, 120 - 157 + 65 + 227);
        tessellator.setBrightness(172 - 280 + 87 - 21 + 242);
        tessellator.addVertex(d - (double)f8 + (double)f11, d2 - (double)f9 + (double)f12, d3 - (double)f10 + (double)f11);
        tessellator.addVertex(d - (double)f8 + (double)f12, d2 - (double)f9 + (double)f12, d3 - (double)f10 + (double)f11);
        tessellator.addVertex(d - (double)f8 + (double)f12, d2 - (double)f9 + (double)f12, d3 - (double)f10 + (double)f11);
        tessellator.addVertex(d - (double)f8 + (double)f12, d2 - (double)f9 + (double)f12, d3 - (double)f10 + (double)f12);
        tessellator.addVertex(d - (double)f8 + (double)f12, d2 - (double)f9 + (double)f12, d3 - (double)f10 + (double)f12);
        tessellator.addVertex(d - (double)f8 + (double)f11, d2 - (double)f9 + (double)f12, d3 - (double)f10 + (double)f12);
        tessellator.addVertex(d - (double)f8 + (double)f11, d2 - (double)f9 + (double)f12, d3 - (double)f10 + (double)f12);
        tessellator.addVertex(d - (double)f8 + (double)f11, d2 - (double)f9 + (double)f12, d3 - (double)f10 + (double)f11);
        tessellator.addVertex(d - (double)f8 + (double)f12, d2 - (double)f9 + (double)f11, d3 - (double)f10 + (double)f11);
        tessellator.addVertex(d - (double)f8 + (double)f12, d2 - (double)f9 + (double)f11, d3 - (double)f10 + (double)f12);
        tessellator.addVertex(d - (double)f8 + (double)f12, d2 - (double)f9 + (double)f11, d3 - (double)f10 + (double)f12);
        tessellator.addVertex(d - (double)f8 + (double)f11, d2 - (double)f9 + (double)f11, d3 - (double)f10 + (double)f12);
        tessellator.addVertex(d - (double)f8 + (double)f11, d2 - (double)f9 + (double)f11, d3 - (double)f10 + (double)f12);
        tessellator.addVertex(d - (double)f8 + (double)f11, d2 - (double)f9 + (double)f11, d3 - (double)f10 + (double)f11);
        tessellator.addVertex(d - (double)f8 + (double)f11, d2 - (double)f9 + (double)f11, d3 - (double)f10 + (double)f11);
        tessellator.addVertex(d - (double)f8 + (double)f12, d2 - (double)f9 + (double)f11, d3 - (double)f10 + (double)f11);
        tessellator.addVertex(d - (double)f8 + (double)f12, d2 - (double)f9 + (double)f11, d3 - (double)f10 + (double)f12);
        tessellator.addVertex(d - (double)f8 + (double)f12, d2 - (double)f9 + (double)f13, d3 - (double)f10 + (double)f12);
        tessellator.addVertex(d - (double)f8 + (double)f12, d2 - (double)f9 + (double)f11, d3 - (double)f10 + (double)f11);
        tessellator.addVertex(d - (double)f8 + (double)f12, d2 - (double)f9 + (double)f13, d3 - (double)f10 + (double)f11);
        tessellator.addVertex(d - (double)f8 + (double)f11, d2 - (double)f9 + (double)f11, d3 - (double)f10 + (double)f12);
        tessellator.addVertex(d - (double)f8 + (double)f11, d2 - (double)f9 + (double)f13, d3 - (double)f10 + (double)f12);
        tessellator.addVertex(d - (double)f8 + (double)f11, d2 - (double)f9 + (double)f11, d3 - (double)f10 + (double)f11);
        tessellator.addVertex(d - (double)f8 + (double)f11, d2 - (double)f9 + (double)f13, d3 - (double)f10 + (double)f11);
        tessellator.draw();
    }

    public static void _lawrence(AxisAlignedBB axisAlignedBB, Color color) {
        GL11.glBlendFunc((int)(124 - 163 + 20 + 789), (int)(28 - 42 + 12 - 2 + 775));
        GL11.glEnable((int)(39 - 60 + 35 - 10 + 3038));
        GL11.glLineWidth((float)2.0f);
        GL11.glDisable((int)(87 - 147 + 89 - 28 + 3552));
        GL11.glDisable((int)(64 - 70 + 28 - 17 + 2924));
        GL11.glDepthMask((boolean)(131 - 164 + 135 + -102));
        GL11.glColor4d((double)(color.getRed() / (57 - 113 + 105 + 206)), (double)(color.getGreen() / (181 - 360 + 205 + 229)), (double)(color.getBlue() / (163 - 316 + 201 - 24 + 231)), (double)(color.getAlpha() / (229 - 298 + 81 - 25 + 268)));
        RenderUtils._reaching(axisAlignedBB);
        GL11.glEnable((int)(27 - 40 + 12 - 1 + 3555));
        GL11.glEnable((int)(193 - 273 + 176 + 2833));
        GL11.glDepthMask((boolean)(168 - 323 + 229 - 129 + 56));
        GL11.glDisable((int)(66 - 102 + 81 - 25 + 3022));
    }

    public static void _binary(Entity entity, Color color) {
        Timer timer = Wrapper.timer;
        double d = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)timer.renderPartialTicks - RenderManager.renderPosX;
        double d2 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)timer.renderPartialTicks - RenderManager.renderPosY;
        double d3 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)timer.renderPartialTicks - RenderManager.renderPosZ;
        AxisAlignedBB axisAlignedBB = entity.boundingBox.offset(-entity.posX, -entity.posY, -entity.posZ).offset(d, d2, d3);
        RenderUtils._lawrence(AxisAlignedBB.getBoundingBox((double)axisAlignedBB.minX, (double)(axisAlignedBB.maxY + 0.2), (double)axisAlignedBB.minZ, (double)axisAlignedBB.maxX, (double)(axisAlignedBB.maxY + 0.26), (double)axisAlignedBB.maxZ), color);
    }

    public static void _leading() {
        GL11.glDepthMask((boolean)(244 - 256 + 200 + -187));
        GL11.glDisable((int)(108 - 144 + 62 + 3016));
        GL11.glEnable((int)(217 - 318 + 18 + 3636));
        GL11.glEnable((int)(241 - 323 + 301 - 136 + 2846));
        GL11.glEnable((int)(235 - 301 + 243 + 2707));
    }

    public static void _clicking(double d, double d2, double d3, double d4, int n, int n2, int n3) {
        Gui.drawRect((int)((int)d), (int)((int)d2), (int)((int)d3), (int)((int)d4), (int)n);
        Gui.drawRect((int)((int)d + (22 - 39 + 27 + -9)), (int)((int)d2 + (228 - 261 + 184 - 68 + -82)), (int)((int)d3 - (258 - 319 + 208 + -146)), (int)((int)d4 - (57 - 59 + 31 - 13 + -15)), (int)n2);
        Gui.drawRect((int)((int)d + (57 - 98 + 79 + -36)), (int)((int)d2 + (263 - 403 + 305 - 197 + 34)), (int)((int)d3 - (178 - 242 + 6 + 60)), (int)((int)d4 - (191 - 215 + 30 - 26 + 22)), (int)n3);
    }

    public static void _melissa(double d, double d2, double d3, int n) {
        d3 *= 2.0;
        d *= 2.0;
        d2 *= 2.0;
        float f = (float)(n >> 191 - 359 + 35 - 17 + 174 & 218 - 307 + 255 + 89) / 255.0f;
        float f2 = (float)(n >> 83 - 113 + 54 + -8 & 188 - 274 + 125 + 216) / 255.0f;
        float f3 = (float)(n >> 208 - 286 + 132 - 83 + 37 & 267 - 468 + 17 - 17 + 456) / 255.0f;
        float f4 = (float)(n & 123 - 213 + 25 + 320) / 255.0f;
        RenderUtils._helpful();
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        GL11.glColor4f((float)f2, (float)f3, (float)f4, (float)f);
        GL11.glBegin((int)(66 - 115 + 98 - 36 + -7));
        for (int i = 244 - 419 + 241 + -66; i <= 28 - 29 + 9 + 352; ++i) {
            double d4 = Math.sin((double)i * Math.PI / 180.0) * d3;
            double d5 = Math.cos((double)i * Math.PI / 180.0) * d3;
            GL11.glVertex2d((double)(d + d4), (double)(d2 + d5));
        }
        GL11.glEnd();
        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
        RenderUtils._showing();
    }

    public static void _brake() {
        GL11.glEnable((int)(218 - 358 + 206 - 139 + 3002));
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glPopMatrix();
    }

    public static void _tower(int n, int n2, int n3, int n4) {
        GL11.glPushMatrix();
        GL11.glEnable((int)(195 - 258 + 222 - 126 + 2815));
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)0.8f);
        GL11.glDisable((int)(211 - 285 + 213 - 197 + 3611));
        GL11.glBlendFunc((int)(87 - 145 + 40 + 788), (int)(202 - 301 + 158 + 712));
        GL11.glEnable((int)(94 - 108 + 101 + 2955));
        GL11.glLineWidth((float)0.5f);
        GL11.glBegin((int)(165 - 220 + 183 - 135 + 8));
        GL11.glVertex2i((int)n, (int)n2);
        GL11.glVertex2i((int)n3, (int)n4);
        GL11.glEnd();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glDisable((int)(70 - 88 + 81 - 3 + 2982));
        GL11.glDisable((int)(242 - 350 + 99 + 2857));
        GL11.glEnable((int)(181 - 307 + 296 - 75 + 3458));
        GL11.glPopMatrix();
    }

    static {
        RenderUtils._agent();
        things$ = Minecraft.getMinecraft();
        tribunal$ = Maps.newHashMap();
        try {
            selected$ = GameSettings.class.getDeclaredField(OVYt.968L.FS1x((String)score$[0], (int)1501717362));
        }
        catch (NoSuchFieldException noSuchFieldException) {
            // empty catch block
        }
        exists$ = new HashMap<Integer, Boolean>();
    }

    public static void _climb(int n, boolean bl) {
        exists$.put(n, GL11.glGetBoolean((int)n));
        RenderUtils._sexually(n, bl);
    }

    public static void _crest(int ... arrn) {
        int[] arrn2 = arrn;
        int n = arrn2.length;
        for (int i = 203 - 295 + 234 + -142; i < n; ++i) {
            int n2 = arrn2[i];
            RenderUtils._climb(n2, 259 - 330 + 116 - 20 + -25);
        }
    }
}

