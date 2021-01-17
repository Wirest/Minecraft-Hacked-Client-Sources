package javazoom.jl.decoder;

import java.io.IOException;

final class SynthesisFilter {
   private float[] v1;
   private float[] v2;
   private float[] actual_v;
   private int actual_write_pos;
   private float[] samples;
   private int channel;
   private float scalefactor;
   private float[] eq;
   private float[] _tmpOut = new float[32];
   private static final double MY_PI = 3.141592653589793D;
   private static final float cos1_64 = (float)(1.0D / (2.0D * Math.cos(0.04908738521234052D)));
   private static final float cos3_64 = (float)(1.0D / (2.0D * Math.cos(0.14726215563702155D)));
   private static final float cos5_64 = (float)(1.0D / (2.0D * Math.cos(0.2454369260617026D)));
   private static final float cos7_64 = (float)(1.0D / (2.0D * Math.cos(0.3436116964863836D)));
   private static final float cos9_64 = (float)(1.0D / (2.0D * Math.cos(0.44178646691106466D)));
   private static final float cos11_64 = (float)(1.0D / (2.0D * Math.cos(0.5399612373357456D)));
   private static final float cos13_64 = (float)(1.0D / (2.0D * Math.cos(0.6381360077604268D)));
   private static final float cos15_64 = (float)(1.0D / (2.0D * Math.cos(0.7363107781851077D)));
   private static final float cos17_64 = (float)(1.0D / (2.0D * Math.cos(0.8344855486097889D)));
   private static final float cos19_64 = (float)(1.0D / (2.0D * Math.cos(0.9326603190344698D)));
   private static final float cos21_64 = (float)(1.0D / (2.0D * Math.cos(1.030835089459151D)));
   private static final float cos23_64 = (float)(1.0D / (2.0D * Math.cos(1.1290098598838318D)));
   private static final float cos25_64 = (float)(1.0D / (2.0D * Math.cos(1.227184630308513D)));
   private static final float cos27_64 = (float)(1.0D / (2.0D * Math.cos(1.325359400733194D)));
   private static final float cos29_64 = (float)(1.0D / (2.0D * Math.cos(1.423534171157875D)));
   private static final float cos31_64 = (float)(1.0D / (2.0D * Math.cos(1.521708941582556D)));
   private static final float cos1_32 = (float)(1.0D / (2.0D * Math.cos(0.09817477042468103D)));
   private static final float cos3_32 = (float)(1.0D / (2.0D * Math.cos(0.2945243112740431D)));
   private static final float cos5_32 = (float)(1.0D / (2.0D * Math.cos(0.4908738521234052D)));
   private static final float cos7_32 = (float)(1.0D / (2.0D * Math.cos(0.6872233929727672D)));
   private static final float cos9_32 = (float)(1.0D / (2.0D * Math.cos(0.8835729338221293D)));
   private static final float cos11_32 = (float)(1.0D / (2.0D * Math.cos(1.0799224746714913D)));
   private static final float cos13_32 = (float)(1.0D / (2.0D * Math.cos(1.2762720155208536D)));
   private static final float cos15_32 = (float)(1.0D / (2.0D * Math.cos(1.4726215563702154D)));
   private static final float cos1_16 = (float)(1.0D / (2.0D * Math.cos(0.19634954084936207D)));
   private static final float cos3_16 = (float)(1.0D / (2.0D * Math.cos(0.5890486225480862D)));
   private static final float cos5_16 = (float)(1.0D / (2.0D * Math.cos(0.9817477042468103D)));
   private static final float cos7_16 = (float)(1.0D / (2.0D * Math.cos(1.3744467859455345D)));
   private static final float cos1_8 = (float)(1.0D / (2.0D * Math.cos(0.39269908169872414D)));
   private static final float cos3_8 = (float)(1.0D / (2.0D * Math.cos(1.1780972450961724D)));
   private static final float cos1_4 = (float)(1.0D / (2.0D * Math.cos(0.7853981633974483D)));
   private static float[] d = null;
   private static float[][] d16 = null;

   public SynthesisFilter(int channelnumber, float factor, float[] eq0) {
      if (d == null) {
         d = load_d();
         d16 = splitArray(d, 16);
      }

      this.v1 = new float[512];
      this.v2 = new float[512];
      this.samples = new float[32];
      this.channel = channelnumber;
      this.scalefactor = factor;
      this.setEQ(this.eq);
      this.reset();
   }

   public void setEQ(float[] eq0) {
      this.eq = eq0;
      if (this.eq == null) {
         this.eq = new float[32];

         for(int i = 0; i < 32; ++i) {
            this.eq[i] = 1.0F;
         }
      }

      if (this.eq.length < 32) {
         throw new IllegalArgumentException("eq0");
      }
   }

   public void reset() {
      int p2;
      for(p2 = 0; p2 < 512; ++p2) {
         this.v1[p2] = this.v2[p2] = 0.0F;
      }

      for(p2 = 0; p2 < 32; ++p2) {
         this.samples[p2] = 0.0F;
      }

      this.actual_v = this.v1;
      this.actual_write_pos = 15;
   }

   public void input_sample(float sample, int subbandnumber) {
      this.samples[subbandnumber] = this.eq[subbandnumber] * sample;
   }

   public void input_samples(float[] s) {
      for(int i = 31; i >= 0; --i) {
         this.samples[i] = s[i] * this.eq[i];
      }

   }

