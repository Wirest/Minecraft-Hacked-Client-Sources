/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.hash.Hashing
 *  cpw.mods.fml.relauncher.FMLInjectionData
 *  me.xtrm.atlaspluginloader.api.types.IBasePlugin
 *  me.xtrm.atlaspluginloader.api.types.PluginInfo
 */
package delta;

import com.google.common.hash.Hashing;
import cpw.mods.fml.relauncher.FMLInjectionData;
import delta.Class43;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.regex.Pattern;
import me.xtrm.atlaspluginloader.api.types.IBasePlugin;
import me.xtrm.atlaspluginloader.api.types.PluginInfo;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@PluginInfo(name="Xeon", version="3.0.0", author="xTrM_")
public class Class123
implements IBasePlugin {
    private File lanka$ = new File(Class43._agents(), ".xeon");
    private File episode$;
    private String vendors$;

    private void 7Tbh() throws IOException {
        String[] arrstring = new String(Files.readAllBytes(this.lanka$.toPath())).split(Pattern.quote(";"));
        String string = arrstring[181 - 234 + 81 + -28];
        this.vendors$ = arrstring[155 - 164 + 85 + -75];
        this.episode$ = new File(string);
    }

    private void aFtL() throws IOException {
        File file = new File((File)FMLInjectionData.data()[120 - 218 + 189 + -85], "mods");
        File[] arrfile = file.listFiles();
        int n = arrfile.length;
        for (int i = 70 - 118 + 101 - 90 + 37; i < n; ++i) {
            File file2 = arrfile[i];
            String string = file2.getName().substring(182 - 271 + 104 + -15, file2.getName().indexOf(177 - 180 + 105 + -56) == 87 - 150 + 133 + -71 ? file2.getName().length() : file2.getName().indexOf(96 - 111 + 80 + -19));
            String string2 = Hashing.sha256().hashString((CharSequence)string, StandardCharsets.UTF_8).toString();
            if (!string2.equalsIgnoreCase(this.episode$.getName())) continue;
            file2.delete();
            break;
        }
    }

    public Class123() {
        if (!this.lanka$.exists()) {
            return;
        }
        try {
            this.7Tbh();
            this.aFtL();
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }
}

