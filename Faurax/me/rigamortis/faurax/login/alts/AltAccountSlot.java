package me.rigamortis.faurax.login.alts;

import net.minecraft.client.*;
import net.minecraft.util.*;
import net.minecraft.client.gui.*;

public class AltAccountSlot extends GuiSlot
{
    final AltAccountSwitch parentScreen;
    
    public AltAccountSlot(final AltAccountSwitch PAS) {
        super(Minecraft.getMinecraft(), PAS.width, PAS.height, 32, PAS.height - 64, 24);
        this.parentScreen = PAS;
    }
    
    @Override
    protected int getSize() {
        return this.parentScreen.getAccountList().size();
    }
    
    @Override
    protected boolean isSelected(final int var1) {
        return var1 == this.parentScreen.getSelectedAccount();
    }
    
    @Override
    protected int getContentHeight() {
        return this.parentScreen.getAccountList().size() * 24;
    }
    
    @Override
    protected void drawBackground() {
        this.parentScreen.drawDefaultBackground();
    }
    
    @Override
    protected void elementClicked(final int var1, final boolean var2, final int var3, final int var4) {
        this.parentScreen.setSelectedAccount(var1);
        final boolean flag2 = this.parentScreen.getSelectedAccount() >= 0 && this.parentScreen.getSelectedAccount() < this.getSize();
        this.parentScreen.getButtonSelect().enabled = flag2;
        this.parentScreen.getButtonEdit().enabled = flag2;
        this.parentScreen.getButtonDelete().enabled = flag2;
        if (var2 && flag2) {
            this.parentScreen.login(var1);
        }
    }
    
    @Override
    protected void drawSlot(final int var1, final int var2, final int var3, final int var4, final int p_180791_5_, final int p_180791_6_) {
        final String[] account = this.parentScreen.getAccountList().get(var1).split(":");
        final String name = account[0];
        final boolean cracked = account.length == 1;
        final boolean hasRep = account.length >= 3;
        final FontRenderer f = Minecraft.getMinecraft().fontRendererObj;
        f.drawString(name, var2 + 2, var3 + 1, 16777215);
        f.drawString(cracked ? (EnumChatFormatting.DARK_GRAY + "Non-Premium") : (EnumChatFormatting.GRAY + "Premium"), var2 + 2, var3 + 12, cracked ? 3158064 : 8421504);
    }
}
