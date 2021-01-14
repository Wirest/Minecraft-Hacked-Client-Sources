package net.minecraft.optifine;

import java.util.Properties;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;

public class CustomSkyLayer {
    public String source = null;
    private int startFadeIn = -1;
    private int endFadeIn = -1;
    private int startFadeOut = -1;
    private int endFadeOut = -1;
    private int blend = 1;
    private boolean rotate = false;
    private float speed = 1.0F;
    private float[] axis;
    public int textureId;
    public static final float[] DEFAULT_AXIS = new float[]{1.0F, 0.0F, 0.0F};

    public CustomSkyLayer(Properties props, String defSource) {
        axis = CustomSkyLayer.DEFAULT_AXIS;
        textureId = -1;
        source = props.getProperty("source", defSource);
        startFadeIn = parseTime(props.getProperty("startFadeIn"));
        endFadeIn = parseTime(props.getProperty("endFadeIn"));
        startFadeOut = parseTime(props.getProperty("startFadeOut"));
        endFadeOut = parseTime(props.getProperty("endFadeOut"));
        blend = Blender.parseBlend(props.getProperty("blend"));
        rotate = parseBoolean(props.getProperty("rotate"), true);
        speed = parseFloat(props.getProperty("speed"), 1.0F);
        axis = parseAxis(props.getProperty("axis"), CustomSkyLayer.DEFAULT_AXIS);
    }

    private int parseTime(String str) {
        if (str == null) {
            return -1;
        } else {
            String[] strs = Config.tokenize(str, ":");

            if (strs.length != 2) {
                Config.warn("Invalid time: " + str);
                return -1;
            } else {
                String hourStr = strs[0];
                String minStr = strs[1];
                int hour = Config.parseInt(hourStr, -1);
                int min = Config.parseInt(minStr, -1);

                if (hour >= 0 && hour <= 23 && min >= 0 && min <= 59) {
                    hour -= 6;

                    if (hour < 0) {
                        hour += 24;
                    }

                    int time = hour * 1000 + (int) (min / 60.0D * 1000.0D);
                    return time;
                } else {
                    Config.warn("Invalid time: " + str);
                    return -1;
                }
            }
        }
    }

    private boolean parseBoolean(String str, boolean defVal) {
        if (str == null) {
            return defVal;
        } else if (str.toLowerCase().equals("true")) {
            return true;
        } else if (str.toLowerCase().equals("false")) {
            return false;
        } else {
            Config.warn("Unknown boolean: " + str);
            return defVal;
        }
    }

    private float parseFloat(String str, float defVal) {
        if (str == null) {
            return defVal;
        } else {
            float val = Config.parseFloat(str, Float.MIN_VALUE);

            if (val == Float.MIN_VALUE) {
                Config.warn("Invalid value: " + str);
                return defVal;
            } else {
                return val;
            }
        }
    }

    private float[] parseAxis(String str, float[] defVal) {
        if (str == null) {
            return defVal;
        } else {
            String[] strs = Config.tokenize(str, " ");

            if (strs.length != 3) {
                Config.warn("Invalid axis: " + str);
                return defVal;
            } else {
                float[] fs = new float[3];

                for (int ax = 0; ax < strs.length; ++ax) {
                    fs[ax] = Config.parseFloat(strs[ax], Float.MIN_VALUE);

                    if (fs[ax] == Float.MIN_VALUE) {
                        Config.warn("Invalid axis: " + str);
                        return defVal;
                    }

                    if (fs[ax] < -1.0F || fs[ax] > 1.0F) {
                        Config.warn("Invalid axis values: " + str);
                        return defVal;
                    }
                }

                float var9 = fs[0];
                float ay = fs[1];
                float az = fs[2];

                if (var9 * var9 + ay * ay + az * az < 1.0E-5F) {
                    Config.warn("Invalid axis values: " + str);
                    return defVal;
                } else {
                    float[] as = new float[]{az, ay, -var9};
                    return as;
                }
            }
        }
    }

