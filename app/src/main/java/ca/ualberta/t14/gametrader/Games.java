package ca.ualberta.t14.gametrader;

import java.util.ArrayList;

/**
 * Created by michaelximac on 2015-11-25.
 */
public class Games extends ArrayList<Game> implements AppObservable{
    private volatile ArrayList<AppObserver> observers = new ArrayList<AppObserver>();
    private static final String RESOURCE_URL = "http://cmput301.softwareprocess.es:8080/cmput301f15t14/game";
    private static final String SEARCH_URL = "http://cmput301.softwareprocess.es:8080/cmput301f15t14/game/_search";

    @Override
    public void addObserver(AppObserver o) {
        observers.add(o);
    }

    @Override
    public void deleteObserver(AppObserver o) {
        observers.remove(o);
    }

    @Override
    public void notifyAllObservers() {
        for (AppObserver o : observers) {
            o.appNotify(this);
        }
    }

    public String getResourceUrl() {
        return RESOURCE_URL;
    }

    public String getSearchUrl() {
        return SEARCH_URL;
    }

    /**
     * Java wants this, we don't need it for Gson/Json
     */
    private static final long serialVersionUID = 3199561696102797345L;
}