   private void compute_new_v() {
      float new_v31 = 0.0F;
      float new_v30 = 0.0F;
      float new_v29 = 0.0F;
      float new_v28 = 0.0F;
      float new_v27 = 0.0F;
      float new_v26 = 0.0F;
      float new_v25 = 0.0F;
      float new_v24 = 0.0F;
      float new_v23 = 0.0F;
      float new_v22 = 0.0F;
      float new_v21 = 0.0F;
      float new_v20 = 0.0F;
      float new_v19 = 0.0F;
      float new_v18 = 0.0F;
      float new_v17 = 0.0F;
      float new_v16 = 0.0F;
      float new_v15 = 0.0F;
      float new_v14 = 0.0F;
      float new_v13 = 0.0F;
      float new_v12 = 0.0F;
      float new_v11 = 0.0F;
      float new_v10 = 0.0F;
      float new_v9 = 0.0F;
      float new_v8 = 0.0F;
      float new_v7 = 0.0F;
      float new_v6 = 0.0F;
      float new_v5 = 0.0F;
      float new_v4 = 0.0F;
      float new_v3 = 0.0F;
      float new_v2 = 0.0F;
      float new_v1 = 0.0F;
      float new_v0 = 0.0F;
      float[] s = this.samples;
      float s0 = s[0];
      float s1 = s[1];
      float s2 = s[2];
      float s3 = s[3];
      float s4 = s[4];
      float s5 = s[5];
      float s6 = s[6];
      float s7 = s[7];
      float s8 = s[8];
      float s9 = s[9];
      float s10 = s[10];
      float s11 = s[11];
      float s12 = s[12];
      float s13 = s[13];
      float s14 = s[14];
      float s15 = s[15];
      float s16 = s[16];
      float s17 = s[17];
      float s18 = s[18];
      float s19 = s[19];
      float s20 = s[20];
      float s21 = s[21];
      float s22 = s[22];
      float s23 = s[23];
      float s24 = s[24];
      float s25 = s[25];
      float s26 = s[26];
      float s27 = s[27];
      float s28 = s[28];
      float s29 = s[29];
      float s30 = s[30];
      float s31 = s[31];
      float p0 = s0 + s31;
      float p1 = s1 + s30;
      float p2 = s2 + s29;
      float p3 = s3 + s28;
      float p4 = s4 + s27;
      float p5 = s5 + s26;
      float p6 = s6 + s25;
      float p7 = s7 + s24;
      float p8 = s8 + s23;
      float p9 = s9 + s22;
      float p10 = s10 + s21;
      float p11 = s11 + s20;
      float p12 = s12 + s19;
      float p13 = s13 + s18;
      float p14 = s14 + s17;
      float p15 = s15 + s16;
      float pp0 = p0 + p15;
      float pp1 = p1 + p14;
      float pp2 = p2 + p13;
      float pp3 = p3 + p12;
      float pp4 = p4 + p11;
      float pp5 = p5 + p10;
      float pp6 = p6 + p9;
      float pp7 = p7 + p8;
      float pp8 = (p0 - p15) * cos1_32;
      float pp9 = (p1 - p14) * cos3_32;
      float pp10 = (p2 - p13) * cos5_32;
      float pp11 = (p3 - p12) * cos7_32;
      float pp12 = (p4 - p11) * cos9_32;
      float pp13 = (p5 - p10) * cos11_32;
      float pp14 = (p6 - p9) * cos13_32;
      float pp15 = (p7 - p8) * cos15_32;
      p0 = pp0 + pp7;
      p1 = pp1 + pp6;
      p2 = pp2 + pp5;
      p3 = pp3 + pp4;
      p4 = (pp0 - pp7) * cos1_16;
      p5 = (pp1 - pp6) * cos3_16;
      p6 = (pp2 - pp5) * cos5_16;
      p7 = (pp3 - pp4) * cos7_16;
      p8 = pp8 + pp15;
      p9 = pp9 + pp14;
      p10 = pp10 + pp13;
      p11 = pp11 + pp12;
      p12 = (pp8 - pp15) * cos1_16;
      p13 = (pp9 - pp14) * cos3_16;
      p14 = (pp10 - pp13) * cos5_16;
      p15 = (pp11 - pp12) * cos7_16;
      pp0 = p0 + p3;
      pp1 = p1 + p2;
      pp2 = (p0 - p3) * cos1_8;
      pp3 = (p1 - p2) * cos3_8;
      pp4 = p4 + p7;
      pp5 = p5 + p6;
      pp6 = (p4 - p7) * cos1_8;
      pp7 = (p5 - p6) * cos3_8;
      pp8 = p8 + p11;
      pp9 = p9 + p10;
      pp10 = (p8 - p11) * cos1_8;
      pp11 = (p9 - p10) * cos3_8;
      pp12 = p12 + p15;
      pp13 = p13 + p14;
      pp14 = (p12 - p15) * cos1_8;
      pp15 = (p13 - p14) * cos3_8;
      p0 = pp0 + pp1;
      p1 = (pp0 - pp1) * cos1_4;
      p2 = pp2 + pp3;
      p3 = (pp2 - pp3) * cos1_4;
      p4 = pp4 + pp5;
      p5 = (pp4 - pp5) * cos1_4;
      p6 = pp6 + pp7;
      p7 = (pp6 - pp7) * cos1_4;
      p8 = pp8 + pp9;
      p9 = (pp8 - pp9) * cos1_4;
      p10 = pp10 + pp11;
      p11 = (pp10 - pp11) * cos1_4;
      p12 = pp12 + pp13;
      p13 = (pp12 - pp13) * cos1_4;
      p14 = pp14 + pp15;
      p15 = (pp14 - pp15) * cos1_4;
      new_v12 = p7;
      new_v19 = -(new_v4 = p7 + p5) - p6;
      new_v27 = -p6 - p7 - p4;
      new_v14 = p15;
      new_v6 = (new_v10 = p15 + p11) + p13;
      new_v17 = -(new_v2 = p15 + p13 + p9) - p14;
      float tmp1;
      new_v21 = (tmp1 = -p14 - p15 - p10 - p11) - p13;
      new_v29 = -p14 - p15 - p12 - p8;
      new_v25 = tmp1 - p12;
      new_v31 = -p0;
      new_v0 = p1;
      new_v8 = p3;
      new_v23 = -p3 - p2;
      p0 = (s0 - s31) * cos1_64;
      p1 = (s1 - s30) * cos3_64;
      p2 = (s2 - s29) * cos5_64;
      p3 = (s3 - s28) * cos7_64;
      p4 = (s4 - s27) * cos9_64;
      p5 = (s5 - s26) * cos11_64;
      p6 = (s6 - s25) * cos13_64;
      p7 = (s7 - s24) * cos15_64;
      p8 = (s8 - s23) * cos17_64;
      p9 = (s9 - s22) * cos19_64;
      p10 = (s10 - s21) * cos21_64;
      p11 = (s11 - s20) * cos23_64;
      p12 = (s12 - s19) * cos25_64;
      p13 = (s13 - s18) * cos27_64;
      p14 = (s14 - s17) * cos29_64;
      p15 = (s15 - s16) * cos31_64;
      pp0 = p0 + p15;
      pp1 = p1 + p14;
      pp2 = p2 + p13;
      pp3 = p3 + p12;
      pp4 = p4 + p11;
      pp5 = p5 + p10;
      pp6 = p6 + p9;
      pp7 = p7 + p8;
      pp8 = (p0 - p15) * cos1_32;
      pp9 = (p1 - p14) * cos3_32;
      pp10 = (p2 - p13) * cos5_32;
      pp11 = (p3 - p12) * cos7_32;
      pp12 = (p4 - p11) * cos9_32;
      pp13 = (p5 - p10) * cos11_32;
      pp14 = (p6 - p9) * cos13_32;
      pp15 = (p7 - p8) * cos15_32;
      p0 = pp0 + pp7;
      p1 = pp1 + pp6;
      p2 = pp2 + pp5;
      p3 = pp3 + pp4;
      p4 = (pp0 - pp7) * cos1_16;
      p5 = (pp1 - pp6) * cos3_16;
      p6 = (pp2 - pp5) * cos5_16;
      p7 = (pp3 - pp4) * cos7_16;
      p8 = pp8 + pp15;
      p9 = pp9 + pp14;
      p10 = pp10 + pp13;
      p11 = pp11 + pp12;
      p12 = (pp8 - pp15) * cos1_16;
      p13 = (pp9 - pp14) * cos3_16;
      p14 = (pp10 - pp13) * cos5_16;
      p15 = (pp11 - pp12) * cos7_16;
      pp0 = p0 + p3;
      pp1 = p1 + p2;
      pp2 = (p0 - p3) * cos1_8;
      pp3 = (p1 - p2) * cos3_8;
      pp4 = p4 + p7;
      pp5 = p5 + p6;
      pp6 = (p4 - p7) * cos1_8;
      pp7 = (p5 - p6) * cos3_8;
      pp8 = p8 + p11;
      pp9 = p9 + p10;
      pp10 = (p8 - p11) * cos1_8;
      pp11 = (p9 - p10) * cos3_8;
      pp12 = p12 + p15;
      pp13 = p13 + p14;
      pp14 = (p12 - p15) * cos1_8;
      pp15 = (p13 - p14) * cos3_8;
      p0 = pp0 + pp1;
      p1 = (pp0 - pp1) * cos1_4;
      p2 = pp2 + pp3;
      p3 = (pp2 - pp3) * cos1_4;
      p4 = pp4 + pp5;
      p5 = (pp4 - pp5) * cos1_4;
      p6 = pp6 + pp7;
      p7 = (pp6 - pp7) * cos1_4;
      p8 = pp8 + pp9;
      p9 = (pp8 - pp9) * cos1_4;
      p10 = pp10 + pp11;
      p11 = (pp10 - pp11) * cos1_4;
      p12 = pp12 + pp13;
      p13 = (pp12 - pp13) * cos1_4;
      p14 = pp14 + pp15;
      p15 = (pp14 - pp15) * cos1_4;
      new_v5 = (new_v11 = (new_v13 = p15 + p7) + p11) + p5 + p13;
      new_v7 = (new_v9 = p15 + p11 + p3) + p13;
      new_v16 = -(new_v1 = (tmp1 = p13 + p15 + p9) + p1) - p14;
      new_v18 = -(new_v3 = tmp1 + p5 + p7) - p6 - p14;
      new_v22 = (tmp1 = -p10 - p11 - p14 - p15) - p13 - p2 - p3;
      new_v20 = tmp1 - p13 - p5 - p6 - p7;
      new_v24 = tmp1 - p12 - p2 - p3;
      float tmp2;
      new_v26 = tmp1 - p12 - (tmp2 = p4 + p6 + p7);
      new_v30 = (tmp1 = -p8 - p12 - p14 - p15) - p0;
      new_v28 = tmp1 - tmp2;
      float[] dest = this.actual_v;
      int pos = this.actual_write_pos;
      dest[0 + pos] = new_v0;
      dest[16 + pos] = new_v1;
      dest[32 + pos] = new_v2;
      dest[48 + pos] = new_v3;
      dest[64 + pos] = new_v4;
      dest[80 + pos] = new_v5;
      dest[96 + pos] = new_v6;
      dest[112 + pos] = new_v7;
      dest[128 + pos] = new_v8;
      dest[144 + pos] = new_v9;
      dest[160 + pos] = new_v10;
      dest[176 + pos] = new_v11;
      dest[192 + pos] = new_v12;
      dest[208 + pos] = new_v13;
      dest[224 + pos] = new_v14;
      dest[240 + pos] = p15;
      dest[256 + pos] = 0.0F;
      dest[272 + pos] = -p15;
      dest[288 + pos] = -new_v14;
      dest[304 + pos] = -new_v13;
      dest[320 + pos] = -new_v12;
      dest[336 + pos] = -new_v11;
      dest[352 + pos] = -new_v10;
      dest[368 + pos] = -new_v9;
      dest[384 + pos] = -new_v8;
      dest[400 + pos] = -new_v7;
      dest[416 + pos] = -new_v6;
      dest[432 + pos] = -new_v5;
      dest[448 + pos] = -new_v4;
      dest[464 + pos] = -new_v3;
      dest[480 + pos] = -new_v2;
      dest[496 + pos] = -new_v1;
      dest = this.actual_v == this.v1 ? this.v2 : this.v1;
      dest[0 + pos] = -new_v0;
      dest[16 + pos] = new_v16;
      dest[32 + pos] = new_v17;
      dest[48 + pos] = new_v18;
      dest[64 + pos] = new_v19;
      dest[80 + pos] = new_v20;
      dest[96 + pos] = new_v21;
      dest[112 + pos] = new_v22;
      dest[128 + pos] = new_v23;
      dest[144 + pos] = new_v24;
      dest[160 + pos] = new_v25;
      dest[176 + pos] = new_v26;
      dest[192 + pos] = new_v27;
      dest[208 + pos] = new_v28;
      dest[224 + pos] = new_v29;
      dest[240 + pos] = new_v30;
      dest[256 + pos] = new_v31;
      dest[272 + pos] = new_v30;
      dest[288 + pos] = new_v29;
      dest[304 + pos] = new_v28;
      dest[320 + pos] = new_v27;
      dest[336 + pos] = new_v26;
      dest[352 + pos] = new_v25;
      dest[368 + pos] = new_v24;
      dest[384 + pos] = new_v23;
      dest[400 + pos] = new_v22;
      dest[416 + pos] = new_v21;
      dest[432 + pos] = new_v20;
      dest[448 + pos] = new_v19;
      dest[464 + pos] = new_v18;
      dest[480 + pos] = new_v17;
      dest[496 + pos] = new_v16;
   }

