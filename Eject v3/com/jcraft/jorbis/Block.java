package com.jcraft.jorbis;

import com.jcraft.jogg.Buffer;
import com.jcraft.jogg.Packet;

public class Block {
    float[][] pcm = new float[0][];
    Buffer opb = new Buffer();
    int lW;
    int W;
    int nW;
    int pcmend;
    int mode;
    int eofflag;
    long granulepos;
    long sequence;
    DspState vd;
    int glue_bits;
    int time_bits;
    int floor_bits;
    int res_bits;

    public Block(DspState paramDspState) {
        this.vd = paramDspState;
        if (paramDspState.analysisp != 0) {
            this.opb.writeinit();
        }
    }

    public void init(DspState paramDspState) {
        this.vd = paramDspState;
    }

    public int clear() {
        if ((this.vd != null) && (this.vd.analysisp != 0)) {
            this.opb.writeclear();
        }
        return 0;
    }

    public int synthesis(Packet paramPacket) {
        Info localInfo = this.vd.vi;
        this.opb.readinit(paramPacket.packet_base, paramPacket.packet, paramPacket.bytes);
        if (this.opb.read(1) != 0) {
            return -1;
        }
        int i = this.opb.read(this.vd.modebits);
        if (i == -1) {
            return -1;
        }
        this.mode = i;
        this.W = localInfo.mode_param[this.mode].blockflag;
        if (this.W != 0) {
            this.lW = this.opb.read(1);
            this.nW = this.opb.read(1);
            if (this.nW == -1) {
                return -1;
            }
        } else {
            this.lW = 0;
            this.nW = 0;
        }
        this.granulepos = paramPacket.granulepos;
        this.sequence = (paramPacket.packetno - 3L);
        this.eofflag = paramPacket.e_o_s;
        this.pcmend = localInfo.blocksizes[this.W];
        if (this.pcm.length < localInfo.channels) {
            this.pcm = new float[localInfo.channels][];
        }
        for (int j = 0; j < localInfo.channels; j++) {
            if ((this.pcm[j] == null) || (this.pcm[j].length < this.pcmend)) {
                this.pcm[j] = new float[this.pcmend];
            } else {
                for (int k = 0; k < this.pcmend; k++) {
                    this.pcm[j][k] = 0.0F;
                }
            }
        }
        j = localInfo.map_type[localInfo.mode_param[this.mode].mapping];
        return FuncMapping.mapping_P[j].inverse(this, this.vd.mode[this.mode]);
    }
}




