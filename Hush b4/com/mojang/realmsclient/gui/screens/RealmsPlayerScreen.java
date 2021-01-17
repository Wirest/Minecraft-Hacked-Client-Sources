// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.realmsclient.gui.screens;

import org.lwjgl.input.Mouse;
import net.minecraft.realms.RealmsClickableScrolledSelectionList;
import org.apache.logging.log4j.LogManager;
import net.minecraft.realms.RealmsDefaultVertexFormat;
import net.minecraft.realms.Tezzelator;
import org.lwjgl.opengl.GL11;
import java.util.Iterator;
import com.mojang.realmsclient.dto.Ops;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.dto.PlayerInfo;
import com.mojang.realmsclient.client.RealmsClient;
import net.minecraft.realms.Realms;
import com.mojang.realmsclient.gui.RealmsConstants;
import org.lwjgl.input.Keyboard;
import net.minecraft.realms.RealmsButton;
import com.mojang.realmsclient.dto.RealmsServer;
import org.apache.logging.log4j.Logger;
import net.minecraft.realms.RealmsScreen;

public class RealmsPlayerScreen extends RealmsScreen
{
    private static final Logger LOGGER;
    private static final String OP_ICON_LOCATION = "realms:textures/gui/realms/op_icon.png";
    private static final String USER_ICON_LOCATION = "realms:textures/gui/realms/user_icon.png";
    private static final String CROSS_ICON_LOCATION = "realms:textures/gui/realms/cross_icon.png";
    private String toolTip;
    private final RealmsConfigureWorldScreen lastScreen;
    private RealmsServer serverData;
    private InvitedSelectionList invitedSelectionList;
    private int column1_x;
    private int column_width;
    private int column2_x;
    private static final int BUTTON_BACK_ID = 0;
    private static final int BUTTON_INVITE_ID = 1;
    private static final int BUTTON_UNINVITE_ID = 2;
    private static final int BUTTON_ACTIVITY_ID = 3;
    private RealmsButton inviteButton;
    private RealmsButton activityButton;
    private int selectedInvitedIndex;
    private String selectedInvited;
    private boolean stateChanged;
    
    public RealmsPlayerScreen(final RealmsConfigureWorldScreen lastScreen, final RealmsServer serverData) {
        this.selectedInvitedIndex = -1;
        this.lastScreen = lastScreen;
        this.serverData = serverData;
    }
    
    @Override
    public void mouseEvent() {
        super.mouseEvent();
        if (this.invitedSelectionList != null) {
            this.invitedSelectionList.mouseEvent();
        }
    }
    
    @Override
    public void tick() {
        super.tick();
    }
    
    @Override
    public void init() {
        this.column1_x = this.width() / 2 - 160;
        this.column_width = 150;
        this.column2_x = this.width() / 2 + 12;
        Keyboard.enableRepeatEvents(true);
        this.buttonsClear();
        this.buttonsAdd(this.inviteButton = RealmsScreen.newButton(1, this.column2_x, RealmsConstants.row(1), this.column_width + 10, 20, RealmsScreen.getLocalizedString("mco.configure.world.buttons.invite")));
        this.buttonsAdd(this.activityButton = RealmsScreen.newButton(3, this.column2_x, RealmsConstants.row(3), this.column_width + 10, 20, RealmsScreen.getLocalizedString("mco.configure.world.buttons.activity")));
        this.buttonsAdd(RealmsScreen.newButton(0, this.column2_x + this.column_width / 2 + 2, RealmsConstants.row(12), this.column_width / 2 + 10 - 2, 20, RealmsScreen.getLocalizedString("gui.back")));
        (this.invitedSelectionList = new InvitedSelectionList()).setLeftPos(this.column1_x);
        this.inviteButton.active(false);
    }
    
    @Override
    public void removed() {
        Keyboard.enableRepeatEvents(false);
    }
    
