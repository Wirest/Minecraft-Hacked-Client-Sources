package shadersmod.client;

import org.lwjgl.opengl.ARBShaderObjects;

public class ShaderUniformInt
        extends ShaderUniformBase {
    private int value = -1;

    public ShaderUniformInt(String paramString) {
        super(paramString);
    }

    protected void onProgramChanged() {
        this.value = -1;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int paramInt) {
        if ((getLocation() >= 0) && (this.value != paramInt)) {
            ARBShaderObjects.glUniform1iARB(getLocation(), paramInt);
            Shaders.checkGLError(getName());
            this.value = paramInt;
        }
    }
}




