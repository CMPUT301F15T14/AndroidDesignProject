package ca.ualberta.t14.gametrader;

import java.util.ArrayList;

/**
 * Created by michaelximac on 2015-11-01.
 */
public class Inventory {
    private ArrayList<Game> gameCollections;
    private User owner;
    public Inventory(){
        this.gameCollections=new ArrayList<Game>();
    }

    public void add(Game game){
        gameCollections.add(game);;
    }
    
    public void remove(Game game){
        gameCollections.remove(game);
    }

    public void clear(){
        gameCollections.clear();
    }

    public boolean contains(Game game){
        return gameCollections.contains(game);
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public User getOwner() {
        return owner;
    }

    public ArrayList<Game> getAllGames() {
        return gameCollections;
    }
}
