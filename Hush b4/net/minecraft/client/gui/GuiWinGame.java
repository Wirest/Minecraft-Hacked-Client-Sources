// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import java.io.InputStream;
import java.util.Collection;
import java.util.Random;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.apache.commons.io.Charsets;
import net.minecraft.util.EnumChatFormatting;
import com.google.common.collect.Lists;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.client.Minecraft;
import java.io.IOException;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.MusicTicker;
import org.apache.logging.log4j.LogManager;
import java.util.List;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Logger;

public class GuiWinGame extends GuiScreen
{
    private static final Logger logger;
    private static final ResourceLocation MINECRAFT_LOGO;
    private static final ResourceLocation VIGNETTE_TEXTURE;
    private int field_146581_h;
    private List<String> field_146582_i;
    private int field_146579_r;
    private float field_146578_s;
    
    static {
        logger = LogManager.getLogger();
        MINECRAFT_LOGO = new ResourceLocation("textures/gui/title/minecraft.png");
        VIGNETTE_TEXTURE = new ResourceLocation("textures/misc/vignette.png");
    }
    
    public GuiWinGame() {
        this.field_146578_s = 0.5f;
    }
    
    @Override
    public void updateScreen() {
        final MusicTicker musicticker = this.mc.func_181535_r();
        final SoundHandler soundhandler = this.mc.getSoundHandler();
        if (this.field_146581_h == 0) {
            musicticker.func_181557_a();
            musicticker.func_181558_a(MusicTicker.MusicType.CREDITS);
            soundhandler.resumeSounds();
        }
        soundhandler.update();
        ++this.field_146581_h;
        final float f = (this.field_146579_r + this.height + this.height + 24) / this.field_146578_s;
        if (this.field_146581_h > f) {
            this.sendRespawnPacket();
        }
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (keyCode == 1) {
            this.sendRespawnPacket();
        }
    }
    