   private void compute_new_v_old() {
      float[] new_v = new float[32];
      float[] p = new float[16];
      float[] pp = new float[16];

      for(int i = 31; i >= 0; --i) {
         new_v[i] = 0.0F;
      }

      float[] x1 = this.samples;
      p[0] = x1[0] + x1[31];
      p[1] = x1[1] + x1[30];
      p[2] = x1[2] + x1[29];
      p[3] = x1[3] + x1[28];
      p[4] = x1[4] + x1[27];
      p[5] = x1[5] + x1[26];
      p[6] = x1[6] + x1[25];
      p[7] = x1[7] + x1[24];
      p[8] = x1[8] + x1[23];
      p[9] = x1[9] + x1[22];
      p[10] = x1[10] + x1[21];
      p[11] = x1[11] + x1[20];
      p[12] = x1[12] + x1[19];
      p[13] = x1[13] + x1[18];
      p[14] = x1[14] + x1[17];
      p[15] = x1[15] + x1[16];
      pp[0] = p[0] + p[15];
      pp[1] = p[1] + p[14];
      pp[2] = p[2] + p[13];
      pp[3] = p[3] + p[12];
      pp[4] = p[4] + p[11];
      pp[5] = p[5] + p[10];
      pp[6] = p[6] + p[9];
      pp[7] = p[7] + p[8];
      pp[8] = (p[0] - p[15]) * cos1_32;
      pp[9] = (p[1] - p[14]) * cos3_32;
      pp[10] = (p[2] - p[13]) * cos5_32;
      pp[11] = (p[3] - p[12]) * cos7_32;
      pp[12] = (p[4] - p[11]) * cos9_32;
      pp[13] = (p[5] - p[10]) * cos11_32;
      pp[14] = (p[6] - p[9]) * cos13_32;
      pp[15] = (p[7] - p[8]) * cos15_32;
      p[0] = pp[0] + pp[7];
      p[1] = pp[1] + pp[6];
      p[2] = pp[2] + pp[5];
      p[3] = pp[3] + pp[4];
      p[4] = (pp[0] - pp[7]) * cos1_16;
      p[5] = (pp[1] - pp[6]) * cos3_16;
      p[6] = (pp[2] - pp[5]) * cos5_16;
      p[7] = (pp[3] - pp[4]) * cos7_16;
      p[8] = pp[8] + pp[15];
      p[9] = pp[9] + pp[14];
      p[10] = pp[10] + pp[13];
      p[11] = pp[11] + pp[12];
      p[12] = (pp[8] - pp[15]) * cos1_16;
      p[13] = (pp[9] - pp[14]) * cos3_16;
      p[14] = (pp[10] - pp[13]) * cos5_16;
      p[15] = (pp[11] - pp[12]) * cos7_16;
      pp[0] = p[0] + p[3];
      pp[1] = p[1] + p[2];
      pp[2] = (p[0] - p[3]) * cos1_8;
      pp[3] = (p[1] - p[2]) * cos3_8;
      pp[4] = p[4] + p[7];
      pp[5] = p[5] + p[6];
      pp[6] = (p[4] - p[7]) * cos1_8;
      pp[7] = (p[5] - p[6]) * cos3_8;
      pp[8] = p[8] + p[11];
      pp[9] = p[9] + p[10];
      pp[10] = (p[8] - p[11]) * cos1_8;
      pp[11] = (p[9] - p[10]) * cos3_8;
      pp[12] = p[12] + p[15];
      pp[13] = p[13] + p[14];
      pp[14] = (p[12] - p[15]) * cos1_8;
      pp[15] = (p[13] - p[14]) * cos3_8;
      p[0] = pp[0] + pp[1];
      p[1] = (pp[0] - pp[1]) * cos1_4;
      p[2] = pp[2] + pp[3];
      p[3] = (pp[2] - pp[3]) * cos1_4;
      p[4] = pp[4] + pp[5];
      p[5] = (pp[4] - pp[5]) * cos1_4;
      p[6] = pp[6] + pp[7];
      p[7] = (pp[6] - pp[7]) * cos1_4;
      p[8] = pp[8] + pp[9];
      p[9] = (pp[8] - pp[9]) * cos1_4;
      p[10] = pp[10] + pp[11];
      p[11] = (pp[10] - pp[11]) * cos1_4;
      p[12] = pp[12] + pp[13];
      p[13] = (pp[12] - pp[13]) * cos1_4;
      p[14] = pp[14] + pp[15];
      p[15] = (pp[14] - pp[15]) * cos1_4;
      new_v[19] = -(new_v[4] = (new_v[12] = p[7]) + p[5]) - p[6];
      new_v[27] = -p[6] - p[7] - p[4];
      new_v[6] = (new_v[10] = (new_v[14] = p[15]) + p[11]) + p[13];
      new_v[17] = -(new_v[2] = p[15] + p[13] + p[9]) - p[14];
      float tmp1;
      new_v[21] = (tmp1 = -p[14] - p[15] - p[10] - p[11]) - p[13];
      new_v[29] = -p[14] - p[15] - p[12] - p[8];
      new_v[25] = tmp1 - p[12];
      new_v[31] = -p[0];
      new_v[0] = p[1];
      new_v[23] = -(new_v[8] = p[3]) - p[2];
      p[0] = (x1[0] - x1[31]) * cos1_64;
      p[1] = (x1[1] - x1[30]) * cos3_64;
      p[2] = (x1[2] - x1[29]) * cos5_64;
      p[3] = (x1[3] - x1[28]) * cos7_64;
      p[4] = (x1[4] - x1[27]) * cos9_64;
      p[5] = (x1[5] - x1[26]) * cos11_64;
      p[6] = (x1[6] - x1[25]) * cos13_64;
      p[7] = (x1[7] - x1[24]) * cos15_64;
      p[8] = (x1[8] - x1[23]) * cos17_64;
      p[9] = (x1[9] - x1[22]) * cos19_64;
      p[10] = (x1[10] - x1[21]) * cos21_64;
      p[11] = (x1[11] - x1[20]) * cos23_64;
      p[12] = (x1[12] - x1[19]) * cos25_64;
      p[13] = (x1[13] - x1[18]) * cos27_64;
      p[14] = (x1[14] - x1[17]) * cos29_64;
      p[15] = (x1[15] - x1[16]) * cos31_64;
      pp[0] = p[0] + p[15];
      pp[1] = p[1] + p[14];
      pp[2] = p[2] + p[13];
      pp[3] = p[3] + p[12];
      pp[4] = p[4] + p[11];
      pp[5] = p[5] + p[10];
      pp[6] = p[6] + p[9];
      pp[7] = p[7] + p[8];
      pp[8] = (p[0] - p[15]) * cos1_32;
      pp[9] = (p[1] - p[14]) * cos3_32;
      pp[10] = (p[2] - p[13]) * cos5_32;
      pp[11] = (p[3] - p[12]) * cos7_32;
      pp[12] = (p[4] - p[11]) * cos9_32;
      pp[13] = (p[5] - p[10]) * cos11_32;
      pp[14] = (p[6] - p[9]) * cos13_32;
      pp[15] = (p[7] - p[8]) * cos15_32;
      p[0] = pp[0] + pp[7];
      p[1] = pp[1] + pp[6];
      p[2] = pp[2] + pp[5];
      p[3] = pp[3] + pp[4];
      p[4] = (pp[0] - pp[7]) * cos1_16;
      p[5] = (pp[1] - pp[6]) * cos3_16;
      p[6] = (pp[2] - pp[5]) * cos5_16;
      p[7] = (pp[3] - pp[4]) * cos7_16;
      p[8] = pp[8] + pp[15];
      p[9] = pp[9] + pp[14];
      p[10] = pp[10] + pp[13];
      p[11] = pp[11] + pp[12];
      p[12] = (pp[8] - pp[15]) * cos1_16;
      p[13] = (pp[9] - pp[14]) * cos3_16;
      p[14] = (pp[10] - pp[13]) * cos5_16;
      p[15] = (pp[11] - pp[12]) * cos7_16;
      pp[0] = p[0] + p[3];
      pp[1] = p[1] + p[2];
      pp[2] = (p[0] - p[3]) * cos1_8;
      pp[3] = (p[1] - p[2]) * cos3_8;
      pp[4] = p[4] + p[7];
      pp[5] = p[5] + p[6];
      pp[6] = (p[4] - p[7]) * cos1_8;
      pp[7] = (p[5] - p[6]) * cos3_8;
      pp[8] = p[8] + p[11];
      pp[9] = p[9] + p[10];
      pp[10] = (p[8] - p[11]) * cos1_8;
      pp[11] = (p[9] - p[10]) * cos3_8;
      pp[12] = p[12] + p[15];
      pp[13] = p[13] + p[14];
      pp[14] = (p[12] - p[15]) * cos1_8;
      pp[15] = (p[13] - p[14]) * cos3_8;
      p[0] = pp[0] + pp[1];
      p[1] = (pp[0] - pp[1]) * cos1_4;
      p[2] = pp[2] + pp[3];
      p[3] = (pp[2] - pp[3]) * cos1_4;
      p[4] = pp[4] + pp[5];
      p[5] = (pp[4] - pp[5]) * cos1_4;
      p[6] = pp[6] + pp[7];
      p[7] = (pp[6] - pp[7]) * cos1_4;
      p[8] = pp[8] + pp[9];
      p[9] = (pp[8] - pp[9]) * cos1_4;
      p[10] = pp[10] + pp[11];
      p[11] = (pp[10] - pp[11]) * cos1_4;
      p[12] = pp[12] + pp[13];
      p[13] = (pp[12] - pp[13]) * cos1_4;
      p[14] = pp[14] + pp[15];
      p[15] = (pp[14] - pp[15]) * cos1_4;
      new_v[5] = (new_v[11] = (new_v[13] = (new_v[15] = p[15]) + p[7]) + p[11]) + p[5] + p[13];
      new_v[7] = (new_v[9] = p[15] + p[11] + p[3]) + p[13];
      new_v[16] = -(new_v[1] = (tmp1 = p[13] + p[15] + p[9]) + p[1]) - p[14];
      new_v[18] = -(new_v[3] = tmp1 + p[5] + p[7]) - p[6] - p[14];
      new_v[22] = (tmp1 = -p[10] - p[11] - p[14] - p[15]) - p[13] - p[2] - p[3];
      new_v[20] = tmp1 - p[13] - p[5] - p[6] - p[7];
      new_v[24] = tmp1 - p[12] - p[2] - p[3];
      float tmp2;
      new_v[26] = tmp1 - p[12] - (tmp2 = p[4] + p[6] + p[7]);
      new_v[30] = (tmp1 = -p[8] - p[12] - p[14] - p[15]) - p[0];
      new_v[28] = tmp1 - tmp2;
      float[] dest = this.actual_v;
      dest[0 + this.actual_write_pos] = new_v[0];
      dest[16 + this.actual_write_pos] = new_v[1];
      dest[32 + this.actual_write_pos] = new_v[2];
      dest[48 + this.actual_write_pos] = new_v[3];
      dest[64 + this.actual_write_pos] = new_v[4];
      dest[80 + this.actual_write_pos] = new_v[5];
      dest[96 + this.actual_write_pos] = new_v[6];
      dest[112 + this.actual_write_pos] = new_v[7];
      dest[128 + this.actual_write_pos] = new_v[8];
      dest[144 + this.actual_write_pos] = new_v[9];
      dest[160 + this.actual_write_pos] = new_v[10];
      dest[176 + this.actual_write_pos] = new_v[11];
      dest[192 + this.actual_write_pos] = new_v[12];
      dest[208 + this.actual_write_pos] = new_v[13];
      dest[224 + this.actual_write_pos] = new_v[14];
      dest[240 + this.actual_write_pos] = new_v[15];
      dest[256 + this.actual_write_pos] = 0.0F;
      dest[272 + this.actual_write_pos] = -new_v[15];
      dest[288 + this.actual_write_pos] = -new_v[14];
      dest[304 + this.actual_write_pos] = -new_v[13];
      dest[320 + this.actual_write_pos] = -new_v[12];
      dest[336 + this.actual_write_pos] = -new_v[11];
      dest[352 + this.actual_write_pos] = -new_v[10];
      dest[368 + this.actual_write_pos] = -new_v[9];
      dest[384 + this.actual_write_pos] = -new_v[8];
      dest[400 + this.actual_write_pos] = -new_v[7];
      dest[416 + this.actual_write_pos] = -new_v[6];
      dest[432 + this.actual_write_pos] = -new_v[5];
      dest[448 + this.actual_write_pos] = -new_v[4];
      dest[464 + this.actual_write_pos] = -new_v[3];
      dest[480 + this.actual_write_pos] = -new_v[2];
      dest[496 + this.actual_write_pos] = -new_v[1];
   }