    @Override
    public void buttonClicked(final RealmsButton button) {
        if (!button.active()) {
            return;
        }
        switch (button.id()) {
            case 0: {
                this.backButtonClicked();
                break;
            }
            case 1: {
                Realms.setScreen(new RealmsInviteScreen(this.lastScreen, this, this.serverData));
                break;
            }
            case 3: {
                Realms.setScreen(new RealmsActivityScreen(this, this.serverData));
                break;
            }
            default: {}
        }
    }
    
    @Override
    public void keyPressed(final char ch, final int eventKey) {
        if (eventKey == 1) {
            this.backButtonClicked();
        }
    }
    
    private void backButtonClicked() {
        if (this.stateChanged) {
            Realms.setScreen(this.lastScreen.getNewScreen());
        }
        else {
            Realms.setScreen(this.lastScreen);
        }
    }
    
    private void op(final int index) {
        final RealmsClient client = RealmsClient.createRealmsClient();
        final String selectedInvite = this.serverData.players.get(index).getName();
        try {
            this.updateOps(client.op(this.serverData.id, selectedInvite));
        }
        catch (RealmsServiceException e) {
            RealmsPlayerScreen.LOGGER.error("Couldn't op the user");
        }
    }
    
    private void deop(final int index) {
        final RealmsClient client = RealmsClient.createRealmsClient();
        final String selectedInvite = this.serverData.players.get(index).getName();
        try {
            this.updateOps(client.deop(this.serverData.id, selectedInvite));
        }
        catch (RealmsServiceException e) {
            RealmsPlayerScreen.LOGGER.error("Couldn't deop the user");
        }
    }
    
    private void updateOps(final Ops ops) {
        for (final PlayerInfo playerInfo : this.serverData.players) {
            playerInfo.setOperator(ops.ops.contains(playerInfo.getName()));
        }
    }
    
    private void uninvite(final int index) {
        if (index >= 0 && index < this.serverData.players.size()) {
            final PlayerInfo playerInfo = this.serverData.players.get(index);
            this.selectedInvited = playerInfo.getUuid();
            this.selectedInvitedIndex = index;
            final RealmsConfirmScreen confirmScreen = new RealmsConfirmScreen(this, "Question", RealmsScreen.getLocalizedString("mco.configure.world.uninvite.question") + " '" + playerInfo.getName() + "' ?", 2);
            Realms.setScreen(confirmScreen);
        }
    }
    
    @Override
    public void confirmResult(final boolean result, final int id) {
        if (id == 2) {
            if (result) {
                final RealmsClient client = RealmsClient.createRealmsClient();
                try {
                    client.uninvite(this.serverData.id, this.selectedInvited);
                }
                catch (RealmsServiceException e) {
                    RealmsPlayerScreen.LOGGER.error("Couldn't uninvite user");
                }
                this.deleteFromInvitedList(this.selectedInvitedIndex);
            }
            this.stateChanged = true;
            Realms.setScreen(this);
        }
    }
    
    private void deleteFromInvitedList(final int selectedInvitedIndex) {
        this.serverData.players.remove(selectedInvitedIndex);
    }
    
    @Override
    public void render(final int xm, final int ym, final float a) {
        this.toolTip = null;
        this.renderBackground();
        if (this.invitedSelectionList != null) {
            this.invitedSelectionList.render(xm, ym, a);
        }
        final int bottom_border = RealmsConstants.row(12) + 20;
        GL11.glDisable(2896);
        GL11.glDisable(2912);
        final Tezzelator t = Tezzelator.instance;
        RealmsScreen.bind("textures/gui/options_background.png");
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        final float s = 32.0f;
        t.begin(7, RealmsDefaultVertexFormat.POSITION_TEX_COLOR);
        t.vertex(0.0, this.height(), 0.0).tex(0.0, (this.height() - bottom_border) / 32.0f + 0.0f).color(64, 64, 64, 255).endVertex();
        t.vertex(this.width(), this.height(), 0.0).tex(this.width() / 32.0f, (this.height() - bottom_border) / 32.0f + 0.0f).color(64, 64, 64, 255).endVertex();
        t.vertex(this.width(), bottom_border, 0.0).tex(this.width() / 32.0f, 0.0).color(64, 64, 64, 255).endVertex();
        t.vertex(0.0, bottom_border, 0.0).tex(0.0, 0.0).color(64, 64, 64, 255).endVertex();
        t.end();
        this.drawCenteredString(RealmsScreen.getLocalizedString("mco.configure.world.players.title"), this.width() / 2, 17, 16777215);
        if (this.serverData != null && this.serverData.players != null) {
            this.drawString(RealmsScreen.getLocalizedString("mco.configure.world.invited") + " (" + this.serverData.players.size() + ")", this.column1_x, RealmsConstants.row(0), 10526880);
            this.inviteButton.active(this.serverData.players.size() < 200);
        }
        else {
            this.drawString(RealmsScreen.getLocalizedString("mco.configure.world.invited"), this.column1_x, RealmsConstants.row(0), 10526880);
            this.inviteButton.active(false);
        }
        super.render(xm, ym, a);
        if (this.serverData == null) {
            return;
        }
        if (this.toolTip != null) {
            this.renderMousehoverTooltip(this.toolTip, xm, ym);
        }
    }
    
