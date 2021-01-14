package moonx.ohare.client.gui.account.gui.components;

import moonx.ohare.client.Moonx;
import moonx.ohare.client.gui.account.gui.GuiAltManager;
import moonx.ohare.client.gui.account.system.Account;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.gui.ScaledResolution;

import static org.lwjgl.opengl.GL11.*;

public class GuiAccountList extends GuiSlot {

    public int selected = -1;
    private GuiAltManager parent;

    public GuiAccountList(GuiAltManager parent){
        super(Minecraft.getMinecraft(), parent.width, parent.height, 36,
                parent.height - 56, 40);

        this.parent = parent;
    }

    @Override
    public int getSize(){
        return Moonx.INSTANCE.getAccountManager().getAccounts().size();
    }

    @Override
    public void elementClicked(int i, boolean b, int i1, int i2){
        selected = i;

        if(b){
            parent.login(getAccount(i));
        }
    }

    @Override
    protected boolean isSelected(int i){
        return i == selected;
    }

    @Override
    protected void drawBackground(){}


    @Override
    protected void drawSlot(int i, int i1, int i2, int i3, int i4, int i5) {
        Account account = getAccount(i);

        Minecraft minecraft = Minecraft.getMinecraft();
        ScaledResolution scaledResolution = new ScaledResolution(minecraft);
        FontRenderer fontRenderer = minecraft.fontRendererObj;

        int x = i1 + 2;
        int y = i2;

        if (y >= scaledResolution.getScaledHeight() || y < 0)
            return;

        glTranslated(x, y, 0);

        drawFace(account.getName(), 0, 6, 24, 24);
        fontRenderer.drawStringWithShadow(account.getName(), 30, 6, 0xFFFFFFFF);
        fontRenderer.drawStringWithShadow("\2477" + account.getEmail(), 30, 6 + fontRenderer.FONT_HEIGHT + 2, 0xFFFFFFFF);

        glTranslated(-x, -y, 0);
    }

    public Account getAccount(int i){
        return  Moonx.INSTANCE.getAccountManager().getAccounts().get(i);
    }

    private void drawFace(String name, int x, int y, int w, int h){
        try{
            AbstractClientPlayer.getDownloadImageSkin(AbstractClientPlayer.getLocationSkin(name), name).loadTexture(Minecraft.getMinecraft().getResourceManager());
            Minecraft.getMinecraft().getTextureManager().bindTexture(AbstractClientPlayer.getLocationSkin(name));

            glEnable(GL_BLEND);

            glColor4f(1, 1, 1, 1);

            Gui.drawModalRectWithCustomSizedTexture(x, y, 24, 24, w, h, 192, 192);
            Gui.drawModalRectWithCustomSizedTexture(x, y, 120, 24, w, h, 192, 192);

            glDisable(GL_BLEND);
        }catch(Exception ignored){}
    }

    public void removeSelected(){
        if(selected == -1)
            return;

        Moonx.INSTANCE.getAccountManager().getAccounts().remove(getAccount(selected));
        Moonx.INSTANCE.getAccountManager().save();
    }

    public Account getSelectedAccount(){
        return getAccount(selected);
    }
}