   private void compute_pcm_samples0(Obuffer buffer) {
      float[] vp = this.actual_v;
      float[] tmpOut = this._tmpOut;
      int dvp = 0;

      for(int i = 0; i < 32; ++i) {
         float[] dp = d16[i];
         float pcm_sample = (vp[0 + dvp] * dp[0] + vp[15 + dvp] * dp[1] + vp[14 + dvp] * dp[2] + vp[13 + dvp] * dp[3] + vp[12 + dvp] * dp[4] + vp[11 + dvp] * dp[5] + vp[10 + dvp] * dp[6] + vp[9 + dvp] * dp[7] + vp[8 + dvp] * dp[8] + vp[7 + dvp] * dp[9] + vp[6 + dvp] * dp[10] + vp[5 + dvp] * dp[11] + vp[4 + dvp] * dp[12] + vp[3 + dvp] * dp[13] + vp[2 + dvp] * dp[14] + vp[1 + dvp] * dp[15]) * this.scalefactor;
         tmpOut[i] = pcm_sample;
         dvp += 16;
      }

   }

   private void compute_pcm_samples1(Obuffer buffer) {
      float[] vp = this.actual_v;
      float[] tmpOut = this._tmpOut;
      int dvp = 0;

      for(int i = 0; i < 32; ++i) {
         float[] dp = d16[i];
         float pcm_sample = (vp[1 + dvp] * dp[0] + vp[0 + dvp] * dp[1] + vp[15 + dvp] * dp[2] + vp[14 + dvp] * dp[3] + vp[13 + dvp] * dp[4] + vp[12 + dvp] * dp[5] + vp[11 + dvp] * dp[6] + vp[10 + dvp] * dp[7] + vp[9 + dvp] * dp[8] + vp[8 + dvp] * dp[9] + vp[7 + dvp] * dp[10] + vp[6 + dvp] * dp[11] + vp[5 + dvp] * dp[12] + vp[4 + dvp] * dp[13] + vp[3 + dvp] * dp[14] + vp[2 + dvp] * dp[15]) * this.scalefactor;
         tmpOut[i] = pcm_sample;
         dvp += 16;
      }

   }

