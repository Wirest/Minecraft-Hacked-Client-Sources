/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  club.minnced.discord.rpc.DiscordUser
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.ScaledResolution
 *  org.apache.logging.log4j.LogManager
 */
package delta.guis;

import club.minnced.discord.rpc.DiscordUser;
import delta.Class1;
import delta.Class163;
import delta.Class19;
import delta.Class22;
import delta.Class23;
import delta.Class32;
import delta.Class33;
import delta.Class47;
import delta.Class69;
import delta.client.DeltaClient;
import delta.utils.TimeHelper;
import delta.utils.Wrapper;
import java.awt.Color;
import java.io.IOException;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.apache.logging.log4j.LogManager;

public class BlackListErrorGui
extends Class19 {
    private Class33 bracelet$;
    private boolean males$;
    private static boolean bright$;
    private boolean genetics$;
    private TimeHelper targeted$;
    private GuiScreen workflow$;
    private boolean blacklisted;
    private static boolean congo$;
    private TimeHelper dinner$;
    private boolean clearing$;

    public void _created(int n, int n2, float f) {
        if (!this.clearing$) {
            this.targeted$.setLastMS();
        }
        if (this.genetics$) {
            congo$ = 225 - 267 + 224 + -181;
            this.field_146297_k.displayGuiScreen(this.workflow$);
            return;
        }
        ScaledResolution scaledResolution = Class22._remedy();
        this.func_146276_q_();
        Gui.drawRect((int)(23 - 45 + 3 - 3 + 21), (int)(132 - 259 + 166 + -40), (int)(scaledResolution.getScaledWidth() + (61 - 77 + 1 + 16)), (int)(scaledResolution.getScaledHeight() + (107 - 197 + 166 + -75)), (int)(122 - 210 + 126 - 46 + -16119278));
        if (this.males$) {
            int n3 = 130 - 209 + 77 - 28 + 30;
            Class69.concepts$._pharmacy("Version d\u00e9sactiv\u00e9e", scaledResolution.getScaledWidth() / (222 - 329 + 267 + -158), scaledResolution.getScaledHeight() / (130 - 218 + 158 - 77 + 15), 262 - 503 + 133 - 63 + 170);
            Class69.develops$._pharmacy("La version b3.7 de Delta Client n'est plus disponible.", scaledResolution.getScaledWidth() / (69 - 110 + 20 + 23), scaledResolution.getScaledHeight() / (192 - 240 + 58 - 58 + 56) + Class69.concepts$._rwanda() + n3 * (Class69.develops$._rwanda() + (269 - 318 + 14 - 4 + 41)), 248 - 412 + 262 - 130 + 31);
            ++n3;
            Class69.develops$._pharmacy("Votre version actuelle de Delta a \u00e9t\u00e9 desactiv\u00e9e", scaledResolution.getScaledWidth() / (54 - 68 + 25 - 1 + -8), scaledResolution.getScaledHeight() / (108 - 204 + 110 + -6) + Class69.concepts$._rwanda() + ++n3 * (Class69.develops$._rwanda() + (62 - 84 + 23 - 12 + 13)), 39 - 40 + 37 - 19 + -18);
            Class69.develops$._pharmacy("pour des raisons de stabilit\u00e9 ou de s\u00e9curit\u00e9.", scaledResolution.getScaledWidth() / (168 - 326 + 154 - 56 + 62), scaledResolution.getScaledHeight() / (82 - 88 + 76 + -62) + Class69.concepts$._rwanda() + ++n3 * (Class69.develops$._rwanda() + (56 - 67 + 54 - 16 + -25)), 143 - 171 + 71 + -44);
            ++n3;
            Class69.develops$._pharmacy("Pour plus d'informations, rendez vous sur notre", scaledResolution.getScaledWidth() / (88 - 103 + 73 - 46 + -10), scaledResolution.getScaledHeight() / (140 - 142 + 14 - 2 + -2) + Class69.concepts$._rwanda() + ++n3 * (Class69.develops$._rwanda() + (44 - 77 + 49 - 37 + 23)), 264 - 513 + 227 - 37 + 58);
            Class69.develops$._pharmacy("serveur discord publique:", scaledResolution.getScaledWidth() / (250 - 459 + 162 - 56 + 105), scaledResolution.getScaledHeight() / (50 - 58 + 2 + 14) + Class69.concepts$._rwanda() + ++n3 * (Class69.develops$._rwanda() + (111 - 114 + 34 + -29)), 199 - 345 + 308 + -163);
            ++n3;
            Class69.develops$._pharmacy(Class23._mustang(), scaledResolution.getScaledWidth() / (108 - 198 + 142 + -50), scaledResolution.getScaledHeight() / (231 - 255 + 112 - 27 + -53) + Class69.concepts$._rwanda() + ++n3 * (Class69.develops$._rwanda() + (241 - 413 + 4 - 3 + 173)), 263 - 288 + 108 - 84 + 0);
            return;
        }
        if (this.blacklisted) {
            int n4 = 71 - 141 + 5 - 1 + 66;
            Class69.concepts$._pharmacy("Vous avez \u00e9t\u00e9 blacklist\u00e9", scaledResolution.getScaledWidth() / (240 - 315 + 108 + -31), scaledResolution.getScaledHeight() / (160 - 275 + 264 - 45 + -96), 58 - 83 + 46 + -22);
            Class69.develops$._pharmacy("Votre acc\u00e8s \u00e0 Delta Client et aux services de", scaledResolution.getScaledWidth() / (244 - 351 + 1 - 1 + 109), scaledResolution.getScaledHeight() / (148 - 235 + 129 - 113 + 79) + Class69.concepts$._rwanda() + ++n4 * (Class69.develops$._rwanda() + (111 - 203 + 27 + 67)), 133 - 221 + 47 - 4 + 44);
            Class69.develops$._pharmacy("nKosmos ont \u00e9t\u00e9 restraints temporairement ou", scaledResolution.getScaledWidth() / (162 - 250 + 158 + -68), scaledResolution.getScaledHeight() / (257 - 430 + 353 + -172) + Class69.concepts$._rwanda() + ++n4 * (Class69.develops$._rwanda() + (208 - 396 + 4 - 2 + 188)), 256 - 288 + 81 + -50);
            Class69.develops$._pharmacy("d\u00e9finitivement par l'administration.", scaledResolution.getScaledWidth() / (75 - 121 + 49 - 30 + 29), scaledResolution.getScaledHeight() / (205 - 358 + 198 + -37) + Class69.concepts$._rwanda() + ++n4 * (Class69.develops$._rwanda() + (228 - 368 + 255 - 180 + 67)), 39 - 47 + 1 + 6);
            ++n4;
            Class69.develops$._pharmacy("Pour plus d'informations, rendez vous sur notre", scaledResolution.getScaledWidth() / (227 - 237 + 227 - 188 + -27), scaledResolution.getScaledHeight() / (247 - 264 + 44 - 6 + -13) + Class69.concepts$._rwanda() + ++n4 * (Class69.develops$._rwanda() + (89 - 121 + 113 - 56 + -23)), 215 - 381 + 157 + 8);
            Class69.develops$._pharmacy("serveur discord publique:", scaledResolution.getScaledWidth() / (54 - 87 + 73 + -38), scaledResolution.getScaledHeight() / (32 - 51 + 40 + -13) + Class69.concepts$._rwanda() + ++n4 * (Class69.develops$._rwanda() + (172 - 263 + 223 - 206 + 76)), 107 - 160 + 2 + 50);
            Class69.develops$._pharmacy(Class23._mustang(), scaledResolution.getScaledWidth() / (87 - 131 + 69 - 16 + -7), scaledResolution.getScaledHeight() / (108 - 209 + 160 - 7 + -44) + Class69.concepts$._rwanda() + ++n4 * (Class69.develops$._rwanda() + (63 - 107 + 1 - 1 + 46)), 75 - 122 + 39 + 7);
            return;
        }
        double d = this.targeted$.subMS() / (32L - 49L + 28L - 18L + 17L);
        int n5 = (int)Math.round(d / 100.0);
        int n6 = (double)n5 % 1.2 == 0.0 ? 165 - 175 + 174 + -163 : 249 - 408 + 229 + -70;
        double d2 = d - (double)(n5 * (247 - 306 + 123 - 91 + 127));
        if (n6 == 0) {
            d2 = 100.0 - d2;
        }
        d2 = Math.abs(d2);
        int n7 = (int)(d2 / 150.0 * 255.0);
        n7 = Math.max(150 - 214 + 104 - 6 + 116, Math.min(n7, 139 - 175 + 91 + 200));
        n7 = (int)((float)n7 + this.bracelet$._warnings());
        n7 = Math.max(113 - 221 + 74 + 34, Math.min(120 - 192 + 52 - 31 + 306, n7));
        Color color = new Color(216 - 376 + 348 - 125 + 192, 58 - 89 + 13 + 273, 142 - 165 + 141 - 111 + 248, n7);
        Color color2 = new Color(273 - 522 + 481 + 23, 110 - 183 + 64 + 264, 246 - 386 + 80 - 62 + 377, Math.max(189 - 339 + 317 + -147, n7));
        Class69.eagles$._pharmacy("Loading Delta...", scaledResolution.getScaledWidth() / (173 - 205 + 164 - 85 + -45), scaledResolution.getScaledHeight() / (271 - 526 + 454 + -195), color2.getRGB());
        int n8 = 149 - 275 + 95 + 371;
        int n9 = 84 - 118 + 51 - 8 + 16;
        Gui.drawRect((int)(scaledResolution.getScaledWidth() / (170 - 295 + 184 - 102 + 45) - n8 / (80 - 148 + 128 + -58)), (int)(scaledResolution.getScaledHeight() / (60 - 84 + 68 + -42)), (int)(scaledResolution.getScaledWidth() / (100 - 185 + 43 - 16 + 60) + n8 / (42 - 63 + 54 + -31)), (int)(scaledResolution.getScaledHeight() / (104 - 123 + 51 + -30) + n9), (int)color.getRGB());
        Gui.drawRect((int)(scaledResolution.getScaledWidth() / (112 - 122 + 56 - 55 + 11) - (n8 / (75 - 125 + 101 + -49) - (250 - 283 + 39 - 27 + 22))), (int)(scaledResolution.getScaledHeight() / (204 - 253 + 1 - 1 + 51) + (193 - 258 + 205 - 132 + -7)), (int)(scaledResolution.getScaledWidth() / (170 - 286 + 225 + -107) + (n8 / (219 - 249 + 34 + -2) - (54 - 76 + 3 - 2 + 22))), (int)(scaledResolution.getScaledHeight() / (162 - 282 + 66 - 6 + 62) + (n9 - (155 - 301 + 20 - 8 + 135))), (int)(228 - 455 + 419 + -16119478));
        double d3 = (double)this.targeted$.subMS() / 4000.0;
        if (d3 > 1.0) {
            d3 = 1.0;
            if (this.targeted$.hasReached(52L - 55L + 41L - 39L + 5001L)) {
                if (this.bracelet$._payday(-255.0f, 0.0f, 4.0)) {
                    this.genetics$ = 126 - 246 + 34 - 28 + 115;
                }
            } else {
                this.bracelet$ = new Class33(0.0f, 0.0f);
            }
        }
        Class47._divided()._paris(scaledResolution.getScaledWidth() / (236 - 297 + 155 + -92) - (n8 / (23 - 44 + 4 - 1 + 20) - (128 - 204 + 37 + 41)), scaledResolution.getScaledHeight() / (27 - 46 + 46 + -25) + (136 - 138 + 34 - 20 + -10), (double)(scaledResolution.getScaledWidth() / (220 - 301 + 156 + -73) - (n8 / (198 - 207 + 94 - 80 + -3) - (206 - 394 + 308 + -118))) + (double)(n8 - (83 - 95 + 29 + -13)) * d3, scaledResolution.getScaledHeight() / (106 - 133 + 71 + -42) + (n9 - (189 - 314 + 308 + -181)), color.getRGB());
        super.func_73863_a(n, n2, f);
    }

    private void _mobile(String string) throws IOException {
        if (!Class1.asylum$) {
            this.clearing$ = 107 - 118 + 41 + -29;
            return;
        }
        Class163 class163 = new Class163(string);
        if (class163 == null) {
            Wrapper._occurs();
        }
        if (class163._probe()) {
            this.blacklisted = 129 - 244 + 92 - 67 + 91;
            Class163._waiting();
        }
        this.clearing$ = 125 - 128 + 36 - 2 + -30;
    }

    public BlackListErrorGui(GuiScreen guiScreen) {
        this.workflow$ = guiScreen;
    }

    @Override
    public void func_73866_w_() {
        if (congo$) {
            this.field_146297_k.displayGuiScreen(this.workflow$);
            return;
        }
        super.func_73866_w_();
        if (bright$) {
            return;
        }
        bright$ = 72 - 85 + 45 + -31;
        this.males$ = Class32._manor();
        this.dinner$ = new TimeHelper();
        this.dinner$.setLastMS();
        Thread thread = new Thread(this::_beginner);
        thread.setDaemon(174 - 225 + 133 + -81);
        this.bracelet$ = new Class33(0.0f, 0.0f);
        this.targeted$ = new TimeHelper();
        this.targeted$.setLastMS();
        if (!Class1.asylum$) {
            this.clearing$ = 170 - 196 + 139 + -112;
            return;
        }
        thread.start();
    }

    private void _beginner() {
        String string = "";
        while (!this.dinner$.hasReached(115L - 198L + 71L - 48L + 10060L)) {
            DiscordUser discordUser = DeltaClient.instance.managers.rpc._vendor();
            if (discordUser == null) continue;
            string = discordUser.userId;
            break;
        }
        if (string == "") {
            LogManager.getLogger((String)"Elf").info("Overdue");
        }
        try {
            this._mobile(string);
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
            Wrapper._occurs();
        }
    }

    protected void _utils(char c, int n) {
    }
}

