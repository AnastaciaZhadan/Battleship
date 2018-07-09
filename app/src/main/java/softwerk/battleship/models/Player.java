package softwerk.battleship.models;

import java.util.List;

import softwerk.battleship.helpers.PlayerResponse;
import softwerk.battleship.helpers.ShipSize;
import softwerk.battleship.helpers.ShipType;
import softwerk.battleship.helpers.ShipsCount;

/**
 * Created by Anastacia on 30.06.2018.
 */
public class Player {
    private int id;
    private PlayerBoard ownBoard;
    private OpponentBoard opponentBoard;
    private int lives;

    public Player(int playerId){
        this.id = playerId;
        ownBoard = new PlayerBoard();
        opponentBoard = new OpponentBoard();

        lives = 0;
        for (ShipType type: ShipType.values()) {
            lives += ShipSize.getShipSize(type) * ShipsCount.getShipsCount(type);
        }
    }

    public int getPlayerId(){
        return id;
    }

    public int getPlayerLives(){
        return lives;
    }

    public OpponentBoard getOpponentBoard(){
        return opponentBoard;
    }

    public PlayerBoard getPlayerBoard(){
        return ownBoard;
    }

    public boolean placeShip(ShipType shipType, int x, int y, boolean direction){
        return ownBoard.placeShip(shipType, new Cell(x,y), direction);
    }

    public PlayerResponse getShot(int x, int y){
        PlayerResponse shotResult = ownBoard.getShot(new Cell(x, y));
        if(shotResult == PlayerResponse.HIT || shotResult == PlayerResponse.KILL)
            lives--;
        return shotResult;
    }

    public List<Cell> updateBoard(int x, int y, PlayerResponse response){
        return opponentBoard.updateBoard(x, y, response);
    }
}
