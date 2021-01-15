package viamcp.exemple;

import com.github.creeper123123321.viafabric.ViaFabric;

public class MainExemple {

    public static MainExemple instance = new MainExemple();

/*-----------------------------------------------------------------*/
// You need to initialize viaFabric this way in your main class.
/*-----------------------------------------------------------------*/

    public void startClient() {

        /* ViaVersion */
        try {
            new ViaFabric().onInitialize();
        } catch (Exception e) {
            e.printStackTrace();
        }
        /* ---------- */

    }

    public void stopClient() {

    }

}
