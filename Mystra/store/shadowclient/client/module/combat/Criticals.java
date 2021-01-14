package store.shadowclient.client.module.combat;

import java.util.ArrayList;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.clickgui.settings.Setting;
import store.shadowclient.client.event.EventTarget;
import store.shadowclient.client.event.events.EventUpdate;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;
import store.shadowclient.client.utils.MathUtils;
import net.minecraft.block.BlockAir;
import net.minecraft.util.BlockPos;

public class Criticals extends Module {
	
	private float FallStack;
	
    public Criticals() {
        super("Criticals", 0, Category.COMBAT);
        
        ArrayList<String> options = new ArrayList<>();
        options.add("Hypixel");
        Shadow.instance.settingsManager.rSetting(new Setting("Criticals Mode", this, "Hypixel", options));
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        String mode = Shadow.instance.settingsManager.getSettingByName("Criticals Mode").getValString();
        if(mode.equalsIgnoreCase("Hypixel")) {
        	if (getMc().thePlayer.motionY < 0 && isBlockUnder() && getMc().thePlayer.onGround && !Shadow.instance.moduleManager.getModuleByName("Speed").isToggled() && !Shadow.instance.moduleManager.getModuleByName("Fly").isToggled() && !getMc().gameSettings.keyBindJump.isKeyDown() && getMc().thePlayer.fallDistance == 0) {
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


    @Override
    public void onEnable() {
        if (getMc().thePlayer == null || getMc().theWorld == null) return;
        FallStack = 0;
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