package ca.ualberta.t14.gametrader;

import android.test.ActivityInstrumentationTestCase2;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by jjohnsto on 11/24/15.
 */
public class NetworkTest extends ActivityInstrumentationTestCase2 {
    public NetworkTest(){super(ca.ualberta.t14.gametrader.MainActivity.class);}

    public void testSearchUsers() {
        NetworkController nc = new NetworkController(getActivity().getApplicationContext());

        try {
            ArrayList<User> searchUsers = nc.searchByUserName("Fred");
            assertFalse(searchUsers.isEmpty());
            assertEquals(searchUsers.get(0).getUserName(), "Fred");
            searchUsers = nc.searchByUserName("DoesNotExist");
            assertTrue(searchUsers.isEmpty());

        }
        catch(IOException e){
            e.printStackTrace();
        }

    }

    public void testCreateUser() {
        NetworkController netCtrl = new NetworkController(getActivity().getApplicationContext());

        User testUser = new User();
        testUser.setAddress("The Land of Oz2");
        testUser.setPhoneNumber("555-555-5555");
        testUser.setEmail("PotionMaster@oz.com");
        testUser.setUserName("Fred");
        testUser.setAndroidID("testusr");

        testUser.addObserver(netCtrl);

        Inventory testInv = new Inventory();
        testUser.setInventory(testInv);
        Game testGame = new Game("The Walking Dead");
        testGame.setShared(true);
        testInv.add(testGame);


        try {
            netCtrl.addUser(testUser);
        }
        catch(IllegalStateException e) {
            e.printStackTrace();
        }
        catch(IOException e) {
            e.printStackTrace();
        }

        User testLoadUser = netCtrl.loadUser("testusr");

        assertFalse(testLoadUser == null);
        assertEquals(testLoadUser.getAddress(), "The Land of Oz2");
        assertEquals(testLoadUser.getPhoneNumber(), "555-555-5555");
        assertEquals(testLoadUser.getEmail(), "PotionMaster@oz.com");
        assertEquals(testLoadUser.getUserName(), "Fred");
    }
}
