package net.minecraft.client.gui;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerMerchant;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;

public class GuiMerchant extends GuiContainer
{
    private static final Logger logger = LogManager.getLogger();

    /** The GUI texture for the villager merchant GUI. */
    private static final ResourceLocation MERCHANT_GUI_TEXTURE = new ResourceLocation("textures/gui/container/villager.png");

    /** The current IMerchant instance in use for this specific merchant. */
    private IMerchant merchant;

    /** The button which proceeds to the next available merchant recipe. */
    private GuiMerchant.MerchantButton nextButton;

    /** Returns to the previous Merchant recipe if one is applicable. */
    private GuiMerchant.MerchantButton previousButton;

    /**
     * The integer value corresponding to the currently selected merchant recipe.
     */
    private int selectedMerchantRecipe;

    /** The chat component utilized by this GuiMerchant instance. */
    private IChatComponent chatComponent;

    public GuiMerchant(InventoryPlayer p_i45500_1_, IMerchant p_i45500_2_, World worldIn)
    {
        super(new ContainerMerchant(p_i45500_1_, p_i45500_2_, worldIn));
        this.merchant = p_i45500_2_;
        this.chatComponent = p_i45500_2_.getDisplayName();
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    @Override
	public void initGui()
    {
        super.initGui();
        int var1 = (this.width - this.xSize) / 2;
        int var2 = (this.height - this.ySize) / 2;
        this.buttonList.add(this.nextButton = new GuiMerchant.MerchantButton(1, var1 + 120 + 27, var2 + 24 - 1, true));
        this.buttonList.add(this.previousButton = new GuiMerchant.MerchantButton(2, var1 + 36 - 19, var2 + 24 - 1, false));
        this.nextButton.enabled = false;
        this.previousButton.enabled = false;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items). Args : mouseX, mouseY
     */
    @Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        String var3 = this.chatComponent.getUnformattedText();
        this.fontRendererObj.drawString(var3, this.xSize / 2 - this.fontRendererObj.getStringWidth(var3) / 2, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);
    }

    /**
     * Called from the main game loop to update the screen.
     */
    @Override
	public void updateScreen()
    {
        super.updateScreen();
        MerchantRecipeList var1 = this.merchant.getRecipes(this.mc.thePlayer);

        if (var1 != null)
        {
            this.nextButton.enabled = this.selectedMerchantRecipe < var1.size() - 1;
            this.previousButton.enabled = this.selectedMerchantRecipe > 0;
        }
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    @Override
	protected void actionPerformed(GuiButton button) throws IOException
    {
        boolean var2 = false;

        if (button == this.nextButton)
        {
            ++this.selectedMerchantRecipe;
            MerchantRecipeList var3 = this.merchant.getRecipes(this.mc.thePlayer);

            if (var3 != null && this.selectedMerchantRecipe >= var3.size())
            {
                this.selectedMerchantRecipe = var3.size() - 1;
            }

            var2 = true;
        }
        else if (button == this.previousButton)
        {
            --this.selectedMerchantRecipe;

            if (this.selectedMerchantRecipe < 0)
            {
                this.selectedMerchantRecipe = 0;
            }

            var2 = true;
        }

        if (var2)
        {
            ((ContainerMerchant)this.inventorySlots).setCurrentRecipeIndex(this.selectedMerchantRecipe);
            PacketBuffer var4 = new PacketBuffer(Unpooled.buffer());
            var4.writeInt(this.selectedMerchantRecipe);
            this.mc.getNetHandler().addToSendQueue(new C17PacketCustomPayload("MC|TrSel", var4));
        }
    }

    /**
     * Args : renderPartialTicks, mouseX, mouseY
     */
    @Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(MERCHANT_GUI_TEXTURE);
        int var4 = (this.width - this.xSize) / 2;
        int var5 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var4, var5, 0, 0, this.xSize, this.ySize);
        MerchantRecipeList var6 = this.merchant.getRecipes(this.mc.thePlayer);

