package me.Corbis.Execution.ui.NiceGui;

import me.Corbis.Execution.module.Category;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class CategoryButton {
    Minecraft mc = Minecraft.getMinecraft();
    int x, y;
    int width, height;
    Category category;
    int mouseTicks = 0;
    NiceGui parent;

    public CategoryButton(int x, int y, Category category, NiceGui parent) {
        this.x = x;
        this.y = y;
        this.category = category;
        this.parent = parent;
        this.width = 45;
        this.height = 45;
    }

    public void draw(int mouseX, int mouseY){
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();

        if(isWithinButton(mouseX, mouseY)){
            if(mouseTicks < 3){
                mouseTicks++;
            }
        }else {
            if(mouseTicks > 0){
                mouseTicks--;
            }
        }

        switch(category){
            case COMBAT:
                mc.getTextureManager().bindTexture(new ResourceLocation("Execution/CombatCat.png"));
                Gui.drawModalRectWithCustomSizedTexture(x - mouseTicks, y - mouseTicks, 0 ,0, width + mouseTicks * 2, height+ mouseTicks * 2, width+ mouseTicks * 2, height+ mouseTicks * 2);
break;
            case MOVEMENT:
                mc.getTextureManager().bindTexture(new ResourceLocation("Execution/MovementCat.png"));
                Gui.drawModalRectWithCustomSizedTexture(x - mouseTicks, y - mouseTicks, 0 ,0, width + mouseTicks * 2, height+ mouseTicks * 2, width+ mouseTicks * 2, height+ mouseTicks * 2);
break;
            case PLAYER:
                mc.getTextureManager().bindTexture(new ResourceLocation("Execution/PlayerCat.png"));
                Gui.drawModalRectWithCustomSizedTexture(x - mouseTicks, y - mouseTicks, 0 ,0, width + mouseTicks * 2, height+ mouseTicks * 2, width+ mouseTicks * 2, height+ mouseTicks * 2);
break;
            case MISC:
                mc.getTextureManager().bindTexture(new ResourceLocation("Execution/MiscCat.png"));
                Gui.drawModalRectWithCustomSizedTexture(x - mouseTicks, y - mouseTicks, 0 ,0, width + mouseTicks * 2, height+ mouseTicks * 2, width+ mouseTicks * 2, height+ mouseTicks * 2);
break;
            case EXPLOIT:
                mc.getTextureManager().bindTexture(new ResourceLocation("Execution/ExploitCat.png"));
                Gui.drawModalRectWithCustomSizedTexture(x - mouseTicks, y - mouseTicks, 0 ,0, width + mouseTicks * 2, height+ mouseTicks * 2, width+ mouseTicks * 2, height+ mouseTicks * 2);
break;
            case WORLD:
                mc.getTextureManager().bindTexture(new ResourceLocation("Execution/WorldCat.png"));
                Gui.drawModalRectWithCustomSizedTexture(x - mouseTicks, y - mouseTicks, 0 ,0, width + mouseTicks * 2, height+ mouseTicks * 2, width+ mouseTicks * 2, height+ mouseTicks * 2);
break;
            case TARGETS:
                mc.getTextureManager().bindTexture(new ResourceLocation("Execution/TargetsCat.png"));
                Gui.drawModalRectWithCustomSizedTexture(x - mouseTicks, y - mouseTicks, 0 ,0, width + mouseTicks * 2, height+ mouseTicks * 2, width+ mouseTicks * 2, height+ mouseTicks * 2);
break;
            case RENDER:
                mc.getTextureManager().bindTexture(new ResourceLocation("Execution/RenderCat.png"));
                Gui.drawModalRectWithCustomSizedTexture(x - mouseTicks, y - mouseTicks, 0 ,0, width + mouseTicks * 2, height+ mouseTicks * 2, width+ mouseTicks * 2, height+ mouseTicks * 2);
break;

        }


    }

    public boolean isWithinButton(int mouseX, int mouseY){
        return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
    }

    public void mouseClicked(int mouseX, int mouseY){
        if(isWithinButton(mouseX, mouseY)){
            parent.currentCategory = this.category;

        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
