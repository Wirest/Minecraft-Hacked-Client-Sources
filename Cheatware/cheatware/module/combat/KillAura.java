package cheatware.module.combat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import cheatware.Cheatware;
import cheatware.event.EventTarget;
import cheatware.event.events.Event2D;
import cheatware.event.events.EventPreMotionUpdate;
import cheatware.module.Category;
import cheatware.module.Module;
import cheatware.utils.RotationUtils;
import cheatware.utils.TimerUtil;
import de.Hero.settings.Setting;
import io.netty.util.internal.ThreadLocalRandom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class KillAura extends Module {
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
    //Variables
    EntityLivingBase target;
    List<EntityLivingBase> loaded = new ArrayList<>();
    TimerUtil timer = new TimerUtil();
    private float lastHealth = 0;
    int index = 0;
    TimerUtil switchTimer = new TimerUtil();
	
    public KillAura() {
        super("KillAura", Keyboard.KEY_H, Category.COMBAT);
    }
    
    @Override
    public void setup() {
        ArrayList<String>  targetModes = new ArrayList<>();
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
        Cheatware.instance.settingsManager.rSetting(minAps = new Setting("MinAPS", this, 8, 1, 20, false));
        Cheatware.instance.settingsManager.rSetting(maxAps = new Setting("MaxAPS", this, 12, 1, 20, false));
        Cheatware.instance.settingsManager.rSetting(Range = new Setting("Range", this, 4, 0, 10, false));
        Cheatware.instance.settingsManager.rSetting(targetMode = new Setting("Target Mode", this, "Priority", targetModes));
        Cheatware.instance.settingsManager.rSetting(priority = new Setting("Target Priority", this, "Angle", priorities));
        Cheatware.instance.settingsManager.rSetting(AutoBlock = new Setting("Auto Block", this, true));
        Cheatware.instance.settingsManager.rSetting(AutoBlockMode = new Setting("Auto Block Mode", this, "Normal", AutoBlockModes));
        Cheatware.instance.settingsManager.rSetting(Rotations = new Setting("Rotations", this, true));
        Cheatware.instance.settingsManager.rSetting(RotationsMode = new Setting("Rotations Mode", this, "Normal", RotationModes));
        Cheatware.instance.settingsManager.rSetting(KeepSprint = new Setting("Keep Sprint", this, true));
        Cheatware.instance.settingsManager.rSetting(AACCheck = new Setting("ACCCheck", this, false));
    }
    
    @EventTarget
    public void onMotionEvent(EventPreMotionUpdate event){

        this.setDisplayName("KillAura " + "§7" + "[" + this.targetMode.getValString() + "]");
           if(targetMode.getValString().equalsIgnoreCase("Priority")){
                if(priority.getValString().equalsIgnoreCase("Health")){
                    target = getHealthPriority();
                }else if(priority.getValString().equalsIgnoreCase("Angle")){
                    target = getAnglePriority();
                }else {
                    target = getClosest(Range.getValDouble());
                }
                if(target != null){
                    if(!KeepSprint.getValBoolean()){
                        mc.thePlayer.setSprinting(false);
                        mc.gameSettings.keyBindSprint.pressed = false;
                    }
                    if(mc.thePlayer.getDistanceToEntity(target) < Range.getValDouble()){
                        boolean block = AutoBlock.getValBoolean();
                        float[] rots = getRotations(target, event);
                        event.setYaw(rots[0]);
                        event.setPitch(rots[1]);
                        if(AACCheck.getValBoolean() && (Math.round(event.getYaw()) - Math.round(RotationUtils.getRotations(target)[0]) > 3 || Math.round(event.getPitch()) - Math.round(RotationUtils.getRotations(target)[1]) > 3)){
                            return;
                        }


                        if (block && mc.thePlayer.inventory.getCurrentItem() != null && mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword) {
                            mc.thePlayer.setItemInUse(mc.thePlayer.getCurrentEquippedItem(), 71999);
                            if(AutoBlockMode.getValString().equalsIgnoreCase("Interact")){
                                mc.getNetHandler().addToSendQueue(new C02PacketUseEntity(target, target.getPositionVector()));
                                mc.getNetHandler().addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.INTERACT));
                            }
                        }
                        if(timer.hasTimeElapsed(randomClickDelay(minAps.getValDouble(), maxAps.getValDouble()), true)){
                            if (mc.thePlayer.isBlocking() && block) {
                                mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-0.3, -0.3092950, -0.3), EnumFacing.DOWN));
                            }
                            mc.thePlayer.swingItem();
                            mc.playerController.attackEntity(mc.thePlayer, target);
                            if ((mc.thePlayer.isBlocking() && block) || (mc.gameSettings.keyBindUseItem.isPressed() && mc.thePlayer.getCurrentEquippedItem() != null)) {
                                mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem());
                            }
                        }
                    }
                }

            }else if(targetMode.getValString().equalsIgnoreCase("Switch")){
                this.loaded = this.getTargets();
                if(this.loaded.size() == 0)
                    return;

                if(switchTimer.hasTimeElapsed(400, true)){
                    if(index < loaded.size() - 1){
                        index++;
                    }else {
                        index = 0;
                    }
                    switchTimer.reset();
                }
                this.target = this.loaded.get(this.index);

                if(target != null) {
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

                        if (block && mc.thePlayer.inventory.getCurrentItem() != null && mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword) {
                            mc.thePlayer.setItemInUse(mc.thePlayer.getCurrentEquippedItem(), 71999);
                            if (AutoBlockMode.getValString().equalsIgnoreCase("Interact")) {
                                mc.getNetHandler().addToSendQueue(new C02PacketUseEntity(target, target.getPositionVector()));
                                mc.getNetHandler().addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.INTERACT));
                            }
                        }
                        if (timer.hasTimeElapsed(randomClickDelay(minAps.getValDouble(), maxAps.getValDouble()), false)) {
                            if (mc.thePlayer.isBlocking() && block) {
                                mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-0.3, -0.3092950, -0.3), EnumFacing.DOWN));
                            }
                            mc.thePlayer.swingItem();
                            mc.playerController.attackEntity(mc.thePlayer, target);
                            if ((mc.thePlayer.isBlocking() && block) || (mc.gameSettings.keyBindUseItem.isPressed() && mc.thePlayer.getCurrentEquippedItem() != null)) {
                                mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem());
                            }
                            timer.reset();
                        }
                    }
                }

            }else {
                this.loaded = this.getTargets();
                if (this.loaded.size() == 0)
                    return;
                if (switchTimer.hasTimeElapsed(1, false)) {
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


                        if (block && mc.thePlayer.inventory.getCurrentItem() != null && mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword) {
                            mc.thePlayer.setItemInUse(mc.thePlayer.getCurrentEquippedItem(), 71999);
                            if (AutoBlockMode.getValString().equalsIgnoreCase("Interact")) {
                                mc.getNetHandler().addToSendQueue(new C02PacketUseEntity(target, target.getPositionVector()));
                                mc.getNetHandler().addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.INTERACT));
                            }
                        }
                        if (timer.hasTimeElapsed(randomClickDelay(minAps.getValDouble(), maxAps.getValDouble()), false)) {
                            if (mc.thePlayer.isBlocking() && block) {
                                mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-0.3, -0.3092950, -0.3), EnumFacing.DOWN));
                            }
                            mc.thePlayer.swingItem();
                            mc.playerController.attackEntity(mc.thePlayer, target);
                            if ((mc.thePlayer.isBlocking() && block) || (mc.gameSettings.keyBindUseItem.isPressed() && mc.thePlayer.getCurrentEquippedItem() != null)) {
                                mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem());
                            }
                            timer.reset();
                        }
                    }
               }
           }
       }
    
    public static long randomClickDelay(final double minCPS, final double maxCPS) {
        return (long) ((Math.random() * (1000 / minCPS - 1000 / maxCPS + 1)) + 1000 / maxCPS);
    }

    public float[] getRotations(EntityLivingBase e, EventPreMotionUpdate event){
        
        if(RotationsMode.getValString().equalsIgnoreCase("Smooth")){
            float[] targetYaw = RotationUtils.getRotations(e);
            float yaw = 0;
            float speed = (float) ThreadLocalRandom.current().nextDouble(1.5, 2.2);
            float yawDifference = event.getLastYaw() - targetYaw[0];
            yaw = event.getLastYaw() - (yawDifference / speed);
            float pitch = 0;
            float pitchDifference = event.getLastPitch() - targetYaw[1];
            pitch = event.getLastPitch() - (pitchDifference / speed);
            return new float[]{yaw, pitch};
        }else if(RotationsMode.getValString().equalsIgnoreCase("AAC") || RotationsMode.getValString().equalsIgnoreCase("Normal")){
            float[] rots = RotationUtils.getRotations(e);
            return new float[]{rots[0] + randomNumber(3, -3), rots[1] + randomNumber(3, -3)};
        }
        return null;
    }

    public EntityLivingBase getHealthPriority(){
        List<EntityLivingBase> entities = new ArrayList<>();
        for(Entity e : mc.theWorld.loadedEntityList){
            if(e instanceof EntityLivingBase){
                EntityLivingBase player = (EntityLivingBase) e;
                if(mc.thePlayer.getDistanceToEntity(player) < Range.getValDouble() && canAttack(player)) {
                    entities.add(player);
                }
            }
        }
        entities.sort((o1, o2) -> (int) (o1.getHealth() - o2.getHealth()));

        if(entities.isEmpty())
            return null;

        return entities.get(0);

    }


    public static int randomNumber(int max, int min) {
        return Math.round(min + (float) Math.random() * ((max - min)));
    }

    public EntityLivingBase getAnglePriority(){
        List<EntityLivingBase> entities = new ArrayList<>();
        for(Entity e : mc.theWorld.loadedEntityList){
            if(e instanceof EntityLivingBase){
                EntityLivingBase player = (EntityLivingBase) e;
                if(mc.thePlayer.getDistanceToEntity(player) < Range.getValDouble() && canAttack(player)) {
                    entities.add(player);
                }
            }
        }
            entities.sort((o1, o2) -> {
                float[] rot1 = RotationUtils.getRotations(o1);
                float[] rot2 = RotationUtils.getRotations(o2);
                return (int) ((mc.thePlayer.rotationYaw - rot1[0]) - (mc.thePlayer.rotationYaw - rot2[0]));
            });
        if(entities.isEmpty())
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

    public List<EntityLivingBase> getTargets(){
        List<EntityLivingBase> load = new ArrayList<>();
        for(Entity e : mc.theWorld.loadedEntityList){
            if(e instanceof EntityLivingBase){
                EntityLivingBase entity = (EntityLivingBase) e;
                if(canAttack(entity) && mc.thePlayer.getDistanceToEntity(entity) < this.Range.getValDouble()){
                    load.add(entity);
                }
            }
        }
        return load;
    }

    public boolean canAttack(EntityLivingBase player) {
        if(player == mc.thePlayer)
            return false;
       
        if(player instanceof EntityPlayer) {
        }
        if (mc.thePlayer.isOnSameTeam(player) && Cheatware.instance.moduleManager.getModuleByName("Teams").isToggled())
            return false;
        if (player.isInvisible() && !Cheatware.instance.moduleManager.getModuleByName("Invisibles").isToggled())
            return false;
        return true;
    }

    @Override
    public void onEnable(){
        super.onEnable();
        timer.reset();
    }

    @EventTarget
    public void onRenderGui(Event2D event){
       // if(CheatWare.instance.moduleManager.getModule(TargetHUD.class).isToggled()){
            if (this.target instanceof EntityPlayer || this.target instanceof EntityOtherPlayerMP) {
                float width = (float) ((event.getWidth() / 2) - 100);
                float height = (float) (event.getHeight() / 2);

                EntityPlayer player = (EntityPlayer) this.target;
                Gui.drawRect(width - 70, height + 30, width + 80, height + 105, new Color(0, 0, 0, 180).getRGB());
                mc.fontRendererObj.drawStringWithShadow(player.getName(), width - 65, height + 35, 0xFFFFFF);
                mc.fontRendererObj.drawStringWithShadow(player.onGround ? "On Ground" : "Off Ground", width - 65, height + 50, 0xFFFFFF);
                mc.fontRendererObj.drawStringWithShadow("Health: " + player.getHealth(), width - 65 + mc.fontRendererObj.getStringWidth("off Ground") + 13, height + 50, 0xFFFFFF);
                mc.fontRendererObj.drawStringWithShadow("Distance: " + mc.thePlayer.getDistanceToEntity(player), width - 65, height + 60, -1);
                mc.fontRendererObj.drawStringWithShadow("Fall Distance: " + player.fallDistance, width - 65, height + 70, -1);
                mc.fontRendererObj.drawStringWithShadow(player.getHealth() > mc.thePlayer.getHealth() ? "You Might Lose" : "You Might Win", width - 65, height + 80, player.getHealth() > mc.thePlayer.getHealth() ? Color.RED.getRGB() : Color.GREEN.getRGB());
                GL11.glPushMatrix();
                GL11.glColor4f(1, 1, 1, 1);
                mc.getRenderItem().renderItemAndEffectIntoGUI(player.getCurrentEquippedItem(), (int)width + 50, (int)height + 80);
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
                GL11.glColor4f(1, 1, 1, 1);
                drawEntityOnScreen((int) width + 60, (int) height + 75, 20, Mouse.getX(), Mouse.getY(), player);
            }
        //}
    }
    
    public static void drawEntityOnScreen(int posX, int posY, int scale, float mouseX, float mouseY, EntityLivingBase ent)
    {
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)posX, (float)posY, 50.0F);
        GlStateManager.scale((float)(-scale), (float)scale, (float)scale);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        float f = ent.renderYawOffset;
        float f1 = ent.rotationYaw;
        float f2 = ent.rotationPitch;
        float f3 = ent.prevRotationYawHead;
        float f4 = ent.rotationYawHead;
        GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-((float)Math.atan((double)(mouseY / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
        ent.renderYawOffset = (float)Math.atan((double)(mouseX / 40.0F)) * 20.0F;
        ent.rotationYaw = (float)Math.atan((double)(mouseX / 40.0F)) * 40.0F;
        ent.rotationPitch = -((float)Math.atan((double)(mouseY / 40.0F))) * 20.0F;
        ent.rotationYawHead = ent.rotationYaw;
        ent.prevRotationYawHead = ent.rotationYaw;
        GlStateManager.translate(0.0F, 0.0F, 0.0F);
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        rendermanager.setPlayerViewY(180.0F);
        rendermanager.setRenderShadow(false);
        rendermanager.renderEntityWithPosYaw(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
        rendermanager.setRenderShadow(true);
        ent.renderYawOffset = f;
        ent.rotationYaw = f1;
        ent.rotationPitch = f2;
        ent.prevRotationYawHead = f3;
        ent.rotationYawHead = f4;
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }


}
