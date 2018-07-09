package softwerk.battleship.computer_strategies;

import java.util.Random;

import softwerk.battleship.helpers.Constants;
import softwerk.battleship.models.Cell;

/**
 * Created by Anastacia on 03.07.2018.
 */
public class EdgesStrategy implements ComputerStrategy{
    Random randomX = new Random(), randomY = new Random(), randomShift = new Random();

    /**
     * Returns a cell on two top/bottom lines or left/right columns.
     * @return      Cell with position (x , y)
     */
    @Override
    public Cell choseCellToShoot() {
        int x, y, shift;
        x = randomX.nextInt(Constants.BOARD_WIDTH);
        y = randomY.nextInt(Constants.BOARD_HEIGHT);
        shift = randomShift.nextInt(2);
        return x > y? new Cell(x, shift): new Cell(shift, y);
    }
}
