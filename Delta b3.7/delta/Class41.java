/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.xtrm.delta.api.command.ICommandListener
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.GuiTextField
 */
package delta;

import delta.Class132;
import delta.Class69;
import delta.client.DeltaClient;
import delta.guis.click.ClickGUI;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import me.xtrm.delta.api.command.ICommandListener;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiTextField;

public class Class41
extends Class132
implements ICommandListener {
    private List<String> becomes$;
    private GuiTextField creating$ = new GuiTextField(Class69.somalia$, 73 - 99 + 25 + 1, 112 - 159 + 120 + -73, 217 - 261 + 138 - 43 + 149, 34 - 56 + 48 + -16);

    public void print(String string) {
        if (string.contains("\n")) {
            String[] arrstring;
            String[] arrstring2 = arrstring = string.split(Pattern.quote("\n"));
            int n = arrstring2.length;
            for (int i = 178 - 299 + 172 - 2 + -49; i < n; ++i) {
                String string2 = arrstring2[i];
                this.becomes$.add(string2);
            }
        } else {
            this.becomes$.add(string);
        }
    }

    @Override
    public void _brook(int n, int n2, float f) {
        super._brook(n, n2, f);
        if (this.Jtml) {
            Gui.drawRect((int)(this.h1bT.longest$ + (237 - 311 + 90 - 72 + 244)), (int)(this.h1bT.apollo$ + (230 - 433 + 81 + 142)), (int)(this.h1bT.longest$ + this.h1bT.carriers$ - (171 - 269 + 75 + 28)), (int)(this.h1bT.apollo$ + this.h1bT.suddenly$ - (242 - 276 + 170 + -131)), (int)(229 - 384 + 331 - 84 + -871296843));
            Gui.drawRect((int)(this.h1bT.longest$ + (32 - 34 + 19 + 73)), (int)(this.h1bT.apollo$ + this.h1bT.suddenly$ - (212 - 374 + 48 + 119) - (187 - 281 + 135 + -24)), (int)(this.h1bT.longest$ + this.h1bT.carriers$ - (125 - 218 + 94 - 46 + 50)), (int)(this.h1bT.apollo$ + this.h1bT.suddenly$ - (48 - 56 + 7 - 5 + 11)), (int)(242 - 370 + 182 - 22 + 2147483616));
            Gui.drawRect((int)(this.h1bT.longest$ + (60 - 97 + 44 - 1 + 84)), (int)(this.h1bT.apollo$ + this.h1bT.suddenly$ - (116 - 174 + 3 + 60) - (150 - 210 + 51 - 45 + 72)), (int)(this.h1bT.longest$ + this.h1bT.carriers$ - (268 - 487 + 205 + 19)), (int)(this.h1bT.apollo$ + this.h1bT.suddenly$ - (217 - 369 + 258 - 202 + 101) - (223 - 368 + 27 - 18 + 153)), (int)(143 - 197 + 19 - 18 + -6155817));
            Class69.somalia$.drawStringWithShadow(">", (int)this.At3d + (int)this.lNEE + (103 - 119 + 22 - 4 + 8), (int)this.esj9 + (int)(this.O1Z7 / 2.0) - Class69.somalia$.FONT_HEIGHT / (251 - 480 + 329 - 120 + 22) - (183 - 285 + 116 - 113 + 100), 156 - 223 + 155 - 140 + 51);
            for (int i = this.becomes$.size(); i > 0; --i) {
                String string = this.becomes$.get(i - (127 - 139 + 9 + 4));
                Class69.somalia$.drawStringWithShadow(string, this.h1bT.longest$ + (29 - 57 + 29 - 8 + 102), (int)(this.esj9 - 14.0 - (double)(this.becomes$.size() * Class69.somalia$.FONT_HEIGHT) + (double)(i * Class69.somalia$.FONT_HEIGHT)), 60 - 104 + 101 - 21 + -37);
                if (i < this.becomes$.size() - (210 - 346 + 201 + -47)) break;
            }
            this.creating$.setFocused(237 - 446 + 252 + -42);
            this.creating$.setEnableBackgroundDrawing(117 - 191 + 42 + 32);
            this.creating$.setMaxStringLength(54 - 73 + 2 + 217);
            this.creating$.drawTextBox();
        }
    }

    @Override
    public void _surface(int n, int n2, int n3) {
        super._surface(n, n2, n3);
        if (this.Jtml) {
            this.creating$.mouseClicked(n, n2, n3);
        }
    }

    public Class41(ClickGUI clickGUI) {
        super("Console", clickGUI);
        this.becomes$ = new ArrayList<String>();
    }

    @Override
    public void _schemes(char c, int n) {
        if (this.Jtml) {
            if (n == 117 - 156 + 81 - 77 + 63) {
                String string = this.creating$.getText();
                this.creating$.setText("");
                DeltaClient.instance.managers.commandsManager.runCommand((ICommandListener)this, string);
                return;
            }
            this.creating$.textboxKeyTyped(c, n);
        }
    }

    @Override
    public void _myself() {
        super._myself();
        this.esj9 = (double)(this.h1bT.apollo$ + this.h1bT.suddenly$ - (244 - 380 + 344 + -203)) - this.O1Z7;
        this.creating$.xPosition = (int)this.At3d + (int)this.lNEE + (61 - 103 + 30 + 17) + (236 - 424 + 45 - 9 + 166);
        this.creating$.yPosition = (int)this.esj9 + (int)(this.O1Z7 / 2.0) - (52 - 84 + 9 - 1 + 29);
        this.creating$.height = (int)this.O1Z7;
        this.creating$.width = this.h1bT.longest$ + this.h1bT.carriers$ - (177 - 248 + 17 + 59) - (this.h1bT.longest$ + (182 - 204 + 77 - 10 + 60));
    }

    public void clear() {
        this.becomes$.clear();
    }
}

