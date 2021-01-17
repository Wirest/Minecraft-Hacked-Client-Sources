package me.rigamortis.faurax.module.modules.combat;

import me.rigamortis.faurax.module.helpers.*;
import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.*;
import me.rigamortis.faurax.values.*;
import me.rigamortis.faurax.module.modules.player.*;
import net.minecraft.network.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.network.play.client.*;
import net.minecraft.client.entity.*;
import java.util.*;
import com.darkmagician6.eventapi.*;
import me.rigamortis.faurax.events.*;
import net.minecraft.entity.player.*;
import net.minecraft.world.*;

public class KillAura extends Module implements CombatHelper, WorldHelper, RenderHelper, PlayerHelper
{
    private int lookDelay;
    private int blockDelay;
    private boolean isBlocking;
    private float oldYaw;
    private float oldPitch;
    private boolean crit;
    private boolean lastTickCrit;
    public static Value fov;
    public static Value range;
    public static Value attackDelay;
    public static Value silent;
    public static Value rayTrace;
    public static Value autoAttack;
    public static Value ticksExisted;
    public static Value autoBlock;
    public static Value target;
    public static Value invisibles;
    public static Value players;
    public static Value mobs;
    public static Value animals;
    public static Value tpAura;
    public static Value aimType;
    public static Value aimSpeed;
    public static Value targetPriority;
    
    static {
        KillAura.fov = new Value("Aura", Float.TYPE, "FOV", 360.0f, 1.0f, 360.0f);
        KillAura.range = new Value("Aura", Float.TYPE, "Range", 4.5f, 1.0f, 6.0f);
        KillAura.attackDelay = new Value("Aura", Integer.TYPE, "AttackDelay", 2.0f, 0.0f, 10.0f);
        KillAura.silent = new Value("Aura", Boolean.TYPE, "Silent", true);
        KillAura.rayTrace = new Value("Aura", Boolean.TYPE, "RayTrace", false);
        KillAura.autoAttack = new Value("Aura", Boolean.TYPE, "AutoAttack", true);
        KillAura.ticksExisted = new Value("Aura", Integer.TYPE, "TicksExisted", 0.0f, 0.0f, 1000.0f);
        KillAura.autoBlock = new Value("Aura", Boolean.TYPE, "AutoBlock", true);
        KillAura.target = new Value("Aura", String.class, "Target", "Single", new String[] { "Single", "Multi" });
        KillAura.invisibles = new Value("Aura", Boolean.TYPE, "Invisibles", true);
        KillAura.players = new Value("Aura", Boolean.TYPE, "Players", true);
        KillAura.mobs = new Value("Aura", Boolean.TYPE, "Mobs", true);
        KillAura.animals = new Value("Aura", Boolean.TYPE, "Animals", false);
        KillAura.tpAura = new Value("Aura", Boolean.TYPE, "Teleport", false);
        KillAura.aimType = new Value("Aura", String.class, "AimType", "Instant", new String[] { "Instant", "Smooth" });
        KillAura.aimSpeed = new Value("Aura", Float.TYPE, "AimSpeed", 4.0f, 1.0f, 10.0f);
        KillAura.targetPriority = new Value("Aura", String.class, "TargetPriority", "Closest", new String[] { "Closest", "Crosshair" });
    }
    
    public KillAura() {
        this.setName("KillAura");
        this.setKey("E");
        this.setType(ModuleType.COMBAT);
        this.setColor(-2996409);
        this.setModInfo("");
        this.setVisible(true);
        Client.getValues();
        ValueManager.values.add(KillAura.silent);
        Client.getValues();
        ValueManager.values.add(KillAura.fov);
        Client.getValues();
        ValueManager.values.add(KillAura.range);
        Client.getValues();
        ValueManager.values.add(KillAura.rayTrace);
        Client.getValues();
        ValueManager.values.add(KillAura.autoAttack);
        Client.getValues();
        ValueManager.values.add(KillAura.ticksExisted);
        Client.getValues();
        ValueManager.values.add(KillAura.attackDelay);
        Client.getValues();
        ValueManager.values.add(KillAura.autoBlock);
        Client.getValues();
        ValueManager.values.add(KillAura.target);
        Client.getValues();
        ValueManager.values.add(KillAura.invisibles);
        Client.getValues();
        ValueManager.values.add(KillAura.players);
        Client.getValues();
        ValueManager.values.add(KillAura.mobs);
        Client.getValues();
        ValueManager.values.add(KillAura.animals);
        Client.getValues();
        ValueManager.values.add(KillAura.tpAura);
        Client.getValues();
        ValueManager.values.add(KillAura.aimType);
        Client.getValues();
        ValueManager.values.add(KillAura.aimSpeed);
        Client.getValues();
        ValueManager.values.add(KillAura.targetPriority);
    }
    
    @Override
    public void onEnabled() {
        super.onEnabled();
    }
    
    @Override
    public void onDisabled() {
        super.onDisabled();
        this.setModInfo("");
        this.lookDelay = 0;
    }
    
