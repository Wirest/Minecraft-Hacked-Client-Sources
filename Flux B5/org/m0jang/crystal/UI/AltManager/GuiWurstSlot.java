package org.m0jang.crystal.UI.AltManager;

import assets.minecraft.euphoria.Texture;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.m0jang.crystal.Wrapper;

public abstract class GuiWurstSlot {
   private final Minecraft field_148161_k;
   protected int field_148155_a;
   private int field_148158_l;
   protected int field_148153_b;
   protected int field_148154_c;
   protected int field_148151_d;
   protected int field_148152_e;
   protected final int field_148149_f;
   private int field_148159_m;
   private int field_148156_n;
   protected int field_148150_g;
   protected int field_148162_h;
   protected boolean field_148163_i = true;
   private float field_148157_o = -2.0F;
   private float field_148170_p;
   private float field_148169_q;
   private int field_148168_r = -1;
   private long field_148167_s;
   private boolean field_148166_t = true;
   private boolean field_148165_u;
   protected int field_148160_j;
   private boolean field_148164_v = true;

   public GuiWurstSlot(Minecraft par1Minecraft, int par2, int par3, int par4, int par5, int par6) {
      this.field_148161_k = par1Minecraft;
      this.field_148155_a = par2;
      this.field_148158_l = par3;
      this.field_148153_b = par4;
      this.field_148154_c = par5;
      this.field_148149_f = par6;
      this.field_148152_e = 0;
      this.field_148151_d = par2;
   }

   public void func_148122_a(int p_148122_1_, int p_148122_2_, int p_148122_3_, int p_148122_4_) {
      this.field_148155_a = p_148122_1_;
      this.field_148158_l = p_148122_2_;
      this.field_148153_b = p_148122_3_;
      this.field_148154_c = p_148122_4_;
      this.field_148152_e = 0;
      this.field_148151_d = p_148122_1_;
   }

   public void func_148130_a(boolean p_148130_1_) {
      this.field_148166_t = p_148130_1_;
   }

   protected void func_148133_a(boolean p_148133_1_, int p_148133_2_) {
      this.field_148165_u = p_148133_1_;
      this.field_148160_j = p_148133_2_;
      if (!p_148133_1_) {
         this.field_148160_j = 0;
      }

   }

   protected abstract int getSize();

   protected abstract void elementClicked(int var1, boolean var2, int var3, int var4);

   protected abstract boolean isSelected(int var1);

   protected int func_148138_e() {
      return this.getSize() * this.field_148149_f + this.field_148160_j;
   }

   protected abstract void drawBackground();

   protected abstract void drawSlot(int var1, int var2, int var3, int var4, int var5, int var6);

   protected void func_148129_a(int p_148129_1_, int p_148129_2_, Tessellator p_148129_3_) {
   }

   protected void func_148132_a(int p_148132_1_, int p_148132_2_) {
   }

   protected void func_148142_b(int p_148142_1_, int p_148142_2_) {
   }

   public int func_148124_c(int p_148124_1_, int p_148124_2_) {
      int var3 = this.field_148152_e + this.field_148155_a / 2 - this.func_148139_c() / 2;
      int var4 = this.field_148152_e + this.field_148155_a / 2 + this.func_148139_c() / 2;
      int var5 = p_148124_2_ - this.field_148153_b - this.field_148160_j + (int)this.field_148169_q - 4;
      int var6 = var5 / this.field_148149_f;
      return p_148124_1_ < this.func_148137_d() && p_148124_1_ >= var3 && p_148124_1_ <= var4 && var6 >= 0 && var5 >= 0 && var6 < this.getSize() ? var6 : -1;
   }

   public void registerScrollButtons(int p_148134_1_, int p_148134_2_) {
      this.field_148159_m = p_148134_1_;
      this.field_148156_n = p_148134_2_;
   }

   private void func_148121_k() {
      int var1 = this.func_148135_f();
      if (var1 < 0) {
         var1 /= 2;
      }

      if (!this.field_148163_i && var1 < 0) {
         var1 = 0;
      }

      if (this.field_148169_q < 0.0F) {
         this.field_148169_q = 0.0F;
      }

      if (this.field_148169_q > (float)var1) {
         this.field_148169_q = (float)var1;
      }

   }

   public int func_148135_f() {
      return this.func_148138_e() - (this.field_148154_c - this.field_148153_b - 4);
   }

   public int func_148148_g() {
      return (int)this.field_148169_q;
   }

   public boolean func_148141_e(int p_148141_1_) {
      return p_148141_1_ >= this.field_148153_b && p_148141_1_ <= this.field_148154_c;
   }

