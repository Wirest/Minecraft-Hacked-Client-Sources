package me.rigamortis.faurax.module.modules.render;

import me.rigamortis.faurax.module.helpers.*;
import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.*;
import me.rigamortis.faurax.values.*;
import com.darkmagician6.eventapi.*;
import me.rigamortis.faurax.events.*;
import net.minecraft.tileentity.*;
import net.minecraft.client.renderer.entity.*;
import me.rigamortis.faurax.utils.*;
import org.lwjgl.opengl.*;
import net.minecraft.entity.*;
import net.minecraft.client.entity.*;
import net.minecraft.entity.player.*;
import java.text.*;
import me.rigamortis.faurax.friends.*;
import java.util.*;
import java.util.regex.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;

public class Tracers extends Module implements RenderHelper, Runnable
{
    public static boolean state;
    public static float alpha;
    public static Value mobs;
    public static Value animals;
    public static Value players;
    public static Value chests;
    public static Value spine;
    public static Value color;
    public static Value colorRed;
    public static Value colorBlue;
    public static Value colorGreen;
    public static Value colorAlpha;
    public static Value width;
    
    static {
        Tracers.mobs = new Value("Tracers", Boolean.TYPE, "Mobs", false);
        Tracers.animals = new Value("Tracers", Boolean.TYPE, "Animals", false);
        Tracers.players = new Value("Tracers", Boolean.TYPE, "Players", true);
        Tracers.chests = new Value("Tracers", Boolean.TYPE, "Chests", false);
        Tracers.spine = new Value("Tracers", Boolean.TYPE, "Spine", false);
        Tracers.color = new Value("Tracers", String.class, "Color", "Distance", new String[] { "Distance", "Health", "Team", "Custom" });
        Tracers.colorRed = new Value("Tracers", Float.TYPE, "Red", 0.0f, 0.0f, 1.0f);
        Tracers.colorBlue = new Value("Tracers", Float.TYPE, "Blue", 0.0f, 0.0f, 1.0f);
        Tracers.colorGreen = new Value("Tracers", Float.TYPE, "Green", 0.0f, 0.0f, 1.0f);
        Tracers.colorAlpha = new Value("Tracers", Float.TYPE, "Alpha", 0.0f, 0.0f, 1.0f);
        Tracers.width = new Value("Tracers", Float.TYPE, "Width", 1.0f, 1.0f, 3.0f);
    }
    
    public Tracers() {
        this.setKey("O");
        this.setName("Tracers");
        this.setType(ModuleType.RENDER);
        this.setColor(-15096001);
        this.setModInfo("");
        this.setVisible(true);
        Client.getValues();
        ValueManager.values.add(Tracers.mobs);
        Client.getValues();
        ValueManager.values.add(Tracers.animals);
        Client.getValues();
        ValueManager.values.add(Tracers.players);
        Client.getValues();
        ValueManager.values.add(Tracers.spine);
        Client.getValues();
        ValueManager.values.add(Tracers.chests);
        Client.getValues();
        ValueManager.values.add(Tracers.color);
        Client.getValues();
        ValueManager.values.add(Tracers.colorRed);
        Client.getValues();
        ValueManager.values.add(Tracers.colorBlue);
        Client.getValues();
        ValueManager.values.add(Tracers.colorGreen);
        Client.getValues();
        ValueManager.values.add(Tracers.colorAlpha);
        Client.getValues();
        ValueManager.values.add(Tracers.width);
    }
    
    @EventTarget(4)
    public void setupViewBobbing(final EventBobWorld event) {
        if (this.isToggled()) {
            event.setCancelled(true);
        }
    }
    
