package softwerk.battleship.computer_strategies;

import java.util.Random;

import softwerk.battleship.helpers.Constants;
import softwerk.battleship.models.Cell;

/**
 * Created by Anastacia on 03.07.2018.
 */
public class CenterStrategy implements ComputerStrategy {
    Random randomX = new Random(), randomY = new Random();

    /**
     * Returns a cell with x and y within range [3-7].
     * @return      Cell with position (x , y)
     */
    @Override
    public Cell choseCellToShoot() {
        int x, y;
        x = randomX.nextInt(Constants.BOARD_WIDTH - 4) + 2;
        y = randomY.nextInt(Constants.BOARD_HEIGHT - 4) + 2;
        return new Cell(x,y);
    }
}
