package ca.ualberta.t14.gametrader;

/**
 * Created by michaelximac on 2015-11-01.
 */
public class InventoryController {
    private Inventory stock;
    public InventoryController(Inventory inventory){
        this.stock=inventory;
    }
    public void addItem(Game game1){
        stock.gameCollections.add(game1);
    }

}
