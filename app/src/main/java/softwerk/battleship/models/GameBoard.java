package softwerk.battleship.models;

import java.util.ArrayList;
import java.util.List;

import softwerk.battleship.helpers.Constants;

/**
 * Created by Anastacia on 30.06.2018.
 */
public class GameBoard {
    private List<Cell> board;

    public GameBoard(){
        setupBoard();
    }

    /**
     * Fills the board of specified size with empty cells.
     */
    private void setupBoard(){
        board = new ArrayList<>();
        for(int x = 0; x < Constants.BOARD_WIDTH; x++)
            for(int y = 0; y < Constants.BOARD_HEIGHT; y++)
                board.add(new Cell(x, y));
    }

    public Cell getCellByPosition(int x, int y){
        for(Cell cell:board){
            if(cell.getX() == x && cell.getY() == y)
                return cell;
        }
        return null;
    }

    public List<Cell> getBoard(){
        return board;
    }
}
