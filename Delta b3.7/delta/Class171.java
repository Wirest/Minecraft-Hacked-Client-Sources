/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  delta.OVYt$968L
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.texture.DynamicTexture
 *  net.minecraft.util.MathHelper
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.util.glu.Project
 */
package delta;

import delta.Class172;
import delta.Class19;
import delta.OVYt;
import delta.utils.RenderUtils;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Project;

public class Class171
extends Class19 {
    private static String[] douglas$;
    private DynamicTexture alone$ = new DynamicTexture(102 - 111 + 4 - 2 + 263, 145 - 260 + 243 + 128);
    private static final Class172[] sticky$;
    protected int manual$;

    static {
        Class171._stuart();
        Class172[] arrclass172 = new Class172[77 - 93 + 81 - 38 + -21];
        arrclass172[85 - 142 + 46 + 11] = new Class172(OVYt.968L.FS1x((String)douglas$[1], (int)1575964463));
        arrclass172[134 - 236 + 175 - 142 + 70] = new Class172(OVYt.968L.FS1x((String)douglas$[2], (int)-1275958210));
        arrclass172[126 - 190 + 91 - 79 + 54] = new Class172(OVYt.968L.FS1x((String)douglas$[3], (int)1094030718));
        arrclass172[253 - 467 + 361 - 330 + 186] = new Class172(OVYt.968L.FS1x((String)douglas$[4], (int)-166100258));
        arrclass172[94 - 141 + 29 - 28 + 50] = new Class172(OVYt.968L.FS1x((String)douglas$[5], (int)-522740615));
        arrclass172[184 - 287 + 43 + 65] = new Class172(OVYt.968L.FS1x((String)douglas$[6], (int)-104738634));
        sticky$ = arrclass172;
    }

    public void _spaces(float f) {
        this.field_146297_k.getTextureManager().bindTexture(this.field_146297_k.getTextureManager().getDynamicTextureLocation(OVYt.968L.FS1x((String)douglas$[0], (int)-1554483918), this.alone$));
        GL11.glTexParameteri((int)(201 - 336 + 264 - 51 + 3475), (int)(213 - 257 + 159 - 14 + 10140), (int)(97 - 163 + 160 - 43 + 9678));
        GL11.glTexParameteri((int)(124 - 161 + 90 - 59 + 3559), (int)(254 - 425 + 252 + 10159), (int)(178 - 347 + 257 + 9641));
        GL11.glCopyTexSubImage2D((int)(35 - 67 + 14 - 6 + 3577), (int)(242 - 302 + 288 - 31 + -197), (int)(26 - 35 + 26 + -17), (int)(135 - 267 + 171 + -39), (int)(51 - 85 + 61 - 34 + 7), (int)(139 - 189 + 154 + -104), (int)(161 - 317 + 112 - 1 + 301), (int)(193 - 381 + 281 + 163));
        GL11.glEnable((int)(233 - 331 + 87 - 2 + 3055));
        OpenGlHelper.glBlendFunc((int)(173 - 208 + 172 + 633), (int)(172 - 222 + 89 + 732), (int)(112 - 122 + 119 + -108), (int)(259 - 262 + 242 - 162 + -77));
        GL11.glColorMask((boolean)(106 - 145 + 51 + -11), (boolean)(243 - 310 + 203 + -135), (boolean)(73 - 145 + 48 - 47 + 72), (boolean)(80 - 120 + 25 + 15));
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        GL11.glDisable((int)(172 - 321 + 16 + 3141));
        int n = 148 - 220 + 33 + 42;
        for (int i = 223 - 276 + 222 + -169; i < n; ++i) {
            tessellator.setColorRGBA_F(1.0f, 1.0f, 1.0f, 1.0f / (float)(i + (216 - 353 + 4 - 1 + 135)));
            int n2 = this.field_146294_l;
            int n3 = this.field_146295_m;
            float f2 = (float)(i - n / (79 - 123 + 94 - 31 + -17)) / 256.0f;
            tessellator.addVertexWithUV((double)n2, (double)n3, (double)this.field_73735_i, (double)(0.0f + f2), 1.0);
            tessellator.addVertexWithUV((double)n2, 0.0, (double)this.field_73735_i, (double)(1.0f + f2), 1.0);
            tessellator.addVertexWithUV(0.0, 0.0, (double)this.field_73735_i, (double)(1.0f + f2), 0.0);
            tessellator.addVertexWithUV(0.0, (double)n3, (double)this.field_73735_i, (double)(0.0f + f2), 0.0);
        }
        tessellator.draw();
        GL11.glEnable((int)(69 - 93 + 76 + 2956));
        GL11.glColorMask((boolean)(234 - 262 + 45 - 9 + -7), (boolean)(250 - 292 + 13 - 3 + 33), (boolean)(210 - 408 + 262 + -63), (boolean)(220 - 402 + 167 - 34 + 50));
    }

    private static void _stuart() {
        douglas$ = new String[]{"\u7550\u7553\u7551\u7559\u7555\u7540\u755d\u7547\u755c\u7556", "\u4f47\u4f5b\u4f5b\u4f5f\u4f5c\u4f15\u4f00\u4f00\u4f41\u4f44\u4f40\u4f5c\u4f42\u4f40\u4f5c\u4f01\u4f48\u4f46\u4f5b\u4f47\u4f5a\u4f4d\u4f01\u4f46\u4f40\u4f00\u4f4e\u4f5c\u4f5c\u4f4a\u4f5b\u4f5c\u4f00\u4f5f\u4f4e\u4f41\u4f40\u4f5d\u4f4e\u4f42\u4f4e\u4f70\u4f1f\u4f01\u4f5f\u4f41\u4f48", "\u6c56\u6c4a\u6c4a\u6c4e\u6c4d\u6c04\u6c11\u6c11\u6c50\u6c55\u6c51\u6c4d\u6c53\u6c51\u6c4d\u6c10\u6c59\u6c57\u6c4a\u6c56\u6c4b\u6c5c\u6c10\u6c57\u6c51\u6c11\u6c5f\u6c4d\u6c4d\u6c5b\u6c4a\u6c4d\u6c11\u6c4e\u6c5f\u6c50\u6c51\u6c4c\u6c5f\u6c53\u6c5f\u6c61\u6c0f\u6c10\u6c4e\u6c50\u6c59", "\u9516\u950a\u950a\u950e\u950d\u9544\u9551\u9551\u9510\u9515\u9511\u950d\u9513\u9511\u950d\u9550\u9519\u9517\u950a\u9516\u950b\u951c\u9550\u9517\u9511\u9551\u951f\u950d\u950d\u951b\u950a\u950d\u9551\u950e\u951f\u9510\u9511\u950c\u951f\u9513\u951f\u9521\u954c\u9550\u950e\u9510\u9519", "\u82b6\u82aa\u82aa\u82ae\u82ad\u82e4\u82f1\u82f1\u82b0\u82b5\u82b1\u82ad\u82b3\u82b1\u82ad\u82f0\u82b9\u82b7\u82aa\u82b6\u82ab\u82bc\u82f0\u82b7\u82b1\u82f1\u82bf\u82ad\u82ad\u82bb\u82aa\u82ad\u82f1\u82ae\u82bf\u82b0\u82b1\u82ac\u82bf\u82b3\u82bf\u8281\u82ed\u82f0\u82ae\u82b0\u82b9", "\u9c11\u9c0d\u9c0d\u9c09\u9c0a\u9c43\u9c56\u9c56\u9c17\u9c12\u9c16\u9c0a\u9c14\u9c16\u9c0a\u9c57\u9c1e\u9c10\u9c0d\u9c11\u9c0c\u9c1b\u9c57\u9c10\u9c16\u9c56\u9c18\u9c0a\u9c0a\u9c1c\u9c0d\u9c0a\u9c56\u9c09\u9c18\u9c17\u9c16\u9c0b\u9c18\u9c14\u9c18\u9c26\u9c4d\u9c57\u9c09\u9c17\u9c1e", "\ud0de\ud0c2\ud0c2\ud0c6\ud0c5\ud08c\ud099\ud099\ud0d8\ud0dd\ud0d9\ud0c5\ud0db\ud0d9\ud0c5\ud098\ud0d1\ud0df\ud0c2\ud0de\ud0c3\ud0d4\ud098\ud0df\ud0d9\ud099\ud0d7\ud0c5\ud0c5\ud0d3\ud0c2\ud0c5\ud099\ud0c6\ud0d7\ud0d8\ud0d9\ud0c4\ud0d7\ud0db\ud0d7\ud0e9\ud083\ud098\ud0c6\ud0d8\ud0d1"};
    }

    public void _kissing(int n, int n2, float f) {
        this.field_146297_k.getFramebuffer().unbindFramebuffer();
        GL11.glViewport((int)(207 - 297 + 166 - 129 + 53), (int)(162 - 231 + 13 - 3 + 59), (int)(150 - 289 + 140 + 255), (int)(175 - 257 + 50 + 288));
        this._steam(n, n2, f);
        this._spaces(f);
        this._spaces(f);
        this._spaces(f);
        this._spaces(f);
        this._spaces(f);
        this._spaces(f);
        this._spaces(f);
        this.field_146297_k.getFramebuffer().bindFramebuffer(156 - 212 + 71 - 54 + 40);
        GL11.glViewport((int)(101 - 178 + 76 + 1), (int)(150 - 217 + 193 - 167 + 41), (int)this.field_146297_k.displayWidth, (int)this.field_146297_k.displayHeight);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        float f2 = this.field_146294_l > this.field_146295_m ? 120.0f / (float)this.field_146294_l : 120.0f / (float)this.field_146295_m;
        float f3 = (float)this.field_146295_m * f2 / 256.0f;
        float f4 = (float)this.field_146294_l * f2 / 256.0f;
        tessellator.setColorRGBA_F(1.0f, 1.0f, 1.0f, 1.0f);
        int n3 = this.field_146294_l;
        int n4 = this.field_146295_m;
        tessellator.addVertexWithUV(0.0, (double)n4, (double)this.field_73735_i, (double)(0.5f - f3), (double)(0.5f + f4));
        tessellator.addVertexWithUV((double)n3, (double)n4, (double)this.field_73735_i, (double)(0.5f - f3), (double)(0.5f - f4));
        tessellator.addVertexWithUV((double)n3, 0.0, (double)this.field_73735_i, (double)(0.5f + f3), (double)(0.5f - f4));
        tessellator.addVertexWithUV(0.0, 0.0, (double)this.field_73735_i, (double)(0.5f + f3), (double)(0.5f + f4));
        tessellator.draw();
    }

    public void _cornwall() {
        this.manual$ += 120 - 166 + 70 - 60 + 38;
        super.func_73876_c();
    }

    public void _steam(int n, int n2, float f) {
        Tessellator tessellator = Tessellator.instance;
        GL11.glMatrixMode((int)(265 - 397 + 186 + 5835));
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        Project.gluPerspective((float)120.0f, (float)1.0f, (float)0.05f, (float)10.0f);
        GL11.glMatrixMode((int)(228 - 319 + 119 - 67 + 5927));
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glRotatef((float)180.0f, (float)1.0f, (float)0.0f, (float)0.0f);
        GL11.glRotatef((float)90.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        GL11.glEnable((int)(78 - 143 + 115 - 85 + 3077));
        GL11.glDisable((int)(43 - 58 + 40 + 2983));
        GL11.glDisable((int)(232 - 427 + 339 - 60 + 2800));
        GL11.glDepthMask((boolean)(43 - 56 + 26 + -13));
        OpenGlHelper.glBlendFunc((int)(120 - 200 + 149 + 701), (int)(252 - 418 + 184 + 753), (int)(234 - 311 + 98 + -20), (int)(185 - 340 + 82 - 67 + 140));
        int n3 = 66 - 130 + 68 + 4;
        for (int i = 183 - 212 + 173 - 156 + 12; i < n3 * n3; ++i) {
            GL11.glPushMatrix();
            float f2 = ((float)(i % n3) / (float)n3 - 0.5f) / 64.0f;
            float f3 = ((float)(i / n3) / (float)n3 - 0.5f) / 64.0f;
            float f4 = 0.0f;
            GL11.glTranslatef((float)f2, (float)f3, (float)f4);
            GL11.glRotatef((float)(MathHelper.sin((float)(((float)this.manual$ + f) / 400.0f)) * 25.0f + 20.0f), (float)1.0f, (float)0.0f, (float)0.0f);
            GL11.glRotatef((float)(-((float)this.manual$ + f) * 0.1f), (float)0.0f, (float)1.0f, (float)0.0f);
            for (int j = 113 - 197 + 183 + -99; j < 189 - 279 + 113 - 14 + -3; ++j) {
                GL11.glPushMatrix();
                if (j == 241 - 450 + 389 + -179) {
                    GL11.glRotatef((float)90.0f, (float)0.0f, (float)1.0f, (float)0.0f);
                }
                if (j == 129 - 140 + 29 - 24 + 8) {
                    GL11.glRotatef((float)180.0f, (float)0.0f, (float)1.0f, (float)0.0f);
                }
                if (j == 82 - 113 + 71 - 33 + -4) {
                    GL11.glRotatef((float)-90.0f, (float)0.0f, (float)1.0f, (float)0.0f);
                }
                if (j == 260 - 280 + 124 - 106 + 6) {
                    GL11.glRotatef((float)90.0f, (float)1.0f, (float)0.0f, (float)0.0f);
                }
                if (j == 69 - 90 + 9 + 17) {
                    GL11.glRotatef((float)-90.0f, (float)1.0f, (float)0.0f, (float)0.0f);
                }
                RenderUtils._trade(sticky$[j]);
                tessellator.startDrawingQuads();
                tessellator.setColorRGBA_I(129 - 194 + 5 - 4 + 16777279, (42 - 66 + 36 + 243) / (i + (183 - 274 + 60 + 32)));
                float f5 = 0.0f;
                tessellator.addVertexWithUV(-1.0, -1.0, 1.0, (double)(0.0f + f5), (double)(0.0f + f5));
                tessellator.addVertexWithUV(1.0, -1.0, 1.0, (double)(1.0f - f5), (double)(0.0f + f5));
                tessellator.addVertexWithUV(1.0, 1.0, 1.0, (double)(1.0f - f5), (double)(1.0f - f5));
                tessellator.addVertexWithUV(-1.0, 1.0, 1.0, (double)(0.0f + f5), (double)(1.0f - f5));
                tessellator.draw();
                GL11.glPopMatrix();
            }
            GL11.glPopMatrix();
            GL11.glColorMask((boolean)(118 - 231 + 228 + -114), (boolean)(52 - 77 + 6 - 3 + 23), (boolean)(43 - 71 + 64 + -35), (boolean)(113 - 151 + 35 + 3));
        }
        tessellator.setTranslation(0.0, 0.0, 0.0);
        GL11.glColorMask((boolean)(131 - 180 + 98 - 85 + 37), (boolean)(102 - 152 + 90 - 90 + 51), (boolean)(205 - 404 + 127 - 14 + 87), (boolean)(202 - 319 + 6 - 6 + 118));
        GL11.glMatrixMode((int)(91 - 113 + 36 + 5875));
        GL11.glPopMatrix();
        GL11.glMatrixMode((int)(76 - 140 + 15 - 9 + 5946));
        GL11.glPopMatrix();
        GL11.glDepthMask((boolean)(113 - 162 + 108 - 6 + -52));
        GL11.glEnable((int)(195 - 316 + 156 - 116 + 2965));
        GL11.glEnable((int)(114 - 203 + 73 + 2945));
    }
}

