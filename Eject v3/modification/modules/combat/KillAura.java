package modification.modules.combat;

import com.google.common.collect.Lists;
import modification.enummerates.Category;
import modification.events.EventMoveFlying;
import modification.events.EventPostMotion;
import modification.events.EventPreMotion;
import modification.events.EventUpdate;
import modification.extenders.Module;
import modification.extenders.Value;
import modification.interfaces.Event;
import modification.main.Modification;
import modification.utilities.RotationUtil;
import modification.utilities.TimeUtil;
import modification.utilities.tojatta.angle.AngleUtility;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class KillAura
        extends Module {
    public static boolean blocking;
    public final Value<Float> range = new Value("Range", Float.valueOf(3.8F), 1.0F, 6.0F, 1, this, new String[0]);
    private final Value<Float> cps = new Value("CPS", Float.valueOf(10.0F), 1.0F, 20.0F, 0, this, new String[0]);
    private final Value<Float> rotateSpeed = new Value("Rotate speed", Float.valueOf(30.0F), 5.0F, 180.0F, 0, this, new String[0]);
    private final Value<Boolean> autoBlock = new Value("Auto block", Boolean.valueOf(true), this, new String[0]);
    private final Value<Boolean> smartBlock = new Value("Smart block", Boolean.valueOf(true), this, new String[]{"Auto block"});
    private final Value<Boolean> moveFix = new Value("Move fix", Boolean.valueOf(true), this, new String[0]);
    private final Value<Boolean> switchAura = new Value("Switch", Boolean.valueOf(true), this, new String[0]);
    private final Value<Boolean> criticals = new Value("Critical hits", Boolean.valueOf(true), this, new String[0]);
    private final TimeUtil timer = new TimeUtil();
    private final TimeUtil blockTimer = new TimeUtil();
    private final TimeUtil randomTimer = new TimeUtil();
    private final TimeUtil offsetTimer = new TimeUtil();
    public EntityLivingBase target;
    private AngleUtility angleUtility = new AngleUtility(20.0F, 100.0F, 5.0F, 50.0F);
    private boolean rotated;
    private boolean blocked;
    private boolean rotationSetter;
    private float yaw;
    private float pitch;
    private float accelYaw;
    private float offsetPitch;
    private float clicks;

    public KillAura(String paramString, Category paramCategory) {
        super(paramString, paramCategory);
    }

    protected void onActivated() {
        this.accelYaw = 0.0F;
        this.rotationSetter = true;
        RotationUtil.jumpFix = false;
        this.target = null;
        this.timer.reset();
        blocking = false;
        this.yaw = RotationUtil.lastRotation.yaw;
        this.pitch = RotationUtil.lastRotation.pitch;
        if (this.rotated) {
            RotationUtil.resetRotations();
            this.rotated = false;
        }
        if (this.blocked) {
            KeyBinding.setKeyBindState(MC.gameSettings.keyBindUseItem.getKeyCode(), false);
            this.blocked = false;
        }
    }

    public void onEvent(Event paramEvent) {
        if (((paramEvent instanceof EventPreMotion)) && (this.target != null)) {
            this.rotated = true;
        }
        ItemStack localItemStack;
        if ((paramEvent instanceof EventUpdate)) {
            if ((this.target = Modification.ENTITY_UTIL.findEntityWithFOV(((Float) this.range.value).floatValue(), true)) == null) {
                onActivated();
                return;
            }
            if (this.clicks == 0.0F) {
                this.clicks = ((Float) this.cps.value).floatValue();
            }
            this.yaw = Modification.ROTATION_UTIL.updateRotation(RotationUtil.lastRotation.yaw, (float) (Modification.ROTATION_UTIL.rotationsToEntity(this.target)[0] + RANDOM.nextGaussian() * RANDOM.nextGaussian() * 8.0D), 180.0F);
            this.pitch = Modification.ROTATION_UTIL.updateRotation(RotationUtil.lastRotation.pitch, (float) (Modification.ROTATION_UTIL.rotationsToEntity(this.target)[1] + RANDOM.nextGaussian() * RANDOM.nextGaussian() * 5.0D), 180.0F);
            RotationUtil.currentRotation = RotationUtil.fixedRotations(this.yaw, this.pitch);
            RotationUtil.moveToRotation = ((Boolean) this.moveFix.value).booleanValue();
            this.rotated = true;
            localItemStack = MC.thePlayer.getCurrentEquippedItem();
            if ((shouldBlock(localItemStack)) && (((Boolean) this.autoBlock.value).booleanValue()) && (!MC.thePlayer.isUsingItem())) {
                KeyBinding.setKeyBindState(MC.gameSettings.keyBindUseItem.getKeyCode(), true);
                this.blocked = true;
            }
        }
        if (((paramEvent instanceof EventMoveFlying)) && (this.target != null) && (((Boolean) this.moveFix.value).booleanValue()) && (RotationUtil.currentRotation != null)) {
            ((EventMoveFlying) paramEvent).yaw = RotationUtil.currentRotation.yaw;
        }
        if ((((Boolean) this.criticals.value).booleanValue() ? (paramEvent instanceof EventPostMotion) : (paramEvent instanceof EventPreMotion)) && (this.target != null) && (this.timer.reached(this.timer.convertToMs((int) this.clicks)))) {
            MC.thePlayer.swingItem();
            if ((MC.objectMouseOver != null) && (MC.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY)) {
                if (MC.thePlayer.isUsingItem()) {
                    blocking = false;
                    MC.playerController.onStoppedUsingItem(MC.thePlayer);
                }
                MC.playerController.attackEntity(MC.thePlayer, MC.objectMouseOver.entityHit);
                MC.effectRenderer.emitParticleAtEntity(MC.objectMouseOver.entityHit, EnumParticleTypes.CRIT);
                MC.effectRenderer.emitParticleAtEntity(MC.objectMouseOver.entityHit, EnumParticleTypes.CRIT_MAGIC);
            }
            this.clicks = Modification.SLIDE_UTIL.slideNormal(this.clicks, ((Float) this.cps.value).floatValue() / 2.0F, ((Float) this.cps.value).floatValue(), 1.0F, RANDOM.nextBoolean());
            this.timer.reset();
        }
        if (((paramEvent instanceof EventPostMotion)) && (this.target != null)) {
            localItemStack = MC.thePlayer.getCurrentEquippedItem();
            if ((localItemStack != null) && ((localItemStack.getItem() instanceof ItemSword))) {
                localItemStack.useItemRightClick(MC.theWorld, MC.thePlayer);
            }
        }
    }

    private boolean shouldBlock(ItemStack paramItemStack) {
        if ((((Boolean) this.smartBlock.value).booleanValue()) && (Math.abs(MathHelper.wrapAngleTo180_float(this.target.rotationYaw - (float) Math.toDegrees(Math.atan2(MC.thePlayer.posZ - this.target.posZ, MC.thePlayer.posX - this.target.posX)) - 90.0F)) < 90.0F)) {
            return false;
        }
        return (paramItemStack != null) && ((paramItemStack.getItem() instanceof ItemSword));
    }

    private List<EntityLivingBase> switchList() {
        ArrayList localArrayList = Lists.newArrayList();
        Iterator localIterator = MC.theWorld.loadedEntityList.iterator();
        while (localIterator.hasNext()) {
            Entity localEntity = (Entity) localIterator.next();
            if ((localEntity instanceof EntityLivingBase)) {
                EntityLivingBase localEntityLivingBase = (EntityLivingBase) localEntity;
                if (Modification.ENTITY_UTIL.validated(localEntityLivingBase, ((Float) this.range.value).floatValue(), true)) {
                    localArrayList.add(localEntityLivingBase);
                }
            }
        }
        return localArrayList;
    }

    protected void onDeactivated() {
        RotationUtil.resetRotations();
        blocking = false;
        RotationUtil.jumpFix = false;
        KeyBinding.setKeyBindState(MC.gameSettings.keyBindUseItem.getKeyCode(), false);
    }
}




