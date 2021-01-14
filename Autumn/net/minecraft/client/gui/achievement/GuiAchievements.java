package net.minecraft.client.gui.achievement;

import java.io.IOException;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.IProgressMeter;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

public class GuiAchievements extends GuiScreen implements IProgressMeter {
   private static final int field_146572_y;
   private static final int field_146571_z;
   private static final int field_146559_A;
   private static final int field_146560_B;
   private static final ResourceLocation ACHIEVEMENT_BACKGROUND;
   protected GuiScreen parentScreen;
   protected int field_146555_f = 256;
   protected int field_146557_g = 202;
   protected int field_146563_h;
   protected int field_146564_i;
   protected float field_146570_r = 1.0F;
   protected double field_146569_s;
   protected double field_146568_t;
   protected double field_146567_u;
   protected double field_146566_v;
   protected double field_146565_w;
   protected double field_146573_x;
   private int field_146554_D;
   private StatFileWriter statFileWriter;
   private boolean loadingAchievements = true;

   public GuiAchievements(GuiScreen parentScreenIn, StatFileWriter statFileWriterIn) {
      this.parentScreen = parentScreenIn;
      this.statFileWriter = statFileWriterIn;
      int i = 141;
      int j = 141;
      this.field_146569_s = this.field_146567_u = this.field_146565_w = (double)(AchievementList.openInventory.displayColumn * 24 - i / 2 - 12);
      this.field_146568_t = this.field_146566_v = this.field_146573_x = (double)(AchievementList.openInventory.displayRow * 24 - j / 2);
   }

