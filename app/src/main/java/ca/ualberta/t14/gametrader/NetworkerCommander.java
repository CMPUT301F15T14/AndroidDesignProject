package ca.ualberta.t14.gametrader;

/**
 * This interface is for a Networker class to use.
 * It is intended to be used so that the NetworkController can subscribe to the Networker
 * class and have this subscriber have it's methods invoked by a command.
 * @author Ryan Satyabrata
 */
public interface NetworkerCommander {

    /**
     * Enables to subscribe to the Networker class.
     * @param listener
     */
    public void addListener(NetworkerListener listener);

    /**
     * Enables removing listeners from the Networker class.
     * @param listener
     */
    public void deletelisteners(NetworkerListener listener);

    /**
     * The method that calls all listeners/subscribers and give them a command code.
     * @param commandCode
     */
    public void notifyAllListeners(int commandCode);
}
