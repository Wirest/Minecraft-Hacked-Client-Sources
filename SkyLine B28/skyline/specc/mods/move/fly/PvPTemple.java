package skyline.specc.mods.move.fly;

import static net.minecraft.client.Mineman.thePlayer;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.MoveEvents.EventMoveRaw;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialTransparent;
import net.minecraft.client.Mineman;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.EventMotion;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.EventPlayerUpdate;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventListener;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventType;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModMode;
import skyline.specc.SkyLine;
import skyline.specc.mods.combat.KillAuraMod;
import skyline.specc.mods.move.Fly;
import skyline.specc.utils.UtilClient;

public class PvPTemple extends ModMode<Fly>
{

	public PvPTemple(Fly parent, String name) {
		super(parent, name);
	}

    @Override
    public void onEnable() {
        if (p.onGround) {
            p.setPosition(p.posX, p.posY + 1.2, p.posZ);
        }
    }

    @EventListener
    public void onMotionUpdate(EventMotion event) {
    	/* TODO: stop trying
    	 * to deobfuscate skid!
    	 *  \u002a\u002f\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0069\u0066\u0020\u0028\u006d\u0063\u002e\u0074\u0068\u0065\u0050\u006c\u0061\u0079\u0065\u0072\u002e\u0069\u0073\u004d\u006f\u0076\u0069\u006e\u0067\u0028\u0029\u0029\u0020\u007b\u000a\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u006d\u0063\u002e\u0074\u0068\u0065\u0050\u006c\u0061\u0079\u0065\u0072\u002e\u006f\u006e\u0047\u0072\u006f\u0075\u006e\u0064\u0020\u003d\u0020\u0074\u0072\u0075\u0065\u003b\u000a\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u006d\u0063\u002e\u0074\u0068\u0065\u0050\u006c\u0061\u0079\u0065\u0072\u002e\u006d\u006f\u0074\u0069\u006f\u006e\u0059\u0020\u003d\u0020\u0030\u003b\u000a\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u006d\u0063\u002e\u0074\u0068\u0065\u0050\u006c\u0061\u0079\u0065\u0072\u002e\u0066\u0061\u006c\u006c\u0044\u0069\u0073\u0074\u0061\u006e\u0063\u0065\u0020\u003d\u0020\u0030\u003b\u000a\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u007d\u000a\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u006d\u0063\u002e\u0074\u0068\u0065\u0050\u006c\u0061\u0079\u0065\u0072\u002e\u0073\u0065\u0074\u0053\u0070\u0065\u0065\u0064\u0028\u006d\u0063\u002e\u0074\u0068\u0065\u0050\u006c\u0061\u0079\u0065\u0072\u002e\u0067\u0065\u0074\u0053\u0070\u0065\u0065\u0064\u0028\u0029\u0029\u003b\u000a\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0069\u0066\u0020\u0028\u006d\u0063\u002e\u0074\u0068\u0065\u0050\u006c\u0061\u0079\u0065\u0072\u002e\u0074\u0069\u0063\u006b\u0073\u0045\u0078\u0069\u0073\u0074\u0065\u0064\u0020\u0025\u0020\u0031\u0030\u0020\u003d\u003d\u0020\u0030\u0029\u0020\u007b\u000a\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0009\u006d\u0063\u002e\u0074\u0068\u0065\u0050\u006c\u0061\u0079\u0065\u0072\u002e\u0073\u0065\u0074\u0050\u006f\u0073\u0069\u0074\u0069\u006f\u006e\u0028\u006d\u0063\u002e\u0074\u0068\u0065\u0050\u006c\u0061\u0079\u0065\u0072\u002e\u0070\u006f\u0073\u0058\u002c\u0020\u006d\u0063\u002e\u0074\u0068\u0065\u0050\u006c\u0061\u0079\u0065\u0072\u002e\u0070\u006f\u0073\u0059\u0020\u002d\u0020\u0030\u002e\u0031\u0044\u002c\u0020\u006d\u0063\u002e\u0074\u0068\u0065\u0050\u006c\u0061\u0079\u0065\u0072\u002e\u0070\u006f\u0073\u005a\u0029\u003b\u000a\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u007d\u000a\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0069\u0066\u0020\u0028\u006d\u0063\u002e\u0074\u0068\u0065\u0050\u006c\u0061\u0079\u0065\u0072\u002e\u0074\u0069\u0063\u006b\u0073\u0045\u0078\u0069\u0073\u0074\u0065\u0064\u0020\u0025\u0020\u0034\u0030\u0020\u003d\u003d\u0020\u0030\u0029\u0020\u007b\u000a\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0009\u006d\u0063\u002e\u0074\u0068\u0065\u0050\u006c\u0061\u0079\u0065\u0072\u002e\u0073\u0065\u0074\u0050\u006f\u0073\u0069\u0074\u0069\u006f\u006e\u0028\u006d\u0063\u002e\u0074\u0068\u0065\u0050\u006c\u0061\u0079\u0065\u0072\u002e\u0070\u006f\u0073\u0058\u002c\u0020\u006d\u0063\u002e\u0074\u0068\u0065\u0050\u006c\u0061\u0079\u0065\u0072\u002e\u0070\u006f\u0073\u0059\u0020\u002b\u0020\u0030\u002e\u0034\u0044\u002c\u0020\u006d\u0063\u002e\u0074\u0068\u0065\u0050\u006c\u0061\u0079\u0065\u0072\u002e\u0070\u006f\u0073\u005a\u0029\u003b\u000a\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u007d\u002f\u002a */


    }
    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        if (p.isPotionActive(Potion.moveSpeed)) {
            final int amplifier = p.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }
        return baseSpeed;
    }

}
