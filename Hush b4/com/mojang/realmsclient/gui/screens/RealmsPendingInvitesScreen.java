// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.realmsclient.gui.screens;

import org.lwjgl.input.Mouse;
import com.mojang.realmsclient.util.RealmsUtil;
import net.minecraft.realms.RealmsDefaultVertexFormat;
import org.lwjgl.opengl.GL11;
import net.minecraft.realms.Tezzelator;
import net.minecraft.realms.RealmsClickableScrolledSelectionList;
import org.apache.logging.log4j.LogManager;
import net.minecraft.realms.Realms;
import com.mojang.realmsclient.RealmsMainScreen;
import net.minecraft.realms.RealmsButton;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.client.RealmsClient;
import org.lwjgl.input.Keyboard;
import com.google.common.collect.Lists;
import com.mojang.realmsclient.dto.PendingInvite;
import java.util.List;
import org.apache.logging.log4j.Logger;
import net.minecraft.realms.RealmsScreen;

public class RealmsPendingInvitesScreen extends RealmsScreen
{
    private static final Logger LOGGER;
    private static final int BUTTON_BACK_ID = 0;
    private static final String ACCEPT_ICON_LOCATION = "realms:textures/gui/realms/accept_icon.png";
    private static final String REJECT_ICON_LOCATION = "realms:textures/gui/realms/reject_icon.png";
    private final RealmsScreen lastScreen;
    private String toolTip;
    private boolean loaded;
    private PendingInvitationList pendingList;
    private List<PendingInvite> pendingInvites;
    
    public RealmsPendingInvitesScreen(final RealmsScreen lastScreen) {
        this.toolTip = null;
        this.loaded = false;
        this.pendingInvites = (List<PendingInvite>)Lists.newArrayList();
        this.lastScreen = lastScreen;
    }
    
    @Override
    public void mouseEvent() {
        super.mouseEvent();
        this.pendingList.mouseEvent();
    }
    
    @Override
    public void init() {
        Keyboard.enableRepeatEvents(true);
        this.buttonsClear();
        this.pendingList = new PendingInvitationList();
        new Thread("Realms-pending-invitations-fetcher") {
            @Override
            public void run() {
                final RealmsClient client = RealmsClient.createRealmsClient();
                try {
                    RealmsPendingInvitesScreen.this.pendingInvites = client.pendingInvites().pendingInvites;
                }
                catch (RealmsServiceException e) {
                    RealmsPendingInvitesScreen.LOGGER.error("Couldn't list invites");
                }
                finally {
                    RealmsPendingInvitesScreen.this.loaded = true;
                }
            }
        }.start();
        this.buttonsAdd(RealmsScreen.newButton(0, this.width() / 2 - 75, this.height() - 32, 153, 20, RealmsScreen.getLocalizedString("gui.done")));
    }
    
    @Override
    public void tick() {
        super.tick();
    }
    
    @Override
    public void buttonClicked(final RealmsButton button) {
        if (!button.active()) {
            return;
        }
        switch (button.id()) {
            case 0: {
                Realms.setScreen(new RealmsMainScreen(this.lastScreen));
                break;
            }
        }
    }
    
    @Override
    public void keyPressed(final char eventCharacter, final int eventKey) {
        if (eventKey == 1) {
            Realms.setScreen(new RealmsMainScreen(this.lastScreen));
        }
    }
    
    private void updateList(final int slot) {
        this.pendingInvites.remove(slot);
    }
    
    private void reject(final int slot) {
        if (slot < this.pendingInvites.size()) {
            new Thread("Realms-reject-invitation") {
                @Override
                public void run() {
                    try {
                        final RealmsClient client = RealmsClient.createRealmsClient();
                        client.rejectInvitation(RealmsPendingInvitesScreen.this.pendingInvites.get(slot).invitationId);
                        RealmsPendingInvitesScreen.this.updateList(slot);
                    }
                    catch (RealmsServiceException e) {
                        RealmsPendingInvitesScreen.LOGGER.error("Couldn't reject invite");
                    }
                }
            }.start();
        }
    }
    
