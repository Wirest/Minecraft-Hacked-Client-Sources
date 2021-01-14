package info.sigmaclient.management;

public class StatManager {
    private static StatManager instance;
    private int connectionCount = 0;

    public static StatManager getInstance() {
        if (instance == null) {
            instance = new StatManager();
        }
        return instance;
    }

    public void onConnection() {
        connectionCount++;
    }
}
