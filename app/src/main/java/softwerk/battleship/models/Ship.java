package softwerk.battleship.models;

import java.util.List;

import softwerk.battleship.helpers.CellState;
import softwerk.battleship.helpers.ShipType;

/**
 * Created by Anastacia on 30.06.2018.
 */
public class Ship {
    private ShipType type;
    private List<Cell> cells;

    public Ship(ShipType type){
        this.type = type;
    }

    public List<Cell> getShipCells(){
        return cells;
    }

    public void setShipCells(List<Cell> cells){
        this.cells = cells;
    }

    public ShipType getShipType(){
        return type;
    }

    public boolean isDead(){
        for (Cell cell: cells) {
            if(cell.getState() == CellState.SHIP)
                return false;
        }
        return true;
    }
}
