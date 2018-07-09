package softwerk.battleship.helpers;

import android.content.Context;
import android.graphics.Point;
import android.support.v4.content.ContextCompat;
import android.view.Display;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import softwerk.battleship.GameStartActivity;
import softwerk.battleship.R;

/**
 * Created by Anastacia on 05.07.2018.
 */

/**
 * Class works with UI battleship table.
 */
public class Table {
    /**
     * Fills the Table Layout with empty cell views and assigns specific OnClick listener to every cell.
     * Cell view is an empty TextView.
     *
     * @param context activity context needed for cell view creation
     * @param tableLayout layout to be filled with cells
     * @param listener specified listener to be assigned to a cell view
     * @return list of created cell views
     */
    public static List<TextView> setupTable(Context context, TableLayout tableLayout, View.OnClickListener listener) {
        List<TextView> cells = new ArrayList<>();
        int cellSize = getCellSize(context);
        for (int y = 0; y < Constants.BOARD_HEIGHT; y++) {
            TableRow tableRow = new TableRow(context);
            TableRow.LayoutParams rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    cellSize);
            rowParams.topMargin = 2;
            rowParams.leftMargin = 2;

            for(int x = 0; x < Constants.BOARD_WIDTH; x++){
                TextView cellButton = new TextView(context);

                //cell params
                cellButton.setLayoutParams(rowParams);
                cellButton.setWidth(cellSize);
                cellButton.setHeight(cellSize);
                cellButton.setBackgroundColor(ContextCompat.getColor(context, R.color.waterCellColor));

                //cell location (x,y)
                cellButton.setTag(R.string.x_axis, x);
                cellButton.setTag(R.string.y_axis, y);

                cellButton.setOnClickListener(listener);
                tableRow.addView(cellButton);

                cells.add(cellButton);
            }
            tableLayout.addView(tableRow);
        }
        return cells;
    }

    /**
     * Defines a size of cell in pixels according to the device screen size.
     * @param context
     * @return size of cell view in pixels
     */
    private static int getCellSize(Context context){
        Display display = ((GameStartActivity)context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        return (width - ((GameStartActivity) context).getMainLayout().getPaddingLeft()
                - ((GameStartActivity) context).getMainLayout().getPaddingRight())/ Constants.BOARD_WIDTH - 2;
    }

    /**
     * Gets cell view by (x,y) position in the list
     * @param cells list of cell views
     * @param x x axis
     * @param y y axic
     * @return reference to cell view or null if the cell wasn't found
     */
    public static TextView getCellByPosition(List<TextView> cells, int x, int y){
        for (TextView cell: cells) {
            if((int)cell.getTag(R.string.x_axis) == x && (int)cell.getTag(R.string.y_axis) == y)
                return cell;
        }
        return null;
    }
}
