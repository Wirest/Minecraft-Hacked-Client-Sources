package me.rigamortis.faurax.module.modules.combat;

import me.rigamortis.faurax.module.helpers.*;
import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.*;
import me.rigamortis.faurax.values.*;
import java.util.*;
import me.rigamortis.faurax.events.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.*;
import me.rigamortis.faurax.friends.*;
import me.rigamortis.faurax.module.modules.player.*;
import net.minecraft.entity.*;
import com.darkmagician6.eventapi.*;

public class Triggerbot extends Module implements CombatHelper
{
    private int delay;
    public static Value delayMode;
    public static Value triggerMode;
    public static Value attackDelay;
    public static Value FOV;
    public static Value mobs;
    public static Value players;
    public static Value animals;
    public static Value invisibles;
    private int[] nums;
    
    static {
        Triggerbot.delayMode = new Value("Triggerbot", String.class, "Delay Mode", "Normal", new String[] { "Normal", "Random" });
        Triggerbot.triggerMode = new Value("Triggerbot", String.class, "Trigger Mode", "BoundingBox", new String[] { "BoundingBox", "FOV" });
        Triggerbot.attackDelay = new Value("Triggerbot", Float.TYPE, "Delay", 3.0f, 0.0f, 6.0f);
        Triggerbot.FOV = new Value("Triggerbot", Float.TYPE, "FOV", 20.0f, 1.0f, 90.0f);
        Triggerbot.mobs = new Value("Triggerbot", Boolean.TYPE, "Mobs", false);
        Triggerbot.players = new Value("Triggerbot", Boolean.TYPE, "Players", true);
        Triggerbot.animals = new Value("Triggerbot", Boolean.TYPE, "Animals", false);
        Triggerbot.invisibles = new Value("Triggerbot", Boolean.TYPE, "Invisibles", false);
    }
    
    public Triggerbot() {
        this.nums = new int[] { 1, 2, 3, 4, 5, 6, 7 };
        this.setName("Triggerbot");
        this.setKey("U");
        this.setType(ModuleType.COMBAT);
        this.setColor(-2996409);
        this.setVisible(true);
        this.setModInfo("");
        Client.getValues();
        ValueManager.values.add(Triggerbot.delayMode);
        Client.getValues();
        ValueManager.values.add(Triggerbot.triggerMode);
        Client.getValues();
        ValueManager.values.add(Triggerbot.attackDelay);
        Client.getValues();
        ValueManager.values.add(Triggerbot.FOV);
        Client.getValues();
        ValueManager.values.add(Triggerbot.invisibles);
        Client.getValues();
        ValueManager.values.add(Triggerbot.players);
        Client.getValues();
        ValueManager.values.add(Triggerbot.mobs);
        Client.getValues();
        ValueManager.values.add(Triggerbot.animals);
    }
    
    public int getRandom(final int[] array) {
        final int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }
    
