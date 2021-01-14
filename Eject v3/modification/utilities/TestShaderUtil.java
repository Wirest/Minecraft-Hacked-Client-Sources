package modification.utilities;

import modification.main.Modification;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public final class TestShaderUtil {
    public final int program = ARBShaderObjects.glCreateProgramObjectARB();

    public TestShaderUtil(String paramString) {
        int i = ARBShaderObjects.glCreateShaderObjectARB(35633);
        int j = ARBShaderObjects.glCreateShaderObjectARB(35632);
        ARBShaderObjects.glShaderSourceARB(i, readShaderSource("default.vert"));
        ARBShaderObjects.glShaderSourceARB(j, readShaderSource(paramString.concat(".frag")));
        ARBShaderObjects.glValidateProgramARB(this.program);
        ARBShaderObjects.glCompileShaderARB(i);
        ARBShaderObjects.glCompileShaderARB(j);
        ARBShaderObjects.glAttachObjectARB(this.program, i);
        ARBShaderObjects.glAttachObjectARB(this.program, j);
        ARBShaderObjects.glLinkProgramARB(this.program);
    }

    public final void renderShader(ScaledResolution paramScaledResolution) {
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glBegin(7);
        GL11.glTexCoord2d(0.0D, 1.0D);
        GL11.glVertex2d(0.0D, 0.0D);
        GL11.glTexCoord2d(0.0D, 0.0D);
        GL11.glVertex2d(0.0D, paramScaledResolution.getScaledHeight());
        GL11.glTexCoord2d(1.0D, 0.0D);
        GL11.glVertex2d(paramScaledResolution.getScaledWidth(), paramScaledResolution.getScaledHeight());
        GL11.glTexCoord2d(1.0D, 1.0D);
        GL11.glVertex2d(paramScaledResolution.getScaledWidth(), 0.0D);
        GL11.glEnd();
        GL11.glDisable(3042);
        ARBShaderObjects.glUseProgramObjectARB(0);
    }

    private String readShaderSource(String paramString) {
        StringBuilder localStringBuilder = new StringBuilder();
        try {
            BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("shaders/".concat(paramString))));
            String str;
            while ((str = localBufferedReader.readLine()) != null) {
                localStringBuilder.append(str).append(System.lineSeparator());
            }
            localBufferedReader.close();
        } catch (IOException localIOException) {
            Modification.LOG_UTIL.sendConsoleMessage("Error: Could't find file!");
        }
        return localStringBuilder.toString();
    }
}




