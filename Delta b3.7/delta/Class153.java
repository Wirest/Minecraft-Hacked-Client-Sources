/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.xtrm.delta.api.module.Category
 *  me.xtrm.delta.api.module.IModule
 */
package delta;

import delta.Class132;
import delta.Class147;
import delta.client.DeltaClient;
import delta.guis.click.ClickGUI;
import me.xtrm.delta.api.module.Category;
import me.xtrm.delta.api.module.IModule;

public class Class153
extends Class132 {
    public Category mazda$;

    @Override
    public void _surface(int n, int n2, int n3) {
        if (n3 == 0 && this._chambers(n, n2) && !this._entry(n, n2)) {
            for (Class132 class132 : this.h1bT.sprint$) {
                class132.worldcat$ = 38 - 64 + 60 - 59 + 25;
            }
            this.Jtml = 196 - 287 + 208 + -116;
        }
        super._surface(n, n2, n3);
    }

    public Class153(Category category, ClickGUI clickGUI) {
        super(category.name(), clickGUI);
        this.mazda$ = category;
        for (IModule iModule : DeltaClient.instance.managers.modulesManager.getModulesInCategory(category)) {
            this.WuRA.add(new Class147(iModule, (Class132)this));
        }
    }
}