   public void func_148145_f(int p_148145_1_) {
      this.field_148169_q += (float)p_148145_1_;
      this.func_148121_k();
      this.field_148157_o = -2.0F;
   }

   public void func_148147_a(GuiButton p_148147_1_) {
      if (p_148147_1_.enabled) {
         if (p_148147_1_.id == this.field_148159_m) {
            this.field_148169_q -= (float)(this.field_148149_f * 2 / 3);
            this.field_148157_o = -2.0F;
            this.func_148121_k();
         } else if (p_148147_1_.id == this.field_148156_n) {
            this.field_148169_q += (float)(this.field_148149_f * 2 / 3);
            this.field_148157_o = -2.0F;
            this.func_148121_k();
         }
      }

   }

   public void drawScreen(int p_148128_1_, int p_148128_2_, float p_148128_3_) {
      this.field_148150_g = p_148128_1_;
      this.field_148162_h = p_148128_2_;
      this.drawBackground();
      int var4 = this.getSize();
      int var5 = this.func_148137_d();
      int var6 = var5 + 6;
      int var9;
      int var10;
      int var13;
      int var19;
      if (p_148128_1_ > this.field_148152_e && p_148128_1_ < this.field_148151_d && p_148128_2_ > this.field_148153_b && p_148128_2_ < this.field_148154_c) {
         if (Mouse.isButtonDown(0) && this.func_148125_i()) {
            if (this.field_148157_o == -1.0F) {
               boolean var15 = true;
               if (p_148128_2_ >= this.field_148153_b && p_148128_2_ <= this.field_148154_c) {
                  int var8 = this.field_148155_a / 2 - this.func_148139_c() / 2;
                  var9 = this.field_148155_a / 2 + this.func_148139_c() / 2;
                  var10 = p_148128_2_ - this.field_148153_b - this.field_148160_j + (int)this.field_148169_q - 4;
                  int var11 = var10 / this.field_148149_f;
                  if (p_148128_1_ >= var8 && p_148128_1_ <= var9 && var11 >= 0 && var10 >= 0 && var11 < var4) {
                     boolean var12 = var11 == this.field_148168_r && Minecraft.getSystemTime() - this.field_148167_s < 250L;
                     this.elementClicked(var11, var12, p_148128_1_, p_148128_2_);
                     this.field_148168_r = var11;
                     this.field_148167_s = Minecraft.getSystemTime();
                  } else if (p_148128_1_ >= var8 && p_148128_1_ <= var9 && var10 < 0) {
                     this.func_148132_a(p_148128_1_ - var8, p_148128_2_ - this.field_148153_b + (int)this.field_148169_q - 4);
                     var15 = false;
                  }

                  if (p_148128_1_ >= var5 && p_148128_1_ <= var6) {
                     this.field_148170_p = -1.0F;
                     var19 = this.func_148135_f();
                     if (var19 < 1) {
                        var19 = 1;
                     }

                     var13 = (int)((float)((this.field_148154_c - this.field_148153_b) * (this.field_148154_c - this.field_148153_b)) / (float)this.func_148138_e());
                     if (var13 < 32) {
                        var13 = 32;
                     }

                     if (var13 > this.field_148154_c - this.field_148153_b - 8) {
                        var13 = this.field_148154_c - this.field_148153_b - 8;
                     }

                     this.field_148170_p /= (float)(this.field_148154_c - this.field_148153_b - var13) / (float)var19;
                  } else {
                     this.field_148170_p = 1.0F;
                  }

                  if (var15) {
                     this.field_148157_o = (float)p_148128_2_;
                  } else {
                     this.field_148157_o = -2.0F;
                  }
               } else {
                  this.field_148157_o = -2.0F;
               }
            } else if (this.field_148157_o >= 0.0F) {
               this.field_148169_q -= ((float)p_148128_2_ - this.field_148157_o) * this.field_148170_p;
               this.field_148157_o = (float)p_148128_2_;
            }
         } else {
            try {
               for(; !Minecraft.gameSettings.touchscreen && Mouse.next(); this.field_148161_k.currentScreen.handleMouseInput()) {
                  int var7 = Mouse.getEventDWheel();
                  if (var7 != 0) {
                     if (var7 > 0) {
                        var7 = -1;
                     } else if (var7 < 0) {
                        var7 = 1;
                     }

                     this.field_148169_q += (float)(var7 * this.field_148149_f / 2);
                  }
               }
            } catch (IOException var15) {
               var15.printStackTrace();
            }

            this.field_148157_o = -1.0F;
         }
      }

      this.func_148121_k();
      GL11.glDisable(2896);
      GL11.glDisable(2912);
      Tessellator ts = Tessellator.getInstance();
      WorldRenderer wr = ts.getWorldRenderer();
      Wrapper.mc.getTextureManager().bindTexture(Texture.backgroundImage);
      Gui.drawScaledCustomSizeModalRect(0, 0, 0.0F, 0.0F, this.field_148152_e, this.field_148153_b, this.field_148152_e, this.field_148153_b, (float)this.field_148152_e, (float)this.field_148153_b);
      var9 = this.field_148152_e + this.field_148155_a / 2 - this.func_148139_c() / 2 + 2;
      var10 = this.field_148153_b + 4 - (int)this.field_148169_q;
      if (this.field_148165_u) {
         this.func_148129_a(var9, var10, Tessellator.getInstance());
      }

      this.func_148120_b(var9, var10, p_148128_1_, p_148128_2_);
      GL11.glDisable(2929);
      byte var18 = 4;
      this.func_148136_c(0, this.field_148153_b, 255, 255);
      this.func_148136_c(this.field_148154_c, this.field_148158_l, 255, 255);
      GL11.glEnable(3042);
      OpenGlHelper.glBlendFunc(770, 771, 0, 1);
      GL11.glDisable(3008);
      GL11.glShadeModel(7425);
      GL11.glDisable(3553);
      wr.startDrawingQuads();
      wr.func_178974_a(0, 0);
      wr.addVertexWithUV((double)this.field_148152_e, (double)(this.field_148153_b + var18), 0.0D, 0.0D, 1.0D);
      wr.addVertexWithUV((double)this.field_148151_d, (double)(this.field_148153_b + var18), 0.0D, 1.0D, 1.0D);
      wr.func_178974_a(0, 255);
      wr.addVertexWithUV((double)this.field_148151_d, (double)this.field_148153_b, 0.0D, 1.0D, 0.0D);
      wr.addVertexWithUV((double)this.field_148152_e, (double)this.field_148153_b, 0.0D, 0.0D, 0.0D);
      ts.draw();
      wr.startDrawingQuads();
      wr.func_178974_a(0, 255);
      wr.addVertexWithUV((double)this.field_148152_e, (double)this.field_148154_c, 0.0D, 0.0D, 1.0D);
      wr.addVertexWithUV((double)this.field_148151_d, (double)this.field_148154_c, 0.0D, 1.0D, 1.0D);
      wr.func_178974_a(0, 0);
      wr.addVertexWithUV((double)this.field_148151_d, (double)(this.field_148154_c - var18), 0.0D, 1.0D, 0.0D);
      wr.addVertexWithUV((double)this.field_148152_e, (double)(this.field_148154_c - var18), 0.0D, 0.0D, 0.0D);
      ts.draw();
      var19 = this.func_148135_f();
      if (var19 > 0) {
         var13 = (this.field_148154_c - this.field_148153_b) * (this.field_148154_c - this.field_148153_b) / this.func_148138_e();
         if (var13 < 32) {
            var13 = 32;
         }

         if (var13 > this.field_148154_c - this.field_148153_b - 8) {
            var13 = this.field_148154_c - this.field_148153_b - 8;
         }

         int var14 = (int)this.field_148169_q * (this.field_148154_c - this.field_148153_b - var13) / var19 + this.field_148153_b;
         if (var14 < this.field_148153_b) {
            var14 = this.field_148153_b;
         }

         wr.startDrawingQuads();
         wr.func_178974_a(0, 255);
         wr.addVertexWithUV((double)var5, (double)this.field_148154_c, 0.0D, 0.0D, 1.0D);
         wr.addVertexWithUV((double)var6, (double)this.field_148154_c, 0.0D, 1.0D, 1.0D);
         wr.addVertexWithUV((double)var6, (double)this.field_148153_b, 0.0D, 1.0D, 0.0D);
         wr.addVertexWithUV((double)var5, (double)this.field_148153_b, 0.0D, 0.0D, 0.0D);
         ts.draw();
         wr.startDrawingQuads();
         wr.func_178974_a(8421504, 255);
         wr.addVertexWithUV((double)var5, (double)(var14 + var13), 0.0D, 0.0D, 1.0D);
         wr.addVertexWithUV((double)var6, (double)(var14 + var13), 0.0D, 1.0D, 1.0D);
         wr.addVertexWithUV((double)var6, (double)var14, 0.0D, 1.0D, 0.0D);
         wr.addVertexWithUV((double)var5, (double)var14, 0.0D, 0.0D, 0.0D);
         ts.draw();
         wr.startDrawingQuads();
         wr.func_178974_a(12632256, 255);
         wr.addVertexWithUV((double)var5, (double)(var14 + var13 - 1), 0.0D, 0.0D, 1.0D);
         wr.addVertexWithUV((double)(var6 - 1), (double)(var14 + var13 - 1), 0.0D, 1.0D, 1.0D);
         wr.addVertexWithUV((double)(var6 - 1), (double)var14, 0.0D, 1.0D, 0.0D);
         wr.addVertexWithUV((double)var5, (double)var14, 0.0D, 0.0D, 0.0D);
         ts.draw();
      }

      this.func_148142_b(p_148128_1_, p_148128_2_);
      GL11.glEnable(3553);
      GL11.glShadeModel(7424);
      GL11.glEnable(3008);
      GL11.glDisable(3042);
   }

