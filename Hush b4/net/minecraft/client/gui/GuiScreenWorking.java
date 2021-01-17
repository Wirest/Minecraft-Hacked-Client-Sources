// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import net.minecraft.util.IProgressUpdate;

public class GuiScreenWorking extends GuiScreen implements IProgressUpdate
{
    private String field_146591_a;
    private String field_146589_f;
    private int progress;
    private boolean doneWorking;
    
    public GuiScreenWorking() {
        this.field_146591_a = "";
        this.field_146589_f = "";
    }
    
    @Override
    public void displaySavingString(final String message) {
        this.resetProgressAndMessage(message);
    }
    
    @Override
    public void resetProgressAndMessage(final String message) {
        this.field_146591_a = message;
        this.displayLoadingString("Working...");
    }
    
    @Override
    public void displayLoadingString(final String message) {
        this.field_146589_f = message;
        this.setLoadingProgress(0);
    }
    
    @Override
    public void setLoadingProgress(final int progress) {
        this.progress = progress;
    }
    
    @Override
    public void setDoneWorking() {
        this.doneWorking = true;
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        if (this.doneWorking) {
            if (!this.mc.func_181540_al()) {
                this.mc.displayGuiScreen(null);
            }
        }
        else {
            this.drawDefaultBackground();
            this.drawCenteredString(this.fontRendererObj, this.field_146591_a, this.width / 2, 70, 16777215);
            this.drawCenteredString(this.fontRendererObj, String.valueOf(this.field_146589_f) + " " + this.progress + "%", this.width / 2, 90, 16777215);
            super.drawScreen(mouseX, mouseY, partialTicks);
        }
    }
}
