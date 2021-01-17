// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.realmsclient.gui.screens;

import org.lwjgl.opengl.GL11;
import com.mojang.realmsclient.util.RealmsUtil;
import net.minecraft.realms.Realms;
import net.minecraft.realms.RealmsButton;
import com.mojang.realmsclient.gui.RealmsConstants;
import net.minecraft.realms.RealmsScreen;

public class RealmsParentalConsentScreen extends RealmsScreen
{
    private final RealmsScreen nextScreen;
    private static final int BUTTON_BACK_ID = 0;
    private static final int BUTTON_OK_ID = 1;
    private final String line1 = "Recently, Mojang was acquired by Microsoft. Microsoft implements";
    private final String line2 = "certain procedures to help protect children and their privacy,";
    private final String line3 = "including complying with the Children\u2019s Online Privacy Protection Act (COPPA)";
    private final String line4 = "You may need to obtain parental consent before accessing your Realms account.";
    private boolean onLink;
    
    public RealmsParentalConsentScreen(final RealmsScreen nextScreen) {
        this.onLink = false;
        this.nextScreen = nextScreen;
    }
    
    @Override
    public void init() {
        this.buttonsClear();
        this.buttonsAdd(RealmsScreen.newButton(1, this.width() / 2 - 100, RealmsConstants.row(11), 200, 20, "Go to accounts page"));
        this.buttonsAdd(RealmsScreen.newButton(0, this.width() / 2 - 100, RealmsConstants.row(13), 200, 20, "Back"));
    }
    
    @Override
    public void tick() {
        super.tick();
    }
    
    @Override
    public void buttonClicked(final RealmsButton button) {
        switch (button.id()) {
            case 1: {
                RealmsUtil.browseTo("https://accounts.mojang.com/me/verify/" + Realms.getUUID());
                break;
            }
            case 0: {
                Realms.setScreen(this.nextScreen);
                break;
            }
            default: {}
        }
    }
    
    @Override
    public void mouseClicked(final int x, final int y, final int buttonNum) {
        if (this.onLink) {
            RealmsUtil.browseTo("http://www.ftc.gov/enforcement/rules/rulemaking-regulatory-reform-proceedings/childrens-online-privacy-protection-rule");
        }
    }
    
    @Override
    public void render(final int xm, final int ym, final float a) {
        this.renderBackground();
        this.drawCenteredString("Recently, Mojang was acquired by Microsoft. Microsoft implements", this.width() / 2, 30, 16777215);
        this.drawCenteredString("certain procedures to help protect children and their privacy,", this.width() / 2, 45, 16777215);
        this.drawCenteredString("including complying with the Children\u2019s Online Privacy Protection Act (COPPA)", this.width() / 2, 60, 16777215);
        this.drawCenteredString("You may need to obtain parental consent before accessing your Realms account.", this.width() / 2, 120, 16777215);
        this.renderLink(xm, ym);
        super.render(xm, ym, a);
    }
    
    private void renderLink(final int xm, final int ym) {
        final String text = RealmsScreen.getLocalizedString("Read more about COPPA");
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPushMatrix();
        final int textWidth = this.fontWidth(text);
        final int leftPadding = this.width() / 2 - textWidth / 2;
        final int topPadding = 75;
        final int x1 = leftPadding;
        final int x2 = x1 + textWidth + 1;
        final int y1 = topPadding;
        final int y2 = y1 + this.fontLineHeight();
        GL11.glTranslatef((float)x1, (float)y1, 0.0f);
        if (x1 <= xm && xm <= x2 && y1 <= ym && ym <= y2) {
            this.onLink = true;
            this.drawString(text, 0, 0, 7107012);
        }
        else {
            this.onLink = false;
            this.drawString(text, 0, 0, 3368635);
        }
        GL11.glPopMatrix();
    }
}