   private void compute_pcm_samples2(Obuffer buffer) {
      float[] vp = this.actual_v;
      float[] tmpOut = this._tmpOut;
      int dvp = 0;

      for(int i = 0; i < 32; ++i) {
         float[] dp = d16[i];
         float pcm_sample = (vp[2 + dvp] * dp[0] + vp[1 + dvp] * dp[1] + vp[0 + dvp] * dp[2] + vp[15 + dvp] * dp[3] + vp[14 + dvp] * dp[4] + vp[13 + dvp] * dp[5] + vp[12 + dvp] * dp[6] + vp[11 + dvp] * dp[7] + vp[10 + dvp] * dp[8] + vp[9 + dvp] * dp[9] + vp[8 + dvp] * dp[10] + vp[7 + dvp] * dp[11] + vp[6 + dvp] * dp[12] + vp[5 + dvp] * dp[13] + vp[4 + dvp] * dp[14] + vp[3 + dvp] * dp[15]) * this.scalefactor;
         tmpOut[i] = pcm_sample;
         dvp += 16;
      }

   }

   private void compute_pcm_samples3(Obuffer buffer) {
      float[] vp = this.actual_v;
      int idx = 0;
      float[] tmpOut = this._tmpOut;
      int dvp = 0;

      for(int i = 0; i < 32; ++i) {
         float[] dp = d16[i];
         float pcm_sample = (vp[3 + dvp] * dp[0] + vp[2 + dvp] * dp[1] + vp[1 + dvp] * dp[2] + vp[0 + dvp] * dp[3] + vp[15 + dvp] * dp[4] + vp[14 + dvp] * dp[5] + vp[13 + dvp] * dp[6] + vp[12 + dvp] * dp[7] + vp[11 + dvp] * dp[8] + vp[10 + dvp] * dp[9] + vp[9 + dvp] * dp[10] + vp[8 + dvp] * dp[11] + vp[7 + dvp] * dp[12] + vp[6 + dvp] * dp[13] + vp[5 + dvp] * dp[14] + vp[4 + dvp] * dp[15]) * this.scalefactor;
         tmpOut[i] = pcm_sample;
         dvp += 16;
      }

   }

