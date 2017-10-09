package schmitt.mmas.aco;

public interface VRPListener {

    void onBestRouteFound(int[] route, double cost);

}
