/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.xtrm.atlaspluginloader.core.event.Event$State
 *  me.xtrm.delta.api.event.events.move.EventMotion
 *  me.xtrm.delta.api.event.events.network.EventPacket
 *  me.xtrm.delta.api.event.events.other.EventIsNormalBlock
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.network.Packet
 */
package delta;

import me.xtrm.atlaspluginloader.core.event.Event;
import me.xtrm.delta.api.event.events.move.EventMotion;
import me.xtrm.delta.api.event.events.network.EventPacket;
import me.xtrm.delta.api.event.events.other.EventIsNormalBlock;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.Packet;

public class Class96 {
    private static float holding$;
    private static double lamps$;
    private static boolean orbit$;
    private static double baghdad$;
    private static float deluxe$;
    private static boolean ecology$;
    private static double appear$;

    public static boolean _relative(Object object) {
        EventIsNormalBlock eventIsNormalBlock = new EventIsNormalBlock();
        eventIsNormalBlock.call();
        return (!eventIsNormalBlock.isCancelled() ? 105 - 144 + 16 - 8 + 32 : 77 - 115 + 62 + -24) != 0;
    }

    public static boolean _clara(EntityPlayerSP entityPlayerSP, boolean bl) {
        if (bl) {
            baghdad$ = entityPlayerSP.field_70165_t;
            appear$ = entityPlayerSP.field_70163_u;
            lamps$ = entityPlayerSP.field_70161_v;
            holding$ = entityPlayerSP.field_70177_z;
            deluxe$ = entityPlayerSP.field_70125_A;
            orbit$ = entityPlayerSP.field_70122_E;
            EventMotion eventMotion = new EventMotion(Event.State.PRE, baghdad$, appear$, lamps$, holding$, deluxe$, orbit$);
            eventMotion.call();
            entityPlayerSP.field_70165_t = eventMotion.getX();
            entityPlayerSP.field_70163_u = eventMotion.getY();
            entityPlayerSP.field_70161_v = eventMotion.getZ();
            entityPlayerSP.field_70177_z = eventMotion.getYaw();
            entityPlayerSP.field_70125_A = eventMotion.getPitch();
            entityPlayerSP.field_70122_E = eventMotion.isOnGround();
            ecology$ = eventMotion.isSilent();
            return eventMotion.isCancelled();
        }
        EventMotion eventMotion = new EventMotion(Event.State.POST, entityPlayerSP.field_70165_t, entityPlayerSP.field_70163_u, entityPlayerSP.field_70161_v, entityPlayerSP.field_70177_z, entityPlayerSP.field_70125_A, entityPlayerSP.field_70122_E);
        if (ecology$) {
            entityPlayerSP.field_70759_as = entityPlayerSP.field_70177_z;
            entityPlayerSP.field_70761_aq = entityPlayerSP.field_70177_z;
            entityPlayerSP.field_70165_t = baghdad$;
            entityPlayerSP.field_70163_u = appear$;
            entityPlayerSP.field_70161_v = lamps$;
            entityPlayerSP.field_70177_z = holding$;
            entityPlayerSP.field_70125_A = deluxe$;
        }
        entityPlayerSP.field_70122_E = orbit$;
        eventMotion.call();
        return eventMotion.isCancelled();
    }

    public static boolean _tooth(Packet packet, boolean bl) {
        EventPacket eventPacket = new EventPacket(packet, bl ? Event.State.RECEIVE : Event.State.SEND);
        eventPacket.call();
        return eventPacket.isCancelled();
    }
}