   public void func_148143_b(boolean p_148143_1_) {
      this.field_148164_v = p_148143_1_;
   }

   public boolean func_148125_i() {
      return this.field_148164_v;
   }

   public int func_148139_c() {
      return 250;
   }

   protected void func_148120_b(int p_148120_1_, int p_148120_2_, int p_148120_3_, int p_148120_4_) {
      int var5 = this.getSize();
      Tessellator ts = Tessellator.getInstance();
      WorldRenderer wr = ts.getWorldRenderer();

      for(int var7 = 0; var7 < var5; ++var7) {
         int var8 = p_148120_2_ + var7 * this.field_148149_f + this.field_148160_j;
         int var9 = this.field_148149_f - 4;
         if (var8 <= this.field_148154_c && var8 + var9 >= this.field_148153_b) {
            if (this.field_148166_t && this.isSelected(var7)) {
               int var10 = this.field_148152_e + this.field_148155_a / 2 - this.func_148139_c() / 2;
               int var11 = this.field_148152_e + this.field_148155_a / 2 + this.func_148139_c() / 2;
               GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
               GL11.glDisable(3553);
               wr.startDrawingQuads();
               wr.func_178991_c(8421504);
               wr.addVertexWithUV((double)var10, (double)(var8 + var9 + 2), 0.0D, 0.0D, 1.0D);
               wr.addVertexWithUV((double)var11, (double)(var8 + var9 + 2), 0.0D, 1.0D, 1.0D);
               wr.addVertexWithUV((double)var11, (double)(var8 - 2), 0.0D, 1.0D, 0.0D);
               wr.addVertexWithUV((double)var10, (double)(var8 - 2), 0.0D, 0.0D, 0.0D);
               wr.func_178991_c(0);
               wr.addVertexWithUV((double)(var10 + 1), (double)(var8 + var9 + 1), 0.0D, 0.0D, 1.0D);
               wr.addVertexWithUV((double)(var11 - 1), (double)(var8 + var9 + 1), 0.0D, 1.0D, 1.0D);
               wr.addVertexWithUV((double)(var11 - 1), (double)(var8 - 1), 0.0D, 1.0D, 0.0D);
               wr.addVertexWithUV((double)(var10 + 1), (double)(var8 - 1), 0.0D, 0.0D, 0.0D);
               ts.draw();
               GL11.glEnable(3553);
            }

            this.drawSlot(var7, p_148120_1_, var8, var9, p_148120_3_, p_148120_4_);
         }
      }

   }