   private void compute_pcm_samples4(Obuffer buffer) {
      float[] vp = this.actual_v;
      float[] tmpOut = this._tmpOut;
      int dvp = 0;

      for(int i = 0; i < 32; ++i) {
         float[] dp = d16[i];
         float pcm_sample = (vp[4 + dvp] * dp[0] + vp[3 + dvp] * dp[1] + vp[2 + dvp] * dp[2] + vp[1 + dvp] * dp[3] + vp[0 + dvp] * dp[4] + vp[15 + dvp] * dp[5] + vp[14 + dvp] * dp[6] + vp[13 + dvp] * dp[7] + vp[12 + dvp] * dp[8] + vp[11 + dvp] * dp[9] + vp[10 + dvp] * dp[10] + vp[9 + dvp] * dp[11] + vp[8 + dvp] * dp[12] + vp[7 + dvp] * dp[13] + vp[6 + dvp] * dp[14] + vp[5 + dvp] * dp[15]) * this.scalefactor;
         tmpOut[i] = pcm_sample;
         dvp += 16;
      }

   }

   private void compute_pcm_samples5(Obuffer buffer) {
      float[] vp = this.actual_v;
      float[] tmpOut = this._tmpOut;
      int dvp = 0;

      for(int i = 0; i < 32; ++i) {
         float[] dp = d16[i];
         float pcm_sample = (vp[5 + dvp] * dp[0] + vp[4 + dvp] * dp[1] + vp[3 + dvp] * dp[2] + vp[2 + dvp] * dp[3] + vp[1 + dvp] * dp[4] + vp[0 + dvp] * dp[5] + vp[15 + dvp] * dp[6] + vp[14 + dvp] * dp[7] + vp[13 + dvp] * dp[8] + vp[12 + dvp] * dp[9] + vp[11 + dvp] * dp[10] + vp[10 + dvp] * dp[11] + vp[9 + dvp] * dp[12] + vp[8 + dvp] * dp[13] + vp[7 + dvp] * dp[14] + vp[6 + dvp] * dp[15]) * this.scalefactor;
         tmpOut[i] = pcm_sample;
         dvp += 16;
      }

   }

