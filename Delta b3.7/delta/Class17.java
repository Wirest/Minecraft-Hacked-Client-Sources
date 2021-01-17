/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  cpw.mods.fml.relauncher.Side
 *  cpw.mods.fml.relauncher.SideOnly
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.client.renderer.entity.RenderItem
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.ItemStack
 *  org.lwjgl.opengl.GL11
 */
package delta;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

@SideOnly(value=Side.CLIENT)
public class Class17
extends GuiButton {
    private boolean travels$;
    public final ItemStack reviewed$;
    private final Minecraft blame$;
    private final RenderItem elvis$ = new RenderItem();
    private final FontRenderer sticker$;
    private boolean ultimate$;

    protected void func_146119_b(Minecraft minecraft, int n, int n2) {
    }

    protected void qXNL(int n, int n2) {
        if (!this.travels$) {
            return;
        }
        GL11.glDisable((int)(228 - 327 + 256 + 32669));
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable((int)(22 - 43 + 42 + 2875));
        GL11.glDisable((int)(244 - 252 + 132 - 83 + 2888));
        List list = this.reviewed$.getTooltip((EntityPlayer)this.blame$.thePlayer, 211 - 363 + 250 - 102 + 5);
        if (!list.isEmpty()) {
            int n3;
            int n4;
            int n5 = 180 - 246 + 39 + 27;
            for (n4 = 176 - 313 + 9 - 7 + 135; n4 < list.size(); ++n4) {
                n3 = this.sticker$.getStringWidth((String)list.get(n4));
                if (n3 <= n5) continue;
                n5 = n3;
            }
            n4 = n + (203 - 349 + 80 + 78);
            n3 = n2 - (137 - 201 + 84 + -8);
            int n6 = 73 - 113 + 109 - 108 + 47;
            if (list.size() > 161 - 167 + 55 - 16 + -32) {
                n6 += 87 - 109 + 58 - 23 + -11 + (list.size() - (178 - 206 + 28 + 1)) * (209 - 264 + 15 + 50);
            }
            if (n3 + n3 + n6 + (216 - 393 + 112 - 107 + 178) > this.field_146121_g) {
                n3 = this.field_146121_g - n6 + n3 - (120 - 127 + 112 + -99);
            }
            this.field_73735_i = 300.0f;
            this.elvis$.zLevel = 300.0f;
            int n7 = 268 - 298 + 96 - 16 + -267386914;
            this.func_73733_a(n4 - (147 - 175 + 81 - 21 + -29), n3 - (105 - 146 + 29 - 16 + 32), n4 + n5 + (203 - 235 + 196 + -161), n3 - (169 - 244 + 191 + -113), n7, n7);
            this.func_73733_a(n4 - (69 - 128 + 44 + 18), n3 + n6 + (266 - 331 + 283 - 50 + -165), n4 + n5 + (209 - 413 + 2 - 2 + 207), n3 + n6 + (81 - 92 + 4 - 2 + 13), n7, n7);
            this.func_73733_a(n4 - (121 - 197 + 91 + -12), n3 - (216 - 412 + 402 - 57 + -146), n4 + n5 + (100 - 140 + 22 - 20 + 41), n3 + n6 + (161 - 231 + 60 + 13), n7, n7);
            this.func_73733_a(n4 - (183 - 304 + 88 - 34 + 71), n3 - (90 - 104 + 72 - 23 + -32), n4 - (254 - 276 + 219 - 151 + -43), n3 + n6 + (236 - 264 + 88 - 38 + -19), n7, n7);
            this.func_73733_a(n4 + n5 + (252 - 431 + 398 - 266 + 50), n3 - (125 - 240 + 111 + 7), n4 + n5 + (162 - 202 + 131 - 4 + -83), n3 + n6 + (143 - 150 + 1 - 1 + 10), n7, n7);
            int n8 = 201 - 322 + 118 + 1347420418;
            int n9 = (n8 & 208 - 252 + 19 - 18 + 16711465) >> 44 - 53 + 4 + 6 | n8 & 190 - 218 + 197 + -16777385;
            this.func_73733_a(n4 - (58 - 60 + 20 + -15), n3 - (44 - 56 + 22 + -7) + (216 - 239 + 5 + 19), n4 - (43 - 68 + 7 + 21) + (198 - 306 + 162 - 42 + -11), n3 + n6 + (213 - 253 + 250 - 64 + -143) - (34 - 38 + 6 + -1), n8, n9);
            this.func_73733_a(n4 + n5 + (271 - 460 + 175 - 33 + 49), n3 - (194 - 340 + 317 + -168) + (84 - 152 + 134 - 39 + -26), n4 + n5 + (129 - 145 + 26 + -7), n3 + n6 + (185 - 346 + 221 - 39 + -18) - (255 - 421 + 80 - 2 + 89), n8, n9);
            this.func_73733_a(n4 - (219 - 425 + 157 + 52), n3 - (47 - 64 + 36 + -16), n4 + n5 + (248 - 257 + 117 + -105), n3 - (45 - 65 + 36 - 12 + -1) + (188 - 237 + 235 - 177 + -8), n8, n8);
            this.func_73733_a(n4 - (254 - 337 + 321 + -235), n3 + n6 + (89 - 163 + 132 + -56), n4 + n5 + (34 - 48 + 16 - 3 + 4), n3 + n6 + (169 - 273 + 156 - 115 + 66), n9, n9);
            for (int i = 208 - 235 + 43 + -16; i < list.size(); ++i) {
                String string = (String)list.get(i);
                string = i == 0 ? "\u00a77" + (Object)this.reviewed$.getRarity().rarityColor + string : "\u00a77" + string;
                this.sticker$.drawStringWithShadow(string, n4, n3, 66 - 107 + 61 + -21);
                if (i == 0) {
                    n3 += 2;
                }
                n3 += 10;
            }
            this.field_73735_i = 0.0f;
            this.elvis$.zLevel = 0.0f;
        }
    }

    public Class17(Minecraft minecraft, int n, int n2, int n3, ItemStack itemStack) {
        super(n, n2, n3, 97 - 192 + 126 - 103 + 72, 260 - 446 + 20 + 166, "");
        this.field_146120_f = 274 - 412 + 41 - 21 + 134;
        this.field_146121_g = 184 - 285 + 45 + 72;
        this.field_146124_l = 77 - 139 + 125 - 90 + 27;
        this.ultimate$ = 207 - 319 + 51 + 62;
        this.field_146127_k = n;
        this.field_146128_h = n2;
        this.field_146129_i = n3;
        this.reviewed$ = itemStack;
        this.blame$ = minecraft;
        this.sticker$ = this.blame$.fontRenderer;
    }

    public boolean func_146116_c(Minecraft minecraft, int n, int n2) {
        return (this.ultimate$ && n >= this.field_146128_h && n2 >= this.field_146129_i && n < this.field_146128_h + this.field_146120_f && n2 < this.field_146129_i + this.field_146121_g ? 181 - 290 + 10 - 8 + 108 : 219 - 424 + 388 - 12 + -171) != 0;
    }

    public void zvlj(int n, int n2) {
        if (this.ultimate$) {
            this.travels$ = n >= this.field_146128_h && n2 >= this.field_146129_i && n < this.field_146128_h + this.field_146120_f && n2 < this.field_146129_i + this.field_146121_g ? 232 - 261 + 206 - 121 + -55 : 270 - 352 + 64 - 19 + 37;
            this.KHLI();
            GL11.glPushMatrix();
            GL11.glTranslatef((float)0.0f, (float)0.0f, (float)500.0f);
            if (this.field_146124_l) {
                this.func_73731_b(this.sticker$, "X", this.field_146128_h + (254 - 457 + 440 + -228), this.field_146129_i + (123 - 190 + 62 - 7 + 20), 28 - 39 + 30 - 17 + 0xFFFFFD);
            }
            if (this.travels$) {
                Gui.drawRect((int)this.field_146128_h, (int)this.field_146129_i, (int)(this.field_146128_h + this.field_146120_f), (int)(this.field_146129_i + this.field_146121_g), (int)(208 - 258 + 17 - 14 + -2130706386));
            }
            GL11.glPopMatrix();
        }
    }

    private void KHLI() {
        this.field_73735_i = 200.0f;
        this.elvis$.zLevel = 200.0f;
        this.elvis$.renderItemAndEffectIntoGUI(this.sticker$, this.blame$.renderEngine, this.reviewed$, this.field_146128_h, this.field_146129_i);
        this.elvis$.renderItemOverlayIntoGUI(this.sticker$, this.blame$.renderEngine, this.reviewed$, this.field_146128_h, this.field_146129_i);
        this.field_73735_i = 0.0f;
        this.elvis$.zLevel = 0.0f;
    }

    public void func_146118_a(int n, int n2) {
    }
}

