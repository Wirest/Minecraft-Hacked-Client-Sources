/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.GuiTextField
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.client.resources.I18n
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.ResourceLocation
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.GL11
 */
package delta;

import delta.Class17;
import delta.Class172;
import delta.Class72;
import delta.module.modules.Xray;
import delta.utils.RenderUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class Class77
extends GuiScreen {
    private GuiTextField olive$;
    private float range$ = 0.0f;
    private GuiScreen latest$;
    private List<Class17> amongst$;
    private boolean argued$;
    private boolean notices$;
    private final ResourceLocation dollar$;
    private int rocky$ = 37 - 46 + 17 - 11 + 3;
    private List<ItemStack> cause$ = new ArrayList<ItemStack>();
    private final Class172 muscle$;
    private int chains$;
    private int banking$;
    private int upload$;

    public void func_73866_w_() {
        super.initGui();
        this.upload$ = (this.field_146294_l - this.chains$) / (97 - 112 + 56 - 15 + -24);
        this.banking$ = (this.field_146295_m - this.rocky$) / (112 - 137 + 61 - 13 + -21);
        this.field_146292_n.clear();
        Keyboard.enableRepeatEvents((boolean)(29 - 55 + 53 - 22 + -4));
        this.olive$ = new GuiTextField(this.field_146289_q, this.upload$ + (184 - 346 + 139 + 48), this.banking$ + (40 - 52 + 43 + 109), this.chains$ - (203 - 374 + 220 - 199 + 200), 107 - 197 + 197 - 174 + 81);
        this.olive$.setMaxStringLength(249 - 488 + 52 + 202);
        this.olive$.setTextColor(74 - 116 + 36 + 0x1000005);
        this.field_146292_n.add(new Class72(173 - 299 + 168 - 9 + 966, this.field_146294_l / (260 - 271 + 11 + 2) - (30 - 47 + 20 + 30), this.banking$ + this.rocky$ - (254 - 433 + 296 - 92 + -5), 131 - 240 + 52 + 107, 190 - 218 + 32 - 16 + 24, I18n.format((String)"gui.done", (Object[])new Object[83 - 91 + 53 - 24 + -21]), 56 - 78 + 61 + -38));
        this.ss1o();
    }

    protected void func_146284_a(GuiButton guiButton) {
        if (guiButton.id == 23 - 25 + 7 + 993) {
            this.field_146297_k.displayGuiScreen(this.latest$);
            this.field_146297_k.renderGlobal.loadRenderers();
        }
        if (guiButton.id == 251 - 270 + 63 - 4 + 959) {
            this.field_146297_k.displayGuiScreen(null);
            this.field_146297_k.renderGlobal.loadRenderers();
        }
    }

    public void func_146274_d() {
        super.handleMouseInput();
        int n = Mouse.getEventDWheel();
        if (n != 0 && this.swiN()) {
            int n2 = this.cause$.size() / (97 - 104 + 31 + -15) - (273 - 473 + 60 - 18 + 163) + (208 - 240 + 146 - 17 + -96);
            if (n > 0) {
                n = 176 - 189 + 61 - 37 + -10;
            }
            if (n < 0) {
                n = 35 - 38 + 5 - 2 + -1;
            }
            float f = this.range$;
            this.range$ = (float)((double)this.range$ - (double)n / (double)n2);
            if (this.range$ < 0.0f) {
                this.range$ = 0.0f;
            }
            if (this.range$ > 1.0f) {
                this.range$ = 1.0f;
            }
            if (f != this.range$) {
                this.ss1o();
            }
        }
    }

    public Class77(GuiScreen guiScreen) {
        this.amongst$ = new ArrayList<Class17>();
        this.muscle$ = new Class172("https://nkosmos.github.io/assets/blacklist_gui.png");
        this.dollar$ = new ResourceLocation("textures/gui/container/creative_inventory/tab_items.png");
        this.field_146291_p = 61 - 97 + 80 - 29 + -14;
        this.rocky$ = 88 - 124 + 93 + 79;
        this.chains$ = 23 - 39 + 12 - 9 + 208;
        this.latest$ = guiScreen;
    }

    public boolean func_73868_f() {
        return 252 - 466 + 371 - 165 + 8;
    }

    protected void func_73869_a(char c, int n) {
        if (n != 0) {
            this.olive$.setFocused(100 - 181 + 163 + -81);
        }
        if (this.olive$.isFocused() && this.olive$.textboxKeyTyped(c, n)) {
            this.ss1o();
        } else {
            super.keyTyped(c, n);
        }
    }

    public void func_73863_a(int n, int n2, float f) {
        this.func_146276_q_();
        this.OZ2U(n, n2, f);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glEnable((int)(142 - 156 + 108 + 32732));
        RenderUtils._trade(this.muscle$);
        this.func_73729_b(this.upload$, this.banking$, 110 - 178 + 61 + 7, 147 - 218 + 189 + -118, this.chains$, this.rocky$);
        int n3 = this.upload$ + (180 - 266 + 128 - 107 + 240);
        int n4 = this.banking$ + (240 - 319 + 87 + 10);
        int n5 = n4 + (80 - 118 + 77 + 73);
        this.field_146297_k.getTextureManager().bindTexture(this.dollar$);
        this.func_73729_b(n3, n4 + (int)((float)(n5 - n4 - (242 - 482 + 99 - 96 + 254)) * this.range$), 26 - 31 + 9 + 228 + (this.swiN() ? 187 - 357 + 105 + 65 : 238 - 453 + 245 - 81 + 63), 28 - 50 + 1 - 1 + 22, 33 - 43 + 23 + -1, 31 - 54 + 46 + -8);
        this.olive$.drawTextBox();
        this.func_73732_a(this.field_146289_q, "XRay Config", this.field_146294_l / (31 - 50 + 1 + 20), this.banking$ + (263 - 269 + 232 - 102 + -118), 169 - 284 + 148 + 0xFFFFDE);
        super.drawScreen(n, n2, f);
        RenderHelper.enableGUIStandardItemLighting();
        for (Class17 class17 : this.amongst$) {
            class17.zvlj(n, n2);
        }
        for (Class17 class17 : this.amongst$) {
            class17.qXNL(n, n2);
        }
        RenderHelper.disableStandardItemLighting();
    }

    protected void func_73864_a(int n, int n2, int n3) {
        this.olive$.mouseClicked(n, n2, n3);
        if (n3 == 0 || n3 == 269 - 443 + 159 - 75 + 91) {
            for (Class17 class17 : this.amongst$) {
                if (!class17.func_146116_c(this.field_146297_k, n, n2)) continue;
                int n4 = 89 - 146 + 48 + 8;
                Xray.xrayBlocks._lighter(Item.getIdFromItem((Item)class17.reviewed$.getItem()));
                if (class17.field_146124_l) {
                    Xray.xrayBlocks._jeans(Item.getIdFromItem((Item)class17.reviewed$.getItem()), n4);
                } else {
                    Xray.xrayBlocks.addBlock(Item.getIdFromItem((Item)class17.reviewed$.getItem()), n4);
                }
                this.ss1o();
                break;
            }
        }
        super.mouseClicked(n, n2, n3);
    }

    public void func_73876_c() {
        this.olive$.updateCursorCounter();
    }

    public void OZ2U(int n, int n2, float f) {
        boolean bl = Mouse.isButtonDown((int)(61 - 78 + 74 - 29 + -28));
        int n3 = this.upload$;
        int n4 = this.banking$;
        int n5 = n3 + (44 - 79 + 20 - 12 + 202);
        int n6 = n4 + (70 - 103 + 41 + 10);
        int n7 = n5 + (35 - 62 + 23 - 17 + 35);
        int n8 = n6 + (127 - 242 + 183 - 124 + 168);
        if (!this.notices$ && bl && n >= n5 && n2 >= n6 && n < n7 && n2 < n8) {
            this.argued$ = this.swiN();
        }
        if (!bl) {
            this.argued$ = 255 - 466 + 361 - 110 + -40;
        }
        this.notices$ = bl;
        if (this.argued$) {
            this.range$ = ((float)(n2 - n6) - 7.5f) / ((float)(n8 - n6) - 15.0f);
            if (this.range$ < 0.0f) {
                this.range$ = 0.0f;
            }
            if (this.range$ > 1.0f) {
                this.range$ = 1.0f;
            }
            this.ss1o();
        }
    }

    private boolean swiN() {
        return (this.cause$.size() >= 157 - 236 + 101 - 16 + 39 ? 110 - 207 + 97 + 1 : 164 - 212 + 18 - 14 + 44) != 0;
    }

    private void ss1o() {
        int n;
        int n2;
        Object object;
        this.amongst$.clear();
        this.cause$.clear();
        ArrayList<Item> arrayList = new ArrayList<Item>();
        for (Item item : Item.itemRegistry) {
            if (item == null) continue;
            arrayList.add(item);
        }
        for (int i = 139 - 143 + 57 + -53; i < arrayList.size(); ++i) {
            object = (Item)arrayList.get(i);
            if (object == null || !(object instanceof ItemBlock)) continue;
            object.getSubItems((Item)object, null, this.cause$);
        }
        Iterator<ItemStack> iterator = this.cause$.iterator();
        object = this.olive$.getText().toLowerCase();
        if (!((String)object).isEmpty()) {
            while (iterator.hasNext()) {
                ItemStack itemStack = iterator.next();
                n2 = 160 - 219 + 61 - 34 + 32;
                for (String string : itemStack.getTooltip((EntityPlayer)this.field_146297_k.thePlayer, this.field_146297_k.gameSettings.advancedItemTooltips)) {
                    if (!string.toLowerCase().contains((CharSequence)object)) continue;
                    n2 = 97 - 117 + 81 + -60;
                    break;
                }
                if (n2 != 0) continue;
                iterator.remove();
            }
        }
        if ((n2 = (int)((double)(this.range$ * (float)(n = this.cause$.size() / (82 - 159 + 43 - 11 + 54) - (88 - 143 + 75 + -15) + (181 - 227 + 168 + -121))) + 0.5)) < 0) {
            n2 = 242 - 255 + 94 + -81;
        }
        for (int i = 190 - 329 + 168 + -29; i < 132 - 212 + 59 - 55 + 81; ++i) {
            for (int j = 242 - 371 + 235 - 131 + 25; j < 174 - 342 + 146 - 70 + 101; ++j) {
                ItemStack itemStack;
                int n3 = j + (i + n2) * (124 - 175 + 52 - 47 + 55);
                if (n3 < 0 || n3 >= this.cause$.size() || (itemStack = this.cause$.get(n3)) == null) continue;
                int n4 = this.upload$ + (53 - 66 + 20 - 20 + 22) + (126 - 224 + 52 + 64) * j;
                int n5 = this.banking$ + (197 - 388 + 268 - 125 + 66) + (113 - 129 + 89 + -55) * i;
                Class17 class17 = new Class17(this.field_146297_k, 188 - 222 + 4 - 1 + 31, n4, n5, itemStack);
                class17.field_146124_l = Xray.xrayBlocks._levitra(Item.getIdFromItem((Item)itemStack.getItem()), itemStack.getItemDamage());
                this.amongst$.add(class17);
            }
        }
        if (!this.swiN()) {
            this.range$ = 0.0f;
        }
    }
}

