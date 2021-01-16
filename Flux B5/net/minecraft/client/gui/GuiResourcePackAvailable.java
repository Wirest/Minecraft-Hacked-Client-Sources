package net.minecraft.client.gui;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

public class GuiResourcePackAvailable extends GuiResourcePackList
{
    private static final String __OBFID = "CL_00000824";

    public GuiResourcePackAvailable(Minecraft mcIn, int p_i45054_2_, int p_i45054_3_, List p_i45054_4_)
    {
        super(mcIn, p_i45054_2_, p_i45054_3_, p_i45054_4_);
    }

    protected String getListHeader()
    {
        return I18n.format("resourcePack.available.title", new Object[0]);
    }
}
