package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.model.ModelBook;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerEnchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnchantmentNameParts;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IWorldNameable;
import net.minecraft.world.World;
import org.lwjgl.util.glu.Project;

public class GuiEnchantment extends GuiContainer
{
    private static final ResourceLocation field_147078_C = new ResourceLocation("textures/gui/container/enchanting_table.png");
    private static final ResourceLocation field_147070_D = new ResourceLocation("textures/entity/enchanting_table_book.png");
    private static final ModelBook field_147072_E = new ModelBook();
    private final InventoryPlayer field_175379_F;
    private Random field_147074_F = new Random();
    private ContainerEnchantment field_147075_G;
    public int field_147073_u;
    public float field_147071_v;
    public float field_147069_w;
    public float field_147082_x;
    public float field_147081_y;
    public float field_147080_z;
    public float field_147076_A;
    ItemStack field_147077_B;
    private final IWorldNameable field_175380_I;
    private static final String __OBFID = "CL_00000757";

    public GuiEnchantment(InventoryPlayer p_i45502_1_, World worldIn, IWorldNameable p_i45502_3_)
    {
        super(new ContainerEnchantment(p_i45502_1_, worldIn));
        this.field_175379_F = p_i45502_1_;
        this.field_147075_G = (ContainerEnchantment)this.inventorySlots;
        this.field_175380_I = p_i45502_3_;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items). Args : mouseX, mouseY
     */
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        this.fontRendererObj.drawString(this.field_175380_I.getDisplayName().getUnformattedText(), 12, 5, 4210752);
        this.fontRendererObj.drawString(this.field_175379_F.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        super.updateScreen();
        this.func_147068_g();
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        int var4 = (this.width - this.xSize) / 2;
        int var5 = (this.height - this.ySize) / 2;

        for (int var6 = 0; var6 < 3; ++var6)
        {
            int var7 = mouseX - (var4 + 60);
            int var8 = mouseY - (var5 + 14 + 19 * var6);

            if (var7 >= 0 && var8 >= 0 && var7 < 108 && var8 < 19 && this.field_147075_G.enchantItem(this.mc.thePlayer, var6))
            {
                this.mc.playerController.sendEnchantPacket(this.field_147075_G.windowId, var6);
            }
        }
    }

