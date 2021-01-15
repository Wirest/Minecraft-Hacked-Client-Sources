package nivia.modules.combat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.*;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import nivia.Pandora;
import nivia.commands.Command;
import nivia.events.EventTarget;
import nivia.events.Priority;
import nivia.events.events.EventPacketSend;
import nivia.events.events.EventPostMotionUpdates;
import nivia.events.events.EventPreMotionUpdates;
import nivia.events.events.EventTick;
import nivia.managers.FriendManager;
import nivia.managers.PropertyManager.DoubleProperty;
import nivia.managers.PropertyManager.Property;
import nivia.modules.Module;
import nivia.modules.ModuleMode;
import nivia.modules.combat.aura.Multi;
import nivia.modules.combat.aura.Switch;
import nivia.modules.combat.aura.Tick;
import nivia.utils.Helper;
import nivia.utils.Logger;
import nivia.utils.Wrapper;

import java.util.ArrayList;

import static net.minecraft.init.Items.*;

public class KillAura extends Module {
    public Switch switchMode = new Switch(this);
    public Tick tickMode = new Tick(this);
    public Multi multiMode = new Multi(this);

    public Property<Boolean> autoblock = new Property<>(this, "Autoblock", true);
    public Property<Boolean> armorbreaker = new Property<>(this, "ArmorBreaker", false);
    public Property<Boolean> players = new Property<>(this, "Players", true);
    public Property<Boolean> animals = new Property<>(this, "Animals", false);
    public Property<Boolean> monsters = new Property<>(this, "Monsters", false);
    public Property<Boolean> invisibles = new Property<>(this, "Invisibles", true);
    public Property<Boolean> friends = new Property<>(this, "Friends", false);
    public Property<Boolean> noswing = new Property<>(this, "NoSwing", false);
    public Property<Boolean> lockview = new Property<>(this, "Lockview", false);
    public Property<Boolean> death = new Property<>(this, "Death", false);
    public Property<Boolean> team = new Property<Boolean>(this, "Team", false);
    public DoubleProperty ticks = new DoubleProperty(this, "Exist Ticks", 30, 0, 100);
    public DoubleProperty reach = new DoubleProperty(this, "Reach", 4.4, 1, 10, 1);
    public DoubleProperty FOV = new DoubleProperty(this, "FOV", 360, 15, 360);

    public DoubleProperty APS = new DoubleProperty(this, "APS", 12, 1, 20);
    public DoubleProperty rnd = new DoubleProperty(this, "Randomization", 4, 0 , 20);
    public DoubleProperty sdelay = new DoubleProperty(this, "Switch Delay", 10, 0, 20);

    public DoubleProperty multiDelay = new DoubleProperty(this, "Multi Delay", 500, 100, 800);
    public DoubleProperty targets = new DoubleProperty(this, "Multi Targets", 4, 1, 50);

    public Property<Sorting> sorting = new Property<>(this, "Sorting", Sorting.Crosshair);
    public Property<AuraMode> mode = new Property<>(this, "Mode", switchMode);

    public boolean nextTick = false;
    public boolean unblock = false;

    public static ArrayList<Entity> attackList = new ArrayList<>();

    public KillAura() {
        super("KillAura", 37, 13369344, Category.COMBAT, "Automatically attack entities that are within range", new String[]{"aura", "ka", "kaura", "killa", "killaura"}, true);
        addMode(tickMode, switchMode, multiMode);
    }

    @EventTarget
    public void onPre(EventPreMotionUpdates e) {
        setupTargets();
        setSuffix(mode.value.getName());
        mode.value.onPreMotion(e);
        if(mc.thePlayer.isDead && death.value){
            this.setState(false);
            Logger.logChat("Killaura disabled due to dying");
        }else{
            if (Helper.mc().getCurrentServerData().serverIP.toLowerCase().contains("hypixel.net") && mc.thePlayer.inventory.hasItem(bed) && mc.thePlayer.inventory.hasItem(comparator) && mc.thePlayer.inventory.hasItem(compass) && mc.thePlayer.inventory.hasItem(paper)) {
                this.setState(false);
                Logger.logChat("Killaura disabled due to dying");
            }
        }
    }
    @EventTarget(Priority.HIGH)
    public void onPost(EventPostMotionUpdates e) {
        mode.value.onPostMotion(e);

    }

