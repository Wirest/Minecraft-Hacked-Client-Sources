package net.minecraft.client.gui.recipebook;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.stats.RecipeBook;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class GuiButtonRecipe extends GuiButton
{
    private static final ResourceLocation field_191780_o = new ResourceLocation("textures/gui/recipe_book.png");
    private RecipeBook field_193930_p;
    private RecipeList field_191774_p;
    private float field_193931_r;
    private float field_191778_t;
    private int field_193932_t;

    public GuiButtonRecipe()
    {
        super(0, 0, 0, 25, 25, "");
    }

    public void func_193928_a(RecipeList p_193928_1_, RecipeBookPage p_193928_2_, RecipeBook p_193928_3_)
    {
        this.field_191774_p = p_193928_1_;
        this.field_193930_p = p_193928_3_;
        List<IRecipe> list = p_193928_1_.func_194208_a(p_193928_3_.func_192815_c());

        for (IRecipe irecipe : list)
        {
            if (p_193928_3_.func_194076_e(irecipe))
            {
                p_193928_2_.func_194195_a(list);
                this.field_191778_t = 15.0F;
                break;
            }
        }
    }

    public RecipeList func_191771_c()
    {
        return this.field_191774_p;
    }

    public void func_191770_c(int p_191770_1_, int p_191770_2_)
    {
        this.xPosition = p_191770_1_;
        this.yPosition = p_191770_2_;
    }

    public void func_191745_a(Minecraft p_191745_1_, int p_191745_2_, int p_191745_3_, float p_191745_4_)
    {
        if (this.visible)
        {
            if (!GuiScreen.isCtrlKeyDown())
            {
                this.field_193931_r += p_191745_4_;
            }

            this.hovered = p_191745_2_ >= this.xPosition && p_191745_3_ >= this.yPosition && p_191745_2_ < this.xPosition + this.width && p_191745_3_ < this.yPosition + this.height;
            RenderHelper.enableGUIStandardItemLighting();
            p_191745_1_.getTextureManager().bindTexture(field_191780_o);
            GlStateManager.disableLighting();
            int i = 29;

            if (!this.field_191774_p.func_192708_c())
            {
                i += 25;
            }

            int j = 206;

            if (this.field_191774_p.func_194208_a(this.field_193930_p.func_192815_c()).size() > 1)
            {
                j += 25;
            }

            boolean flag = this.field_191778_t > 0.0F;

            if (flag)
            {
                float f = 1.0F + 0.1F * (float)Math.sin((double)(this.field_191778_t / 15.0F * (float)Math.PI));
                GlStateManager.pushMatrix();
                GlStateManager.translate((float)(this.xPosition + 8), (float)(this.yPosition + 12), 0.0F);
                GlStateManager.scale(f, f, 1.0F);
                GlStateManager.translate((float)(-(this.xPosition + 8)), (float)(-(this.yPosition + 12)), 0.0F);
                this.field_191778_t -= p_191745_4_;
            }

            this.drawTexturedModalRect(this.xPosition, this.yPosition, i, j, this.width, this.height);
            List<IRecipe> list = this.func_193927_f();
            this.field_193932_t = MathHelper.floor(this.field_193931_r / 30.0F) % list.size();
            ItemStack itemstack = ((IRecipe)list.get(this.field_193932_t)).getRecipeOutput();
            int k = 4;

            if (this.field_191774_p.func_194211_e() && this.func_193927_f().size() > 1)
            {
                p_191745_1_.getRenderItem().renderItemAndEffectIntoGUI(itemstack, this.xPosition + k + 1, this.yPosition + k + 1);
                --k;
            }

            p_191745_1_.getRenderItem().renderItemAndEffectIntoGUI(itemstack, this.xPosition + k, this.yPosition + k);

            if (flag)
            {
                GlStateManager.popMatrix();
            }

            GlStateManager.enableLighting();
            RenderHelper.disableStandardItemLighting();
        }
    }

    private List<IRecipe> func_193927_f()
    {
        List<IRecipe> list = this.field_191774_p.func_194207_b(true);

        if (!this.field_193930_p.func_192815_c())
        {
            list.addAll(this.field_191774_p.func_194207_b(false));
        }

        return list;
    }

    public boolean func_193929_d()
    {
        return this.func_193927_f().size() == 1;
    }

    public IRecipe func_193760_e()
    {
        List<IRecipe> list = this.func_193927_f();
        return list.get(this.field_193932_t);
    }

    public List<String> func_191772_a(GuiScreen p_191772_1_)
    {
        ItemStack itemstack = ((IRecipe)this.func_193927_f().get(this.field_193932_t)).getRecipeOutput();
        List<String> list = p_191772_1_.func_191927_a(itemstack);

        if (this.field_191774_p.func_194208_a(this.field_193930_p.func_192815_c()).size() > 1)
        {
            list.add(I18n.format("gui.recipebook.moreRecipes"));
        }

        return list;
    }

    public int getButtonWidth()
    {
        return 25;
    }
}