    protected void renderMousehoverTooltip(final String msg, final int x, final int y) {
        if (msg == null) {
            return;
        }
        final int rx = x + 12;
        final int ry = y - 12;
        final int width = this.fontWidth(msg);
        this.fillGradient(rx - 3, ry - 3, rx + width + 3, ry + 8 + 3, -1073741824, -1073741824);
        this.fontDrawShadow(msg, rx, ry, 16777215);
    }
    
    private void drawRemoveIcon(final int x, final int y, final int xm, final int ym) {
        final boolean hovered = xm >= x && xm <= x + 9 && ym >= y && ym <= y + 9 && ym < this.height() - 25 && ym > RealmsConstants.row(1);
        RealmsScreen.bind("realms:textures/gui/realms/cross_icon.png");
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPushMatrix();
        RealmsScreen.blit(x, y, 0.0f, hovered ? 7.0f : 0.0f, 8, 7, 8.0f, 14.0f);
        GL11.glPopMatrix();
        if (hovered) {
            this.toolTip = RealmsScreen.getLocalizedString("mco.configure.world.invites.remove.tooltip");
        }
    }
    
    private void drawOpped(final int x, final int y, final int xm, final int ym) {
        final boolean hovered = xm >= x && xm <= x + 9 && ym >= y && ym <= y + 9 && ym < this.height() - 25 && ym > RealmsConstants.row(1);
        RealmsScreen.bind("realms:textures/gui/realms/op_icon.png");
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPushMatrix();
        RealmsScreen.blit(x, y, 0.0f, hovered ? 8.0f : 0.0f, 8, 8, 8.0f, 16.0f);
        GL11.glPopMatrix();
        if (hovered) {
            this.toolTip = RealmsScreen.getLocalizedString("mco.configure.world.invites.ops.tooltip");
        }
    }
    
