package softwerk.battleship.helpers;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Anastacia on 30.06.2018.
 */

/**
 * Predefined size of ship of every type
 */
public class ShipSize {
    private static Map<ShipType, Integer> shipTypeToSize;
    static {
        Map<ShipType, Integer> shipSizeMap = new HashMap<>();
        shipSizeMap.put(ShipType.BATTLESHIP, 4);
        shipSizeMap.put(ShipType.CRUISER, 3);
        shipSizeMap.put(ShipType.DESTROYER, 2);
        shipSizeMap.put(ShipType.SUBMARINE, 1);

        shipTypeToSize = Collections.unmodifiableMap(shipSizeMap);
    }

    public static Integer getShipSize(ShipType type){
        return shipTypeToSize.get(type);
    }
}
