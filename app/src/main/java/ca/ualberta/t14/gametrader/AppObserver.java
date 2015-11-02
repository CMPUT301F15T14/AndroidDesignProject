package ca.ualberta.t14.gametrader;

/**
 * This interface adds the ability to be notified on updates, Makes the implementing class an observer..
 * @author Ryan Satyabrata
 */
public interface AppObserver {
    /**
     * able to receive updates from the observing object.
     * @param observable contains the object that is being observed by this class.
     */
    public void appNotify(AppObservable observable);
}
