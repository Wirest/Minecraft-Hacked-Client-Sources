package de.iotacb.cu.core.render.shader;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import net.minecraft.client.Minecraft;

public abstract class Shader {

	private int shaderProgramId;
	
	private Map<String, Integer> uniforms;
	
	private static final Minecraft MC = Minecraft.getMinecraft();
	
	public Shader(final String fragementShaderName) {
		try {
			final InputStream vertex = getClass().getResourceAsStream("/assets/minecraft/cu/shaders/base.vert");
			final int vertexId = createShaderId(IOUtils.toString(vertex), ARBVertexShader.GL_VERTEX_SHADER_ARB);
			IOUtils.closeQuietly(vertex);
			final InputStream fragment = getClass().getResourceAsStream("/assets/minecraft/cu/shaders/".concat(fragementShaderName).concat(".frag"));
			final int fragmentId = createShaderId(IOUtils.toString(fragment), ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);
			IOUtils.closeQuietly(fragment);
			
			if (vertexId == 0 || fragmentId == 0) return;
			
			shaderProgramId = ARBShaderObjects.glCreateProgramObjectARB();
			
			if (shaderProgramId == 0) return;
			
			ARBShaderObjects.glAttachObjectARB(shaderProgramId, vertexId);
			ARBShaderObjects.glAttachObjectARB(shaderProgramId, fragmentId);
			
			ARBShaderObjects.glLinkProgramARB(shaderProgramId);
			ARBShaderObjects.glValidateProgramARB(shaderProgramId);
			
			System.out.println("Loaded shader!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public abstract void initUniforms();
	public abstract void updateUniforms();
	
	public void startShader() {
		GL11.glPushMatrix();
		GL20.glUseProgram(shaderProgramId);
		
		if (getUniforms() == null) {
			uniforms = new HashMap<>();
			initUniforms();
		}
		
		updateUniforms();
	}
	
	public void stopShader() {
		GL20.glUseProgram(0);
		GL11.glPopMatrix();
	}
	
	public int getShaderProgramId() {
		return shaderProgramId;
	}
	
	public Map<String, Integer> getUniforms() {
		return uniforms;
	}
	
	public int getUniform(String uniformName) {
		return getUniforms().get(uniformName);
	}
	
	public static Minecraft getMc() {
		return MC;
	}
	
	public void addUniform(String uniformName) {
		getUniforms().put(uniformName, GL20.glGetUniformLocation(getShaderProgramId(), uniformName));
	}
	
	
	private int createShaderId(final String shaderCode, final int shaderType) {
		int shader = ARBShaderObjects.glCreateShaderObjectARB(shaderType);
		
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
