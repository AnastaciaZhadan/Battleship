package softwerk.battleship.models;

import java.util.Random;

import softwerk.battleship.computer_strategies.CenterStrategy;
import softwerk.battleship.computer_strategies.ComputerStrategy;
import softwerk.battleship.computer_strategies.EdgesStrategy;
import softwerk.battleship.helpers.Constants;
import softwerk.battleship.helpers.ShipType;
import softwerk.battleship.helpers.ShipsCount;

/**
 * Created by Anastacia on 04.07.2018.
 */
public class ComputerPlayer extends Player{
    private ComputerStrategy[] strategies = {new CenterStrategy(), new EdgesStrategy()}; // all the strategies available to computer
    private int strategyIndex;
    private ComputerStrategy strategy;
    private Cell lastSuccessfulShot = null;
    private int predefinedStrategiesUsageCount = 4;

    public ComputerPlayer(int playerId) {
        super(playerId);
        strategy = chooseStartStrategy();
    }

    private ComputerStrategy chooseStartStrategy() {
        Random strategyIndexRandom = new Random();
        strategyIndex = strategyIndexRandom.nextInt(2);
        return strategies[strategyIndex];
    }

    public void setLastSuccessfulShot(Cell cell){
        lastSuccessfulShot = cell;
    }

    public void setupBoardRandomly(){
        for(ShipType shipType:ShipType.values()){
            for(int i = 0; i < ShipsCount.getShipsCount(shipType); i++){
                Random randomShipCellX = new Random();
                Random randomShipCellY = new Random();
                Random randomDirection = new Random();

                int direction = randomDirection.nextInt(2);

                while(!placeShip(shipType, randomShipCellX.nextInt(10), randomShipCellY.nextInt(10), direction == 0? false:true));
            }
        }

        getPlayerBoard().fillInUnusedCells();
    }

    /**
     * Choose next cell for shot.
     * If previous shot was successful, but ship wasn't killed, computer will shoot neighbor cells.
     * In other cases, cell is chosen according to strategy.
     * If 10 picked cells are not available for shot, computer changes strategy.
     * Computer is allowed to change strategy for 4 times. After that computer will shoot every empty cell.
     *
     * @return next cell to shoot
     */
    public Cell chooseCellToShoot(){
        Cell cellToShoot = null;
        if(lastSuccessfulShot != null) {
            int x = lastSuccessfulShot.getX(), y = lastSuccessfulShot.getY();

            for (int i = -1; i <= 1; i += 2) {
                if ((x + i >= 0 && x + i < Constants.BOARD_WIDTH) && isAvailableForShot(new Cell(x + i, y))) {
                    cellToShoot = new Cell(x + i, y);
                    break;
                } else if ((y + i >= 0 && y + i < Constants.BOARD_HEIGHT) && isAvailableForShot(new Cell(x, y + i))) {
                    cellToShoot = new Cell(x, y + i);
                    break;
                }
            }
        }

        if(cellToShoot == null) {
            int i = 10;
            cellToShoot = strategy.choseCellToShoot();

            while (i > 0){
                if(!isAvailableForShot(cellToShoot))
                    cellToShoot = strategy.choseCellToShoot();
                else break;
                i--;
            }

            if (i == 0 && !isAvailableForShot(cellToShoot)) {
                if(predefinedStrategiesUsageCount != 0) {
                    changeStrategy();
                    cellToShoot = chooseCellToShoot();
                } else
                    cellToShoot = chooseEmptyCell();
            }
        }
        return cellToShoot;
    }

    private Cell chooseEmptyCell() {
        for(Cell cell:getOpponentBoard().getBoard()){
            if(cell.getState() == null)
                return cell;
        }
        return null;
    }

    private void changeStrategy() {
        if(strategyIndex == 0)
            strategyIndex = 1;
        else strategyIndex = 0;

        predefinedStrategiesUsageCount--;
        strategy = strategies[strategyIndex];
    }

    private boolean isAvailableForShot(Cell cell) {
        Cell boardCell = getOpponentBoard().getCellByPosition(cell.getX(), cell.getY());
        return boardCell.getState() == null;
    }

}
