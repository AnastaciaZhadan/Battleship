package softwerk.battleship;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import softwerk.battleship.fragments.GameFragment;
import softwerk.battleship.fragments.ShipPlacementFragment;
import softwerk.battleship.models.Game;

public class GameStartActivity extends AppCompatActivity
        implements ShipPlacementFragment.OnFragmentInteractionListener, GameFragment.OnFragmentInteractionListener {
    private RelativeLayout mainLayout;
    private ShipPlacementFragment shipPlacementFragment;
    private GameFragment gameFragment;
    private Game game = new Game();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_start);
        mainLayout = (RelativeLayout)  findViewById(R.id.mainLayout);
        setupPlayers();
    }

    public RelativeLayout getMainLayout(){
        return mainLayout;
    }

    private void setupPlayers(){
        game.setCurrentPlayer(0);
        startPlacingShips();
    }

    private void startPlacingShips() {
        shipPlacementFragment = new ShipPlacementFragment();
        getFragmentManager().beginTransaction().replace(R.id.centralFrameLayout, shipPlacementFragment).commit();
    }

    public Game getGame(){
        return game;
    }

    public void startGame(){
        game.setOpponentPlayer(1);
        setupPlayingBoard();
    }

    private void setupPlayingBoard(){
        gameFragment = new GameFragment();
        getFragmentManager().beginTransaction().replace(R.id.centralFrameLayout, gameFragment).commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
