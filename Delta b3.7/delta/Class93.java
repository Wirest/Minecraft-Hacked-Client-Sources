/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  delta.OVYt$968L
 *  me.xtrm.atlaspluginloader.core.event.Event$State
 *  me.xtrm.atlaspluginloader.core.event.EventTarget
 *  me.xtrm.delta.api.event.events.move.EventMotion
 *  me.xtrm.delta.api.module.Category
 *  me.xtrm.delta.api.module.IModule
 *  me.xtrm.delta.api.module.Module
 *  me.xtrm.delta.api.setting.Setting
 */
package delta;

import delta.Class23;
import delta.OVYt;
import delta.utils.TimeHelper;
import java.util.Random;
import me.xtrm.atlaspluginloader.core.event.Event;
import me.xtrm.atlaspluginloader.core.event.EventTarget;
import me.xtrm.delta.api.event.events.move.EventMotion;
import me.xtrm.delta.api.module.Category;
import me.xtrm.delta.api.module.IModule;
import me.xtrm.delta.api.module.Module;
import me.xtrm.delta.api.setting.Setting;

public class Class93
extends Module {
    public static String basin$;
    private static String[] fraser$;
    private static Random supports$;
    private static TimeHelper violin$;

    static {
        Class93.pPgT();
        violin$ = new TimeHelper();
        supports$ = new Random();
        basin$ = OVYt.968L.FS1x((String)fraser$[11], (int)-2061188194);
    }

    public Class93() {
        super(OVYt.968L.FS1x((String)fraser$[12], (int)52073875), Category.Player);
        this.setDescription(OVYt.968L.FS1x((String)fraser$[13], (int)855883248));
        this.addSetting(new Setting(OVYt.968L.FS1x((String)fraser$[14], (int)-897297918), (IModule)this, 3000.0, 0.0, 10000.0, 177 - 236 + 123 + -63));
        this.addSetting(new Setting(OVYt.968L.FS1x((String)fraser$[15], (int)-1210894214), (IModule)this, 89 - 90 + 47 + -45));
    }

    private static void pPgT() {
        fraser$ = new String[]{"\u747c\u7460\u7460\u7464\u7467\u742e\u743b\u743b", "", "\ue953\ue94f\ue94f\ue94b\ue901\ue914\ue914", "", "\u8050", "\ubc72\ubc74\ubc73", "\u3d26\u3d07\u3d0e\u3d03\u3d1b", "\u884f\u886e\u8867\u887f\u886a\u882b\u8848\u8867\u8862\u886e\u8865\u887f\u882b\u887b\u886a\u8879\u882b\u8873\u885f\u8879\u8846\u8854\u882b\u8877\u882b\u885b\u887e\u8862\u8878\u8878\u886a\u8865\u887f\u882b\u882d\u882b\u884c\u8879\u886a\u887f\u887e\u8862\u887f\u882b\u8877\u882b\u8847\u8862\u886e\u8865\u8831\u882b", "\u834f\u8346\u8346", "\u874f\u8760\u877a\u8767\u875d\u877e\u876f\u8763", "\ucca6", "\uc3f1\uc3f8\uc3f8", "\u95c0\u95e3\u95f2\u95fe\u95fe\u95f6\u95e1", "\ubda3\ubd80\ubd91\ubd9d\ubdd0\ubd85\ubd9e\ubdd0\ubd9d\ubd95\ubd83\ubd83\ubd91\ubd97\ubd95\ubdd0\ubd94\ubd91\ubd9e\ubd83\ubdd0\ubd9c\ubd95\ubdd0\ubd93\ubd98\ubd91\ubd84", "\u5246\u5267\u526e\u5263\u527b", "\u383b\u3814\u380e\u3813\u3829\u380a\u381b\u3817"};
    }

    @EventTarget
    public void vE4X(EventMotion eventMotion) {
        if (eventMotion.getType() != Event.State.PRE) {
            return;
        }
        String string = Class23._mustang().replace(OVYt.968L.FS1x((String)fraser$[0], (int)1759671316), OVYt.968L.FS1x((String)fraser$[1], (int)-181175367)).replace(OVYt.968L.FS1x((String)fraser$[2], (int)-1347688133), OVYt.968L.FS1x((String)fraser$[3], (int)1888164934)).replace(OVYt.968L.FS1x((String)fraser$[4], (int)-1127972738), OVYt.968L.FS1x((String)fraser$[5], (int)-1274364838));
        if (!violin$.hasReached((long)this.getSetting(OVYt.968L.FS1x((String)fraser$[6], (int)1408449890)).getSliderValue())) {
            return;
        }
        violin$.setLastMS();
        String string2 = OVYt.968L.FS1x((String)fraser$[7], (int)-1352431605) + string;
        if (!basin$.equalsIgnoreCase(OVYt.968L.FS1x((String)fraser$[8], (int)1915061024))) {
            string2 = basin$;
        }
        if (this.getSetting(OVYt.968L.FS1x((String)fraser$[9], (int)-797145330)).getCheckValue()) {
            string2 = string2 + OVYt.968L.FS1x((String)fraser$[10], (int)-1034171258) + Math.min(54 - 57 + 21 + 99981, supports$.nextInt(30 - 59 + 52 - 22 + 99999) + (128 - 168 + 33 - 6 + 10013));
        }
        this.mc.thePlayer.sendChatMessage(string2);
    }
}

