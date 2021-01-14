package moonx.ohare.client.module.impl.ghost;

import moonx.ohare.client.event.bus.Handler;
import moonx.ohare.client.event.impl.input.ClickMouseEvent;
import moonx.ohare.client.module.Module;
import moonx.ohare.client.utils.CombatUtil;
import moonx.ohare.client.utils.value.impl.NumberValue;
import net.minecraft.entity.Entity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

import java.awt.*;

public class Reach extends Module {
    private NumberValue<Float> range = new NumberValue<>("Range", 3.1F, 3F, 5.0F, 0.01F);

    public Reach() {
        super("Reach", Category.GHOST, new Color(0xA4A29E).getRGB());
        setDescription("Increase your reach");
    }

    @Handler
    public void onUpdate(ClickMouseEvent event) {
        final Object[] objects = CombatUtil.getEntityCustom(getMc().thePlayer.rotationPitch, getMc().thePlayer.rotationYaw, range.getValue(), 0, 0.0F);
        if (objects == null) {
            return;
        }
        getMc().objectMouseOver = new MovingObjectPosition((Entity) objects[0], (Vec3) objects[1]);
        getMc().pointedEntity = (Entity)objects[0];
    }
}