    @EventTarget(Priority.LOWEST)
    public void onTick(EventTick e){
        mode.value.onRunTick(e);
        if(noswing.value) mc.thePlayer.swingProgressInt = 100;

    }

    @EventTarget(Priority.HIGH)
    public void onPacket(EventPacketSend e){
        mode.value.onPacketSend(e);
        if(e.getPacket() instanceof C09PacketHeldItemChange) {
            switchMode.potTimer.reset();
            switchMode.duraTimer.reset();
        }
    }
    public boolean canAttack() {
        if (!attackList.isEmpty() && !Pandora.getModManager().getModState("Freecam") && mode.value.getTarget() != null && !AutoPot.doPot && !AutoSoup.isSouping())
            return true;
        return false;
    }
    @Override
    public void onEnable(){
        super.onEnable();
        mode.value.onEnabled();
        updateProperties();
        if(team.value)
            Logger.logChat("Teaming enabled, warning its buggy! It might make aura not attack some players");

    }
    @Override
    public void onDisable(){
        super.onDisable();
        attackList.clear();
        nextTick = false;
        mode.value.onDisabled();
        Helper.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.UP));
    }
    public void doCritAttack(Entity target, boolean crit) {
        if(noswing.value) Helper.sendPacket(new C0APacketAnimation());
        else Helper.player().swingItem();
        if (crit) Criticals.doCritOverride();
        else Helper.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Wrapper.getMinecraft().thePlayer.posX, Wrapper.getMinecraft().thePlayer.posY, Wrapper.getMinecraft().thePlayer.posZ, true));
        Helper.sendPacket(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
    }
    public void doCritAttack(Entity target, boolean crit, boolean swing) {
        if(swing)
            Helper.player().swingItem();
        if (crit)
            Criticals.doCritOverride();
        else Helper.sendPacket(new C03PacketPlayer(true));
        Helper.sendPacket(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
    }
    public boolean canBlock() {
        if (Helper.player().getHeldItem() == null)
            return false;
        if(Helper.player().isBlocking() || (mc.thePlayer.isUsingItem() && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword))
            return true;
        if (Wrapper.getPlayer().getHeldItem().getItem() instanceof ItemSword && Wrapper.getMinecraft().gameSettings.keyBindUseItem.getIsKeyPressed())
            return true;
        if ((Helper.player().getHeldItem().getItem() instanceof ItemSword  && autoblock.value))
            return true;
        return false;
    }
    public void setupTargets() {
        if(Wrapper.getWorld() == null)
            return;
        Wrapper.getWorld().loadedEntityList.forEach(o -> {
            Entity e = (Entity)o;
            if(attackList.size() >= 100) return;
            if(isValidTarget(e) && !attackList.contains(e))
                attackList.add(e);
            if(!isValidTarget(e) && attackList.contains(e))
                attackList.remove(e);
            if(Helper.entityUtils().getDistanceToEntity(Wrapper.getPlayer(), e) > 15 && attackList.contains(e))
                attackList.remove(e);

        });
    }
    private boolean isValidEntity(Entity e){
        if (animals.value && e instanceof EntityAnimal || e instanceof EntityVillager)
            return true;

        if (players.value && e instanceof EntityPlayer)
            return true;

        if (monsters.value && (e instanceof EntityMob || e instanceof EntitySlime || e instanceof EntityWither || e instanceof EntityDragon))
            return true;
        return false;
    }
    public void updateProperties(){
        Pandora.getFileManager().saveFiles();
        if(mode.value.equals(switchMode)) {
            Pandora.getPropertyManager().addProperty(APS);
            Pandora.getPropertyManager().addProperty(rnd);
            Pandora.getPropertyManager().addProperty(sdelay);
        } else {
            Pandora.getPropertyManager().removeProperty(APS);
            Pandora.getPropertyManager().removeProperty(rnd);
            Pandora.getPropertyManager().removeProperty(sdelay);
        }
        if(mode.value.equals(multiMode)) {
            Pandora.getPropertyManager().addProperty(multiDelay);
            Pandora.getPropertyManager().addProperty(targets);
        } else {
            Pandora.getPropertyManager().removeProperty(multiDelay);
            Pandora.getPropertyManager().removeProperty(targets);
        }
        nextTick = false;
    }
    public void sortTargets() {
        switch (sorting.value){
            case Angle:
                attackList.sort((o1, o2) -> {
                    double rot1 = Helper.entityUtils().getRotationToEntity(o1)[0];
                    double rot2 = Helper.entityUtils().getRotationToEntity(o2)[0];
                    return (rot1 < rot2) ? 1 : (rot1 == rot2) ? 0 : -1;
                });
                break;
            case Range:
                attackList.sort((o1, o2) -> {
                    double range1 = Helper.player().getDistanceToEntity(o1);
                    double range2 = Helper.player().getDistanceToEntity(o2);
                    return (range1 < range2) ? -1 : (range1 == range2) ? 0 : 1;
                });
                break;
            case Health:
                attackList.sort((o1, o2) -> {
                    double h1 = ((EntityLivingBase) o1).getHealth();
                    double h2 = ((EntityLivingBase) o2).getHealth();
                    return (h1 < h2) ? -1 : (h1 == h2) ? 0 : 1;
                });
                break;
            case Crosshair:
                attackList.sort((o1, o2) -> {
                    double rot1 = Helper.entityUtils().getRotationToEntity(o1)[0];
                    double rot2 = Helper.entityUtils().getRotationToEntity(o2)[0];
                    double h1 = (Wrapper.getPlayer().rotationYaw - rot1) ;
                    double h2 = (Wrapper.getPlayer().rotationYaw - rot2) ;
                    return (h1 < h2) ? -1 : (h1 == h2) ? 0 : 1;
                });
                break;
        }
    }

    public static float[] getRotations(Entity entity){
        if(entity == null)
            return null;

        double diffX = entity.posX - Wrapper.getPlayer().posX;
        double diffZ = entity.posZ - Wrapper.getPlayer().posZ;
        double diffY;
        if(entity instanceof EntityLivingBase){
            EntityLivingBase elb = (EntityLivingBase) entity;
            diffY = elb.posY
                    + (elb.getEyeHeight() - 0.4)
                    - (Wrapper.getPlayer().posY + Wrapper.getPlayer()
                    .getEyeHeight());
        }else
            diffY = (entity.boundingBox.minY + entity.boundingBox.maxY)
                    / 2.0D
                    - (Wrapper.getPlayer().posY + Wrapper.getPlayer()
                    .getEyeHeight());
        double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / 3.141592653589793D) - 90.0F;
        float pitch = (float) -(Math.atan2(diffY, dist) * 180.0D / 3.141592653589793D);

        return new float[] { yaw, pitch };
    }

    private boolean isValidTarget(Entity en){

        return Helper.player().getDistanceToEntity(en) <= reach.getValue() && (en.isInvisible() ? invisibles.value : true) && en.isEntityAlive() &&
                isValidEntity(en) && (en != Helper.player()) && en.ticksExisted >= ticks.getValue() && Helper.entityUtils().isWithingFOV(en, (int)FOV.getValue()) && !isOnSameTeam(en) &&
                (FriendManager.isFriend(en.getName()) ? friends.value : true);
    }

    private boolean isOnSameTeam(Entity entity) {
        boolean team = false;

        if(this.team.value && entity instanceof EntityPlayer) {
            String n = entity.getDisplayName().getFormattedText();
            if(n.startsWith('\u00a7' + "f") && !n.equalsIgnoreCase(entity.getName()))
                team = (n.substring(0, 6).equalsIgnoreCase(mc.thePlayer.getDisplayName().getFormattedText().substring(0, 6)));
            else team = (n.substring(0,2).equalsIgnoreCase(mc.thePlayer.getDisplayName().getFormattedText().substring(0,2)));
        }

        return team;
    }

    public static boolean isAttacking() {
        return Switch.attack || Tick.attack || Multi.attack;
    }

    private enum Sorting {
        Range, Health, Angle, Crosshair;
    }
    public boolean hasPlayerNear(){
        return mc.theWorld.loadedEntityList.stream().anyMatch(e -> (e instanceof EntityPlayer) &&
                !(e instanceof EntityArmorStand) && mc.thePlayer.getDistanceToEntity((EntityLivingBase) e) < 8
                && !(e instanceof EntityPlayerSP) && !isOnSameTeam((Entity) e));
    }
    public static  KillAura getAura() {
        return (KillAura) Pandora.getModManager().getModule(KillAura.class);
    }


    public static abstract class AuraMode extends ModuleMode{
        private String name;
        protected static KillAura ka = null;
        protected final Minecraft mc = Minecraft.getMinecraft();

        public AuraMode(String name, KillAura killAura) {
            super(killAura, name);
            this.name = name;
            ka = killAura;
        }

        public String getName() {
            return name;
        }

        public Property<AuraMode> getMode() {

            return ka.mode;
        }

        public abstract void onPreMotion(EventPreMotionUpdates e);

        public abstract void onPostMotion(EventPostMotionUpdates e);

        public abstract void onPacketSend(EventPacketSend e);

        public abstract void onRunTick(EventTick e);

        public abstract EntityLivingBase getTarget();

        public abstract void onDisabled();

        public abstract void onEnabled();
    }
    protected void addCommand(){
        String[] Options = new String[]{"AutoBlock", "Animals", "Monsters", "Players", "Friends", "Death", "LockView", "Aps", "FOV", "Delay", "Switch" , "Targeting", "Reach", "ArmorBreaker", "Multi Delay", "Values"};
        Pandora.getCommandManager().cmds.add(new Command
                ("KillAura", "Manages KillAura", Logger.LogExecutionFail("Option, Options:", Options), "ka", "aura", "killa", "kaura") {
            @Override
            public void execute(String commandName, String[] arguments) {
                String message = arguments[1];
                switch(message.toLowerCase()) {
                    case "ab":
                    case "autoblock":
                        autoblock.value = !autoblock.value;
                        Logger.logToggleMessage("Autoblock", autoblock.value);
                        break;
                    case "de":
                    case "death":
                    case "dead":
                        death.value = !death.value;
                        Logger.logToggleMessage("Death", death.value);
                        break;
                    case "animals":
                    case "an":
                        animals.value = !animals.value;
                        Logger.logToggleMessage("Animals", animals.value);
                        break;
                    case "monsters":
                    case "mobs":
                        monsters.value = !monsters.value;
                        Logger.logToggleMessage("Monsters", monsters.value);
                        break;
                    case "players":
                    case "p":
                        players.value = !players.value;
                        Logger.logToggleMessage("Players", players.value);
                        break;
                    case "friends":
                    case "fr":
                    case "friend":
                        friends.value = !friends.value;
                        Logger.logToggleMessage("Friends", friends.value);
                        break;
                    case "noswing":
                    case "ns":
                    case "nswing":
                        noswing.value = !noswing.value;
                        Logger.logToggleMessage("NoSwing", noswing.value);
                        break;
                    case "switch":
                    case "s":
                    case "normal":
                    case "n":
                        mode.value = switchMode;
                        Logger.logChat(String.format("Set KillAura %sMode%s to %s%s", EnumChatFormatting.GOLD, EnumChatFormatting.GRAY, EnumChatFormatting.GOLD, mode.value.getName()));
                        break;
                    case "t":
                    case "tick":
                    case "slow":
                        mode.value = tickMode;
                        Logger.logChat(String.format("Set KillAura %sMode%s to %s%s.", EnumChatFormatting.GOLD, EnumChatFormatting.GRAY, EnumChatFormatting.GOLD, mode.value.getName()));
                        break;
                    case "m":
                    case "multi":
                        mode.value = multiMode;
                        Logger.logChat(String.format("Set KillAura %sMode%s to %s%s.", EnumChatFormatting.GOLD, EnumChatFormatting.GRAY, EnumChatFormatting.GOLD, mode.value.getName()));
                        break;
                    case "armorbreaker":
                    case "abreaker":
                    case "abreak":
                    case "armorbreak":
                    case "armor":
                    case "dura":
                        armorbreaker.value = !armorbreaker.value;
                        Logger.logToggleMessage("Armor Breaker", armorbreaker.value);
                        break;

                    case "lockview":
                    case "lv":
                        lockview.value = !lockview.value;
                        Logger.logToggleMessage("Lockview", lockview.value);
                        break;
                    case "invisibles":
                    case "invisible":
                    case "inv":
                        invisibles.value = !invisibles.value;
                        Logger.logToggleMessage("Invisibles", invisibles.value);
                        break;
                    case "speed":
                    case "aps":
                        try {
                            String message2 = arguments[2];
                            Integer aps = Integer.parseInt(message2);
                            APS.setValue(aps);
                            if(rnd.getValue() > APS.getValue()) rnd.setValue(APS.getValue() - 1);
                            Logger.logSetMessage("KillAura","APS", APS);
                        } catch (Exception e) {
                            Logger.LogExecutionFail("Value!");
                        }
                        break;
                    case "fov":
                    case "f":
                    case "angle":
                        try {
                            String message2 = arguments[2];
                            Integer FOVe = Integer.parseInt(message2);
                            FOV.setValue(FOVe);
                            Logger.logSetMessage("KillAura","FOV", FOV);
                        } catch (Exception e) {
                            Logger.LogExecutionFail("Value!");
                        }
                        break;
                    case "random":
                    case "randomization":
                    case "ran":
                    case "rnd":
                    case "rand":
                        try {
                            String message2 = arguments[2];
                            Integer rnde = Integer.parseInt(message2);
                            rnd.setValue(rnde);
                            Logger.logSetMessage("KillAura","Randomization", rnd);
                        } catch (Exception e) {
                            Logger.LogExecutionFail("Value!");
                        }
                        break;
                    case "switchdelay":
                    case "sdelay":
                    case "sd":
                        try {
                            String message2 = arguments[2];
                            Integer sD = Integer.parseInt(message2);
                            sdelay.setValue(sD);
                            Logger.logSetMessage("KillAura","Switch Delay", sdelay);
                        } catch (Exception e) {
                            Logger.LogExecutionFail("Value!");
                        }
                        break;
                    case "multidelay":
                    case "mdelay":
                    case "md":
                        try {
                            String message2 = arguments[2];
                            Integer sD = Integer.parseInt(message2);
                            multiDelay.setValue(sD);
                            Logger.logSetMessage("KillAura","Multi Delay", multiDelay);
                        } catch (Exception e) {
                            Logger.LogExecutionFail("Value!");
                        }
                        break;
                    case "delay":
                    case "ticks":
                        try {
                            String message2 = arguments[2];
                            Integer tickse = Integer.parseInt(message2);
                            ticks.setValue(tickse);
                            Logger.logSetMessage("KillAura", "Ticks Existed", ticks);
                        } catch (Exception e) {
                            Logger.LogExecutionFail("Value!");
                        }
                        break;
                    case "so":
                    case "st":
                    case "sort":
                    case "sorting":
                        String[] Modes = new String[]{"Distance", "Health", "Angle" , "Crosshair", "All"};
                        try {
                            String message2 = arguments[2];
                            switch (message2.toLowerCase()) {
                                case "Distance":
                                case "distance":
                                case "d":
                                    sorting.value = Sorting.Range;
                                    Logger.logSetMessage("KillAura", "Sorting Mode", sorting);
                                    break;
                                case "Angle":
                                case "angle":
                                case "a":
                                    sorting.value = Sorting.Angle;
                                    Logger.logSetMessage("KillAura", "Sorting Mode", sorting);
                                    break;
                                case "Health":
                                case "health":
                                case "hp":
                                    sorting.value = Sorting.Health;
                                    Logger.logSetMessage("KillAura", "Sorting Mode", sorting);
                                    break;
                                case "cross":
                                case "c":
                                case "Crosshair":
                                    sorting.value = Sorting.Crosshair;
                                    Logger.logSetMessage("KillAura", "Sorting Mode", sorting);
                                default:
                                    Logger.LogExecutionFail("Mode!", Modes);
                                    break;
                            }
                        } catch (Exception e) {
                            Logger.LogExecutionFail("Mode!", Modes);
                        }
                        break;
                    case "reach":
                    case "range":
                    case "r":
                        try {
                            String message2 = arguments[2];
                            Double nreach = Double.parseDouble(message2);
                            reach.setValue(nreach);
                            Logger.logSetMessage("KillAura", "Reach", reach);
                        } catch (Exception e) {
                            Logger.LogExecutionFail("Value!");
                        }
                        break;
                    case "values":
                    case "actual":
                        logValues();
                        break;
                    default:
                        Logger.logChat(getError());
                        break;

                }
                updateProperties();
                //AutoBlock | \2477Animals | Monsters | \2477Players | Friends | Neutral | Aimbot | Lockview | APS | \2477Delay | \2477Targeting | Reach | Multi | Values
            }});
    }
}
