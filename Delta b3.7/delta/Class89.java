/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.xtrm.delta.api.setting.ISetting
 *  me.xtrm.delta.api.setting.ISettingManager
 */
package delta;

import java.util.ArrayList;
import java.util.List;
import me.xtrm.delta.api.setting.ISetting;
import me.xtrm.delta.api.setting.ISettingManager;

public class Class89
implements ISettingManager {
    private List<ISetting> wires$ = new ArrayList<ISetting>();

    public List<ISetting> getSettings() {
        return this.wires$;
    }

    public void addSetting(ISetting iSetting) {
        this.wires$.add(iSetting);
    }
}

