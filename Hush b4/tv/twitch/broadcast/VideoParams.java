// 
// Decompiled by Procyon v0.5.36
// 

package tv.twitch.broadcast;

public class VideoParams
{
    public int outputWidth;
    public int outputHeight;
    public PixelFormat pixelFormat;
    public int maxKbps;
    public int targetFps;
    public EncodingCpuUsage encodingCpuUsage;
    public boolean disableAdaptiveBitrate;
    public boolean verticalFlip;
    
    public VideoParams() {
        this.pixelFormat = PixelFormat.TTV_PF_BGRA;
        this.encodingCpuUsage = EncodingCpuUsage.TTV_ECU_HIGH;
        this.disableAdaptiveBitrate = false;
        this.verticalFlip = false;
    }
    
    public VideoParams clone() {
        final VideoParams videoParams = new VideoParams();
        videoParams.outputWidth = this.outputWidth;
        videoParams.outputHeight = this.outputHeight;
        videoParams.pixelFormat = this.pixelFormat;
        videoParams.maxKbps = this.maxKbps;
        videoParams.targetFps = this.targetFps;
        videoParams.encodingCpuUsage = this.encodingCpuUsage;
        videoParams.disableAdaptiveBitrate = this.disableAdaptiveBitrate;
        videoParams.verticalFlip = this.verticalFlip;
        return videoParams;
    }
}
