package net.minecraft.client.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.Charsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

public class GuiWinGame extends GuiScreen
{
    private static final Logger logger = LogManager.getLogger();
    private static final ResourceLocation MINECRAFT_LOGO = new ResourceLocation("textures/gui/title/minecraft.png");
    private static final ResourceLocation VIGNETTE_TEXTURE = new ResourceLocation("textures/misc/vignette.png");
    private int field_146581_h;
    private List field_146582_i;
    private int field_146579_r;
    private float field_146578_s = 0.5F;

    /**
     * Called from the main game loop to update the screen.
     */
    @Override
	public void updateScreen()
    {
        ++this.field_146581_h;
        float var1 = (this.field_146579_r + this.height + this.height + 24) / this.field_146578_s;

        if (this.field_146581_h > var1)
        {
            this.sendRespawnPacket();
        }
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    @Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if (keyCode == 1)
        {
            this.sendRespawnPacket();
        }
    }

    private void sendRespawnPacket()
    {
        this.mc.thePlayer.sendQueue.addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.PERFORM_RESPAWN));
        this.mc.displayGuiScreen((GuiScreen)null);
    }

    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    @Override
	public boolean doesGuiPauseGame()
    {
        return true;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    @Override
	public void initGui()
    {
        if (this.field_146582_i == null)
        {
            this.field_146582_i = Lists.newArrayList();

            try
            {
                String var1 = "";
                String var2 = "" + EnumChatFormatting.WHITE + EnumChatFormatting.OBFUSCATED + EnumChatFormatting.GREEN + EnumChatFormatting.AQUA;
                short var3 = 274;
                BufferedReader var4 = new BufferedReader(new InputStreamReader(this.mc.getResourceManager().getResource(new ResourceLocation("texts/end.txt")).getInputStream(), Charsets.UTF_8));
                Random var5 = new Random(8124371L);
                int var6;

                while ((var1 = var4.readLine()) != null)
                {
                    String var7;
                    String var8;

                    for (var1 = var1.replaceAll("PLAYERNAME", this.mc.getSession().getUsername()); var1.contains(var2); var1 = var7 + EnumChatFormatting.WHITE + EnumChatFormatting.OBFUSCATED + "XXXXXXXX".substring(0, var5.nextInt(4) + 3) + var8)
                    {
                        var6 = var1.indexOf(var2);
                        var7 = var1.substring(0, var6);
                        var8 = var1.substring(var6 + var2.length());
                    }

                    this.field_146582_i.addAll(this.mc.fontRendererObj.listFormattedStringToWidth(var1, var3));
                    this.field_146582_i.add("");
                }

                for (var6 = 0; var6 < 8; ++var6)
                {
                    this.field_146582_i.add("");
                }

                var4 = new BufferedReader(new InputStreamReader(this.mc.getResourceManager().getResource(new ResourceLocation("texts/credits.txt")).getInputStream(), Charsets.UTF_8));

                while ((var1 = var4.readLine()) != null)
                {
                    var1 = var1.replaceAll("PLAYERNAME", this.mc.getSession().getUsername());
                    var1 = var1.replaceAll("\t", "    ");
                    this.field_146582_i.addAll(this.mc.fontRendererObj.listFormattedStringToWidth(var1, var3));
                    this.field_146582_i.add("");
                }

                this.field_146579_r = this.field_146582_i.size() * 12;
            }
            catch (Exception var9)
            {
                logger.error("Couldn\'t load credits", var9);
            }
        }
    }

    private void drawWinGameScreen(int p_146575_1_, int p_146575_2_, float p_146575_3_)
    {
        Tessellator var4 = Tessellator.getInstance();
        WorldRenderer var5 = var4.getWorldRenderer();
        this.mc.getTextureManager().bindTexture(Gui.optionsBackground);
        var5.startDrawingQuads();
        var5.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F);
        int var6 = this.width;
        float var7 = 0.0F - (this.field_146581_h + p_146575_3_) * 0.5F * this.field_146578_s;
        float var8 = this.height - (this.field_146581_h + p_146575_3_) * 0.5F * this.field_146578_s;
        float var9 = 0.015625F;
        float var10 = (this.field_146581_h + p_146575_3_ - 0.0F) * 0.02F;
        float var11 = (this.field_146579_r + this.height + this.height + 24) / this.field_146578_s;
        float var12 = (var11 - 20.0F - (this.field_146581_h + p_146575_3_)) * 0.005F;

        if (var12 < var10)
        {
            var10 = var12;
        }

        if (var10 > 1.0F)
        {
            var10 = 1.0F;
        }

        var10 *= var10;
        var10 = var10 * 96.0F / 255.0F;
        var5.setColorOpaque_F(var10, var10, var10);
        var5.addVertexWithUV(0.0D, this.height, this.zLevel, 0.0D, var7 * var9);
        var5.addVertexWithUV(var6, this.height, this.zLevel, var6 * var9, var7 * var9);
        var5.addVertexWithUV(var6, 0.0D, this.zLevel, var6 * var9, var8 * var9);
        var5.addVertexWithUV(0.0D, 0.0D, this.zLevel, 0.0D, var8 * var9);
        var4.draw();
    }

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    @Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawWinGameScreen(mouseX, mouseY, partialTicks);
        Tessellator var4 = Tessellator.getInstance();
        WorldRenderer var5 = var4.getWorldRenderer();
        short var6 = 274;
        int var7 = this.width / 2 - var6 / 2;
        int var8 = this.height + 50;
        float var9 = -(this.field_146581_h + partialTicks) * this.field_146578_s;
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0F, var9, 0.0F);
        this.mc.getTextureManager().bindTexture(MINECRAFT_LOGO);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.drawTexturedModalRect(var7, var8, 0, 0, 155, 44);
        this.drawTexturedModalRect(var7 + 155, var8, 0, 45, 155, 44);
        var5.setColorOpaque_I(16777215);
        int var10 = var8 + 200;
        int var11;

        for (var11 = 0; var11 < this.field_146582_i.size(); ++var11)
        {
            if (var11 == this.field_146582_i.size() - 1)
            {
                float var12 = var10 + var9 - (this.height / 2 - 6);

                if (var12 < 0.0F)
                {
                    GlStateManager.translate(0.0F, -var12, 0.0F);
                }
            }

            if (var10 + var9 + 12.0F + 8.0F > 0.0F && var10 + var9 < this.height)
            {
                String var13 = (String)this.field_146582_i.get(var11);

                if (var13.startsWith("[C]"))
                {
                    this.fontRendererObj.drawStringWithShadow(var13.substring(3), var7 + (var6 - this.fontRendererObj.getStringWidth(var13.substring(3))) / 2, var10, 16777215);
                }
                else
                {
                    this.fontRendererObj.fontRandom.setSeed(var11 * 4238972211L + this.field_146581_h / 4);
                    this.fontRendererObj.drawStringWithShadow(var13, var7, var10, 16777215);
                }
            }

            var10 += 12;
        }

        GlStateManager.popMatrix();
        this.mc.getTextureManager().bindTexture(VIGNETTE_TEXTURE);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(0, 769);
        var5.startDrawingQuads();
        var5.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F);
        var11 = this.width;
        int var14 = this.height;
        var5.addVertexWithUV(0.0D, var14, this.zLevel, 0.0D, 1.0D);
        var5.addVertexWithUV(var11, var14, this.zLevel, 1.0D, 1.0D);
        var5.addVertexWithUV(var11, 0.0D, this.zLevel, 1.0D, 0.0D);
        var5.addVertexWithUV(0.0D, 0.0D, this.zLevel, 0.0D, 0.0D);
        var4.draw();
        GlStateManager.disableBlend();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
