package ca.ualberta.t14.gametrader;

/**
 * Created by satyabra on 11/1/15.
 */
public class GameController {

    private Game model;

    public GameController() {
        model = new Game();
        //model.loadJson();//???
    }

    public Game createGame() {
        model = new Game();
        return model;
    }

}
