/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.lwjgl.BufferUtils
 *  org.lwjgl.opengl.Display
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.util.glu.GLU
 */
package me.razerboy420.weepcraft.util;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;
import java.util.Random;
import me.razerboy420.weepcraft.Weepcraft;
import me.razerboy420.weepcraft.module.ModuleManager;
import me.razerboy420.weepcraft.module.modules.combat.aura.Aura;
import me.razerboy420.weepcraft.util.RenderUtils2D;
import me.razerboy420.weepcraft.util.Wrapper;
import me.razerboy420.weepcraft.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Timer;
import net.minecraft.util.text.ITextComponent;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public final class MathUtils {
    private static final Random rng = new Random();

    public static Random getRng() {
        return rng;
    }

    public static float getAngleDifference(float alpha, float beta) {
        float phi = Math.abs(beta - alpha) % 360.0f;
        float distance = phi > 180.0f ? 360.0f - phi : phi;
        return distance;
    }

    public static int transitionTo(int from, int to) {
        int i;
        if (from < to) {
            Wrapper.mc();
            if (Minecraft.getDebugFPS() >= 60) {
                i = 0;
                while (i < 3) {
                    ++from;
                    ++i;
                }
            }
        }
        if (from > to) {
            Wrapper.mc();
            if (Minecraft.getDebugFPS() >= 60) {
                i = 0;
                while (i < 3) {
                    --from;
                    ++i;
                }
            }
        }
        if (from < to) {
            Wrapper.mc();
            if (Minecraft.getDebugFPS() >= 40) {
                Wrapper.mc();
                if (Minecraft.getDebugFPS() <= 59) {
                    i = 0;
                    while (i < 4) {
                        ++from;
                        ++i;
                    }
                }
            }
        }
        if (from > to) {
            Wrapper.mc();
            if (Minecraft.getDebugFPS() >= 40) {
                Wrapper.mc();
                if (Minecraft.getDebugFPS() <= 59) {
                    i = 0;
                    while (i < 4) {
                        --from;
                        ++i;
                    }
                }
            }
        }
        if (from < to) {
            Wrapper.mc();
            if (Minecraft.getDebugFPS() >= 0) {
                Wrapper.mc();
                if (Minecraft.getDebugFPS() <= 39) {
                    i = 0;
                    while (i < 6) {
                        ++from;
                        ++i;
                    }
                }
            }
        }
        if (from > to) {
            Wrapper.mc();
            if (Minecraft.getDebugFPS() >= 0) {
                Wrapper.mc();
                if (Minecraft.getDebugFPS() <= 39) {
                    i = 0;
                    while (i < 6) {
                        --from;
                        ++i;
                    }
                }
            }
        }
        return from;
    }

    public static float cap(float i, float j, float k) {
        if (i > j) {
            i = j;
        }
        if (i < k) {
            i = k;
        }
        return i;
    }

//    public static double get3DPosX(EntityLivingBase jew) {
//        Wrapper.mc().getRenderManager();
//        double x = jew.lastTickPosX + (jew.posX - jew.lastTickPosX) * (double)Wrapper.mc().timer.renderPartialTicks - RenderManager.renderPosX;
//        return x;
//    }
//
//    public static double get3DPosY(EntityLivingBase jew) {
//        Wrapper.mc().getRenderManager();
//        double y = jew.lastTickPosY + (jew.posY - jew.lastTickPosY) * (double)Wrapper.mc().timer.renderPartialTicks - RenderManager.renderPosY;
//        return y;
//    }
//
//    public static double get3DPosZ(EntityLivingBase jew) {
//        Wrapper.mc().getRenderManager();
//        double z = jew.lastTickPosZ + (jew.posZ - jew.lastTickPosZ) * (double)Wrapper.mc().timer.renderPartialTicks - RenderManager.renderPosZ;
//        return z;
//    }

    public static boolean isOnSameTeam(EntityLivingBase nigger, EntityLivingBase jew) {
        String all = "0123456789abcdef";
        int i = 0;
        while (i < all.length()) {
            char s = all.charAt(i);
            if (nigger.getDisplayName().getUnformattedText().toLowerCase().startsWith("\u00a7" + s) && jew.getDisplayName().getUnformattedText().toLowerCase().startsWith("\u00a7" + s)) {
                return true;
            }
            ++i;
        }
        return false;
    }

    public static boolean isAuraBlocking() {
        if (ModuleManager.aura.isToggled() && Aura.block.boolvalue) {
            for (Object o : Wrapper.getWorld().loadedEntityList) {
                if (!(o instanceof EntityLivingBase)) continue;
                EntityLivingBase nig = (EntityLivingBase)o;
                if (!Weepcraft.getAura().isInBlockRange(nig) || !Weepcraft.getAura().isAttackable(nig) || Weepcraft.getAura().getShield() == null) continue;
                return true;
            }
        }
        return false;
    }

    public static float getRandom() {
        return rng.nextFloat();
    }

    public static int getRandom(int cap) {
        return rng.nextInt(cap);
    }

    public static int getRandom(int floor, int cap) {
        return floor + rng.nextInt(cap - floor + 1);
    }

    public static int randInt(int min, int max) {
        return new Random().nextInt(max - min + 1) + min;
    }

    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static float getSimilarity(String string1, String string2) {
        int halflen = Math.min(string1.length(), string2.length()) / 2 + Math.min(string1.length(), string2.length()) % 2;
        StringBuffer common1 = MathUtils.getCommonCharacters(string1, string2, halflen);
        StringBuffer common2 = MathUtils.getCommonCharacters(string2, string1, halflen);
        if (common1.length() == 0 || common2.length() == 0) {
            return 0.0f;
        }
        if (common1.length() != common2.length()) {
            return 0.0f;
        }
        int transpositions = 0;
        int n = common1.length();
        int i = 0;
        while (i < n) {
            if (common1.charAt(i) != common2.charAt(i)) {
                ++transpositions;
            }
            ++i;
        }
        transpositions = (int)((float)transpositions / 2.0f);
        return (float)(common1.length() / string1.length() + common2.length() / string2.length() + (common1.length() - transpositions) / common1.length()) / 3.0f;
    }

    private static StringBuffer getCommonCharacters(String string1, String string2, int distanceSep) {
        StringBuffer returnCommons = new StringBuffer();
        StringBuffer copy = new StringBuffer(string2);
        int n = string1.length();
        int m = string2.length();
        int i = 0;
        while (i < n) {
            char ch = string1.charAt(i);
            boolean foundIt = false;
            int j = Math.max(0, i - distanceSep);
            while (!foundIt && j < Math.min(i + distanceSep, m - 1)) {
                if (copy.charAt(j) == ch) {
                    foundIt = true;
                    returnCommons.append(ch);
                    copy.setCharAt(j, '\u0000');
                }
                ++j;
            }
            ++i;
        }
        return returnCommons;
    }

    public static int[] getScreenCoords(double x, double y, double z) {
        FloatBuffer screenCoords = BufferUtils.createFloatBuffer((int)3);
        IntBuffer viewport = BufferUtils.createIntBuffer((int)16);
        FloatBuffer modelView = BufferUtils.createFloatBuffer((int)16);
        FloatBuffer projection = BufferUtils.createFloatBuffer((int)16);
        GL11.glGetFloat((int)2982, (FloatBuffer)modelView);
        GL11.glGetFloat((int)2983, (FloatBuffer)projection);
        GL11.glGetInteger((int)2978, (IntBuffer)viewport);
        boolean result = GLU.gluProject((float)((float)x), (float)((float)y), (float)((float)z), (FloatBuffer)modelView, (FloatBuffer)projection, (IntBuffer)viewport, (FloatBuffer)screenCoords);
        if (result) {
            int xx = (int)screenCoords.get(0);
            int yy = (int)screenCoords.get(1);
            int zz = (int)screenCoords.get(2);
            return new int[]{xx, Display.getHeight() - yy, zz};
        }
        System.out.println("Failed.");
        return null;
    }

    public static int[] get2Dshit(double x, double y, double z) {
        int midX = RenderUtils2D.newScaledResolution().getScaledWidth() / 2;
        int midY = RenderUtils2D.newScaledResolution().getScaledHeight() / 2;
        float zoom = Wrapper.mc().gameSettings.fovSetting;
        double xx = x / z * (double)zoom + (double)midX;
        double yy = y / z * (double)zoom + (double)midY;
        if (z > 0.0) {
            xx = x / z * (double)zoom + (double)midX;
            yy = y / z * (double)zoom + (double)midY;
        }
        return new int[]{(int)xx, (int)yy};
    }

    public static double parabolic(double value, double to, double inc) {
        if (Math.abs(value - to) < 0.01) {
            return to;
        }
        return value + (to - value) / inc;
    }

    public static double roundToPlace(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}