   private void compute_pcm_samples6(Obuffer buffer) {
      float[] vp = this.actual_v;
      float[] tmpOut = this._tmpOut;
      int dvp = 0;

      for(int i = 0; i < 32; ++i) {
         float[] dp = d16[i];
         float pcm_sample = (vp[6 + dvp] * dp[0] + vp[5 + dvp] * dp[1] + vp[4 + dvp] * dp[2] + vp[3 + dvp] * dp[3] + vp[2 + dvp] * dp[4] + vp[1 + dvp] * dp[5] + vp[0 + dvp] * dp[6] + vp[15 + dvp] * dp[7] + vp[14 + dvp] * dp[8] + vp[13 + dvp] * dp[9] + vp[12 + dvp] * dp[10] + vp[11 + dvp] * dp[11] + vp[10 + dvp] * dp[12] + vp[9 + dvp] * dp[13] + vp[8 + dvp] * dp[14] + vp[7 + dvp] * dp[15]) * this.scalefactor;
         tmpOut[i] = pcm_sample;
         dvp += 16;
      }

   }

   private void compute_pcm_samples7(Obuffer buffer) {
      float[] vp = this.actual_v;
      float[] tmpOut = this._tmpOut;
      int dvp = 0;

      for(int i = 0; i < 32; ++i) {
         float[] dp = d16[i];
         float pcm_sample = (vp[7 + dvp] * dp[0] + vp[6 + dvp] * dp[1] + vp[5 + dvp] * dp[2] + vp[4 + dvp] * dp[3] + vp[3 + dvp] * dp[4] + vp[2 + dvp] * dp[5] + vp[1 + dvp] * dp[6] + vp[0 + dvp] * dp[7] + vp[15 + dvp] * dp[8] + vp[14 + dvp] * dp[9] + vp[13 + dvp] * dp[10] + vp[12 + dvp] * dp[11] + vp[11 + dvp] * dp[12] + vp[10 + dvp] * dp[13] + vp[9 + dvp] * dp[14] + vp[8 + dvp] * dp[15]) * this.scalefactor;
         tmpOut[i] = pcm_sample;
         dvp += 16;
      }

   }

   private void compute_pcm_samples8(Obuffer buffer) {
      float[] vp = this.actual_v;
      float[] tmpOut = this._tmpOut;
      int dvp = 0;

      for(int i = 0; i < 32; ++i) {
         float[] dp = d16[i];
         float pcm_sample = (vp[8 + dvp] * dp[0] + vp[7 + dvp] * dp[1] + vp[6 + dvp] * dp[2] + vp[5 + dvp] * dp[3] + vp[4 + dvp] * dp[4] + vp[3 + dvp] * dp[5] + vp[2 + dvp] * dp[6] + vp[1 + dvp] * dp[7] + vp[0 + dvp] * dp[8] + vp[15 + dvp] * dp[9] + vp[14 + dvp] * dp[10] + vp[13 + dvp] * dp[11] + vp[12 + dvp] * dp[12] + vp[11 + dvp] * dp[13] + vp[10 + dvp] * dp[14] + vp[9 + dvp] * dp[15]) * this.scalefactor;
         tmpOut[i] = pcm_sample;
         dvp += 16;
      }

   }

   private void compute_pcm_samples9(Obuffer buffer) {
      float[] vp = this.actual_v;
      float[] tmpOut = this._tmpOut;
      int dvp = 0;

      for(int i = 0; i < 32; ++i) {
         float[] dp = d16[i];
         float pcm_sample = (vp[9 + dvp] * dp[0] + vp[8 + dvp] * dp[1] + vp[7 + dvp] * dp[2] + vp[6 + dvp] * dp[3] + vp[5 + dvp] * dp[4] + vp[4 + dvp] * dp[5] + vp[3 + dvp] * dp[6] + vp[2 + dvp] * dp[7] + vp[1 + dvp] * dp[8] + vp[0 + dvp] * dp[9] + vp[15 + dvp] * dp[10] + vp[14 + dvp] * dp[11] + vp[13 + dvp] * dp[12] + vp[12 + dvp] * dp[13] + vp[11 + dvp] * dp[14] + vp[10 + dvp] * dp[15]) * this.scalefactor;
         tmpOut[i] = pcm_sample;
         dvp += 16;
      }

   }

   private void compute_pcm_samples10(Obuffer buffer) {
      float[] vp = this.actual_v;
      float[] tmpOut = this._tmpOut;
      int dvp = 0;

      for(int i = 0; i < 32; ++i) {
         float[] dp = d16[i];
         float pcm_sample = (vp[10 + dvp] * dp[0] + vp[9 + dvp] * dp[1] + vp[8 + dvp] * dp[2] + vp[7 + dvp] * dp[3] + vp[6 + dvp] * dp[4] + vp[5 + dvp] * dp[5] + vp[4 + dvp] * dp[6] + vp[3 + dvp] * dp[7] + vp[2 + dvp] * dp[8] + vp[1 + dvp] * dp[9] + vp[0 + dvp] * dp[10] + vp[15 + dvp] * dp[11] + vp[14 + dvp] * dp[12] + vp[13 + dvp] * dp[13] + vp[12 + dvp] * dp[14] + vp[11 + dvp] * dp[15]) * this.scalefactor;
         tmpOut[i] = pcm_sample;
         dvp += 16;
      }

   }

   private void compute_pcm_samples11(Obuffer buffer) {
      float[] vp = this.actual_v;
      float[] tmpOut = this._tmpOut;
      int dvp = 0;

      for(int i = 0; i < 32; ++i) {
         float[] dp = d16[i];
         float pcm_sample = (vp[11 + dvp] * dp[0] + vp[10 + dvp] * dp[1] + vp[9 + dvp] * dp[2] + vp[8 + dvp] * dp[3] + vp[7 + dvp] * dp[4] + vp[6 + dvp] * dp[5] + vp[5 + dvp] * dp[6] + vp[4 + dvp] * dp[7] + vp[3 + dvp] * dp[8] + vp[2 + dvp] * dp[9] + vp[1 + dvp] * dp[10] + vp[0 + dvp] * dp[11] + vp[15 + dvp] * dp[12] + vp[14 + dvp] * dp[13] + vp[13 + dvp] * dp[14] + vp[12 + dvp] * dp[15]) * this.scalefactor;
         tmpOut[i] = pcm_sample;
         dvp += 16;
      }

   }

