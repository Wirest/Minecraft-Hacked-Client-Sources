package info.sigmaclient.management.users;

public class TTSystem {
    private byte[] tattoo;
    private static TTSystem instance = null;

    public TTSystem() {
        tattoo = new byte[]{0x6C, 0x69, 0x76, 0x65, 0x20, 0x64, 0x65, 0x76, 0x20, 0x6C, 0x69, 0x76, 0x65, 0x20, 0x64, 0x65, 0x76, 0x20, 0x6C, 0x69, 0x76, 0x65, 0x20, 0x64};
    }

    public static TTSystem getInstance() {
        if (instance == null) {
            instance = new TTSystem();
        }
        return instance;
    }

    public byte[] getTT() {
        return tattoo;
    }
}