    /**
     * Args : renderPartialTicks, mouseX, mouseY
     */
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(field_147078_C);
        int var4 = (this.width - this.xSize) / 2;
        int var5 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var4, var5, 0, 0, this.xSize, this.ySize);
        GlStateManager.pushMatrix();
        GlStateManager.matrixMode(5889);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        ScaledResolution var6 = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        GlStateManager.viewport((var6.getScaledWidth() - 320) / 2 * var6.getScaleFactor(), (var6.getScaledHeight() - 240) / 2 * var6.getScaleFactor(), 320 * var6.getScaleFactor(), 240 * var6.getScaleFactor());
        GlStateManager.translate(-0.34F, 0.23F, 0.0F);
        Project.gluPerspective(90.0F, 1.3333334F, 9.0F, 80.0F);
        float var7 = 1.0F;
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.translate(0.0F, 3.3F, -16.0F);
        GlStateManager.scale(var7, var7, var7);
        float var8 = 5.0F;
        GlStateManager.scale(var8, var8, var8);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(field_147070_D);
        GlStateManager.rotate(20.0F, 1.0F, 0.0F, 0.0F);
        float var9 = this.field_147076_A + (this.field_147080_z - this.field_147076_A) * partialTicks;
        GlStateManager.translate((1.0F - var9) * 0.2F, (1.0F - var9) * 0.1F, (1.0F - var9) * 0.25F);
        GlStateManager.rotate(-(1.0F - var9) * 90.0F - 90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
        float var10 = this.field_147069_w + (this.field_147071_v - this.field_147069_w) * partialTicks + 0.25F;
        float var11 = this.field_147069_w + (this.field_147071_v - this.field_147069_w) * partialTicks + 0.75F;
        var10 = (var10 - (float)MathHelper.truncateDoubleToInt((double)var10)) * 1.6F - 0.3F;
        var11 = (var11 - (float)MathHelper.truncateDoubleToInt((double)var11)) * 1.6F - 0.3F;

        if (var10 < 0.0F)
        {
            var10 = 0.0F;
        }

        if (var11 < 0.0F)
        {
            var11 = 0.0F;
        }

        if (var10 > 1.0F)
        {
            var10 = 1.0F;
        }

        if (var11 > 1.0F)
        {
            var11 = 1.0F;
        }

        GlStateManager.enableRescaleNormal();
        field_147072_E.render((Entity)null, 0.0F, var10, var11, var9, 0.0F, 0.0625F);
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.matrixMode(5889);
        GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        EnchantmentNameParts.func_178176_a().reseedRandomGenerator((long)this.field_147075_G.field_178149_f);
        int var12 = this.field_147075_G.func_178147_e();

        for (int var13 = 0; var13 < 3; ++var13)
        {
            int var14 = var4 + 60;
            int var15 = var14 + 20;
            byte var16 = 86;
            String var17 = EnchantmentNameParts.func_178176_a().generateNewRandomName();
            this.zLevel = 0.0F;
            this.mc.getTextureManager().bindTexture(field_147078_C);
            int var18 = this.field_147075_G.enchantLevels[var13];
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            if (var18 == 0)
            {
                this.drawTexturedModalRect(var14, var5 + 14 + 19 * var13, 0, 185, 108, 19);
            }
            else
            {
                String var19 = "" + var18;
                FontRenderer var20 = this.mc.standardGalacticFontRenderer;
                int var21 = 6839882;

                if ((var12 < var13 + 1 || this.mc.thePlayer.experienceLevel < var18) && !this.mc.thePlayer.capabilities.isCreativeMode)
                {
                    this.drawTexturedModalRect(var14, var5 + 14 + 19 * var13, 0, 185, 108, 19);
                    this.drawTexturedModalRect(var14 + 1, var5 + 15 + 19 * var13, 16 * var13, 239, 16, 16);
                    var20.drawSplitString(var17, var15, var5 + 16 + 19 * var13, var16, (var21 & 16711422) >> 1);
                    var21 = 4226832;
                }
                else
                {
                    int var22 = mouseX - (var4 + 60);
                    int var23 = mouseY - (var5 + 14 + 19 * var13);

                    if (var22 >= 0 && var23 >= 0 && var22 < 108 && var23 < 19)
                    {
                        this.drawTexturedModalRect(var14, var5 + 14 + 19 * var13, 0, 204, 108, 19);
                        var21 = 16777088;
                    }
                    else
                    {
                        this.drawTexturedModalRect(var14, var5 + 14 + 19 * var13, 0, 166, 108, 19);
                    }

                    this.drawTexturedModalRect(var14 + 1, var5 + 15 + 19 * var13, 16 * var13, 223, 16, 16);
                    var20.drawSplitString(var17, var15, var5 + 16 + 19 * var13, var16, var21);
                    var21 = 8453920;
                }

                var20 = this.mc.fontRendererObj;
                var20.func_175063_a(var19, (float)(var15 + 86 - var20.getStringWidth(var19)), (float)(var5 + 16 + 19 * var13 + 7), var21);
            }
        }
    }

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);
        boolean var4 = this.mc.thePlayer.capabilities.isCreativeMode;
        int var5 = this.field_147075_G.func_178147_e();

        for (int var6 = 0; var6 < 3; ++var6)
        {
            int var7 = this.field_147075_G.enchantLevels[var6];
            int var8 = this.field_147075_G.field_178151_h[var6];
            int var9 = var6 + 1;

            if (this.isPointInRegion(60, 14 + 19 * var6, 108, 17, mouseX, mouseY) && var7 > 0 && var8 >= 0)
            {
                ArrayList var10 = Lists.newArrayList();
                String var11;

                if (var8 >= 0 && Enchantment.func_180306_c(var8 & 255) != null)
                {
                    var11 = Enchantment.func_180306_c(var8 & 255).getTranslatedName((var8 & 65280) >> 8);
                    var10.add(EnumChatFormatting.WHITE.toString() + EnumChatFormatting.ITALIC.toString() + I18n.format("container.enchant.clue", new Object[] {var11}));
                }

                if (!var4)
                {
                    if (var8 >= 0)
                    {
                        var10.add("");
                    }

                    if (this.mc.thePlayer.experienceLevel < var7)
                    {
                        var10.add(EnumChatFormatting.RED.toString() + "Level Requirement: " + this.field_147075_G.enchantLevels[var6]);
                    }
                    else
                    {
                        var11 = "";

                        if (var9 == 1)
                        {
                            var11 = I18n.format("container.enchant.lapis.one", new Object[0]);
                        }
                        else
                        {
                            var11 = I18n.format("container.enchant.lapis.many", new Object[] {Integer.valueOf(var9)});
                        }

                        if (var5 >= var9)
                        {
                            var10.add(EnumChatFormatting.GRAY.toString() + "" + var11);
                        }
                        else
                        {
                            var10.add(EnumChatFormatting.RED.toString() + "" + var11);
                        }

                        if (var9 == 1)
                        {
                            var11 = I18n.format("container.enchant.level.one", new Object[0]);
                        }
                        else
                        {
                            var11 = I18n.format("container.enchant.level.many", new Object[] {Integer.valueOf(var9)});
                        }

                        var10.add(EnumChatFormatting.GRAY.toString() + "" + var11);
                    }
                }

                this.drawHoveringText(var10, mouseX, mouseY);
                break;
            }
        }
    }

    public void func_147068_g()
    {
        ItemStack var1 = this.inventorySlots.getSlot(0).getStack();

        if (!ItemStack.areItemStacksEqual(var1, this.field_147077_B))
        {
            this.field_147077_B = var1;

            do
            {
                this.field_147082_x += (float)(this.field_147074_F.nextInt(4) - this.field_147074_F.nextInt(4));
            }
            while (this.field_147071_v <= this.field_147082_x + 1.0F && this.field_147071_v >= this.field_147082_x - 1.0F);
        }

        ++this.field_147073_u;
        this.field_147069_w = this.field_147071_v;
        this.field_147076_A = this.field_147080_z;
        boolean var2 = false;

        for (int var3 = 0; var3 < 3; ++var3)
        {
            if (this.field_147075_G.enchantLevels[var3] != 0)
            {
                var2 = true;
            }
        }

        if (var2)
        {
            this.field_147080_z += 0.2F;
        }
        else
        {
            this.field_147080_z -= 0.2F;
        }

        this.field_147080_z = MathHelper.clamp_float(this.field_147080_z, 0.0F, 1.0F);
        float var5 = (this.field_147082_x - this.field_147071_v) * 0.4F;
        float var4 = 0.2F;
        var5 = MathHelper.clamp_float(var5, -var4, var4);
        this.field_147081_y += (var5 - this.field_147081_y) * 0.9F;
        this.field_147071_v += this.field_147081_y;
    }
}
