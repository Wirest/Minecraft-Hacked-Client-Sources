package shadersmod.client;

public enum EnumShaderOption {
	ANTIALIASING("Antialiasing", "antialiasingLevel", "0"), NORMAL_MAP(
			"Normal Map", "normalMapEnabled",
			"true"), SPECULAR_MAP("Specular Map", "specularMapEnabled", "true"), RENDER_RES_MUL(
					"Render Quality", "renderResMul",
					"1.0"), SHADOW_RES_MUL("Shadow Quality", "shadowResMul", "1.0"), HAND_DEPTH_MUL(
							"Hand Depth", "handDepthMul",
							"0.125"), CLOUD_SHADOW("of.options.shaders.CLOUD_SHADOW", "cloudShadow",
									"true"), OLD_LIGHTING("Old Lighting", "oldLighting",
											"default"), SHADER_PACK("of.options.shaders.SHADER_PACK", "shaderPack",
													""), TWEAK_BLOCK_DAMAGE("of.options.shaders.TWEAK_BLOCK_DAMAGE",
															"tweakBlockDamage", "false"), SHADOW_CLIP_FRUSTRUM(
																	"of.options.shaders.SHADOW_CLIP_FRUSTRUM",
																	"shadowClipFrustrum", "true"), TEX_MIN_FIL_B(
																			"of.options.shaders.TEX_MIN_FIL_B",
																			"TexMinFilB", "0"), TEX_MIN_FIL_N(
																					"of.options.shaders.TEX_MIN_FIL_N",
																					"TexMinFilN", "0"), TEX_MIN_FIL_S(
																							"of.options.shaders.TEX_MIN_FIL_S",
																							"TexMinFilS",
																							"0"), TEX_MAG_FIL_B(
																									"of.options.shaders.TEX_MAG_FIL_B",
																									"TexMagFilB",
																									"0"), TEX_MAG_FIL_N(
																											"of.options.shaders.TEX_MAG_FIL_N",
																											"TexMagFilN",
																											"0"), TEX_MAG_FIL_S(
																													"of.options.shaders.TEX_MAG_FIL_S",
																													"TexMagFilS",
																													"0");

	private String resourceKey = null;
	private String propertyKey = null;
	private String valueDefault = null;

	private EnumShaderOption(String resourceKey, String propertyKey, String valueDefault) {
		this.resourceKey = resourceKey;
		this.propertyKey = propertyKey;
		this.valueDefault = valueDefault;
	}

	public String getResourceKey() {
		return this.resourceKey;
	}

	public String getPropertyKey() {
		return this.propertyKey;
	}

	public String getValueDefault() {
		return this.valueDefault;
	}
}
