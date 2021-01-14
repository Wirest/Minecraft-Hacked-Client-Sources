package de.iotacb.client.utilities.render.shader;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import de.iotacb.client.Client;
import net.minecraft.client.Minecraft;
import wriva.core.Reader;

public abstract class Shader {

	/**
	 * http://wiki.lwjgl.org/wiki/GLSL_Shaders_with_LWJGL.html
	 */
	
	private int shaderProgramId;
	
	private Map<String, Integer> uniforms;
	
	private static final Minecraft MC = Minecraft.getMinecraft();
	
	public Shader(String fragementShaderName) {
		try {
			final InputStream vertex = getClass().getResourceAsStream("/assets/minecraft/client/shaders/base.vert");
			final int vertexId = Client.SHADER_UTIL.createShader(IOUtils.toString(vertex), ARBVertexShader.GL_VERTEX_SHADER_ARB);
			IOUtils.closeQuietly(vertex);
			final InputStream fragment = getClass().getResourceAsStream("/assets/minecraft/client/shaders/".concat(fragementShaderName).concat(".frag"));
			final int fragmentId = Client.SHADER_UTIL.createShader(IOUtils.toString(fragment), ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);
			IOUtils.closeQuietly(fragment);
			
			if (vertexId == 0 || fragmentId == 0) return;
			
			shaderProgramId = ARBShaderObjects.glCreateProgramObjectARB(); // Create program
			
			if (shaderProgramId == 0) return;
			
			ARBShaderObjects.glAttachObjectARB(shaderProgramId, vertexId); // Attach vertex shader to the program
			ARBShaderObjects.glAttachObjectARB(shaderProgramId, fragmentId); // Attach fragment shader to the program
			
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
}
