package de.iotacb.client.utilities.render.shader;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;

public class ShaderUtil {
	
	public int createShader(String shaderCode, int type) {
		int shader = ARBShaderObjects.glCreateShaderObjectARB(type);
		
		if (shader == 0) return 0;
		
		ARBShaderObjects.glShaderSourceARB(shader, shaderCode);
		ARBShaderObjects.glCompileShaderARB(shader);
		
		if (ARBShaderObjects.glGetObjectParameteriARB(shader, ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL11.GL_FALSE) {
			System.out.println("Error when creating: " + getLogInfo(shader));
			ARBShaderObjects.glDeleteObjectARB(shader);
			return shader;
		}
		
		return shader;
	}
	
    private String getLogInfo(int i) {
        return ARBShaderObjects.glGetInfoLogARB(i, ARBShaderObjects.glGetObjectParameteriARB(i, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
    }
	
	

}