    private void sendRespawnPacket() {
        Minecraft.thePlayer.sendQueue.addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.PERFORM_RESPAWN));
        this.mc.displayGuiScreen(null);
    }
    
    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }
    
    @Override
    public void initGui() {
        if (this.field_146582_i == null) {
            this.field_146582_i = (List<String>)Lists.newArrayList();
            try {
                String s = "";
                final String s2 = new StringBuilder().append(EnumChatFormatting.WHITE).append(EnumChatFormatting.OBFUSCATED).append(EnumChatFormatting.GREEN).append(EnumChatFormatting.AQUA).toString();
                final int i = 274;
                InputStream inputstream = this.mc.getResourceManager().getResource(new ResourceLocation("texts/end.txt")).getInputStream();
                BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputstream, Charsets.UTF_8));
                final Random random = new Random(8124371L);
                while ((s = bufferedreader.readLine()) != null) {
                    String s3;
                    String s4;
                    for (s = s.replaceAll("PLAYERNAME", this.mc.getSession().getUsername()); s.contains(s2); s = String.valueOf(s3) + EnumChatFormatting.WHITE + EnumChatFormatting.OBFUSCATED + "XXXXXXXX".substring(0, random.nextInt(4) + 3) + s4) {
                        final int j = s.indexOf(s2);
                        s3 = s.substring(0, j);
                        s4 = s.substring(j + s2.length());
                    }
                    this.field_146582_i.addAll(this.mc.fontRendererObj.listFormattedStringToWidth(s, i));
                    this.field_146582_i.add("");
                }
                inputstream.close();
                for (int k = 0; k < 8; ++k) {
                    this.field_146582_i.add("");
                }
                inputstream = this.mc.getResourceManager().getResource(new ResourceLocation("texts/credits.txt")).getInputStream();
                bufferedreader = new BufferedReader(new InputStreamReader(inputstream, Charsets.UTF_8));
                while ((s = bufferedreader.readLine()) != null) {
                    s = s.replaceAll("PLAYERNAME", this.mc.getSession().getUsername());
                    s = s.replaceAll("\t", "    ");
                    this.field_146582_i.addAll(this.mc.fontRendererObj.listFormattedStringToWidth(s, i));
                    this.field_146582_i.add("");
                }
                inputstream.close();
                this.field_146579_r = this.field_146582_i.size() * 12;
            }
            catch (Exception exception) {
                GuiWinGame.logger.error("Couldn't load credits", exception);
            }
        }
    }
    
    private void drawWinGameScreen(final int p_146575_1_, final int p_146575_2_, final float p_146575_3_) {
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        this.mc.getTextureManager().bindTexture(Gui.optionsBackground);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        final int i = this.width;
        final float f = 0.0f - (this.field_146581_h + p_146575_3_) * 0.5f * this.field_146578_s;
        final float f2 = this.height - (this.field_146581_h + p_146575_3_) * 0.5f * this.field_146578_s;
        final float f3 = 0.015625f;
        float f4 = (this.field_146581_h + p_146575_3_ - 0.0f) * 0.02f;
        final float f5 = (this.field_146579_r + this.height + this.height + 24) / this.field_146578_s;
        final float f6 = (f5 - 20.0f - (this.field_146581_h + p_146575_3_)) * 0.005f;
        if (f6 < f4) {
            f4 = f6;
        }
        if (f4 > 1.0f) {
            f4 = 1.0f;
        }
        f4 *= f4;
        f4 = f4 * 96.0f / 255.0f;
        worldrenderer.pos(0.0, this.height, this.zLevel).tex(0.0, f * f3).color(f4, f4, f4, 1.0f).endVertex();
        worldrenderer.pos(i, this.height, this.zLevel).tex(i * f3, f * f3).color(f4, f4, f4, 1.0f).endVertex();
        worldrenderer.pos(i, 0.0, this.zLevel).tex(i * f3, f2 * f3).color(f4, f4, f4, 1.0f).endVertex();
        worldrenderer.pos(0.0, 0.0, this.zLevel).tex(0.0, f2 * f3).color(f4, f4, f4, 1.0f).endVertex();
        tessellator.draw();
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawWinGameScreen(mouseX, mouseY, partialTicks);
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        final int i = 274;
        final int j = this.width / 2 - i / 2;
        final int k = this.height + 50;
        final float f = -(this.field_146581_h + partialTicks) * this.field_146578_s;
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0f, f, 0.0f);
        this.mc.getTextureManager().bindTexture(GuiWinGame.MINECRAFT_LOGO);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.drawTexturedModalRect(j, k, 0, 0, 155, 44);
        this.drawTexturedModalRect(j + 155, k, 0, 45, 155, 44);
        int l = k + 200;
        for (int i2 = 0; i2 < this.field_146582_i.size(); ++i2) {
            if (i2 == this.field_146582_i.size() - 1) {
                final float f2 = l + f - (this.height / 2 - 6);
                if (f2 < 0.0f) {
                    GlStateManager.translate(0.0f, -f2, 0.0f);
                }
            }
            if (l + f + 12.0f + 8.0f > 0.0f && l + f < this.height) {
                final String s = this.field_146582_i.get(i2);
                if (s.startsWith("[C]")) {
                    this.fontRendererObj.drawStringWithShadow(s.substring(3), (float)(j + (i - this.fontRendererObj.getStringWidth(s.substring(3))) / 2), (float)l, 16777215);
                }
                else {
                    this.fontRendererObj.fontRandom.setSeed(i2 * 4238972211L + this.field_146581_h / 4);
                    this.fontRendererObj.drawStringWithShadow(s, (float)j, (float)l, 16777215);
                }
            }
            l += 12;
        }
        GlStateManager.popMatrix();
        this.mc.getTextureManager().bindTexture(GuiWinGame.VIGNETTE_TEXTURE);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(0, 769);
        final int j2 = this.width;
        final int k2 = this.height;
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldrenderer.pos(0.0, k2, this.zLevel).tex(0.0, 1.0).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
        worldrenderer.pos(j2, k2, this.zLevel).tex(1.0, 1.0).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
        worldrenderer.pos(j2, 0.0, this.zLevel).tex(1.0, 0.0).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
        worldrenderer.pos(0.0, 0.0, this.zLevel).tex(0.0, 0.0).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
        tessellator.draw();
        GlStateManager.disableBlend();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