    public boolean isValid(String path) {
        if (source == null) {
            Config.warn("No source texture: " + path);
            return false;
        } else {
            source = TextureUtils.fixResourcePath(source, TextureUtils.getBasePath(path));

            if (startFadeIn >= 0 && endFadeIn >= 0 && endFadeOut >= 0) {
                int timeFadeIn = normalizeTime(endFadeIn - startFadeIn);

                if (startFadeOut < 0) {
                    startFadeOut = normalizeTime(endFadeOut - timeFadeIn);
                }

                int timeOn = normalizeTime(startFadeOut - endFadeIn);
                int timeFadeOut = normalizeTime(endFadeOut - startFadeOut);
                int timeOff = normalizeTime(startFadeIn - endFadeOut);
                int timeSum = timeFadeIn + timeOn + timeFadeOut + timeOff;

                if (timeSum != 24000) {
                    Config.warn("Invalid fadeIn/fadeOut times, sum is more than 24h: " + timeSum);
                    return false;
                } else if (speed < 0.0F) {
                    Config.warn("Invalid speed: " + speed);
                    return false;
                } else {
                    return true;
                }
            } else {
                Config.warn("Invalid times, required are: startFadeIn, endFadeIn and endFadeOut.");
                return false;
            }
        }
    }

    private int normalizeTime(int timeMc) {
        while (timeMc >= 24000) {
            timeMc -= 24000;
        }

        while (timeMc < 0) {
            timeMc += 24000;
        }

        return timeMc;
    }

    public void render(int timeOfDay, float celestialAngle, float rainBrightness) {
        float brightness = rainBrightness * getFadeBrightness(timeOfDay);
        brightness = Config.limit(brightness, 0.0F, 1.0F);

        if (brightness >= 1.0E-4F) {
            GlStateManager.bindTexture(textureId);
            Blender.setupBlend(blend, brightness);
            GlStateManager.pushMatrix();

            if (rotate) {
                GlStateManager.rotate(celestialAngle * 360.0F * speed, axis[0], axis[1], axis[2]);
            }

            Tessellator tess = Tessellator.getInstance();
            GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(-90.0F, 0.0F, 0.0F, 1.0F);
            renderSide(tess, 4);
            GlStateManager.pushMatrix();
            GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
            renderSide(tess, 1);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
            renderSide(tess, 0);
            GlStateManager.popMatrix();
            GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
            renderSide(tess, 5);
            GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
            renderSide(tess, 2);
            GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
            renderSide(tess, 3);
            GlStateManager.popMatrix();
        }
    }

    private float getFadeBrightness(int timeOfDay) {
        int timeFadeOut;
        int timeDiff;

        if (timeBetween(timeOfDay, startFadeIn, endFadeIn)) {
            timeFadeOut = normalizeTime(endFadeIn - startFadeIn);
            timeDiff = normalizeTime(timeOfDay - startFadeIn);
            return (float) timeDiff / (float) timeFadeOut;
        } else if (timeBetween(timeOfDay, endFadeIn, startFadeOut)) {
            return 1.0F;
        } else if (timeBetween(timeOfDay, startFadeOut, endFadeOut)) {
            timeFadeOut = normalizeTime(endFadeOut - startFadeOut);
            timeDiff = normalizeTime(timeOfDay - startFadeOut);
            return 1.0F - (float) timeDiff / (float) timeFadeOut;
        } else {
            return 0.0F;
        }
    }

    private void renderSide(Tessellator tess, int side) {
        WorldRenderer wr = tess.getWorldRenderer();
        double tx = side % 3 / 3.0D;
        double ty = side / 3 / 2.0D;
        wr.startDrawingQuads();
        wr.addVertexWithUV(-100.0D, -100.0D, -100.0D, tx, ty);
        wr.addVertexWithUV(-100.0D, -100.0D, 100.0D, tx, ty + 0.5D);
        wr.addVertexWithUV(100.0D, -100.0D, 100.0D, tx + 0.3333333333333333D, ty + 0.5D);
        wr.addVertexWithUV(100.0D, -100.0D, -100.0D, tx + 0.3333333333333333D, ty);
        tess.draw();
    }

    public boolean isActive(int timeOfDay) {
        return !timeBetween(timeOfDay, endFadeOut, startFadeIn);
    }

    private boolean timeBetween(int timeOfDay, int timeStart, int timeEnd) {
        return timeStart <= timeEnd ? timeOfDay >= timeStart && timeOfDay <= timeEnd : timeOfDay >= timeStart || timeOfDay <= timeEnd;
    }
}
