package ca.ualberta.t14.gametrader;

/**
 * Created by kobitoko on 06/11/15.
 */
public class UserSingleton {
    //Lazy init taken from https://en.wikipedia.org/wiki/Singleton_pattern#Lazy_initialization
    private static volatile UserSingleton instance;
    private User u;
    private UserSingleton() { u = new User(); }


    public static UserSingleton getInstance() {
        if(instance == null) {
            synchronized (UserSingleton.class) {
                if(instance == null) {
                    instance = new UserSingleton();
                }
            }
        }
        return instance;
    }

    public void setUser(User user) { u = user; }

    public User getUser() { return u; }

}