    @EventTarget
    public void preTick(final EventPreTick event) {
        if (this.isToggled()) {
            if (Triggerbot.delayMode.getSelectedOption().equalsIgnoreCase("Normal")) {
                if (Triggerbot.triggerMode.getSelectedOption().equalsIgnoreCase("BoundingBox")) {
                    final boolean isValidEntity = (Triggerbot.mobs.getBooleanValue() && Triggerbot.mc.objectMouseOver.entityHit instanceof EntityMob && !Triggerbot.mc.objectMouseOver.entityHit.isInvisible() && !(Triggerbot.mc.objectMouseOver.entityHit instanceof EntityAnimal) && !(Triggerbot.mc.objectMouseOver.entityHit instanceof EntityPlayer)) || (Triggerbot.animals.getBooleanValue() && Triggerbot.mc.objectMouseOver.entityHit instanceof EntityAnimal && !Triggerbot.mc.objectMouseOver.entityHit.isInvisible() && !(Triggerbot.mc.objectMouseOver.entityHit instanceof EntityMob) && !(Triggerbot.mc.objectMouseOver.entityHit instanceof EntityPlayer)) || (Triggerbot.players.getBooleanValue() && Triggerbot.mc.objectMouseOver.entityHit instanceof EntityPlayer && !Triggerbot.mc.objectMouseOver.entityHit.isInvisible() && !(Triggerbot.mc.objectMouseOver.entityHit instanceof EntityAnimal) && !(Triggerbot.mc.objectMouseOver.entityHit instanceof EntityMob) && !FriendManager.isFriend(Triggerbot.mc.objectMouseOver.entityHit.getName())) || (Triggerbot.invisibles.getBooleanValue() && Triggerbot.mc.objectMouseOver.entityHit.isInvisible() && !FriendManager.isFriend(Triggerbot.mc.objectMouseOver.entityHit.getName()));
                    if (Triggerbot.mc.objectMouseOver.entityHit != null && isValidEntity && !Regen.potting && Client.getClientHelper().shouldHitEntity(Triggerbot.mc.objectMouseOver.entityHit, 4.0, 360.0f, true, 0, Triggerbot.invisibles.getBooleanValue() ? 1 : 0, Triggerbot.players.getBooleanValue() ? 1 : 0, Triggerbot.mobs.getBooleanValue() ? 1 : 0, Triggerbot.animals.getBooleanValue() ? 1 : 0)) {
                        ++this.delay;
                        if (this.delay >= Triggerbot.attackDelay.getFloatValue()) {
                            Triggerbot.mc.thePlayer.swingItem();
                            Triggerbot.mc.playerController.attackEntity(Triggerbot.mc.thePlayer, Triggerbot.mc.objectMouseOver.entityHit);
                            this.delay = 0;
                        }
                    }
                }
                if (Triggerbot.triggerMode.getSelectedOption().equalsIgnoreCase("FOV")) {
                    final Entity entity = Client.getClientHelper().getBestEntity(4.0, Triggerbot.FOV.getFloatValue(), true, 0, Triggerbot.invisibles.getBooleanValue() ? 1 : 0, Triggerbot.players.getBooleanValue() ? 1 : 0, Triggerbot.mobs.getBooleanValue() ? 1 : 0, Triggerbot.animals.getBooleanValue() ? 1 : 0);
                    final boolean isValidEntity2 = (Triggerbot.mobs.getBooleanValue() && entity instanceof EntityMob && !entity.isInvisible() && !(entity instanceof EntityAnimal) && !(entity instanceof EntityPlayer)) || (Triggerbot.animals.getBooleanValue() && entity instanceof EntityAnimal && !entity.isInvisible() && !(entity instanceof EntityMob) && !(entity instanceof EntityPlayer)) || (Triggerbot.players.getBooleanValue() && entity instanceof EntityPlayer && !entity.isInvisible() && !(entity instanceof EntityAnimal) && !(entity instanceof EntityMob) && !FriendManager.isFriend(entity.getName())) || (Triggerbot.invisibles.getBooleanValue() && entity.isInvisible() && !FriendManager.isFriend(entity.getName()));
                    if (entity != null && !Regen.potting && isValidEntity2) {
                        ++this.delay;
                        if (this.delay >= Triggerbot.attackDelay.getFloatValue()) {
                            Triggerbot.mc.thePlayer.swingItem();
                            Triggerbot.mc.playerController.attackEntity(Triggerbot.mc.thePlayer, entity);
                            this.delay = 0;
                        }
                    }
                }
            }
            if (Triggerbot.delayMode.getSelectedOption().equalsIgnoreCase("Random")) {
                if (Triggerbot.triggerMode.getSelectedOption().equalsIgnoreCase("BoundingBox")) {
                    final boolean isValidEntity = (Triggerbot.mobs.getBooleanValue() && Triggerbot.mc.objectMouseOver.entityHit instanceof EntityMob && !Triggerbot.mc.objectMouseOver.entityHit.isInvisible() && !(Triggerbot.mc.objectMouseOver.entityHit instanceof EntityAnimal) && !(Triggerbot.mc.objectMouseOver.entityHit instanceof EntityPlayer)) || (Triggerbot.animals.getBooleanValue() && Triggerbot.mc.objectMouseOver.entityHit instanceof EntityAnimal && !Triggerbot.mc.objectMouseOver.entityHit.isInvisible() && !(Triggerbot.mc.objectMouseOver.entityHit instanceof EntityMob) && !(Triggerbot.mc.objectMouseOver.entityHit instanceof EntityPlayer)) || (Triggerbot.players.getBooleanValue() && Triggerbot.mc.objectMouseOver.entityHit instanceof EntityPlayer && !Triggerbot.mc.objectMouseOver.entityHit.isInvisible() && !(Triggerbot.mc.objectMouseOver.entityHit instanceof EntityAnimal) && !(Triggerbot.mc.objectMouseOver.entityHit instanceof EntityMob) && !FriendManager.isFriend(Triggerbot.mc.objectMouseOver.entityHit.getName())) || (Triggerbot.invisibles.getBooleanValue() && Triggerbot.mc.objectMouseOver.entityHit.isInvisible() && !FriendManager.isFriend(Triggerbot.mc.objectMouseOver.entityHit.getName()));
                    if (Triggerbot.mc.objectMouseOver.entityHit != null && isValidEntity && !Regen.potting && Client.getClientHelper().shouldHitEntity(Triggerbot.mc.objectMouseOver.entityHit, 4.0, 360.0f, true, 0, Triggerbot.invisibles.getBooleanValue() ? 1 : 0, Triggerbot.players.getBooleanValue() ? 1 : 0, Triggerbot.mobs.getBooleanValue() ? 1 : 0, Triggerbot.animals.getBooleanValue() ? 1 : 0)) {
                        ++this.delay;
                        if (this.delay >= this.getRandom(this.nums)) {
                            Triggerbot.mc.thePlayer.swingItem();
                            Triggerbot.mc.playerController.attackEntity(Triggerbot.mc.thePlayer, Triggerbot.mc.objectMouseOver.entityHit);
                            this.delay = 0;
                        }
                    }
                }
                if (Triggerbot.triggerMode.getSelectedOption().equalsIgnoreCase("FOV")) {
                    final Entity entity = Client.getClientHelper().getBestEntity(4.0, Triggerbot.FOV.getFloatValue(), true, 0, Triggerbot.invisibles.getBooleanValue() ? 1 : 0, Triggerbot.players.getBooleanValue() ? 1 : 0, Triggerbot.mobs.getBooleanValue() ? 1 : 0, Triggerbot.animals.getBooleanValue() ? 1 : 0);
                    final boolean isValidEntity2 = (Triggerbot.mobs.getBooleanValue() && entity instanceof EntityMob && !entity.isInvisible() && !(entity instanceof EntityAnimal) && !(entity instanceof EntityPlayer)) || (Triggerbot.animals.getBooleanValue() && entity instanceof EntityAnimal && !entity.isInvisible() && !(entity instanceof EntityMob) && !(entity instanceof EntityPlayer)) || (Triggerbot.players.getBooleanValue() && entity instanceof EntityPlayer && !entity.isInvisible() && !(entity instanceof EntityAnimal) && !(entity instanceof EntityMob) && !FriendManager.isFriend(entity.getName())) || (Triggerbot.invisibles.getBooleanValue() && entity.isInvisible() && !FriendManager.isFriend(entity.getName()));
                    if (entity != null && !Regen.potting && isValidEntity2) {
                        ++this.delay;
                        if (this.delay >= this.getRandom(this.nums)) {
                            Triggerbot.mc.thePlayer.swingItem();
                            Triggerbot.mc.playerController.attackEntity(Triggerbot.mc.thePlayer, entity);
                            this.delay = 0;
                        }
                    }
                }
            }
        }
    }
}
