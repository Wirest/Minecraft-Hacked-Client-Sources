package net.minecraft.client.gui.stream;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.stream.IStream;
import net.minecraft.client.stream.NullStream;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Session;
import net.minecraft.util.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import tv.twitch.ErrorCode;

public class GuiStreamUnavailable extends GuiScreen {
   private static final Logger field_152322_a = LogManager.getLogger();
   private final IChatComponent field_152324_f;
   private final GuiScreen parentScreen;
   private final GuiStreamUnavailable.Reason field_152326_h;
   private final List field_152327_i;
   private final List field_152323_r;
   // $FF: synthetic field
   private static int[] $SWITCH_TABLE$net$minecraft$client$gui$stream$GuiStreamUnavailable$Reason;
   // $FF: synthetic field
   private static int[] $SWITCH_TABLE$net$minecraft$util$Util$EnumOS;
   // $FF: synthetic field
   private static int[] $SWITCH_TABLE$net$minecraft$client$stream$IStream$AuthFailureReason;

   public GuiStreamUnavailable(GuiScreen p_i1070_1_, GuiStreamUnavailable.Reason p_i1070_2_) {
      this(p_i1070_1_, p_i1070_2_, (List)null);
   }

   public GuiStreamUnavailable(GuiScreen parentScreenIn, GuiStreamUnavailable.Reason p_i46311_2_, List p_i46311_3_) {
      this.field_152324_f = new ChatComponentTranslation("stream.unavailable.title", new Object[0]);
      this.field_152323_r = Lists.newArrayList();
      this.parentScreen = parentScreenIn;
      this.field_152326_h = p_i46311_2_;
      this.field_152327_i = p_i46311_3_;
   }

   public void initGui() {
      if (this.field_152323_r.isEmpty()) {
         this.field_152323_r.addAll(this.fontRendererObj.listFormattedStringToWidth(this.field_152326_h.func_152561_a().getFormattedText(), (int)((float)this.width * 0.75F)));
         if (this.field_152327_i != null) {
            this.field_152323_r.add("");
            Iterator var2 = this.field_152327_i.iterator();

            while(var2.hasNext()) {
               ChatComponentTranslation chatcomponenttranslation = (ChatComponentTranslation)var2.next();
               this.field_152323_r.add(chatcomponenttranslation.getUnformattedTextForChat());
            }
         }
      }

      if (this.field_152326_h.func_152559_b() != null) {
         this.buttonList.add(new GuiButton(0, this.width / 2 - 155, this.height - 50, 150, 20, I18n.format("gui.cancel")));
         this.buttonList.add(new GuiButton(1, this.width / 2 - 155 + 160, this.height - 50, 150, 20, I18n.format(this.field_152326_h.func_152559_b().getFormattedText())));
      } else {
         this.buttonList.add(new GuiButton(0, this.width / 2 - 75, this.height - 50, 150, 20, I18n.format("gui.cancel")));
      }

   }

   public void onGuiClosed() {
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.drawDefaultBackground();
      int i = Math.max((int)((double)this.height * 0.85D / 2.0D - (double)((float)(this.field_152323_r.size() * this.fontRendererObj.FONT_HEIGHT) / 2.0F)), 50);
      this.drawCenteredString(this.fontRendererObj, this.field_152324_f.getFormattedText(), this.width / 2, i - this.fontRendererObj.FONT_HEIGHT * 2, 16777215);

      for(Iterator var6 = this.field_152323_r.iterator(); var6.hasNext(); i += this.fontRendererObj.FONT_HEIGHT) {
         String s = (String)var6.next();
         this.drawCenteredString(this.fontRendererObj, s, this.width / 2, i, 10526880);
      }

      super.drawScreen(mouseX, mouseY, partialTicks);
   }

