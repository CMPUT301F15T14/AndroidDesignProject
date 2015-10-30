package ca.ualberta.t14.gametrader;

/**
 * Implementing this would make the implementing class observable which then can
 * notify each observer that it was updated.
 * @author Ryan Satyabrata
 */
public interface AppObservable {
    /**
     * Adds the ability to be able to add observers who observe the class.
     * @param observer The observer to be added which wants to be notified on the update.
     */
    public void addObserver(AppObserver observer);
}