    private void accept(final int slot) {
        if (slot < this.pendingInvites.size()) {
            new Thread("Realms-accept-invitation") {
                @Override
                public void run() {
                    try {
                        final RealmsClient client = RealmsClient.createRealmsClient();
                        client.acceptInvitation(RealmsPendingInvitesScreen.this.pendingInvites.get(slot).invitationId);
                        RealmsPendingInvitesScreen.this.updateList(slot);
                    }
                    catch (RealmsServiceException e) {
                        RealmsPendingInvitesScreen.LOGGER.error("Couldn't accept invite");
                    }
                }
            }.start();
        }
    }
    
    @Override
    public void render(final int xm, final int ym, final float a) {
        this.toolTip = null;
        this.renderBackground();
        this.pendingList.render(xm, ym, a);
        this.drawCenteredString(RealmsScreen.getLocalizedString("mco.invites.title"), this.width() / 2, 12, 16777215);
        if (this.toolTip != null) {
            this.renderMousehoverTooltip(this.toolTip, xm, ym);
        }
        if (this.pendingInvites.size() == 0 && this.loaded) {
            this.drawCenteredString(RealmsScreen.getLocalizedString("mco.invites.nopending"), this.width() / 2, this.height() / 2 - 20, 16777215);
        }
        super.render(xm, ym, a);
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
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    private class PendingInvitationList extends RealmsClickableScrolledSelectionList
    {
        public PendingInvitationList() {
            super(RealmsPendingInvitesScreen.this.width() + 50, RealmsPendingInvitesScreen.this.height(), 32, RealmsPendingInvitesScreen.this.height() - 40, 36);
        }
        
        @Override
        public int getItemCount() {
            return RealmsPendingInvitesScreen.this.pendingInvites.size();
        }
        
        @Override
        public int getMaxPosition() {
            return this.getItemCount() * 36;
        }
        
        @Override
        public void renderBackground() {
            RealmsPendingInvitesScreen.this.renderBackground();
        }
        
        @Override
        public void renderSelected(final int width, final int y, final int h, final Tezzelator t) {
            final int x0 = this.getScrollbarPosition() - 290;
            final int x2 = this.getScrollbarPosition() - 10;
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glDisable(3553);
            t.begin(7, RealmsDefaultVertexFormat.POSITION_TEX_COLOR);
            t.vertex(x0, y + h + 2, 0.0).tex(0.0, 1.0).color(128, 128, 128, 255).endVertex();
            t.vertex(x2, y + h + 2, 0.0).tex(1.0, 1.0).color(128, 128, 128, 255).endVertex();
            t.vertex(x2, y - 2, 0.0).tex(1.0, 0.0).color(128, 128, 128, 255).endVertex();
            t.vertex(x0, y - 2, 0.0).tex(0.0, 0.0).color(128, 128, 128, 255).endVertex();
            t.vertex(x0 + 1, y + h + 1, 0.0).tex(0.0, 1.0).color(0, 0, 0, 255).endVertex();
            t.vertex(x2 - 1, y + h + 1, 0.0).tex(1.0, 1.0).color(0, 0, 0, 255).endVertex();
            t.vertex(x2 - 1, y - 1, 0.0).tex(1.0, 0.0).color(0, 0, 0, 255).endVertex();
            t.vertex(x0 + 1, y - 1, 0.0).tex(0.0, 0.0).color(0, 0, 0, 255).endVertex();
            t.end();
            GL11.glEnable(3553);
        }
        
        @Override
        public void renderItem(final int i, final int x, final int y, final int h, final int mouseX, final int mouseY) {
            if (i < RealmsPendingInvitesScreen.this.pendingInvites.size()) {
                this.renderPendingInvitationItem(i, x, y, h);
            }
        }
        
        private void renderPendingInvitationItem(final int i, final int x, final int y, final int h) {
            final PendingInvite invite = RealmsPendingInvitesScreen.this.pendingInvites.get(i);
            RealmsPendingInvitesScreen.this.drawString(invite.worldName, x + 2, y + 1, 16777215);
            RealmsPendingInvitesScreen.this.drawString(invite.worldOwnerName, x + 2, y + 12, 7105644);
            RealmsPendingInvitesScreen.this.drawString(RealmsUtil.convertToAgePresentation(System.currentTimeMillis() - invite.date.getTime()), x + 2, y + 24, 7105644);
            final int dx = this.getScrollbarPosition() - 50;
            this.drawAccept(dx, y, this.xm(), this.ym());
            this.drawReject(dx + 20, y, this.xm(), this.ym());
            RealmsScreen.bindFace(invite.worldOwnerUuid, invite.worldOwnerName);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            RealmsScreen.blit(x - 36, y, 8.0f, 8.0f, 8, 8, 32, 32, 64.0f, 64.0f);
            RealmsScreen.blit(x - 36, y, 40.0f, 8.0f, 8, 8, 32, 32, 64.0f, 64.0f);
        }
        
        private void drawAccept(final int x, final int y, final int xm, final int ym) {
            boolean hovered = false;
            if (xm >= x && xm <= x + 15 && ym >= y && ym <= y + 15 && ym < RealmsPendingInvitesScreen.this.height() - 40 && ym > 32) {
                hovered = true;
            }
            RealmsScreen.bind("realms:textures/gui/realms/accept_icon.png");
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glPushMatrix();
            RealmsScreen.blit(x, y, hovered ? 19.0f : 0.0f, 0.0f, 18, 18, 37.0f, 18.0f);
            GL11.glPopMatrix();
            if (hovered) {
                RealmsPendingInvitesScreen.this.toolTip = RealmsScreen.getLocalizedString("mco.invites.button.accept");
            }
        }
        
        private void drawReject(final int x, final int y, final int xm, final int ym) {
            boolean hovered = false;
            if (xm >= x && xm <= x + 15 && ym >= y && ym <= y + 15 && ym < RealmsPendingInvitesScreen.this.height() - 40 && ym > 32) {
                hovered = true;
            }
            RealmsScreen.bind("realms:textures/gui/realms/reject_icon.png");
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glPushMatrix();
            RealmsScreen.blit(x, y, hovered ? 19.0f : 0.0f, 0.0f, 18, 18, 37.0f, 18.0f);
            GL11.glPopMatrix();
            if (hovered) {
                RealmsPendingInvitesScreen.this.toolTip = RealmsScreen.getLocalizedString("mco.invites.button.reject");
            }
        }
        
        @Override
        public void itemClicked(final int clickSlotPos, final int slot, final int xm, final int ym, final int width) {
            final int x = this.getScrollbarPosition() - 50;
            final int y = clickSlotPos + 30 - this.getScroll();
            if (xm >= x && xm <= x + 15 && ym >= y && ym <= y + 15) {
                RealmsPendingInvitesScreen.this.accept(slot);
            }
            else if (xm >= x + 20 && xm <= x + 20 + 15 && ym >= y && ym <= y + 15) {
                RealmsPendingInvitesScreen.this.reject(slot);
            }
        }
        
        @Override
        public void customMouseEvent(final int y0, final int y1, final int headerHeight, final float yo, final int itemHeight) {
            if (Mouse.isButtonDown(0) && this.ym() >= y0 && this.ym() <= y1) {
                final int x0 = this.width() / 2 - 92;
                final int x2 = this.width();
                final int clickSlotPos = this.ym() - y0 - headerHeight + (int)yo - 4;
                final int slot = clickSlotPos / itemHeight;
                if (this.xm() >= x0 && this.xm() <= x2 && slot >= 0 && clickSlotPos >= 0 && slot < this.getItemCount()) {
                    this.itemClicked(clickSlotPos, slot, this.xm(), this.ym(), this.width());
                }
            }
        }
    }
}
