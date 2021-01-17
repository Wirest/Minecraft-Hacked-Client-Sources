// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import net.minecraft.client.settings.GameSettings;

public class GuiOptionButton extends GuiButton
{
    private final GameSettings.Options enumOptions;
    
    public GuiOptionButton(final int p_i45011_1_, final int p_i45011_2_, final int p_i45011_3_, final String p_i45011_4_) {
        this(p_i45011_1_, p_i45011_2_, p_i45011_3_, null, p_i45011_4_);
    }
    
    public GuiOptionButton(final int p_i45012_1_, final int p_i45012_2_, final int p_i45012_3_, final int p_i45012_4_, final int p_i45012_5_, final String p_i45012_6_) {
        super(p_i45012_1_, p_i45012_2_, p_i45012_3_, p_i45012_4_, p_i45012_5_, p_i45012_6_);
        this.enumOptions = null;
    }
    
    public GuiOptionButton(final int p_i45013_1_, final int p_i45013_2_, final int p_i45013_3_, final GameSettings.Options p_i45013_4_, final String p_i45013_5_) {
        super(p_i45013_1_, p_i45013_2_, p_i45013_3_, 150, 20, p_i45013_5_);
        this.enumOptions = p_i45013_4_;
    }
    
    public GameSettings.Options returnEnumOptions() {
        return this.enumOptions;
    }
}
