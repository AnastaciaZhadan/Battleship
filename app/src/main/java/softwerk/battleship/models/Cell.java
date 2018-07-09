package softwerk.battleship.models;

import softwerk.battleship.helpers.CellState;

/**
 * Created by Anastacia on 30.06.2018.
 */
public class Cell {
    private int positionX;
    private int positionY;
    private CellState state; //water, ship, shot - miss, shot - hit
    private Ship ship;

    public Cell(int x, int y){
        positionX = x;
        positionY = y;
    }

    public void setShip(Ship ship){
        this.ship = ship;
        state = CellState.SHIP;
    }

    public Ship getShip(){
        return ship;
    }

    public void setState(CellState state){
        this.state = state;
    }

    public CellState getState(){
        return state;
    }

    public int getX(){
        return positionX;
    }

    public int getY(){
        return positionY;
    }

    /**
     * Switches cell state after shot.
     * If WATER then MISS, if SHIP then HIT
     * @return new cell state
     */
    public CellState updateState(){
        if(state == CellState.WATER)
            state = CellState.MISS;
        else if(state == CellState.SHIP) {
            state = CellState.HIT;
            for(Cell shipCell: ship.getShipCells()){
                if(shipCell.getX() == positionX && shipCell.getY() == positionY) {
                    shipCell.setState(CellState.HIT);
                    break;
                }
            }
        }
        return state;
    }

    public boolean comparePosition(Cell cell){
        if(cell!=null)
            return positionX == cell.getX() && positionY == cell.getY();
        return false;
    }
}
