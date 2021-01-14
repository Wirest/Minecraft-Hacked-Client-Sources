package de.iotacb.client.utilities.render.shader.shaders;

import org.lwjgl.opengl.GL20;

import de.iotacb.client.utilities.render.shader.FrameBufferShader;

public class GlowShader extends FrameBufferShader {
	
	public static final GlowShader GLOW_SHADER = new GlowShader();

	public GlowShader() {
		super("glow");
	}

	@Override
	public void initUniforms() {
		addUniform("texture");
		addUniform("texelSize");
		addUniform("color");
		addUniform("divider");
		addUniform("radius");
		addUniform("maxSample");
	}

	@Override
	public void updateUniforms() {
		GL20.glUniform1i(getUniform("texture"), 0);
		GL20.glUniform2f(getUniform("texelSize"), 1F / getMc().displayWidth, 1F / getMc().displayHeight);
		GL20.glUniform3f(getUniform("color"), 1, 1, 1);
		GL20.glUniform1f(getUniform("divider"), 100F);
		GL20.glUniform1f(getUniform("radius"), 3F);
		GL20.glUniform1f(getUniform("maxSample"), 10F);
	}

}
