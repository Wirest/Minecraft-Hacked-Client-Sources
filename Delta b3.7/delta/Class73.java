/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.xtrm.delta.api.command.ICommand
 *  me.xtrm.delta.api.command.ICommandListener
 */
package delta;

import me.xtrm.delta.api.command.ICommand;
import me.xtrm.delta.api.command.ICommandListener;

public class Class73
implements ICommand {
    public String getName() {
        return "crash";
    }

    public String[] getAliases() {
        String[] arrstring = new String[132 - 151 + 71 + -51];
        arrstring[122 - 209 + 98 + -11] = "fuckthisworld";
        return arrstring;
    }

    public boolean execute(ICommandListener iCommandListener, String[] arrstring) {
        Class73.mc.thePlayer.func_70107_b(Class73.mc.thePlayer.field_70165_t, Class73.mc.thePlayer.field_70163_u + 1000000.0, Class73.mc.thePlayer.field_70161_v);
        Class73.mc.thePlayer.func_70107_b(Class73.mc.thePlayer.field_70165_t, Class73.mc.thePlayer.field_70163_u + 100000.0, Class73.mc.thePlayer.field_70161_v);
        Class73.mc.thePlayer.func_70107_b(Class73.mc.thePlayer.field_70165_t, Class73.mc.thePlayer.field_70163_u + 1.0E7, Class73.mc.thePlayer.field_70161_v);
        return 37 - 49 + 33 - 33 + 13;
    }

    public String getDescription() {
        return "Niquer paladium.";
    }

    public String getHelp() {
        return "crash";
    }
}