   private void compute_pcm_samples12(Obuffer buffer) {
      float[] vp = this.actual_v;
      float[] tmpOut = this._tmpOut;
      int dvp = 0;

      for(int i = 0; i < 32; ++i) {
         float[] dp = d16[i];
         float pcm_sample = (vp[12 + dvp] * dp[0] + vp[11 + dvp] * dp[1] + vp[10 + dvp] * dp[2] + vp[9 + dvp] * dp[3] + vp[8 + dvp] * dp[4] + vp[7 + dvp] * dp[5] + vp[6 + dvp] * dp[6] + vp[5 + dvp] * dp[7] + vp[4 + dvp] * dp[8] + vp[3 + dvp] * dp[9] + vp[2 + dvp] * dp[10] + vp[1 + dvp] * dp[11] + vp[0 + dvp] * dp[12] + vp[15 + dvp] * dp[13] + vp[14 + dvp] * dp[14] + vp[13 + dvp] * dp[15]) * this.scalefactor;
         tmpOut[i] = pcm_sample;
         dvp += 16;
      }

   }

   private void compute_pcm_samples13(Obuffer buffer) {
      float[] vp = this.actual_v;
      float[] tmpOut = this._tmpOut;
      int dvp = 0;

      for(int i = 0; i < 32; ++i) {
         float[] dp = d16[i];
         float pcm_sample = (vp[13 + dvp] * dp[0] + vp[12 + dvp] * dp[1] + vp[11 + dvp] * dp[2] + vp[10 + dvp] * dp[3] + vp[9 + dvp] * dp[4] + vp[8 + dvp] * dp[5] + vp[7 + dvp] * dp[6] + vp[6 + dvp] * dp[7] + vp[5 + dvp] * dp[8] + vp[4 + dvp] * dp[9] + vp[3 + dvp] * dp[10] + vp[2 + dvp] * dp[11] + vp[1 + dvp] * dp[12] + vp[0 + dvp] * dp[13] + vp[15 + dvp] * dp[14] + vp[14 + dvp] * dp[15]) * this.scalefactor;
         tmpOut[i] = pcm_sample;
         dvp += 16;
      }

   }

   private void compute_pcm_samples14(Obuffer buffer) {
      float[] vp = this.actual_v;
      float[] tmpOut = this._tmpOut;
      int dvp = 0;

      for(int i = 0; i < 32; ++i) {
         float[] dp = d16[i];
         float pcm_sample = (vp[14 + dvp] * dp[0] + vp[13 + dvp] * dp[1] + vp[12 + dvp] * dp[2] + vp[11 + dvp] * dp[3] + vp[10 + dvp] * dp[4] + vp[9 + dvp] * dp[5] + vp[8 + dvp] * dp[6] + vp[7 + dvp] * dp[7] + vp[6 + dvp] * dp[8] + vp[5 + dvp] * dp[9] + vp[4 + dvp] * dp[10] + vp[3 + dvp] * dp[11] + vp[2 + dvp] * dp[12] + vp[1 + dvp] * dp[13] + vp[0 + dvp] * dp[14] + vp[15 + dvp] * dp[15]) * this.scalefactor;
         tmpOut[i] = pcm_sample;
         dvp += 16;
      }

   }

   private void compute_pcm_samples15(Obuffer buffer) {
      float[] vp = this.actual_v;
      float[] tmpOut = this._tmpOut;
      int dvp = 0;

      for(int i = 0; i < 32; ++i) {
         float[] dp = d16[i];
         float pcm_sample = (vp[15 + dvp] * dp[0] + vp[14 + dvp] * dp[1] + vp[13 + dvp] * dp[2] + vp[12 + dvp] * dp[3] + vp[11 + dvp] * dp[4] + vp[10 + dvp] * dp[5] + vp[9 + dvp] * dp[6] + vp[8 + dvp] * dp[7] + vp[7 + dvp] * dp[8] + vp[6 + dvp] * dp[9] + vp[5 + dvp] * dp[10] + vp[4 + dvp] * dp[11] + vp[3 + dvp] * dp[12] + vp[2 + dvp] * dp[13] + vp[1 + dvp] * dp[14] + vp[0 + dvp] * dp[15]) * this.scalefactor;
         tmpOut[i] = pcm_sample;
         dvp += 16;
      }

   }

   private void compute_pcm_samples(Obuffer buffer) {
      switch(this.actual_write_pos) {
      case 0:
         this.compute_pcm_samples0(buffer);
         break;
      case 1:
         this.compute_pcm_samples1(buffer);
         break;
      case 2:
         this.compute_pcm_samples2(buffer);
         break;
      case 3:
         this.compute_pcm_samples3(buffer);
         break;
      case 4:
         this.compute_pcm_samples4(buffer);
         break;
      case 5:
         this.compute_pcm_samples5(buffer);
         break;
      case 6:
         this.compute_pcm_samples6(buffer);
         break;
      case 7:
         this.compute_pcm_samples7(buffer);
         break;
      case 8:
         this.compute_pcm_samples8(buffer);
         break;
      case 9:
         this.compute_pcm_samples9(buffer);
         break;
      case 10:
         this.compute_pcm_samples10(buffer);
         break;
      case 11:
         this.compute_pcm_samples11(buffer);
         break;
      case 12:
         this.compute_pcm_samples12(buffer);
         break;
      case 13:
         this.compute_pcm_samples13(buffer);
         break;
      case 14:
         this.compute_pcm_samples14(buffer);
         break;
      case 15:
         this.compute_pcm_samples15(buffer);
      }

      if (buffer != null) {
         buffer.appendSamples(this.channel, this._tmpOut);
      }

   }

   public void calculate_pcm_samples(Obuffer buffer) {
      this.compute_new_v();
      this.compute_pcm_samples(buffer);
      this.actual_write_pos = this.actual_write_pos + 1 & 15;
      this.actual_v = this.actual_v == this.v1 ? this.v2 : this.v1;

      for(int p = 0; p < 32; ++p) {
         this.samples[p] = 0.0F;
      }

   }

   private static float[] load_d() {
      try {
         Class elemType = Float.TYPE;
         Object o = JavaLayerUtils.deserializeArrayResource("sfd.ser", elemType, 512);
         return (float[])o;
      } catch (IOException var2) {
         throw new ExceptionInInitializerError(var2);
      }
   }

   private static float[][] splitArray(float[] array, int blockSize) {
      int size = array.length / blockSize;
      float[][] split = new float[size][];

      for(int i = 0; i < size; ++i) {
         split[i] = subArray(array, i * blockSize, blockSize);
      }

      return split;
   }

   private static float[] subArray(float[] array, int offs, int len) {
      if (offs + len > array.length) {
         len = array.length - offs;
      }

      if (len < 0) {
         len = 0;
      }

      float[] subarray = new float[len];

      for(int i = 0; i < len; ++i) {
         subarray[i] = array[offs + i];
      }

      return subarray;
   }
}