   public void initGui() {
      this.mc.getNetHandler().addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.REQUEST_STATS));
      this.buttonList.clear();
      this.buttonList.add(new GuiOptionButton(1, this.width / 2 + 24, this.height / 2 + 74, 80, 20, I18n.format("gui.done")));
   }

   protected void actionPerformed(GuiButton button) throws IOException {
      if (!this.loadingAchievements && button.id == 1) {
         this.mc.displayGuiScreen(this.parentScreen);
      }

   }

   protected void keyTyped(char typedChar, int keyCode) throws IOException {
      if (keyCode == this.mc.gameSettings.keyBindInventory.getKeyCode()) {
         this.mc.displayGuiScreen((GuiScreen)null);
         this.mc.setIngameFocus();
      } else {
         super.keyTyped(typedChar, keyCode);
      }

   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      if (this.loadingAchievements) {
         this.drawDefaultBackground();
         this.drawCenteredString(this.fontRendererObj, I18n.format("multiplayer.downloadingStats"), this.width / 2, this.height / 2, 16777215);
         FontRenderer var10001 = this.fontRendererObj;
         String var10002 = lanSearchStates[(int)(Minecraft.getSystemTime() / 150L % (long)lanSearchStates.length)];
         int var10003 = this.width / 2;
         int var10004 = this.height / 2;
         FontRenderer var10005 = this.fontRendererObj;
         this.drawCenteredString(var10001, var10002, var10003, var10004 + 9 * 2, 16777215);
      } else {
         int i1;
         if (Mouse.isButtonDown(0)) {
            i1 = (this.width - this.field_146555_f) / 2;
            int j = (this.height - this.field_146557_g) / 2;
            int k = i1 + 8;
            int l = j + 17;
            if ((this.field_146554_D == 0 || this.field_146554_D == 1) && mouseX >= k && mouseX < k + 224 && mouseY >= l && mouseY < l + 155) {
               if (this.field_146554_D == 0) {
                  this.field_146554_D = 1;
               } else {
                  this.field_146567_u -= (double)((float)(mouseX - this.field_146563_h) * this.field_146570_r);
                  this.field_146566_v -= (double)((float)(mouseY - this.field_146564_i) * this.field_146570_r);
                  this.field_146565_w = this.field_146569_s = this.field_146567_u;
                  this.field_146573_x = this.field_146568_t = this.field_146566_v;
               }

               this.field_146563_h = mouseX;
               this.field_146564_i = mouseY;
            }
         } else {
            this.field_146554_D = 0;
         }

         i1 = Mouse.getDWheel();
         float f3 = this.field_146570_r;
         if (i1 < 0) {
            this.field_146570_r += 0.25F;
         } else if (i1 > 0) {
            this.field_146570_r -= 0.25F;
         }

         this.field_146570_r = MathHelper.clamp_float(this.field_146570_r, 1.0F, 2.0F);
         if (this.field_146570_r != f3) {
            float var10000 = f3 - this.field_146570_r;
            float f4 = f3 * (float)this.field_146555_f;
            float f = f3 * (float)this.field_146557_g;
            float f1 = this.field_146570_r * (float)this.field_146555_f;
            float f2 = this.field_146570_r * (float)this.field_146557_g;
            this.field_146567_u -= (double)((f1 - f4) * 0.5F);
            this.field_146566_v -= (double)((f2 - f) * 0.5F);
            this.field_146565_w = this.field_146569_s = this.field_146567_u;
            this.field_146573_x = this.field_146568_t = this.field_146566_v;
         }

         if (this.field_146565_w < (double)field_146572_y) {
            this.field_146565_w = (double)field_146572_y;
         }

         if (this.field_146573_x < (double)field_146571_z) {
            this.field_146573_x = (double)field_146571_z;
         }

         if (this.field_146565_w >= (double)field_146559_A) {
            this.field_146565_w = (double)(field_146559_A - 1);
         }

         if (this.field_146573_x >= (double)field_146560_B) {
            this.field_146573_x = (double)(field_146560_B - 1);
         }

         this.drawDefaultBackground();
         this.drawAchievementScreen(mouseX, mouseY, partialTicks);
         GlStateManager.disableLighting();
         GlStateManager.disableDepth();
         this.drawTitle();
         GlStateManager.enableLighting();
         GlStateManager.enableDepth();
      }

   }

   public void doneLoading() {
      if (this.loadingAchievements) {
         this.loadingAchievements = false;
      }

   }

   public void updateScreen() {
      if (!this.loadingAchievements) {
         this.field_146569_s = this.field_146567_u;
         this.field_146568_t = this.field_146566_v;
         double d0 = this.field_146565_w - this.field_146567_u;
         double d1 = this.field_146573_x - this.field_146566_v;
         if (d0 * d0 + d1 * d1 < 4.0D) {
            this.field_146567_u += d0;
            this.field_146566_v += d1;
         } else {
            this.field_146567_u += d0 * 0.85D;
            this.field_146566_v += d1 * 0.85D;
         }
      }

   }

   protected void drawTitle() {
      int i = (this.width - this.field_146555_f) / 2;
      int j = (this.height - this.field_146557_g) / 2;
      this.fontRendererObj.drawString(I18n.format("gui.achievements"), i + 15, j + 5, 4210752);
   }

   protected void drawAchievementScreen(int p_146552_1_, int p_146552_2_, float p_146552_3_) {
      int i = MathHelper.floor_double(this.field_146569_s + (this.field_146567_u - this.field_146569_s) * (double)p_146552_3_);
      int j = MathHelper.floor_double(this.field_146568_t + (this.field_146566_v - this.field_146568_t) * (double)p_146552_3_);
      if (i < field_146572_y) {
         i = field_146572_y;
      }

      if (j < field_146571_z) {
         j = field_146571_z;
      }

      if (i >= field_146559_A) {
         i = field_146559_A - 1;
      }

      if (j >= field_146560_B) {
         j = field_146560_B - 1;
      }

      int k = (this.width - this.field_146555_f) / 2;
      int l = (this.height - this.field_146557_g) / 2;
      int i1 = k + 16;
      int j1 = l + 17;
      this.zLevel = 0.0F;
      GlStateManager.depthFunc(518);
      GlStateManager.pushMatrix();
      GlStateManager.translate((float)i1, (float)j1, -200.0F);
      GlStateManager.scale(1.0F / this.field_146570_r, 1.0F / this.field_146570_r, 0.0F);
      GlStateManager.enableTexture2D();
      GlStateManager.disableLighting();
      GlStateManager.enableRescaleNormal();
      GlStateManager.enableColorMaterial();
      int k1 = i + 288 >> 4;
      int l1 = j + 288 >> 4;
      int i2 = (i + 288) % 16;
      int j2 = (j + 288) % 16;
      int k2 = true;
      int l2 = true;
      int i3 = true;
      int j3 = true;
      int k3 = true;
      Random random = new Random();
      float f = 16.0F / this.field_146570_r;
      float f1 = 16.0F / this.field_146570_r;

      int l3;
      float f3;
      int k5;
      int l5;
      for(l3 = 0; (float)l3 * f - (float)j2 < 155.0F; ++l3) {
         f3 = 0.6F - (float)(l1 + l3) / 25.0F * 0.3F;
         GlStateManager.color(f3, f3, f3, 1.0F);

         for(k5 = 0; (float)k5 * f1 - (float)i2 < 224.0F; ++k5) {
            random.setSeed((long)(this.mc.getSession().getPlayerID().hashCode() + k1 + k5 + (l1 + l3) * 16));
            l5 = random.nextInt(1 + l1 + l3) + (l1 + l3) / 2;
            TextureAtlasSprite textureatlassprite = this.func_175371_a(Blocks.sand);
            if (l5 <= 37 && l1 + l3 != 35) {
               if (l5 == 22) {
                  if (random.nextInt(2) == 0) {
                     textureatlassprite = this.func_175371_a(Blocks.diamond_ore);
                  } else {
                     textureatlassprite = this.func_175371_a(Blocks.redstone_ore);
                  }
               } else if (l5 == 10) {
                  textureatlassprite = this.func_175371_a(Blocks.iron_ore);
               } else if (l5 == 8) {
                  textureatlassprite = this.func_175371_a(Blocks.coal_ore);
               } else if (l5 > 4) {
                  textureatlassprite = this.func_175371_a(Blocks.stone);
               } else if (l5 > 0) {
                  textureatlassprite = this.func_175371_a(Blocks.dirt);
               }
            } else {
               Block block = Blocks.bedrock;
               textureatlassprite = this.func_175371_a(block);
            }

            this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
            this.drawTexturedModalRect(k5 * 16 - i2, l3 * 16 - j2, textureatlassprite, 16, 16);
         }
      }

      GlStateManager.enableDepth();
      GlStateManager.depthFunc(515);
      this.mc.getTextureManager().bindTexture(ACHIEVEMENT_BACKGROUND);

      int l8;
      int i9;
      int l6;
      for(l3 = 0; l3 < AchievementList.achievementList.size(); ++l3) {
         Achievement achievement1 = (Achievement)AchievementList.achievementList.get(l3);
         if (achievement1.parentAchievement != null) {
            k5 = achievement1.displayColumn * 24 - i + 11;
            l5 = achievement1.displayRow * 24 - j + 11;
            int j6 = achievement1.parentAchievement.displayColumn * 24 - i + 11;
            l6 = achievement1.parentAchievement.displayRow * 24 - j + 11;
            boolean flag = this.statFileWriter.hasAchievementUnlocked(achievement1);
            boolean flag1 = this.statFileWriter.canUnlockAchievement(achievement1);
            l8 = this.statFileWriter.func_150874_c(achievement1);
            if (l8 <= 4) {
               i9 = -16777216;
               if (flag) {
                  i9 = -6250336;
               } else if (flag1) {
                  i9 = -16711936;
               }

               this.drawHorizontalLine(k5, j6, l5, i9);
               this.drawVerticalLine(j6, l5, l6, i9);
               if (k5 > j6) {
                  this.drawTexturedModalRect(k5 - 11 - 7, l5 - 5, 114, 234, 7, 11);
               } else if (k5 < j6) {
                  this.drawTexturedModalRect(k5 + 11, l5 - 5, 107, 234, 7, 11);
               } else if (l5 > l6) {
                  this.drawTexturedModalRect(k5 - 5, l5 - 11 - 7, 96, 234, 11, 7);
               } else if (l5 < l6) {
                  this.drawTexturedModalRect(k5 - 5, l5 + 11, 96, 241, 11, 7);
               }
            }
         }
      }

      Achievement achievement = null;
      f3 = (float)(p_146552_1_ - i1) * this.field_146570_r;
      float f4 = (float)(p_146552_2_ - j1) * this.field_146570_r;
      RenderHelper.enableGUIStandardItemLighting();
      GlStateManager.disableLighting();
      GlStateManager.enableRescaleNormal();
      GlStateManager.enableColorMaterial();

      int j7;
      int l7;
      for(l5 = 0; l5 < AchievementList.achievementList.size(); ++l5) {
         Achievement achievement2 = (Achievement)AchievementList.achievementList.get(l5);
         l6 = achievement2.displayColumn * 24 - i;
         j7 = achievement2.displayRow * 24 - j;
         if (l6 >= -24 && j7 >= -24 && (float)l6 <= 224.0F * this.field_146570_r && (float)j7 <= 155.0F * this.field_146570_r) {
            l7 = this.statFileWriter.func_150874_c(achievement2);
            float f9;
            if (this.statFileWriter.hasAchievementUnlocked(achievement2)) {
               f9 = 0.75F;
               GlStateManager.color(f9, f9, f9, 1.0F);
            } else if (this.statFileWriter.canUnlockAchievement(achievement2)) {
               f9 = 1.0F;
               GlStateManager.color(f9, f9, f9, 1.0F);
            } else if (l7 < 3) {
               f9 = 0.3F;
               GlStateManager.color(f9, f9, f9, 1.0F);
            } else if (l7 == 3) {
               f9 = 0.2F;
               GlStateManager.color(f9, f9, f9, 1.0F);
            } else {
               if (l7 != 4) {
                  continue;
               }

               f9 = 0.1F;
               GlStateManager.color(f9, f9, f9, 1.0F);
            }

            this.mc.getTextureManager().bindTexture(ACHIEVEMENT_BACKGROUND);
            if (achievement2.getSpecial()) {
               this.drawTexturedModalRect(l6 - 2, j7 - 2, 26, 202, 26, 26);
            } else {
               this.drawTexturedModalRect(l6 - 2, j7 - 2, 0, 202, 26, 26);
            }

            if (!this.statFileWriter.canUnlockAchievement(achievement2)) {
               f9 = 0.1F;
               GlStateManager.color(f9, f9, f9, 1.0F);
               this.itemRender.func_175039_a(false);
            }

            GlStateManager.enableLighting();
            GlStateManager.enableCull();
            this.itemRender.renderItemAndEffectIntoGUI(achievement2.theItemStack, l6 + 3, j7 + 3);
            GlStateManager.blendFunc(770, 771);
            GlStateManager.disableLighting();
            if (!this.statFileWriter.canUnlockAchievement(achievement2)) {
               this.itemRender.func_175039_a(true);
            }

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            if (f3 >= (float)l6 && f3 <= (float)(l6 + 22) && f4 >= (float)j7 && f4 <= (float)(j7 + 22)) {
               achievement = achievement2;
            }
         }
      }

      GlStateManager.disableDepth();
      GlStateManager.enableBlend();
      GlStateManager.popMatrix();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(ACHIEVEMENT_BACKGROUND);
      this.drawTexturedModalRect(k, l, 0, 0, this.field_146555_f, this.field_146557_g);
      this.zLevel = 0.0F;
      GlStateManager.depthFunc(515);
      GlStateManager.disableDepth();
      GlStateManager.enableTexture2D();
      super.drawScreen(p_146552_1_, p_146552_2_, p_146552_3_);
      if (achievement != null) {
         String s = achievement.getStatName().getUnformattedText();
         String s1 = achievement.getDescription();
         l6 = p_146552_1_ + 12;
         j7 = p_146552_2_ - 4;
         l7 = this.statFileWriter.func_150874_c(achievement);
         if (this.statFileWriter.canUnlockAchievement(achievement)) {
            l8 = Math.max(this.fontRendererObj.getStringWidth(s), 120);
            i9 = this.fontRendererObj.splitStringWidth(s1, l8);
            if (this.statFileWriter.hasAchievementUnlocked(achievement)) {
               i9 += 12;
            }

            this.drawGradientRect(l6 - 3, j7 - 3, l6 + l8 + 3, j7 + i9 + 3 + 12, -1073741824, -1073741824);
            this.fontRendererObj.drawSplitString(s1, l6, j7 + 12, l8, -6250336);
            if (this.statFileWriter.hasAchievementUnlocked(achievement)) {
               this.fontRendererObj.drawStringWithShadow(I18n.format("achievement.taken"), (float)l6, (float)(j7 + i9 + 4), -7302913);
            }
         } else {
            int j9;
            String s3;
            if (l7 == 3) {
               s = I18n.format("achievement.unknown");
               l8 = Math.max(this.fontRendererObj.getStringWidth(s), 120);
               s3 = (new ChatComponentTranslation("achievement.requires", new Object[]{achievement.parentAchievement.getStatName()})).getUnformattedText();
               j9 = this.fontRendererObj.splitStringWidth(s3, l8);
               this.drawGradientRect(l6 - 3, j7 - 3, l6 + l8 + 3, j7 + j9 + 12 + 3, -1073741824, -1073741824);
               this.fontRendererObj.drawSplitString(s3, l6, j7 + 12, l8, -9416624);
            } else if (l7 < 3) {
               l8 = Math.max(this.fontRendererObj.getStringWidth(s), 120);
               s3 = (new ChatComponentTranslation("achievement.requires", new Object[]{achievement.parentAchievement.getStatName()})).getUnformattedText();
               j9 = this.fontRendererObj.splitStringWidth(s3, l8);
               this.drawGradientRect(l6 - 3, j7 - 3, l6 + l8 + 3, j7 + j9 + 12 + 3, -1073741824, -1073741824);
               this.fontRendererObj.drawSplitString(s3, l6, j7 + 12, l8, -9416624);
            } else {
               s = null;
            }
         }

         if (s != null) {
            this.fontRendererObj.drawStringWithShadow(s, (float)l6, (float)j7, this.statFileWriter.canUnlockAchievement(achievement) ? (achievement.getSpecial() ? -128 : -1) : (achievement.getSpecial() ? -8355776 : -8355712));
         }
      }

      GlStateManager.enableDepth();
      GlStateManager.enableLighting();
      RenderHelper.disableStandardItemLighting();
   }

   private TextureAtlasSprite func_175371_a(Block p_175371_1_) {
      return Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(p_175371_1_.getDefaultState());
   }

   public boolean doesGuiPauseGame() {
      return !this.loadingAchievements;
   }

   static {
      field_146572_y = AchievementList.minDisplayColumn * 24 - 112;
      field_146571_z = AchievementList.minDisplayRow * 24 - 112;
      field_146559_A = AchievementList.maxDisplayColumn * 24 - 77;
      field_146560_B = AchievementList.maxDisplayRow * 24 - 77;
      ACHIEVEMENT_BACKGROUND = new ResourceLocation("textures/gui/achievement/achievement_background.png");
   }
}