    private void drawNormal(final int x, final int y, final int xm, final int ym) {
        final boolean hovered = xm >= x && xm <= x + 9 && ym >= y && ym <= y + 9;
        RealmsScreen.bind("realms:textures/gui/realms/user_icon.png");
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPushMatrix();
        RealmsScreen.blit(x, y, 0.0f, hovered ? 8.0f : 0.0f, 8, 8, 8.0f, 16.0f);
        GL11.glPopMatrix();
        if (hovered) {
            this.toolTip = RealmsScreen.getLocalizedString("mco.configure.world.invites.normal.tooltip");
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    private class InvitedSelectionList extends RealmsClickableScrolledSelectionList
    {
        public InvitedSelectionList() {
            super(RealmsPlayerScreen.this.column_width + 10, RealmsConstants.row(12) + 20, RealmsConstants.row(1), RealmsConstants.row(12) + 20, 13);
        }
        
        @Override
        public void customMouseEvent(final int y0, final int y1, final int headerHeight, final float yo, final int itemHeight) {
            if (Mouse.isButtonDown(0) && this.ym() >= y0 && this.ym() <= y1) {
                final int x0 = RealmsPlayerScreen.this.column1_x;
                final int x2 = RealmsPlayerScreen.this.column1_x + RealmsPlayerScreen.this.column_width;
                final int clickSlotPos = this.ym() - y0 - headerHeight + (int)yo - 4;
                final int slot = clickSlotPos / itemHeight;
                if (this.xm() >= x0 && this.xm() <= x2 && slot >= 0 && clickSlotPos >= 0 && slot < this.getItemCount()) {
                    this.itemClicked(clickSlotPos, slot, this.xm(), this.ym(), this.width());
                }
            }
        }
        
        @Override
        public void itemClicked(final int clickSlotPos, final int slot, final int xm, final int ym, final int width) {
            if (slot < 0 || slot > RealmsPlayerScreen.this.serverData.players.size() || RealmsPlayerScreen.this.toolTip == null) {
                return;
            }
            if (RealmsPlayerScreen.this.toolTip.equals(RealmsScreen.getLocalizedString("mco.configure.world.invites.ops.tooltip")) || RealmsPlayerScreen.this.toolTip.equals(RealmsScreen.getLocalizedString("mco.configure.world.invites.normal.tooltip"))) {
                if (RealmsPlayerScreen.this.serverData.players.get(slot).isOperator()) {
                    RealmsPlayerScreen.this.deop(slot);
                }
                else {
                    RealmsPlayerScreen.this.op(slot);
                }
            }
            else if (RealmsPlayerScreen.this.toolTip.equals(RealmsScreen.getLocalizedString("mco.configure.world.invites.remove.tooltip"))) {
                RealmsPlayerScreen.this.uninvite(slot);
            }
        }
        
        @Override
        public void renderBackground() {
            RealmsPlayerScreen.this.renderBackground();
        }
        
        @Override
        public int getScrollbarPosition() {
            return RealmsPlayerScreen.this.column1_x + this.width() - 5;
        }
        
        @Override
        public int getItemCount() {
            return (RealmsPlayerScreen.this.serverData == null) ? 1 : RealmsPlayerScreen.this.serverData.players.size();
        }
        
        @Override
        public int getMaxPosition() {
            return this.getItemCount() * 13;
        }
        
        @Override
        protected void renderItem(final int i, final int x, final int y, final int h, final Tezzelator t, final int mouseX, final int mouseY) {
            if (RealmsPlayerScreen.this.serverData == null) {
                return;
            }
            if (i < RealmsPlayerScreen.this.serverData.players.size()) {
                this.renderInvitedItem(i, x, y, h);
            }
        }
        
        private void renderInvitedItem(final int i, final int x, final int y, final int h) {
            final PlayerInfo invited = RealmsPlayerScreen.this.serverData.players.get(i);
            RealmsPlayerScreen.this.drawString(invited.getName(), RealmsPlayerScreen.this.column1_x + 3 + 12, y + 1, invited.getAccepted() ? 16777215 : 10526880);
            if (invited.isOperator()) {
                RealmsPlayerScreen.this.drawOpped(RealmsPlayerScreen.this.column1_x + RealmsPlayerScreen.this.column_width - 10, y + 1, this.xm(), this.ym());
            }
            else {
                RealmsPlayerScreen.this.drawNormal(RealmsPlayerScreen.this.column1_x + RealmsPlayerScreen.this.column_width - 10, y + 1, this.xm(), this.ym());
            }
            RealmsPlayerScreen.this.drawRemoveIcon(RealmsPlayerScreen.this.column1_x + RealmsPlayerScreen.this.column_width - 22, y + 2, this.xm(), this.ym());
            RealmsScreen.bindFace(invited.getUuid(), invited.getName());
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            RealmsScreen.blit(RealmsPlayerScreen.this.column1_x + 2 + 2, y + 1, 8.0f, 8.0f, 8, 8, 8, 8, 64.0f, 64.0f);
            RealmsScreen.blit(RealmsPlayerScreen.this.column1_x + 2 + 2, y + 1, 40.0f, 8.0f, 8, 8, 8, 8, 64.0f, 64.0f);
        }
    }
}
