package softwerk.battleship.fragments;

import android.app.Fragment;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import softwerk.battleship.GameStartActivity;
import softwerk.battleship.R;
import softwerk.battleship.helpers.ShipSize;
import softwerk.battleship.helpers.ShipType;
import softwerk.battleship.helpers.ShipsCount;
import softwerk.battleship.helpers.Table;
import softwerk.battleship.models.Game;
import softwerk.battleship.models.Ship;

public class ShipPlacementFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private TableLayout tableLayout;
    private ImageView horizontalImage;
    private ImageView verticalImage;
    private List<TextView> cells;

    private List<Ship> shipsToPlace = new ArrayList<>();
    private Ship currentShipToPlace;
    private ShipType currentShipType = null;
    private int direction = -1; // ship direction: 0 - horizontal, 1 - vertical, -1 - unset
    private Game game;

    public ShipPlacementFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        game = ((GameStartActivity) getActivity()).getGame();
        fillShipList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ship_placement, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        tableLayout = (TableLayout) getView().findViewById(R.id.battlefieldTableLayout);
        horizontalImage = (ImageView) getView().findViewById(R.id.horizontalImageView);
        verticalImage = (ImageView) getView().findViewById(R.id.verticalImageView);

        horizontalImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentShipType!=null) {
                    Drawable highlight = ContextCompat.getDrawable((GameStartActivity) getActivity(), R.drawable.highlight);
                    verticalImage.setBackground(null);
                    horizontalImage.setBackground(highlight);
                    direction = 0;
                }
            }
        });
        verticalImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentShipType!=null){
                    Drawable highlight = ContextCompat.getDrawable((GameStartActivity) getActivity(), R.drawable.highlight);
                    horizontalImage.setBackground(null);
                    verticalImage.setBackground(highlight);
                    direction = 1;
                }
            }
        });

        currentShipToPlace = shipsToPlace.get(0);
        currentShipType = currentShipToPlace.getShipType();

        setupShipPlacement();
        cells = Table.setupTable(getActivity(), tableLayout, new ShipSetupCellClick());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void fillShipList(){
        for (ShipType shipType: ShipType.values()) {
            for(int i = 0; i < ShipsCount.getShipsCount(shipType); i++)
                shipsToPlace.add(new Ship(shipType));
        }
    }

    private void setupShipPlacement(){
        int id = R.drawable.battleship;
        switch(currentShipType){
            case BATTLESHIP:
                id = R.drawable.battleship;
                break;
            case CRUISER:
                id = R.drawable.cruiser;
                break;
            case DESTROYER:
                id = R.drawable.destroyer;
                break;
            case SUBMARINE:
                id = R.drawable.submarine;
                break;
        }

        if(currentShipType == ShipType.SUBMARINE){
            verticalImage.setVisibility(View.GONE);
            horizontalImage.setImageResource(id);
            horizontalImage.setBackgroundResource(R.drawable.highlight);
        }
        verticalImage.setImageResource(id);
        horizontalImage.setImageResource(id);
    }

    private class ShipSetupCellClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if(shipsToPlace.size() == 0)
                return;
            if(currentShipType!=null){
                if(direction == -1) {
                    Toast.makeText(getActivity(), "Choose direction first", Toast.LENGTH_SHORT).show();
                    return;
                }
                int x = (int) v.getTag(R.string.x_axis), y = (int) v.getTag(R.string.y_axis);
                boolean isPlaced = game.setShip(game.getCurrentPlayer(), currentShipType, x, y, direction == 1? true:false);
                if(!isPlaced) {
                    Toast.makeText(getActivity(), "Can not place ship here", Toast.LENGTH_SHORT).show();
                    return;
                }

                drawShip(x, y);
                shipsToPlace.remove(currentShipToPlace);
                if(shipsToPlace.size()!= 0) {
                    currentShipToPlace = shipsToPlace.get(0);
                    currentShipType = currentShipToPlace.getShipType();
                    setupShipPlacement();
                    return;
                }
                game.getCurrentPlayer().getPlayerBoard().fillInUnusedCells();
                ((GameStartActivity) getActivity()).startGame();
            }
        }
    }

    /**
     * ship type and direction is got from globals
     *
     * @param x y of first cell.
     * @param y y of first cell.
     */
    private void drawShip(int x, int y) {
        for(int i = 0; i < ShipSize.getShipSize(currentShipType); i++){
            Table.getCellByPosition(cells, x + i*(direction == 0? 1:0), y+i*direction).setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.shipCellColor));
        }
    }
}
