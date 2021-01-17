// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui.stream;

import net.minecraft.client.stream.IStream;
import java.util.Arrays;
import net.minecraft.util.Session;
import net.minecraft.util.Util;
import tv.twitch.ErrorCode;
import net.minecraft.client.stream.NullStream;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.Minecraft;
import java.net.URI;
import java.io.IOException;
import java.util.Iterator;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import java.util.Collection;
import com.google.common.collect.Lists;
import org.apache.logging.log4j.LogManager;
import net.minecraft.util.ChatComponentTranslation;
import java.util.List;
import net.minecraft.util.IChatComponent;
import org.apache.logging.log4j.Logger;
import net.minecraft.client.gui.GuiScreen;

public class GuiStreamUnavailable extends GuiScreen
{
    private static final Logger field_152322_a;
    private final IChatComponent field_152324_f;
    private final GuiScreen parentScreen;
    private final Reason field_152326_h;
    private final List<ChatComponentTranslation> field_152327_i;
    private final List<String> field_152323_r;
    
    static {
        field_152322_a = LogManager.getLogger();
    }
    
    public GuiStreamUnavailable(final GuiScreen p_i1070_1_, final Reason p_i1070_2_) {
        this(p_i1070_1_, p_i1070_2_, null);
    }
    
    public GuiStreamUnavailable(final GuiScreen parentScreenIn, final Reason p_i46311_2_, final List<ChatComponentTranslation> p_i46311_3_) {
        this.field_152324_f = new ChatComponentTranslation("stream.unavailable.title", new Object[0]);
        this.field_152323_r = (List<String>)Lists.newArrayList();
        this.parentScreen = parentScreenIn;
        this.field_152326_h = p_i46311_2_;
        this.field_152327_i = p_i46311_3_;
    }
    
    @Override
    public void initGui() {
        if (this.field_152323_r.isEmpty()) {
            this.field_152323_r.addAll(this.fontRendererObj.listFormattedStringToWidth(this.field_152326_h.func_152561_a().getFormattedText(), (int)(this.width * 0.75f)));
            if (this.field_152327_i != null) {
                this.field_152323_r.add("");
                for (final ChatComponentTranslation chatcomponenttranslation : this.field_152327_i) {
                    this.field_152323_r.add(chatcomponenttranslation.getUnformattedTextForChat());
                }
            }
        }
        if (this.field_152326_h.func_152559_b() != null) {
            this.buttonList.add(new GuiButton(0, this.width / 2 - 155, this.height - 50, 150, 20, I18n.format("gui.cancel", new Object[0])));
            this.buttonList.add(new GuiButton(1, this.width / 2 - 155 + 160, this.height - 50, 150, 20, I18n.format(this.field_152326_h.func_152559_b().getFormattedText(), new Object[0])));
        }
        else {
            this.buttonList.add(new GuiButton(0, this.width / 2 - 75, this.height - 50, 150, 20, I18n.format("gui.cancel", new Object[0])));
        }
    }
    
    @Override
    public void onGuiClosed() {
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        int i = Math.max((int)(this.height * 0.85 / 2.0 - this.field_152323_r.size() * this.fontRendererObj.FONT_HEIGHT / 2.0f), 50);
        this.drawCenteredString(this.fontRendererObj, this.field_152324_f.getFormattedText(), this.width / 2, i - this.fontRendererObj.FONT_HEIGHT * 2, 16777215);
        for (final String s : this.field_152323_r) {
            this.drawCenteredString(this.fontRendererObj, s, this.width / 2, i, 10526880);
            i += this.fontRendererObj.FONT_HEIGHT;
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.enabled) {
            if (button.id == 1) {
                switch (this.field_152326_h) {
                    case ACCOUNT_NOT_BOUND:
                    case FAILED_TWITCH_AUTH: {
                        this.func_152320_a("https://account.mojang.com/me/settings");
                        break;
                    }
                    case ACCOUNT_NOT_MIGRATED: {
                        this.func_152320_a("https://account.mojang.com/migrate");
                        break;
                    }
                    case UNSUPPORTED_OS_MAC: {
                        this.func_152320_a("http://www.apple.com/osx/");
                        break;
                    }
                    case LIBRARY_FAILURE:
                    case INITIALIZATION_FAILURE:
                    case UNKNOWN: {
                        this.func_152320_a("http://bugs.mojang.com/browse/MC");
                        break;
                    }
                }
            }
            this.mc.displayGuiScreen(this.parentScreen);
        }
    }
    
