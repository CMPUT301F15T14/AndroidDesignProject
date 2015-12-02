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
        String id = prefixName + "_" + userToGetInstallationId.getAndroidID() + "_" + managerAdderIdCount.toString();
        managerAdderIdCount += 1;
        return id;
    }

}
