package ca.ualberta.t14.gametrader;

import java.util.ArrayList;

/**
 * Created by michaelximac on 2015-11-01.
 */
public class Inventory {
    public ArrayList<Game> gameCollections;
    private User owner=new User();
    public Inventory(){
        this.gameCollections=new ArrayList<Game>();
    }
    private InventoryController control=new InventoryController(this);

    public void add(Game game){
        control.addItem(game);
    }
    
    public void remove(Game game){
        control.removeItem(game);
    }

    public boolean contains(Game game){
        return gameCollections.contains(game);
    }

}
