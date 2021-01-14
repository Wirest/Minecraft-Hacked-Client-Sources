package shadersmod.uniform;

import net.minecraft.world.biome.BiomeGenBase;
import net.optifine.entity.model.anim.ExpressionType;
import net.optifine.entity.model.anim.IExpressionFloat;
import shadersmod.client.Shaders;

public enum ShaderParameterFloat implements IExpressionFloat
{
    BIOME("biome"),
    HELD_ITEM_ID("heldItemId"),
    HELD_BLOCK_LIGHT_VALUE("heldBlockLightValue"),
    HELD_ITEM_ID2("heldItemId2"),
    HELD_BLOCK_LIGHT_VALUE2("heldBlockLightValue2"),
    FOG_MODE("fogMode"),
    WORLD_TIME("worldTime"),
    WORLD_DAY("worldDay"),
    MOON_PHASE("moonPhase"),
    FRAME_COUNTER("frameCounter"),
    FRAME_TIME("frameTime"),
    FRAME_TIME_COUNTER("frameTimeCounter"),
    SUN_ANGLE("sunAngle"),
    SHADOW_ANGLE("shadowAngle"),
    RAIN_STRENGTH("rainStrength"),
    ASPECT_RATIO("aspectRatio"),
    VIEW_WIDTH("viewWidth"),
    VIEW_HEIGHT("viewHeight"),
    NEAR("near"),
    FAR("far"),
    WETNESS("wetness"),
    EYE_ALTITUDE("eyeAltitude"),
    EYE_BRIGHTNESS_X("eyeBrightness.x"),
    EYE_BRIGHTNESS_Y("eyeBrightness.y"),
    TERRAIN_TEXTURE_SIZE_X("terrainTextureSize.x"),
    TERRAIN_TEXTURE_SIZE_Y("terrainTextureSize.y"),
    TERRRAIN_ICON_SIZE("terrainIconSize"),
    IS_EYE_IN_WATER("isEyeInWater"),
    NIGHT_VISION("nightVision"),
    BLINDNESS("blindness"),
    SCREEN_BRIGHTNESS("screenBrightness"),
    HIDE_GUI("hideGUI"),
    CENTER_DEPT_SMOOTH("centerDepthSmooth"),
    ATLAS_SIZE_X("atlasSize.x"),
    ATLAS_SIZE_Y("atlasSize.y"),
    CAMERA_POSITION_X("cameraPosition.x"),
    CAMERA_POSITION_Y("cameraPosition.y"),
    CAMERA_POSITION_Z("cameraPosition.z"),
    PREVIOUS_CAMERA_POSITION_X("previousCameraPosition.x"),
    PREVIOUS_CAMERA_POSITION_Y("previousCameraPosition.y"),
    PREVIOUS_CAMERA_POSITION_Z("previousCameraPosition.z"),
    SUN_POSITION_X("sunPosition.x"),
    SUN_POSITION_Y("sunPosition.y"),
    SUN_POSITION_Z("sunPosition.z"),
    MOON_POSITION_X("moonPosition.x"),
    MOON_POSITION_Y("moonPosition.y"),
    MOON_POSITION_Z("moonPosition.z"),
    SHADOW_LIGHT_POSITION_X("shadowLightPosition.x"),
    SHADOW_LIGHT_POSITION_Y("shadowLightPosition.y"),
    SHADOW_LIGHT_POSITION_Z("shadowLightPosition.z"),
    UP_POSITION_X("upPosition.x"),
    UP_POSITION_Y("upPosition.y"),
    UP_POSITION_Z("upPosition.z"),
    FOG_COLOR_R("fogColor.r"),
    FOG_COLOR_G("fogColor.g"),
    FOG_COLOR_B("fogColor.b"),
    SKY_COLOR_R("skyColor.r"),
    SKY_COLOR_G("skyColor.g"),
    SKY_COLOR_B("skyColor.b");

    private String name;

    private ShaderParameterFloat(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public ExpressionType getExpressionType()
    {
        return ExpressionType.FLOAT;
    }

    public float eval()
    {
        switch (this)
        {
            case BIOME:
                BiomeGenBase biomegenbase = Shaders.getCurrentWorld().getBiomeGenForCoords(Shaders.getCameraPosition());
                return (float)biomegenbase.biomeID;

            default:
                Number number = LegacyUniforms.getNumber(this.name);
                return number != null ? number.floatValue() : 0.0F;
        }
    }
}
