package ca.ualberta.t14.gametrader;

import java.util.HashMap;

/**
 * Created by kobitoko on 06/11/15.
 */
public class ObjParseSingleton {
    //Lazy init taken from https://en.wikipedia.org/wiki/Singleton_pattern#Lazy_initialization
    private static volatile ObjParseSingleton instance;
    private HashMap<String, Object> objectStorage;
    private ObjParseSingleton() { objectStorage = new HashMap<String, Object>(); }


    public static ObjParseSingleton getInstance() {
        if(instance == null) {
            synchronized (ObjParseSingleton.class) {
                if(instance == null) {
                    instance = new ObjParseSingleton();
                }
            }
        }
        return instance;
    }

    public void addObject(String idName, Object objToParse) {
        objectStorage.put(idName, objToParse);
    }

    public Object popObject(String idName) {
        Object obj = objectStorage.get(idName);
        objectStorage.remove(idName);
        return obj;
    }

    public Object getObject(String idName) {
        return objectStorage.get(idName);
    }

}
