package modification.utilities;

import modification.main.Modification;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public final class ShaderUtil {
    public final int program = GL20.glCreateProgram();
    public float time;
    public boolean count;

    public ShaderUtil(String paramString) {
        int i = GL20.glCreateShader(35632);
        int j = GL20.glCreateShader(35633);
        GL20.glShaderSource(i, readShaderSource(paramString.concat(".frag")));
        GL20.glShaderSource(j, readShaderSource("default.vert"));
        GL20.glCompileShader(j);
        GL20.glCompileShader(i);
        GL20.glValidateProgram(this.program);
        GL20.glAttachShader(this.program, j);
        GL20.glAttachShader(this.program, i);
        GL20.glLinkProgram(this.program);
    }

    public final void renderShader(ScaledResolution paramScaledResolution) {
        if (this.count) {
            this.time = ((float) (this.time + 0.015D));
        }
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
        GL20.glUseProgram(0);
    }

    public final int getUniform(String paramString) {
        return GL20.glGetUniformLocation(this.program, paramString);
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




