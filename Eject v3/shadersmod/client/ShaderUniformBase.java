package shadersmod.client;

import org.lwjgl.opengl.ARBShaderObjects;

public abstract class ShaderUniformBase {
    private String name;
    private int program = -1;
    private int location = -1;

    public ShaderUniformBase(String paramString) {
        this.name = paramString;
    }

    protected abstract void onProgramChanged();

    public String getName() {
        return this.name;
    }

    public int getProgram() {
        return this.program;
    }

    public void setProgram(int paramInt) {
        if (this.program != paramInt) {
            this.program = paramInt;
            this.location = ARBShaderObjects.glGetUniformLocationARB(paramInt, this.name);
            onProgramChanged();
        }
    }

    public int getLocation() {
        return this.location;
    }

    public boolean isDefined() {
        return this.location >= 0;
    }
}




