package net.minecraft.client.gui.recipebook;

import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButtonToggle;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.util.RecipeBookClient;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.stats.RecipeBook;

public class GuiButtonRecipeTab extends GuiButtonToggle
{
    private final CreativeTabs field_193921_u;
    private float field_193922_v;

    public GuiButtonRecipeTab(int p_i47588_1_, CreativeTabs p_i47588_2_)
    {
        super(p_i47588_1_, 0, 0, 35, 27, false);
        this.field_193921_u = p_i47588_2_;
        this.func_191751_a(153, 2, 35, 0, GuiRecipeBook.field_191894_a);
    }

    public void func_193918_a(Minecraft p_193918_1_)
    {
        RecipeBook recipebook = p_193918_1_.player.func_192035_E();
        label21:

        for (RecipeList recipelist : RecipeBookClient.field_194086_e.get(this.field_193921_u))
        {
            Iterator iterator = recipelist.func_194208_a(recipebook.func_192815_c()).iterator();

            while (true)
            {
                if (!iterator.hasNext())
                {
                    continue label21;
                }

                IRecipe irecipe = (IRecipe)iterator.next();

                if (recipebook.func_194076_e(irecipe))
                {
                    break;
                }
            }

            this.field_193922_v = 15.0F;
            return;
        }
    }

    public void func_191745_a(Minecraft p_191745_1_, int p_191745_2_, int p_191745_3_, float p_191745_4_)
    {
        if (this.visible)
        {
            if (this.field_193922_v > 0.0F)
            {
                float f = 1.0F + 0.1F * (float)Math.sin((double)(this.field_193922_v / 15.0F * (float)Math.PI));
                GlStateManager.pushMatrix();
                GlStateManager.translate((float)(this.xPosition + 8), (float)(this.yPosition + 12), 0.0F);
                GlStateManager.scale(1.0F, f, 1.0F);
                GlStateManager.translate((float)(-(this.xPosition + 8)), (float)(-(this.yPosition + 12)), 0.0F);
            }

            this.hovered = p_191745_2_ >= this.xPosition && p_191745_3_ >= this.yPosition && p_191745_2_ < this.xPosition + this.width && p_191745_3_ < this.yPosition + this.height;
            p_191745_1_.getTextureManager().bindTexture(this.field_191760_o);
            GlStateManager.disableDepth();
            int k = this.field_191756_q;
            int i = this.field_191757_r;

            if (this.field_191755_p)
            {
                k += this.field_191758_s;
            }

            if (this.hovered)
            {
                i += this.field_191759_t;
            }

            int j = this.xPosition;

            if (this.field_191755_p)
            {
                j -= 2;
            }

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.drawTexturedModalRect(j, this.yPosition, k, i, this.width, this.height);
            GlStateManager.enableDepth();
            RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.disableLighting();
            this.func_193920_a(p_191745_1_.getRenderItem());
            GlStateManager.enableLighting();
            RenderHelper.disableStandardItemLighting();

            if (this.field_193922_v > 0.0F)
            {
                GlStateManager.popMatrix();
                this.field_193922_v -= p_191745_4_;
            }
        }
    }

    private void func_193920_a(RenderItem p_193920_1_)
    {
        ItemStack itemstack = this.field_193921_u.getIconItemStack();

        if (this.field_193921_u == CreativeTabs.TOOLS)
        {
            p_193920_1_.renderItemAndEffectIntoGUI(itemstack, this.xPosition + 3, this.yPosition + 5);
            p_193920_1_.renderItemAndEffectIntoGUI(CreativeTabs.COMBAT.getIconItemStack(), this.xPosition + 14, this.yPosition + 5);
        }
        else if (this.field_193921_u == CreativeTabs.MISC)
        {
            p_193920_1_.renderItemAndEffectIntoGUI(itemstack, this.xPosition + 3, this.yPosition + 5);
            p_193920_1_.renderItemAndEffectIntoGUI(CreativeTabs.FOOD.getIconItemStack(), this.xPosition + 14, this.yPosition + 5);
        }
        else
        {
            p_193920_1_.renderItemAndEffectIntoGUI(itemstack, this.xPosition + 9, this.yPosition + 5);
        }
    }

    public CreativeTabs func_191764_e()
    {
        return this.field_193921_u;
    }

    public boolean func_193919_e()
    {
        List<RecipeList> list = (List)RecipeBookClient.field_194086_e.get(this.field_193921_u);
        this.visible = false;

        for (RecipeList recipelist : list)
        {
            if (recipelist.func_194209_a() && recipelist.func_194212_c())
            {
                this.visible = true;
                break;
            }
        }

        return this.visible;
    }
}
