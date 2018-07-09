package softwerk.battleship.models;

import java.util.ArrayList;
import java.util.List;

import softwerk.battleship.helpers.CellState;
import softwerk.battleship.helpers.Constants;
import softwerk.battleship.helpers.PlayerResponse;
import softwerk.battleship.helpers.ShipSize;
import softwerk.battleship.helpers.ShipType;

/**
 * Created by Anastacia on 03.07.2018.
 */
public class PlayerBoard extends GameBoard {
    private List<Ship> ships;

    public PlayerBoard(){
        super();
        ships = new ArrayList<>();
    }

    /**
     * Creates ship, assignes cells to ship and ship to its cells.
     * Returns true, if ship does not fit the edge of board or if it crosses other ship area.
     *
     * @param shipType type of ship to define size
     * @param firstCell first cell of ship
     * @param direction horizontal or vertical ship direction
     * @return true if placed, false otherwise
     */
    public boolean placeShip(ShipType shipType, Cell firstCell, boolean direction){//direction: 0 - horizontal, 1 - vertical
        if(!direction & (firstCell.getX()+ ShipSize.getShipSize(shipType) > Constants.BOARD_WIDTH)
                || direction & (firstCell.getY()+ShipSize.getShipSize(shipType) > Constants.BOARD_HEIGHT))
            return false;

        List<Cell> shipCells = new ArrayList<>();

        for(int i = 0, j = 0; i < ShipSize.getShipSize(shipType); i++, j++)
            shipCells.add(getCellByPosition(firstCell.getX() + i*(direction?0:1), firstCell.getY() + j*(direction?1:0)));
        if(available(shipCells)) {
            Ship newShip = new Ship(shipType);
            for (Cell cell : shipCells)
                cell.setShip(newShip);
            newShip.setShipCells(shipCells);
            lockCellsForShip(newShip);
            ships.add(newShip);
            return true;
        }
        return false;
    }

    private boolean available(List<Cell> cells){
        for (Cell cell:cells) {
            for(int i = -1; i <= 1; i++){
                for(int j = -1; j <= 1; j++) {
                    if(getCellByPosition(cell.getX() + i, cell.getY() + j) == null)
                        continue;
                    if(getCellByPosition(cell.getX() + i, cell.getY() + j).getState() == CellState.SHIP)
                        return false;
                }
            }
        }
        return true;
    }


    /**
     * Surrounds cells of the SHIP with WATER.
     * @param ship
     */
    private void lockCellsForShip(Ship ship){
        for (Cell cell:ship.getShipCells()) {
            for(int i = -1; i <= 1; i++){
                for(int j = -1; j <= 1; j++) {
                    Cell waterCell = getCellByPosition(cell.getX() + i, cell.getY() + j);
                    if (waterCell!=null && waterCell.getState() != CellState.SHIP)
                        waterCell.setState(CellState.WATER);
                }
            }
        }
    }
    
    public void fillInUnusedCells(){
        for (Cell cell:getBoard()) {
            if(cell.getState() == null)
                cell.setState(CellState.WATER);
        }
    }

    /**
     * Executes shot on the players board.
     *
     * @param cell shot cell
     * @return MISS if cell is WATER, HIT if SHIP was hit, KILL if SHIP was killed
     */
    public PlayerResponse getShot(Cell cell){
        Cell currentCell = getCellByPosition(cell.getX(), cell.getY());
        CellState newCellState = currentCell.updateState();
        if(newCellState == CellState.HIT){
            Ship hitShip = currentCell.getShip();
            return hitShip.isDead()? PlayerResponse.KILL : PlayerResponse.HIT;
        }
        return PlayerResponse.MISS;
    }

}