   protected void actionPerformed(GuiButton button) throws IOException {
      if (button.enabled) {
         if (button.id == 1) {
            switch($SWITCH_TABLE$net$minecraft$client$gui$stream$GuiStreamUnavailable$Reason()[this.field_152326_h.ordinal()]) {
            case 3:
            case 11:
            case 12:
               this.func_152320_a("http://bugs.mojang.com/browse/MC");
            case 4:
            case 6:
            case 10:
            default:
               break;
            case 5:
               this.func_152320_a("http://www.apple.com/osx/");
               break;
            case 7:
               this.func_152320_a("https://account.mojang.com/migrate");
               break;
            case 8:
            case 9:
               this.func_152320_a("https://account.mojang.com/me/settings");
            }
         }

         this.mc.displayGuiScreen(this.parentScreen);
      }

   }

   private void func_152320_a(String p_152320_1_) {
      try {
         Class oclass = Class.forName("java.awt.Desktop");
         Object object = oclass.getMethod("getDesktop").invoke((Object)null);
         oclass.getMethod("browse", URI.class).invoke(object, new URI(p_152320_1_));
      } catch (Throwable var4) {
         field_152322_a.error("Couldn't open link", var4);
      }

   }

   public static void func_152321_a(GuiScreen p_152321_0_) {
      Minecraft minecraft = Minecraft.getMinecraft();
      IStream istream = minecraft.getTwitchStream();
      if (!OpenGlHelper.framebufferSupported) {
         List list = Lists.newArrayList();
         list.add(new ChatComponentTranslation("stream.unavailable.no_fbo.version", new Object[]{GL11.glGetString(7938)}));
         list.add(new ChatComponentTranslation("stream.unavailable.no_fbo.blend", new Object[]{GLContext.getCapabilities().GL_EXT_blend_func_separate}));
         list.add(new ChatComponentTranslation("stream.unavailable.no_fbo.arb", new Object[]{GLContext.getCapabilities().GL_ARB_framebuffer_object}));
         list.add(new ChatComponentTranslation("stream.unavailable.no_fbo.ext", new Object[]{GLContext.getCapabilities().GL_EXT_framebuffer_object}));
         minecraft.displayGuiScreen(new GuiStreamUnavailable(p_152321_0_, GuiStreamUnavailable.Reason.NO_FBO, list));
      } else if (istream instanceof NullStream) {
         if (((NullStream)istream).func_152937_a().getMessage().contains("Can't load AMD 64-bit .dll on a IA 32-bit platform")) {
            minecraft.displayGuiScreen(new GuiStreamUnavailable(p_152321_0_, GuiStreamUnavailable.Reason.LIBRARY_ARCH_MISMATCH));
         } else {
            minecraft.displayGuiScreen(new GuiStreamUnavailable(p_152321_0_, GuiStreamUnavailable.Reason.LIBRARY_FAILURE));
         }
      } else if (!istream.func_152928_D() && istream.func_152912_E() == ErrorCode.TTV_EC_OS_TOO_OLD) {
         switch($SWITCH_TABLE$net$minecraft$util$Util$EnumOS()[Util.getOSType().ordinal()]) {
         case 3:
            minecraft.displayGuiScreen(new GuiStreamUnavailable(p_152321_0_, GuiStreamUnavailable.Reason.UNSUPPORTED_OS_WINDOWS));
            break;
         case 4:
            minecraft.displayGuiScreen(new GuiStreamUnavailable(p_152321_0_, GuiStreamUnavailable.Reason.UNSUPPORTED_OS_MAC));
            break;
         default:
            minecraft.displayGuiScreen(new GuiStreamUnavailable(p_152321_0_, GuiStreamUnavailable.Reason.UNSUPPORTED_OS_OTHER));
         }
      } else if (!minecraft.getTwitchDetails().containsKey("twitch_access_token")) {
         if (minecraft.getSession().getSessionType() == Session.Type.LEGACY) {
            minecraft.displayGuiScreen(new GuiStreamUnavailable(p_152321_0_, GuiStreamUnavailable.Reason.ACCOUNT_NOT_MIGRATED));
         } else {
            minecraft.displayGuiScreen(new GuiStreamUnavailable(p_152321_0_, GuiStreamUnavailable.Reason.ACCOUNT_NOT_BOUND));
         }
      } else if (!istream.func_152913_F()) {
         switch($SWITCH_TABLE$net$minecraft$client$stream$IStream$AuthFailureReason()[istream.func_152918_H().ordinal()]) {
         case 1:
         default:
            minecraft.displayGuiScreen(new GuiStreamUnavailable(p_152321_0_, GuiStreamUnavailable.Reason.FAILED_TWITCH_AUTH_ERROR));
            break;
         case 2:
            minecraft.displayGuiScreen(new GuiStreamUnavailable(p_152321_0_, GuiStreamUnavailable.Reason.FAILED_TWITCH_AUTH));
         }
      } else if (istream.func_152912_E() != null) {
         List list1 = Arrays.asList(new ChatComponentTranslation("stream.unavailable.initialization_failure.extra", new Object[]{ErrorCode.getString(istream.func_152912_E())}));
         minecraft.displayGuiScreen(new GuiStreamUnavailable(p_152321_0_, GuiStreamUnavailable.Reason.INITIALIZATION_FAILURE, list1));
      } else {
         minecraft.displayGuiScreen(new GuiStreamUnavailable(p_152321_0_, GuiStreamUnavailable.Reason.UNKNOWN));
      }

   }

