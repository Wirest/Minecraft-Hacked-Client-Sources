package moonx.ohare.client.module.impl.combat;

import moonx.ohare.client.Moonx;
import moonx.ohare.client.event.bus.*;
import moonx.ohare.client.event.impl.player.*;
import moonx.ohare.client.module.Module;
import moonx.ohare.client.utils.MathUtils;
import moonx.ohare.client.utils.Printer;
import moonx.ohare.client.utils.value.impl.EnumValue;
import net.minecraft.block.BlockAir;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
import org.apache.commons.lang3.*;
import org.lwjgl.input.Mouse;

import java.awt.*;

public class Criticals extends Module {
    public EnumValue<mode> Mode = new EnumValue<>("Mode",mode.HYPIXEL);

    private float FallStack;

    public Criticals() {
        super("Criticals", Category.COMBAT, new Color(0xA4A29E).getRGB());
    }

    @Handler
    public void onUpdate(UpdateEvent event) {
        setSuffix(StringUtils.capitalize(Mode.getValue().name().toLowerCase()));
        if (Mode.getValue() == mode.CUBECRAFT) {
            if (event.isPre()) {
                if (!Mouse.isButtonDown(0) && getMc().thePlayer.motionY < 0 && getMc().thePlayer.onGround && !Moonx.INSTANCE.getModuleManager().getModule("scaffold").isEnabled() && getMc().thePlayer.onGround && !Moonx.INSTANCE.getModuleManager().getModule("speed").isEnabled() && !Moonx.INSTANCE.getModuleManager().getModule("flight").isEnabled() && !getMc().gameSettings.keyBindJump.isKeyDown() && getMc().thePlayer.fallDistance == 0) {
                    event.setOnGround(false);
                    if (getMc().thePlayer.ticksExisted % 2 == 0) {
                        double value = 0.0624 + MathUtils.getRandomInRange(1E-8, 1E-7);
                        event.setY(getMc().thePlayer.posY + value);
                    } else {
                        event.setY(getMc().thePlayer.posY + MathUtils.getRandomInRange(1E-11, 1E-10));
                    }
                }
            }
        }
        if (Mode.getValue() == mode.NOGROUND) {
            if (event.isPre()) {
                event.setOnGround(false);
            }
        }
        if (Mode.getValue() == mode.HYPIXELHVH) {
            if (event.isPre()) {
                if (getMc().thePlayer.motionY < 0 && isBlockUnder() && getMc().thePlayer.onGround && !Moonx.INSTANCE.getModuleManager().getModule("speed").isEnabled() && !Moonx.INSTANCE.getModuleManager().getModule("flight").isEnabled() && !getMc().gameSettings.keyBindJump.isKeyDown() && getMc().thePlayer.fallDistance == 0) {
                    event.setOnGround(false);
                    if (FallStack >= 0 && FallStack < 0.1 && getMc().thePlayer.ticksExisted % 2 == 0) {
                        double value = 0.0624 + MathUtils.getRandomInRange(1E-8, 1E-7);
                        FallStack += value;
                        event.setY(getMc().thePlayer.posY + value);
                    } else {
                        //event.setY(getMc().thePlayer.posY + MathUtils.getRandomInRange(1E-11, 1E-10));
                        event.setY(getMc().thePlayer.posY + 1E-8);
                        if (FallStack < 0) {
                            FallStack = 0;
                            event.setOnGround(true);
                            event.setY(getMc().thePlayer.posY);
                        }
                    }
                }
                else {
                    FallStack = -1;
                }
            }
        }
    }


    @Override
    public void onEnable() {
        if (getMc().thePlayer == null || getMc().theWorld == null) return;
        FallStack = 0;
    }

    public enum mode {
        HYPIXEL, HYPIXELHVH, NCP, CUBECRAFT, AREA51, NOGROUND
    }
    private boolean isBlockUnder() {
        for (int i = (int) (getMc().thePlayer.posY - 1.0); i > 0; --i) {
            BlockPos pos = new BlockPos(getMc().thePlayer.posX, i, getMc().thePlayer.posZ);
            if (getMc().theWorld.getBlockState(pos).getBlock() instanceof BlockAir) continue;
            return true;
        }
        return false;
    }
}
