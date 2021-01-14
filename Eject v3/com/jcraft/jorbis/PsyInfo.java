package com.jcraft.jorbis;

class PsyInfo {
    int athp;
    int decayp;
    int smoothp;
    int noisefitp;
    int noisefit_subblock;
    float noisefit_threshdB;
    float ath_att;
    int tonemaskp;
    float[] toneatt_125Hz = new float[5];
    float[] toneatt_250Hz = new float[5];
    float[] toneatt_500Hz = new float[5];
    float[] toneatt_1000Hz = new float[5];
    float[] toneatt_2000Hz = new float[5];
    float[] toneatt_4000Hz = new float[5];
    float[] toneatt_8000Hz = new float[5];
    int peakattp;
    float[] peakatt_125Hz = new float[5];
    float[] peakatt_250Hz = new float[5];
    float[] peakatt_500Hz = new float[5];
    float[] peakatt_1000Hz = new float[5];
    float[] peakatt_2000Hz = new float[5];
    float[] peakatt_4000Hz = new float[5];
    float[] peakatt_8000Hz = new float[5];
    int noisemaskp;
    float[] noiseatt_125Hz = new float[5];
    float[] noiseatt_250Hz = new float[5];
    float[] noiseatt_500Hz = new float[5];
    float[] noiseatt_1000Hz = new float[5];
    float[] noiseatt_2000Hz = new float[5];
    float[] noiseatt_4000Hz = new float[5];
    float[] noiseatt_8000Hz = new float[5];
    float max_curve_dB;
    float attack_coeff;
    float decay_coeff;

    void free() {
    }
}




