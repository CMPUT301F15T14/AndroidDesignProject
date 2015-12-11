package ca.ualberta.t14.gametrader;

/**
 * To be used by something that wants to subscribe to a Networker.
 * Such as the NetworkController.
 * It handles here the commands it received from the Networker.
 * @author Ryan Satybrata
 */
public interface NetworkerListener {
    /**
     * The method that receives and perhaps handles the commands from the classes it subscribed to.
     * @param commandRequest
     */
    public void netListenerNotify(int commandRequest);
}
