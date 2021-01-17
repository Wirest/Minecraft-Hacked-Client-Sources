// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import net.minecraft.util.MathHelper;
import net.minecraft.client.Minecraft;

public class ScaledResolution
{
    private final double scaledWidthD;
    private final double scaledHeightD;
    private int scaledWidth;
    private int scaledHeight;
    private int scaleFactor;
    
    public ScaledResolution(final Minecraft p_i46445_1_) {
        this.scaledWidth = Minecraft.displayWidth;
        this.scaledHeight = Minecraft.displayHeight;
        this.scaleFactor = 1;
        final boolean flag = p_i46445_1_.isUnicode();
        int i = p_i46445_1_.gameSettings.guiScale;
        if (i == 0) {
            i = 1000;
        }
        while (this.scaleFactor < i && this.scaledWidth / (this.scaleFactor + 1) >= 320 && this.scaledHeight / (this.scaleFactor + 1) >= 240) {
            ++this.scaleFactor;
        }
        if (flag && this.scaleFactor % 2 != 0 && this.scaleFactor != 1) {
            --this.scaleFactor;
        }
        this.scaledWidthD = this.scaledWidth / (double)this.scaleFactor;
        this.scaledHeightD = this.scaledHeight / (double)this.scaleFactor;
        this.scaledWidth = MathHelper.ceiling_double_int(this.scaledWidthD);
        this.scaledHeight = MathHelper.ceiling_double_int(this.scaledHeightD);
    }
    
    public int getScaledWidth() {
        return this.scaledWidth;
    }
    
    public int getScaledHeight() {
        return this.scaledHeight;
    }
    
    public double getScaledWidth_double() {
        return this.scaledWidthD;
    }
    
    public double getScaledHeight_double() {
        return this.scaledHeightD;
    }
    
    public int getScaleFactor() {
        return this.scaleFactor;
    }
}
