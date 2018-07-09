package softwerk.battleship.helpers;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Anastacia on 30.06.2018.
 */

/**
 * Predefined number of ships of every type available for game
 */
public class ShipsCount {
    public static Map<ShipType, Integer> shipTypeToCount;
    static {
        Map<ShipType, Integer> shipCountMap = new HashMap<>();
        shipCountMap.put(ShipType.BATTLESHIP, 1);
        shipCountMap.put(ShipType.CRUISER, 2);
        shipCountMap.put(ShipType.DESTROYER, 3);
        shipCountMap.put(ShipType.SUBMARINE, 4);

        shipTypeToCount = Collections.unmodifiableMap(shipCountMap);
    }

    public static Integer getShipsCount(ShipType type){
        return shipTypeToCount.get(type);
    }
}
