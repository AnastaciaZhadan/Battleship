package softwerk.battleship.models;

import java.util.ArrayList;
import java.util.List;

import softwerk.battleship.helpers.CellState;
import softwerk.battleship.helpers.Constants;
import softwerk.battleship.helpers.PlayerResponse;

/**
 * Created by Anastacia on 03.07.2018.
 */
public class OpponentBoard extends GameBoard {
    public OpponentBoard(){
        super();
    }

    public Cell updateBoardCell(Cell cell, CellState newState){
        Cell currentCell = getCellByPosition(cell.getX(), cell.getY());
        currentCell.setState(newState);
        return currentCell;
    }

    public boolean isHitShipCell(Cell cell){
        return getCellByPosition(cell.getX(), cell.getY()).getState() == CellState.HIT;
    }

    /**
     * Changes cell states of opponent board according to opponent's response for a hit.
     * If cell was HIT or KILLED, invokes method to change state of neighbor cells, which are definitely not ship.
     *
     * @param x x axis
     * @param y y axis
     * @param response response from a player after shot (MISS, HIT, KILL)
     * @return cells that were updated after shot
     */
    public List<Cell> updateBoard(int x, int y, PlayerResponse response){
        List<Cell> updatedCells = new ArrayList<>();
        Cell cellTpUpdate = new Cell(x, y);
        if(response == PlayerResponse.MISS) {
            updatedCells.add(updateBoardCell(cellTpUpdate, CellState.MISS));
            return updatedCells;
        }
        updatedCells.add(updateBoardCell(cellTpUpdate, CellState.HIT));
        updatedCells.addAll(surroundShipCell(cellTpUpdate));

        if(response == PlayerResponse.KILL){
            updatedCells.addAll(surroundKilledShip(cellTpUpdate, null));
        }
        return updatedCells;
    }

    /**
     * Updates state of cells that can not be ship cells, changes their state to MISS.
     * Diagonal cells are first cells to be changed since ships are not allowed to touch with their corners.
     * If one of the neighbor cells belong to the same ship, left/right ot top/bottom cells can not be ships too.
     * @param cell HIT cell
     * @return updated cells
     */
    private List<Cell> surroundShipCell(Cell cell) {
        List<Cell> updatedCells = new ArrayList<>();
        int x = cell.getX(), y = cell.getY();

        for(int i = -1; i <= 1 && x + i < Constants.BOARD_WIDTH && x + i >= 0; i+=2){
            for(int j = -1; j <= 1 && y + j < Constants.BOARD_HEIGHT && y + j >= 0; j+=2) {
                updatedCells.add(updateBoardCell(new Cell(x + i, y + j), CellState.MISS));
            }
        }

        for(int i = -1, j = -1; i <= 1; i+=2, j+=2){
            if(x + i < Constants.BOARD_WIDTH && x + i >= 0 && isHitShipCell(new Cell(x + i, y))){
                if(y - 1 > 0)
                    updatedCells.add(updateBoardCell(new Cell(x, y - 1), CellState.MISS));
                if(y + 1 < Constants.BOARD_HEIGHT)
                    updatedCells.add(updateBoardCell(new Cell(x , y + 1), CellState.MISS));
                break;
            }

            if(y + j < Constants.BOARD_HEIGHT && y + j >= 0 && isHitShipCell(new Cell(x, y + j))){
                if(x - 1 > 0)
                    updatedCells.add(updateBoardCell(new Cell(x - 1, y), CellState.MISS));
                if(x + 1 < Constants.BOARD_WIDTH)
                    updatedCells.add(updateBoardCell(new Cell(x + 1, y), CellState.MISS));
                break;
            }
        }
        return updatedCells;
    }

    /**
     * Updates state of neighbor cells of killed ship, changes their state to MISS.
     * Changes every neighbor cell of every ship cell. Recursively goes to every cell of the ship.
     *
     * @param cell HIT cell
     * @param parentCell previous HIT cell in order to avoid endless loop
     * @return updated cells
     */
    private List<Cell> surroundKilledShip(Cell cell, Cell parentCell) {
        List<Cell> updatedCells = new ArrayList<>();
        int x = cell.getX(), y = cell.getY();

        for(int i = -1; i <= 1 && x + i < Constants.BOARD_WIDTH && x + i >= 0; i++){
            for (int j = -1; j <= 1 && y + j < Constants.BOARD_HEIGHT && y + j >= 0; j++) {
                Cell checkCell = new Cell(x + i, y + j);
                if(checkCell.comparePosition(parentCell) || checkCell.comparePosition(cell))
                    continue;
                if (!isHitShipCell(checkCell)) {
                    updatedCells.add(updateBoardCell(checkCell, CellState.MISS));
                } else updatedCells.addAll(surroundKilledShip(checkCell, cell));
            }
        }
        return updatedCells;
    }
}
