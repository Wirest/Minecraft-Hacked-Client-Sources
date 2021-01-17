// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.realmsclient.gui.screens;

import net.minecraft.realms.Realms;
import net.minecraft.realms.RealmsButton;
import com.mojang.realmsclient.gui.RealmsConstants;
import com.mojang.realmsclient.gui.LongRunningTask;
import com.mojang.realmsclient.gui.ErrorCallback;
import net.minecraft.realms.RealmsScreen;

public class RealmsLongRunningMcoTaskScreen extends RealmsScreen implements ErrorCallback
{
    private final int BUTTON_CANCEL_ID = 666;
    private final int BUTTON_BACK_ID = 667;
    private final RealmsScreen lastScreen;
    private final LongRunningTask taskThread;
    private volatile String title;
    private volatile boolean error;
    private volatile String errorMessage;
    private volatile boolean aborted;
    private int animTicks;
    private LongRunningTask task;
    private int buttonLength;
    public static final String[] symbols;
    
    public RealmsLongRunningMcoTaskScreen(final RealmsScreen lastScreen, final LongRunningTask task) {
        this.title = "";
        this.buttonLength = 212;
        this.lastScreen = lastScreen;
        (this.task = task).setScreen(this);
        this.taskThread = task;
    }
    
    public void start() {
        new Thread(this.taskThread, "Realms-long-running-task").start();
    }
    
    @Override
    public void tick() {
        super.tick();
        ++this.animTicks;
        this.task.tick();
    }
    
    @Override
    public void keyPressed(final char eventCharacter, final int eventKey) {
        if (eventKey == 1) {
            this.cancelOrBackButtonClicked();
        }
    }
    
    @Override
    public void init() {
        this.task.init();
        this.buttonsAdd(RealmsScreen.newButton(666, this.width() / 2 - this.buttonLength / 2, RealmsConstants.row(12), this.buttonLength, 20, RealmsScreen.getLocalizedString("gui.cancel")));
    }
    
    @Override
    public void buttonClicked(final RealmsButton button) {
        if (button.id() == 666 || button.id() == 667) {
            this.cancelOrBackButtonClicked();
        }
        this.task.buttonClicked(button);
    }
    
    private void cancelOrBackButtonClicked() {
        this.aborted = true;
        this.task.abortTask();
        Realms.setScreen(this.lastScreen);
    }
    
    @Override
    public void render(final int xm, final int ym, final float a) {
        this.renderBackground();
        this.drawCenteredString(this.title, this.width() / 2, RealmsConstants.row(3), 16777215);
        if (!this.error) {
            this.drawCenteredString(RealmsLongRunningMcoTaskScreen.symbols[this.animTicks % RealmsLongRunningMcoTaskScreen.symbols.length], this.width() / 2, RealmsConstants.row(8), 8421504);
        }
        if (this.error) {
            this.drawCenteredString(this.errorMessage, this.width() / 2, RealmsConstants.row(8), 16711680);
        }
        super.render(xm, ym, a);
    }
    
    @Override
    public void error(final String errorMessage) {
        this.error = true;
        this.errorMessage = errorMessage;
        this.buttonsClear();
        this.buttonsAdd(RealmsScreen.newButton(667, this.width() / 2 - this.buttonLength / 2, this.height() / 4 + 120 + 12, RealmsScreen.getLocalizedString("gui.back")));
    }
    
    public void setTitle(final String title) {
        this.title = title;
    }
    
    public boolean aborted() {
        return this.aborted;
    }
    
    static {
        symbols = new String[] { "\u2583 \u2584 \u2585 \u2586 \u2587 \u2588 \u2587 \u2586 \u2585 \u2584 \u2583", "_ \u2583 \u2584 \u2585 \u2586 \u2587 \u2588 \u2587 \u2586 \u2585 \u2584", "_ _ \u2583 \u2584 \u2585 \u2586 \u2587 \u2588 \u2587 \u2586 \u2585", "_ _ _ \u2583 \u2584 \u2585 \u2586 \u2587 \u2588 \u2587 \u2586", "_ _ _ _ \u2583 \u2584 \u2585 \u2586 \u2587 \u2588 \u2587", "_ _ _ _ _ \u2583 \u2584 \u2585 \u2586 \u2587 \u2588", "_ _ _ _ \u2583 \u2584 \u2585 \u2586 \u2587 \u2588 \u2587", "_ _ _ \u2583 \u2584 \u2585 \u2586 \u2587 \u2588 \u2587 \u2586", "_ _ \u2583 \u2584 \u2585 \u2586 \u2587 \u2588 \u2587 \u2586 \u2585", "_ \u2583 \u2584 \u2585 \u2586 \u2587 \u2588 \u2587 \u2586 \u2585 \u2584", "\u2583 \u2584 \u2585 \u2586 \u2587 \u2588 \u2587 \u2586 \u2585 \u2584 \u2583", "\u2584 \u2585 \u2586 \u2587 \u2588 \u2587 \u2586 \u2585 \u2584 \u2583 _", "\u2585 \u2586 \u2587 \u2588 \u2587 \u2586 \u2585 \u2584 \u2583 _ _", "\u2586 \u2587 \u2588 \u2587 \u2586 \u2585 \u2584 \u2583 _ _ _", "\u2587 \u2588 \u2587 \u2586 \u2585 \u2584 \u2583 _ _ _ _", "\u2588 \u2587 \u2586 \u2585 \u2584 \u2583 _ _ _ _ _", "\u2587 \u2588 \u2587 \u2586 \u2585 \u2584 \u2583 _ _ _ _", "\u2586 \u2587 \u2588 \u2587 \u2586 \u2585 \u2584 \u2583 _ _ _", "\u2585 \u2586 \u2587 \u2588 \u2587 \u2586 \u2585 \u2584 \u2583 _ _", "\u2584 \u2585 \u2586 \u2587 \u2588 \u2587 \u2586 \u2585 \u2584 \u2583 _" };
    }
}
