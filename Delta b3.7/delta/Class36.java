/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.xtrm.atlaspluginloader.core.event.EventTarget
 *  me.xtrm.delta.api.event.events.player.EventKeyboard
 *  me.xtrm.delta.api.event.events.render.EventRender2D
 *  me.xtrm.delta.api.module.Category
 *  me.xtrm.delta.api.module.IModule
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.util.EnumChatFormatting
 */
package delta;

import delta.Class47;
import delta.Class64;
import delta.Class69;
import delta.Class88;
import delta.client.DeltaClient;
import delta.utils.Wrapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import me.xtrm.atlaspluginloader.core.event.EventTarget;
import me.xtrm.delta.api.event.events.player.EventKeyboard;
import me.xtrm.delta.api.event.events.render.EventRender2D;
import me.xtrm.delta.api.module.Category;
import me.xtrm.delta.api.module.IModule;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.EnumChatFormatting;

public class Class36
extends GuiScreen {
    public int fatty$;
    boolean bradley$;
    public Category bleeding$;
    public int monitor$;
    public int worry$;
    public int niagara$;
    public boolean thomson$;
    public int jerry$;
    public int myspace$;
    boolean webcast$;
    public int mailman$ = 226 - 377 + 319 + -168;
    public int churches$;
    public int mainland$;
    boolean humans$;
    public int kinds$;
    public int optimum$;
    public int reports$ = 151 - 292 + 269 + -128;

    @EventTarget
    public void EnBr(EventRender2D eventRender2D) {
        String string;
        Object object;
        int n;
        int n2;
        int n3;
        if (!DeltaClient.instance.managers.modulesManager.getModule("HUD").getSetting("TabGui").getCheckValue()) {
            return;
        }
        if (Wrapper.mc.gameSettings.showDebugInfo) {
            return;
        }
        String string2 = DeltaClient.instance.managers.modulesManager.getModule("HUD").getSetting("Mode").getComboValue();
        if (string2.equalsIgnoreCase("Delta")) {
            this.jerry$ = 180 - 207 + 104 - 84 + 12;
            this.optimum$ = 232 - 410 + 363 - 135 + 30;
            this.kinds$ = 51 - 63 + 32 - 7 + -2;
        } else if (string2.equalsIgnoreCase("Ancien")) {
            this.jerry$ = 268 - 497 + 374 + -140;
            this.optimum$ = 64 - 78 + 77 + -21;
            this.kinds$ = 159 - 161 + 3 - 2 + 14;
        } else {
            this.jerry$ = 259 - 307 + 248 - 180 + -17;
            this.optimum$ = 231 - 379 + 186 + -20;
            this.kinds$ = 219 - 356 + 197 + -48;
        }
        this.churches$ = 29 - 30 + 1 + 78;
        this.monitor$ = this.jerry$ + (127 - 147 + 13 - 5 + 13);
        int n4 = this.jerry$ + this.churches$ + (161 - 274 + 157 + -42);
        int n5 = 25 - 43 + 36 + -18;
        Category[] arrcategory = Category.values();
        int n6 = arrcategory.length;
        for (n3 = 230 - 239 + 223 - 85 + -129; n3 < n6; ++n3) {
            Category category = arrcategory[n3];
            if (category == Category.Gui) continue;
            ++n5;
        }
        if (this.mailman$ > n5 - (59 - 96 + 68 - 31 + 1)) {
            this.mailman$ = 36 - 37 + 33 + -32;
        }
        if (this.mailman$ < 0) {
            this.mailman$ = n5 - (59 - 61 + 29 - 26 + 0);
        }
        this.bleeding$ = Category.values()[this.mailman$];
        this.fatty$ = this.optimum$ + this.mailman$ * this.kinds$;
        if (this.worry$ == 48 - 90 + 78 + -702) {
            this.worry$ = this.fatty$;
        }
        if (this.worry$ < this.fatty$) {
            this.worry$ += 65 - 75 + 67 + -56;
        }
        if (this.worry$ > this.fatty$) {
            this.worry$ -= 56 - 67 + 38 - 27 + 1;
        }
        if (this.worry$ < this.fatty$) {
            this.worry$ += 218 - 258 + 210 - 59 + -110;
        }
        if (this.worry$ > this.fatty$) {
            this.worry$ -= 127 - 147 + 108 - 78 + -9;
        }
        int n7 = 183 - 210 + 127 + -100;
        Category[] arrcategory2 = Category.values();
        n3 = arrcategory2.length;
        for (n2 = 41 - 50 + 33 + -24; n2 < n3; ++n2) {
            Category category = arrcategory2[n2];
            if (category == Category.Gui) continue;
            Class36.func_73734_a((int)this.jerry$, (int)(this.optimum$ + n7 * this.kinds$), (int)(this.jerry$ + this.churches$), (int)(this.optimum$ + (n7 + (79 - 112 + 5 - 5 + 34)) * this.kinds$), (int)(171 - 208 + 134 + 1426063263));
            ++n7;
        }
        Class47._divided()._surfing(this.monitor$ - (114 - 209 + 17 - 10 + 89), this.worry$, this.jerry$ + this.churches$, this.worry$ + this.kinds$, 258 - 480 + 168 + -301989834, 122 - 167 + 4 + -1432219189);
        int n8 = 221 - 245 + 96 + -72;
        Object object2 = Category.values();
        n2 = ((Category[])object2).length;
        for (n = 179 - 329 + 186 - 123 + 87; n < n2; ++n) {
            object = object2[n];
            if (object == Category.Gui) continue;
            string = object.name().substring(66 - 112 + 28 + 18, 122 - 185 + 47 - 38 + 55).toUpperCase() + object.name().substring(39 - 54 + 47 + -31).toLowerCase();
            if (string2.equalsIgnoreCase("Delta")) {
                Class69.details$._college(string, this.jerry$ + (Category.values()[this.mailman$].name().equalsIgnoreCase(object.name()) ? 126 - 246 + 20 - 15 + 119 : 137 - 264 + 139 - 75 + 64), this.optimum$ + n8 * this.kinds$, 182 - 300 + 240 + 0xFFFF85);
            } else if (string2.equalsIgnoreCase("Ancien")) {
                Class69.develops$._college(string, this.jerry$ + (Category.values()[this.mailman$].name().equalsIgnoreCase(object.name()) ? 49 - 87 + 87 + -45 : 108 - 189 + 65 - 58 + 75), this.optimum$ + n8 * this.kinds$, 207 - 345 + 213 + 0xFFFFB4);
            } else {
                Class69.somalia$.drawStringWithShadow(string, this.jerry$ + (Category.values()[this.mailman$].name().equalsIgnoreCase(object.name()) ? 63 - 103 + 80 + -34 : 125 - 159 + 105 - 21 + -47), this.optimum$ + n8 * this.kinds$ + (47 - 57 + 44 + -32), 106 - 107 + 40 + 0xFFFFD8);
            }
            ++n8;
        }
        Class47._divided()._surfing(this.jerry$, this.optimum$, this.churches$ + this.jerry$, this.optimum$ + n7 * this.kinds$, 180 - 266 + 163 - 112 + -301989853, 50 - 76 + 76 + -50);
        if (this.thomson$) {
            Object object3;
            object2 = new ArrayList();
            DeltaClient.instance.managers.modulesManager.getModules().stream().filter(this::WjV5).forEach(((List)object2)::add);
            if (string2.equalsIgnoreCase("Altas")) {
                Collections.sort(object2, new Class88(this));
            } else {
                Collections.sort(object2, new Class64(this, string2));
            }
            n2 = 83 - 107 + 27 - 1 + -2;
            n = 235 - 286 + 79 + -28;
            object = object2.iterator();
            while (object.hasNext()) {
                string = (IModule)object.next();
                if (string.getCategory() != this.bleeding$) continue;
                if (string2.equalsIgnoreCase("Delta")) {
                    if (Class69.details$._commit(string.getName()) > n) {
                        n = Class69.details$._commit(string.getName());
                    }
                } else if (string2.equalsIgnoreCase("Ancien")) {
                    if (Class69.develops$._commit(string.getName()) > n) {
                        n = Class69.develops$._commit(string.getName());
                    }
                } else if (string2.equalsIgnoreCase("Altas") && Class69.somalia$.getStringWidth(string.getName()) > n) {
                    n = Class69.somalia$.getStringWidth(string.getName());
                }
                ++n2;
            }
            n += 10;
            if (this.reports$ > n2 - (46 - 79 + 46 - 15 + 3)) {
                this.reports$ = 186 - 283 + 79 + 18;
            }
            if (this.reports$ < 0) {
                this.reports$ = n2 - (90 - 127 + 42 - 23 + 19);
            }
            int n9 = this.mailman$ * this.kinds$;
            this.mainland$ = this.optimum$ + n9 + this.reports$ * this.kinds$;
            if (this.niagara$ < this.mainland$) {
                this.niagara$ += 153 - 215 + 201 - 91 + -47;
            }
            if (this.niagara$ > this.mainland$) {
                this.niagara$ -= 40 - 62 + 50 + -27;
            }
            if (this.niagara$ < this.mainland$) {
                this.niagara$ += 202 - 287 + 88 - 23 + 21;
            }
            if (this.niagara$ > this.mainland$) {
                this.niagara$ -= 248 - 267 + 92 + -72;
            }
            int n10 = 132 - 213 + 61 - 3 + 23;
            Iterator iterator = object2.iterator();
            while (iterator.hasNext()) {
                object3 = (IModule)iterator.next();
                if (object3.getCategory() != this.bleeding$) continue;
                Class36.func_73734_a((int)n4, (int)(this.optimum$ + n9 + n10 * this.kinds$), (int)(n4 + n + (67 - 99 + 3 + 30)), (int)(this.optimum$ + n9 + (n10 + (263 - 348 + 158 + -72)) * this.kinds$), (int)(154 - 266 + 193 - 140 + 1426063419));
                ++n10;
            }
            Class47._divided()._surfing(n4 + this.myspace$, this.niagara$, n4 + n + (252 - 279 + 7 + 21), this.niagara$ + this.kinds$, 154 - 297 + 68 + -301989813, 194 - 357 + 310 + -1432219377);
            Class47._divided()._surfing(n4, this.optimum$ + n9, n4 + n + (190 - 266 + 122 + -45), this.optimum$ + n9 + n10 * this.kinds$, 24 - 33 + 11 - 1 + -301989889, 226 - 312 + 160 - 139 + 65);
            int n11 = 163 - 256 + 83 + 10;
            object3 = object2.iterator();
            while (object3.hasNext()) {
                IModule iModule = (IModule)object3.next();
                if (iModule.getCategory() != this.bleeding$) continue;
                if (string2.equalsIgnoreCase("Delta")) {
                    Class69.details$._college((Object)(iModule.isEnabled() ? EnumChatFormatting.WHITE : EnumChatFormatting.GRAY) + iModule.getName(), n4 + (((IModule)object2.get(this.reports$)).getName().equalsIgnoreCase(iModule.getName()) ? 246 - 425 + 318 - 224 + 89 : 130 - 162 + 135 - 60 + -42), this.optimum$ + n9 + n11 * this.kinds$, 204 - 353 + 249 - 208 + 16777323);
                } else if (string2.equalsIgnoreCase("Ancien")) {
                    Class69.develops$._college((Object)(iModule.isEnabled() ? EnumChatFormatting.WHITE : EnumChatFormatting.GRAY) + iModule.getName(), n4 + (((IModule)object2.get(this.reports$)).getName().equalsIgnoreCase(iModule.getName()) ? 65 - 91 + 90 + -60 : 144 - 284 + 143 + -2), this.optimum$ + n9 + n11 * this.kinds$, 87 - 117 + 34 + 0xFFFFFB);
                } else {
                    Class69.somalia$.drawStringWithShadow((Object)(iModule.isEnabled() ? EnumChatFormatting.WHITE : EnumChatFormatting.GRAY) + iModule.getName(), n4 + (((IModule)object2.get(this.reports$)).getName().equalsIgnoreCase(iModule.getName()) ? 200 - 261 + 129 - 60 + -2 : 58 - 97 + 24 - 4 + 22), this.optimum$ + n9 + n11 * this.kinds$ + (54 - 78 + 69 - 27 + -16), 49 - 55 + 36 - 34 + 0x1000003);
                }
                ++n11;
            }
            if (this.bradley$) {
                this.bradley$ = 84 - 141 + 4 - 2 + 55;
                ((IModule)object2.get(this.reports$)).toggle();
            }
        }
    }

    private boolean WjV5(IModule iModule) {
        return (iModule.getCategory() == this.bleeding$ ? 137 - 215 + 82 - 32 + 29 : 123 - 173 + 35 + 15) != 0;
    }

    @EventTarget
    public void WtmD(EventKeyboard eventKeyboard) {
        if (!DeltaClient.instance.managers.modulesManager.getModule("HUD").getSetting("TabGui").getCheckValue()) {
            return;
        }
        int n = eventKeyboard.getKey();
        if (!this.thomson$) {
            if (n == 176 - 273 + 125 - 118 + 290) {
                this.mailman$ -= 99 - 159 + 101 - 49 + 9;
            }
            if (n == 49 - 69 + 32 - 22 + 218) {
                this.mailman$ += 262 - 286 + 161 + -136;
            }
            if (n == 141 - 178 + 110 + -45 || n == 170 - 244 + 161 - 45 + 163) {
                this.thomson$ = 24 - 42 + 7 - 4 + 16;
                this.niagara$ = this.optimum$ + this.mailman$ * this.kinds$ + this.reports$ * this.kinds$;
            }
        } else {
            if (n == 111 - 166 + 5 + 250) {
                this.reports$ -= 155 - 201 + 24 + 23;
            }
            if (n == 270 - 306 + 226 + 18) {
                this.reports$ += 217 - 366 + 339 - 137 + -52;
            }
            if (n == 175 - 191 + 150 + -106 || n == 239 - 281 + 9 - 8 + 246) {
                this.bradley$ = 218 - 343 + 239 + -113;
            }
            if (n == 254 - 400 + 263 + 86) {
                this.thomson$ = 146 - 266 + 143 + -23;
            }
        }
    }

    public Class36() {
        this.fatty$ = 171 - 309 + 303 + -165;
        this.mainland$ = 61 - 94 + 69 - 5 + -31;
        this.myspace$ = 87 - 173 + 30 - 23 + 79;
        this.niagara$ = 157 - 243 + 157 + -737;
        this.monitor$ = 38 - 59 + 47 + -26;
        this.worry$ = 121 - 179 + 163 - 6 + -765;
        this.thomson$ = 217 - 376 + 361 + -202;
        this.bradley$ = 251 - 433 + 340 - 16 + -142;
        this.humans$ = 186 - 222 + 31 - 2 + 7;
        this.webcast$ = 70 - 115 + 7 - 1 + 39;
    }
}