   protected int func_148137_d() {
      return this.field_148155_a / 2 + 134;
   }

   private void func_148136_c(int p_148136_1_, int p_148136_2_, int p_148136_3_, int p_148136_4_) {
      Tessellator ts = Tessellator.getInstance();
      WorldRenderer wr = ts.getWorldRenderer();
      this.field_148161_k.getTextureManager().bindTexture(Texture.transperent32);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      float var6 = 32.0F;
      wr.startDrawingQuads();
      wr.func_178974_a(4210752, p_148136_4_);
      wr.addVertexWithUV((double)this.field_148152_e, (double)p_148136_2_, 0.0D, 0.0D, (double)((float)p_148136_2_ / var6));
      wr.addVertexWithUV((double)(this.field_148152_e + this.field_148155_a), (double)p_148136_2_, 0.0D, (double)((float)this.field_148155_a / var6), (double)((float)p_148136_2_ / var6));
      wr.func_178974_a(4210752, p_148136_3_);
      wr.addVertexWithUV((double)(this.field_148152_e + this.field_148155_a), (double)p_148136_1_, 0.0D, (double)((float)this.field_148155_a / var6), (double)((float)p_148136_1_ / var6));
      wr.addVertexWithUV((double)this.field_148152_e, (double)p_148136_1_, 0.0D, 0.0D, (double)((float)p_148136_1_ / var6));
      ts.draw();
   }

   public void func_148140_g(int p_148140_1_) {
      this.field_148152_e = p_148140_1_;
      this.field_148151_d = p_148140_1_ + this.field_148155_a;
   }

   public int func_148146_j() {
      return this.field_148149_f;
   }
}