   // $FF: synthetic method
   static int[] $SWITCH_TABLE$net$minecraft$client$gui$stream$GuiStreamUnavailable$Reason() {
      int[] var10000 = $SWITCH_TABLE$net$minecraft$client$gui$stream$GuiStreamUnavailable$Reason;
      if ($SWITCH_TABLE$net$minecraft$client$gui$stream$GuiStreamUnavailable$Reason != null) {
         return var10000;
      } else {
         int[] var0 = new int[GuiStreamUnavailable.Reason.values().length];

         try {
            var0[GuiStreamUnavailable.Reason.ACCOUNT_NOT_BOUND.ordinal()] = 8;
         } catch (NoSuchFieldError var12) {
            ;
         }

         try {
            var0[GuiStreamUnavailable.Reason.ACCOUNT_NOT_MIGRATED.ordinal()] = 7;
         } catch (NoSuchFieldError var11) {
            ;
         }

         try {
            var0[GuiStreamUnavailable.Reason.FAILED_TWITCH_AUTH.ordinal()] = 9;
         } catch (NoSuchFieldError var10) {
            ;
         }

         try {
            var0[GuiStreamUnavailable.Reason.FAILED_TWITCH_AUTH_ERROR.ordinal()] = 10;
         } catch (NoSuchFieldError var9) {
            ;
         }

         try {
            var0[GuiStreamUnavailable.Reason.INITIALIZATION_FAILURE.ordinal()] = 11;
         } catch (NoSuchFieldError var8) {
            ;
         }

         try {
            var0[GuiStreamUnavailable.Reason.LIBRARY_ARCH_MISMATCH.ordinal()] = 2;
         } catch (NoSuchFieldError var7) {
            ;
         }

         try {
            var0[GuiStreamUnavailable.Reason.LIBRARY_FAILURE.ordinal()] = 3;
         } catch (NoSuchFieldError var6) {
            ;
         }

         try {
            var0[GuiStreamUnavailable.Reason.NO_FBO.ordinal()] = 1;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            var0[GuiStreamUnavailable.Reason.UNKNOWN.ordinal()] = 12;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            var0[GuiStreamUnavailable.Reason.UNSUPPORTED_OS_MAC.ordinal()] = 5;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            var0[GuiStreamUnavailable.Reason.UNSUPPORTED_OS_OTHER.ordinal()] = 6;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            var0[GuiStreamUnavailable.Reason.UNSUPPORTED_OS_WINDOWS.ordinal()] = 4;
         } catch (NoSuchFieldError var1) {
            ;
         }

         $SWITCH_TABLE$net$minecraft$client$gui$stream$GuiStreamUnavailable$Reason = var0;
         return var0;
      }
   }

   // $FF: synthetic method
   static int[] $SWITCH_TABLE$net$minecraft$util$Util$EnumOS() {
      int[] var10000 = $SWITCH_TABLE$net$minecraft$util$Util$EnumOS;
      if ($SWITCH_TABLE$net$minecraft$util$Util$EnumOS != null) {
         return var10000;
      } else {
         int[] var0 = new int[Util.EnumOS.values().length];

         try {
            var0[Util.EnumOS.LINUX.ordinal()] = 1;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            var0[Util.EnumOS.OSX.ordinal()] = 4;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            var0[Util.EnumOS.SOLARIS.ordinal()] = 2;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            var0[Util.EnumOS.UNKNOWN.ordinal()] = 5;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            var0[Util.EnumOS.WINDOWS.ordinal()] = 3;
         } catch (NoSuchFieldError var1) {
            ;
         }

         $SWITCH_TABLE$net$minecraft$util$Util$EnumOS = var0;
         return var0;
      }
   }

