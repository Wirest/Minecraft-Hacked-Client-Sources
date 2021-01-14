package net.minecraft.client.gui;

import com.google.common.base.Charsets;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.base64.Base64;

import java.awt.image.BufferedImage;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerListEntryNormal implements GuiListExtended.IGuiListEntry {
    private static final Logger logger = LogManager.getLogger();
    private static final ThreadPoolExecutor field_148302_b = new ScheduledThreadPoolExecutor(5, (new ThreadFactoryBuilder()).setNameFormat("Server Pinger #%d").setDaemon(true).build());
    private static final ResourceLocation field_178015_c = new ResourceLocation("textures/misc/unknown_server.png");
    private static final ResourceLocation field_178014_d = new ResourceLocation("textures/gui/server_selection.png");
    private final GuiMultiplayer field_148303_c;
    private final Minecraft field_148300_d;
    private final ServerData field_148301_e;
    private final ResourceLocation field_148306_i;
    private String field_148299_g;
    private DynamicTexture field_148305_h;
    private long field_148298_f;
    private static final String __OBFID = "CL_00000817";

    protected ServerListEntryNormal(GuiMultiplayer p_i45048_1_, ServerData p_i45048_2_) {
        this.field_148303_c = p_i45048_1_;
        this.field_148301_e = p_i45048_2_;
        this.field_148300_d = Minecraft.getMinecraft();
        this.field_148306_i = new ResourceLocation("servers/" + p_i45048_2_.serverIP + "/icon");
        this.field_148305_h = (DynamicTexture) this.field_148300_d.getTextureManager().getTexture(this.field_148306_i);
    }

    public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected) {
        if (!this.field_148301_e.field_78841_f) {
            this.field_148301_e.field_78841_f = true;
            this.field_148301_e.pingToServer = -2L;
            this.field_148301_e.serverMOTD = "";
            this.field_148301_e.populationInfo = "";
            field_148302_b.submit(new Runnable() {
                private static final String __OBFID = "CL_00000818";

                public void run() {
                    try {
                        ServerListEntryNormal.this.field_148303_c.getOldServerPinger().ping(ServerListEntryNormal.this.field_148301_e);
                    } catch (UnknownHostException var2) {
                        ServerListEntryNormal.this.field_148301_e.pingToServer = -1L;
                        ServerListEntryNormal.this.field_148301_e.serverMOTD = EnumChatFormatting.DARK_RED + "Can\'t resolve hostname";
                    } catch (Exception var3) {
                        ServerListEntryNormal.this.field_148301_e.pingToServer = -1L;
                        ServerListEntryNormal.this.field_148301_e.serverMOTD = EnumChatFormatting.DARK_RED + "Can\'t connect to server.";
                    }
                }
            });
        }

        boolean var9 = this.field_148301_e.version > 47;
        boolean var10 = this.field_148301_e.version < 47;
        boolean var11 = var9 || var10;
        this.field_148300_d.fontRendererObj.drawString(this.field_148301_e.serverName, x + 32 + 3, y + 1, 16777215);
        List var12 = this.field_148300_d.fontRendererObj.listFormattedStringToWidth(this.field_148301_e.serverMOTD, listWidth - 32 - 2);

        for (int var13 = 0; var13 < Math.min(var12.size(), 2); ++var13) {
            this.field_148300_d.fontRendererObj.drawString((String) var12.get(var13), x + 32 + 3, y + 12 + this.field_148300_d.fontRendererObj.FONT_HEIGHT * var13, 8421504);
        }

        String var23 = var11 ? EnumChatFormatting.DARK_RED + this.field_148301_e.gameVersion : this.field_148301_e.populationInfo;
        int var14 = this.field_148300_d.fontRendererObj.getStringWidth(var23);
        this.field_148300_d.fontRendererObj.drawString(var23, x + listWidth - var14 - 15 - 2, y + 1, 8421504);
        byte var15 = 0;
        String var17 = null;
        int var16;
        String var18;

        if (var11) {
            var16 = 5;
            var18 = var9 ? "Client out of date!" : "Server out of date!";
            var17 = this.field_148301_e.playerList;
        } else if (this.field_148301_e.field_78841_f && this.field_148301_e.pingToServer != -2L) {
            if (this.field_148301_e.pingToServer < 0L) {
                var16 = 5;
            } else if (this.field_148301_e.pingToServer < 150L) {
                var16 = 0;
            } else if (this.field_148301_e.pingToServer < 300L) {
                var16 = 1;
            } else if (this.field_148301_e.pingToServer < 600L) {
                var16 = 2;
            } else if (this.field_148301_e.pingToServer < 1000L) {
                var16 = 3;
            } else {
                var16 = 4;
            }

            if (this.field_148301_e.pingToServer < 0L) {
                var18 = "(no connection)";
            } else {
                var18 = this.field_148301_e.pingToServer + "ms";
                var17 = this.field_148301_e.playerList;
            }
        } else {
            var15 = 1;
            var16 = (int) (Minecraft.getSystemTime() / 100L + (long) (slotIndex * 2) & 7L);

            if (var16 > 4) {
                var16 = 8 - var16;
            }

            var18 = "Pinging...";
        }

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.field_148300_d.getTextureManager().bindTexture(Gui.icons);
        Gui.drawModalRectWithCustomSizedTexture(x + listWidth - 15, y, (float) (var15 * 10), (float) (176 + var16 * 8), 10, 8, 256.0F, 256.0F);

        if (this.field_148301_e.getBase64EncodedIconData() != null && !this.field_148301_e.getBase64EncodedIconData().equals(this.field_148299_g)) {
            this.field_148299_g = this.field_148301_e.getBase64EncodedIconData();
            this.prepareServerIcon();
            this.field_148303_c.getServerList().saveServerList();
        }

        if (this.field_148305_h != null) {
            this.func_178012_a(x, y, this.field_148306_i);
        } else {
            this.func_178012_a(x, y, field_178015_c);
        }

        int var19 = mouseX - x;
        int var20 = mouseY - y;

        if (var19 >= listWidth - 15 && var19 <= listWidth - 5 && var20 >= 0 && var20 <= 8) {
            this.field_148303_c.func_146793_a(var18);
        } else if (var19 >= listWidth - var14 - 15 - 2 && var19 <= listWidth - 15 - 2 && var20 >= 0 && var20 <= 8) {
            this.field_148303_c.func_146793_a(var17);
        }

        if (this.field_148300_d.gameSettings.touchscreen || isSelected) {
            this.field_148300_d.getTextureManager().bindTexture(field_178014_d);
            Gui.drawRect(x, y, x + 32, y + 32, -1601138544);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            int var21 = mouseX - x;
            int var22 = mouseY - y;

            if (this.func_178013_b()) {
                if (var21 < 32 && var21 > 16) {
                    Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 32.0F, 32, 32, 256.0F, 256.0F);
                } else {
                    Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, 32, 32, 256.0F, 256.0F);
                }
            }

            if (this.field_148303_c.func_175392_a(this, slotIndex)) {
                if (var21 < 16 && var22 < 16) {
                    Gui.drawModalRectWithCustomSizedTexture(x, y, 96.0F, 32.0F, 32, 32, 256.0F, 256.0F);
                } else {
                    Gui.drawModalRectWithCustomSizedTexture(x, y, 96.0F, 0.0F, 32, 32, 256.0F, 256.0F);
                }
            }

            if (this.field_148303_c.func_175394_b(this, slotIndex)) {
                if (var21 < 16 && var22 > 16) {
                    Gui.drawModalRectWithCustomSizedTexture(x, y, 64.0F, 32.0F, 32, 32, 256.0F, 256.0F);
                } else {
                    Gui.drawModalRectWithCustomSizedTexture(x, y, 64.0F, 0.0F, 32, 32, 256.0F, 256.0F);
                }
            }
        }
    }

    protected void func_178012_a(int p_178012_1_, int p_178012_2_, ResourceLocation p_178012_3_) {
        this.field_148300_d.getTextureManager().bindTexture(p_178012_3_);
        GlStateManager.enableBlend();
        Gui.drawModalRectWithCustomSizedTexture(p_178012_1_, p_178012_2_, 0.0F, 0.0F, 32, 32, 32.0F, 32.0F);
        GlStateManager.disableBlend();
    }

    private boolean func_178013_b() {
        return true;
    }

    private void prepareServerIcon() {
        if (this.field_148301_e.getBase64EncodedIconData() == null) {
            this.field_148300_d.getTextureManager().deleteTexture(this.field_148306_i);
            this.field_148305_h = null;
        } else {
            ByteBuf var2 = Unpooled.copiedBuffer(this.field_148301_e.getBase64EncodedIconData(), Charsets.UTF_8);
            ByteBuf var3 = Base64.decode(var2);
            BufferedImage var1;
            label74:
            {
                try {
                    var1 = TextureUtil.func_177053_a(new ByteBufInputStream(var3));
                    Validate.validState(var1.getWidth() == 64, "Must be 64 pixels wide", new Object[0]);
                    Validate.validState(var1.getHeight() == 64, "Must be 64 pixels high", new Object[0]);
                    break label74;
                } catch (Exception var8) {
                    logger.error("Invalid icon for server " + this.field_148301_e.serverName + " (" + this.field_148301_e.serverIP + ")", var8);
                    this.field_148301_e.setBase64EncodedIconData((String) null);
                } finally {
                    var2.release();
                    var3.release();
                }

                return;
            }

            if (this.field_148305_h == null) {
                this.field_148305_h = new DynamicTexture(var1.getWidth(), var1.getHeight());
                this.field_148300_d.getTextureManager().loadTexture(this.field_148306_i, this.field_148305_h);
            }

            var1.getRGB(0, 0, var1.getWidth(), var1.getHeight(), this.field_148305_h.getTextureData(), 0, var1.getWidth());
            this.field_148305_h.updateDynamicTexture();
        }
    }

    /**
     * Returns true if the mouse has been pressed on this control.
     */
    public boolean mousePressed(int p_148278_1_, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_, int p_148278_6_) {
        if (p_148278_5_ <= 32) {
            if (p_148278_5_ < 32 && p_148278_5_ > 16 && this.func_178013_b()) {
                this.field_148303_c.selectServer(p_148278_1_);
                this.field_148303_c.connectToSelected();
                return true;
            }

            if (p_148278_5_ < 16 && p_148278_6_ < 16 && this.field_148303_c.func_175392_a(this, p_148278_1_)) {
                this.field_148303_c.func_175391_a(this, p_148278_1_, GuiScreen.isShiftKeyDown());
                return true;
            }

            if (p_148278_5_ < 16 && p_148278_6_ > 16 && this.field_148303_c.func_175394_b(this, p_148278_1_)) {
                this.field_148303_c.func_175393_b(this, p_148278_1_, GuiScreen.isShiftKeyDown());
                return true;
            }
        }

        this.field_148303_c.selectServer(p_148278_1_);

        if (Minecraft.getSystemTime() - this.field_148298_f < 250L) {
            this.field_148303_c.connectToSelected();
        }

        this.field_148298_f = Minecraft.getSystemTime();
        return false;
    }

    public void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_) {
    }

    /**
     * Fired when the mouse button is released. Arguments: index, x, y, mouseEvent, relativeX, relativeY
     */
    public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
    }

    public ServerData getServerData() {
        return this.field_148301_e;
    }
}
