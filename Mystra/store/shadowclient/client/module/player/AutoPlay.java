package store.shadowclient.client.module.player;

import java.util.ArrayList;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.clickgui.settings.Setting;
import store.shadowclient.client.event.EventTarget;
import store.shadowclient.client.event.events.EventUpdate;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;
import store.shadowclient.client.utils.TimeHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;

public class AutoPlay extends Module {
	
	TimeHelper delay = new TimeHelper();
    boolean cansend = false;
    double offset = 0;
    int width = 140;
    int height = 28;
    private long fadedIn;
    private long fadeOut;
    private long start;
    private long end;
    long time = getTime();

    public AutoPlay() {
        super("AutoPlay", 0, Category.PLAYER);
        
        ArrayList<String> options = new ArrayList<>();
        options.add("Solo Insane");
        options.add("Solo Normal");
        options.add("Teams Insane");
        options.add("Teams Normal");
        
        Shadow.instance.settingsManager.rSetting(new Setting("AutoPlay Mode", this, "Solo Insane", options));
    }


    private String getServerMessage() {
    	String mode = Shadow.instance.settingsManager.getSettingByName("AutoPlay Mode").getValString();
    	
    	if(mode.equalsIgnoreCase("Solo Insane")) {
	        if (this.mc.getCurrentServerData().serverIP.equalsIgnoreCase("hypixel.net")) {
	            return "/play solo_insane";
	        }
    	}
    	
    	if(mode.equalsIgnoreCase("Solo Normal")) {
	        if (this.mc.getCurrentServerData().serverIP.equalsIgnoreCase("hypixel.net")) {
	            return "/play solo_normal";
	        }
    	}
    	
    	if(mode.equalsIgnoreCase("Teams Insane")) {
	        if (this.mc.getCurrentServerData().serverIP.equalsIgnoreCase("hypixel.net")) {
	            return "/play teams_insane";
	        }
    	}
    	
    	if(mode.equalsIgnoreCase("Teams Normal")) {
	        if (this.mc.getCurrentServerData().serverIP.equalsIgnoreCase("hypixel.net")) {
	            return "/play teams_normal";
	        }
    	}
        return null;
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (this.mc.getCurrentServerData() == null) {
            return;
        }
        /*
         *  CHECKING IF PLAYER IS DEAD (IF DEAD THEN SEND TO NEXT GAME)
         */
        
        if ((this.mc.thePlayer.isDead) || (this.mc.thePlayer.getHealth() == 0.0F) || (this.mc.thePlayer.isSpectator()) || (this.mc.thePlayer.capabilities.allowFlying)) {
            this.cansend = true;
        }
        
        if (this.delay.hasReached(1000)) {
            if ((this.mc.getCurrentServerData().serverIP.equalsIgnoreCase("blocksmc.com")) || (this.mc.getCurrentServerData().serverIP.equalsIgnoreCase("mc-central.net") || (this.mc.getCurrentServerData().serverIP.equalsIgnoreCase("mooncraft.es")))) {
                if (this.cansend) {
                    int i = 7;
                    if (this.mc.getCurrentServerData().serverIP.equalsIgnoreCase("mc-central.net")) {
                        i = 4;
                    }
                    C09PacketHeldItemChange localC09PacketHeldItemChange1 = new C09PacketHeldItemChange(i);
                    this.mc.thePlayer.sendQueue.getNetworkManager().sendPacket(localC09PacketHeldItemChange1);
                    try {
                        this.mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(this.mc.thePlayer.inventoryContainer.getSlot(i).getStack()));
                    } catch (Exception localException) {
                    }
                    C09PacketHeldItemChange localC09PacketHeldItemChange2 = new C09PacketHeldItemChange(this.mc.thePlayer.inventory.currentItem);
                    this.mc.thePlayer.sendQueue.getNetworkManager().sendPacket(localC09PacketHeldItemChange2);
                    drawRect(100, 100, 100 - 0 + 4, 100 - 5, -1);
                    Shadow.addChatMessage("Sending to New Game");
                    this.cansend = false;
                }
            } else if (this.mc.getCurrentServerData().serverIP.equalsIgnoreCase("hypixel.net")) {
                if (this.cansend) {
                    if (getServerMessage() == null) {
                    	drawRect(100, 100, 100 - 0 + 4, 100 - 5, -1);
                    	Shadow.addChatMessage("AutoPlay does not work on this server");
                    } else {
                        this.mc.thePlayer.sendChatMessage(getServerMessage());
                        drawRect(100, 100, 100 - 0 + 4, 100 - 5, -1);
                        Shadow.addChatMessage("Sending to New Game");
                    }
                    this.cansend = false;
                }
            } else if (this.mc.getCurrentServerData().serverIP.equalsIgnoreCase("hivemc.com")) {
                if (this.cansend) {
                    if (getServerMessage() == null) {
                        this.mc.thePlayer.sendChatMessage(getServerMessage());
                        drawRect(100, 100, 100 - 0 + 4, 100 - 5, -1);
                        Shadow.addChatMessage("Sending to New Game");
                    }
                    this.cansend = false;
                }
            } else if ((this.mc.thePlayer.ticksExisted < 13) && (this.mc.thePlayer.ticksExisted > 9) && (this.cansend)) {
                if (getServerMessage() == null) {
                	Shadow.addChatMessage("AutoPlay does not work on this server");
                	drawRect(100, 100, 100 - 0 + 4, 100 - 5, -1);
                } else {
                    this.mc.thePlayer.sendChatMessage(getServerMessage());
                    Shadow.addChatMessage("Sending to New Game");
                    drawRect(100, 100, 100 - 0 + 4, 100 - 5, -1);
                }
                this.cansend = false;
            }
            this.delay.reset();
        }
        if (time < fadedIn) {
            offset = Math.tanh(time / (double) (fadedIn) * 3.0) * width;
        } else if (time > fadeOut) {
            offset = (Math.tanh(2.3 - (time - fadeOut) / (double) (end - fadeOut) * 2.8) * width);
        } else {
            offset = width;
        }
    }
    
    
    public void show() {
        start = System.currentTimeMillis();
    }

    public boolean isShown() {
        return getTime() <= end;
    }

    private long getTime() {
        return System.currentTimeMillis() - start;
    }
    
    public static void drawRect(double left, double top, double right, double bottom, int color) {
        if (left < right) {
            double i = left;
            left = right;
            right = i;
        }

        if (top < bottom) {
            double j = top;
            top = bottom;
            bottom = j;
        }

        float f3 = (float) (color >> 24 & 255) / 255.0F;
        float f = (float) (color >> 16 & 255) / 255.0F;
        float f1 = (float) (color >> 8 & 255) / 255.0F;
        float f2 = (float) (color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f, f1, f2, f3);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(left, bottom, 0.0D).endVertex();
        worldrenderer.pos(right, bottom, 0.0D).endVertex();
        worldrenderer.pos(right, top, 0.0D).endVertex();
        worldrenderer.pos(left, top, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawRect(int mode, double left, double top, double right, double bottom, int color) {
        if (left < right) {
            double i = left;
            left = right;
            right = i;
        }

        if (top < bottom) {
            double j = top;
            top = bottom;
            bottom = j;
        }

        float f3 = (float) (color >> 24 & 255) / 255.0F;
        float f = (float) (color >> 16 & 255) / 255.0F;
        float f1 = (float) (color >> 8 & 255) / 255.0F;
        float f2 = (float) (color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f, f1, f2, f3);
        worldrenderer.begin(mode, DefaultVertexFormats.POSITION);
        worldrenderer.pos(left, bottom, 0.0D).endVertex();
        worldrenderer.pos(right, bottom, 0.0D).endVertex();
        worldrenderer.pos(right, top, 0.0D).endVertex();
        worldrenderer.pos(left, top, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
}