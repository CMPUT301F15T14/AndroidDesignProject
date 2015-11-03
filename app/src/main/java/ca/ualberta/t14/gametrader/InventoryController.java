package ca.ualberta.t14.gametrader;

/**
 * Created by michaelximac on 2015-11-01.
 */
public class InventoryController {
    private Inventory stock;
    public InventoryController(Inventory inventory,User owner){
        this.stock=inventory;
        stock.setOwner(owner);
    }
    public void addItem(Game game){
        stock.add(game);
    }

    public void removeItem(Game game){
        stock.remove(game);
    }

    public User identifyOwner(){
        return stock.getOwner();
    }

    public void clearInventory(){
        stock.clear();
    }
    public boolean contains(Game game){
        return stock.contains(game);
    }

}
