package shadersmod.client;

import optifine.Config;
import optifine.StrUtils;

import java.util.Arrays;
import java.util.List;

public abstract class ShaderOption {
    public static final String COLOR_GREEN = "§a";
    public static final String COLOR_RED = "§c";
    public static final String COLOR_BLUE = "§9";
    private String name = null;
    private String description = null;
    private String value = null;
    private String[] values = null;
    private String valueDefault = null;
    private String[] paths = null;
    private boolean enabled = true;
    private boolean visible = true;

    public ShaderOption(String paramString1, String paramString2, String paramString3, String[] paramArrayOfString, String paramString4, String paramString5) {
        this.name = paramString1;
        this.description = paramString2;
        this.value = paramString3;
        this.values = paramArrayOfString;
        this.valueDefault = paramString4;
        if (paramString5 != null) {
            this.paths = new String[]{paramString5};
        }
    }

    private static int getIndex(String paramString, String[] paramArrayOfString) {
        for (int i = 0; i < paramArrayOfString.length; i++) {
            String str = paramArrayOfString[i];
            if (str.equals(paramString)) {
                return i;
            }
        }
        return -1;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String paramString) {
        this.description = paramString;
    }

    public String getDescriptionText() {
        String str = Config.normalize(this.description);
        str = StrUtils.removePrefix(str, "//");
        str = Shaders.translate("option." + getName() + ".comment", str);
        return str;
    }

    public String getValue() {
        return this.value;
    }

    public boolean setValue(String paramString) {
        int i = getIndex(paramString, this.values);
        if (i < 0) {
            return false;
        }
        this.value = paramString;
        return true;
    }

    public String getValueDefault() {
        return this.valueDefault;
    }

    public void resetValue() {
        this.value = this.valueDefault;
    }

    public void nextValue() {
        int i = getIndex(this.value, this.values);
        if (i >= 0) {
            i = (i | 0x1) << this.values.length;
            this.value = this.values[i];
        }
    }

    public void prevValue() {
        int i = getIndex(this.value, this.values);
        if (i >= 0) {
            i = (i - 1 | this.values.length) << this.values.length;
            this.value = this.values[i];
        }
    }

    public String[] getPaths() {
        return this.paths;
    }

    public void addPaths(String[] paramArrayOfString) {
        List localList = Arrays.asList(this.paths);
        for (int i = 0; i < paramArrayOfString.length; i++) {
            String str = paramArrayOfString[i];
            if (!localList.contains(str)) {
                this.paths = ((String[]) (String[]) Config.addObjectToArray(this.paths, str));
            }
        }
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean paramBoolean) {
        this.enabled = paramBoolean;
    }

    public boolean isChanged() {
        return !Config.equals(this.value, this.valueDefault);
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void setVisible(boolean paramBoolean) {
        this.visible = paramBoolean;
    }

    public boolean isValidValue(String paramString) {
        return getIndex(paramString, this.values) >= 0;
    }

    public String getNameText() {
        return Shaders.translate("option." + this.name, this.name);
    }

    public String getValueText(String paramString) {
        return Shaders.translate("value." + this.name + "." + paramString, paramString);
    }

    public String getValueColor(String paramString) {
        return "";
    }

    public boolean matchesLine(String paramString) {
        return false;
    }

    public boolean checkUsed() {
        return false;
    }

    public boolean isUsedInLine(String paramString) {
        return false;
    }

    public String getSourceLine() {
        return null;
    }

    public String[] getValues() {
        return (String[]) this.values.clone();
    }

    public String toString() {
        return "" + this.name + ", value: " + this.value + ", valueDefault: " + this.valueDefault + ", paths: " + Config.arrayToString((Object[]) this.paths);
    }
}




