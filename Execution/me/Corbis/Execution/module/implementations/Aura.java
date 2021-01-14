package me.Corbis.Execution.module.implementations;


import de.Hero.settings.Setting;
import me.Corbis.Execution.Execution;
import me.Corbis.Execution.event.Event;
import me.Corbis.Execution.event.EventTarget;
import me.Corbis.Execution.event.events.Event2D;
import me.Corbis.Execution.event.events.Event3D;
import me.Corbis.Execution.event.events.EventMotionUpdate;
import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import me.Corbis.Execution.ui.UnicodeFontRenderer;
import me.Corbis.Execution.utils.*;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.*;
import net.minecraft.util.*;
import org.lwjgl.input.Keyboard;
import de.Hero.settings.Setting;

import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static net.minecraft.client.gui.inventory.GuiInventory.drawEntityOnScreen;
import static org.lwjgl.opengl.GL11.*;

public class Aura extends Module {
    //Settings
    public Setting minAps;
    public Setting maxAps;
    public Setting targetMode;
    public Setting priority;
    public Setting Range;
    public Setting AutoBlock;
    public Setting AutoBlockMode;
    public Setting KeepSprint;
    public Setting Rotations;
    public Setting RotationsMode;
    public Setting AACCheck;
    public Setting ESP;
    //Variables
    EntityLivingBase target;
    List<EntityLivingBase> loaded = new ArrayList<>();
    TimeHelper timer = new TimeHelper();
    private float lastHealth = 0;
    int index = 0;
    TimeHelper switchTimer = new TimeHelper();

    public Aura() {
        super("Aura", Keyboard.KEY_R, Category.COMBAT);
        ArrayList<String> targetModes = new ArrayList<>();
        targetModes.add("Priority");
        targetModes.add("Switch");
        targetModes.add("Multi");
        ArrayList<String> priorities = new ArrayList<>();
        priorities.add("Health");
        priorities.add("Angle");
        priorities.add("Distance");
        ArrayList<String> AutoBlockModes = new ArrayList<>();
        AutoBlockModes.add("Normal");
        AutoBlockModes.add("Interact");
        ArrayList<String> RotationModes = new ArrayList<>();
        RotationModes.add("Normal");
        RotationModes.add("Smooth");
        RotationModes.add("AAC");
        Execution.instance.settingsManager.rSetting(minAps = new Setting("MinAPS", this, 8, 1, 20, false));
        Execution.instance.settingsManager.rSetting(maxAps = new Setting("MaxAPS", this, 12, 1, 20, false));
        Execution.instance.settingsManager.rSetting(Range = new Setting("Range", this, 4, 0, 10, false));
        Execution.instance.settingsManager.rSetting(targetMode = new Setting("Target Mode", this, "Priority", targetModes));
        Execution.instance.settingsManager.rSetting(priority = new Setting("Target Priority", this, "Angle", priorities));
        Execution.instance.settingsManager.rSetting(AutoBlock = new Setting("Auto Block", this, true));
        Execution.instance.settingsManager.rSetting(AutoBlockMode = new Setting("Auto Block Mode", this, "Normal", AutoBlockModes));
        Execution.instance.settingsManager.rSetting(Rotations = new Setting("Rotations", this, true));
        Execution.instance.settingsManager.rSetting(RotationsMode = new Setting("Rotations Mode", this, "Normal", RotationModes));
        Execution.instance.settingsManager.rSetting(KeepSprint = new Setting("Keep Sprint", this, true));
        Execution.instance.settingsManager.rSetting(AACCheck = new Setting("ACCCheck", this, false));
        Execution.instance.settingsManager.rSetting(ESP = new Setting("ESP", this, false));


    }

    boolean direction = false;
    boolean blocking;

    private boolean isBlockUnder() {
        for (int i = (int) (mc.thePlayer.posY - 1.0); i > 0; --i) {
            BlockPos pos = new BlockPos(mc.thePlayer.posX, i, mc.thePlayer.posZ);
            if (mc.theWorld.getBlockState(pos).getBlock() instanceof BlockAir) continue;
            return true;
        }
        return false;
    }

