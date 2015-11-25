package ca.ualberta.t14.gametrader;

/**
 * This class is the base for the managers, should implement the assigning and keeping track of unique id
 * and hav a list array of object or string.....
 * Created by satyabra on 11/25/15.
 */
public class Manager {

    Integer managerAdderIdCount;

    public Manager() {
        managerAdderIdCount = 0;
    }

    String addItemToTrack(User userToGetInstallationId, String prefixName) {
        // Name Format: <prefix>_<InstId>_<itemId>
        // TODO: add the item to elastic search and/or to the stack that will push to elastic search when able to connect to the internet.
        String id = prefixName + "_" + userToGetInstallationId.getInstallationId() + "_" + managerAdderIdCount.toString();
        managerAdderIdCount += 1;
        return id;
    }

    /**
     * To be used in e.g. remove game, so it will be removed from the tracked list. (and elastic search)
     * @param id the key string identifying the object to be removed.
     * @return True if the key and item was removed. False if that key entry didn't exist in the first place.
     */
    Boolean removeItemToTrack(String id) {
        // TODO: remove the item from elastic search and/or from the stack that will push to elastic search when able to connect to the internet.
        return Boolean.FALSE;
    }

}