    private void func_152320_a(final String p_152320_1_) {
        try {
            final Class<?> oclass = Class.forName("java.awt.Desktop");
            final Object object = oclass.getMethod("getDesktop", (Class<?>[])new Class[0]).invoke(null, new Object[0]);
            oclass.getMethod("browse", URI.class).invoke(object, new URI(p_152320_1_));
        }
        catch (Throwable throwable) {
            GuiStreamUnavailable.field_152322_a.error("Couldn't open link", throwable);
        }
    }
    
    public static void func_152321_a(final GuiScreen p_152321_0_) {
        final Minecraft minecraft = Minecraft.getMinecraft();
        final IStream istream = minecraft.getTwitchStream();
        if (!OpenGlHelper.framebufferSupported) {
            final List<ChatComponentTranslation> list = (List<ChatComponentTranslation>)Lists.newArrayList();
            list.add(new ChatComponentTranslation("stream.unavailable.no_fbo.version", new Object[] { GL11.glGetString(7938) }));
            list.add(new ChatComponentTranslation("stream.unavailable.no_fbo.blend", new Object[] { GLContext.getCapabilities().GL_EXT_blend_func_separate }));
            list.add(new ChatComponentTranslation("stream.unavailable.no_fbo.arb", new Object[] { GLContext.getCapabilities().GL_ARB_framebuffer_object }));
            list.add(new ChatComponentTranslation("stream.unavailable.no_fbo.ext", new Object[] { GLContext.getCapabilities().GL_EXT_framebuffer_object }));
            minecraft.displayGuiScreen(new GuiStreamUnavailable(p_152321_0_, Reason.NO_FBO, list));
        }
        else if (istream instanceof NullStream) {
            if (((NullStream)istream).func_152937_a().getMessage().contains("Can't load AMD 64-bit .dll on a IA 32-bit platform")) {
                minecraft.displayGuiScreen(new GuiStreamUnavailable(p_152321_0_, Reason.LIBRARY_ARCH_MISMATCH));
            }
            else {
                minecraft.displayGuiScreen(new GuiStreamUnavailable(p_152321_0_, Reason.LIBRARY_FAILURE));
            }
        }
        else if (!istream.func_152928_D() && istream.func_152912_E() == ErrorCode.TTV_EC_OS_TOO_OLD) {
            switch (Util.getOSType()) {
                case WINDOWS: {
                    minecraft.displayGuiScreen(new GuiStreamUnavailable(p_152321_0_, Reason.UNSUPPORTED_OS_WINDOWS));
                    break;
                }
                case OSX: {
                    minecraft.displayGuiScreen(new GuiStreamUnavailable(p_152321_0_, Reason.UNSUPPORTED_OS_MAC));
                    break;
                }
                default: {
                    minecraft.displayGuiScreen(new GuiStreamUnavailable(p_152321_0_, Reason.UNSUPPORTED_OS_OTHER));
                    break;
                }
            }
        }
        else if (!minecraft.getTwitchDetails().containsKey("twitch_access_token")) {
            if (minecraft.getSession().getSessionType() == Session.Type.LEGACY) {
                minecraft.displayGuiScreen(new GuiStreamUnavailable(p_152321_0_, Reason.ACCOUNT_NOT_MIGRATED));
            }
            else {
                minecraft.displayGuiScreen(new GuiStreamUnavailable(p_152321_0_, Reason.ACCOUNT_NOT_BOUND));
            }
        }
        else if (!istream.func_152913_F()) {
            switch (istream.func_152918_H()) {
                case INVALID_TOKEN: {
                    minecraft.displayGuiScreen(new GuiStreamUnavailable(p_152321_0_, Reason.FAILED_TWITCH_AUTH));
                    break;
                }
                default: {
                    minecraft.displayGuiScreen(new GuiStreamUnavailable(p_152321_0_, Reason.FAILED_TWITCH_AUTH_ERROR));
                    break;
                }
            }
        }
        else if (istream.func_152912_E() != null) {
            final List<ChatComponentTranslation> list2 = Arrays.asList(new ChatComponentTranslation("stream.unavailable.initialization_failure.extra", new Object[] { ErrorCode.getString(istream.func_152912_E()) }));
            minecraft.displayGuiScreen(new GuiStreamUnavailable(p_152321_0_, Reason.INITIALIZATION_FAILURE, list2));
        }
        else {
            minecraft.displayGuiScreen(new GuiStreamUnavailable(p_152321_0_, Reason.UNKNOWN));
        }
    }
    
