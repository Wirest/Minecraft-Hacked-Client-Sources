// 
// Decompiled by Procyon v0.5.36
// 

package com.jcraft.jorbis;

class PsyInfo
{
    int athp;
    int decayp;
    int smoothp;
    int noisefitp;
    int noisefit_subblock;
    float noisefit_threshdB;
    float ath_att;
    int tonemaskp;
    float[] toneatt_125Hz;
    float[] toneatt_250Hz;
    float[] toneatt_500Hz;
    float[] toneatt_1000Hz;
    float[] toneatt_2000Hz;
    float[] toneatt_4000Hz;
    float[] toneatt_8000Hz;
    int peakattp;
    float[] peakatt_125Hz;
    float[] peakatt_250Hz;
    float[] peakatt_500Hz;
    float[] peakatt_1000Hz;
    float[] peakatt_2000Hz;
    float[] peakatt_4000Hz;
    float[] peakatt_8000Hz;
    int noisemaskp;
    float[] noiseatt_125Hz;
    float[] noiseatt_250Hz;
    float[] noiseatt_500Hz;
    float[] noiseatt_1000Hz;
    float[] noiseatt_2000Hz;
    float[] noiseatt_4000Hz;
    float[] noiseatt_8000Hz;
    float max_curve_dB;
    float attack_coeff;
    float decay_coeff;
    
    PsyInfo() {
        this.toneatt_125Hz = new float[5];
        this.toneatt_250Hz = new float[5];
        this.toneatt_500Hz = new float[5];
        this.toneatt_1000Hz = new float[5];
        this.toneatt_2000Hz = new float[5];
        this.toneatt_4000Hz = new float[5];
        this.toneatt_8000Hz = new float[5];
        this.peakatt_125Hz = new float[5];
        this.peakatt_250Hz = new float[5];
        this.peakatt_500Hz = new float[5];
        this.peakatt_1000Hz = new float[5];
        this.peakatt_2000Hz = new float[5];
        this.peakatt_4000Hz = new float[5];
        this.peakatt_8000Hz = new float[5];
        this.noiseatt_125Hz = new float[5];
        this.noiseatt_250Hz = new float[5];
        this.noiseatt_500Hz = new float[5];
        this.noiseatt_1000Hz = new float[5];
        this.noiseatt_2000Hz = new float[5];
        this.noiseatt_4000Hz = new float[5];
        this.noiseatt_8000Hz = new float[5];
    }
    
    void free() {
    }
}
