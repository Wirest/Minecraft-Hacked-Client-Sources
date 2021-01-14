package moonx.ohare.client.module.impl.other;

import java.awt.Color;
import java.util.ArrayList;

import moonx.ohare.client.event.bus.Handler;
import moonx.ohare.client.event.impl.game.PacketEvent;
import moonx.ohare.client.event.impl.player.UpdateEvent;
import moonx.ohare.client.event.impl.render.Render3DEvent;
import moonx.ohare.client.module.Module;
import moonx.ohare.client.utils.GLUtil;
import moonx.ohare.client.utils.Printer;
import moonx.ohare.client.utils.RenderUtil;
import org.lwjgl.opengl.GL11;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBed;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemPotion;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;

public class MurderMystery extends Module {
    public static ArrayList<EntityPlayer> murderers = new ArrayList<>();
    public static ArrayList<EntityPlayer> gudpeople = new ArrayList<>();
    private final String[] nigga = new String[]{"1st Killer - ", "1st Place - ", "Winner: ", " - Damage Dealt - ", "1st - ", "Winning Team - ", "Winners: ", "Winner: ", "Winning Team: ", " win the game!", "Top Seeker: ","1st Place: ","Last team standing!","Winner #1 (", "Top Survivors","Winners - "};

    public MurderMystery() {
        super("MurderMystery", Category.OTHER, new Color(62, 131, 227, 255).getRGB());
        setRenderLabel("Murder Mystery");
        setDescription("Shows all murderers and good guys.");
    }

    @Override
    public void onDisable() {
        murderers.clear();
        gudpeople.clear();
    }

    @Handler
    public void onRender3D(Render3DEvent event) {
        for (EntityPlayer murderer : murderers) {
            double x = murderer.lastTickPosX + (murderer.posX - murderer.lastTickPosX) * event.getPartialTicks();
            double y = murderer.lastTickPosY + (murderer.posY - murderer.lastTickPosY) * event.getPartialTicks();
            double z = murderer.lastTickPosZ + (murderer.posZ - murderer.lastTickPosZ) * event.getPartialTicks();
			drawEntityESP(x - getMc().getRenderManager().getRenderPosX(), y - getMc().getRenderManager().getRenderPosY(), z - getMc().getRenderManager().getRenderPosZ(), murderer.height - 0.1, murderer.width - 0.12,new Color(0xE33726));
        }
        for (EntityPlayer good : gudpeople) {
            double x = good.lastTickPosX + (good.posX - good.lastTickPosX) * event.getPartialTicks();
            double y = good.lastTickPosY + (good.posY - good.lastTickPosY) * event.getPartialTicks();
            double z = good.lastTickPosZ + (good.posZ - good.lastTickPosZ) * event.getPartialTicks();
			drawEntityESP(x - getMc().getRenderManager().getRenderPosX(), y - getMc().getRenderManager().getRenderPosY(), z - getMc().getRenderManager().getRenderPosZ(), good.height - 0.1, good.width - 0.12,new Color(0x3E83E3));
        }
    }

    @Handler
    public void onUpdate(UpdateEvent event) {
        if (getMc().getCurrentServerData() != null && getMc().theWorld != null) {
            for (Object entities : getMc().theWorld.loadedEntityList) {
                if (entities instanceof EntityPlayer) {
                    EntityPlayer entity = (EntityPlayer) entities;
                    if (murderers.contains(entity) && !entity.isEntityAlive()) {
                        murderers.remove(entity);
                    }
                    if (gudpeople.contains(entity) && !entity.isEntityAlive()) {
                        gudpeople.remove(entity);
                    }
                    if (entity != getMc().thePlayer && !entity.isDead) {
                        if (!murderers.contains(entity)) {
                            if (entity.getHeldItem() != null) {
                                if (!(entity.getHeldItem().getItem() instanceof ItemMap) && !(entity.getHeldItem().getItem() instanceof ItemBow) && !(entity.getHeldItem().getItem() instanceof ItemBed) && entity.getHeldItem().getItem() != Items.gold_ingot && entity.getHeldItem().getItem() != Items.arrow && entity.getHeldItem().getItem() != Items.dye && !(entity.getHeldItem().getItem() instanceof ItemPotion) && !(entity.getHeldItem().getItem() instanceof ItemBlock)) {
                                    Printer.print(EnumChatFormatting.RED + entity.getName() + " might be a murderer watch out!");
                                    murderers.add(entity);
                                }
                            }
                        }
                        if (!gudpeople.contains(entity) && !murderers.contains(entity)) {
                            if (entity.getHeldItem() != null) {
                                if (entity.getHeldItem().getItem() instanceof ItemBow) {
                                    Printer.print(EnumChatFormatting.BLUE + entity.getName() + " is a good guy.");
                                    gudpeople.add(entity);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (getMc().thePlayer.isDead) {
            gudpeople.clear();
            murderers.clear();
        }
    }
    @Handler
    public void onPacket(PacketEvent event) {
        if (!event.isSending()) {
            if (event.getPacket() instanceof S02PacketChat) {
                S02PacketChat packet = (S02PacketChat) event.getPacket();
                for (String str : nigga) {
                    if (packet.getChatComponent().getUnformattedText().contains(str)) {
                        gudpeople.clear();
                        murderers.clear();
                    }
                }
            }
        }
    }
    private void drawEntityESP(double x, double y, double z, double height, double width, Color color) {
        GL11.glPushMatrix();
        GLUtil.setGLCap(3042, true);
        GLUtil.setGLCap(3553, false);
        GLUtil.setGLCap(2896, false);
        GLUtil.setGLCap(2929, false);
        GL11.glDepthMask(false);
        GL11.glLineWidth(1.8f);
        GL11.glBlendFunc(770, 771);
        GLUtil.setGLCap(2848, true);
        GL11.glDepthMask(true);
        RenderUtil.BB(new AxisAlignedBB(x - width, y + 0.1, z - width, x + width, y + height + 0.25, z + width), new Color(color.getRed(), color.getGreen(), color.getBlue(), 120).getRGB());
        RenderUtil.OutlinedBB(new AxisAlignedBB(x - width, y + 0.1, z - width, x + width, y + height + 0.25, z + width), 1, color.getRGB());
        GLUtil.revertAllCaps();
        GL11.glPopMatrix();
        GL11.glColor4f(1, 1, 1, 1);
    }
}