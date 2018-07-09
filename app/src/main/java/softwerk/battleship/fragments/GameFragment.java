package softwerk.battleship.fragments;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import softwerk.battleship.GameStartActivity;
import softwerk.battleship.R;
import softwerk.battleship.helpers.CellState;
import softwerk.battleship.helpers.Table;
import softwerk.battleship.models.Cell;
import softwerk.battleship.models.ComputerPlayer;
import softwerk.battleship.models.Game;

public class GameFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private TableLayout tableLayout;
    private List<TextView> cells;
    private TextView[] opponentMessageTextViews;
    private TextView remainingLivesTextView;

    private Game game;
    private boolean turnAllowed = false;
    private String livesMessage = "Lives: %d";

    public GameFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        game = ((GameStartActivity) getActivity()).getGame();
        opponentMessageTextViews = new TextView[2];
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        tableLayout = (TableLayout) getView().findViewById(R.id.gameBattlefieldTableLayout);
        opponentMessageTextViews[0] = (TextView) getView().findViewById(R.id.opponentMessageTextView);
        opponentMessageTextViews[1] = (TextView) getView().findViewById(R.id.opponentMessage2TextView);
        remainingLivesTextView = (TextView) getView().findViewById(R.id.remainingLivesTextView);

        cells = Table.setupTable(getActivity(), tableLayout, new ShootCellClickListener());
        remainingLivesTextView.setText(String.format(livesMessage, game.getCurrentPlayer().getPlayerLives()));
        turnAllowed = true;
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

    private class ShootCellClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if(!turnAllowed){
                Toast.makeText(getActivity(), "Wait for your turn!", Toast.LENGTH_SHORT).show();
                return;
            }

            if(game.isGameOver()){
                Toast.makeText(getActivity(), "The game is over", Toast.LENGTH_SHORT).show();
                return;
            }

            if(v.getTag(R.string.cell_state) == null){
                shoot((int) v.getTag(R.string.x_axis), (int) v.getTag(R.string.y_axis));

                if(game.isGameOver()) {
                    opponentMessageTextViews[0].setText(String.format("Player %d wins!", game.getWinner().get(0).getPlayerId()));
                    opponentMessageTextViews[1].setText("");
                    return;
                }

                if(game.getCurrentPlayer() instanceof ComputerPlayer) {
                    turnAllowed = false;
                    computerShoot();

                    if(game.isGameOver()) {
                        opponentMessageTextViews[0].setText(String.format("Player %d wins!", game.getWinner().get(0).getPlayerId()));
                        opponentMessageTextViews[1].setText("");
                        return;
                    }

                    updateTurn();
                    turnAllowed = true;
                }
            }
        }
    }

    /**
     * Performs shot by human player on opponents board.
     *
     * @param x
     * @param y
     */
    private void shoot(int x, int y){
        List<Cell> result = game.shoot(x, y);
        updateBoard(result);

        if(result.size() == 1 && result.get(0).getState() == CellState.MISS)
            updateTurn();
    }

    private void updateBoard(List<Cell> updatedCells) {
        for (Cell cell:updatedCells) {
            TextView cellToUpdate = Table.getCellByPosition(cells, cell.getX(), cell.getY());
            updateCell(cellToUpdate, cell.getState());
        }
    }

    private void updateCell(TextView cell, CellState state){
        switch(state){
            case HIT:
                cell.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.hitCellColor));
                cell.setText("X");
                cell.setTag(R.string.cell_state, "HIT");
                break;
            case MISS:
                cell.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.missCellColor));
                cell.setText("~");
                cell.setTag(R.string.cell_state, "MISS");
                break;
        }
    }

    /**
     * Switches turns.
     */
    private void updateTurn() {
        int currentPlayerId = game.getCurrentPlayer().getPlayerId();
        int opponentPlayerId = game.getOpponentPlayer().getPlayerId();
        game.setCurrentPlayer(opponentPlayerId);
        game.setOpponentPlayer(currentPlayerId);
    }

    /**
     * Performs computer turn. Method recursively called until computer MISS.
     * @return cells updated during last shot.
     */
    private List<Cell> computerShoot(){
        Cell cellToShoot = ((ComputerPlayer) game.getCurrentPlayer()).chooseCellToShoot();
        int x = cellToShoot.getX(), y = cellToShoot.getY();
        String messageTemplate ="Player %1d %2s at (%3d,%4d)";
        List<Cell> result = game.shoot(x, y);

        if(result.size() == 1 && result.get(0).getState() == CellState.MISS){
            opponentMessageTextViews[0].setText(opponentMessageTextViews[1].getText());
            opponentMessageTextViews[1].setText(String.format(messageTemplate, game.getCurrentPlayer().getPlayerId(), "missed", x, y));
        } else {
            remainingLivesTextView.setText(String.format(livesMessage, game.getOpponentPlayer().getPlayerLives()));
            opponentMessageTextViews[0].setText(opponentMessageTextViews[1].getText());
            opponentMessageTextViews[1].setText(String.format(messageTemplate, game.getCurrentPlayer().getPlayerId(), "hit", x, y));

            result = computerShoot();
        }
        return result;

    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}