    @EventTarget(4)
    public void preTick(final EventPreTick e) {
        if (this.isToggled()) {
            if (KillAura.silent.getBooleanValue()) {
                this.oldYaw = KillAura.mc.thePlayer.rotationYaw;
                this.oldPitch = KillAura.mc.thePlayer.rotationPitch;
            }
            if (KillAura.target.getSelectedOption().equalsIgnoreCase("Single")) {
                this.setModInfo(" §7Single");
                Entity entity = null;
                if (KillAura.targetPriority.getSelectedOption().equalsIgnoreCase("Closest")) {
                    entity = Client.getClientHelper().getBestEntity(KillAura.range.getFloatValue(), KillAura.fov.getFloatValue(), KillAura.rayTrace.getBooleanValue(), KillAura.ticksExisted.getIntValue(), KillAura.invisibles.getBooleanValue() ? 1 : 0, KillAura.players.getBooleanValue() ? 1 : 0, KillAura.mobs.getBooleanValue() ? 1 : 0, KillAura.animals.getBooleanValue() ? 1 : 0);
                }
                if (KillAura.targetPriority.getSelectedOption().equalsIgnoreCase("Crosshair")) {
                    entity = Client.getClientHelper().getClosestEntityToCursor(90.0f, KillAura.range.getFloatValue(), KillAura.fov.getFloatValue(), KillAura.rayTrace.getBooleanValue(), KillAura.ticksExisted.getIntValue(), KillAura.invisibles.getBooleanValue() ? 1 : 0, KillAura.players.getBooleanValue() ? 1 : 0, KillAura.mobs.getBooleanValue() ? 1 : 0, KillAura.animals.getBooleanValue() ? 1 : 0);
                }
                if (entity != null) {
                    if (Regen.potting) {
                        return;
                    }
                    if (KillAura.aimType.getSelectedOption().equalsIgnoreCase("Instant")) {
                        Client.getClientHelper().faceEntity(entity);
                    }
                    if (KillAura.aimType.getSelectedOption().equalsIgnoreCase("Smooth")) {
                        final EntityPlayerSP thePlayer = KillAura.mc.thePlayer;
                        thePlayer.rotationPitch += Client.getClientHelper().getPitchChange(entity) / KillAura.aimSpeed.getFloatValue();
                        final EntityPlayerSP thePlayer2 = KillAura.mc.thePlayer;
                        thePlayer2.rotationYaw += Client.getClientHelper().getYawChange(entity) / KillAura.aimSpeed.getFloatValue();
                    }
                    ++this.lookDelay;
                    if (this.lookDelay >= KillAura.attackDelay.getFloatValue() && KillAura.autoAttack.getBooleanValue()) {
                        if (KillAura.tpAura.getBooleanValue()) {
                            KillAura.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(entity.posX, entity.posY + 2.0, entity.posZ, true));
                            Client.getClientHelper().attackTarget((EntityLivingBase)entity);
                            KillAura.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(KillAura.mc.thePlayer.posX, KillAura.mc.thePlayer.posY, KillAura.mc.thePlayer.posZ, true));
                        }
                        if (!KillAura.tpAura.getBooleanValue()) {
                            Client.getClientHelper().attackTarget((EntityLivingBase)entity);
                        }
                        this.lookDelay = 0;
                    }
                }
            }
            if (KillAura.target.getSelectedOption().equalsIgnoreCase("Multi")) {
                this.setModInfo(" §7Multi");
                for (final Object i : KillAura.mc.theWorld.loadedEntityList) {
                    final Entity entity2 = (Entity)i;
                    if (Client.getClientHelper().shouldHitEntity(entity2, KillAura.range.getFloatValue(), KillAura.fov.getFloatValue(), KillAura.rayTrace.getBooleanValue(), KillAura.ticksExisted.getIntValue(), KillAura.invisibles.getBooleanValue() ? 1 : 0, KillAura.players.getBooleanValue() ? 1 : 0, KillAura.mobs.getBooleanValue() ? 1 : 0, KillAura.animals.getBooleanValue() ? 1 : 0) && entity2 != null && KillAura.autoAttack.getBooleanValue()) {
                        Client.getClientHelper().attackTarget((EntityLivingBase)entity2);
                    }
                }
            }
            if (KillAura.autoBlock.getBooleanValue() && this.isBlocking && KillAura.mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword) {
                KillAura.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, Client.getClientHelper().getBlockPos(KillAura.mc.thePlayer.posX, KillAura.mc.thePlayer.posY, KillAura.mc.thePlayer.posZ), Client.getClientHelper().getEnumFacing((int)KillAura.mc.thePlayer.posX, (int)KillAura.mc.thePlayer.posY, (int)KillAura.mc.thePlayer.posZ)));
                this.isBlocking = false;
            }
            if (!KillAura.autoBlock.getBooleanValue() && KillAura.mc.thePlayer.isUsingItem() && KillAura.mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword) {
                KillAura.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, Client.getClientHelper().getBlockPos(KillAura.mc.thePlayer.posX, KillAura.mc.thePlayer.posY, KillAura.mc.thePlayer.posZ), Client.getClientHelper().getEnumFacing((int)KillAura.mc.thePlayer.posX, (int)KillAura.mc.thePlayer.posY, (int)KillAura.mc.thePlayer.posZ)));
            }
        }
    }
    
    @EventTarget(0)
    public void postTick(final EventPostTick e) {
        if (this.isToggled()) {
            if (KillAura.silent.getBooleanValue()) {
                KillAura.mc.thePlayer.rotationPitch = this.oldPitch;
                KillAura.mc.thePlayer.rotationYaw = this.oldYaw;
            }
            if (KillAura.autoBlock.getBooleanValue() && !this.isBlocking && KillAura.mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword) {
                KillAura.mc.playerController.sendUseItem(KillAura.mc.thePlayer, KillAura.mc.theWorld, KillAura.mc.thePlayer.inventory.getCurrentItem());
                this.isBlocking = true;
            }
        }
    }
    
    private void swap(final int slot, final int hotbar) {
        KillAura.mc.playerController.windowClick(KillAura.mc.thePlayer.inventoryContainer.windowId, slot, hotbar, 2, KillAura.mc.thePlayer);
    }
}
