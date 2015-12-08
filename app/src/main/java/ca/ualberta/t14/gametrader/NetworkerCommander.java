package ca.ualberta.t14.gametrader;

/**
 * Created by Ryan on 2015-12-02.
 */
public interface NetworkerCommander {

    public void addListener(NetworkerListener listener);

    public void deletelisteners(NetworkerListener listener);

    public void notifyAllListeners(int commandCode);
}
