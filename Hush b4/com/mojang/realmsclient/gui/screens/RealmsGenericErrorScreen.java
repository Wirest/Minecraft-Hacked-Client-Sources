// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.realmsclient.gui.screens;

import net.minecraft.realms.Realms;
import net.minecraft.realms.RealmsButton;
import com.mojang.realmsclient.exception.RealmsServiceException;
import net.minecraft.realms.RealmsScreen;

public class RealmsGenericErrorScreen extends RealmsScreen
{
    private final RealmsScreen nextScreen;
    private static final int OK_BUTTON_ID = 10;
    private String line1;
    private String line2;
    
    public RealmsGenericErrorScreen(final RealmsServiceException realmsServiceException, final RealmsScreen nextScreen) {
        this.nextScreen = nextScreen;
        this.errorMessage(realmsServiceException);
    }
    
    public RealmsGenericErrorScreen(final String message, final RealmsScreen nextScreen) {
        this.nextScreen = nextScreen;
        this.errorMessage(message);
    }
    
    public RealmsGenericErrorScreen(final String title, final String message, final RealmsScreen nextScreen) {
        this.nextScreen = nextScreen;
        this.errorMessage(title, message);
    }
    
    private void errorMessage(final RealmsServiceException realmsServiceException) {
        if (realmsServiceException.errorCode != -1) {
            this.line1 = "Realms (" + realmsServiceException.errorCode + "):";
            final String translationKey = "mco.errorMessage." + realmsServiceException.errorCode;
            final String translated = RealmsScreen.getLocalizedString(translationKey);
            this.line2 = (translated.equals(translationKey) ? realmsServiceException.errorMsg : translated);
        }
        else {
            this.line1 = "An error occurred (" + realmsServiceException.httpResultCode + "):";
            this.line2 = realmsServiceException.httpResponseContent;
        }
    }
    
    private void errorMessage(final String message) {
        this.line1 = "An error occurred: ";
        this.line2 = message;
    }
    
    private void errorMessage(final String title, final String message) {
        this.line1 = title;
        this.line2 = message;
    }
    
    @Override
    public void init() {
        this.buttonsClear();
        this.buttonsAdd(RealmsScreen.newButton(10, this.width() / 2 - 100, this.height() - 52, 200, 20, "Ok"));
    }
    
    @Override
    public void tick() {
        super.tick();
    }
    
    @Override
    public void buttonClicked(final RealmsButton button) {
        if (button.id() == 10) {
            Realms.setScreen(this.nextScreen);
        }
    }
    
    @Override
    public void render(final int xm, final int ym, final float a) {
        this.renderBackground();
        this.drawCenteredString(this.line1, this.width() / 2, 80, 16777215);
        this.drawCenteredString(this.line2, this.width() / 2, 100, 16711680);
        super.render(xm, ym, a);
    }
}