    public enum Reason
    {
        NO_FBO("NO_FBO", 0, (IChatComponent)new ChatComponentTranslation("stream.unavailable.no_fbo", new Object[0])), 
        LIBRARY_ARCH_MISMATCH("LIBRARY_ARCH_MISMATCH", 1, (IChatComponent)new ChatComponentTranslation("stream.unavailable.library_arch_mismatch", new Object[0])), 
        LIBRARY_FAILURE("LIBRARY_FAILURE", 2, (IChatComponent)new ChatComponentTranslation("stream.unavailable.library_failure", new Object[0]), (IChatComponent)new ChatComponentTranslation("stream.unavailable.report_to_mojang", new Object[0])), 
        UNSUPPORTED_OS_WINDOWS("UNSUPPORTED_OS_WINDOWS", 3, (IChatComponent)new ChatComponentTranslation("stream.unavailable.not_supported.windows", new Object[0])), 
        UNSUPPORTED_OS_MAC("UNSUPPORTED_OS_MAC", 4, (IChatComponent)new ChatComponentTranslation("stream.unavailable.not_supported.mac", new Object[0]), (IChatComponent)new ChatComponentTranslation("stream.unavailable.not_supported.mac.okay", new Object[0])), 
        UNSUPPORTED_OS_OTHER("UNSUPPORTED_OS_OTHER", 5, (IChatComponent)new ChatComponentTranslation("stream.unavailable.not_supported.other", new Object[0])), 
        ACCOUNT_NOT_MIGRATED("ACCOUNT_NOT_MIGRATED", 6, (IChatComponent)new ChatComponentTranslation("stream.unavailable.account_not_migrated", new Object[0]), (IChatComponent)new ChatComponentTranslation("stream.unavailable.account_not_migrated.okay", new Object[0])), 
        ACCOUNT_NOT_BOUND("ACCOUNT_NOT_BOUND", 7, (IChatComponent)new ChatComponentTranslation("stream.unavailable.account_not_bound", new Object[0]), (IChatComponent)new ChatComponentTranslation("stream.unavailable.account_not_bound.okay", new Object[0])), 
        FAILED_TWITCH_AUTH("FAILED_TWITCH_AUTH", 8, (IChatComponent)new ChatComponentTranslation("stream.unavailable.failed_auth", new Object[0]), (IChatComponent)new ChatComponentTranslation("stream.unavailable.failed_auth.okay", new Object[0])), 
        FAILED_TWITCH_AUTH_ERROR("FAILED_TWITCH_AUTH_ERROR", 9, (IChatComponent)new ChatComponentTranslation("stream.unavailable.failed_auth_error", new Object[0])), 
        INITIALIZATION_FAILURE("INITIALIZATION_FAILURE", 10, (IChatComponent)new ChatComponentTranslation("stream.unavailable.initialization_failure", new Object[0]), (IChatComponent)new ChatComponentTranslation("stream.unavailable.report_to_mojang", new Object[0])), 
        UNKNOWN("UNKNOWN", 11, (IChatComponent)new ChatComponentTranslation("stream.unavailable.unknown", new Object[0]), (IChatComponent)new ChatComponentTranslation("stream.unavailable.report_to_mojang", new Object[0]));
        
        private final IChatComponent field_152574_m;
        private final IChatComponent field_152575_n;
        
        private Reason(final String s, final int n, final IChatComponent p_i1066_3_) {
            this(s, n, p_i1066_3_, null);
        }
        
        private Reason(final String name, final int ordinal, final IChatComponent p_i1067_3_, final IChatComponent p_i1067_4_) {
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
