package modification.modules.combat;

import modification.enummerates.Category;
import modification.events.EventUpdate;
import modification.extenders.Module;
import modification.extenders.Value;
import modification.interfaces.Event;
import modification.main.Modification;
import modification.utilities.RotationUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;

public final class BowAimbot
        extends Module {
    private final Value<Boolean> moveFix = new Value("Move fix", Boolean.valueOf(true), this, new String[0]);
    private final Value<Boolean> autoShoot = new Value("Auto shoot", Boolean.valueOf(true), this, new String[0]);
    private boolean rotated;

    public BowAimbot(String paramString, Category paramCategory) {
        super(paramString, paramCategory);
    }

    protected void onActivated() {
    }

    public void onEvent(Event paramEvent) {
        if ((paramEvent instanceof EventUpdate)) {
            this.tag = (((Boolean) this.autoShoot.value).booleanValue() ? "Auto shoot" : "");
            ItemStack localItemStack = MC.thePlayer.getCurrentEquippedItem();
            if ((localItemStack != null) && ((localItemStack.getItem() instanceof ItemBow))) {
                EntityLivingBase localEntityLivingBase = Modification.ENTITY_UTIL.findEntityWithFOV(100.0F, true);
                if (localEntityLivingBase == null) {
                    if (this.rotated) {
                        RotationUtil.resetRotations();
                        this.rotated = false;
                    }
                    return;
                }
                int i = MC.thePlayer.getItemInUseDuration();
                float f1 = i / 20.0F;
                f1 = (f1 * f1 + f1 * 2.0F) / 3.0F;
                if (f1 < 0.1D) {
                    return;
                }
                if (f1 > 1.0F) {
                    f1 = 1.0F;
                }
                RotationUtil.moveToRotation = ((Boolean) this.moveFix.value).booleanValue();
                float f2 = Modification.ROTATION_UTIL.updateRotation(RotationUtil.lastRotation.yaw, Modification.ROTATION_UTIL.rotationsToEntityWithBow(localEntityLivingBase)[0], 180.0F);
                float f3 = Modification.ROTATION_UTIL.updateRotation(RotationUtil.lastRotation.pitch, Modification.ROTATION_UTIL.rotationsToEntityWithBow(localEntityLivingBase)[1], 180.0F);
                RotationUtil.currentRotation = RotationUtil.fixedRotations(f2, f3);
                this.rotated = true;
                if ((((Boolean) this.autoShoot.value).booleanValue()) && (f1 == 1.0F) && (MC.thePlayer.canEntityBeSeen(localEntityLivingBase))) {
                    MC.playerController.onStoppedUsingItem(MC.thePlayer);
                }
                return;
            }
            if (this.rotated) {
                RotationUtil.resetRotations();
                this.rotated = false;
            }
        }
    }

    protected void onDeactivated() {
    }
}




