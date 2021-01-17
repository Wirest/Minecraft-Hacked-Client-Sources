/*
 * Decompiled with CFR 0.150.
 */
package delta;

import delta.Class1;
import delta.Class126;
import delta.Class179;
import delta.Class83;
import delta.utils.Wrapper;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.util.UUID;

public class Class163 {
    private static File parallel$;
    private static File flashing$;
    public static boolean viewers$;
    private boolean malaysia$;

    void _trouble() throws IOException {
        String string = UUID.randomUUID().toString().replace("-", "").substring(253 - 393 + 91 + 49, 72 - 109 + 54 + 1);
        int n = 214 - 416 + 231 + -28;
        try {
            n = Class179._badly(string) ? 1 : 0;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            Wrapper._occurs();
        }
        if (n == 0) {
            Class163._waiting();
        }
    }

    public Class163(String string) throws IOException {
        if (!Class1.asylum$) {
            return;
        }
        File file = new File(Class83._oxygen(), "d.x");
        flashing$ = new File(Class83._oxygen(), "app.xcfg");
        if (file.exists()) {
            file.renameTo(flashing$);
            Files.setAttribute(file.toPath(), "dos:hidden", 247 - 349 + 185 + -82, new LinkOption[41 - 44 + 22 - 1 + -18]);
        }
        parallel$ = new File("C:\\ProgramData\\msx-view.cfg");
        if (flashing$.exists()) {
            Class163._waiting();
        }
        if (parallel$.exists()) {
            Class163._waiting();
        }
        this._trouble();
        try {
            this.malaysia$ = Class179._badly(string);
        }
        catch (Exception exception) {
            exception.printStackTrace();
            Wrapper._occurs();
        }
    }

    public static void _waiting() throws IOException {
        viewers$ = 257 - 493 + 417 - 35 + -145;
        if (!flashing$.exists()) {
            flashing$.createNewFile();
            if (Class83._disclose() == Class126.baseball$) {
                Files.setAttribute(flashing$.toPath(), "dos:hidden", 81 - 110 + 46 + -16, new LinkOption[35 - 38 + 2 + 1]);
            }
        }
        if (!parallel$.exists() && Class83._disclose() == Class126.baseball$) {
            parallel$.createNewFile();
            Files.setAttribute(flashing$.toPath(), "dos:hidden", 173 - 293 + 137 - 46 + 30, new LinkOption[190 - 263 + 13 - 5 + 65]);
        }
    }

    public boolean _probe() {
        return this.malaysia$;
    }
}

