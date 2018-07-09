package softwerk.battleship.models;

import java.util.ArrayList;
import java.util.List;

import softwerk.battleship.helpers.PlayerResponse;
import softwerk.battleship.helpers.ShipType;

/**
 * Created by Anastacia on 02.07.2018.
 */
public class Game {
    private List<Player> players;
    private Player currentPlayer;
    private Player opponentPlayer;

    public Game(){
        players = new ArrayList<>();
        players.add(new Player(0));
        players.add(new ComputerPlayer(1));
        ((ComputerPlayer) getPlayerById(1)).setupBoardRandomly();
    }

    public void setCurrentPlayer(int playerId){
        currentPlayer = getPlayerById(playerId);
    }

    public void setOpponentPlayer(int playerId){
        opponentPlayer = getPlayerById(playerId);
    }

    public Player getCurrentPlayer(){
        return currentPlayer;
    }

    public Player getOpponentPlayer(){
        return opponentPlayer;
    }

    public Player getPlayerById(int playerId){
        for(Player player:players){
            if(player.getPlayerId() == playerId)
                return player;
        }
        return null;
        //return players.stream().filter(p -> p.getPlayerId() == playerId).findFirst().get();
    }

    public boolean setShip(Player player, ShipType shipType, int x, int y, boolean direction){
        return player.placeShip(shipType, x, y, direction);
    }

    public List<Cell> shoot(int x, int y){
        PlayerResponse shotResult = opponentPlayer.getShot(x, y);
        List<Cell> result = currentPlayer.updateBoard(x, y, shotResult);
        if(currentPlayer instanceof ComputerPlayer) {
            switch (shotResult){
                case HIT:
                    ((ComputerPlayer) currentPlayer).setLastSuccessfulShot(new Cell(x, y));
                    break;
                case KILL:
                    ((ComputerPlayer) currentPlayer).setLastSuccessfulShot(null);
            }
        }
        return result;
    }

    public boolean isGameOver(){
        for (Player player:players) {
            if(player.getPlayerLives() == 0)
                return true;
        }
        return false;
    }

    public List <Player> getWinner(){
        List<Player> winners = new ArrayList<>();
        for (Player player:players) {
            if(player.getPlayerLives() != 0)
                winners.add(player);
        }
        return winners;
    }
}