   // $FF: synthetic method
   static int[] $SWITCH_TABLE$net$minecraft$client$stream$IStream$AuthFailureReason() {
      int[] var10000 = $SWITCH_TABLE$net$minecraft$client$stream$IStream$AuthFailureReason;
      if ($SWITCH_TABLE$net$minecraft$client$stream$IStream$AuthFailureReason != null) {
         return var10000;
      } else {
         int[] var0 = new int[IStream.AuthFailureReason.values().length];

         try {
            var0[IStream.AuthFailureReason.ERROR.ordinal()] = 1;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            var0[IStream.AuthFailureReason.INVALID_TOKEN.ordinal()] = 2;
         } catch (NoSuchFieldError var1) {
            ;
         }

         $SWITCH_TABLE$net$minecraft$client$stream$IStream$AuthFailureReason = var0;
         return var0;
      }
   }

   public static enum Reason {
      NO_FBO(new ChatComponentTranslation("stream.unavailable.no_fbo", new Object[0])),
      LIBRARY_ARCH_MISMATCH(new ChatComponentTranslation("stream.unavailable.library_arch_mismatch", new Object[0])),
      LIBRARY_FAILURE(new ChatComponentTranslation("stream.unavailable.library_failure", new Object[0]), new ChatComponentTranslation("stream.unavailable.report_to_mojang", new Object[0])),
      UNSUPPORTED_OS_WINDOWS(new ChatComponentTranslation("stream.unavailable.not_supported.windows", new Object[0])),
      UNSUPPORTED_OS_MAC(new ChatComponentTranslation("stream.unavailable.not_supported.mac", new Object[0]), new ChatComponentTranslation("stream.unavailable.not_supported.mac.okay", new Object[0])),
      UNSUPPORTED_OS_OTHER(new ChatComponentTranslation("stream.unavailable.not_supported.other", new Object[0])),
      ACCOUNT_NOT_MIGRATED(new ChatComponentTranslation("stream.unavailable.account_not_migrated", new Object[0]), new ChatComponentTranslation("stream.unavailable.account_not_migrated.okay", new Object[0])),
      ACCOUNT_NOT_BOUND(new ChatComponentTranslation("stream.unavailable.account_not_bound", new Object[0]), new ChatComponentTranslation("stream.unavailable.account_not_bound.okay", new Object[0])),
      FAILED_TWITCH_AUTH(new ChatComponentTranslation("stream.unavailable.failed_auth", new Object[0]), new ChatComponentTranslation("stream.unavailable.failed_auth.okay", new Object[0])),
      FAILED_TWITCH_AUTH_ERROR(new ChatComponentTranslation("stream.unavailable.failed_auth_error", new Object[0])),
      INITIALIZATION_FAILURE(new ChatComponentTranslation("stream.unavailable.initialization_failure", new Object[0]), new ChatComponentTranslation("stream.unavailable.report_to_mojang", new Object[0])),
      UNKNOWN(new ChatComponentTranslation("stream.unavailable.unknown", new Object[0]), new ChatComponentTranslation("stream.unavailable.report_to_mojang", new Object[0]));

      private final IChatComponent field_152574_m;
      private final IChatComponent field_152575_n;

      private Reason(IChatComponent p_i1066_3_) {
         this(p_i1066_3_, (IChatComponent)null);
      }

      private Reason(IChatComponent p_i1067_3_, IChatComponent p_i1067_4_) {
         this.field_152574_m = p_i1067_3_;
         this.field_152575_n = p_i1067_4_;
      }

      public IChatComponent func_152561_a() {
         return this.field_152574_m;
      }

      public IChatComponent func_152559_b() {
         return this.field_152575_n;
      }
   }
}