    @EventTarget(4)
    public void renderWorld(final EventRenderWorld e) {
        if (this.isToggled()) {
            if (Tracers.chests.getBooleanValue()) {
                for (final Object o : Tracers.mc.theWorld.loadedTileEntityList) {
                    final TileEntity tileEntity = (TileEntity)o;
                    if (tileEntity instanceof TileEntityChest || tileEntity instanceof TileEntityEnderChest) {
                        final double n = tileEntity.getPos().getX() + 0.5f;
                        Tracers.mc.getRenderManager();
                        final double x = n - RenderManager.renderPosX;
                        final double n2 = tileEntity.getPos().getY() + 1.8f;
                        Tracers.mc.getRenderManager();
                        final double y = n2 - RenderManager.renderPosY;
                        final double n3 = tileEntity.getPos().getZ() + 0.5f;
                        Tracers.mc.getRenderManager();
                        final double z = n3 - RenderManager.renderPosZ;
                        final GuiUtils guiUtils = Tracers.guiUtils;
                        GuiUtils.start3DGLConstants();
                        GL11.glEnable(2848);
                        GL11.glHint(3154, 4353);
                        GL11.glLineWidth(Tracers.width.getFloatValue());
                        GL11.glColor4f(1.0f, 0.7f, 0.0f, Tracers.alpha);
                        GL11.glBegin(1);
                        GL11.glVertex3f(0.0f, Tracers.mc.thePlayer.getEyeHeight(), 0.0f);
                        GL11.glVertex3d(x, y - 1.4199999570846558, z);
                        GL11.glEnd();
                        GL11.glDisable(2848);
                        final GuiUtils guiUtils2 = Tracers.guiUtils;
                        GuiUtils.finish3DGLConstants();
                        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                    }
                }
            }
            for (final Object i : Tracers.mc.theWorld.loadedEntityList) {
                if (i instanceof EntityLivingBase) {
                    final Entity entity = (Entity)i;
                    if (entity.getName() == Tracers.mc.thePlayer.getName() || entity instanceof EntityPlayerSP || !validEntity(entity)) {
                        continue;
                    }
                    final float distance = Tracers.mc.thePlayer.getDistanceToEntity(entity);
                    final float health = ((EntityLivingBase)entity).getHealth();
                    final float posX = (float)(entity.lastTickPosX + (entity.posX - entity.lastTickPosX) - RenderManager.renderPosX);
                    final float posY = (float)(entity.lastTickPosY + 1.4 + (entity.posY - entity.lastTickPosY) - RenderManager.renderPosY);
                    final float posZ = (float)(entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) - RenderManager.renderPosZ);
                    final GuiUtils guiUtils3 = Tracers.guiUtils;
                    GuiUtils.start3DGLConstants();
                    GL11.glEnable(2848);
                    GL11.glHint(3154, 4353);
                    GL11.glLineWidth(Tracers.width.getFloatValue());
                    if (Tracers.color.getSelectedOption().equalsIgnoreCase("Distance")) {
                        if (distance <= 5.0f) {
                            GL11.glColor4f(1.0f, 0.0f, 0.0f, Tracers.alpha);
                        }
                        else if (distance <= 40.0f) {
                            GL11.glColor4f(1.0f, distance / 100.0f, 0.0f, Tracers.alpha);
                        }
                        else if (distance > 40.0f) {
                            GL11.glColor4f(0.0f, 255.0f, 0.0f, Tracers.alpha);
                        }
                    }
                    if (Tracers.color.getSelectedOption().equalsIgnoreCase("Team")) {
                        if (this.isOnSameTeam((EntityPlayer)entity, Tracers.mc.thePlayer)) {
                            GL11.glColor4f(0.0f, 255.0f, 0.0f, Tracers.alpha);
                        }
                        else {
                            GL11.glColor4f(255.0f, 0.0f, 0.0f, Tracers.alpha);
                        }
                    }
                    if (Tracers.color.getSelectedOption().equalsIgnoreCase("Custom")) {
                        GL11.glColor4f(Tracers.colorRed.getFloatValue(), Tracers.colorGreen.getFloatValue(), Tracers.colorBlue.getFloatValue(), Tracers.colorAlpha.getFloatValue());
                    }
                    if (Tracers.color.getSelectedOption().equalsIgnoreCase("Health")) {
                        final DecimalFormat decimal = new DecimalFormat("#.#");
                        final float percent = Float.valueOf(decimal.format(health / 2.0f));
                        if (percent >= 6.0f) {
                            GL11.glColor4f(0.0f, 1.0f, 0.0f, Tracers.alpha);
                        }
                        if (percent < 6.0f) {
                            GL11.glColor4f(1.0f, 0.5f, 0.0f, Tracers.alpha);
                        }
                        if (percent < 3.0f) {
                            GL11.glColor4f(1.0f, 0.0f, 0.0f, Tracers.alpha);
                        }
                    }
                    if (FriendManager.isFriend(entity.getName())) {
                        GL11.glColor4f(0.0f, 255.0f, 10.0f, Tracers.alpha);
                    }
                    GL11.glBegin(1);
                    GL11.glVertex3f(0.0f, Tracers.mc.thePlayer.getEyeHeight(), 0.0f);
                    GL11.glVertex3f(posX, posY - 1.42f, posZ);
                    if (Tracers.spine.getBooleanValue()) {
                        GL11.glVertex3f(posX, posY - 1.42f, posZ);
                        GL11.glVertex3f(posX, posY - 1.5f + entity.height * (((EntityLivingBase)entity).getHealth() / ((EntityLivingBase)entity).getMaxHealth()), posZ);
                    }
                    GL11.glEnd();
                    GL11.glDisable(2848);
                    final GuiUtils guiUtils4 = Tracers.guiUtils;
                    GuiUtils.finish3DGLConstants();
                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                }
            }
        }
    }
    
    public boolean isOnSameTeam(final EntityPlayer e, final EntityPlayer e2) {
        return e.getDisplayName().getFormattedText().contains("§" + this.getTeamFromName(e)) && e2.getDisplayName().getFormattedText().contains("§" + this.getTeamFromName(e));
    }
    
    public String getTeamColor(final EntityPlayer e) {
        return this.getTeamFromName(e);
    }
    
    public String getTeamFromName(final Entity e) {
        final Matcher m = Pattern.compile("§(.).*§r").matcher(e.getDisplayName().getFormattedText());
        if (m.find()) {
            return m.group(1);
        }
        return "f";
    }
    
    public static boolean validEntity(final Entity e) {
        final boolean isValidEntity = (Tracers.mobs.getBooleanValue() && e instanceof EntityMob && !(e instanceof EntityAnimal) && !(e instanceof EntityPlayer)) || (Tracers.animals.getBooleanValue() && e instanceof EntityAnimal && !(e instanceof EntityMob) && !(e instanceof EntityPlayer)) || (Tracers.players.getBooleanValue() && e instanceof EntityPlayer && !(e instanceof EntityMob) && !(e instanceof EntityAnimal));
        return isValidEntity;
    }
    
    @Override
    public void onEnabled() {
        super.onEnabled();
        Tracers.state = true;
        new Thread(new Tracers()).start();
    }
    
    @Override
    public void onDisabled() {
        super.onDisabled();
        Tracers.state = false;
        new Thread(new Tracers()).start();
    }
    
    @Override
    public void run() {
        if (Tracers.state) {
            while (Tracers.alpha < 0.7f) {
                if (!Client.getModules().isModToggled("Tracers")) {
                    break;
                }
                Tracers.alpha += 0.01f;
                try {
                    Thread.sleep(20L);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            Tracers.alpha = 0.0f;
        }
    }
}