    @EventTarget
    public void onMotionEvent(EventMotionUpdate event) {
        if (!isBlockUnder() || mc.thePlayer.isCollidedHorizontally) {
            direction = !direction;
        }
        this.setDisplayName("Aura " + EnumChatFormatting.WHITE + "[" + this.targetMode.getValString() + "]");
        if (event.isPre()) {

            if (targetMode.getValString().equalsIgnoreCase("Priority")) {
                if (priority.getValString().equalsIgnoreCase("Health")) {
                    target = getHealthPriority();
                } else if (priority.getValString().equalsIgnoreCase("Angle")) {
                    target = getAnglePriority();
                } else {
                    target = getClosest(Range.getValDouble());
                }
                if (target != null) {
                    if (!KeepSprint.getValBoolean()) {
                        mc.thePlayer.setSprinting(false);
                        mc.gameSettings.keyBindSprint.pressed = false;
                    }
                    if (mc.thePlayer.getDistanceToEntity(target) < Range.getValDouble()) {
                        boolean block = AutoBlock.getValBoolean();
                        float[] rots = getRotations(target, event);
                        event.setYaw(rots[0]);
                        event.setPitch(rots[1]);
                        if (AACCheck.getValBoolean() && (Math.round(event.getYaw()) - Math.round(RotationUtils.getRotations(target)[0]) > 3 || Math.round(event.getPitch()) - Math.round(RotationUtils.getRotations(target)[1]) > 3)) {
                            return;
                        }



                        if (timer.hasReached(randomClickDelay(minAps.getValDouble(), maxAps.getValDouble()))) {
                            if ( block) {
                                mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                            }
                            blocking = false;
                            if(Execution.instance.moduleManager.getModule(Criticals.class).isEnabled){
                                Criticals.criticals(event);
                            }
                            mc.thePlayer.swingItem();
                            mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));

                            timer.reset();
                        }
                    }
                }

            } else if (targetMode.getValString().equalsIgnoreCase("Switch")) {
                this.loaded = this.getTargets();
                if (this.loaded.size() == 0)
                    return;

                if (switchTimer.hasReached(400)) {
                    if (index < loaded.size() - 1) {
                        index++;
                    } else {
                        index = 0;
                    }
                    switchTimer.reset();
                }
                this.target = this.loaded.get(this.index);

                if (target != null) {
                    if (!KeepSprint.getValBoolean()) {
                        mc.thePlayer.setSprinting(false);
                        mc.gameSettings.keyBindSprint.pressed = false;
                    }
                    if (mc.thePlayer.getDistanceToEntity(target) < Range.getValDouble()) {
                        boolean block = AutoBlock.getValBoolean();
                        float[] rots = getRotations(target, event);
                        event.setYaw(rots[0]);
                        event.setPitch(rots[1]);
                        if (AACCheck.getValBoolean() && (Math.round(event.getYaw()) - Math.round(RotationUtils.getRotations(target)[0]) > 3 || Math.round(event.getPitch()) - Math.round(RotationUtils.getRotations(target)[1]) > 3)) {
                            return;
                        }



                        if (timer.hasReached(randomClickDelay(minAps.getValDouble(), maxAps.getValDouble()))) {
                            if ( block) {
                                mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                            }
                            blocking = false;
                            if(Execution.instance.moduleManager.getModule(Criticals.class).isEnabled){
                                Criticals.criticals(event);
                            }
                            mc.thePlayer.swingItem();
                            mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));

                            timer.reset();
                        }
                    }
                }

            } else {
                this.loaded = this.getTargets();
                if (this.loaded.size() == 0)
                    return;
                if (switchTimer.hasReached(1)) {
                    if (index < loaded.size() - 1) {
                        index++;
                    } else {
                        index = 0;
                    }
                    switchTimer.reset();
                    ;
                }
                this.target = this.loaded.get(this.index);

                if (target != null) {
                    if (!KeepSprint.getValBoolean()) {
                        mc.thePlayer.setSprinting(false);
                        mc.gameSettings.keyBindSprint.pressed = false;
                    }
                    if (mc.thePlayer.getDistanceToEntity(target) < Range.getValDouble()) {
                        boolean block = AutoBlock.getValBoolean();
                        float[] rots = getRotations(target, event);
                        event.setYaw(rots[0]);
                        event.setPitch(rots[1]);
                        if (AACCheck.getValBoolean() && (Math.round(event.getYaw()) - Math.round(RotationUtils.getRotations(target)[0]) > 3 || Math.round(event.getPitch()) - Math.round(RotationUtils.getRotations(target)[1]) > 3)) {
                            return;
                        }



                        if (timer.hasReached(randomClickDelay(minAps.getValDouble(), maxAps.getValDouble()))) {
                            if ( block) {
                                mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                            }
                            blocking = false;
                            if(Execution.instance.moduleManager.getModule(Criticals.class).isEnabled){
                                Criticals.criticals(event);
                            }
                            mc.thePlayer.swingItem();
                            mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));

                            timer.reset();
                        }
                    }
                }

            }
        } else {
            if(target != null) {
                boolean block = AutoBlock.getValBoolean();
                if(Execution.instance.moduleManager.getModule(Criticals.class).isEnabled){
                    Criticals.postCriticals();
                }
                if (block && mc.thePlayer.inventory.getCurrentItem() != null && mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword) {
                    Wrapper.getPlayer().setItemInUse(Wrapper.getPlayer().getCurrentEquippedItem(), Wrapper.getPlayer().getCurrentEquippedItem().getMaxItemUseDuration());
                    if (!blocking) {
                        mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, null, 0.0f, 0.0f, 0.0f));
                        blocking = true;
                    }
                }
            }

        }
        if (this.loaded.isEmpty() && !this.targetMode.getValString().equalsIgnoreCase("Priority")) {
            this.target = null;
        }
    }

    public static long randomClickDelay(final double minCPS, final double maxCPS) {
        return (long) ((Math.random() * (1000 / minCPS - 1000 / maxCPS + 1)) + 1000 / maxCPS);
    }

    public float[] getRotations(EntityLivingBase e, EventMotionUpdate event) {
        if (RotationsMode.getValString().equalsIgnoreCase("Normal")) {
            return RotationUtils.getRotations(e);
        } else if (RotationsMode.getValString().equalsIgnoreCase("Smooth")) {
            float[] targetYaw = RotationUtils.getRotations(e);
            float yaw = 0;
            float speed = (float) ThreadLocalRandom.current().nextDouble(1.5, 2.2);
            float yawDifference = event.getLastYaw() - targetYaw[0];
            yaw = event.getLastYaw() - (yawDifference / speed);
            float pitch = 0;
            float pitchDifference = event.getLastPitch() - targetYaw[1];
            pitch = event.getLastPitch() - (pitchDifference / speed);
            return new float[]{yaw, pitch};
        } else if (RotationsMode.getValString().equalsIgnoreCase("AAC")) {
            float[] rots = RotationUtils.getRotations(e);
            return new float[]{rots[0] + randomNumber(3, -3), rots[1] + randomNumber(3, -3)};
        }
        return null;
    }

    public EntityLivingBase getHealthPriority() {
        List<EntityLivingBase> entities = new ArrayList<>();
        for (Entity e : mc.theWorld.loadedEntityList) {
            if (e instanceof EntityLivingBase) {
                EntityLivingBase player = (EntityLivingBase) e;
                if (mc.thePlayer.getDistanceToEntity(player) < Range.getValDouble() && canAttack(player)) {
                    entities.add(player);
                }
            }
        }
        entities.sort((o1, o2) -> (int) (o1.getHealth() - o2.getHealth()));

        if (entities.isEmpty())
            return null;

        return entities.get(0);

    }


    public static int randomNumber(int max, int min) {
        return Math.round(min + (float) Math.random() * ((max - min)));
    }

    public EntityLivingBase getAnglePriority() {
        List<EntityLivingBase> entities = new ArrayList<>();
        for (Entity e : mc.theWorld.loadedEntityList) {
            if (e instanceof EntityLivingBase) {
                EntityLivingBase player = (EntityLivingBase) e;
                if (mc.thePlayer.getDistanceToEntity(player) < Range.getValDouble() && canAttack(player)) {
                    entities.add(player);
                }
            }
        }
        entities.sort((o1, o2) -> {
            float[] rot1 = RotationUtils.getRotations(o1);
            float[] rot2 = RotationUtils.getRotations(o2);
            return (int) ((mc.thePlayer.rotationYaw - rot1[0]) - (mc.thePlayer.rotationYaw - rot2[0]));
        });
        if (entities.isEmpty())
            return null;
        return entities.get(0);

    }

    private EntityLivingBase getClosest(double range) {
        double dist = range;
        EntityLivingBase target = null;
        for (Object object : mc.theWorld.loadedEntityList) {
            Entity entity = (Entity) object;
            if (entity instanceof EntityLivingBase) {
                EntityLivingBase player = (EntityLivingBase) entity;
                if (canAttack(player)) {
                    double currentDist = mc.thePlayer.getDistanceToEntity(player);
                    if (currentDist <= dist) {
                        dist = currentDist;
                        target = player;
                    }
                }
            }
        }
        return target;
    }

    public List<EntityLivingBase> getTargets() {
        List<EntityLivingBase> load = new ArrayList<>();
        for (Entity e : mc.theWorld.loadedEntityList) {
            if (e instanceof EntityLivingBase) {
                EntityLivingBase entity = (EntityLivingBase) e;
                if (canAttack(entity) && mc.thePlayer.getDistanceToEntity(entity) < this.Range.getValDouble()) {
                    load.add(entity);
                }
            }
        }
        return load;
    }

    public boolean canAttack(EntityLivingBase player) {
        if (player == mc.thePlayer)
            return false;
        if ((player instanceof EntityPlayer || player instanceof EntityAnimal || player instanceof EntityMob || player instanceof EntityVillager)) {
            if (player instanceof EntityPlayer && !Execution.instance.moduleManager.getModuleByName("Players").isEnabled)
                return false;
            if (player instanceof EntityAnimal && !Execution.instance.moduleManager.getModuleByName("Mobs").isEnabled)
                return false;
            if (player instanceof EntityMob && !Execution.instance.moduleManager.getModuleByName("Mobs").isEnabled)
                return false;
            if (player instanceof EntityVillager && !Execution.instance.moduleManager.getModuleByName("Villagers").isEnabled)
                return false;
            if (!player.isEntityAlive() && !Execution.instance.moduleManager.getModuleByName("Dead").isEnabled)
                return false;

        }
        if (player instanceof EntityPlayer) {
            if (AntiBot.isBot((EntityPlayer) player))
                return false;
        }
        if (player instanceof EntityPlayer && this.isTeam(mc.thePlayer, (EntityPlayer) player) && Execution.instance.moduleManager.getModuleByName("Teams").isEnabled)
            return false;
        if (player.isInvisible() && !Execution.instance.moduleManager.getModuleByName("Invisibles").isEnabled)
            return false;
        return true;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        timer.reset();
    }

    UnicodeFontRenderer ufr;
    UnicodeFontRenderer ufr2;

    public static boolean isTeam(final EntityPlayer e, final EntityPlayer e2) {
        if (e2.getTeam() != null && e.getTeam() != null) {
            Character target = e2.getDisplayName().getFormattedText().charAt(1);
            Character player = e.getDisplayName().getFormattedText().charAt(1);
            if (target.equals(player)) {
                return true;
            }
        } else {
            return true;
        }
        return false;
    }
    float oldHealth = 20;
    @EventTarget
    public void onRenderGui(Event2D event) {
        if (Execution.instance.getModuleManager().getModule(TargetHUD.class).isEnabled) {
            if (ufr == null) {
                ufr = UnicodeFontRenderer.getFontFromAssets("Roboto-Light", 20, Font.PLAIN, 2, 2);
            }
            if (ufr2 == null) {
                ufr2 = UnicodeFontRenderer.getFontFromAssets("Roboto-Light", 15, Font.PLAIN, 2, 2);
            }
            if (this.target instanceof EntityPlayer || this.target instanceof EntityOtherPlayerMP) {
                float width = (float) ((event.getWidth() / 2) + 100);
                float height = (float) (event.getHeight() / 2);

                EntityPlayer player = (EntityPlayer) this.target;
                Gui.drawRect(width - 70, height + 30, width + 80, height + 105, new Color(0, 0, 0, 180).getRGB());
                ufr.drawStringWithShadow(player.getName(), width - 65, height + 35, 0xFFFFFF);
                ufr2.drawStringWithShadow(player.onGround ? "On Ground" : "Off Ground", width - 65, height + 50, 0xFFFFFF);
                ufr2.drawStringWithShadow("Health: " + player.getHealth(), width - 65 + ufr2.getStringWidth("off Ground") + 13, height + 50, 0xFFFFFF);
                ufr2.drawStringWithShadow("Distance: " + mc.thePlayer.getDistanceToEntity(player), width - 65, height + 60, -1);
                ufr2.drawStringWithShadow("Fall Distance: " + player.fallDistance, width - 65, height + 70, -1);
                ufr.drawStringWithShadow(player.getHealth() > mc.thePlayer.getHealth() ? "You Might Lose" : "You Might Win", width - 65, height + 80, player.getHealth() > mc.thePlayer.getHealth() ? Color.RED.getRGB() : Color.GREEN.getRGB());
                GL11.glPushMatrix();
                GL11.glColor4f(1, 1, 1, 1);
                GlStateManager.scale(1.0f, 1.0f,1.0f);
                mc.getRenderItem().renderItemAndEffectIntoGUI(player.getCurrentEquippedItem(), (int) width + 50, (int) height + 80);
                GL11.glPopMatrix();

                float health = player.getHealth();
                float healthPercentage = (health / player.getMaxHealth());
                float targetHealthPercentage = 0;
                if (healthPercentage != lastHealth) {
                    float diff = healthPercentage - this.lastHealth;
                    targetHealthPercentage = this.lastHealth;
                    this.lastHealth += diff / 8;
                }
                Color healthcolor = Color.WHITE;
                if (healthPercentage * 100 > 75) {
                    healthcolor = Color.GREEN;
                } else if (healthPercentage * 100 > 50 && healthPercentage * 100 < 75) {
                    healthcolor = Color.YELLOW;
                } else if (healthPercentage * 100 < 50 && healthPercentage * 100 > 25) {
                    healthcolor = Color.ORANGE;
                } else if (healthPercentage * 100 < 25) {
                    healthcolor = Color.RED;
                }
                Gui.drawRect(width - 70, height + 104, width - 70 + (149 * targetHealthPercentage), height + 106, healthcolor.getRGB());
                Gui.drawRect(width - 70, height + 104, width - 70 + (149 * healthPercentage), height + 106, Color.GREEN.getRGB()    );
                GL11.glColor4f(1, 1, 1, 1);
                drawEntityOnScreen((int) width + 60, (int) height + 75, 20, Mouse.getX(), Mouse.getY(), player);
            }
        }
    }
    double delay = 0;
    boolean step  = false;
    @EventTarget
    public void on3D(Event3D event){
        if(ESP.getValBoolean()){
            if(target != null){
                //for(int i = 0; i < 5; i++){
                    drawCircle(target, event.getPartialTicks(), 0.8, delay / 100);
              //  }
            }
        }
        if(delay > 200){
            step = false;
        }
        if(delay < 0){
            step = true;
        }
        if(step){
            delay+= 3;
        }else {
            delay-= 3;
        }
    }

    public int getAlpha(int delay){
        double state = Math.ceil((System.currentTimeMillis()) / 10);
        state %= delay * 2;
        return (int) ((int) state > delay ? (delay * 2 - state) : state) > 0 ? (int) ((int) state > delay ? (delay * 2 - state) : state) : 1;
    }

    private void drawCircle(Entity entity, float partialTicks, double rad, double height) {
        glPushMatrix();
        glDisable(GL_TEXTURE_2D);
        glDisable(GL_DEPTH_TEST);
        glDepthMask(false);
        glLineWidth(2.0f);
        glBegin(GL_LINE_STRIP);

        final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks - mc.getRenderManager().viewerPosX;
        final double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks - mc.getRenderManager().viewerPosY;
        final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks - mc.getRenderManager().viewerPosZ;

        final float r = ((float) 1 / 255) * Color.WHITE.getRed();
        final float g = ((float) 1 / 255) * Color.WHITE.getGreen();
        final float b = ((float) 1 / 255) * Color.WHITE.getBlue();

        final double pix2 = Math.PI * 2.0D;

        for (int i = 0; i <= 90; ++i) {
            glVertex3d(x + rad * Math.cos(i * pix2 / 45), y + height, z + rad * Math.sin(i * pix2 / 45));
        }

        glEnd();
        glDepthMask(true);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_TEXTURE_2D);
        glPopMatrix();
    }


}