        if (var6 != null && !var6.isEmpty())
        {
            int var7 = this.selectedMerchantRecipe;

            if (var7 < 0 || var7 >= var6.size())
            {
                return;
            }

            MerchantRecipe var8 = (MerchantRecipe)var6.get(var7);

            if (var8.isRecipeDisabled())
            {
                this.mc.getTextureManager().bindTexture(MERCHANT_GUI_TEXTURE);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.disableLighting();
                this.drawTexturedModalRect(this.guiLeft + 83, this.guiTop + 21, 212, 0, 28, 21);
                this.drawTexturedModalRect(this.guiLeft + 83, this.guiTop + 51, 212, 0, 28, 21);
            }
        }
    }

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    @Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);
        MerchantRecipeList var4 = this.merchant.getRecipes(this.mc.thePlayer);

        if (var4 != null && !var4.isEmpty())
        {
            int var5 = (this.width - this.xSize) / 2;
            int var6 = (this.height - this.ySize) / 2;
            int var7 = this.selectedMerchantRecipe;
            MerchantRecipe var8 = (MerchantRecipe)var4.get(var7);
            ItemStack var9 = var8.getItemToBuy();
            ItemStack var10 = var8.getSecondItemToBuy();
            ItemStack var11 = var8.getItemToSell();
            GlStateManager.pushMatrix();
            RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableColorMaterial();
            GlStateManager.enableLighting();
            this.itemRender.zLevel = 100.0F;
            this.itemRender.renderItemAndEffectIntoGUI(var9, var5 + 36, var6 + 24);
            this.itemRender.renderItemOverlays(this.fontRendererObj, var9, var5 + 36, var6 + 24);

            if (var10 != null)
            {
                this.itemRender.renderItemAndEffectIntoGUI(var10, var5 + 62, var6 + 24);
                this.itemRender.renderItemOverlays(this.fontRendererObj, var10, var5 + 62, var6 + 24);
            }

            this.itemRender.renderItemAndEffectIntoGUI(var11, var5 + 120, var6 + 24);
            this.itemRender.renderItemOverlays(this.fontRendererObj, var11, var5 + 120, var6 + 24);
            this.itemRender.zLevel = 0.0F;
            GlStateManager.disableLighting();

            if (this.isPointInRegion(36, 24, 16, 16, mouseX, mouseY) && var9 != null)
            {
                this.renderToolTip(var9, mouseX, mouseY);
            }
            else if (var10 != null && this.isPointInRegion(62, 24, 16, 16, mouseX, mouseY) && var10 != null)
            {
                this.renderToolTip(var10, mouseX, mouseY);
            }
            else if (var11 != null && this.isPointInRegion(120, 24, 16, 16, mouseX, mouseY) && var11 != null)
            {
                this.renderToolTip(var11, mouseX, mouseY);
            }
            else if (var8.isRecipeDisabled() && (this.isPointInRegion(83, 21, 28, 21, mouseX, mouseY) || this.isPointInRegion(83, 51, 28, 21, mouseX, mouseY)))
            {
                this.drawCreativeTabHoveringText(I18n.format("merchant.deprecated", new Object[0]), mouseX, mouseY);
            }

            GlStateManager.popMatrix();
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            RenderHelper.enableStandardItemLighting();
        }
    }

    public IMerchant getMerchant()
    {
        return this.merchant;
    }

    static class MerchantButton extends GuiButton
    {
        private final boolean field_146157_o;

        public MerchantButton(int buttonID, int x, int y, boolean p_i1095_4_)
        {
            super(buttonID, x, y, 12, 19, "");
            this.field_146157_o = p_i1095_4_;
        }

        @Override
		public void drawButton(Minecraft mc, int mouseX, int mouseY)
        {
            if (this.visible)
            {
                mc.getTextureManager().bindTexture(GuiMerchant.MERCHANT_GUI_TEXTURE);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                boolean var4 = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
                int var5 = 0;
                int var6 = 176;

                if (!this.enabled)
                {
                    var6 += this.width * 2;
                }
                else if (var4)
                {
                    var6 += this.width;
                }

                if (!this.field_146157_o)
                {
                    var5 += this.height;
                }

                this.drawTexturedModalRect(this.xPosition, this.yPosition, var6, var5, this.width, this.height);
            }
        }
    }
}
